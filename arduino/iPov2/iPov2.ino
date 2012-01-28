#include <SerialPort.h>

/*
 iPov
 A rotating Pov-Display which receives its data through a serial port.
 Version without delays
 
 Andreas Rettig, 2011, Beuth Hochschule für Technik
 */

#include "debounce.h"
#include "face.h"
#include "flup.h"


#define NumLED 16
#define Rows 64

/* Receive states */
#define RECEIVE_IMAGE 2
#define RECEIVE_OFFSET 1
#define RECEIVE_NONE 0;

int receiveState = RECEIVE_NONE;

float interval = 1000;

long lstDebounceTime = 0;     // the last time the output pin was toggled
long dbounceDelay = 10000;    // the debounce time; increase if the output flickers
int inByte = 0;
int offset = 0;
float oneRow = 100;

SerialPort<0, 150, 0> NewSerial;

int receiveCounter = 0;
int dispBuffer[Rows*2];
int dispBuffer2[Rows*2];

boolean dispToggle = true;
boolean newData = false;

/* Mapping from LED-No to Pin */
int LEDs[] = {
  18,3,4,5,6,7,8,9,17,16,15,14,13,12,11,10
};

void setup() {
  NewSerial.begin(115200);
  for (int i = 0; i< 19; i++){
    pinMode(i, OUTPUT); 
  }
  for (int i=0;i<Rows*2;i++){
    dispBuffer[i]  = face[i];
    dispBuffer2[i] = flup[i];
  }
  attachInterrupt(0, isr, RISING);
  NewSerial.println("setup");
}


void setLED(int n, int state){
  if (n<16){
    digitalWrite(LEDs[n],state);
  }
}

void setLeds(int bits, int group){
  for (int i=0;i<8;i++){
    setLED(i+group*8,(bits & B10000000 >> i) > 0);
  }
}


void receiveData(){

  while(NewSerial.available())  {
    int inByte = NewSerial.read();
    /* Ready to receive */
    if (receiveCounter == 0){
      /* 'd' starts a data package */
      switch (inByte){
      case 'd':
        receiveState = RECEIVE_IMAGE;
        receiveCounter = 127;
        break;
        /* 'o' sets the offset */
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
          dispBuffer2[127-receiveCounter] = inByte;
        } 
        else {
          dispBuffer[127-receiveCounter] = inByte;
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

void isr()
{
  if (micros() -lstDebounceTime > dbounceDelay){
    NewSerial.print("l");
    //Toggle the buffers if newData arrived
    if (newData){
      newData = false;
      dispToggle = !dispToggle; 
    }
    //Calculate the time for one turn
    interval = micros()-lstDebounceTime;
    //Save the last trigger time
    lstDebounceTime = micros();
    oneRow = interval / Rows;
  }
}

void loop() {
  receiveData();

  int i = (micros() - lstDebounceTime) / oneRow;
  int index = (i % Rows * 2 + offset*2) % (Rows*2);
  if (dispToggle){
    setLeds(dispBuffer[index],0);
    setLeds(dispBuffer[index+1],1);
           //NewSerial.println("B1");
  } 
  else {
    setLeds(dispBuffer2[index],0);
    setLeds(dispBuffer2[index+1],1);
           //NewSerial.println("B2");
  }
}

