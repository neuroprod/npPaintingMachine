package com.example.arduinodrawingmachine;


import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity
{

	Button L1;
	Button L2;
	Button L3;
	Model model;
	DrawView drawView;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		ActionBar actionBar = getActionBar();
		actionBar.hide();

		model =Model.getInstance();
		
		
		 drawView = new DrawView(this);
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
		layout.addView(drawView, 0);

		Button settingsButton = (Button) findViewById(R.id.buttonSettings);
		settingsButton.setOnClickListener(goSettings);
		
		Button printButton = (Button) findViewById(R.id.buttonPrint);
		 printButton.setOnClickListener(goPrint);
		

		L1 = (Button) findViewById(R.id.buttonLayer1);
		L1.setOnClickListener(setLayer);
		L2 = (Button) findViewById(R.id.buttonLayer2);
		L2.setOnClickListener(setLayer);
		L3 = (Button) findViewById(R.id.buttonLayer3);
		L3.setOnClickListener(setLayer);

	}

	View.OnClickListener goPrint = new View.OnClickListener()
	{
		public void onClick(View v)
		{
			
			  goPrintSettings();
			
		}

	};
	View.OnClickListener goSettings = new View.OnClickListener()
	{
		public void onClick(View v)
		{
			
			  goSaveLoad();
			
		}

	};
	@Override
	protected void onResume() {
	    super.onResume();
	    drawView.invalidate();
	    // Normal case behavior follows
	}
	public void goPrintSettings()
	{
		 Intent intent = new Intent(this, PrintActivity.class);
		 
		    startActivity(intent);
		
	}
public void goSaveLoad()
{
	 Intent intent = new Intent(this, SaveLoad.class);
	 
	    startActivity(intent);
	
}
	View.OnClickListener setLayer = new View.OnClickListener()
	{
		public void onClick(View v)
		{
			L1.setEnabled(true);
			L2.setEnabled(true);
			L3.setEnabled(true);
			if (v == L1)
			{
				L1.setEnabled(false);
				model.setLayer(1) ;
			} else if (v == L2)
			{
				L2.setEnabled(false);
				model.setLayer(2) ;
			} else if (v == L3)
			{
				L3.setEnabled(false);
				model.setLayer(3) ;
			}

		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
