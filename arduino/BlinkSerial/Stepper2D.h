#ifndef Stepper2D_h
#define Stepper2D_h

#include "Arduino.h"


#define MOVEX 0
#define MOVEY 1
#define MOVEXY 2
#define MOVENONE 4

class Stepper2D
{
  public:
    Stepper2D (int pinXStep,int pinXDir,int pinYStep,int pinYDir);
    
     void goUp(unsigned long distance);
     void goDown(unsigned long distance);
     void goLeft(unsigned long distance);
     void goRight(unsigned long distance);
     
     void goTo(int x,int y);
     void updateTime(unsigned long timeStep);
     void drawLine(int x1,int y1,int x2,int y2);
     
      unsigned int speed;
  private:
    unsigned long currentTime;
    
    int _pinXStep;
    int _pinXDir;
    int _pinYStep;
    int _pinYDir;
    int moveType;
    unsigned long _distance;
     
     
    int currentX;
    int currentY;
    int xinc1;
    int xinc2;
    int yinc1;
    int yinc2;
    int den ;
    int num ;
    int numadd ;
    int numpixels;
    int x;
    int y;
    int stepX;
    int stepY;
   
};

#endif
