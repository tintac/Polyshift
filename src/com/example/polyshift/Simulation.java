package com.example.polyshift;

import android.util.Log;

public class Simulation {
	
	final int PLAYGROUND_MAX_X = 16;
	final int PLAYGROUND_MIN_X = 0;
	final int PLAYGROUND_MAX_Y = 8;
	final int PLAYGROUND_MIN_Y = 0;
	
	int touchedX;
	int touchedY;

	public GameObject[][] objects;
	
	public Simulation(GameActivity activity){
		
		this.objects = new GameObject[PLAYGROUND_MAX_X+1][PLAYGROUND_MAX_Y+1];
		
		Player player = new Player();
		addGameObject(player, 0, 4);
		Player player2 = new Player();
		addGameObject(player2, 16, 4);
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
			if((x == touchedX + 1 && y == touchedY) || (x == touchedX + 2 && y == touchedY)){
				moveObject(touchedX, touchedY, "right");
			}
			else if((x == touchedX - 1 && y == touchedY) || (x == touchedX - 2 && y == touchedY)){
				moveObject(touchedX, touchedY, "left");
			}
			else if((y == touchedY + 1 && x == touchedX) || (y == touchedY + 2 && x == touchedX)){
				moveObject(touchedX, touchedY, "up");
			}
			else if((y == touchedY - 1 && x == touchedX) || (y == touchedY - 2 && x == touchedX)){
				moveObject(touchedX, touchedY, "down");
			}
			touchedX = x;
			touchedY = y;
		}
	}
	public void moveObject(int touchedX, int touchedY, String direction){
		boolean collision = false;
		int x = touchedX;
		int y = touchedY;
		
		if(objects[x][y] != null){
			objects[x][y].block_position = new Vector(x,y,0);
			while(collision == false){
				if(direction.equals("right")){
					if(x+1 <= PLAYGROUND_MAX_X && objects[x+1][y] == null){
						objects[x][y].isMovingRight = true;
						objects[x+1][y] = objects[x][y];
						objects[x][y] = null;
						x++;
					}
					else{
						collision = true;
					}
				}
				if(direction.equals("left")){
					if( x-1 >= PLAYGROUND_MIN_X && objects[x-1][y] == null){
						objects[x][y].isMovingLeft = true;
						objects[x-1][y] = objects[x][y];
						objects[x][y] = null;
						x--;
					}
					else{
						collision = true;
					}
				}
				if(direction.equals("up")){
					if(y+1 <= PLAYGROUND_MAX_Y && objects[x][y+1] == null){
						objects[x][y].isMovingUp = true;
						objects[x][y+1] = objects[x][y];
						objects[x][y] = null;
						y++;
					}
					else{
						collision = true;
					}
				}
				if(direction.equals("down")){
					if(y-1 >= PLAYGROUND_MIN_Y && objects[x][y-1] == null){
						objects[x][y].isMovingDown = true;
						objects[x][y-1] = objects[x][y];
						objects[x][y] = null;
						y--;
					}
					else{
						collision = true;
					}
				}
			}
		}
		
	}
	
	public void update(GameActivity activity){
		getTouch(activity);
	}
}
