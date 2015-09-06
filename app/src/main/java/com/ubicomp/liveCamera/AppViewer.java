package com.ubicomp.liveCamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AppViewer extends FrameLayout {
    private static final String TAG = "AppViewer";
	private final TextView mTextView;
	private final ImageView mLastImageView;

	public AppViewer(Context context) {
	    this(context, null, 0);
    	Log.v(TAG, "AppViewer constructor");
	}

	public AppViewer(Context context, AttributeSet attrs) {
	    this(context, attrs, 0);
    	Log.v(TAG, "AppViewer constructor 2");
	}
	    	
	public AppViewer(Context context, AttributeSet attrs, int style) {
	    super(context, attrs, style);
    	Log.v(TAG, "AppViewer constructor 3");
	    LayoutInflater.from(context).inflate(R.layout.start, this);

	    mTextView =  (TextView) findViewById(R.id.hello_view);
	    mTextView.setText("Smart\nCamera");

		mLastImageView = (ImageView)findViewById(R.id.LastImageView);
		File picFolder = new File("storage/emulated/0/DCIM/Camera");
		List<String> paths = new ArrayList<String>();
//		File directory = new File("/mnt/sdcard/folder");
		File[] files = picFolder.listFiles();
		for (int i = 0; i < files.length; ++i) {
			paths.add(files[i].getAbsolutePath());
			Log.v(TAG, String.valueOf(paths.size()));
		}
		Bitmap myBitmap = BitmapFactory.decodeFile(paths.get(0));
		Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 640, 360, true);
		mLastImageView.setImageBitmap(scaled);
	}
}
