package com.example.jetpackandroid;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Player {
	
	private int x, y;
	private int speedX, speedY;
	private String moveDirection;
	private Bitmap bitmap;
	private int centerX, centerY;
	private boolean isMoving, isFalling;
	private int score;
	
	public Player(Bitmap bitmap){
		x = ((int)GamePanel.Width / 2) - (bitmap.getWidth() / 2);
		y = ((int)GamePanel.Height / 2) - (bitmap.getHeight() / 2);
		speedX = 12;
		speedY = 7;
		score = 0;
		isMoving = false;
		isFalling = false;
		moveDirection = "";
		this.bitmap = bitmap;
		centerX = bitmap.getWidth() / 2;
		centerY = bitmap.getHeight() / 2;
	}
	
	public int getX(){ return x; }
	public int getY(){ return y; }
	public int getSpeedX(){ return speedX; }
	public int getSpeedY(){ return speedY; }
	public int getWidth(){ return bitmap.getWidth(); }
	public int getHeight(){ return bitmap.getHeight(); }	
	public int getScore() { return score; }
	
	public void setPosition(int x, int y){ this.x = x; this.y = y; }
	
	public void updateMoveDirection(int inputX){
		isMoving = true;
		
		if(inputX > GamePanel.Width / 2)
			moveDirection = "right";
		else
			moveDirection = "left";
	}
	
	public void stopMoving(){
		isMoving = false;
	}
	
	public void setIsFalling(boolean isFalling){
		this.isFalling = isFalling;
	}
	
	public void update(){
		score++;
		if(isFalling)
			y += GamePanel.GameSpeed;
		else
			y -= speedY;
		if(isMoving){
			if(moveDirection.equals("right"))
				x += speedX;
			else if(moveDirection.equals("left"))
				x -= speedX;
		}
		
		if(x + bitmap.getWidth() > GamePanel.Width)
			x = (int) (GamePanel.Width - bitmap.getWidth());
		if(x < 0)
			x = 0;
		if(y < 0)
			y = 0;
	}

	public void draw(Canvas canvas, boolean allowMotion){
		
		// rotate the bitmap in the direction it is traveling
		Matrix matrix = new Matrix();
		matrix.reset();
		matrix.preTranslate(-centerX, -centerY);
		
		if(allowMotion && moveDirection.equals("right"))
			matrix.setRotate(20);
		else if(allowMotion && moveDirection.equals("left"))
			matrix.setRotate(-20);
		
		if(!isMoving) { matrix.setRotate(0); }

		Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		canvas.drawBitmap(rotatedBitmap, x, y, null);
	}
}
