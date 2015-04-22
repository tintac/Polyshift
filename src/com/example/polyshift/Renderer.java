package com.example.polyshift;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import com.example.polyshift.Mesh.PrimitiveType;
import com.example.polyshift.Texture.TextureFilter;
import com.example.polyshift.Texture.TextureWrap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.opengl.GLU;
import android.util.Log;
import android.view.Display;

public abstract class Renderer {
	
	float block_width;
	float block_height;
	float object_width;
	float object_height;
	int count = 0;
	Texture texturePlayerOne;
	Texture texturePlayerTwo;
	Texture texturePlayerOneLock;
	Texture texturePlayerTwoLock;
	Texture textureLocker;
	ArrayList<Mesh> coordinates_list;

	public abstract void renderObjects(GameActivity activity, GL10 gl, GameObject[][] objects);
	
	public abstract void setPerspective(GameActivity activity, GL10 gl);
	
	public abstract void enableCoordinates(GL10 gl, GameObject[][] objects);
	
	public abstract void renderLight(GL10 gl);
}