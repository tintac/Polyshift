package com.example.polyshift;

import java.util.Comparator;

public class BlockComparator implements Comparator<Block> {

	public int compare(Block lhs, Block rhs) {
		return Integer.compare(lhs.y, rhs.y);
	}

}
