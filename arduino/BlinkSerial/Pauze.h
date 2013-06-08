#ifndef Pauze_h
#define Pauze_h

#include "Arduino.h"



class Pauze
{
  public:
      Pauze();
      void setValue(unsigned int value);
      void updateTime(unsigned long timeStep);
  private:
      unsigned long currentTime;
      unsigned int pauzeValue;
   
};

#endif
