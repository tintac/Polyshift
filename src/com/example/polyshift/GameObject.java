package com.example.polyshift;

public class GameObject {
	
	public boolean isMovingLeft = false;
	public boolean isMovingRight = false;
	public boolean isMovingUp = false;
	public boolean isMovingDown = false;
	public Vector block_position;
	public Vector pixel_position = new Vector(-1,-1,0);
	private Mesh mesh;
	public  float []colors = new float[4];
	boolean isPlayerOne;

	public Mesh getMesh() {
		return mesh;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}
	public void getColors(){
		
		
	}

}
