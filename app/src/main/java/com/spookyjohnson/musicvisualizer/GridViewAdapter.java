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

/**
 * Created by spooky on 6/12/16.
 */
public class GridViewAdapter extends BaseAdapter {
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
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        int yIndex = position / mData.length;
        int xIndex = position - (yIndex * mData.length);
        Log.e("TAG","Reading mData["+yIndex+"]["+xIndex+"]");
        imageView.setBackgroundColor(mData[yIndex][xIndex]);
        return imageView;
    }

}
