package com.chrisperko.jetpackandroid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.chrisperko.jetpackandroid.Sprites.Obstacle;
import com.chrisperko.jetpackandroid.Sprites.ObstacleHandler;
import com.chrisperko.jetpackandroid.gamestatescreens.GameOverScreen;
import com.chrisperko.jetpackandroid.gamestatescreens.PauseScreen;
import com.chrisperko.jetpackandroid.gamestatescreens.TitleScreen;
import com.chrisperko.jetpackandroid.R;

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
	private ObstacleHandler obstacleHandler = new ObstacleHandler();
	private Random random = new Random();
	private int money;
	private TitleScreen titleScreen = new TitleScreen();
	private PauseScreen pauseScreen = new PauseScreen();
	private GameOverScreen gameOverScreen = new GameOverScreen();
	
	private Bitmap playerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
	private Bitmap pauseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pause);
	
	private final int PAUSEWIDTH = 100;
	private final int PAUSEHEIGHT = 110;
	private final int INITGAMESPEED = 8;
	private final int TEXTSIZE = 34;
	private final int OBSTACLESPOTS = 10;
	
	private enum GameState{
		Paused, Running, TitleScreen, GameOver;
	}
	private GameState currentGameState;


	public GamePanel(Context context) {
		super(context);
		GameSpeed = INITGAMESPEED;
		currentGameState = GameState.TitleScreen;
		scorePaint.setColor(Color.WHITE);
		scorePaint.setTextSize(TEXTSIZE);
		getHolder().addCallback(this); // Allow access to the SurfaceHolder
		gameThread = new GameThread(this);
	}
	
	public void draw(Canvas canvas){		
		switch(currentGameState){
		case Running:
			drawGame(canvas);
			break;
		case GameOver:
			gameOverScreen.draw(canvas);
			break;
		case Paused:
			pauseScreen.draw(canvas);
			break;
		case TitleScreen:
			titleScreen.draw(canvas);
			break;
		default:
			break;
		}
	}	
	
	private void drawGame(Canvas canvas) {
		// Clear the screen / prep for drawing new frame
		canvas.drawColor(Color.BLACK); 
		
		// Draw or create the player
		if(player != null){
			boolean allowPlayerMovement = currentGameState == GameState.Running ? true : false;
			player.draw(canvas, allowPlayerMovement);
		}
		else
			player = new Player(playerBitmap);
		
		// Draw all obstacles
		for(Obstacle obstacle : obstacles){
			obstacle.draw(canvas);
		}
		
		canvas.drawText("Score: " + player.getScore(), 0, 30, scorePaint);
		canvas.drawText("testing : " + money, 0, 60, scorePaint);
		pauseBitmap = Bitmap.createScaledBitmap(pauseBitmap, 80, 80, true);
		canvas.drawBitmap(pauseBitmap, Width - PAUSEWIDTH, 10, null);
	}
	
	public void update(Canvas canvas) {
		switch(currentGameState){
		case GameOver:
			break;
		case Paused:
			break;
		case Running:
			updateGame(canvas);
			break;
		case TitleScreen:
			break;
		default:
			break;
		}
	}
	
	private void updateGame(Canvas canvas){
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
			obstacleHandler.generateObstacleRow(obstacles, OBSTACLESPOTS);
		}		
		obstacleHandler.updateObstacles(obstacles);
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
			currentGameState = GameState.GameOver;
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
		
		boolean actionWait = false;
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			
			if(actionWait) return false;
			
			// handle press event on game over and title screen - reset game when pressed
			if(currentGameState == GameState.GameOver || currentGameState == GameState.TitleScreen){
				player = null;
				obstacles = new ArrayList<Obstacle>();
				currentGameState = GameState.Running;
				gameThread.setRunning(true);
				return true;
			}
			
			// unpause
			if(currentGameState == GameState.Paused)
				currentGameState = GameState.Running;
			
			// pause
			if(currentGameState == GameState.Running 
					&& event.getX() > Width - PAUSEWIDTH && event.getY() < PAUSEHEIGHT){
				currentGameState = GameState.Paused;
				actionWait = true;
			}			
			
			// update player direction of flight
			if(currentGameState == GameState.Running)
				player.updateMoveDirection((int)event.getX());			
			
			return true;
		}
		
		if(player==null) return false;
		
		if(event.getAction() == MotionEvent.ACTION_MOVE){			
			player.updateMoveDirection((int)event.getX());
			return true;
			
		}
		
		else if(event.getAction() == MotionEvent.ACTION_UP){			
			player.stopMoving();
			actionWait = false;
			return true;
			
		}
		
		return false;
	}	
	
	
	
	public void setMoney(int money){ this.money = money; }	
	public int getMoney() { return money; }
	
}
