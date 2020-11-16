package com.brins.baselib.widget;

import android.view.View;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class TagAdapter<T> {
    private List<T> mTagDatas;
    private OnDataChangedListener mOnDataChangedListener;

    public TagAdapter(List<T> datas) {
        mTagDatas = datas;
    }

    public TagAdapter(T[] datas) {
        mTagDatas = new ArrayList<T>(Arrays.asList(datas));
    }

    interface OnDataChangedListener {
        void onChanged();
    }

    void setOnDataChangedListener(OnDataChangedListener listener) {
        mOnDataChangedListener = listener;
    }


    public int getCount() {
        return mTagDatas == null ? 0 : mTagDatas.size();
    }


    public T getItem(int position) {
        return mTagDatas.get(position);
    }

    public abstract View getView(FlowLayout parent, int position, T t);

    public void addNewData(List<T> datas) {
        mTagDatas.addAll(datas);
        if (mOnDataChangedListener != null) {
            mOnDataChangedListener.onChanged();
        }
    }

    public void addNewData(T[] datas) {
        mTagDatas.addAll(Arrays.asList(datas));
        if (mOnDataChangedListener != null) {
            mOnDataChangedListener.onChanged();
        }
    }

    public void addNewData(T data) {
        mTagDatas.add(data);
        if (mOnDataChangedListener != null) {
            mOnDataChangedListener.onChanged();
        }
    }

    public void clear() {
        mTagDatas.clear();
        if (mOnDataChangedListener != null) {
            mOnDataChangedListener.onChanged();
        }
    }
}
