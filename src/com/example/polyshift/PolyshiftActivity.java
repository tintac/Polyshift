package com.example.polyshift;

import javax.microedition.khronos.opengles.GL10;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.polyshift.Tools.PHPConnector;


public class PolyshiftActivity extends GameActivity implements GameListener {

	Player player;
	Player player2;
	Polynomio poly;
	Renderer renderer;
	StartScreen startScreen;
	EndScreen endScreen;
	Simulation simulation;
	GameLoop gameLoop;
    private Menu menu;
    private String response;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_OPTIONS_PANEL);

		super.onCreate(savedInstanceState);
		
		setGameListener(this);

		Log.d( "Polyshift", "Polyshift Spiel erstellt");
	}

	public void onSaveInstanceState( Bundle outState )
	{
		super.onSaveInstanceState( outState );
		Log.d( "Polyshift", "Polyshift Spielstand gespeichert" );
	}

	@Override
	public void onPause( )
	{
		super.onPause();
		Log.d( "Polyshift", "Polyshift pausiert" );
	}
	
	@Override
	public void onResume( )
	{
		super.onResume();		
		Log.d( "Polyshift", "Polyshift wiederhergestellt" );
	}	
	
	@Override
	public void onDestroy( )
	{
		super.onDestroy();
		Log.d( "Polyshift", "Polyshift beendet" );
	}
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_status, menu);
        this.menu = menu;
        return true;
    }

	@Override
	public void setup(GameActivity activity, GL10 gl) {
		
		if(!(simulation instanceof Simulation)){
			startScreen = new StartScreen(gl, activity);
			endScreen = new EndScreen(gl, activity);
			simulation = new Simulation(activity);
			gameLoop = new GameLoop();
			renderer = new Renderer3D(activity, gl, simulation.objects);
			renderer.enableCoordinates(gl, simulation.objects);

            Thread game_status_thread = new GameStatusThread();
            game_status_thread.start();
            try {
                long waitMillis = 10000;
                while (game_status_thread.isAlive()) {
                    game_status_thread.join(waitMillis);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MenuItem game_status = menu.findItem(R.id.action_game_status);
                    String game[] = response.split(":");
                    if(game[4].equals("false")) {
                        setTitle("Du bist dran!");
                    }
                    else{
                        setTitle(game[2].split("=")[1] + " ist dran.");
                    }
                }
            });
		}	
	}
	  
	long start = System.nanoTime();
    int frames = 0;
	
	@Override
	public void mainLoopIteration(GameActivity activity, GL10 gl) {
		if(!startScreen.isDone){
			startScreen.update(activity);
			startScreen.render(gl, activity);
		}
		else{
			renderer.setPerspective(activity, gl);
			renderer.renderLight(gl);
			renderer.renderObjects(activity, gl, simulation.objects);
			simulation.update(activity);
			gameLoop.update(simulation);
			if(simulation.hasWinner){
				endScreen.setWinner(simulation.winner);
				endScreen.render(gl, activity);
				endScreen.update(activity);

				if(activity.isTouched()){
					activity.finish();
					startActivity(activity.getIntent());
				}
			}
			
		}
			
        frames++;
        if( System.nanoTime() - start > 1000000000 )
        {
                frames = 0;
                start = System.nanoTime();
        }

	}
    private class GameStatusThread extends Thread{
        public void run(){
            response = PHPConnector.doRequest("get_game_status.php");
        }
    }
}
