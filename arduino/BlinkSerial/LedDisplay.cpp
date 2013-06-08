#include "LedDisplay.h"
  
  
  
LedDisplay::LedDisplay (int l1,int l2, int l3, int s1, int s2, int s3, int s4, int s5, int s6, int s7, int s8)
{
  l1value=15;
  l2value=15 ;
  l3value=15;
  l1pvalue=false ;
  l2pvalue=false ;
  l3pvalue=false ;
  
  count=0;
  
  L1 =l1;
  L2 =l2;
  L3 =l3;
  S1 =s1;
  S2 =s2;
  S3 =s3;
  S4 =s4;
  S5 =s5;
  S6 =s6;
  S7 =s7;
  S8 =s8;
  
  
  pinMode(L1, OUTPUT); 
  pinMode(L2, OUTPUT); 
  pinMode(L3, OUTPUT); 
  pinMode(S1, OUTPUT);
  pinMode(S2, OUTPUT);
  pinMode(S3, OUTPUT);
  pinMode(S4, OUTPUT);
  pinMode(S5, OUTPUT);
  pinMode(S6, OUTPUT);
  pinMode(S7, OUTPUT);
  pinMode(S8, OUTPUT);        

}
void LedDisplay::updateTime(unsigned long timeStep)
{
  currentTime+=timeStep;
  if (currentTime > 1000)
  {
  
      currentTime=0;
      if (count ==0)
      {
           digitalWrite(L1,HIGH);  
           digitalWrite(L2,LOW);  
           digitalWrite(L3,LOW);  
           setValue(l1value,l1pvalue);
           
      }
      else if (count ==1)
      {
           digitalWrite(L1,LOW);  
           digitalWrite(L2,HIGH);  
           digitalWrite(L3,LOW);  
           setValue(l2value,l2pvalue);
      
      } else if (count ==2)
      {
           digitalWrite(L1,LOW);  
           digitalWrite(L2,LOW);  
           digitalWrite(L3,HIGH);  
           setValue(l3value,l3pvalue);
      }
  
  
  
  
  
    count++;
    if (count==3)count=0;
  }
}
void LedDisplay::setDisplay1(int v)
{
  l1value =v;
}
void LedDisplay::setDisplay2(int v)
{
  l2value =v;
}
void LedDisplay::setDisplay3(int v)
{
  l3value =v;
}
void LedDisplay::setPoint1(boolean v)
{
  l1pvalue =v;
}
void LedDisplay::setPoint2(boolean v)
{
  l2pvalue =v;
}
void LedDisplay::setPoint3(boolean v)
{
  l3pvalue =v;
}
void LedDisplay::setValue(int v, boolean p)
{
  //0=VBottemL
  //1=HBottem
  //2=VBottemR
  //3=point
  //4=HMiddel;
  //5=VTopL;
  //6=HTop;
  //7=VTOpRiht
  if(v==0)
  {
    setLeds(1,1,1,p,0,1,1,1);//
  }
  else if(v==1)
  {
    setLeds(0,0,1,p,0,0,0,1);//
  }
   else if(v==2)
  {
    setLeds(1,1,0,p,1,0,1,1);
  }
   else if(v==3)
  {
    setLeds(0,1,1,p,1,0,1,1);
  }
   else if(v==4)
  {
    setLeds(0,0,1,p,1,1,0,1);//
  }
   else if(v==5)
  {
    setLeds(0,1,1,p,1,1,1,0);//
  }
   else if(v==6)
  {
    setLeds(1,1,1,p,1,1,1,0);
  }
   else if(v==7)
  {
    setLeds(0,0,1,p,0,0,1,1);
  }
   else if(v==8)
  {
    setLeds(1,1,1,p,1,1,1,1);
  }
   else if(v==9)
  {
    setLeds(0,1,1,p,1,1,1,1);
  }
   else if(v==10)
  {
    //A
    setLeds(1,0,1,p,1,1,1,1);//
  }
   else if(v==11)
  {
    //b
    setLeds(1,1,1,p,1,1,0,0);
  }
    else if(v==12)
  {
    
    //C
    setLeds(0,1,1,p,0,0,1,1);
  }  
  else if(v==13)
  {
    //d
    setLeds(1,1,1,p,1,0,0,1);
  }  
  else if(v==14)
  {
    //e
    setLeds(1,1,0,p,1,1,1,0);
  } else if(v==15)
  {
    //f
    setLeds(1,0,0,p,1,1,1,0);
  } 
  

}
void LedDisplay::setLeds(boolean s1,boolean s2,boolean s3, boolean s4, boolean s5, boolean s6, boolean s7, boolean s8)
{
   digitalWrite(S1,s1);  
   digitalWrite(S2,s2);  
   digitalWrite(S3,s3);  
   digitalWrite(S4,s4);  
   digitalWrite(S5,s5);  
   digitalWrite(S6,s6);  
   digitalWrite(S7,s7);  
   digitalWrite(S8,s8);  
  
   
}
