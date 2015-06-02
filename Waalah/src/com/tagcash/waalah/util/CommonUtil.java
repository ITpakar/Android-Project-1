package com.tagcash.waalah.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.FloatMath;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;

public class CommonUtil {

	private static final int DEFAULT_ANIMATE_DURATION = 200;
	private static final int ANIMATION_DURATION = 500;
	// 0%
	private static final float PERCENTAGE_ZEROR = 0f;
	// 100%
	private static final float PERCENTAGE_FULL = 1f;

	public static String getOriginalFilePath(Context context) {
		//String tempDirPath = context.getCacheDir().toString() + "/temp/";
		String tempDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/healthchat/temp/";
		String tempFileName = "original.jpg";

		File tempDir = new File(tempDirPath);
		if (!tempDir.exists())
			tempDir.mkdirs();
		File tempFile = new File(tempDirPath + tempFileName);
		if (!tempFile.exists())
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		return tempDirPath + tempFileName;
	}

	public static String getWaterMarkedFilePath(Context context) {
		//String tempDirPath = context.getCacheDir().toString() + "/temp/";
		String tempDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/healthchat/temp/";
		String tempFileName = "watermarked.jpg";

		File tempDir = new File(tempDirPath);
		if (!tempDir.exists())
			tempDir.mkdirs();
		File tempFile = new File(tempDirPath + tempFileName);
		if (tempFile.exists())
			tempFile.delete();
		return tempDirPath + tempFileName;
	}

	public static String getCachedFilePath(Context context) {
		//String tempDirPath = context.getCacheDir().toString() + "/temp/";
		String tempDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/healthchat/temp/";
		String tempFileName = "cached.jpg";

		File tempDir = new File(tempDirPath);
		if (!tempDir.exists())
			tempDir.mkdirs();
		File tempFile = new File(tempDirPath + tempFileName);
		if (tempFile.exists())
			tempFile.delete();
		return tempDirPath + tempFileName;
	}

	public static String getCameraCaptureFilePath() {
		String tempDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/healthchat/temp/";
		String tempFileName = "cameracapture.jpg";

//		File tempDir = new File(tempDirPath);
//		if (!tempDir.exists())
//			tempDir.mkdirs();
//		
//		if (bDelete) {
//			File tempFile = new File(tempDirPath + tempFileName);
//			if (tempFile.exists()) {
//				try {
//					tempFile.delete();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}

//		try {
//			tempFile.createNewFile();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		return tempDirPath + tempFileName;
	}

	public static String getUploadPicturePath(Context context) {
		String tempDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/healthchat/temp/";
		String tempFileName = "postpicture.jpg";

		File tempDir = new File(tempDirPath);
		if (!tempDir.exists())
			tempDir.mkdirs();
		
		File tempFile = new File(tempDirPath + tempFileName);
		if (tempFile.exists()) {
			try {
				tempFile.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			tempFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tempDirPath + tempFileName;
	}

	/**
	 * Part of BitmapUtil
	 */
	public static Bitmap getSampledBitmap(String filePath, int reqWidth, int reqHeight) {		
		Options options = new Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(filePath, options);

		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = (int)FloatMath.floor(((float)height / reqHeight)+0.5f); //Math.round((float)height / (float)reqHeight);
			} else {
				inSampleSize = (int)FloatMath.floor(((float)width / reqWidth)+0.5f); //Math.round((float)width / (float)reqWidth);
			}
		}

		options.inSampleSize = inSampleSize;
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	public static BitmapSize getBitmapSize(String filePath) {
		Options options = new Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(filePath, options);

		return new BitmapSize(options.outWidth, options.outHeight);
	}

	public static BitmapSize getScaledSize(int originalWidth, int originalHeight, int numPixels) {
		float ratio = (float)originalWidth/originalHeight;

		int scaledHeight = (int)FloatMath.sqrt((float)numPixels/ratio);
		int scaledWidth = (int)(ratio * FloatMath.sqrt((float)numPixels/ratio));

		return new BitmapSize(scaledWidth, scaledHeight);
	}

	public static class BitmapSize {		
		public int width;
		public int height;

		public BitmapSize(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}

	/**
	 * Part of FileUtil
	 */
	public static byte[] readFileToByteArray(File file) throws IOException {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024 * 4];
			int n = 0;
			while (-1 != (n = inputStream.read(buffer))) {
				output.write(buffer, 0, n);
			}
			return output.toByteArray();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				// Do nothing
			}
		}
	}

	/**
	 * Part of MediaUtil
	 */
	public static String getPath(Activity activity, Uri uri) {
		String[] projection = {
				MediaStore.Images.Media.DATA,
		};
		Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
		int pathIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		String path = null;
		if (cursor.moveToFirst()) {
			path = cursor.getString(pathIndex);
		}
		cursor.close();
		return path;
	}
	public static int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filepath);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		if (exif != null) {
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
			if (orientation != -1) {
				// We only recognise a subset of orientation tag values.
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				}
			}
		}
		return degree;
	}

	public static int getImageOrientation(String filePath) {
		try {
			ExifInterface exif = new ExifInterface(filePath);
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
			switch (orientation) {
			case ExifInterface.ORIENTATION_NORMAL:
				return 0;
			case ExifInterface.ORIENTATION_ROTATE_90:
				return 90;
			case ExifInterface.ORIENTATION_ROTATE_180:
				return 180;
			case ExifInterface.ORIENTATION_ROTATE_270:
				return 270;
			default:
				return 0;
			}
		} catch (Exception e) {
			return 0;
		}
	}

	public static Bitmap decodeUri(Context context, Uri selectedImage,int reqSize) throws FileNotFoundException {
		// Decode image size
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, options);

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = options.outWidth, height_tmp = options.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < reqSize
					|| height_tmp / 2 < reqSize) {
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}
		// Decode with inSampleSize
		options = new BitmapFactory.Options();
		options.inSampleSize = scale;
		return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, options);
	}

	public static Bitmap decodeFile(String path,int reqSize) throws FileNotFoundException {
		// Decode image size
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		File file = new File(path);
		FileInputStream stream = new FileInputStream(file);
		BitmapFactory.decodeStream(stream, null, options);
		try {
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Find the correct scale value. It should be the power of 2.
		int width_tmp = options.outWidth, height_tmp = options.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < reqSize
					|| height_tmp / 2 < reqSize) {
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}
		// Decode with inSampleSize
		options = new BitmapFactory.Options();
		options.inSampleSize = scale;
		Bitmap bm = null;

		File file1 = new File(path);
		FileInputStream stream1 = new FileInputStream(file1);
		bm = BitmapFactory.decodeStream(stream1, null, options);
		try {
			stream1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bm;
	}

	public static void hideKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static void animationShowFromLeft(View view) {
		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, -PERCENTAGE_FULL,
				Animation.RELATIVE_TO_SELF, PERCENTAGE_ZEROR,
				Animation.RELATIVE_TO_SELF, PERCENTAGE_ZEROR,
				Animation.RELATIVE_TO_SELF, PERCENTAGE_ZEROR);
		animation.setDuration(DEFAULT_ANIMATE_DURATION);
		view.startAnimation(animation);
	}

	public static void animationShowFromRight(View view) {
		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, PERCENTAGE_FULL,
				Animation.RELATIVE_TO_SELF, PERCENTAGE_ZEROR,
				Animation.RELATIVE_TO_SELF, PERCENTAGE_ZEROR,
				Animation.RELATIVE_TO_SELF, PERCENTAGE_ZEROR);
		animation.setDuration(DEFAULT_ANIMATE_DURATION);
		view.startAnimation(animation);
	}

	public static void animationBottomUp(View view) {
		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, PERCENTAGE_ZEROR,
				Animation.RELATIVE_TO_PARENT, PERCENTAGE_ZEROR,
				Animation.RELATIVE_TO_PARENT, PERCENTAGE_FULL,
				Animation.RELATIVE_TO_PARENT, PERCENTAGE_ZEROR);
		animation.setDuration(ANIMATION_DURATION);
		view.startAnimation(animation);
	}

	public static void animationTopDown(View view) {
		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, PERCENTAGE_ZEROR,
				Animation.RELATIVE_TO_PARENT, PERCENTAGE_ZEROR,
				Animation.RELATIVE_TO_PARENT, PERCENTAGE_ZEROR,
				Animation.RELATIVE_TO_PARENT, PERCENTAGE_FULL);
		animation.setDuration(ANIMATION_DURATION);
		view.startAnimation(animation);
	}

	public static void AppliationFinsh(final int time) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}.execute();
	}

	public static Bitmap getBitmapFromAsset(Context context, String path) {
		AssetManager asset_manager = context.getAssets();
		try {
			Bitmap localBitmap = BitmapFactory.decodeStream(asset_manager.open(path));
			return localBitmap;
		} catch (IOException localIOException) {
		}
		return null;
	}
}
