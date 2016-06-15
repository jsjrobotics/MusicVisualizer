package com.spookyjohnson.musicvisualizer;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

public class GridViewAdapter extends BaseAdapter {
    private static final int VIEW_WIDTH = 85;
    private final int[][] mData;
    private Context mContext;

    public GridViewAdapter(Context c, int[][] downscaledMatrix) {
        mContext = c;
        mData = downscaledMatrix;
    }


    public int getCount() {
        return mData.length * mData[0].length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(VIEW_WIDTH, VIEW_WIDTH));
        } else {
            imageView = (ImageView) convertView;
        }

        int yIndex = position / mData.length;
        int xIndex = position - (yIndex * mData.length);
        Log.e("TAG","Reading mData["+yIndex+"]["+xIndex+"]");
        imageView.setBackgroundColor(mData[yIndex][xIndex]);
        return imageView;
    }

    public int getViewWidth() {
        return VIEW_WIDTH;
    }
}
