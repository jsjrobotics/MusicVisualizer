package com.spookyjohnson.musicvisualizer.inputStateMachine;

import com.spookyjohnson.musicvisualizer.functional.Receiver;

import junit.framework.TestCase;

import java.util.Arrays;

public class InputStateMachineTest extends TestCase{

    private InputStateMachine mTestSubject;

    public void testReceiveInput(){
        final char[] command = {'t','e','s','t'};
        final char[] data = {'h','e','l','p'};
        String endToken = ParameterFromStream.END_DATA_TOKEN;
        char[] testData = new char[
                command.length +
                data.length +
                endToken.length() ];
        int testIndex = 0;
        for(int index = 0; index < command.length; index++){
            testData[testIndex] = command[index];
            testIndex++;
        }
        for(int index = 0; index < data.length; index++){
            testData[testIndex] = data[index];
            testIndex++;
        }
        for(int index = 0; index < endToken.length(); index++){
            testData[testIndex] = endToken.charAt(index);
            testIndex++;
        }
        mTestSubject = new InputStateMachine(new Receiver<RequestFromStream>() {
            @Override
            public void accept(RequestFromStream received) {
                assertTrue(Arrays.equals(received.mCommand, command));
                assertTrue(Arrays.equals(received.mData, data));
            }
        });
        assertTrue(mTestSubject.receiveInput(testData.length, testData));
    }
}
