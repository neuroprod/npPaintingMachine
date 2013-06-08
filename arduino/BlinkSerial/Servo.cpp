#include "Arduino.h"
#include "Servo.h"


Servo::Servo (int pin)
{
  pinMode(pin, OUTPUT);
  currentTime=0;
 _pin=pin; 
 isLow =true;
}
void Servo::setValue(unsigned int value)
{
  waitLow = 20000-(value);
  waitHigh = value;
 digitalWrite(_pin, LOW); 
 currentTime=0;
}


void Servo::updateTime(unsigned long timeStep)
{
  currentTime+=timeStep;
  
  if(isLow)
  {
    if(currentTime>=waitLow)
    {
      currentTime=0;
      isLow =false;
      digitalWrite(_pin, HIGH); 
  
    }
  }
  else
  {
    if(currentTime>=  waitHigh)
    {
      currentTime=0;
      isLow =true;
      digitalWrite(_pin, LOW); 
    }
  }
  
}
