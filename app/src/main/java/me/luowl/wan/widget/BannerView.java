package me.luowl.wan.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import me.luowl.wan.R;

public class BannerView extends FrameLayout {

    private static final int DEFAULT_SELECTED_COLOR = 0xffffffff;
    private static final int DEFAULT_UNSELECTED_COLOR = 0x50ffffff;

    private int mInterval;
    private boolean isShowIndicator;
    private Drawable mSelectedDrawable;
    private Drawable mUnselectedDrawable;
    private int mSize;
    private int mSpace;

    private ViewPager2 mViewPager;
    private LinearLayout mLinearLayout;
    private TextView mTitleTextView;

    private RecyclerAdapter adapter;
    private OnRvBannerClickListener onRvBannerClickListener;
    private OnSwitchRvBannerListener onSwitchRvBannerListener;
    OnSwitchRvBannerTitleListener onSwitchRvBannerTitleListener;
    private List<Object> mData = new ArrayList<>();
    private int startX, startY;
    private boolean isPlaying;
    private Handler handler = new Handler();
    private boolean isAutoPlaying;

    private Runnable playTask = new Runnable() {

        @Override
        public void run() {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            handler.postDelayed(this, mInterval);
        }
    };

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BannerView);
        mInterval = a.getInt(R.styleable.BannerView_rvb_interval, 5000);
        isShowIndicator = a.getBoolean(R.styleable.BannerView_rvb_showIndicator, true);
        isAutoPlaying = a.getBoolean(R.styleable.BannerView_rvb_autoPlaying, true);
        Drawable sd = a.getDrawable(R.styleable.BannerView_rvb_indicatorSelectedSrc);
        Drawable usd = a.getDrawable(R.styleable.BannerView_rvb_indicatorUnselectedSrc);
        if (sd == null) {
            mSelectedDrawable = generateDefaultDrawable(DEFAULT_SELECTED_COLOR);
        } else {
            if (sd instanceof ColorDrawable) {
                mSelectedDrawable = generateDefaultDrawable(((ColorDrawable) sd).getColor());
            } else {
                mSelectedDrawable = sd;
            }
        }
        if (usd == null) {
            mUnselectedDrawable = generateDefaultDrawable(DEFAULT_UNSELECTED_COLOR);
        } else {
            if (usd instanceof ColorDrawable) {
                mUnselectedDrawable = generateDefaultDrawable(((ColorDrawable) usd).getColor());
            } else {
                mUnselectedDrawable = usd;
            }
        }
        mSize = a.getDimensionPixelSize(R.styleable.BannerView_rvb_indicatorSize, 0);
        mSpace = a.getDimensionPixelSize(R.styleable.BannerView_rvb_indicatorSpace, dp2px(4));
        int margin = a.getDimensionPixelSize(R.styleable.BannerView_rvb_indicatorMargin, dp2px(8));
        int g = a.getInt(R.styleable.BannerView_rvb_indicatorGravity, 1);
        int gravity;
        if (g == 0) {
            gravity = GravityCompat.START;
        } else if (g == 2) {
            gravity = GravityCompat.END;
        } else {
            gravity = Gravity.CENTER;
        }
        a.recycle();

        mViewPager = new ViewPager2(context);
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (isShowIndicator) {
                    switchIndicator();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        adapter = new RecyclerAdapter();
        mViewPager.setAdapter(adapter);
        LayoutParams vpLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mViewPager, vpLayoutParams);

        mLinearLayout = new LinearLayout(context);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setGravity(Gravity.CENTER);
        LayoutParams linearLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.gravity = Gravity.BOTTOM | gravity;
        linearLayoutParams.setMargins(margin, margin, margin, margin);
        addView(mLinearLayout, linearLayoutParams);

        mTitleTextView = new TextView(context);
        mTitleTextView.setTextSize(14);
        mTitleTextView.setTextColor(getResources().getColor(R.color.white));
        mTitleTextView.setBackgroundColor(getResources().getColor(R.color.black_transparent));
        mTitleTextView.setPadding(dp2px(10), margin, dp2px(10), margin);
        LayoutParams titleLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        titleLayoutParams.gravity = Gravity.BOTTOM;

        addView(mTitleTextView, titleLayoutParams);

        // 便于在xml中编辑时观察，运行时不执行
        if (isInEditMode()) {
            for (int i = 0; i < 3; i++) {
                mData.add("");
            }
            createIndicators();
        }
    }

    /**
     * 默认指示器是一系列直径为6dp的小圆点
     */
    private GradientDrawable generateDefaultDrawable(int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setSize(dp2px(6), dp2px(6));
        gradientDrawable.setCornerRadius(dp2px(6));
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }

    /**
     * 设置是否显示指示器导航点
     *
     * @param show 显示
     */
    public void isShowIndicator(boolean show) {
        this.isShowIndicator = show;
    }

    /**
     * 设置轮播间隔时间
     *
     * @param millisecond 时间毫秒
     */
    public void setIndicatorInterval(int millisecond) {
        this.mInterval = millisecond;
    }

    /**
     * 设置是否自动播放（上锁）
     *
     * @param playing 开始播放
     */
    private synchronized void setPlaying(boolean playing) {
        if (isAutoPlaying) {
            if (!isPlaying && playing && adapter != null && adapter.getItemCount() > 2) {
                handler.postDelayed(playTask, mInterval);
                isPlaying = true;
            } else if (isPlaying && !playing) {
                handler.removeCallbacksAndMessages(null);
                isPlaying = false;
            }
        }
    }

    /**
     * 设置是否禁止滚动播放
     *
     * @param isAutoPlaying true  是自动滚动播放,false 是禁止自动滚动
     */
    public void setRvAutoPlaying(boolean isAutoPlaying) {
        this.isAutoPlaying = isAutoPlaying;
    }

    /**
     * 设置轮播数据集
     *
     * @param data Banner对象列表
     */
    public void setRvBannerData(List data) {
        setPlaying(false);
        mData.clear();
        if (data != null) {
            mData.addAll(data);
        }
        if (mData.size() > 1) {
            adapter.notifyDataSetChanged();
            int currentIndex = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % mData.size();
            // 将起始点设为最靠近的 MAX_VALUE/2 的，且为mData.size()整数倍的位置
            mViewPager.setCurrentItem(currentIndex);
            if (isShowIndicator) {
                createIndicators();
            }
            updateTitle();
            setPlaying(true);
        } else {
            adapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(0);
            updateTitle();
        }
    }

    /**
     * 指示器整体由数据列表容量数量的AppCompatImageView均匀分布在一个横向的LinearLayout中构成
     * 使用AppCompatImageView的好处是在Fragment中也使用Compat相关属性
     */
    private void createIndicators() {
        mLinearLayout.removeAllViews();
        for (int i = 0; i < mData.size(); i++) {
            AppCompatImageView img = new AppCompatImageView(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.leftMargin = mSpace / 2;
            lp.rightMargin = mSpace / 2;
            if (mSize >= dp2px(4)) { // 设置了indicatorSize属性
                lp.width = lp.height = mSize;
            } else {
                // 如果设置的resource.xml没有明确的宽高，默认最小2dp，否则太小看不清
                img.setMinimumWidth(dp2px(2));
                img.setMinimumHeight(dp2px(2));
            }
            img.setImageDrawable(i == 0 ? mSelectedDrawable : mUnselectedDrawable);
            mLinearLayout.addView(img, lp);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //手动触摸的时候，停止自动播放，根据手势变换
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                int disX = moveX - startX;
                int disY = moveY - startY;
                boolean hasMoved = 2 * Math.abs(disX) > Math.abs(disY);
                getParent().requestDisallowInterceptTouchEvent(hasMoved);
                if (hasMoved) {
                    setPlaying(false);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!isPlaying) {
                    setPlaying(true);
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setPlaying(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setPlaying(false);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        if (visibility == GONE || visibility == INVISIBLE) {
            // 停止轮播
            setPlaying(false);
        } else if (visibility == VISIBLE) {
            // 开始轮播
            setPlaying(true);
        }
        super.onWindowVisibilityChanged(visibility);
    }

    private void switchBanner(int position, AppCompatImageView img) {
        if (onSwitchRvBannerListener != null) {
            onSwitchRvBannerListener.switchBanner(position % mData.size(), img);
        }
    }

    /**
     * RecyclerView适配器
     */
    private class RecyclerAdapter extends RecyclerView.Adapter {

        @Override
        @NonNull
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            AppCompatImageView img = new AppCompatImageView(parent.getContext());
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            img.setLayoutParams(params);
            img.setId(R.id.rvb_banner_image_view_id);
            img.setScaleType(AppCompatImageView.ScaleType.CENTER_CROP);
            img.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRvBannerClickListener != null) {
                        onRvBannerClickListener.onClick(mViewPager.getCurrentItem() % mData.size());
                    }
                }
            });
            return new RecyclerView.ViewHolder(img) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            AppCompatImageView img = holder.itemView.findViewById(R.id.rvb_banner_image_view_id);
            switchBanner(position, img);
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size() < 2 ? mData.size() : Integer.MAX_VALUE;
        }
    }

    /**
     * 改变导航的指示点
     */
    private void switchIndicator() {
        if (mLinearLayout != null && mLinearLayout.getChildCount() > 0) {
            for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
                ((AppCompatImageView) mLinearLayout.getChildAt(i)).setImageDrawable(
                        i == mViewPager.getCurrentItem() % mData.size() ? mSelectedDrawable : mUnselectedDrawable);
            }
        }
        updateTitle();
    }

    private void updateTitle() {
        if (onSwitchRvBannerTitleListener != null) {
            onSwitchRvBannerTitleListener.switchBanner(mViewPager.getCurrentItem() % mData.size(), mTitleTextView);
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    public interface OnSwitchRvBannerListener {
        void switchBanner(int position, AppCompatImageView bannerView);
    }

    public void setOnSwitchRvBannerListener(OnSwitchRvBannerListener listener) {
        this.onSwitchRvBannerListener = listener;
    }

    public interface OnRvBannerClickListener {
        void onClick(int position);
    }

    public void setOnRvBannerClickListener(OnRvBannerClickListener onRvBannerClickListener) {
        this.onRvBannerClickListener = onRvBannerClickListener;
    }

    public interface OnSwitchRvBannerTitleListener {
        void switchBanner(int position, TextView titleView);
    }

    public void setOnSwitchRvBannerTitleListener(OnSwitchRvBannerTitleListener onSwitchRvBannerTitleListener) {
        this.onSwitchRvBannerTitleListener = onSwitchRvBannerTitleListener;
    }
}
