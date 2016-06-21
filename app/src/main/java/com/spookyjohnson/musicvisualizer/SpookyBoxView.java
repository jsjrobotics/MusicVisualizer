package com.spookyjohnson.musicvisualizer;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.spookyjohnson.musicvisualizer.functional.Receiver;

import java.util.ArrayList;
import java.util.List;

public class SpookyBoxView {
    private final View mView;
    private final GridView mDownscaledView;
    private final Activity mActivity;
    private final Button mConnect;
    private final SoundMaker mSoundMaker;
    private List<Receiver<Void>> mOnConnectClicked = new ArrayList<>();

    public SpookyBoxView(Activity activity, LayoutInflater inflater, ViewGroup container) {
        mActivity = activity;
        mView = inflater.inflate(R.layout.fragment_main, container, false);
        mDownscaledView = (GridView) mView.findViewById(R.id.matrix_holder);
        mSoundMaker = new SoundMaker(activity);
        mConnect = (Button) mView.findViewById(R.id.connect);
        mConnect.setOnClickListener(v -> {
            for(Receiver<Void> r : mOnConnectClicked){
                r.accept(null);
            }
        });
        mConnect.setBackgroundColor(Color.GREEN);
    }

    public void addOnConnectListener(Receiver<Void> listener){
        mOnConnectClicked.add(listener);
    }


    public void drawDownscaledMatrix(final int[][] downscaledMatrix) {
        mActivity.runOnUiThread(() -> {
            GridViewAdapter adapter = new GridViewAdapter(mActivity, downscaledMatrix);
            mDownscaledView.setAdapter(adapter);
            mDownscaledView.setNumColumns(downscaledMatrix.length);
            mDownscaledView.setStretchMode(GridView.NO_STRETCH);
            mDownscaledView.setVerticalSpacing(0);
            mDownscaledView.setHorizontalSpacing(0);
            mDownscaledView.setColumnWidth(adapter.getViewWidth());
            mDownscaledView.invalidate();

            mSoundMaker.accept(downscaledMatrix);


        });
    }

    public View getLayout() {
        return mView;
    }

    public void enableConnectButton() {
        mActivity.runOnUiThread(() -> {
            mConnect.setEnabled(true);
            mConnect.setBackgroundColor(Color.GREEN);
        });
    }

    public void disableConnectButton() {
        mActivity.runOnUiThread(() -> {
            mConnect.setEnabled(false);
            mConnect.setBackgroundColor(Color.RED);
        });
    }
}
