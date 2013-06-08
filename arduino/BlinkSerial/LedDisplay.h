#ifndef LedDispay_h
#define LedDispay_h

#include "Arduino.h"
class LedDisplay 
{
  public:
    LedDisplay (int l1,int l2, int l3, int s1, int s2, int s3, int s4, int s5, int s6, int s7, int s8);
    void updateTime(unsigned long timeStep);
    void setDisplay1(int v);
    void setDisplay2(int v);
    void setDisplay3(int v);
    void setPoint1(boolean v);
    void setPoint2(boolean v);
    void setPoint3(boolean v);
    void setValue(int v, boolean p);
    void setLeds(boolean s1,boolean s2,boolean s3, boolean s4, boolean s5, boolean s6, boolean s7, boolean s8);
 
    unsigned long currentTime;
    
    int l1value ;
    int l2value ;
    int l3value ;
    
    boolean l1pvalue ;
    boolean l2pvalue ;
    boolean l3pvalue ;
    
    int count;
    
    int L1 ;
    int L2 ;
    int L3 ;
    int S1 ;
    int S2 ;
    int S3 ;
    int S4 ;
    int S5 ;
    int S6 ;
    int S7 ;
    int S8;
   
};

#endif
