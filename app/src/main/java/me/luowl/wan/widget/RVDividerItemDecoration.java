package me.luowl.wan.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView分割线，对于一些间隔比较特殊的用这个
 */
public abstract class RVDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;

    private Context context;

    public RVDividerItemDecoration(Context context) {
        this.context = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //left, top, right, bottom
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            int itemPosition = ((RecyclerView.LayoutParams) child.getLayoutParams()).getViewLayoutPosition();

            RVDivider divider = getDivider(parent, itemPosition);

            if (divider.getLeftSideLine().isHave()) {
                int lineWidthPx = dp2px(divider.getLeftSideLine().getWidthDp());
                int startPaddingPx = dp2px(divider.getLeftSideLine().getStartPaddingDp());
                int endPaddingPx = dp2px(divider.getLeftSideLine().getEndPaddingDp());
                drawChildLeftVertical(child, c, parent, divider.getLeftSideLine()
                        .getColor(), lineWidthPx, startPaddingPx, endPaddingPx);
            }
            if (divider.getTopSideLine().isHave()) {
                int lineWidthPx = dp2px(divider.getTopSideLine().getWidthDp());
                int startPaddingPx = dp2px(divider.getTopSideLine().getStartPaddingDp());
                int endPaddingPx = dp2px(divider.getTopSideLine().getEndPaddingDp());
                drawChildTopHorizontal(child, c, parent, divider.topSideLine.getColor(), lineWidthPx, startPaddingPx, endPaddingPx);
            }
            if (divider.getRightSideLine().isHave()) {
                int lineWidthPx = dp2px(divider.getRightSideLine().getWidthDp());
                int startPaddingPx = dp2px(divider.getRightSideLine().getStartPaddingDp());
                int endPaddingPx = dp2px(divider.getRightSideLine().getEndPaddingDp());
                drawChildRightVertical(child, c, parent, divider.getRightSideLine()
                        .getColor(), lineWidthPx, startPaddingPx, endPaddingPx);
            }
            if (divider.getBottomSideLine().isHave()) {
                int lineWidthPx = dp2px(divider.getBottomSideLine().getWidthDp());
                int startPaddingPx = dp2px(divider.getBottomSideLine().getStartPaddingDp());
                int endPaddingPx = dp2px(divider.getBottomSideLine().getEndPaddingDp());
                drawChildBottomHorizontal(child, c, parent, divider.getBottomSideLine()
                        .getColor(), lineWidthPx, startPaddingPx, endPaddingPx);
            }
        }
    }

    private void drawChildBottomHorizontal(View child, Canvas c, RecyclerView parent, @ColorInt int color, int lineWidthPx, int startPaddingPx, int endPaddingPx) {

        int leftPadding = 0;
        int rightPadding = 0;

        if (startPaddingPx <= 0) {
            //padding<0当作==0处理
            //上下左右默认分割线的两头都出头一个分割线的宽度，避免十字交叉的时候，交叉点是空白
            leftPadding = -lineWidthPx;
        } else {
            leftPadding = startPaddingPx;
        }

        if (endPaddingPx <= 0) {
            rightPadding = lineWidthPx;
        } else {
            rightPadding = -endPaddingPx;
        }

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                .getLayoutParams();
        int left = child.getLeft() - params.leftMargin + leftPadding;
        int right = child.getRight() + params.rightMargin + rightPadding;
        int top = child.getBottom() + params.bottomMargin;
        int bottom = top + lineWidthPx;
        mPaint.setColor(color);

        c.drawRect(left, top, right, bottom, mPaint);

    }

    private void drawChildTopHorizontal(View child, Canvas c, RecyclerView parent, @ColorInt int color, int lineWidthPx, int startPaddingPx, int endPaddingPx) {
        int leftPadding = 0;
        int rightPadding = 0;

        if (startPaddingPx <= 0) {
            //padding<0当作==0处理
            //上下左右默认分割线的两头都出头一个分割线的宽度，避免十字交叉的时候，交叉点是空白
            leftPadding = -lineWidthPx;
        } else {
            leftPadding = startPaddingPx;
        }
        if (endPaddingPx <= 0) {
            rightPadding = lineWidthPx;
        } else {
            rightPadding = -endPaddingPx;
        }

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                .getLayoutParams();
        int left = child.getLeft() - params.leftMargin + leftPadding;
        int right = child.getRight() + params.rightMargin + rightPadding;
        int bottom = child.getTop() - params.topMargin;
        int top = bottom - lineWidthPx;
        mPaint.setColor(color);

        c.drawRect(left, top, right, bottom, mPaint);

    }

    private void drawChildLeftVertical(View child, Canvas c, RecyclerView parent, @ColorInt int color, int lineWidthPx, int startPaddingPx, int endPaddingPx) {
        int topPadding = 0;
        int bottomPadding = 0;

        if (startPaddingPx <= 0) {
            //padding<0当作==0处理
            //上下左右默认分割线的两头都出头一个分割线的宽度，避免十字交叉的时候，交叉点是空白
            topPadding = -lineWidthPx;
        } else {
            topPadding = startPaddingPx;
        }
        if (endPaddingPx <= 0) {
            bottomPadding = lineWidthPx;
        } else {
            bottomPadding = -endPaddingPx;
        }

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                .getLayoutParams();
        int top = child.getTop() - params.topMargin + topPadding;
        int bottom = child.getBottom() + params.bottomMargin + bottomPadding;
        int right = child.getLeft() - params.leftMargin;
        int left = right - lineWidthPx;
        mPaint.setColor(color);

        c.drawRect(left, top, right, bottom, mPaint);

    }

    private void drawChildRightVertical(View child, Canvas c, RecyclerView parent, @ColorInt int color, int lineWidthPx, int startPaddingPx, int endPaddingPx) {

        int topPadding = 0;
        int bottomPadding = 0;

        if (startPaddingPx <= 0) {
            //padding<0当作==0处理
            //上下左右默认分割线的两头都出头一个分割线的宽度，避免十字交叉的时候，交叉点是空白
            topPadding = -lineWidthPx;
        } else {
            topPadding = startPaddingPx;
        }
        if (endPaddingPx <= 0) {
            bottomPadding = lineWidthPx;
        } else {
            bottomPadding = -endPaddingPx;
        }

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                .getLayoutParams();
        int top = child.getTop() - params.topMargin + topPadding;
        int bottom = child.getBottom() + params.bottomMargin + bottomPadding;
        int left = child.getRight() + params.rightMargin;
        int right = left + lineWidthPx;
        mPaint.setColor(color);

        c.drawRect(left, top, right, bottom, mPaint);

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        //outRect 看源码可知这里只是把Rect类型的outRect作为一个封装了left,right,top,bottom的数据结构,
        //作为传递left,right,top,bottom的偏移值来用的

        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();

        RVDivider divider = getDivider(parent, itemPosition);

        if (divider == null) {
            divider = new RVDividerBuilder().create();
        }

        int left = divider.getLeftSideLine().isHave() ? dp2px(divider.getLeftSideLine()
                .getWidthDp()) : 0;
        int top = divider.getTopSideLine().isHave() ? dp2px(divider.getTopSideLine()
                .getWidthDp()) : 0;
        int right = divider.getRightSideLine().isHave() ? dp2px(divider.getRightSideLine()
                .getWidthDp()) : 0;
        int bottom = divider.getBottomSideLine().isHave() ? dp2px(divider.getBottomSideLine()
                .getWidthDp()) : 0;

        outRect.set(left, top, right, bottom);
    }

    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }


    public abstract @Nullable
    RVDivider getDivider(RecyclerView parent, int itemPosition);

    public class RVSideLine {

        public boolean isHave = false;
        /**
         * A single color value in the form 0xAARRGGBB.
         **/
        public int color;
        /**
         * 单位dp
         */
        public float widthDp;
        /**
         * startPaddingDp,分割线起始的padding，水平方向左为start，垂直方向上为start
         * endPaddingDp,分割线尾部的padding，水平方向右为end，垂直方向下为end
         */
        public float startPaddingDp;
        public float endPaddingDp;

        public RVSideLine(boolean isHave, @ColorInt int color, float widthDp, float startPaddingDp, float endPaddingDp) {
            this.isHave = isHave;
            this.color = color;
            this.widthDp = widthDp;
            this.startPaddingDp = startPaddingDp;
            this.endPaddingDp = endPaddingDp;
        }

        public float getStartPaddingDp() {
            return startPaddingDp;
        }

        public void setStartPaddingDp(float startPaddingDp) {
            this.startPaddingDp = startPaddingDp;
        }

        public float getEndPaddingDp() {
            return endPaddingDp;
        }

        public void setEndPaddingDp(float endPaddingDp) {
            this.endPaddingDp = endPaddingDp;
        }

        public boolean isHave() {
            return isHave;
        }

        public void setHave(boolean have) {
            isHave = have;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public float getWidthDp() {
            return widthDp;
        }

        public void setWidthDp(float widthDp) {
            this.widthDp = widthDp;
        }
    }

    public class RVDivider {
        public RVSideLine leftSideLine;
        public RVSideLine topSideLine;
        public RVSideLine rightSideLine;
        public RVSideLine bottomSideLine;


        public RVDivider(RVSideLine leftSideLine, RVSideLine topSideLine, RVSideLine rightSideLine, RVSideLine bottomSideLine) {
            this.leftSideLine = leftSideLine;
            this.topSideLine = topSideLine;
            this.rightSideLine = rightSideLine;
            this.bottomSideLine = bottomSideLine;
        }

        public RVSideLine getLeftSideLine() {
            return leftSideLine;
        }

        public void setLeftSideLine(RVSideLine leftSideLine) {
            this.leftSideLine = leftSideLine;
        }

        public RVSideLine getTopSideLine() {
            return topSideLine;
        }

        public void setTopSideLine(RVSideLine topSideLine) {
            this.topSideLine = topSideLine;
        }

        public RVSideLine getRightSideLine() {
            return rightSideLine;
        }

        public void setRightSideLine(RVSideLine rightSideLine) {
            this.rightSideLine = rightSideLine;
        }

        public RVSideLine getBottomSideLine() {
            return bottomSideLine;
        }

        public void setBottomSideLine(RVSideLine bottomSideLine) {
            this.bottomSideLine = bottomSideLine;
        }
    }

    public class RVDividerBuilder {

        private RVSideLine leftSideLine;
        private RVSideLine topSideLine;
        private RVSideLine rightSideLine;
        private RVSideLine bottomSideLine;


        public RVDividerBuilder setLeftSideLine(boolean isHave, @ColorInt int color, float widthDp, float startPaddingDp, float endPaddingDp) {
            this.leftSideLine = new RVSideLine(isHave, color, widthDp, startPaddingDp, endPaddingDp);
            return this;
        }

        public RVDividerBuilder setTopSideLine(boolean isHave, @ColorInt int color, float widthDp, float startPaddingDp, float endPaddingDp) {
            this.topSideLine = new RVSideLine(isHave, color, widthDp, startPaddingDp, endPaddingDp);
            return this;
        }

        public RVDividerBuilder setRightSideLine(boolean isHave, @ColorInt int color, float widthDp, float startPaddingDp, float endPaddingDp) {
            this.rightSideLine = new RVSideLine(isHave, color, widthDp, startPaddingDp, endPaddingDp);
            return this;
        }

        public RVDividerBuilder setBottomSideLine(boolean isHave, @ColorInt int color, float widthDp, float startPaddingDp, float endPaddingDp) {
            this.bottomSideLine = new RVSideLine(isHave, color, widthDp, startPaddingDp, endPaddingDp);
            return this;
        }

        public RVDivider create() {
            //提供一个默认不显示的sideline，防止空指针
            RVSideLine defaultSideLine = new RVSideLine(false, 0xff666666, 0, 0, 0);

            leftSideLine = (leftSideLine != null ? leftSideLine : defaultSideLine);
            topSideLine = (topSideLine != null ? topSideLine : defaultSideLine);
            rightSideLine = (rightSideLine != null ? rightSideLine : defaultSideLine);
            bottomSideLine = (bottomSideLine != null ? bottomSideLine : defaultSideLine);

            return new RVDivider(leftSideLine, topSideLine, rightSideLine, bottomSideLine);
        }
    }

}