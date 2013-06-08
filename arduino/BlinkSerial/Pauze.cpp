#include "Pauze.h"
#include "Arduino.h"


Pauze::Pauze()
{
  pauzeValue =0;
}


void Pauze::setValue(unsigned int value)
{
  pauzeValue =value;
  currentTime=0;
}

void Pauze::updateTime(unsigned long timeStep)
{
  if (pauzeValue==0)return;
  currentTime+=timeStep;
  
  if (pauzeValue<=currentTime)
  {
      pauzeValue=0;
      Serial.write('p');
  
  }

}
