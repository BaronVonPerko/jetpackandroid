package com.chrisperko.jetpackandroid.gamestatescreens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class TitleScreen {
	
	public TitleScreen(){}
	
	public void draw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		Paint paint = new Paint();
		paint.setTextSize(60);
		paint.setColor(Color.YELLOW);
		canvas.drawText("Touch to Play!", 100, 100, paint);
	}
}
