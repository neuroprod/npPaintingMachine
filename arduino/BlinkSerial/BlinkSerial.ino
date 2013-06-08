
#include "Servo.h"
#include "Stepper2D.h"
#include "Display.h"
#include "Pauze.h"
#include "LedDisplay.h"
unsigned long timePreviuos;
int byteCount =0;     
byte byteArray[8]; 


boolean dataComplete = false; 
boolean firstByte =true;

int RS =50;
int RW =48;
int E =46;
int DB0 =44;
int DB1 =42;
int DB2 =40;
int DB3 =38;
int DB4 =36;
int DB5 =34;
int DB6 =32;
int DB7 =30;

LedDisplay ledDisplay (53,51,49,47,45,43,41,39,37,35,33);
Display display(RS,RW,E,DB0,DB1,DB2,DB3,DB4,DB5,DB6,DB7);
Servo servo(12);
Stepper2D stepper (3,4,20,21);
Pauze pauze ;

void setup() {                
 
  Serial.begin(9600);

  timePreviuos  =micros();
  servo.setValue(2400);
  
   display.setLine(0);
 display.print("wait....                   a");
 display.setLine(1);
 display.print("                            a");
}


// the loop routine runs over and over again forever:
void loop() {
  
   unsigned long timeCurrent=micros();
   unsigned long timeElapsed=timeCurrent-timePreviuos;
   
   
   timePreviuos =timeCurrent;
  
   servo.updateTime(timeElapsed);
   stepper.updateTime(timeElapsed);
   pauze.updateTime(timeElapsed);
   display.updateTime(timeElapsed);
   ledDisplay.updateTime(timeElapsed);
   //clear the first incoming byte, not sure why i get that one

   // get dhe data
   while(Serial.available())
   {
     byte bin = Serial.read(); 
         ledDisplay.setDisplay1(byteCount);
    display.print(bin,HEX);
    display.print("-");
     if (byteCount==0 && bin==255)
     {
     // i have a stray first byte sometimes (connect)
     
     }
     else{
     
        byteArray[byteCount] = bin;
        byteCount++;
       if (byteCount ==4) display.setLine(1);
        if( byteCount==8) dataComplete = true;
     }
      ledDisplay.setDisplay2(byteCount);
   }
  
  if(dataComplete)
  {
    unsigned int command  = (unsigned int) byteArray[0];
    servo.setValue( (unsigned int) byteArray[1]*5 +1600);
    unsigned int value1 = (unsigned int) byteArray[2]*256 +(unsigned int) byteArray[3];
    unsigned int value2 = (unsigned int) byteArray[4]*256 +(unsigned int) byteArray[5];
    unsigned int speed = (unsigned int) byteArray[6]*10;
    
    display.setLine(0);
    ledDisplay.setDisplay3(command);
    ledDisplay.setPoint3(!ledDisplay.l3pvalue);
    stepper.speed  = speed;
    if (command ==0)
    {
    //pauze
      Serial.write('a');
      //Serial.flush();
    
    
    }
    else if (command==1)
    {
      ///up
      stepper.goUp(value1);
    }
    else if (command==2)
    {
      //down
      stepper.goDown(value1);
    }
    else if (command==3)
    {
    //left
      stepper.goLeft(value1);
    }
    else if (command==4)
    {
    //right
      stepper.goRight(value1);
    }
    else if (command==5)
    {
    //moveto
      stepper.goTo(value1,value2);
    }
      else if (command==6)
    {
      //pauze
        pauze.setValue(value1*100);
     
    }
    byteCount =0;
    dataComplete = false;

  }
   
  
   
    
}


