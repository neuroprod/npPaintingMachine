#include "Arduino.h"
#include "Stepper2D.h"
Stepper2D::Stepper2D (int pinXStep,int pinXDir,int pinYStep,int pinYDir)
{
   currentX =0;
   currentY =0;
  _pinXStep =pinXStep;
  _pinXDir=pinXDir;
  _pinYStep=pinYStep;
  _pinYDir =pinYDir;
  
   pinMode(_pinXStep, OUTPUT);
   pinMode(_pinXDir, OUTPUT);
   pinMode(_pinYStep, OUTPUT);
   pinMode(_pinYDir, OUTPUT);
   currentTime =0;
   _distance =0;
   moveType =MOVENONE;

}
void Stepper2D::goUp(unsigned long distance)
{
  digitalWrite(_pinYDir, LOW); 
  _distance =distance;
    currentX = currentY =0;
  moveType =MOVEY;
}
void Stepper2D::goDown(unsigned long distance)
{
  digitalWrite(_pinYDir, HIGH); 
  _distance =distance;
    currentX = currentY =0;
  moveType =MOVEY;
}
void Stepper2D::goLeft(unsigned long distance)
{
  digitalWrite(_pinXDir, HIGH); 
  _distance =distance;
    currentX = currentY =0;
  moveType =MOVEX;
}
void Stepper2D::goRight(unsigned long distance)
{
  digitalWrite(_pinXDir, LOW);
  _distance =distance; 
  currentX = currentY =0;
  moveType =MOVEX;
}

void Stepper2D::goTo(int xt, int yt)
{
  moveType =MOVEXY;
  int deltax = abs(xt - currentX);		// The difference between the x's
  int deltay = abs(yt - currentY);
   //currentX+=x2 - x1;
      
     //  currentY+=y2 - y1;
    		// The difference between the y's
   x = currentX;				   	// Start x off at the first pixel
   y =currentY;				   	// Start y off at the first pixel
    
    
    
    if (xt >= currentX)			 	// The x-values are increasing
    {
        xinc1 = 1;
        xinc2 = 1;
    }
    else						  // The x-values are decreasing
    {
        xinc1 = -1;
        xinc2 = -1;
    }
    
    if (yt >= currentY)			 	// The y-values are increasing
    {
        yinc1 = 1;
        yinc2 = 1;
    }
    else						  // The y-values are decreasing
    {
        yinc1 = -1;
        yinc2 = -1;
    }
    
    if (deltax >= deltay)	 	// There is at least one x-value for every y-value
    {
        xinc1 = 0;				  // Don't change the x when numerator >= denominator
        yinc2 = 0;				  // Don't change the y for every iteration
        den = deltax;
        num = deltax / 2;
        numadd = deltay;
        numpixels = deltax;	 	// There are more x-values than y-values
    }
    else						  // There is at least one y-value for every x-value
    {
        xinc2 = 0;				  // Don't change the x for every iteration
        yinc1 = 0;				  // Don't change the y when numerator >= denominator
        den = deltay;
        num = deltay / 2;
        numadd = deltax;
        numpixels = deltay;	 	// There are more y-values than x-values
    }
}





void Stepper2D::updateTime(unsigned long timeStep)
{
  currentTime+=timeStep;
  if(currentTime<=speed)
  {
    return;

  }
  currentTime=0;


  if (moveType == MOVENONE)
  {

  }
  else if (moveType ==MOVEX)
  {
    digitalWrite( _pinXStep, HIGH);  
    delayMicroseconds(1) ;
    digitalWrite( _pinXStep, LOW);  
    _distance--;
    if (_distance==0)
    {
      Serial.write('x');
     // Serial.flush();
      moveType =MOVENONE;
    }

  }
  else if (moveType ==MOVEY)
  {
    
    
    
    digitalWrite( _pinYStep,HIGH);  
    delayMicroseconds(1) ;
    digitalWrite( _pinYStep, LOW);  
    _distance--;
    if (_distance==0)
    {
      Serial.write('y');
     // Serial.flush();
      moveType =MOVENONE;
    }
  }  else if (moveType ==MOVEXY)
  {
    
     
       
       
       stepX = (x-currentX);
       stepY = (y-currentY);
   
       currentX+=stepX;
      
       currentY+=stepY;
       
       if (stepX==-1)
       {
          stepX=1;
          digitalWrite(_pinXDir, HIGH); 
       }
       else if (stepX==1)
       {
          digitalWrite(_pinXDir, LOW); 
       }
       
       if (stepY==-1)
       {
          stepY=1;
          digitalWrite(_pinYDir, LOW); 
       }
       else if (stepY==1)
       {
          digitalWrite(_pinYDir, HIGH  ); 
       }
      
       
       if (stepY==1) digitalWrite(_pinYStep, HIGH); 
       if (stepX==1)  digitalWrite(_pinXStep, HIGH);  
       
       delayMicroseconds(1) ;
       
      if (stepY==1)   digitalWrite(_pinYStep, LOW); 
      if (stepX==1)  digitalWrite(_pinXStep, LOW);  
      
        num += numadd;			 
      if (num >= den)		 	
      {
         num -= den;		   	
         x += xinc1;		   	
         y += yinc1;		   	
       }
       x += xinc2;			 	
       y += yinc2;
       
       numpixels--;
       if (numpixels==0)
       {
           moveType =MOVENONE;
           Serial.write('l');
          // Serial.flush();  
       }
      
  }
  //do stuff

}


