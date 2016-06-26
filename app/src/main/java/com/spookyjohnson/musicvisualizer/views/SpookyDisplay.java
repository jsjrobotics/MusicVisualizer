package com.spookyjohnson.musicvisualizer.views;

import android.app.Activity;
import android.opengl.GLSurfaceView;

import com.spookyjohnson.musicvisualizer.functional.LifecycleActivity;
import com.spookyjohnson.musicvisualizer.functional.Receiver;
import com.spookyjohnson.musicvisualizer.kinect.MotionToInput;
import com.spookyjohnson.musicvisualizer.kinect.SpookyBoxReceiver;
import com.spookyjohnson.musicvisualizer.openGl.OpenGlPresenter;

public class SpookyDisplay extends GLSurfaceView {

    private final OpenGlPresenter mOpenGlPresenter;

    public SpookyDisplay(LifecycleActivity activity) {
        super(activity);
        mOpenGlPresenter = new OpenGlPresenter();
        setEGLContextClientVersion(2);
        setRenderer(mOpenGlPresenter.getRenderer());
        activity.getLifecycle().addOnResumeListener(mOpenGlPresenter.getOnResumeListener());
        activity.getLifecycle().addOnPauseListener(mOpenGlPresenter.getOnPauseListener());
    }

}
