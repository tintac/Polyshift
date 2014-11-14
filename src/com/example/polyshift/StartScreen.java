package com.example.polyshift;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;

import com.example.polyshift.Font.FontStyle;
import com.example.polyshift.Font.Text;
import com.example.polyshift.Mesh.PrimitiveType;

public class StartScreen implements GameScreen {

	boolean isDone = false;
	Font font;
	Text text;
	String pressText = "Touch Screen to Start!";
	
	public StartScreen( GL10 gl, GameActivity activity )
	{			
		
	}	

	@Override
	public boolean isDone() 
	{	
		return isDone;
	}

	@Override
	public void update(GameActivity activity) 
	{	
		if( activity.isTouched() )
			isDone = true;
	}
	
	@Override
	public void render(GL10 gl, GameActivity activity) 
	{	

	}



	
}
