package com.spookyjohnson.musicvisualizer.kinect;

import com.spookyjohnson.musicvisualizer.functional.Receiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class SpookyBoxReceiver {
    private static final String DATA = "DATA";
    private final URL mUrl;
    private final Receiver<int[][]> mRgbReceiver;
    private SpookyBoxConnection mSpookyBoxConnection;
    private boolean mIsConnected = false;
    private final Receiver<String> mDataReceiver;
    private Thread mThread;
    private boolean mDisconnect = false;

    public SpookyBoxReceiver(Receiver<int[][]> rgbReceiver, String url){
        mRgbReceiver = rgbReceiver;
        try {
            mUrl = new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Failed to build url: "+e);
        }
        mDataReceiver = buildReceiver();
    }

    private Receiver<String> buildReceiver() {
        return data -> {
            try {
                JSONObject received = new JSONObject(data);
                JSONArray matrixArray = received.getJSONArray(DATA);
                for(int index = 0; index < matrixArray.length(); index++){
                    int[][] rgbMatrix = transformToRgbMatrix(matrixArray.getJSONArray(index));
                    mRgbReceiver.accept(rgbMatrix);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
    }

    private int[][] transformToRgbMatrix(JSONArray jsonArray) {
        int[][] result = new int[jsonArray.length()][];
        try {
            for(int yIndex = 0; yIndex < jsonArray.length(); yIndex++) {
                JSONArray rgbRow = jsonArray.getJSONArray(yIndex);
                result[yIndex] = new int[rgbRow.length()];
                for(int xIndex = 0; xIndex < rgbRow.length(); xIndex++){
                    result[yIndex][xIndex] = (int) Long.parseLong(rgbRow.getString(xIndex),16);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    public void disconnect(){
        mDisconnect = true;
        if(mSpookyBoxConnection != null){
            mSpookyBoxConnection.disconnect();
        }
    }

    public void connect(){
        if(mIsConnected){
            return;
        }
        mDisconnect = false;
        mIsConnected = true;
        mThread = new Thread(() -> {
            while(!mDisconnect){
                startConnection();
            }
            mIsConnected = false;
        });
        mThread.start();
    }

    private void startConnection() {
        mSpookyBoxConnection = new SpookyBoxConnection(mUrl, mDataReceiver);
        mSpookyBoxConnection.connect();
    }
}
