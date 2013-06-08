package com.example.arduinodrawingmachine;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class PrintActivity extends Activity
{

	public static final int PAUZE = 0;
	public static final int UP = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	public static final int RIGHT = 4;
	public static final int MOVEXY = 5;
	public static final int WAIT = 6;
	UsbManager manager;
	PendingIntent mPermissionIntent;
	UsbDevice device;
	UsbDeviceConnection connection;
	private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
	UsbEndpoint epIN = null;
	UsbEndpoint epOUT = null;
	TextView debugText;
	SharedPreferences settings;

	/**
	 * The system's USB service.
	 */
	private UsbManager mUsbManager;

	private TextView mDumpTextView;

	private final ExecutorService mExecutor = Executors
			.newSingleThreadExecutor();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print);

		mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
				ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		registerReceiver(mUsbReceiver, filter);

		settings = getSharedPreferences("printSettings", 0);

		// /////////////////// connect
		debugText = (TextView) findViewById(R.id.debugtext);
		Button connectButton = (Button) findViewById(R.id.connectButton);
		connectButton.setOnClickListener(tryConnect);

		// print buttons

		Button buttonPrint1 = (Button) findViewById(R.id.printButton1);
		buttonPrint1.setOnClickListener(goPrint1);

		Button buttonPrint2 = (Button) findViewById(R.id.printButton2);
		buttonPrint2.setOnClickListener(goPrint2);

		Button buttonPrint3 = (Button) findViewById(R.id.printButton3);
		buttonPrint3.setOnClickListener(goPrint3);
		// //// movebtns

		Button buttonUp = (Button) findViewById(R.id.buttonUp);
		buttonUp.setOnClickListener(goUp);

		Button buttonDown = (Button) findViewById(R.id.buttonDown);
		buttonDown.setOnClickListener(goDown);

		Button buttonLeft = (Button) findViewById(R.id.buttonLeft);
		buttonLeft.setOnClickListener(goLeft);

		Button buttonRight = (Button) findViewById(R.id.buttonRight);
		buttonRight.setOnClickListener(goRight);

		// /////////////////// paint distance
		int minPaintDistance = settings.getInt("minPaintDistance", 0);
		SeekBar seekBarMinPaint = (SeekBar) findViewById(R.id.seekBarMinPaint);
		seekBarMinPaint.setProgress(minPaintDistance);
		seekBarMinPaint.setOnSeekBarChangeListener(setMinPaintChange);
		updateMinPaintDistance();

		int maxPaintDistance = settings.getInt("maxPaintDistance", 0);
		SeekBar seekBarMaxPaint = (SeekBar) findViewById(R.id.seekBarMaxPaint);
		seekBarMaxPaint.setProgress(maxPaintDistance);
		seekBarMaxPaint.setOnSeekBarChangeListener(setMaxPaintChange);
		updateMaxPaintDistance();

		// /////////////////// paint height
		int up = settings.getInt("up", 0);
		upValue = (byte) up;
		SeekBar seekBarUp = (SeekBar) findViewById(R.id.seekBarUp);
		seekBarUp.setProgress(up);
		seekBarUp.setOnSeekBarChangeListener(setUpChange);
		updateUp();

		int downMin = settings.getInt("downMin", 0);
		SeekBar seekBarDownMin = (SeekBar) findViewById(R.id.seekBarDownMin);
		seekBarDownMin.setProgress(downMin);
		seekBarDownMin.setOnSeekBarChangeListener(setDownMinChange);
		updateDownMin();

		int downMax = settings.getInt("downMax", 0);
		SeekBar seekBarDownMax = (SeekBar) findViewById(R.id.seekBarDownMax);
		seekBarDownMax.setProgress(downMax);
		seekBarDownMax.setOnSeekBarChangeListener(setDownMaxChange);
		updateDownMax();

		// speed
		int speed = settings.getInt("speed", 300);
		SeekBar seekBarSpeed = (SeekBar) findViewById(R.id.seekBarSpeed);
		seekBarSpeed.setProgress(speed);
		seekBarSpeed.setOnSeekBarChangeListener(setSpeedChange);
		updateSpeed();

		// test height;
		SeekBar barTestSet = (SeekBar) findViewById(R.id.barTestSet);
		barTestSet.setOnSeekBarChangeListener(testSetHeight);

	}

	@Override
	protected void onPause()
	{
		super.onPause();

	}

	@Override
	protected void onResume()
	{
		super.onResume();

	}

	/*
	 * 
	 * PRINT
	 */

	View.OnClickListener goPrint1 = new View.OnClickListener()
	{
		public void onClick(View v)
		{
			currentLayer = Model.getInstance().layer1;
			isPrinting = true;
			printCount = 0;
			LinearLayout printView = (LinearLayout) findViewById(R.id.printView);

			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) printView
					.getLayoutParams();
			// params.height = 230;
			printView.setLayoutParams(params);
			printNextLine();

		}

	};
	View.OnClickListener goPrint2 = new View.OnClickListener()
	{
		public void onClick(View v)
		{
			currentLayer = Model.getInstance().layer2;
			isPrinting = true;
			printCount = 0;
			LinearLayout printView = (LinearLayout) findViewById(R.id.printView);

			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) printView
					.getLayoutParams();
			// params.height = 230;
			printView.setLayoutParams(params);
			printNextLine();

		}

	};
	View.OnClickListener goPrint3 = new View.OnClickListener()
	{
		public void onClick(View v)
		{
			currentLayer = Model.getInstance().layer3;
			isPrinting = true;
			printCount = 0;
			LinearLayout printView = (LinearLayout) findViewById(R.id.printView);

			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) printView
					.getLayoutParams();
			// params.height = 230;
			printView.setLayoutParams(params);
			printNextLine();

		}

	};
	Layer currentLayer;
	int printCount = 0;
	boolean isPrinting = false;

	int upValue = 0;
	int downMinValue = 0;
	int downMaxValue = 0;

	int maxPaintDistance = 0;
	int minPaintDistance = 0;

	int printState = 0;
	Point previousPoint = null;
	int distance = 0;
	int printScale = 50;
	Point upPoint;
	Point p;

	void printNextLine()
	{
		float fup = (float) upValue / 10000;
		byte servoValueUp = (byte) (fup * 200);

		float fdownMax = (float) downMaxValue / 10000;
		byte servoValueDownMax = (byte) (fdownMax * 200);

		float fdownMin = (float) downMinValue / 10000;
		byte servoValueDownMin = (byte) (fdownMin * 200);

		if (printState == 1)
		{
			// go up and wait
			debug("up go to paint");
			servoValue = servoValueUp;
			command = WAIT;
			setValue1(5000);
			sendBytes();
			printState++;
		} else if (printState == 2)
		{
			// goToPaint
			// debug("go to paint");
			setValue1(0);
			setValue2(0);
			distance = 0;
			command = MOVEXY;
			sendBytes();
			printState++;

		} else if (printState == 3)
		{
			// dop in paint
			// debug("dop in paint");
			servoValue = servoValueDownMax;
			command = WAIT;
			setValue1(5000);
			sendBytes();
			printState++;

		} else if (printState == 4)
		{
			// goUp
			// debug("go up");
			servoValue = servoValueUp;
			command = WAIT;
			setValue1(15000);
			sendBytes();
			printState++;

		} else if (printState == 5)
		{
			// goToLastPos
			// debug("go to new pos");
			setValue1((int) upPoint.x * printScale);
			setValue2((int) upPoint.y * printScale);
			command = MOVEXY;
			servoValue = servoValueUp;
			sendBytes();
			printState++;

		} else if (printState == 6)
		{
			// set correct startValue
			// debug("go down");
			float size = (upPoint.size - 1) / 10;
			float f = fdownMin + (fdownMax - fdownMin) * size;
			servoValue = (byte) (f * 200);
			setValue1(5000);
			command = WAIT;

			sendBytes();
			printState = 0;

		} else if (printState == 100)
		{
			// set correct startValue
			// debug("go up for next");
			servoValue = servoValueUp;
			command = WAIT;
			setValue1(5000);
			sendBytes();
			printState = 5;

		} else if (printState == 0)
		{

			if (printCount < currentLayer.points.size())
			{
				previousPoint = p;
				p = currentLayer.points.get(printCount);

				if (distance > maxPaintDistance * 10)
				{
					// getpaint
					debug("start get paint- midline");
					upPoint = previousPoint;
					printState = 1;
					printCount++;
					printNextLine();
					return;
				}

				if (p.isUP)
				{
					// float f = (float) upValue / 10000;
					// servoValue = (byte) (f * 200);
					upPoint = p;
					if (distance > minPaintDistance * 10)
					{
						// getpaint
						debug("start get paint end line");
						printState = 1;

					} else
					{
						debug("start goUp");
						printState = 100;

					}
					printCount++;
					printNextLine();
					return;

				} else
				{
					float size = (p.size - 1) / 10;
					float f = fdownMin + (fdownMax - fdownMin) * size;
					servoValue = (byte) (f * 200);

				}
				if (previousPoint != null)
				{

					distance += Math.sqrt(Math.pow(p.x - previousPoint.x, 2)
							+ Math.pow(p.y - previousPoint.y, 2))
							* printScale;

				}

				debug("currentDistance: " + distance + " count:" + printCount);
				setValue1((int) p.x * printScale);
				setValue2((int) p.y * printScale);
				previousPoint = p;
				command = MOVEXY;
				sendBytes();
			} else
			{
				debug("printing done");
				setValue1(0);
				setValue2(0);
				servoValue = servoValueUp;
				command = MOVEXY;
				sendBytes();
				isPrinting = false;
			}
			printCount++;
		}
	}

	/*
	 * 
	 * MOVE BUTTONS
	 */
	View.OnClickListener goUp = new View.OnClickListener()
	{
		public void onClick(View v)
		{
			boolean on = ((Switch) findViewById(R.id.switchStepSize))
					.isChecked();
			if (on)
				setValue1(5000);
			else
				setValue1(300);
			command = UP;
			float f = (float) upValue / 10000;
			servoValue = (byte) (f * 200);
			sendBytes();

		}

	};

	View.OnClickListener goDown = new View.OnClickListener()
	{
		public void onClick(View v)
		{
			boolean on = ((Switch) findViewById(R.id.switchStepSize))
					.isChecked();
			if (on)
				setValue1(5000);
			else
				setValue1(300);
			command = DOWN;
			float f = (float) upValue / 10000;
			servoValue = (byte) (f * 200);
			sendBytes();
		}

	};

	View.OnClickListener goLeft = new View.OnClickListener()
	{
		public void onClick(View v)
		{

			boolean on = ((Switch) findViewById(R.id.switchStepSize))
					.isChecked();
			if (on)
				setValue1(5000);
			else
				setValue1(300);

			command = LEFT;
			float f = (float) upValue / 10000;
			servoValue = (byte) (f * 200);
			sendBytes();

		}

	};

	View.OnClickListener goRight = new View.OnClickListener()
	{
		public void onClick(View v)
		{
			boolean on = ((Switch) findViewById(R.id.switchStepSize))
					.isChecked();
			if (on)
				setValue1(5000);
			else
				setValue1(300);
			command = RIGHT;
			float f = (float) upValue / 10000;
			servoValue = (byte) (f * 200);
			sendBytes();

		}

	};

	/*
	 * 
	 * PAINT DISTANCE
	 */
	SeekBar.OnSeekBarChangeListener setMinPaintChange = new OnSeekBarChangeListener()
	{
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser)
		{

		}

		public void onStartTrackingTouch(SeekBar seekBar)
		{

		}

		public void onStopTrackingTouch(SeekBar seekBar)
		{

			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("minPaintDistance", seekBar.getProgress());
			editor.commit();
			updateMinPaintDistance();

		}
	};

	void updateMinPaintDistance()
	{
		minPaintDistance = settings.getInt("minPaintDistance", 0);
		TextView minPaintText = (TextView) findViewById(R.id.minPaintText);
		minPaintText.setText("Min Paint Distance : " + minPaintDistance);
	}

	SeekBar.OnSeekBarChangeListener setMaxPaintChange = new OnSeekBarChangeListener()
	{
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser)
		{

		}

		public void onStartTrackingTouch(SeekBar seekBar)
		{

		}

		public void onStopTrackingTouch(SeekBar seekBar)
		{

			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("maxPaintDistance", seekBar.getProgress());
			editor.commit();
			updateMaxPaintDistance();

		}
	};

	void updateMaxPaintDistance()
	{
		maxPaintDistance = settings.getInt("maxPaintDistance", 0);
		TextView maxPaintText = (TextView) findViewById(R.id.maxPaintText);

		maxPaintText.setText("Max Paint Distance : " + maxPaintDistance);
	}

	/*
	 * 
	 * PAINT HEIGHTS
	 */
	SeekBar.OnSeekBarChangeListener setUpChange = new OnSeekBarChangeListener()
	{
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser)
		{

		}

		public void onStartTrackingTouch(SeekBar seekBar)
		{

		}

		public void onStopTrackingTouch(SeekBar seekBar)
		{

			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("up", seekBar.getProgress());
			editor.commit();
			updateUp();

		}
	};

	void updateUp()
	{
		int up = settings.getInt("up", 0);
		upValue = up;
		TextView upText = (TextView) findViewById(R.id.textViewUp);
		upText.setText("up Value : " + up);
	}

	SeekBar.OnSeekBarChangeListener setDownMinChange = new OnSeekBarChangeListener()
	{
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser)
		{

		}

		public void onStartTrackingTouch(SeekBar seekBar)
		{

		}

		public void onStopTrackingTouch(SeekBar seekBar)
		{

			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("downMin", seekBar.getProgress());
			editor.commit();
			updateDownMin();

		}
	};

	void updateDownMin()
	{
		downMinValue = settings.getInt("downMin", 0);
		TextView downMinText = (TextView) findViewById(R.id.textViewDownMin);
		downMinText.setText("Min Down Value : " + downMinValue);
	}

	SeekBar.OnSeekBarChangeListener setDownMaxChange = new OnSeekBarChangeListener()
	{
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser)
		{

		}

		public void onStartTrackingTouch(SeekBar seekBar)
		{

		}

		public void onStopTrackingTouch(SeekBar seekBar)
		{

			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("downMax", seekBar.getProgress());
			editor.commit();
			updateDownMax();

		}
	};

	void updateDownMax()
	{
		downMaxValue = settings.getInt("downMax", 0);
		TextView downMaxText = (TextView) findViewById(R.id.textViewDownMax);
		downMaxText.setText("Max Down Value : " + downMaxValue);
	}

	SeekBar.OnSeekBarChangeListener setSpeedChange = new OnSeekBarChangeListener()
	{
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser)
		{

		}

		public void onStartTrackingTouch(SeekBar seekBar)
		{

		}

		public void onStopTrackingTouch(SeekBar seekBar)
		{

			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("speed", seekBar.getProgress());
			editor.commit();
			updateSpeed();

		}
	};

	void updateSpeed()
	{
		int speed = settings.getInt("speed", 0);
		speedByte = (byte) (speed / 10);
		TextView speedText = (TextView) findViewById(R.id.textViewSpeed);
		speedText.setText("Speed Value : " + speed);
	}

	// /
	SeekBar.OnSeekBarChangeListener testSetHeight = new OnSeekBarChangeListener()
	{
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser)
		{

		}

		public void onStartTrackingTouch(SeekBar seekBar)
		{
			// float f =(float)seekBar.getProgress()/10000;
		}

		public void onStopTrackingTouch(SeekBar seekBar)
		{
			float f = (float) seekBar.getProgress() / 10000;

			servoValue = (byte) (f * 200);

			TextView textTestSet = (TextView) findViewById(R.id.textTestSet);
			command = PAUZE;
			textTestSet.setText("send val:" + (servoValue & 0xFF));
			sendBytes();

		}
	};
	/*
	 * 
	 * 
	 * SEND STUFF
	 */

	boolean canSend = true;
	byte command = 0;
	byte servoValue;
	byte val1B256 = 3;
	byte val1B1 = 0;
	byte val2B256 = 0;
	byte val2B1 = 0;
	byte speedByte = 20;

	void setValue1(int v)
	{
		val1B256 = (byte) (v / 256);
		val1B1 = (byte) (v % 256);

	}

	void setValue2(int v)
	{
		val2B256 = (byte) (v / 256);
		val2B1 = (byte) (v % 256);

	}

	void sendBytes()
	{
		if (canSend)
		{
			canSend = false;
			debug("send and wait " + command);

			connection.bulkTransfer(epOUT, new byte[] { (byte) command,
					servoValue, val1B256, val1B1, val2B256, val2B1, speedByte,
					0 }, 8, 0);
			new WaitForResult().execute();
		}

	}

	/*
	 * CONNECT STUFF
	 */

	View.OnClickListener tryConnect = new View.OnClickListener()
	{
		public void onClick(View v)
		{
			manager = (UsbManager) getSystemService(Context.USB_SERVICE);

			HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
			Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
			while (deviceIterator.hasNext())
			{
				UsbDevice device = deviceIterator.next();
				debug(device.getDeviceName());
				manager.requestPermission(device, mPermissionIntent);
			}
		}
	};
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver()
	{
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action))
			{
				synchronized (this)
				{
					device = (UsbDevice) intent
							.getParcelableExtra(UsbManager.EXTRA_DEVICE);

					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false))
					{
						if (device != null)
						{
							
							connection = manager.openDevice(device);
							connection.controlTransfer(0x21, 34, 0, 0, null, 0,
									0);
							connection.controlTransfer(0x21, 32, 0, 0,
									new byte[] { (byte) 0x80, 0x25, 0x00, 0x00,
											0x00, 0x00, 0x08 }, 7, 0);

							UsbInterface usbIf = device.getInterface(1);
							for (int i = 0; i < usbIf.getEndpointCount(); i++)
							{
								if (usbIf.getEndpoint(i).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK)
								{
									if (usbIf.getEndpoint(i).getDirection() == UsbConstants.USB_DIR_IN)
										epIN = usbIf.getEndpoint(i);
									else
										epOUT = usbIf.getEndpoint(i);
								}
							}

							if (epIN == null)
							{
								//fail
							}
							
						}
					} else
					{
						//permision denide
					}
				}
			}
		}
	};

	public void setResult(String result)
	{

		debug("ok->" + result);
		canSend = true;
		if (isPrinting)
		{
			printNextLine();

		}
		// if printing, do next;
	}

	private class WaitForResult extends AsyncTask<byte[], Void, String>
	{
		protected String doInBackground(byte[]... params)
		{

			byte[] buffer = new byte[40];
			String s2 = "0";
			int len = 0;
			while (len <= 0)
			{
				len = connection.bulkTransfer(epIN, buffer, 40, 5000);
				s2 = new String(buffer);
				try
				{
					Thread.sleep(5);
				} catch (InterruptedException ie)
				{
				}
			}
			return s2;
		}

		protected void onPostExecute(String result)
		{
			setResult(result);
		}
	}

	public void debug(String s)
	{
		/*
		 * debugText.append(s +"\n");
		 * 
		 * ScrollView sv = (ScrollView) findViewById(R.id.scrollView2);
		 * sv.fullScroll(View.FOCUS_DOWN);
		 */
	}
}