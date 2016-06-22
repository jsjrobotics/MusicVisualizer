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

package com.spookyjohnson.musicvisualizer.defaultDisplay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.spookyjohnson.musicvisualizer.kinect.SpookyBoxPresenter;
import com.spookyjohnson.musicvisualizer.kinect.SpookyBoxView;

public class MainFragment extends android.app.Fragment {
    private SpookyBoxPresenter mSpookyBoxPresenter;

    private Button mConnect;
    private SpookyBoxView mView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mView = new SpookyBoxView(getActivity(), inflater, container);
        return mView.getLayout();
    }


    @Override
    public void onResume(){
        super.onResume();
        mSpookyBoxPresenter = new SpookyBoxPresenter(mView, "http://192.168.1.11:8000");
    }

}