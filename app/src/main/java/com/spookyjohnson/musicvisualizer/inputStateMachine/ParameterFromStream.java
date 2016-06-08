package com.spookyjohnson.musicvisualizer.inputStateMachine;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ParameterFromStream {
    private static final int NO_SET_LENGTH = -1;
    public static final String END_DATA_TOKEN = "/END";
    private final char[] mBuffer;
    private final int mValidLength;
    private int mIndex;
    private final List<char[]> mValidValues;

    public ParameterFromStream(int validValueLength, List<char[]> validValues) {
        mBuffer = new char[validValueLength];
        mValidLength = validValueLength;
        mIndex = 0;
        for(char[] array : validValues){
            if(array.length != validValueLength){
                throw new IllegalArgumentException("Invalid length in valid values");
            }
        }
        mValidValues = validValues;
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

    public boolean checkValidParameter(){
        if(mValidLength == NO_SET_LENGTH){
            if(mIndex < END_DATA_TOKEN.length()){
                return false;
            }
            StringBuilder lastTokens = new StringBuilder();
            for(int index = mIndex-END_DATA_TOKEN.length(); index < mIndex; index++){
                lastTokens.append(index);
            }
            if(lastTokens.toString().equals(END_DATA_TOKEN)){
                return true;
            }
            return false;
        }
        else if(mIndex != mValidLength){
            return false;
        }
        for(char[] array : mValidValues){
            if(Arrays.equals(mBuffer, array)){
                return true;
            }
        }
        return false;
    }

    public static ParameterFromStream command() {
        List<char[]> v = getValidCommandValues();
        return new ParameterFromStream(v.get(0).length, v);
    }

    private static List<char[]> getValidCommandValues() {
        char[] command1 = {'c', 'o', 'm', 'm'};
        char[] command2 = {};
        return Arrays.asList(
                command1,
                command2
        );
    }

    public static ParameterFromStream data() {
        return new ParameterFromStream(1024, null);
    }
}
