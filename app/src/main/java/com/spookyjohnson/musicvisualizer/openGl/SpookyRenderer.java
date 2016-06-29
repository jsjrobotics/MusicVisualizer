package com.spookyjohnson.musicvisualizer.openGl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SpookyRenderer implements GLSurfaceView.Renderer {

    private static final float INCREMENT_FACTOR = 0.1F;
    private final boolean mSpookyConnectionEnabled;
    private Triangle mTriangle;
    private Cube mCube;
    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];
    private volatile float mAngle = 0.0f;
    private volatile float mEyeXPosition = 0f;

    public SpookyRenderer(boolean spookyConnectionEnabled) {
        mSpookyConnectionEnabled = spookyConnectionEnabled;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        mTriangle = new Triangle();
        mCube = new Cube();

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // Set the camera position (View matrix)
        if(mSpookyConnectionEnabled){
            Matrix.setLookAtM(mViewMatrix, 0, 0, 0f, -4f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        } else {
            Matrix.setLookAtM(mViewMatrix, 0, mEyeXPosition, 0f, -4f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
            mEyeXPosition = (mEyeXPosition + .01f) % 4;
        }
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        float[] scratch = new float[16];
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        mCube.draw(scratch);
    }

    public void incrementAngle() {
        mAngle = mAngle + INCREMENT_FACTOR;
    }

    public void decrementAngle() {
        mAngle = mAngle - INCREMENT_FACTOR;
    }
}
