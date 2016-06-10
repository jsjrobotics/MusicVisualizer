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

import com.spookyjohnson.musicvisualizer.functional.Receiver;
import com.spookyjohnson.musicvisualizer.inputStateMachine.RequestFromStream;

public class MainFragment extends android.app.Fragment {
    private SpookyBoxPresenter mSpookyBoxPresenter;

    private Button mConnect;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSpookyBoxPresenter = new SpookyBoxPresenter("http://10.89.196.6:8000", new Receiver<RequestFromStream>() {
            @Override
            public void accept(RequestFromStream data) {
                Log.e("MainFragment", "Received request from stream: "+data.mData[data.mData.length-1]);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_main, container, false);
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