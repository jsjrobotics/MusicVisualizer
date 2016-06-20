package com.spookyjohnson.musicvisualizer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

public class GridViewAdapter extends BaseAdapter {
    private final int[][] mData;
    private final int mViewWidth;
    private Context mContext;

    public GridViewAdapter(Context c, int[][] downscaledMatrix) {
        mContext = c;
        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mViewWidth = 40;//size.x / downscaledMatrix.length;
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
            imageView.setLayoutParams(new GridView.LayoutParams(mViewWidth, mViewWidth));
        } else {
            imageView = (ImageView) convertView;
        }

        int yIndex = position / mData[0].length;
        int xIndex = position - (yIndex * mData[0].length);
        imageView.setBackgroundColor(mData[yIndex][xIndex]);
        return imageView;
    }

    public int getViewWidth() { return mViewWidth;
    }
}
