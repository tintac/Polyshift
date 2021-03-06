package com.example.polyshift;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import com.example.polyshift.Tools.PHPConnector;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class GameLoop {

    public boolean PlayerOnesTurn;
    private boolean RoundFinished;

    public GameLoop(){
        RoundFinished = true;
    }

    public void setRandomPlayer(){
        Random random = new Random();
        PlayerOnesTurn = random.nextBoolean();
        updateGameStatus();
    }

    public void update(Simulation simulation){
        if(PlayerOnesTurn){
            simulation.player2.isLocked = true;
            if(simulation.player.isMovingRight || simulation.player.isMovingLeft || simulation.player.isMovingUp || simulation.player.isMovingDown){
                RoundFinished = false;
            }
            if(!RoundFinished){
                if(!simulation.player.isMovingRight && !simulation.player.isMovingLeft && !simulation.player.isMovingUp && !simulation.player.isMovingDown){
                    RoundFinished = true;
                    PlayerOnesTurn = false;
                    simulation.player.isLocked = true;
                    simulation.player2.isLocked = false;
                    updateGameStatus();
                    GameSync.uploadSimulation(simulation);
                }
            }
        }
        if(!PlayerOnesTurn){
            simulation.player.isLocked = true;
            if(simulation.player2.isMovingRight || simulation.player2.isMovingLeft || simulation.player2.isMovingUp || simulation.player2.isMovingDown){
                RoundFinished = false;
            }
            if(!RoundFinished){
                if(!simulation.player2.isMovingRight && !simulation.player2.isMovingLeft && !simulation.player2.isMovingUp && !simulation.player2.isMovingDown){
                    RoundFinished = true;
                    PlayerOnesTurn = true;
                    simulation.player2.isLocked = true;
                    simulation.player.isLocked = false;
                    updateGameStatus();
                    GameSync.uploadSimulation(simulation);
                }
            }
        }
    }
    public void updateGameStatus(){
        new Thread(
                new Runnable(){
                    public void run(){
                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                        nameValuePairs.add(new BasicNameValuePair("playerOnesTurn", "" + ((PlayerOnesTurn) ? 1 : 0)));
                        PHPConnector.doRequest(nameValuePairs, "update_game.php");
                    }
                }
        ).start();
    }
}
