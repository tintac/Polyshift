package com.example.polyshift;

import javax.microedition.khronos.opengles.GL10;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.polyshift.Tools.PHPConnector;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class PolyshiftActivity extends GameActivity implements GameListener {

    Player player;
    Player player2;
    Polynomino poly;
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
            gameLoop = new GameLoop();

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

            String[] game = response.split(":");
            if(game[6].split("=")[1].equals("1")){
                simulation = new Simulation(activity);
                gameLoop.setRandomPlayer();
                GameSync.uploadSimulation(simulation);
                Log.d("Test","Upload!");
            }
            else{
                simulation = GameSync.downloadSimulation();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MenuItem game_status = menu.findItem(R.id.action_game_status);
                    String game[] = response.split(":");
                    if(game[4].split("=")[1].equals("0") && game[5].split("=")[1].equals("yes")) {  // my turn & my game
                        setTitle("Du bist dran!");
                        gameLoop.PlayerOnesTurn = true;
                    }
                    else if(game[4].split("=")[1].equals("0") && game[5].split("=")[1].equals("no")) { // my turn & not my game
                        setTitle("Du bist dran!");
                        gameLoop.PlayerOnesTurn = false;
                    }
                    else if(game[4].split("=")[1].equals("1") && game[5].split("=")[1].equals("yes")) { //  not my turn & my game
                        setTitle(game[2].split("=")[1] + " ist dran.");
                        gameLoop.PlayerOnesTurn = false;
                    }
                    else { //  not my turn & not my game
                        setTitle(game[2].split("=")[1] + " ist dran.");
                        gameLoop.PlayerOnesTurn = true;
                    }
                }
            });
            renderer = new Renderer3D(activity, gl, simulation.objects);
            renderer.enableCoordinates(gl, simulation.objects);

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
