package com.example.polyshift;

import android.util.Log;

public class Simulation {
	
	int touchedX;
	int touchedY;

	public GameObject[][] objects;
	
	public Simulation(GameActivity activity){
		
		this.objects = new GameObject[16][8];
		
		Player player = new Player();
		addGameObject(player, 0, 4);
		Player player2 = new Player();
		addGameObject(player2, 15, 4);
		Polynomio poly = new Polynomio();
		addGameObject(poly, 5, 5);
		Polynomio poly2 = new Polynomio();
		addGameObject(poly2, 7, 3);
		Polynomio poly3 = new Polynomio();
		addGameObject(poly3, 9, 2);
	}
	public void addGameObject(GameObject object, int x, int y){
		this.objects[x][y] = object;
	}
	
	public void getTouch(GameActivity activity){
		if(activity.isTouched()){
			int x = Math.round(activity.getTouchX() / (activity.getViewportWidth() / objects.length));
			int y = Math.round(objects[0].length - (activity.getTouchY() / (activity.getViewportHeight() / objects[0].length)) - 1);
			if(x == touchedX + 1 && y == touchedY){
				moveObject(touchedX, touchedY, x, y);
			}
			else if(x == touchedX - 1 && y == touchedY){
				moveObject(touchedX, touchedY, x, y);
			}
			else if(y == touchedY + 1 && x == touchedX){
				moveObject(touchedX, touchedY, x, y);
			}
			else if(y == touchedY - 1 && x == touchedX){
				moveObject(touchedX, touchedY, x, y);
			}
			touchedX = x;
			touchedY = y;
		}
	}
	public void moveObject(int x, int y, int x_new, int y_new){
		if(objects[x][y] != null){
			objects[x_new][y_new] = objects[x][y];
			objects[x][y] = null;
		}
		
	}
	
	public void update(GameActivity activity){
		getTouch(activity);
	}
}
