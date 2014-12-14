package com.example.polyshift;

import java.util.ArrayList;

import android.util.Log;

public class Simulation {
	
	final int PLAYGROUND_MAX_X = 16;
	final int PLAYGROUND_MIN_X = 0;
	final int PLAYGROUND_MAX_Y = 8;
	final int PLAYGROUND_MIN_Y = 0;
	final int PLAYGROUND_POPULATE = 4;
	final int POLYNOMIO_SIZE = 4;
	final static String RIGHT = "right";
	final static String LEFT = "left";
	final static String UP = "up";
	final static String DOWN = "down";
	
	public boolean hasWinner =  false;
	public Player player;
	public Player player2;
	public Player winner;
	
	ArrayList<Polynomio>polynomios = new ArrayList<Polynomio>();
	
	public GameObject[][] objects = new GameObject[PLAYGROUND_MAX_X+1][PLAYGROUND_MAX_Y+1];

	public GameObject lastMovedObject;
	public Polynomio lastMovedPolynomio;
	
	public Simulation(GameActivity activity){
		populate();
	}
	
	public void populate(){
		
		ArrayList<int[]>directions = new ArrayList<int[]>();
		
		this.objects = new GameObject[PLAYGROUND_MAX_X+1][PLAYGROUND_MAX_Y+1];
		
		player = new Player(true);
		setGameObject(player, PLAYGROUND_MIN_X, PLAYGROUND_MAX_Y/2);
		player2 = new Player(false);
		setGameObject(player2, PLAYGROUND_MAX_X,PLAYGROUND_MAX_Y/2);
		
		int a = 0;
		while(a<10){
			for(int x = (PLAYGROUND_MAX_X/2)-(PLAYGROUND_POPULATE/2);x <= (PLAYGROUND_MAX_X/2)+(PLAYGROUND_POPULATE/2);x++){
				for(int y = 0;y < PLAYGROUND_MAX_Y;y++){
					if(!(objects[x][y] instanceof GameObject)){
						int currentPolynomioSize = 0;
						Polynomio polynomio = new Polynomio();
						int currentX = x;
						int currentY = y;
						int lastX = x;
						int lastY = y;
						boolean canGoRight = true;
						boolean canGoLeft = true;
						boolean canGoUp = true;
						boolean canGoDown = true;
						while(currentPolynomioSize < POLYNOMIO_SIZE &&
								canGoRight &&
								canGoLeft &&
								canGoUp &&
								canGoDown){
							boolean free = true;
							lastX = currentX;
							lastY = currentY;
							int newBlockDirection =   (int)(Math.random()*4+1);
							if(newBlockDirection == 1 && (currentX < (PLAYGROUND_MAX_X/2)+(PLAYGROUND_POPULATE/2))){
						            currentX++;
							}
							else if(newBlockDirection == 2 && (currentX > (PLAYGROUND_MAX_X/2)-(PLAYGROUND_POPULATE/2))){ 
						            currentX--; 
							}
				            else if(newBlockDirection == 3 && (currentY < PLAYGROUND_MAX_Y)){ 
						            currentY++;
				            }
				            else if(newBlockDirection == 4 && (currentY > PLAYGROUND_MIN_Y)){ 
						            currentY--;
					        }
							if(!(objects[currentX][currentY] instanceof GameObject)){
								for(int i = 0; i < polynomio.size; i++){
									Block block = polynomio.blocks.get(i);
									if(block.x == currentX && block.y == currentY){
										free = false;
										currentX = lastX;
										currentY = lastY;
									}
								}
								if(free){
									polynomio.addBlock(new Block(currentX,currentY));
									polynomio.blockCounter();
									currentPolynomioSize++;
								}
							}else{
								currentX = lastX;
								currentY = lastY;
								if(newBlockDirection == 1 ){
						            canGoRight = false;
								}
								else if(newBlockDirection == 2){ 
						            canGoLeft = false;
								}
								else if(newBlockDirection == 3){ 
						            canGoUp = false;
								}
								else if(newBlockDirection == 4){ 
						            canGoDown = false;
								}
							}
						}
						if (polynomio.size == POLYNOMIO_SIZE){
							polynomios.add(polynomio);
							for(int j = 0; j< polynomio.blocks.size();j++){
								Block block = polynomio.blocks.get(j);
								setGameObject(polynomio, block.x, block.y);
							}
						}
					}
				}
			}
		a++;
		}
		
		
		
	}
	
		
	
	public void setGameObject(GameObject object, int x, int y){
		objects[x][y] = object;
	}
	
	public void getTouch(GameActivity activity){
		if(activity.isSwiped()){
			int x = Math.round(activity.getTouchX() / (activity.getViewportWidth() / objects.length));
			int y = Math.round(objects[0].length - (activity.getTouchY() / (activity.getViewportHeight() / objects[0].length)) - 1);
			int touchedX = Math.round(activity.getTouchedX() / (activity.getViewportWidth() / objects.length));
			int touchedY = Math.round(objects[0].length - (activity.getTouchedY() / (activity.getViewportHeight() / objects[0].length)) - 1);
			
			if(x > touchedX && y == touchedY){
				activity.isSwiped = false;
				if(objects[touchedX][touchedY] instanceof Player && !objects[touchedX][touchedY].isLocked){
					movePlayer(touchedX, touchedY, RIGHT);
				}
				if(objects[touchedX][touchedY] instanceof Polynomio && !(lastMovedObject instanceof Polynomio) && !objects[touchedX][touchedY].isLocked){
					movePolynomio(touchedX, touchedY, RIGHT);
				}
			}
			else if(x < touchedX && y == touchedY){
				activity.isSwiped = false;
				if(objects[touchedX][touchedY] instanceof Player && !objects[touchedX][touchedY].isLocked){
					movePlayer(touchedX, touchedY, LEFT);
				}
				if(objects[touchedX][touchedY] instanceof Polynomio && !(lastMovedObject instanceof Polynomio) && !objects[touchedX][touchedY].isLocked){
					movePolynomio(touchedX, touchedY, LEFT);
				}
			}
			else if(y > touchedY && x == touchedX){
				activity.isSwiped = false;
				if(objects[touchedX][touchedY] instanceof Player && !objects[touchedX][touchedY].isLocked){
					movePlayer(touchedX, touchedY, UP);
				}
				if(objects[touchedX][touchedY] instanceof Polynomio && !(lastMovedObject instanceof Polynomio) && !objects[touchedX][touchedY].isLocked){
					movePolynomio(touchedX, touchedY, UP);
				}
			}
			else if(y < touchedY && x == touchedX){
				activity.isSwiped = false;
				if(objects[touchedX][touchedY] instanceof Player && !objects[touchedX][touchedY].isLocked){
					movePlayer(touchedX, touchedY, DOWN);
				}
				if(objects[touchedX][touchedY] instanceof Polynomio && !(lastMovedObject instanceof Polynomio) && !objects[touchedX][touchedY].isLocked){
					movePolynomio(touchedX, touchedY, DOWN);
				}
			}
		}
	}
	public void moveObject(int x, int y, String direction){
		
		if(objects[x][y] != null){
			lastMovedObject  = objects[x][y];
			if(objects[x][y] instanceof Polynomio){
				if(lastMovedPolynomio instanceof Polynomio){
					lastMovedPolynomio.isLocked = false;
				}
				lastMovedPolynomio = (Polynomio) objects[x][y];
				objects[x][y].isLocked = true;
			}
			
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
		polynomio.block_position = new Vector(x,y,0);
		polynomio.sortBlocks(direction);
		polynomio.blocks.get(3).block_position.x = polynomio.blocks.get(3).x;
		polynomio.blocks.get(0).block_position.x = polynomio.blocks.get(0).x;
		polynomio.blocks.get(3).block_position.y = polynomio.blocks.get(3).y;
		polynomio.blocks.get(0).block_position.y = polynomio.blocks.get(0).y;
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
				if(lastMovedObject != null && objects[i][j] == lastMovedObject && objects[i][j] instanceof Player){
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
