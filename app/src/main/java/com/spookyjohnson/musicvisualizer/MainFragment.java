/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.spookyjohnson.musicvisualizer;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.spookyjohnson.musicvisualizer.functional.Receiver;
import com.spookyjohnson.musicvisualizer.inputStateMachine.ParameterFromStream;
import com.spookyjohnson.musicvisualizer.inputStateMachine.RequestFromStream;

import java.util.Arrays;

public class MainFragment extends android.app.Fragment {
    private SpookyBoxPresenter mSpookyBoxPresenter;

    private Button mConnect;
    private GridView mGridView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSpookyBoxPresenter = new SpookyBoxPresenter("http://192.168.1.11:8000", new Receiver<RequestFromStream>() {
            @Override
            public void accept(RequestFromStream data) {
                if(ParameterFromStream.equalsNoneCommand(data)){
                    Log.e("MainFragment", "Received none command");
                } else if(ParameterFromStream.equalsTestCommand(data)){
                    String dataBuffer = String.valueOf(data.mData);
                    int[][] downscaledMatrix = readDownscaledMatrix(dataBuffer);
                    while(downscaledMatrix != null){
                        int endIndex = dataBuffer.indexOf("/END");
                        dataBuffer = dataBuffer.substring(endIndex+4);
                        drawDownscaledMatrix(downscaledMatrix);
                        downscaledMatrix = readDownscaledMatrix(dataBuffer);
                    }

                    Log.e("MainFragment", "Received request from stream: "+data.mData[data.mData.length-1]);
                }
            }
        });
    }

    private void drawDownscaledMatrix(final int[][] downscaledMatrix) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGridView.setAdapter(new GridViewAdapter(getActivity(),downscaledMatrix));
                mGridView.invalidate();
            }
        });
    }

    private int[][] readDownscaledMatrix(String input) {
        if(input == null || input.isEmpty()){
            return null;
        }
        String[] tokens = input.split(":");
        if(!tokens[0].equals("DEPTH")){
            System.out.println("Expected a depth stream");
            return null;
        }
        int height = Integer.valueOf(tokens[1].replace("Height", ""));
        int width = Integer.valueOf(tokens[2].replace("Width", ""));
        int[][] result = new int[height][width];
        for(int yIndex = 0; yIndex < height; yIndex++){
            int yOffset = yIndex * width;
            for(int xIndex = 3; xIndex < width; xIndex++){
                result[yIndex][xIndex] = (int) Long.parseLong(tokens[yOffset + xIndex],16);
            }
        }
        return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mGridView = (GridView) view.findViewById(R.id.matrix_holder);
        return view;
    }


    @Override
    public void onResume(){
        super.onResume();
        mConnect = (Button) getView().findViewById(R.id.connect);
        mConnect.setOnClickListener(mSpookyBoxPresenter.getClickListener());
        mConnect.setBackgroundColor(Color.GREEN);
    }

}