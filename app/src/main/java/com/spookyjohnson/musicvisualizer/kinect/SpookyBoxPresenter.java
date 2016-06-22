package com.spookyjohnson.musicvisualizer.kinect;

import com.spookyjohnson.musicvisualizer.functional.Receiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class SpookyBoxPresenter {
    private static final String DATA = "DATA";
    private final URL mUrl;
    private final SpookyBoxView mView;
    private SpookyBoxConnection mSpookyBoxConnection;
    private boolean mIsConnected = false;
    private final Receiver<String> mReceiver;
    private Thread mThread;

    public SpookyBoxPresenter(SpookyBoxView view, String url){
        mView = view;
        try {
            mUrl = new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Failed to build url: "+e);
        }
        mReceiver = buildReceiver();
        mView.addOnConnectListener(getClickListener());

    }

    private Receiver<String> buildReceiver() {
        return data -> {
            try {
                JSONObject received = new JSONObject(data);
                JSONArray matrixArray = received.getJSONArray(DATA);
                for(int index = 0; index < matrixArray.length(); index++){
                    int[][] rgbMatrix = transformToRgbMatrix(matrixArray.getJSONArray(index));
                    mView.drawDownscaledMatrix(rgbMatrix);
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
        if(mSpookyBoxConnection != null){
            mSpookyBoxConnection.disconnect();
        }
    }

    public Receiver<Void> getClickListener(){
        return ignored -> {
            if(mIsConnected){
                return;
            }
            mIsConnected = true;
            mThread = new Thread(() -> {
                for(int index = 0; index < 100;){
                    onConnectClicked();
                }
                mIsConnected = false;
                mView.enableConnectButton();
            });

            mView.disableConnectButton();
            mThread.start();
        };
    }

    private void onConnectClicked() {
        mSpookyBoxConnection = new SpookyBoxConnection(mUrl, mReceiver);
        mSpookyBoxConnection.connect();
    }
}
