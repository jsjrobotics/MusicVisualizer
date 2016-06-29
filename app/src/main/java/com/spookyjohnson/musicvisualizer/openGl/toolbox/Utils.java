package com.spookyjohnson.musicvisualizer.openGl.toolbox;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Utils {
    public static FloatBuffer createVertexArray(float[] coordinates) {
        ByteBuffer bb = ByteBuffer.allocateDirect(coordinates.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(coordinates);
        fb.position(0);
        return fb;
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public static int createProgram(String vertexShaderCode, String fragmentShaderCode) {
        int vertexShader = Utils.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = Utils.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);


        // create empty OpenGL ES Program
        int programHandle = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(programHandle, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(programHandle, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(programHandle);
        return programHandle;
    }
}
