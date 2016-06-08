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

    /**
     * Returns true when all characters in the buffer are consumed
     * on failure, all characters in buffer are dropped.
     * True does not mean that a complete object was generated
     * @param length
     * @param buffer
     * @return
     */
    public Integer receiveInput(int length, char[] buffer) {
        if(mState != State.STOPPED){
            throw new IllegalArgumentException("Can't receive input unless in stopped state");
        }
        int received = 0;
        mState = nextState();
        for(int index = 0; index < length; index++){
            char in = buffer[index];
            switch(mState){
                case AWAITING_START:
                    if(!mCommandParameter.acceptChar(in)){
                        index--;
                        if(mCommandParameter.isValidParameter()){
                            mState = nextState();
                        } else {
                            mCommandParameter.shiftLeft();
                        }
                    }
                    break;
                case AWAITING_DATA:
                    if(!mDataParameter.acceptChar(in)){
                        reset();
                        return -1;
                    }
                    if(mDataParameter.isValidParameter()){
                        received++;
                        mCallback.accept(
                            new RequestFromStream(
                                    mCommandParameter.getBuffer(),
                                    mDataParameter.getParsedData()
                            )
                        );
                        mCommandParameter.clear();
                        mDataParameter.clear();
                        mState = State.AWAITING_START;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Default case reached in receive input");
            }
        }

        return received;
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
                return State.STOPPED;
            default:
                throw new IllegalArgumentException("Unknown state");
        }
    }
}
