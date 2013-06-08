package com.example.arduinodrawingmachine;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class SaveLoad extends Activity
{
	String[] values;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_load);
		Button buttonClear = (Button) findViewById(R.id.buttonClear);
		buttonClear.setOnClickListener(tryClear);
	
		// buttonSave
		Button buttonSave = (Button) findViewById(R.id.buttonSave);
		buttonSave.setOnClickListener(trySave);
		ListView listView = (ListView) findViewById(R.id.listView1);
		 values = fileList ();

		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < values.length; ++i)
		{
			list.add(values[i]);
		}
		final StableArrayAdapter adapter = new StableArrayAdapter(this,
				 android.R.layout.simple_list_item_1, list);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id)
			{
				goLoad(values[position]);
				
			}

		});

	}
	View.OnClickListener tryClear= new View.OnClickListener()
	{
		public void onClick(View v)
		{
			doClear();
		
			
		}

	};
	public void doClear()
	{
		Model.getInstance().clearDrawing();
		
		 Intent intent = new Intent(this,MainActivity.class);
		 
		    startActivity(intent);
	}
	View.OnClickListener trySave = new View.OnClickListener()
	{
		public void onClick(View v)
		{
			goSave();
		}

	};
	public void goLoad(String name)
	{
		Model.getInstance().loadFile(name,this);
		 Intent intent = new Intent(this,MainActivity.class);
		 
		    startActivity(intent);
		
	}
	public void goSave()
	{
		EditText editText = (EditText) findViewById(R.id.editText);
		if (editText.getText().toString().isEmpty())return;
		String filename = editText.getText().toString();
		

		
			Model.getInstance().saveFile(filename,this);
			
			editText.setText("");
			
			ListView listView = (ListView) findViewById(R.id.listView1);
			 values = fileList ();

			final ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < values.length; ++i)
			{
				list.add(values[i]);
			}
			final StableArrayAdapter adapter = new StableArrayAdapter(this,
					 android.R.layout.simple_list_item_1, list);
			listView.setAdapter(adapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.save_load, menu);
		return true;
	}

	private class StableArrayAdapter extends ArrayAdapter<String>
	{

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects)
		{
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i)
			{
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position)
		{
			String item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds()
		{
			return true;
		}

	}

}
