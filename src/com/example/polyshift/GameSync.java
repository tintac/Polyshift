package com.example.polyshift;

import android.util.Base64;
import android.util.Log;

import com.example.polyshift.Tools.PHPConnector;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GameSync {

    static Simulation simulation;

    public static void uploadSimulation(final Simulation simulation){
        new Thread(
                new Runnable(){
                    public void run(){
                        String serializedObjects = "";
                        try {
                            for(int i = 0; i < simulation.objects.length; i++) {
                                for (int j = 0; j < simulation.objects[0].length; j++) {
                                    if(simulation.objects[i][j] instanceof Player) {
                                        Log.d("Objekt: ", "" + i + j + simulation.objects[i][j].isPlayerOne);
                                    }
                                }
                            }
                            ByteArrayOutputStream bo = new ByteArrayOutputStream();
                            ObjectOutputStream so = new ObjectOutputStream(bo);
                            so.writeObject(simulation);
                            so.flush();

                            serializedObjects = Base64.encodeToString(bo.toByteArray(), Base64.DEFAULT);
                            Log.d("objects: ",serializedObjects);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                        nameValuePairs.add(new BasicNameValuePair("objects", serializedObjects));
                        PHPConnector.doRequest(nameValuePairs, "update_playground.php");
                    }
                }
        ).start();

    }
    public static Simulation downloadSimulation() {
        Thread download_playground_thread = new DownloadSimulationThread();
        download_playground_thread.start();
        try {
            long waitMillis = 10000;
            while (download_playground_thread.isAlive()) {
                download_playground_thread.join(waitMillis);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return simulation;
    }

    public static class DownloadSimulationThread extends Thread{

        public void run(){
            String serializedObjects = PHPConnector.doRequest("update_playground.php");

            try {
                byte b[] = Base64.decode(serializedObjects,Base64.DEFAULT);
                //byte b[] = serializedObjects.getBytes();
                ByteArrayInputStream bi = new ByteArrayInputStream(b);
                ObjectInputStream si = new ObjectInputStream(bi);
                simulation = (Simulation) si.readObject();
                for(int i = 0; i < simulation.objects.length; i++) {
                    for (int j = 0; j < simulation.objects[0].length; j++) {
                        if(simulation.objects[i][j] instanceof Player) {
                            if(simulation.player == simulation.objects[i][j]){
                                simulation.objects[i][j].isPlayerOne = true;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }
}
