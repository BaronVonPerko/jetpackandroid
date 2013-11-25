package com.chrisperko.jetpackandroid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.chrisperko.jetpackandroid.Sprites.Obstacle;
import com.example.jetpackandroid.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
	
	public static float Width, Height;
	public static int GameSpeed;
	
	private Player player;
	private GameThread gameThread;
	private Paint scorePaint = new Paint();
	private int timeBetweenObstacles, currentTimeBetweenObstacles = 0;
	private List<Obstacle> obstacles = new ArrayList<Obstacle>();
	private Random random = new Random();
	private int money;
	
	private Bitmap playerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
	private Bitmap pauseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pause);
	
	private final int PAUSEWIDTH = 100;
	private final int PAUSEHEIGHT = 110;
	private final int INITGAMESPEED = 8;
	private final int TEXTSIZE = 34;
	private final int OBSTACLESPOTS = 10;


	public GamePanel(Context context) {
		super(context);
		GameSpeed = INITGAMESPEED;
		scorePaint.setColor(Color.WHITE);
		scorePaint.setTextSize(TEXTSIZE);
		getHolder().addCallback(this); // Allow access to the SurfaceHolder
		gameThread = new GameThread(this);
	}
	
	public void draw(Canvas canvas){		
		
		// Clear the screen / prep for drawing new frame
		canvas.drawColor(Color.BLACK); 
		
		// Draw or create the player
		if(player != null){
			player.draw(canvas, !gameThread.getIsPaused());
		}
		else
			player = new Player(playerBitmap);
		
		// Draw all obstacles
		List<Obstacle> obstaclesToRemove = new ArrayList<Obstacle>();
		for(Obstacle obstacle : obstacles){
			obstacle.draw(canvas);
		}
		
		canvas.drawText("Score: " + player.getScore(), 0, 30, scorePaint);
		canvas.drawText("testing : " + money, 0, 60, scorePaint);
		pauseBitmap = Bitmap.createScaledBitmap(pauseBitmap, 80, 80, true);
		canvas.drawBitmap(pauseBitmap, Width - PAUSEWIDTH, 10, null);
	}
	
	public void update(Canvas canvas){
		timeBetweenObstacles = GameSpeed * 2 + 25;
		currentTimeBetweenObstacles--;
		
		if(player != null)
			player.update();
		else
			player = new Player(playerBitmap);
		
		detectCollisions();
		detectDeath();

		// Generate new obstacles when the timer has expired
		if(currentTimeBetweenObstacles <= 0){ 
			currentTimeBetweenObstacles = timeBetweenObstacles;
			int obstacleX = 0;
			int obstacleChance = 6; // Increase to make more obstacles appear
			boolean containsHole = false;
			for(int i=0; i < OBSTACLESPOTS; i++){
				if(i == (OBSTACLESPOTS - 1) && !containsHole)
					continue;
				if(random.nextInt(OBSTACLESPOTS) < obstacleChance){
					Obstacle newObstacle = new Obstacle(obstacleX, OBSTACLESPOTS);
					newObstacle.initialize();
					obstacles.add(newObstacle);
				}
				else{ containsHole = true; }
				obstacleX += Width / OBSTACLESPOTS;
			}
		}
		
		// Update all obstacles
		List<Obstacle> obstaclesToRemove = new ArrayList<Obstacle>();
		for(Obstacle obstacle : obstacles){
			if(obstacle.getY() > (GamePanel.Height + obstacle.getHeight()))
				obstaclesToRemove.add(obstacle);
			obstacle.update();
		}
		
		// Remove obstacles
		for(Obstacle obstacle : obstaclesToRemove){
			obstacles.remove(obstacle);
		}
	}
	
	public void detectCollisions() {
		for(Obstacle obstacle : obstacles){

			// Ignore obstacles that are below the player
			if(obstacle.getY() > player.getY())
				continue;
			
			// Detect unit collision
			if( player.getX() < (obstacle.getX() + obstacle.getWidth()) && (player.getX() + player.getWidth()) > obstacle.getX()){ 
				if( ( player.getY()) < ( obstacle.getY() + GameSpeed + obstacle.getHeight() ) ){
					player.setIsFalling(true);
					return;
				} 
			}
			
			// Otherwise, allow the player to fly upwards
			else { 
				player.setIsFalling(false);
			}
		}
	}

	public void detectDeath(){
		if(player.getY() > (Height + 10)){
			gameThread.setRunning(false); // TODO show 'game over' and move to main menu (to be created...)
		}
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Width = width;
		Height = height;
	}

	public void surfaceCreated(SurfaceHolder holder) {
		if(!gameThread.isAlive()){
			gameThread = new GameThread(this);
			gameThread.setRunning(true);
			gameThread.start();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		if(gameThread.isAlive()){
			gameThread.setRunning(false);
		}
	}

	public boolean onTouchEvent(MotionEvent event){
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			
			if(gameThread.getIsPaused())
				gameThread.togglePaused();
			
			if(!gameThread.getIsPaused() && event.getX() > Width - PAUSEWIDTH && event.getY() < PAUSEHEIGHT)
				gameThread.togglePaused();
			
			if(!gameThread.getIsPaused())
				player.updateMoveDirection((int)event.getX());			
			
			return true;
		}
		
		else if(event.getAction() == MotionEvent.ACTION_MOVE){
			
			player.updateMoveDirection((int)event.getX());
			return true;
			
		}
		
		else if(event.getAction() == MotionEvent.ACTION_UP){
			
			player.stopMoving();
			return true;
			
		}
		
		return false;
	}	
	
	
	
	public void setMoney(int money){ this.money = money; }	
	public int getMoney() { return money; }
	
}
