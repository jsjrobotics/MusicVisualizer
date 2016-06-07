package com.spookyjohnson.musicvisualizer;

import com.spookyjohnson.musicvisualizer.inputStateMachine.InputStateMachine;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SpookyBoxConnection {

    private final InputStateMachine mInputStateMachine;

    public SpookyBoxConnection(){
        mInputStateMachine = new InputStateMachine();
    }
    public void connect(){
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL("http://www.android.com/");
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStreamReader in =new InputStreamReader(urlConnection.getInputStream());
            readStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
        }
    }

    private void readStream(InputStreamReader in) {
        try {
            char[] buffer = new char[100];
            mInputStateMachine.receiveInput(in.read(buffer), buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
