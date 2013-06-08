#ifndef Servo_h
#define Servo_h

#include "Arduino.h"



class Servo
{
  public:
    Servo (int pin);
    void setValue(unsigned int value);
    void updateTime(unsigned long timeStep);
  private:
    unsigned long currentTime;
    int _pin;
    unsigned long waitLow;
    unsigned long waitHigh;
    boolean isLow;
   
};

#endif
