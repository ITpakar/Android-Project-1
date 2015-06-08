package com.tagcash.waalah.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class HexagonImageView extends ImageView {
	public HexagonImageView(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		Drawable drawable = getDrawable();

		if (drawable == null) {
			return;
		}

		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}
		Bitmap b = ((BitmapDrawable) drawable).getBitmap();
		Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

		int w = getWidth(), h = getHeight();

		Bitmap roundBitmap = getRoundedCroppedBitmap(bitmap, w);
		canvas.drawBitmap(roundBitmap, 0, 0, null);

	}

	public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius) {
		Bitmap finalBitmap;
		if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
			finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius,
					false);
		else
			finalBitmap = bitmap;
		Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
				finalBitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, finalBitmap.getWidth(),
				finalBitmap.getHeight());

//		Point point1_draw = new Point(37, 0);
//		Point point2_draw = new Point(0, 18);
//		Point point3_draw = new Point(0, 57);
//		Point point4_draw = new Point(37, 75);
//		Point point5_draw = new Point(74, 57);
//		Point point6_draw = new Point(74, 18);
		
		int x0 = 0, x1 = radius/2, x2 = radius;
		int y0 = 0, y1 = radius/4 - 2, y2 = radius * 3 /4 + 2, y3 = radius;
		
		Point point1_draw = new Point(x1, y0);
		Point point2_draw = new Point(x0, y1);
		Point point3_draw = new Point(x0, y2);
		Point point4_draw = new Point(x1, y3);
		Point point5_draw = new Point(x2, y2);
		Point point6_draw = new Point(x2, y1);

		Path path = new Path();
		path.moveTo(point1_draw.x, point1_draw.y);
		path.lineTo(point2_draw.x, point2_draw.y);
		path.lineTo(point3_draw.x, point3_draw.y);
		path.lineTo(point4_draw.x, point4_draw.y);
		path.lineTo(point5_draw.x, point5_draw.y);
		path.lineTo(point6_draw.x, point6_draw.y);

		path.close();
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.parseColor("#BAB399"));
		canvas.drawPath(path, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(finalBitmap, rect, rect, paint);

		return output;
	}


}
