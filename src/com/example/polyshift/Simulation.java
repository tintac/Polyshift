package com.example.polyshift;

public class Simulation {

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
}
