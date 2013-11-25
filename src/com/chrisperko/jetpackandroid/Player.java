package com.chrisperko.jetpackandroid;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Player {
	
	private int x, y;
	private int speedX, speedY;
	private Direction moveDirection;
	private Bitmap bitmap;
	private int centerX, centerY;
	private boolean isMoving, isFalling;
	private int score;
	
	private final int INITSPEEDX = 12;
	private final int INITSPEEDY = 7;
	
	private enum Direction{
		RIGHT, LEFT, NONE
	}
	
	public Player(Bitmap bitmap){
		x = ((int)GamePanel.Width / 2) - (bitmap.getWidth() / 2);
		y = ((int)GamePanel.Height / 2) - (bitmap.getHeight() / 2);
		speedX = INITSPEEDX;
		speedY = INITSPEEDY;
		score = 0;
		isMoving = false;
		isFalling = false;
		moveDirection = Direction.NONE;
		this.bitmap = bitmap;
		centerX = bitmap.getWidth() / 2;
		centerY = bitmap.getHeight() / 2;
	}	
	
	
	public void updateMoveDirection(int inputX){
		isMoving = true;
		
		if(inputX > GamePanel.Width / 2)
			moveDirection = Direction.RIGHT;
		else
			moveDirection = Direction.LEFT;
	}	
	
	
	public void update(){
		score++;
		if(isFalling)
			y += GamePanel.GameSpeed;
		else
			y -= speedY;
		if(isMoving){
			if(moveDirection == Direction.RIGHT)
				x += speedX;
			else if(moveDirection == Direction.LEFT)
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
		
		if(allowMotion && moveDirection == Direction.RIGHT)
			matrix.setRotate(20);
		else if(allowMotion && moveDirection == Direction.LEFT)
			matrix.setRotate(-20);
		
		if(!isMoving) { matrix.setRotate(0); }

		Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		canvas.drawBitmap(rotatedBitmap, x, y, null);
	}
	
	
	public void stopMoving(){
		isMoving = false;
	}
	
	public void setIsFalling(boolean isFalling){
		this.isFalling = isFalling;
	}
	
	
	public int getX(){ return x; }
	public int getY(){ return y; }
	public int getSpeedX(){ return speedX; }
	public int getSpeedY(){ return speedY; }
	public int getWidth(){ return bitmap.getWidth(); }
	public int getHeight(){ return bitmap.getHeight(); }	
	public int getScore() { return score; }	
	public void setPosition(int x, int y){ this.x = x; this.y = y; }
}
