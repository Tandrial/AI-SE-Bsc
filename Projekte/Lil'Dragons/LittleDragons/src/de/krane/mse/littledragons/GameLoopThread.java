package de.krane.mse.littledragons;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

public class GameLoopThread extends Thread {

	static final long FPS = 30;

	private GameView theView;
	private boolean isRunning = false;

	public GameLoopThread(GameView theView) {
		this.theView = theView;
	}

	public void setRunning(boolean run) {
		this.isRunning = run;
	}

	@SuppressLint("WrongCall")
	@Override
	public void run() {
		long TPS = 1000 / FPS;
		long startTime, sleeTime;
		while (isRunning) {
			Canvas theCanvas = null;
			startTime = System.currentTimeMillis();
			try {
				theCanvas = theView.getHolder().lockCanvas();
				synchronized (theView.getHolder()) {
					theView.onDraw(theCanvas);
				}
			} finally {
				if (theCanvas != null) {
					theView.getHolder().unlockCanvasAndPost(theCanvas);
				}
			}
			sleeTime = TPS - (System.currentTimeMillis() - startTime);
			try {
				if (sleeTime > 0) {
					sleep(sleeTime);
				} else
					sleep(10);
			} catch (Exception e) {
			}
		}
	}
}
