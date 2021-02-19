package com.brins.baselib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brins.baselib.R;
import com.brins.baselib.utils.UIUtils;

/**
 * Created by lipeilin
 * on 2021/2/18
 */
public class StickHeaderDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    int mBannerHeight = (int) UIUtils.getDimenSize(R.dimen.size_750_88px);
    private View mStickView;

    public StickHeaderDecoration(Context context) {
        this.mContext = context;
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            int pos = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
            View child = parent.findViewHolderForAdapterPosition(pos).itemView;
            int top = child.getTop();
            Log.d("StickHeaderDecoration", top + "");
            if (pos == 0 && mStickView == null) {
                mStickView = child.findViewWithTag("stick_header");
            }
            if ((top < 0 || pos > 0) && mStickView != null) {
                drawHeader(mStickView, c);
            }
        }
    }

    private void drawHeader(View stickView, Canvas c) {
//        c.translate(0, 30);
        stickView.draw(c);
    }
}
