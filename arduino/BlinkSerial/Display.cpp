 
#include "Display.h"
 
Display::Display (int rs,int rw, int e, int db0, int db1, int db2, int db3, int db4, int db5, int db6, int db7)
{
   RS =rs; 
   RW = rw; 
   E=e;
   DB7=db7;
   DB6=db6;
   DB5=db5;
   DB4=db4;
   DB3=db3;
   DB2=db2;
   DB1=db1;
   DB0=db0;
 
  
  
  
   pinMode(RS, OUTPUT); 
   pinMode(RW, OUTPUT); 
   pinMode(E, OUTPUT); 
   pinMode(DB7, OUTPUT); 
   pinMode(DB6, OUTPUT); 
   pinMode(DB5, OUTPUT); 
   pinMode(DB4, OUTPUT); 
   pinMode(DB3, OUTPUT); 
   pinMode(DB2, OUTPUT); 
   pinMode(DB1, OUTPUT); 
   pinMode(DB0, OUTPUT); 
   
   currentCommand=0;
   numCommands=0;
   lastCommand=0;
   waitTime =50000;
   currentTime=0;
   
   //function
  Command cf;
   cf.time =4500;
   cf.set(0,0,0,0,1,1,1,0,0,0);
   addCommand(cf);
   
     cf.time =150;
   cf.set(0,0,0,0,1,1,1,0,0,0);
   addCommand(cf);
   
     cf.time =150;
   cf.set(0,0,0,0,1,1,1,0,0,0);
   addCommand(cf);
  
    cf.time =100;
   cf.set(0,0,0,0,1,1,1,0,0,0);
   addCommand(cf);
  
  //display cursor
  Command cd;
   cd.time =3000;
   cd.set(0,0,0,0,0,0,1,1,0,0);
   addCommand(cd);
  
   Command cdp;
   cdp.time =100;
   cdp.set(0,0,1,1,0,0,0,0,0,0);
   addCommand(cdp);
  
  //
  
  //clear
  //Command cc;
  // cc.time =3000;
  // cc.set(0,0,0,0,0,0,0,0,0,1);
   //addCommand(cc);

 /* Command home;
   ch.time =3000;
   ch.set(0,0,0,0,0,0,0,0,0,1);
   addCommand(ch);*/
/*   
   
*/
 
 
 
  // addCommand(ct);
 
}
void Display::setLine(int l)
{
   Command cdp;
   cdp.time =100;
   if(l ==0)
   {
     cdp.set(0,0,1,0,0,0,0,0,0,0);
   }  else
   {
     cdp.set(0,0,1,1,0,0,0,0,0,0);
   }
  addCommand(cdp);
}
void Display::addCommand(Command com)
{
  
  commands[lastCommand]=com; 
  lastCommand++;
  numCommands++;
  if(lastCommand==100)lastCommand=0;
 

}
size_t Display::write(uint8_t value) {
  Command ct;
  ct.set(1,0,(value >> 7) & 0x01,(value >> 6) & 0x01,(value >> 5) & 0x01,(value >> 4) & 0x01,(value >> 3) & 0x01,(value >> 2) & 0x01,(value >>1) & 0x01,(value >> 0) & 0x01);
   addCommand(ct);
}
void Display::updateTime(unsigned long timeStep)
{
  if (numCommands>0)
  {
   
     currentTime +=timeStep;
     if(currentTime<waitTime)return;
     
     numCommands--;
     
     Command command = commands[currentCommand];
     digitalWrite(RS,command.RS);
     digitalWrite(RW,command.RW);
     digitalWrite(DB7,command.DB7);
     digitalWrite(DB6,command.DB6);
     
     digitalWrite(DB5,command.DB5);
     digitalWrite(DB4,command.DB4);
     digitalWrite(DB3,command.DB3);
     digitalWrite(DB2,command.DB2);
     digitalWrite(DB1,command.DB1);
     digitalWrite(DB0,command.DB0);
     
  
      digitalWrite(E, HIGH);
      delayMicroseconds(1);    // enable pulse must be >450ns
      digitalWrite(E, LOW);
      currentCommand++;
     if (currentCommand==100)currentCommand=0;
      currentTime=0;
      waitTime = command.time;
    
    
  }
  
}
