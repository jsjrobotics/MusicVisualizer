package com.spookyjohnson.musicvisualizer.inputStateMachine;

import java.util.Arrays;
import java.util.List;

public class InputStateMachine {
    private final ParameterFromStream mCommandParameter;
    private final ParameterFromStream mDataParameter;

    private State mState;

    public InputStateMachine(){
        mCommandParameter = ParameterFromStream.command();
        mDataParameter = ParameterFromStream.data();
        mState = State.STOPPED;
    }

    public boolean receiveInput(int length, char[] buffer) {
        if(mState != State.STOPPED){
            throw new IllegalArgumentException("Can't receive input unless in stopped state");
        }
        mState = nextState();
        for(int index = 0; index < length; /* Do Nothing */){
            char in = buffer[index];
            switch(mState){
                case STOPPED:
                    return true;
                case AWAITING_START:
                    if(mCommandParameter.acceptChar(in)){
                        index++;
                    } else {
                        if(!mCommandParameter.checkValidParameter()){
                            mCommandParameter.clear();
                            return false;
                        }
                        mState = nextState();
                    }
                case AWAITING_DATA:
                    return false;
                case PROCESSING:
                    return false;
                default:
                    throw new IllegalArgumentException("Default case reached in receive input");
            }
        }
        return true;
    }

    private State nextState() {
        switch(mState){
            case STOPPED:
                return State.STOPPED;
            case AWAITING_START:
                return State.AWAITING_DATA;
            case AWAITING_DATA:
                return State.PROCESSING;
            case PROCESSING:
                return State.STOPPED;
            default:
                throw new IllegalArgumentException("Unknown state");
        }
    }
}
