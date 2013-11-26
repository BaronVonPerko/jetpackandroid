package com.chrisperko.androidgameframework;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
	
	private GamePanel gamePanel;
	private SurfaceHolder surfaceHolder;
	private boolean isRunning, isPaused;
	
	public GameThread(GamePanel panel) {
		this.gamePanel = panel;
		surfaceHolder = panel.getHolder();
	}

	public void setRunning(boolean running) {
		this.isRunning = running;
	}
	
	public void togglePaused(){
		this.isPaused = !this.isPaused;
	}
	
	public boolean getIsPaused(){
		return isPaused;
	}
	
	public void toggleRunning() {
		this.isRunning = !isRunning;
	}

	@Override
	public void run() {
		Canvas canvas = null;
		while(isRunning) {			
			canvas = surfaceHolder.lockCanvas();
			if(canvas != null) {
				gamePanel.draw(canvas);
				if(!isPaused) gamePanel.update(canvas);
				
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
}
