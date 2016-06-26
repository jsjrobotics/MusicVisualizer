package com.spookyjohnson.musicvisualizer.functional;

import android.app.Activity;
import android.app.Fragment;

public abstract class LifecycleFragment extends Fragment{
    protected FragmentLifecycleManager mLifecycleManager = new FragmentLifecycleManager();

    public final FragmentLifecycleManager getLifecycle(){
        return mLifecycleManager;
    }

    @Override
    public void onStart(){
        super.onStart();
        mLifecycleManager.notifyOnStart(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        mLifecycleManager.notifyOnResume(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        mLifecycleManager.notifyOnPause(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        mLifecycleManager.notifyOnStop(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mLifecycleManager.notifyOnDestroy(this);
    }
}
