#ifndef debounce_h
#define debounce_h

#include "Arduino.h"

class Debounce
{
  public:
    Debounce(long dDelay);
    boolean valid();
};

#endif
