package de.mkrane.mse_blatt08_a13b;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

public class PaintView extends View {

	Bitmap bitmap;
	Matrix matrix = new Matrix();
	public float rot = 0;

	public PaintView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.vector_compass);
	}

	public PaintView(Context context, AttributeSet attrs) {
		super(context, attrs);
		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.vector_compass);
	}

	public PaintView(Context context) {
		super(context);
		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.vector_compass);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// Rotation des Bildes innerhalb der PaintView
		matrix.setTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2);
		matrix.postRotate(rot);
		matrix.postTranslate(getWidth() / 2, getHeight() / 2);
		canvas.drawBitmap(bitmap, matrix, null);

	}
}
