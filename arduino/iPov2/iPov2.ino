#include <SerialPort.h>

/*
 iPov
 A rotating Pov-Display which receives its data through a serial port.
 Version without delays
 
 Andreas Rettig, 2011, Beuth Hochschule für Technik
 */

#include "face.h"

#define NumLED 16
#define Rows 64

/* Receive states */
#define RECEIVE_IMAGE 2
#define RECEIVE_OFFSET 1
#define RECEIVE_NONE 0;
int receiveState = RECEIVE_NONE;

SerialPort<0, 150, 0> serial;

long interval        = 1000;
long lstDebounceTime = 0;     // the last time the output pin was toggled
long dbounceDelay    = 9000;  // the debounce time in microseconds
long oneRow          = 100;   // Time for one column

int inByte = 0;
int offset = 0;               //Angle offset
int receiveCounter = 0;       //Counter for bytes to be received
int dispBuffer[Rows*2];       //First display buffer
int dispBuffer2[Rows*2];      //Second display buffer

boolean dispToggle = true;   //Toggle flag for the display buffers
boolean newData = false;     //Flag that indicated new buffer data

/* Mapping from LED-No to Pin */
int LEDs[] = {
  18,3,4,5,6,7,8,9,17,16,15,14,13,12,11,10
};

void setup() {
  serial.begin(115200);
  /* Switch the LED pins to output */
  for (int i = 0; i< 19; i++){
    pinMode(i, OUTPUT); 
  }
  /* Initialize with a funny face :-) */
  for (int i=0;i<Rows*2;i++){
    dispBuffer[i]  = face[i];
    dispBuffer2[i] = face[i];
  }
  attachInterrupt(0, isr, RISING);
}

/* Set a single LED */
void setLED(int n, int state){
  if (n<16){
    digitalWrite(LEDs[n],state);
  }
}

/* Set LEDs according to bits */
void setLeds(int bits, int group){
  for (int i=0;i<8;i++){
    setLED(i+group*8,(bits & B10000000 >> i) > 0);
  }
}

/* Receive serial data if available */
void receiveData(){
  while(serial.available())  {
    int inByte = serial.read();
    /* Ready to receive */
    if (receiveCounter == 0){
      switch (inByte){
        /* 'd' starts a data package */
        case 'd':
        receiveState = RECEIVE_IMAGE;
        receiveCounter = 127;
        break;
        /* 'o' sets the offset      */
      case 'o':
        receiveState = RECEIVE_OFFSET;
        receiveCounter = 1;
        break;

      default:
        receiveState = RECEIVE_NONE;
        break;
      }
    } 
    else {
      /* Receive data */
      switch(receiveState){
      case  RECEIVE_IMAGE:
        if (dispToggle){
          dispBuffer2[receiveCounter] = inByte;
        } 
        else {
          dispBuffer[receiveCounter] = inByte;
        }
        if (receiveCounter == 1) {
          newData = true;
        } //receiveCounter wird danach 0
        break;
      case RECEIVE_OFFSET:
        offset = inByte;
        break;
      default:
        break;
      }
      receiveCounter--;
    }
  }
}

/* The interrupt routine */
void isr()
{
  if (micros() - lstDebounceTime > dbounceDelay){
  
    /* Send the loop message */
    serial.print("l");
  
    /* Toggle the buffers if newData arrived */
    if (newData){
      newData = false;
      dispToggle = !dispToggle; 
    }
  
    /* Calculate the time for one turn */
    interval = micros()-lstDebounceTime;
    oneRow = interval / Rows;

    
    /* Save the last trigger time */
    lstDebounceTime = micros();
 
  }
}

/* Main loop */
void loop() {
  receiveData();

  long elapsed = (micros() - lstDebounceTime);
  int index = (elapsed / oneRow + offset) % Rows;
  
  if (dispToggle){
    setLeds(dispBuffer[index*2],1);
    setLeds(dispBuffer[index*2+1],0);
  } else {
    setLeds(dispBuffer2[index*2],1);
    setLeds(dispBuffer2[index*2+1],0);
  }
}

