package com.spookyjohnson.musicvisualizer;

import com.spookyjohnson.musicvisualizer.functional.Receiver;
import com.spookyjohnson.musicvisualizer.inputStateMachine.InputStateMachine;
import com.spookyjohnson.musicvisualizer.inputStateMachine.RequestFromStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SpookyBoxConnection {

    private final InputStateMachine mInputStateMachine;
    private final URL mUrl;
    private boolean mIsDisconnecting = true;

    public SpookyBoxConnection(URL url, Receiver<RequestFromStream> receiver){
        mUrl = url;
        mInputStateMachine = new InputStateMachine(receiver);
    }
    public void connect(){
        mIsDisconnecting = false;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) mUrl.openConnection();
            urlConnection.setConnectTimeout(10000);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
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

    public void disconnect(){
        mIsDisconnecting = true;
    }

    private void readStream(InputStreamReader in) {
        try {
            while(true && !mIsDisconnecting) {
                char[] buffer = new char[100];
                mInputStateMachine.receiveInput(in.read(buffer), buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
