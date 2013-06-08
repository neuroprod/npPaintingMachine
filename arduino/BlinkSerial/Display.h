#ifndef Dispay_h
#define Dispay_h

#include "Arduino.h"

class Command
{
  public:
  
  void set(boolean rs,boolean rw,boolean db7,boolean db6,boolean db5,boolean db4,boolean db3,boolean db2,boolean db1,boolean db0){
 RS =rs; 
   RW = rw; 
 
   DB7=db7;
   DB6=db6;
   DB5=db5;
   DB4=db4;
   DB3=db3;
   DB2=db2;
   DB1=db1;
   DB0=db0;
   time =100;
}
    unsigned int time;
    
    int RS ;
    int RW ;
    int DB0 ;
    int DB1 ;
    int DB2 ;
    int DB3 ;
    int DB4 ;
    int DB5 ;
    int DB6 ;
    int DB7 ;
    
   
};

class Display  : public Print
{
  public:
    Display (int rs,int rw, int e, int db0, int db1, int db2, int db3, int db4, int db5, int db6, int db7);
    virtual size_t  write(uint8_t);
    
    void updateTime(unsigned long timeStep);
    void addCommand(Command com);
    void setLine(int l);
    Command commands[101];
    int currentCommand;
    int lastCommand;
    int numCommands;
    int waitTime;
   
    boolean isProsesing;
    
    
  private:
    unsigned long currentTime;
    int RS ;
    int RW ;
    int E ;
    int DB0 ;
    int DB1 ;
    int DB2 ;
    int DB3 ;
    int DB4 ;
    int DB5 ;
    int DB6 ;
    int DB7;
   
};

#endif
