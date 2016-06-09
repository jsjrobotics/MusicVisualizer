package com.spookyjohnson.musicvisualizer;

import android.graphics.Color;
import android.view.View;

import com.spookyjohnson.musicvisualizer.functional.Receiver;
import com.spookyjohnson.musicvisualizer.inputStateMachine.RequestFromStream;

import java.net.MalformedURLException;
import java.net.URL;

public class SpookyBoxPresenter {
    private final URL mUrl;
    private SpookyBoxConnection mSpookyBoxConnection;
    private boolean mIsConnected = false;
    private final Receiver<RequestFromStream> mReceiver;
    private Thread mThread;

    public SpookyBoxPresenter(String url, Receiver<RequestFromStream> receiver){
        try {
            mUrl = new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Failed to build url: "+e);
        }
        mReceiver = receiver;
    }

    public void disconnect(){
        if(mSpookyBoxConnection != null){
            mSpookyBoxConnection.disconnect();
        }
    }

    public View.OnClickListener getClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mIsConnected){
                    mThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            onConnectClicked();
                            mIsConnected = false;
                        }
                    });
                    mIsConnected = true;
                    mThread.start();
                    v.setBackgroundColor(Color.RED);
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            disconnect();
                            mIsConnected = false;
                            v.setBackgroundColor(Color.GREEN);
                            v.setOnClickListener(getClickListener());
                        }
                    });
                }
            }
        };
    }

    private void onConnectClicked() {
        mSpookyBoxConnection = new SpookyBoxConnection(mUrl, mReceiver);
        mSpookyBoxConnection.connect();
    }
}
