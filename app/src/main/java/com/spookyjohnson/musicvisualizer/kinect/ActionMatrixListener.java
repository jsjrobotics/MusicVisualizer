package com.spookyjohnson.musicvisualizer.kinect;

public abstract class ActionMatrixListener {
    abstract boolean matches(int[][] input);
    abstract void execute();
    public final void accept(int[][] input){
        if(matches(input)){
            execute();
        }
    }
}
