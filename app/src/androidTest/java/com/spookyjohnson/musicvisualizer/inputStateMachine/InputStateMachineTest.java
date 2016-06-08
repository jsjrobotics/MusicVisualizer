package com.spookyjohnson.musicvisualizer.inputStateMachine;

import com.spookyjohnson.musicvisualizer.functional.Receiver;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputStateMachineTest extends TestCase{

    private InputStateMachine mTestSubject;

    public char[] listToArray(List<Character> in){
        char[] result = new char[in.size()];
        for(int index = 0; index < result.length; index++){
            result[index] = in.get(index);
        }
        return result;
    }

    public char[] appendArrays(char[] first, char[] second){
        char[] result = new char[first.length + second.length];
        int resultIndex = 0;
        for(int index = 0; index < first.length; index++){
            result[resultIndex] = first[index];
            resultIndex++;
        }
        for(int index = 0; index < second.length; index++){
            result[resultIndex] = second[index];
            resultIndex++;
        }
        return result;
    }

    public void testReceiveSingleInput(){
        final char[] command = {'t','e','s','t'};
        final char[] data = {'h','e','l','p'};
        char[] testData = appendArrays(
                appendArrays(command, data),
                ParameterFromStream.END_DATA_TOKEN.toCharArray()
        );
        mTestSubject = new InputStateMachine(new Receiver<RequestFromStream>() {
            @Override
            public void accept(RequestFromStream received) {
                assertTrue(Arrays.equals(received.mCommand, command));
                assertTrue(Arrays.equals(received.mData, data));
            }
        });
        Integer received = mTestSubject.receiveInput(testData.length, testData);
        assertEquals(Integer.valueOf(1), received);
    }

    public void testReceiveDualInput(){
        final char[] test1 = {'t','e','s','t','h','e','l','p','1','/','E','N','D'};
        final char[] test2 = {'t','e','s','t','h','e','l','p','2','/','E','N','D'};
        char[] testData = appendArrays(test1, test2);
        mTestSubject = new InputStateMachine(new Receiver<RequestFromStream>() {
            int receivedObjects = 0;
            @Override
            public void accept(RequestFromStream received) {
                char[] command = {'t','e','s','t'};
                assertTrue(Arrays.equals(received.mCommand, command));
                if(receivedObjects == 0){
                    char[] data = {'h','e','l','p','1'};
                    assertTrue(Arrays.equals(received.mData, data));
                    receivedObjects++;
                } else if(receivedObjects == 1){
                    char[] data = {'h','e','l','p','2'};
                    assertTrue(Arrays.equals(received.mData, data));
                    receivedObjects++;
                }
            }
        });
        assertEquals(Integer.valueOf(2), mTestSubject.receiveInput(testData.length, testData));
    }

    public void testReceiveDualInputWithNoise(){
        final char[] test1 = {'t','e','s','t','h','e','l','p','1','/','E','N','D','r','r','r'};
        final char[] test2 = {'t','e','s','t','h','e','l','p','2','/','E','N','D'};
        char[] testData = appendArrays(test1, test2);
        mTestSubject = new InputStateMachine(new Receiver<RequestFromStream>() {
            int receivedObjects = 0;
            @Override
            public void accept(RequestFromStream received) {
                char[] command = {'t','e','s','t'};
                assertTrue(Arrays.equals(received.mCommand, command));
                if(receivedObjects == 0){
                    char[] data = {'h','e','l','p','1'};
                    assertTrue(Arrays.equals(received.mData, data));
                    receivedObjects++;
                } else if(receivedObjects == 1){
                    char[] data = {'h','e','l','p','2'};
                    assertTrue(Arrays.equals(received.mData, data));
                    receivedObjects++;
                }
            }
        });
        assertEquals(Integer.valueOf(2), mTestSubject.receiveInput(testData.length, testData));
    }
}
