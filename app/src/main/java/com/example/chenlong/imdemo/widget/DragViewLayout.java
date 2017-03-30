package com.example.chenlong.imdemo.widget;

import android.animation.FloatEvaluator;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by ChenLong on 2017/2/20.
 */

public class DragViewLayout extends FrameLayout
{
    private Context mContext;
    private ViewDragHelper mDragHelper;
    private ViewGroup mMenuView;
    private ViewGroup mMainView;
    private int mWidth;
    private int mHeight;
    private int mMaxDragRange;


    public DragViewLayout(Context context)
    {
        this(context, null);
    }

    public DragViewLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public DragViewLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init()
    {
        //1.创建一个viewDragHelper对象
        mDragHelper = ViewDragHelper.create(this, mCallback);
    }

    //2.触摸事件的拦截交由viewDragHelper处理
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //3.事件的触摸行为由viewDragHelper处理
        mDragHelper.processTouchEvent(event);
        return true;    //确定触摸事件由自己接收
    }

    //4.viewDragHelper的回调 重写监听里的方法
    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback()
    {
        /** step1 tryCaptureView : 尝试拖拽一个view
         * @param child 被触摸的孩子
         * @param pointerId 多点触摸;第一个触摸点pointerId=0;后面++
         * @return true表示进行拖拽
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId)
        {
            return child == mMainView;
        }


        /** step2 onViewCaptured : View已经被捕获  也就是view在拖拽
         * @param capturedChild 被拖拽的view
         * @param activePointerId 多点触控;第一个触摸点pointerId=0;后面++
         */
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId)
        {
            super.onViewCaptured(capturedChild, activePointerId);
        }

        /** step3 clampViewPositionHorizontal : 控制view水平方向移动的位置
         * 如果需要修改垂直方向移动就重写clampViewPositionVertical(View child, int top, int dy)方法
         * @param child
         * @param left
         * @param dx
         * @return 默认值super的方法为0, 即表示不会动
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx)
        {
            if (child == mMainView)
            {
                if (left > mMaxDragRange)
                {
                    left = mMaxDragRange;
                } else if (left < 0)
                {
                    left = 0;
                }
            }
            return left;
        }

        //step4 :在viewDragHelper的源码父类中view的真正发生的移动是调用了
        // offsetLeftAndRight(mCaptureView,clampedX-oldLeft) oldLeft就是step3返回的值 默认为0
        //所以随着left的返回值变动,那么view的位置就随着移动了

        /** step5 onViewPositionChanged : 当view的位置发生改变的时候被调用
         * @param changedView 被拖拽的view
         * @param left        水平方向移动的距离
         * @param top         垂直方向移动的距离
         * @param dx          水平方向的偏移量
         * @param dy          垂直方向的偏移量
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy)
        {
            super.onViewPositionChanged(changedView, left, top, dx, dy);

            //主页面移动的百分比,不要使用left,因为有可能拖拽的是别的菜单
            float percent = mMainView.getLeft() * 1.0f / mMaxDragRange;
            //执行一系列动画
            executeAnimation(percent);
            //执行接口回调
            executeListener(percent);
        }

        /** step6 onViewReleased : view被释放 也就是松手了
         * @param releasedChild 被拖拽的view
         * @param xvel          水平方向放手时的速度
         * @param yvel          垂直方向放手时的速度
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel)
        {
            //松手时,根据滑动的参数,和滑动的距离,判断打开或关闭菜单的view
            if ((xvel == 0 && mMainView.getLeft() > mMaxDragRange / 2) || xvel > 0)
            {
                //松手时,滑动大于一半的距离
                openMenuView();
            } else
            {
                closeMenuView();
            }
        }


        @Override
        public int getViewHorizontalDragRange(View child)
        {
            return mMaxDragRange;
        }
    };

    /**
     * 接口的回调
     *
     * @param percent
     */
    private void executeListener(float percent)
    {
        currentState = updateState(percent);

        if (mOnDragStateChangedListener != null)
        {
            if (currentState == DragState.OnOpen && currentState != preState)
            {
                mOnDragStateChangedListener.onOpen();
            } else if (currentState == DragState.onClose && currentState != preState)
            {
                mOnDragStateChangedListener.onClose();
            } else
            {
                mOnDragStateChangedListener.onDragging(percent);
            }
        }

        preState = currentState;
    }

    /**
     * 更新接口状态信息
     *
     * @param percent
     * @return
     */
    private DragState updateState(float percent)
    {
        if (percent == 1)
        {
            return DragState.OnOpen;
        } else if (percent == 0)
        {
            return DragState.onClose;
        }
        return DragState.onDragging;
    }

    private void closeMenuView()
    {
//        mMainView.layout(0, 0, mWidth, mHeight);    这样滑动太过生硬

        //使用另一种过渡时的动画 调用computeScroll执行
        if (mDragHelper.smoothSlideViewTo(mMainView, 0, 0))
        {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void openMenuView()
    {
        if (mDragHelper.smoothSlideViewTo(mMainView, mMaxDragRange, 0))
        {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    //计算滚动的距离
    @Override
    public void computeScroll()
    {
        super.computeScroll();
        //一帧一帧的执行动画,
        if (mDragHelper.continueSettling(true))
        {
//            invalidate();
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void executeAnimation(float percent)
    {
        //估值器
        FloatEvaluator floatEvaluator = new FloatEvaluator();
        Float evaluate = floatEvaluator.evaluate(percent, 1.0f, 0.8f);

        //主页面布局缩放效果
        ViewCompat.setScaleX(mMainView, evaluate);
        ViewCompat.setScaleY(mMainView, evaluate);

        //菜单布局平移效果 从屏幕外面移动进来
        evaluate = floatEvaluator.evaluate(percent, -mMenuView.getWidth() * 0.2f, 0);
        ViewCompat.setTranslationX(mMenuView, evaluate);
    }

    /**
     * 定义状态枚举
     */
    public enum DragState
    {
        OnOpen, onClose, onDragging;
    }

    public DragState currentState = DragState.onClose;    //默认状态
    public DragState preState = DragState.onClose;        //记录上一次状态

    /**
     * 拖拽的接口回调
     */
    public interface OnDragStateChangedListener
    {
        void onOpen();

        void onClose();

        void onDragging(float percent);
    }

    private OnDragStateChangedListener mOnDragStateChangedListener;

    public void setOnDragStateChangedListener(OnDragStateChangedListener onDragStateChangedListener)
    {
        mOnDragStateChangedListener = onDragStateChangedListener;
    }

    //此方法会在布局文件加载完毕读取到控件,但是onMeasure之前调用
    // 找到自己的子布局,其实也可以统统在onMeasure时调用
    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        //各种健壮性判断
        //判断子布局不为2个时 抛出异常
        if (getChildCount() != 2)
        {
            throw new IllegalStateException("you must have two children");
        }
        if (!(getChildAt(0) instanceof ViewGroup || !(getChildAt(1) instanceof ViewGroup)))
        {
            throw new IllegalArgumentException("your child must instanceof viewGroup");
        }
        mMenuView = (ViewGroup) getChildAt(0);      //拿到第一个布局
        mMainView = (ViewGroup) getChildAt(1);      //拿到第二个布局
    }

    //当宽高发生改变时调用,onMeasure之后调用
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mMaxDragRange = (int) (mWidth * 0.8f);  //定义一个最大的拖拽距离
    }
}
