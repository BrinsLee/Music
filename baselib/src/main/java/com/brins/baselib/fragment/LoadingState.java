package com.brins.baselib.fragment;


public class LoadingState {

    public LoadingState(int state) {
        this.state = state;
    }

    public static final int LOADING_STATE_SUCCESS = 0;
    public static final int LOADING_STATE_FAIL = 1;

    public int state;
}
