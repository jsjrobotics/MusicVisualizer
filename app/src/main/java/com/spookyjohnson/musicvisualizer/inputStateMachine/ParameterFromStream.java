package com.spookyjohnson.musicvisualizer.inputStateMachine;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ParameterFromStream {
    private static final int NO_SET_LENGTH = -1;
    public static final String END_DATA_TOKEN = "/END";
    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private final char[] mBuffer;
    private final int mValidLength;
    private int mIndex;
    private final List<char[]> mValidValues;

    public ParameterFromStream(){
        this(NO_SET_LENGTH, Collections.<char[]>emptyList());
    }
    public ParameterFromStream(int validValueLength, List<char[]> validValues) {
        if(validValueLength == NO_SET_LENGTH){
            mBuffer = new char[DEFAULT_BUFFER_SIZE];
        } else {
            mBuffer = new char[validValueLength];
        }
        mValidLength = validValueLength;
        mIndex = 0;
        for(char[] array : validValues){
            if(array.length != validValueLength){
                throw new IllegalArgumentException("Invalid length in valid values");
            }
        }
        mValidValues = validValues;
    }

    public char[] getBuffer(){
        return mBuffer;
    }

    public void clear(){
        mIndex = 0;
    }

    public boolean acceptChar(char input){
        if(mIndex < 0 || mIndex >= mBuffer.length){
            return false;
        }
        mBuffer[mIndex] = input;
        mIndex += 1;
        return true;
    }

    public boolean isValidParameter(){
        if(mValidLength == NO_SET_LENGTH){
            return isLastReceivedEndDataToken();
        }
        else {
            return isValidValueInBuffer();
        }
    }

    private boolean isValidValueInBuffer() {
        if(mIndex != mValidLength){
            return false;
        }
        for(char[] array : mValidValues){
            if(Arrays.equals(mBuffer, array)){
                return true;
            }
        }
        return false;
    }

    private boolean isLastReceivedEndDataToken() {
        if(mIndex < END_DATA_TOKEN.length()){
            return false;
        }
        StringBuilder lastTokens = new StringBuilder();
        for(int index = mIndex-END_DATA_TOKEN.length(); index < mIndex; index++){
            lastTokens.append(mBuffer[index]);
        }
        return lastTokens.toString().equals(END_DATA_TOKEN);
    }

    public static ParameterFromStream command() {
        List<char[]> v = getValidCommandValues();
        return new ParameterFromStream(v.get(0).length, v);
    }

    private static List<char[]> getValidCommandValues() {
        char[] command1 = {'t', 'e', 's', 't'};
        char[] command2 = {'n', 'o', 'n', 'e'};
        return Arrays.asList(
                command1,
                command2
        );
    }

    public static ParameterFromStream data() {
        return new ParameterFromStream();
    }

    public char[] getParsedData() {
        return Arrays.copyOfRange(mBuffer, 0, mIndex - END_DATA_TOKEN.length());
    }

    public void shiftLeft() {
        for(int index = 0; index < mBuffer.length-1; index++){
            mBuffer[index] = mBuffer[index+1];
        }
        mIndex--;
    }
}
