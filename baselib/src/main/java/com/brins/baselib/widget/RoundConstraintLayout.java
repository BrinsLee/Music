
package com.brins.baselib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.brins.baselib.R;


public class RoundConstraintLayout extends ConstraintLayout {

    private Paint mMaskPaint;
    private Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private Bitmap mMaskBitmap;
    private final Paint mLayerPaint = new Paint();
    private static final int ROUND_RADIUS_DEFAULT = 4;
    private int mRadius;

    public RoundConstraintLayout(Context context) {
        this(context, null);
    }

    public RoundConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.RoundConstraintLayout);
        mRadius = a.getDimensionPixelSize(R.styleable.RoundConstraintLayout_round_corner, ROUND_RADIUS_DEFAULT);
        a.recycle();
    }

    private void init(Context context) {
        setWillNotDraw(false);
        mMaskPaint = new Paint();
        mMaskPaint.setAntiAlias(true);
        mMaskPaint.setFilterBitmap(true);
        mMaskPaint.setColor(ContextCompat.getColor(context, R.color.white));
    }

    private void updateMask() {
        if (mMaskBitmap != null && !mMaskBitmap.isRecycled()) {
            mMaskBitmap.recycle();
            mMaskBitmap = null;
        }
        try {
            mMaskBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                    Bitmap.Config.ARGB_8888);
        } catch (Throwable e) {

        }
        if (mMaskBitmap != null) {
            final Canvas canvas = new Canvas(mMaskBitmap);
            canvas.drawRoundRect(new RectF(0, 0, mMaskBitmap.getWidth(),
                    mMaskBitmap.getHeight()), mRadius, mRadius, mMaskPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateMask();
    }

    @Override
    public void draw(Canvas canvas) {
        if (mMaskBitmap != null) {
            final int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(),
                    mLayerPaint, Canvas.ALL_SAVE_FLAG);
            super.draw(canvas);
            mMaskPaint.setXfermode(mXfermode);
            canvas.drawBitmap(mMaskBitmap, 0, 0, mMaskPaint);
            mMaskPaint.setXfermode(null);
            canvas.restoreToCount(sc);
        } else {
            super.draw(canvas);
        }
    }


}
