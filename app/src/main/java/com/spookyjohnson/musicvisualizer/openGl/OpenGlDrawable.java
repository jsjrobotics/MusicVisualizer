package com.spookyjohnson.musicvisualizer.openGl;

public interface OpenGlDrawable {
    void draw(float[] modelViewProjectionMatrix);
    String getVertexShaderCode();
    String getFragmentShaderCode();
}
