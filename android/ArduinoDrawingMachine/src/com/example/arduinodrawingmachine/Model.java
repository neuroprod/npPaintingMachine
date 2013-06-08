package com.example.arduinodrawingmachine;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.content.Context;

public class Model
{

	Layer layer1 = new Layer();
	Layer layer2 = new Layer();
	Layer layer3 = new Layer();
	Layer currentLayer = layer1;

	public void setLayer(int layer)
	{

		if (layer == 1)
			currentLayer = layer1;
		if (layer == 2)
			currentLayer = layer2;
		if (layer == 3)
			currentLayer = layer3;
	}

	// saveload

	public void saveFile(String fileName, Activity act)
	{
		LayersVO vo = new LayersVO();
		vo.pointsL1 = layer1.points;
		vo.pointsL2 = layer2.points;
		vo.pointsL3 = layer3.points;
		try
		{
			FileOutputStream outputStream;

			outputStream = act.openFileOutput(fileName, Context.MODE_PRIVATE);
			ObjectOutputStream out = new ObjectOutputStream(outputStream);

			out.writeObject(vo);
			outputStream.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void clearDrawing()
	{
		layer1.points.clear();
		layer1.redraw();
		layer2.points.clear();
		layer2.redraw();
		layer3.points.clear();
		layer3.redraw();
	}

	public void loadFile(String fileName, Activity act)
	{
		try
		{
			FileInputStream fis = act.openFileInput(fileName);
			ObjectInputStream is = new ObjectInputStream(fis);
			LayersVO vo = (LayersVO) is.readObject();
			is.close();
			layer1.points = vo.pointsL1;
			layer1.redraw();
			layer2.points = vo.pointsL2;
			layer2.redraw();
			layer3.points = vo.pointsL3;
			layer3.redraw();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// singleton crap
	private static Model instance = null;

	protected Model()
	{
		// Exists only to defeat instantiation.
		layer1.paint.setColor(0xff000000);
		layer2.paint.setColor(0xffff0000);
		layer3.paint.setColor(0xffffFF00);
	}

	public static Model getInstance()
	{
		if (instance == null)
		{
			instance = new Model();
		}
		return instance;
	}
}
