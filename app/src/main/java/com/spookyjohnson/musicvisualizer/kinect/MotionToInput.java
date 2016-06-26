package com.spookyjohnson.musicvisualizer.kinect;

import com.spookyjohnson.musicvisualizer.functional.Receiver;

public class MotionToInput extends ActionMatrixListener{

    private static final int THRESHHOLD =   0x30;
    private static final int RED_MASK =     0x00FF0000;
    private static final int GREEN_MASK =   0x0000FF00;
    private static final int BLUE_MASK =    0x000000FF;
    private static final String TAG = "SoundMaker";
    private final Receiver<boolean[]> mReceiver;

    private boolean mButton0Detected = false;
    private boolean mButton1Detected = false;


    public MotionToInput(Receiver<boolean[]> receiver){
        mReceiver = receiver;
    }

    @Override
    protected boolean matches(int[][] input) {
        boolean result = false;
        for(int yIndex = 0; yIndex < input.length; yIndex++){
            for(int xIndex = 0; xIndex < input[yIndex].length; xIndex++){
                int xValue = input[yIndex][xIndex];
                if(xValue != 0) {
                    int red = redComponent(xValue);
                    int green = greenComponent(xValue);
                    int blue = blueComponent(xValue);
                    if (red > THRESHHOLD && green < THRESHHOLD && blue < THRESHHOLD) {
                        if(xIndex < input[yIndex].length/2){
                            detectedButton0();
                        } else {
                            detectedButton1();
                        }
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    private void detectedButton1() {
        mButton1Detected = true;
    }

    private void detectedButton0() {
        mButton0Detected = true;
    }

    private static byte redComponent(int input){
        return (byte) ((input & RED_MASK) >> 16);
    }

    private static byte greenComponent(int input){
        return (byte) ((input & GREEN_MASK) >> 8);
    }
    private static byte blueComponent(int input){
        return (byte) (input & BLUE_MASK);
    }

    @Override
    protected void execute() {
        boolean[] buttons = new boolean[2];
        buttons[0] = mButton0Detected;
        buttons[1] = mButton1Detected;

        mButton0Detected = false;
        mButton1Detected = false;
        mReceiver.accept(buttons);
    }
}
