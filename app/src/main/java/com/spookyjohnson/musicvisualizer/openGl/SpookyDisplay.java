package com.spookyjohnson.musicvisualizer.openGl;

import android.app.Activity;
import android.opengl.GLSurfaceView;

public class SpookyDisplay extends GLSurfaceView {
    private final Activity mActivity;
    private final SpookyRenderer mRenderer;

    public SpookyDisplay(Activity activity) {
        super(activity);
        mActivity = activity;
        setEGLContextClientVersion(2);
        mRenderer = new SpookyRenderer();
        setRenderer(mRenderer);
    }
}
