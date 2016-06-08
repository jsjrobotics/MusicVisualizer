package com.spookyjohnson.musicvisualizer.functional;

public interface Receiver<T> {

    public void accept(T data);
}
