package com.brins.baselib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.brins.baselib.R;

import static com.brins.baselib.utils.SizeUtils.dp2px;

/**
 * Created by lipeilin
 * on 2020/11/16
 */
public class TagFlowLayout extends FlowLayout implements TagAdapter.OnDataChangedListener {

    private static final String TAG = "TagFlowLayout";
    private OnTagClickListener mOnTagClickListener;
    private TagAdapter mTagAdapter;
    private int mShowMax = -1;


    public interface OnTagClickListener {
        boolean onTagClick(View view, int position, FlowLayout parent);
    }

    public TagFlowLayout(Context context) {
        this(context, null);
    }

    public TagFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagFlowLayout);
        mShowMax = ta.getInt(R.styleable.TagFlowLayout_max_show, -1);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            TagView tagView = (TagView) getChildAt(i);
            if (tagView.getVisibility() == View.GONE) {
                continue;
            }
            if (tagView.getTagView().getVisibility() == View.GONE) {
                tagView.setVisibility(View.GONE);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        mOnTagClickListener = onTagClickListener;
    }

    public void setAdapter(TagAdapter adapter) {
        mTagAdapter = adapter;
        mTagAdapter.setOnDataChangedListener(this);
        changeAdapter(mShowMax > 0 ? Math.min(adapter.getCount(), mShowMax) : adapter.getCount());
    }

    private void changeAdapter(final int showNum) {
        removeAllViews();
        final TagAdapter adapter = mTagAdapter;
        TagView tagViewContainer = null;
        for (int i = 0; i <= showNum; i++) {
            View tagView;
            if (i == showNum) {
                ImageView imageView = new ImageView(getContext());
                imageView.setImageResource(showNum == adapter.getCount() ? R.drawable.base_icon_back_up_black : R.drawable.base_icon_back_down_black);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setBackgroundResource(R.drawable.base_bg_circle);
                tagView = imageView;
                if (adapter.getCount() != 0 && adapter.getCount() > mShowMax) {
                    tagView.setVisibility(VISIBLE);
                } else {
                    tagView.setVisibility(GONE);
                }

            } else {
                tagView = adapter.getView(this, i, adapter.getItem(i));
                tagView.setClickable(false);
            }
            tagViewContainer = new TagView(getContext());
            tagView.setDuplicateParentStateEnabled(true);
            if (tagView.getLayoutParams() != null) {
                tagViewContainer.setLayoutParams(tagView.getLayoutParams());

            } else {
                MarginLayoutParams lp = new MarginLayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                lp.setMargins(dp2px(5),
                        dp2px(5),
                        dp2px(5),
                        dp2px(5));
                tagViewContainer.setLayoutParams(lp);

            }
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            tagView.setLayoutParams(lp);
            tagViewContainer.addView(tagView);
            final TagView finalTagViewContainer = tagViewContainer;
            final int position = i;
            addView(tagViewContainer);
            tagViewContainer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnTagClickListener != null && position != showNum) {
                        mOnTagClickListener.onTagClick(finalTagViewContainer, position,
                                TagFlowLayout.this);
                    } else {
                        changeAdapter(showNum == adapter.getCount() ? Math.min(mShowMax, adapter.getCount()) : adapter.getCount());
                    }
                }
            });
        }
    }

    public TagAdapter getAdapter() {
        return mTagAdapter;
    }


    @Override
    public void onChanged() {
        changeAdapter(mShowMax > 0 ? Math.min(mTagAdapter.getCount(), mShowMax) : mTagAdapter.getCount());
    }

}
