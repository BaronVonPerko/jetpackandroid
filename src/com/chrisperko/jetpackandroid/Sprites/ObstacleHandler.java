package com.chrisperko.jetpackandroid.Sprites;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.chrisperko.jetpackandroid.GamePanel;

public class ObstacleHandler {
	private Random random;
	
	public ObstacleHandler() {
		random = new Random();
	}
	
	public void generateObstacleRow(List<Obstacle> obstacles, int numSpots) {
		int obstacleX = 0;
		int obstacleChance = 6; // Increase to make more obstacles appear
		boolean containsHole = false;
		for(int i=0; i < numSpots; i++){
			if(i == (numSpots - 1) && !containsHole)
				continue;
			if(random.nextInt(numSpots) < obstacleChance){
				Obstacle newObstacle = new Obstacle(obstacleX, numSpots);
				newObstacle.initialize();
				obstacles.add(newObstacle);
			}
			else{ containsHole = true; }
			obstacleX += GamePanel.Width / numSpots;
		}
	}
	
	public void updateObstacles(List<Obstacle> obstacles){
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
}
