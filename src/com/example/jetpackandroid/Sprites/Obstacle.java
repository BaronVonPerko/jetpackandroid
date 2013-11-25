package com.example.jetpackandroid.Sprites;
import com.example.jetpackandroid.GamePanel;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Obstacle {
	private Paint paint = new Paint();
	private Rect rect = new Rect();
	private int width, height, x, y;
	
	
	public Obstacle(int x){
		height = 25;
		this.x = x;
		y = -100;
		width = (int)GamePanel.Width / 10;
		paint.setColor(Color.GREEN);
	}
	
	
	public int getX(){ return x; }
	public int getY(){ return y; }
	public int getWidth(){ return width; }
	public int getHeight(){ return height; }
	
	
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
}
