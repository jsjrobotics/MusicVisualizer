package com.spookyjohnson.musicvisualizer.kinect;

import com.spookyjohnson.musicvisualizer.functional.Receiver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpookyBoxConnection {

    private final URL mUrl;
    private final Receiver<String> mReceiver;
    private boolean mIsDisconnecting = true;

    public SpookyBoxConnection(URL url, Receiver<String> receiver){
        mUrl = url;
        mReceiver = receiver;
    }

    public void connect(){
        mIsDisconnecting = false;
        HttpURLConnection urlConnection = null;
        String data = null;
        try {
            urlConnection = (HttpURLConnection) mUrl.openConnection();
            urlConnection.setConnectTimeout(10000);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStreamReader in =new InputStreamReader(urlConnection.getInputStream());
            data = readStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
        }
        if(data != null){
            mReceiver.accept(data);
        }
    }

    public void disconnect(){
        mIsDisconnecting = true;
    }

    private String readStream(InputStreamReader in) {
        try {
            StringBuilder data = new StringBuilder();
            char[] inputBuffer = new char[500];
            int read = in.read(inputBuffer);
            while(!mIsDisconnecting && read != -1) {
                data.append(inputBuffer,0,read);
                read = in.read(inputBuffer);
            }
            return data.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
