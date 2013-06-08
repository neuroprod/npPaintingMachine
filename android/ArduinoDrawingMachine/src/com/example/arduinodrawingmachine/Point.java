package com.example.arduinodrawingmachine;

import java.io.Serializable;

public class Point implements Serializable
{
	float x, y, size;
	Boolean isUP = false;
	public Point(){}
	
	@Override
	public String toString()
	{
		return x + ", " + y;
	}
}
