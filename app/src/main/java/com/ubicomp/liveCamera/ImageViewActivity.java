package com.ubicomp.liveCamera;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageViewActivity extends Activity implements Runnable { 
	public static final String TAG = "ImageViewActivity";
	ImageView mImageview;
	private GestureDetector mGestureDetector;
	String mPicturePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.imageview);
		File mPictureFilePath = new File("storage/emulated/0/DCIM/Camera");
		List<String> paths = new ArrayList<String>();
		File[] files = mPictureFilePath.listFiles();
		for (int i = 0; i < files.length; ++i) {
			paths.add(files[i].getAbsolutePath());
			Log.v(TAG, String.valueOf(paths.size()));
		}
		Bitmap myBitmap = BitmapFactory.decodeFile(paths.get(0));
		mPicturePath = paths.get(0);
		Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 640, 360, true);
		mImageview =  (ImageView) findViewById(R.id.picture);
		mImageview.setImageBitmap(scaled);

		mGestureDetector = new GestureDetector(this);
		mGestureDetector.setBaseListener(new GestureDetector.BaseListener() {
			@Override
			public boolean onGesture(Gesture gesture) {
				if (gesture == Gesture.TAP) {
					Log.v(TAG, "TAP");
					openOptionsMenu();
					return true;
				}
				return false;
			}
		});
	}

	public boolean onGenericMotionEvent(MotionEvent event) {
		if (mGestureDetector != null) {
			return mGestureDetector.onMotionEvent(event);
		}
		return false;
	}

	public void run() {
		Mail m = new Mail(Constant.em, Constant.constant);
//		Pattern emailPattern = Patterns.EMAIL_ADDRESS;
//		Account[] accounts = AccountManager.get(AppService.appService()).getAccounts();
//		for (Account account : accounts) {
//			if (emailPattern.matcher(account.name).matches()) {
//				mEmail = account.name;
//				Log.v(TAG, "mEmail:"+ mEmail);
//			}
//		}
		String[] toArr = {Constant.frame};
		m.setTo(toArr); 
		m.setFrom(Constant.em);
		m.setSubject("Picture taken with google glass");
		m.setBody("to grandma frame");
		try { 
			m.addAttachment(mPicturePath);

			if(m.send()) {
				Toast.makeText(ImageViewActivity.this, "sent", Toast.LENGTH_SHORT).show();
				(new File(mPicturePath)).delete();
			}
			else {
				Toast.makeText(ImageViewActivity.this, "failed", Toast.LENGTH_SHORT).show();
			} 
		} catch(Exception e) { 
			Log.e("MailApp", "Could not send email", e); 
		} 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.imageview, menu);

		return true;
	} 

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.email:
			Toast.makeText(ImageViewActivity.this, "Sending email...", Toast.LENGTH_LONG).show();
			// has to send network activity in the background, not the main thread, or app exception!	
			Thread thread = new Thread(ImageViewActivity.this);
			thread.start();
			return true;
		case R.id.delete:
			(new File(mPicturePath)).delete();
			Toast.makeText(ImageViewActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}    	      

}
