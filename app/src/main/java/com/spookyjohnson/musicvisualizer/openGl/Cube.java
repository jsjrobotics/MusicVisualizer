package com.spookyjohnson.musicvisualizer.openGl;

import android.opengl.GLES20;

import com.spookyjohnson.musicvisualizer.openGl.toolbox.Utils;

import java.nio.FloatBuffer;

public class Cube implements OpenGlDrawable {
    private final FloatBuffer mVertexBuffer;
    private final int mProgram;

    private float deltaX = 0.5f;
    private float deltaY = 1f;
    private float deltaZ = 0.5f;

    static final int COORDINATES_PER_VERTEX = 3;
    // front face  0-1-3, 3-1-2
    // back face  4-5-7,  7-5-6
    // Left face  4-0-7, 7-0-3
    // Right face 1-5-2, 2-5-6
    // Top face 3-2-7, 7-2-6
    // Bottom Face 0-1-4, 4-1-5
    int[] mTriangles = {
            0,1,3,
            3,1,2,
            4,5,7,
            7,5,6,
            4,0,7,
            7,0,3,
            1,5,2,
            2,5,6,
            3,2,7,
            7,2,6,
            0,1,4,
            4,1,5
    };
    private float[] mVertexDefinitions = {
            0f, 0f, 0f,             // 0
            deltaX, 0f, 0f,         // 1
            deltaX, deltaY, 0f,     // 2
            0f, deltaY, 0f,         // 3
            0f, 0f, deltaZ,         // 4
            deltaX, 0f, deltaZ,     // 5
            deltaX, deltaY, deltaZ, // 6
            0f, deltaY, deltaZ      // 7
    };

    private float[] mVertexCoordinates = new float[mTriangles.length * COORDINATES_PER_VERTEX];


    private float[] vertex(int index) {
        float[] result = new float[3];
        int offset = index * 3;
        result[0] = mVertexDefinitions[offset];
        result[1] = mVertexDefinitions[offset+1];
        result[2] = mVertexDefinitions[offset+2];
        return result;
    }

    private final int mVertexCount = mVertexCoordinates.length / COORDINATES_PER_VERTEX;
    private final int mVertexStride = COORDINATES_PER_VERTEX * 4; // 4 bytes per vertex

    public Cube(){
        for(int index = 0; index < mTriangles.length; index++){
            addVertex(index, mTriangles[index] );
        }
        mVertexBuffer = Utils.createVertexArray(mVertexCoordinates);
        mProgram = Utils.createProgram(getVertexShaderCode(), getFragmentShaderCode());
    }

    private void addVertex(int addIndex, int vertexIndex) {
        float[] coordinates = vertex(vertexIndex);
        int offset = addIndex*COORDINATES_PER_VERTEX;
        mVertexCoordinates[offset] = coordinates[0];
        mVertexCoordinates[offset+1] = coordinates[1];
        mVertexCoordinates[offset+2] = coordinates[2];
    }

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };
    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        int positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, COORDINATES_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                mVertexStride, mVertexBuffer);

        // get handle to fragment shader's vColor member
        int colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        int mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, mVertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    @Override
    public String getVertexShaderCode() {
        return // This matrix member variable provides a hook to manipulate
                // the coordinates of the objects that use this vertex shader
                "uniform mat4 uMVPMatrix;" +
                        "attribute vec4 vPosition;" +
                        "void main() {" +
                        // the matrix must be included as a modifier of gl_Position
                        // Note that the uMVPMatrix factor *must be first* in order
                        // for the matrix multiplication product to be correct.
                        "  gl_Position = uMVPMatrix * vPosition;" +
                        "}";
    }

    @Override
    public String getFragmentShaderCode() {
        return "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}";
    }

}
