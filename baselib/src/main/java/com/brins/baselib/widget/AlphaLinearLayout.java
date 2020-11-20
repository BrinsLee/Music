package com.brins.baselib.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * Created by lipeilin
 * on 2020/11/18
 */

public class AlphaLinearLayout extends LinearLayout {

    private ObjectAnimator mAnimatorOut;
    private boolean mIsClick = false;

    public AlphaLinearLayout(Context context) {
        super(context);
        init();
    }

    public AlphaLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlphaLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AlphaLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mAnimatorOut = ObjectAnimator.ofFloat(this, "alpha", 0.6f, 1f);
        mAnimatorOut.setDuration(100);
    }
    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        if (l == null) {
            mIsClick = false;
        } else {
            mIsClick = true;
        }
        super.setOnClickListener(l);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsClick && isEnabled()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    this.setAlpha(0.6f);
                    break;
                case MotionEvent.ACTION_UP:
                    mAnimatorOut.start();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    mAnimatorOut.start();
                    break;
            }
        }
        return super.onTouchEvent(event);
    }
}
