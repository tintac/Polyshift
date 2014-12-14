package com.example.polyshift;

import java.util.ArrayList;
import java.util.Collections;

import android.util.Log;

public class Polynomio extends GameObject {
	
	public ArrayList<Block>blocks = new ArrayList<Block>();
	public int size = 0;
	public Vector block_position;
	public Vector pixel_position = new Vector(0,0,0);
	
	public Polynomio(){
		colors[0] = (float)Math.random();
		colors[1] = (float)Math.random();
		colors[2] = (float)Math.random();
		colors[3] = 1f;
		//populate(direction,startX,startY);
	}
	
	public void addBlock(Block block){
		blocks.add(block);
	}
	
	/*
	public void populate(int[]direction,int startX, int startY){
		int x = startX;
		int y = startY;
		addBlock(x, y);
		for(int i = 0 ; i<size;i++){
			if(direction[i] == 1){
				x++;
			}else if (direction[i]==2){
				x--;
			}else if (direction[i]==3){
				y++;
			}else if (direction[i]==4){
				y--;
			}else{
				x= 100;
				y= 100;
			}
			if(x != 100 && y != 100){
				addBlock(x, y);
			}
		}
	}
	
	*/
	public void sortBlocks(String direction){
		if(direction.equals("left")){
			Collections.sort(blocks);
		}
		if(direction.equals("right")){
			Collections.sort(blocks, Collections.reverseOrder());
		}
		if(direction.equals("up")){
			Collections.sort(blocks, Collections.reverseOrder(new BlockComparator()));
		}
		if(direction.equals("down")){
			Collections.sort(blocks, new BlockComparator());
		}
	}

	public void blockCounter() {
		this.size++;
	}
	
}
	
	