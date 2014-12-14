package com.example.polyshift;

public class GameObject {
	
	public boolean isMovingLeft = false;
	public boolean isMovingRight = false;
	public boolean isMovingUp = false;
	public boolean isMovingDown = false;
	public boolean isLocked = false;
	public String lastState = "";
	public float movingVelocity = 0.15f;
	public Vector block_position;
	public Vector pixel_position;
	private Mesh mesh;
	public  float []colors = new float[4];
	boolean isPlayerOne;

	public Mesh getMesh() {
		return mesh;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
		this.pixel_position = new Vector(-1,-1,0);
	}
	public void getColors(){
		
		
	}

}
