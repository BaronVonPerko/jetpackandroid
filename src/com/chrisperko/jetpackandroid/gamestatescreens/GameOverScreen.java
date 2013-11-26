package com.chrisperko.jetpackandroid.gamestatescreens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class GameOverScreen {
	public GameOverScreen(){}
	
	public void draw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		Paint paint = new Paint();
		paint.setTextSize(60);
		paint.setColor(Color.YELLOW);
		canvas.drawText("Game Over!", 100, 100, paint);
	}
}
