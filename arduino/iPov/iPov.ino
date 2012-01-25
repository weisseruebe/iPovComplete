#include <SerialPort.h>

/*
 iPov
 A rotating Pov-Display which receives its data through a serial port.
 
 Andreas Rettig, 2011, Beuth Hochschule für Technik
 */

#include "debounce.h"
#include "face.h"
//#include "flup.h"

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
int timeLineIndex = 0;

SerialPort<0, 150, 0> NewSerial;

volatile boolean start = false;

int receiveCounter = 0;

int dispBuffer[Rows*2];


/* Mapping from LED-No to Pin */
int LEDs[] = {
  18,3,4,5,6,7,8,9,17,16,15,14,13,12,11,10
};

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

void setup() {
  NewSerial.begin(115200);
  for (int i = 0; i< 18; i++){
    pinMode(i, OUTPUT); 
  }
  for (int i=0;i<Rows*2;i++){
    dispBuffer[i] = 0;
  }
  attachInterrupt(0, on, RISING);
  NewSerial.println("setup");
}

void receiveImage(){
  //if (NewSerial.available()){
    while(NewSerial.available())  {
     // cli();
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
          dispBuffer[127-receiveCounter] = inByte; // Drehen!
          break;
        case RECEIVE_OFFSET:
          offset = inByte;
          break;
        default:
          break;
        }
        receiveCounter--;
        //if (receiveCounter == 0) NewSerial.print("r");
      }
      //sei();
    //}
  }
}

void on()
{
  if (micros() -lstDebounceTime > dbounceDelay){
    start = true;
    interval = micros()-lstDebounceTime;
    lstDebounceTime = micros();
  }
}

void loop() {
  receiveImage();
  
  float oneRow = interval / Rows;
  if (start){
    start = false; 
    NewSerial.print("l");
    for (int i=0;i<Rows*2;i+=2){
      long startTime = micros();
      int index = (i+offset*2) % (Rows*2);

      if (start)break;
      setLeds(dispBuffer[index],0);
      setLeds(dispBuffer[index+1],1);

      if (start)break;
      //delayMicroseconds(oneRow - (micros()-startTime)-150);
      delayMicroseconds(oneRow);
      
    }
  }


}




