/*
 iPov
 A rotating Pov-Display which receives its data through a serial port.
 
 Andreas Rettig, 2011, Beuth Hochschule für Technik
 */


#include "debounce.h"

#define NumLED 16
#define Rows 64
#define RECEIVE_IMAGE 2
#define RECEIVE_OFFSET 1
#define RECEIVE_NONE 0;

int receiveState = RECEIVE_NONE;

int inByte = 0;
volatile long cnt = 0;
volatile long duration = 200;
volatile long lastLoop = 0;
volatile int index = 0;
long lstDebounceTime = 0;  // the last time the output pin was toggled
long dbounceDelay = 50000;    // the debounce time; increase if the output flickers
float interval = 1000;
volatile boolean start = false;
int offset = 0;

Debounce debounce(40);
int receiveCounter = 0;
int dispBuffer[Rows*2];

int h[Rows*2] ={
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  3,224,
  7,248,
  14,28,
  28,6,
  61,150,
  61,155,
  56,11,
  248,11,
  121,155,
  125,158,
  29,134,
  14,12,
  7,248,
  3,224,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0,
  0,0
};

/* Mapping from LED-No to Pin */
int LEDs[] = {
  18,3,4,5,6,7,8,9,17,16,15,14,13,12,11,10};

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

void setDispBuffer(int* newDispBuffer, int num){
  for (int i=0;i<num;i++){
    dispBuffer[i] = newDispBuffer[i];
    // Serial.println(dispBuffer[i]);
  }
}

void setup() {
  Serial.begin(115200);
  for (int i = 0; i< 18; i++){
    pinMode(i, OUTPUT); 
  }
  for (int i=0;i<Rows*2;i++){
    dispBuffer[i] = 0;
  }
  attachInterrupt(0, on, RISING);
  setDispBuffer(h,Rows*2);
}

void receiveImage(){
  if (Serial.available()){
    while(Serial.available())  {
      int inByte = Serial.read();
      if (receiveCounter == 0){
        /* 'd' starts a data package */
        switch (inByte){
        case 'd':
          receiveState = RECEIVE_IMAGE;
          receiveCounter = 127;
          break;
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
        switch(receiveState){
        case  RECEIVE_IMAGE:
          dispBuffer[127-receiveCounter] = inByte;
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
    //delayMicroseconds(oneRow*(offset/10));
    start = false; 
    for (int i=0;i<Rows*2;i+=2){
      long startTime = micros()-100;
      int index = (i+offset*2) % (Rows*2);
       // int index = i;
   
      setLeds(dispBuffer[index],0);
      setLeds(dispBuffer[index+1],1);
      if (start)break;
      delayMicroseconds(oneRow - (micros()-startTime));
    }
  }

}



