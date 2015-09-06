//package com.ubicomp.liveCamera;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.hardware.Camera;
//import android.os.Bundle;
//import android.os.Environment;
//import android.util.Log;
//import android.view.GestureDetector;
//import android.view.KeyEvent;
//import android.view.MotionEvent;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class ZoomActivity extends Activity implements GestureDetector.OnGestureListener, Camera.OnZoomChangeListener
//{
//	public static final String TAG = "ZoomActivity";
//
//	private SurfaceView mPreview;
//	private SurfaceHolder mPreviewHolder;
//	private Camera mCamera;
//	private boolean mInPreview = false;
//	private boolean mCameraConfigured = false;
//	private TextView mZoomLevelView;
//	private int mZoomTo;
//
//	private GestureDetector mGestureDetector;
//
//	public static final int MEDIA_TYPE_IMAGE = 1;
//	public static final int MEDIA_TYPE_VIDEO = 2;
//
//
//	/** Create a File for saving an image or video */
//	private static File getOutputMediaFile(int type){
//		// To be safe, you should check that the SDCard is mounted
//		// using Environment.getExternalStorageState() before doing this.
//
//		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//				Environment.DIRECTORY_PICTURES), "SmartCamera");
//		// This location works best if you want the created images to be shared
//		// between applications and persist after your app has been uninstalled.
//
//		// Create the storage directory if it does not exist
//		if (! mediaStorageDir.exists()){
//			if (! mediaStorageDir.mkdir()){
//				Log.d("MyCameraApp", "failed to create directory");
//				return null;
//			}
//		}
//
//		// Create a media file name
//		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//		File mediaFile;
//		if (type == MEDIA_TYPE_IMAGE){
//			mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//					"IMG_"+ timeStamp + ".jpg");
//
//
//		} else if(type == MEDIA_TYPE_VIDEO) {
//			mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//					"VID_"+ timeStamp + ".mp4");
//		} else {
//			return null;
//		}
//
//		return mediaFile;
//	}
//
//	// code copied from http://developer.android.com/guide/topics/media/camera.html
//	/** A safe way to get an instance of the Camera object. */
//	public static Camera getCameraInstance(){
//	    Camera c = null;
//	    try {
//	        c = Camera.open(); // attempt to get a Camera instance
//	    }
//	    catch (Exception e){
//	        // Camera is not available (in use or does not exist)
//	    	Log.e(TAG, "Camera is not available:"+e.getLocalizedMessage());
//	    }
//	    return c; // returns null if camera is unavailable
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		Log.v(TAG, "onCreate");
//		super.onCreate(savedInstanceState);
//
//		setContentView(R.layout.zoom);
//
//		// as long as this window is visible to the user, keep the device's screen turned on and bright.
//		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // WORKS ON GLASS!
//
//		mPreview = (SurfaceView)findViewById(R.id.preview);
//		mPreviewHolder = mPreview.getHolder();
//		mPreviewHolder.addCallback(surfaceCallback);
//
//		mZoomLevelView = (TextView)findViewById(R.id.zoomLevel);
//
//		// comment to test the press of Camera button during preview; otherwise long press would take the picture
//		mGestureDetector = new GestureDetector(this, this);
//
//		mCamera = getCameraInstance();
//		if (mCamera != null)
//			startPreview();
//	}
//
//
//
//	private void configPreview(int width, int height)
//	{
//		if ( mCamera != null && mPreviewHolder.getSurface() != null) {
//			try {
//				mCamera.setPreviewDisplay(mPreviewHolder);
//			}
//			catch (IOException e) {
//				Toast.makeText(ZoomActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//			}
//
//			Log.v(TAG, "mCameraConfigured="+(mCameraConfigured?"true":"false"));
//			if ( !mCameraConfigured )
//			{
//				Camera.Parameters parameters = mCamera.getParameters();
//
//				// parameters.setPreviewSize(240, 160);
//				parameters.setPreviewFpsRange(30000, 30000);
//				parameters.setPreviewSize(640, 360);
//
////				//parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
////				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
////				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
////				parameters.setJpegQuality(100);
//
//				mCamera.setParameters(parameters);
//				Log.v(TAG, "setZoomChangeListener");
//				mCamera.setZoomChangeListener(this);
//
//				mCameraConfigured = true;
//			}
//		}
//	}
//
//
//
//	private void startPreview()
//	{
//		if ( mCameraConfigured && mCamera != null ) {
//			mCamera.startPreview();
//			mInPreview = true;
//		}
//	}
//
//
//
//	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
//		public void surfaceCreated( SurfaceHolder holder ) {
//			// nothing
//			Log.v(TAG, "surfaceCreated");
//		}
//
//		public void surfaceChanged( SurfaceHolder holder, int format, int width, int height ) {
//			Log.v(TAG, "surfaceChanged="+width+","+height);
//			configPreview(width, height);
//			startPreview();
//		}
//
//		public void surfaceDestroyed( SurfaceHolder holder ) {
//			Log.v(TAG, "surfaceDestroyed");
//			if (mCamera != null) {
//				mCamera.release();
//				mCamera = null;
//			}
//		}
//	};
//
//	@Override
//	public boolean onGenericMotionEvent(MotionEvent event) {
//		mGestureDetector.onTouchEvent(event);
//		return true;
//	}
//
//	@Override
//	public boolean onDown(MotionEvent e) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean onFling( MotionEvent e1, MotionEvent e2, float velocityX, float velocityY ) {
//		Log.v(TAG,  mCamera==null?"mCamere is null":"mCamera NOT null");
//		if (mCamera==null || mPreviewHolder.getSurface() == null) return true;
//
//		Camera.Parameters parameters = mCamera.getParameters();
//		Log.v(TAG, "parameters.getMaxZoom="+ parameters.getMaxZoom()); // 60 on Glass
//
//		int zoom = parameters.getZoom();
//		Log.v(TAG, "current zoom="+zoom);
//
//		if ( velocityX < 0.0f ) {
//			zoom -= 5;
//			if ( zoom < 0 )
//				zoom = 0;
//		}
//		else if ( velocityX > 0.0f ) {
//			zoom += 5;
//			if ( zoom > parameters.getMaxZoom() )
//				zoom = parameters.getMaxZoom();
//		}
//		mZoomTo = zoom;
//
//		// Applications can call stopSmoothZoom() to stop the zoom earlier. Applications should not call startSmoothZoom
//		// again or change the zoom value before zoom stops, or the app will crash!
//		mCamera.stopSmoothZoom();
//		Log.w(TAG, mCamera.getParameters().isSmoothZoomSupported()?"smoothZoomSupported":"smoothZoom not supported");
//		mCamera.startSmoothZoom(zoom);
//		return false;
//	}
//
//	@Override
//	public void onLongPress(MotionEvent e) {
//
//		Log.v(TAG, "onLongPress");
//		mCamera.takePicture(null, null, mPictureCallback);
//	}
//
//	@Override
//	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//		//Log.d(TAG, "distanceX: " + distanceX + ", distanceY: " + distanceY);
//		return false;
//	}
//
//	@Override
//	public void onShowPress(MotionEvent e) {
//
//	}
//
//	@Override
//	public boolean onSingleTapUp(MotionEvent e) {
//
//		return false;
//	}
//
//	@Override
//	public void onZoomChange(int zoomValue, boolean stopped, Camera camera) {
//		Log.v(TAG, "inside onZoomChange:zoomValue="+zoomValue+",stopped="+(stopped?"true":"false"));
//
//		mZoomLevelView.setText("ZOOM: " + zoomValue);
//	}
//
//
////	Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback(){
////
////		@Override
////		public void onShutter() {
////
////
////		}
////
////	};
////	Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
////		public void onPictureTaken(byte[] data, Camera c) {
////			if(data !=null)
////			{
////
////			}
////		}
////	};
//
//	Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
//		public void onPictureTaken(byte[] data, Camera camera) {
//			// copied from http://developer.android.com/guide/topics/media/camera.html#custom-camera
//			File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
//			if (pictureFile == null){
//				Log.v(TAG, "Error creating media file, check storage permissions: ");
//				return;
//			}
//
//			try {
//				FileOutputStream fos = new FileOutputStream(pictureFile);
//				fos.write(data);
//				fos.close();
//
//				Intent intent = new Intent(AppService.appService(), ImageViewActivity.class);
//				intent.putExtra("picturefilepath", pictureFile);
//				startActivity(intent);
//
//				finish(); // works! (after card inserted to timeline)
//
//			} catch (FileNotFoundException e) {
//				Log.d(TAG, "File not found: " + e.getMessage());
//			} catch (IOException e) {
//				Log.d(TAG, "Error accessing file: " + e.getMessage());
//			}
//		}
//
//	};
//
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		Log.v(TAG,  "onKeyDown");
//		if (keyCode == KeyEvent.KEYCODE_CAMERA) { // for both quick press (image capture) and long press (video capture)
//			Log.v(TAG,  "KEYCODE_CAMERA: "+ (event.isLongPress()?"long press": "not long press"));
//
//			if (event.isLongPress()) // video capture
//				return true; // If you return true from onKeyDown(), your activity consumes the event and the Glass camera
//			// doesn't start. Do this only if there is no way to interrupt your activity's use of the camera (for example,
//			// if you are capturing continuous video).
//
//
//			// Stop the preview and release the camera.
//			// Execute your logic as quickly as possible
//			// so the capture happens quickly.
//
//			if ( mInPreview ) {
//				mCamera.stopPreview();
//
//				mCamera.release();
//				mCamera = null;
//				mInPreview = false;
//			}
//
//			return false;
//
//
//		} else {
//			Log.v(TAG,  "NOT KEYCODE_CAMERA");
//
//			return super.onKeyDown(keyCode, event);
//		}
//	}
//
//
//	@Override
//	public void onResume() {
//		Log.v(TAG, "onResume");
//		super.onResume();
//
//		// Re-acquire the camera and start the preview.
//		if (mCamera == null) {
//			mCamera = getCameraInstance();
//			if (mCamera != null) {
//				Log.v(TAG, "mCamera!=null");
//				configPreview(640, 360);
//				startPreview();
//			}
//			else
//				Log.v(TAG, "mCamera==null");
//		}
//	}
//
//	@Override
//	public void onPause()
//	{
//		Log.v(TAG, "onPause");
//		if ( mInPreview ) {
//			mCamera.stopPreview();
//
//			mCamera.release();
//			mCamera = null;
//			mInPreview = false;
//			mCameraConfigured = false;
//		}
//		super.onPause();
//	}
//
//}