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
    private final GridView mGridView;
    private final Activity mActivity;
    private final Button mConnect;
    private List<Receiver<Void>> mOnConnectClicked = new ArrayList<>();

    public SpookyBoxView(Activity activity, LayoutInflater inflater, ViewGroup container) {
        mActivity = activity;
        mView = inflater.inflate(R.layout.fragment_main, container, false);
        mGridView = (GridView) mView.findViewById(R.id.matrix_holder);
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
            mGridView.setAdapter(adapter);
            mGridView.setNumColumns(downscaledMatrix.length);
            mGridView.setStretchMode(GridView.NO_STRETCH);
            mGridView.setVerticalSpacing(0);
            mGridView.setHorizontalSpacing(0);
            mGridView.setColumnWidth(adapter.getViewWidth());
            mGridView.invalidate();
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
