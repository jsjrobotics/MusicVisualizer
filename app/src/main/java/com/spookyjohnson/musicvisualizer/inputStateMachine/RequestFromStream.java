package com.spookyjohnson.musicvisualizer.inputStateMachine;

public class RequestFromStream {
    public final char[] mCommand;
    public final char[] mData;

    public RequestFromStream(char[] command, char[] data) {
        mCommand = command;
        mData = data;
    }
}
