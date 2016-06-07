package com.spookyjohnson.musicvisualizer.inputStateMachine;

import java.util.Arrays;
import java.util.List;

public class InputStateMachine {
    private final ParameterFromStream mCommandParameter;

    private State mState;

    public InputStateMachine(){
        mCommandParameter = ParameterFromStream.command();
        mDataParameter = ParameterFromStream.data();
        mState = State.STOPPED;
    }

    public void receiveInput(int length, char[] buffer) {
        for(int index = 0; index < length; /* Do Nothing */){
            char in = buffer[index];
            switch(mState){
                case STOPPED:
                    throw new IllegalArgumentException("Can't receive input in stopped state");
                case AWAITING_START:
                    if(mCommandParameter.acceptChar(in)){
                        index++;
                    } else {
                        mState = nextState();
                    }
                default:
                    throw new IllegalArgumentException("Default case reached in receive input");
            }
        }
    }

    private State nextState() {
        switch(mState){
            case STOPPED:
                return State.STOPPED;
            case AWAITING_START:
                return State.AWAITING_DATA;
            default:
                throw new IllegalArgumentException("Unknown state");
        }
    }
}
