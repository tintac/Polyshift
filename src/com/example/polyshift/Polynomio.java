package com.example.polyshift;

import java.util.ArrayList;
import java.util.Collections;

import android.util.Log;

public class Polynomio extends GameObject {
	
	public ArrayList<Block>blocks = new ArrayList<Block>();

	private int size;
	
	public Polynomio(int [] direction, int size, int startX, int startY){
		this.size = size;
		colors[0] = (float)Math.random();
		colors[1] = (float)Math.random();
		colors[2] = (float)Math.random();
		colors[3] = (float)Math.random();
		populate(direction,startX,startY);
	}
	
	private void addBlock(int x, int y){
		Block block = new Block(x, y);
		blocks.add(block);
	}
	
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
	public void sortBlocks(String direction){
		if(direction.equals("left")){
			Collections.sort(blocks);
		}
		if(direction.equals("right")){
			Collections.sort(blocks, Collections.reverseOrder());
		}
		if(direction.equals("up")){
			Collections.sort(blocks, new BlockComparator());
		}
		if(direction.equals("down")){
			Collections.sort(blocks, Collections.reverseOrder(new BlockComparator()));
		}
	}
	
}
	
	