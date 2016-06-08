package com.spookyjohnson.musicvisualizer.inputStateMachine;

import com.spookyjohnson.musicvisualizer.functional.Receiver;

public class InputStateMachine {
    private final ParameterFromStream mCommandParameter;
    private final ParameterFromStream mDataParameter;
    private final Receiver<RequestFromStream> mCallback;

    private State mState;

    public InputStateMachine(Receiver<RequestFromStream> callback){
        mCallback = callback;
        mCommandParameter = ParameterFromStream.command();
        mDataParameter = ParameterFromStream.data();
        mState = State.STOPPED;
    }

    public boolean receiveInput(int length, char[] buffer) {
        if(mState != State.STOPPED){
            throw new IllegalArgumentException("Can't receive input unless in stopped state");
        }
        mState = nextState();
        for(int index = 0; index < length; index++){
            char in = buffer[index];
            switch(mState){
                case STOPPED:
                    return true;
                case AWAITING_START:
                    if(!mCommandParameter.acceptChar(in)){
                        index--;
                        mState = nextState();
                        if(!mCommandParameter.isValidParameter()){
                            reset();
                            return false;
                        }
                    }
                    break;
                case AWAITING_DATA:
                    if(!mDataParameter.acceptChar(in)){
                        reset();
                        return false;
                    }
                    if(mDataParameter.isValidParameter()){
                        break;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Default case reached in receive input");
            }
        }
        if(!mDataParameter.isValidParameter()){
            reset();
            return false;
        }
        mState = nextState();
        if(mState != State.PROCESSING){
            reset();
            return false;
        }
        mCallback.accept(
                new RequestFromStream(
                        mCommandParameter.getBuffer(),
                        mDataParameter.getParsedData()
                )
        );
        mState = nextState();
        return true;
    }

    private void reset() {
        mDataParameter.clear();
        mCommandParameter.clear();
        mState = State.STOPPED;
    }

    private State nextState() {
        switch(mState){
            case STOPPED:
                return State.AWAITING_START;
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
