package com.example.polyshift;

import java.util.Comparator;

public class Block extends GameObject implements Comparable<Block>{
	public int x;
	public int y;
	
	public Block(int x, int y){
		this.x = x;
		this.y = y;
		
	}

	@Override
	public int compareTo(Block another) {
		
		return Integer.compare(this.x,another.x);
	}
}

