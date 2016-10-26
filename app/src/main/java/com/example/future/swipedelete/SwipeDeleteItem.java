package com.example.future.swipedelete;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:滑动删除条目<br>
 * date: 2016/10/26  14:21.
 */

public class SwipeDeleteItem extends FrameLayout {
    private static final String TAG = "SwipeDeleteItem";
    private View swipedel_content;
    private View swipedel_choice;
    private int swipedel_height;
    private int swipedel_content_width;
    private int swipedel_choice_width;
    private ViewDragHelper dragHelper;
    private OnOpenStateChangeListener mOnOpenStateChangeListener;
    private boolean isOpen;
    // 第七：ViewDragHelper 的 回调
    ViewDragHelper.Callback dragerCallback = new ViewDragHelper.Callback() {
        @Override
        // 移动  子 View
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            // 根据不同的 子View 拖动 另一个 子 View
            if (changedView == swipedel_content) {
                swipedel_choice.layout(swipedel_choice.getLeft() + dx, swipedel_choice.getTop() + dy,
                        swipedel_choice.getRight() + dx, swipedel_choice.getBottom() + dy);
            } else if (changedView == swipedel_choice) {
                swipedel_content.layout(swipedel_content.getLeft() + dx, swipedel_choice.getTop() + dy,
                        swipedel_choice.getRight() + dx, swipedel_choice.getBottom() + dy);
            }
            // 判断 滑动菜单是否打开了
            if (swipedel_content.getLeft() == 0 && isOpen) {
                isOpen = false;
                if (mOnOpenStateChangeListener != null) {
                    mOnOpenStateChangeListener.onClose();
                }
                // 取消记录 打开的 SwipeDeleteItem  使用场景  用户打开 再关闭
                SwipeDelItemManager.getInstance().unRegisterSwipeItem(SwipeDeleteItem.this);
            } else if (swipedel_content.getLeft() == -swipedel_choice_width && !isOpen) {
                isOpen = true;
                if (mOnOpenStateChangeListener != null) {
                    mOnOpenStateChangeListener.onOpen();
                }
                // 保证只有一个 SwipeDeleteItem 条目 打开
                if (!SwipeDelItemManager.getInstance().isSwipeItemNull()) {
                    SwipeDelItemManager.getInstance().getOpenedItem().close();
                }
                // 记录 打开的 SwipeDeleteItem
                SwipeDelItemManager.getInstance().registerSwipeItem(SwipeDeleteItem.this);

            }

        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            Log.i(TAG, "onViewReleased: " + xvel);
            // 根据滑动速度 来 开关
            if (xvel > 300) {
                close();
                return;
            } else if (xvel < -300) {
                open();
                return;
            }

            // 根据 滑动位置 来 开关
            if (swipedel_content.getLeft() < -swipedel_choice_width / 2) {
                open();
            } else {
                close();
            }
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return swipedel_choice_width;

        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == swipedel_content || child == swipedel_choice;
        }

        @Override
        // 限制  子 View 的 向左向右 滑动 范围
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == swipedel_content) {
                if (left > 0) left = 0; // 限制 向 右 继续滑动
                if (left < -swipedel_choice_width) left = -swipedel_choice_width; // 限制向左 继续滑动
            }
            if (child == swipedel_choice) {
                if (left > swipedel_content_width) left = swipedel_content_width;
                if (left < (swipedel_content_width - swipedel_choice_width))
                    left = swipedel_content_width - swipedel_choice_width; // 限制向左 继续滑动
            }
            Log.i(TAG, "clampViewPositionHorizontal: " + left);
            return left;
        }
    };
    // 第六： 交由 ViewDragHelper 判断 消费事件
    private float downX, downY;

    public SwipeDeleteItem(Context context) {
        this(context, null);
    }

    public SwipeDeleteItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeDeleteItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // 第一：初始化一下 对象
    private void init() {
        dragHelper = ViewDragHelper.create(this, dragerCallback);
    }

    // 第四： 摆放 2 个 子 view
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        swipedel_content.layout(0, 0, swipedel_content_width, swipedel_height);
        swipedel_choice.layout(swipedel_content.getRight(), 0, swipedel_content.getRight() +
                swipedel_content_width, swipedel_height);
    }
    // 第五 ： 交由 ViewDragHelper 判断是否 应该 拦截事件

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
        // return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);

        return true;
    }

    //第三 获取 子View 的 宽 高
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 因为2 个 子 View的 高都是80dp
        swipedel_height = swipedel_content.getMeasuredHeight();
        swipedel_content_width = swipedel_content.getMeasuredWidth();
        swipedel_choice_width = swipedel_choice.getMeasuredWidth();
    }

    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    // 第二：获取 子View 的 实例
    @Override
    protected void onFinishInflate() {
        swipedel_content = getChildAt(0);
        swipedel_choice = getChildAt(1);
    }

    public void close() {
        dragHelper.smoothSlideViewTo(swipedel_content, 0, swipedel_content.getTop());
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void open() {
        dragHelper.smoothSlideViewTo(swipedel_content, -swipedel_choice_width, swipedel_content.getTop());
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void setOnOpenStateChangeListener(OnOpenStateChangeListener listener) {
        this.mOnOpenStateChangeListener = listener;
    }

    public boolean isOpen() {
        return isOpen;
    }

    /**
     * 回调接口
     */
    public interface OnOpenStateChangeListener {
        void onOpen();

        void onClose();
    }
}
