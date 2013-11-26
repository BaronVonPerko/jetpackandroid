package com.chrisperko.jetpackandroid.Sprites;
import com.chrisperko.jetpackandroid.GamePanel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Obstacle {
	private Paint paint = new Paint();
	private Rect rect = new Rect();
	private int width, height, x, y;
	
	private final int HEIGHT = 25;
	private final int SPAWN_Y = -100;
	
	
	public Obstacle(int x, int numObstaclesPerRow){
		height = HEIGHT;
		this.x = x;
		y = SPAWN_Y;
		width = (int)GamePanel.Width / numObstaclesPerRow;
		paint.setColor(Color.GREEN);
	}
	
		
	public void initialize(){
		rect.set(x, y, x + width, y + height);
	}
	
	
	public void update(){
		y += GamePanel.GameSpeed;
		rect.set(x, y, x + width, y + height);
	}
	
	
	public void draw(Canvas canvas){
		canvas.drawRect(rect, paint);
	}
	
	
	public int getX(){ return x; }
	public int getY(){ return y; }
	public int getWidth(){ return width; }
	public int getHeight(){ return height; }
}
