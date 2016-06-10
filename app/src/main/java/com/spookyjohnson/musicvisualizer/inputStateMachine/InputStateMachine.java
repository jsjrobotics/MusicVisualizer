package com.spookyjohnson.musicvisualizer.inputStateMachine;

import android.util.Log;

import com.spookyjohnson.musicvisualizer.functional.Receiver;

/**
 * InputStateMachine expects to receive data from a constantly polled input stream.
 * Will search the stream input for a command and data pair
 */
public class InputStateMachine {
    private static final String TAG = "InputStateMachine";
    private final ParameterFromStream mCommandParameter;
    private final ParameterFromStream mDataParameter;
    private final Receiver<RequestFromStream> mCallback;

    private State mState;

    public InputStateMachine(Receiver<RequestFromStream> callback){
        mCallback = callback;
        mCommandParameter = ParameterFromStream.command();
        mDataParameter = ParameterFromStream.data();
        mState = State.AWAITING_START;
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
        int received = 0;
        for(int index = 0; index < length; index++){
            char in = buffer[index];
            switch(mState){
                case AWAITING_START:
                    // Command parameter accepts input until it is time to check if valid
                    // If not valid, shift left to make room for input
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
                    // If data parameter doesn't accept input, overflow has occured
                    if(!mDataParameter.acceptChar(in)){
                        Log.e(TAG, "data parameter didn't accept data");
                        reset();
                        return -1;
                    }
                    // If is valid, notify callback and return to start state
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
                        mState = nextState();
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
        mState = State.AWAITING_START;
    }

    private State nextState() {
        switch(mState){
            case AWAITING_START:
                return State.AWAITING_DATA;
            case AWAITING_DATA:
                return State.AWAITING_START;
            default:
                throw new IllegalArgumentException("Unknown state");
        }
    }
}
