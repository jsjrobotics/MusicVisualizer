package com.spookyjohnson.musicvisualizer.openGl;

import android.app.Activity;
import android.opengl.GLSurfaceView;

import com.spookyjohnson.musicvisualizer.functional.Receiver;
import com.spookyjohnson.musicvisualizer.kinect.MotionToInput;
import com.spookyjohnson.musicvisualizer.kinect.SpookyBoxReceiver;

public class OpenGlPresenter {
    private static final String URL = "http://192.168.1.11:8000";
    private final SpookyRenderer mRenderer;
    private final SpookyBoxReceiver mSpookyBoxReceiver;
    private final MotionToInput mMotionDetector;
    private final boolean mSpookyConnectionEnabled;

    public OpenGlPresenter(boolean spookyConnectionEnabled){
        mSpookyConnectionEnabled = spookyConnectionEnabled;
        mRenderer = new SpookyRenderer(mSpookyConnectionEnabled);
        mMotionDetector = new MotionToInput(getMotionReceiver());
        mSpookyBoxReceiver = new SpookyBoxReceiver(getRgbReceiver(), URL);

    }

    private Receiver<int[][]> getRgbReceiver() {
        return mMotionDetector::accept;
    }
    
    private Receiver<boolean[]> getMotionReceiver() {
        return buttons -> {
            if(buttons[0] && buttons[1]){
                return;
            }
            if(buttons[1]){
                mRenderer.incrementAngle();
            } else if(buttons[0]){
                mRenderer.decrementAngle();
            }
        };
    }

    public GLSurfaceView.Renderer getRenderer() {
        return mRenderer;
    }

    public Receiver<Activity> getOnPauseListener() {
        return activity -> {
            if(mSpookyConnectionEnabled){
                mSpookyBoxReceiver.disconnect();
            }
        };
    }

    public Receiver<Activity> getOnResumeListener() {
        return activity -> {
            if(mSpookyConnectionEnabled){
                mSpookyBoxReceiver.connect();
            }
        };
    }
}
