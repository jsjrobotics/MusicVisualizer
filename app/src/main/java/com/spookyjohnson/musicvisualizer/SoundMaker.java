package com.spookyjohnson.musicvisualizer;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundMaker extends ActionMatrixListener{

    private static final int THRESHHOLD =   0xFF770000;
    private static final int RED_MASK =     0x00FF0000;
    private static final int GREEN_MASK =   0x0000FF00;
    private static final int BLUE_MASK =    0x000000FF;

    private final MediaPlayer mMediaPlayer;
    private boolean mIsCompleted = true;

    public SoundMaker(Context context){
        mMediaPlayer = MediaPlayer.create(context, R.raw.golf_swing);
        mMediaPlayer.setOnCompletionListener(mediaPlayer -> mIsCompleted = true);
    }

    @Override
    protected boolean matches(int[][] input) {
        for(int yIndex = 0; yIndex < input.length; yIndex++){
            for(int xIndex = 0; xIndex < input[yIndex].length; xIndex++){
                int xValue = input[yIndex][xIndex];
                int red = redComponent(xValue);
                int green = greenComponent(xValue);
                int blue = blueComponent(xValue);
                if(red > THRESHHOLD && red > green && red > blue){
                    return true;
                }
            }
        }
        return false;
    }

    private static int redComponent(int input){
        return input & RED_MASK;
    }

    private static int greenComponent(int input){
        return input & GREEN_MASK;
    }
    private static int blueComponent(int input){
        return input & BLUE_MASK;
    }

    @Override
    protected void execute() {
        if(mIsCompleted){
            mIsCompleted = false;
            mMediaPlayer.start();
        }
    }
}
