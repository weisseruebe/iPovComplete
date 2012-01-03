/*
 iPov
 A rotating Pov-Display which receives its data through a serial port.
 
 Andreas Rettig, 2011, Beuth Hochschule für Technik
 */

#include "debounce.h"
#include "face.h"
#include "flup.h"

#define NumLED 16
#define Rows 64

/* Receive states */
#define RECEIVE_INDEX 3
#define RECEIVE_IMAGE 2
#define RECEIVE_OFFSET 1
#define RECEIVE_NONE 0;

int receiveState = RECEIVE_NONE;

float interval = 1000;

long lstDebounceTime = 0;  // the last time the output pin was toggled
long dbounceDelay = 50000;    // the debounce time; increase if the output flickers
int inByte = 0;
int offset = 0;
int timeLineIndex = 0;

volatile boolean start = false;

//Debounce debounce(40);
int receiveCounter = 0;

int* dispBuffer;//[Rows*2];
int** timeline;//[Rows*2];

//int* dispBuffer;//[Rows*2];
//int timeline[5][Rows*2];


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

void setDispBuffer(int* newDispBuffer){
    dispBuffer = newDispBuffer;
}

void setup() {
  Serial.begin(115200);
  for (int i = 0; i< 18; i++){
    pinMode(i, OUTPUT); 
  }
  //dispBuffer = (int*)  malloc(Rows*2*sizeof(int));
  timeline   = (int**) malloc(10*Rows*2*sizeof(int));
  
  //for(int i = 0; i < 1; i++){
  //    timeline[i] = (int *)malloc(2 * Rows * sizeof(int));
  //}

  for (int i=0;i<Rows*2;i++){
     // dispBuffer[i] = 0;
  }
  attachInterrupt(0, on, RISING);
  Serial.println("setup");
  
    setDispBuffer(face);
  
    timeline[0] = face;
    timeline[1] = flup;
    timeline[2] = face;
    timeline[3] = flup;
    timeline[4] = face;
    timeline[5] = flup;
  
}

void receiveImage(){
  if (Serial.available()){
    while(Serial.available())  {
      int inByte = Serial.read();
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
        case 'p':
          receiveState = RECEIVE_INDEX;
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
          dispBuffer[127-receiveCounter] = inByte;
          //timeline[timeLineIndex][127-receiveCounter] = inByte;
          break;
        case RECEIVE_OFFSET:
          offset = inByte;
          break;
        case RECEIVE_INDEX:
          timeLineIndex = inByte;
          dispBuffer = timeline[timeLineIndex];
          break;
        default:
          break;
        }

        receiveCounter--;
      }
    }
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
    for (int i=0;i<Rows*2;i+=2){
      long startTime = micros()-100;
      int index = (i+offset*2) % (Rows*2);
      
      setLeds(dispBuffer[index],0);
      setLeds(dispBuffer[index+1],1);
      
     // setLeds(timeline[timeLineIndex][index],0);
     // setLeds(timeline[timeLineIndex][index+1],1);
       Serial.println("Huch");
      if (start)break;
      delayMicroseconds(oneRow - (micros()-startTime));
    }
  }
}


