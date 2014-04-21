package de.krane.mse.littledragons;

import de.krane.mse.littledragons.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("WrongCall")
public class GameView extends SurfaceView {

	private GameLoopThread theGameLoopThread;
	private SurfaceHolder surfaceHolder;
	private Bitmap bmp;

	private Sprite sprite1, sprite2;

	public GameView(Context context) {
		super(context);
		theGameLoopThread = new GameLoopThread(this);

		surfaceHolder = getHolder();
		surfaceHolder.addCallback(new SurfaceHolder.Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				boolean retry = true;
				theGameLoopThread.setRunning(false);
				while (retry) {
					try {
						theGameLoopThread.join();
						retry = false;
					} catch (InterruptedException e) {
					}
				}
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				theGameLoopThread.setRunning(true);
				theGameLoopThread.start();

			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
			}

		});
		bmp = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);
		sprite1 = new Sprite(this, bmp);
		sprite2 = new Sprite(this, bmp);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.DKGRAY);
		sprite1.onDraw(canvas);
		sprite2.onDraw(canvas);
	}
}
