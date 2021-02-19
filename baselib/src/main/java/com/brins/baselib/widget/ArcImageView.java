package com.brins.baselib.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.brins.baselib.R;

/**
 * Created by lipeilin
 * on 2021/2/10
 */
public class ArcImageView extends AppCompatImageView {
    private int mArcHeight;

    public ArcImageView(@NonNull Context context) {
        this(context, null);
    }

    public ArcImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ArcImageView);
        mArcHeight = array.getDimensionPixelSize(R.styleable.ArcImageView_image_arc_height, 0);
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(0, getHeight() - mArcHeight);
        path.quadTo(getWidth() / 2, getHeight(), getWidth(), getHeight() - mArcHeight);
        path.lineTo(getWidth(), 0);
        path.close();
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
