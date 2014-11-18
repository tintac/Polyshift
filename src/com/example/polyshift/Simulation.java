package com.example.polyshift;

import java.util.ArrayList;

public class Simulation {
	
	final int PLAYGROUND_MAX_X = 16;
	final int PLAYGROUND_MIN_X = 0;
	final int PLAYGROUND_MAX_Y = 8;
	final int PLAYGROUND_MIN_Y = 0;
	final static String RIGHT = "right";
	final static String LEFT = "left";
	final static String UP = "up";
	final static String DOWN = "down";
	
	public boolean hasWinner =  false;
	public Player winner;
	
	ArrayList<Polynomio>polynomios = new ArrayList<Polynomio>();
	
	public GameObject[][] objects = new GameObject[PLAYGROUND_MAX_X+1][PLAYGROUND_MAX_Y+1];
	int touchedX;
	int touchedY;

	public Object lastMovedObject;
	
	public Simulation(GameActivity activity){
		populate();
	}
	
	public void populate(){
		
		ArrayList<int[]>directions = new ArrayList<int[]>();
		
		this.objects = new GameObject[PLAYGROUND_MAX_X+1][PLAYGROUND_MAX_Y+1];

		directions.add(new int[]{1,1,1,0}); //0 Horizontaler Strich positive X-Achse
		directions.add(new int[]{2,2,2,0}); //1 Horizontaler Strich negative X-Achse//
		directions.add(new int[]{3,3,3,0}); //2 Vertikaler Strich positive Y-Achse
		directions.add(new int[]{4,4,4,0}); //3 Vertikaler Strich negative Y-Achse
		directions.add(new int[]{1,1,3,0}); //4 Liegendes L nach Oben zeigend
		directions.add(new int[]{1,1,4,0}); //5 Liegendes L nach unten zeigend
		directions.add(new int[]{3,3,2,0}); //6 Stehendes L nach Oben zeigend
		directions.add(new int[]{4,4,1,0}); //7 Stehendes L nach unten zeigend
		directions.add(new int[]{1,3,1,0}); //8 S-Form liegend
		directions.add(new int[]{3,1,3,0}); //9 S-Form stehend - rechtsknick
		directions.add(new int[]{3,2,3,0}); //10 S-Form stehend - linksknick
		directions.add(new int[]{1,3,4,1}); //11 Dreieck liegend - nach oben zeigend
		directions.add(new int[]{1,4,3,1}); //12 Dreieck liegend - nach unten zeigend
		directions.add(new int[]{3,2,1,3}); //13 Dreieck stehend - nach links zeigend
		directions.add(new int[]{3,1,2,3}); //14 Dreieck stehend - nach rechts zeigend
		directions.add(new int[]{1,3,2,0}); //15 Quader
		
		Player player = new Player(true);
		setGameObject(player, PLAYGROUND_MIN_X, PLAYGROUND_MAX_Y/2);
		Player player2 = new Player(false);

		setGameObject(player2, PLAYGROUND_MAX_X,PLAYGROUND_MAX_Y/2);

		polynomios.add(newPolynomio(directions.get(11),7,0));
		polynomios.add(newPolynomio(directions.get(2),7,1));
		polynomios.add(newPolynomio(directions.get(15),9,1));
		polynomios.add(newPolynomio(directions.get(9),8,2));
		polynomios.add(newPolynomio(directions.get(6),10,3));
		polynomios.add(newPolynomio(directions.get(10),8,4));
		polynomios.add(newPolynomio(directions.get(13),8,6));
		polynomios.add(newPolynomio(directions.get(7),9,8));
		
		for(int i = 0;i<polynomios.size();i++){
			Polynomio polynomio = polynomios.get(i);
			for(int j = 0; j< polynomio.blocks.size();j++){
				Block block = polynomio.blocks.get(j);
				setGameObject(polynomio, block.x, block.y);
			}
		}
		
	}
		
	public Polynomio newPolynomio(int[] direction, int startX, int startY){
		return (new Polynomio(direction, 4, startX, startY));

	}
	
	public void setGameObject(GameObject object, int x, int y){
		objects[x][y] = object;
	}
	
	public void getTouch(GameActivity activity){
		if(activity.isTouched()){
			int x = Math.round(activity.getTouchX() / (activity.getViewportWidth() / objects.length));
			int y = Math.round(objects[0].length - (activity.getTouchY() / (activity.getViewportHeight() / objects[0].length)) - 1);
			if(x > touchedX && y == touchedY && objects[x][y] == null){
				if(objects[touchedX][touchedY] instanceof Player){
					movePlayer(touchedX, touchedY, RIGHT);
				}
				if(objects[touchedX][touchedY] instanceof Polynomio){
					movePolynomio(touchedX, touchedY, RIGHT);
				}
			}
			else if(x < touchedX && y == touchedY && objects[x][y] == null){
				if(objects[touchedX][touchedY] instanceof Player){
					movePlayer(touchedX, touchedY, LEFT);
				}
				if(objects[touchedX][touchedY] instanceof Polynomio){
					movePolynomio(touchedX, touchedY, LEFT);
				}
			}
			else if(y > touchedY && x == touchedX && objects[x][y] == null){
				if(objects[touchedX][touchedY] instanceof Player){
					movePlayer(touchedX, touchedY, UP);
				}
				if(objects[touchedX][touchedY] instanceof Polynomio){
					movePolynomio(touchedX, touchedY, UP);
				}
			}
			else if(y < touchedY && x == touchedX && objects[x][y] == null){
				if(objects[touchedX][touchedY] instanceof Player){
					movePlayer(touchedX, touchedY, DOWN);
				}
				if(objects[touchedX][touchedY] instanceof Polynomio){
					movePolynomio(touchedX, touchedY, DOWN);
				}
			}
			touchedX = x;
			touchedY = y;
		}
	}
	public void moveObject(int x, int y, String direction){
		
		if(objects[x][y] != null){
			lastMovedObject  = objects[x][y];
			
			if(direction.equals(RIGHT)){
				objects[x][y].isMovingRight = true;
				objects[x+1][y] = objects[x][y];
				objects[x][y] = null;
			}
			if(direction.equals(LEFT)){
				objects[x][y].isMovingLeft = true;
				objects[x-1][y] = objects[x][y];
				objects[x][y] = null;
			}
			if(direction.equals(UP)){
				objects[x][y].isMovingUp = true;
				objects[x][y+1] = objects[x][y];
				objects[x][y] = null;
			}
			if(direction.equals(DOWN)){
				objects[x][y].isMovingDown = true;
				objects[x][y-1] = objects[x][y];
				objects[x][y] = null;
			}
		}
		
	}
	
	public boolean predictCollision(int x, int y, String direction){
		boolean collision = false;
		if(x <= PLAYGROUND_MAX_X && x >= PLAYGROUND_MIN_X && y <= PLAYGROUND_MAX_Y && y >= PLAYGROUND_MIN_Y){
			if(objects[x][y] instanceof Player){
				if(direction.equals(RIGHT)){
					if((x+1 > PLAYGROUND_MAX_X || objects[x+1][y] != null)){
						collision = true;
					}
				}
				if(direction.equals(LEFT)){
					if((x-1 < PLAYGROUND_MIN_X || objects[x-1][y] != null)){
						collision = true;
					}
				}
				if(direction.equals(UP)){
					if((y+1 > PLAYGROUND_MAX_Y || objects[x][y+1] != null)){
						collision = true;
					}
				}
				if(direction.equals(DOWN)){
					if((y-1 < PLAYGROUND_MIN_Y || objects[x][y-1] != null)){
						collision = true;
					}
				}
			}
			if(objects[x][y] instanceof Polynomio){
				if(direction.equals(RIGHT)){
					if(x+1 > PLAYGROUND_MAX_X){
						collision = true;
					}	
					else if(objects[x+1][y] != null){
						collision = true;
						if(objects[x+1][y] == objects[x][y]){
							if(!predictCollision(x+2,y,RIGHT)){	
								collision = false;
							}
						}
					}
				}
				if(direction.equals(LEFT)){
					if(x-1 < PLAYGROUND_MIN_X){
						collision = true;
					}	
					else if(objects[x-1][y] != null){
						collision = true;
						if(objects[x-1][y] == objects[x][y]){
							if(!predictCollision(x-2,y,LEFT)){	
								collision = false;
							}
						}
					}
				}
				if(direction.equals(UP)){
					if(y+1 > PLAYGROUND_MAX_Y){
						collision = true;
					}	
					else if(objects[x][y+1] != null){
						collision = true;
						if(objects[x][y+1] == objects[x][y]){
							if(!predictCollision(x,y+2,UP)){	
								collision = false;
							}
						}
					}
				}
				if(direction.equals(DOWN)){
					if(y-1 < PLAYGROUND_MIN_Y){
						collision = true;
					}	
					else if(objects[x][y-1] != null){
						collision = true;
						if(objects[x][y-1] == objects[x][y]){
							if(!predictCollision(x,y-2,DOWN)){	
								collision = false;
							}
						}
					}
				}
			}
		}
		return collision;
	}
	public void movePolynomio(int x, int y, String direction){
		boolean collision = false;
		Polynomio polynomio = (Polynomio) objects[x][y];
		polynomio.sortBlocks(direction);
		while(!collision){
			for(int i = 0; i < polynomio.blocks.size(); i++){
				if(predictCollision(polynomio.blocks.get(i).x, polynomio.blocks.get(i).y, direction)){
					collision = true;
				}
			}
			if(!collision){
				for(int i = 0; i < polynomio.blocks.size(); i++){
					moveObject(polynomio.blocks.get(i).x, polynomio.blocks.get(i).y, direction);
					Block block = polynomio.blocks.get(i);
					if(direction.equals(RIGHT)){
						block.x++;
					}
					else if(direction.equals(LEFT)){
						block.x--;
					}
					else if(direction.equals(UP)){
						block.y++;
					}
					else if(direction.equals(DOWN)){
						block.y--;
					}

					polynomio.blocks.set(i, block);
				}				
			}
		}
	}
	public void movePlayer(int x, int y, String direction){
		objects[x][y].block_position = new Vector(x,y,0);
		while(!predictCollision(x, y, direction)){
			moveObject(x, y, direction);
			if(direction.equals(RIGHT)){
				x++;
			}
			else if(direction.equals(LEFT)){
				x--;
			}
			else if(direction.equals(UP)){
				y++;
			}
			else if(direction.equals(DOWN)){
				y--;
			}
		}
	}
	
	public void checkPlayerPosition(){
		for(int i = 0; i < objects.length; i++){
			for(int j = 0; j < objects[0].length; j++){
				if(lastMovedObject != null && objects[i][j] == lastMovedObject){
					if(!objects[i][j].isMovingRight && !objects[i][j].isMovingLeft && !objects[i][j].isMovingUp && !objects[i][j].isMovingDown){
						if(objects[i][j].isPlayerOne && i == PLAYGROUND_MAX_X){
							setWinner((Player) objects[i][j]);
						}
						else if(!objects[i][j].isPlayerOne && i == PLAYGROUND_MIN_X){
							setWinner((Player) objects[i][j]);
						}
						else if((predictCollision(i, j, UP) && objects[i][j].lastState.equals(UP)) || (predictCollision(i, j, DOWN) && objects[i][j].lastState.equals(DOWN))){
							if(!predictCollision(i, j, RIGHT) && predictCollision(i, j, LEFT)){
								movePlayer(i, j, RIGHT);
							}
							else if(predictCollision(i, j, RIGHT) && !predictCollision(i, j, LEFT)){
								movePlayer(i, j, LEFT);
							}
						}
						else if((predictCollision(i, j, RIGHT) && objects[i][j].lastState.equals(RIGHT)) || (predictCollision(i, j, LEFT) && objects[i][j].lastState.equals(LEFT))){
							if(!predictCollision(i, j, UP) && predictCollision(i, j, DOWN)){
								movePlayer(i, j, UP);
							}
							else if(predictCollision(i, j, UP) && !predictCollision(i, j, DOWN)){
								movePlayer(i, j, DOWN);
							}
						}
					}
				}
			}
		}
	}
	
	public void setWinner(Player winner){
		this.winner = winner;
		hasWinner = true;
	}
	
	public void update(GameActivity activity){
		getTouch(activity);
		checkPlayerPosition();
		
	}
}
