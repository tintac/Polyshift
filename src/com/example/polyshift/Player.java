package com.example.polyshift;

import java.io.Serializable;

public class Player extends GameObject implements Serializable {
	
	public Player (boolean isPlayerOne){
		this.isPlayerOne =  isPlayerOne;
		
	}
}
