#ifndef Morse_h
#define Morse_h

#include "WProgram.h"

class Debounce
{
  public:
    Debounce(long dDelay);
    boolean valid();
};

#endif
