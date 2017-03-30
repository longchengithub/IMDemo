package com.example.chenlong.imdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.chenlong.imdemo.util.Cheeses;

/**
 * Created by ChenLong on 2017/2/21.
 */

public class FastIndexBar extends View
{
    private Context mContext;
    private Paint mPaint;
    private int mCellWidth;
    private float mCellHeight;
    private Rect mRect;
    private int mCurrentIndex = -1;

    public FastIndexBar(Context context)
    {
        this(context, null);
    }

    public FastIndexBar(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public FastIndexBar(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mContext = context;

        init();
    }

    private void init()
    {
        //抗锯齿
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(40);

        mRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        for (int i = 0; i < Cheeses.LETTERS.length; i++)
        {
            String letter = Cheeses.LETTERS[i];
            //拿到每个文本的具体宽高,用之前的mRect接收       参数2,3的0和1代表 onMeasure测量接收的参数0,1测量自己
            mPaint.getTextBounds(letter, 0, 1, mRect);
            int textWidth = mRect.width();
            int textHeight = mRect.height();

            //绘制文本的x,y坐标点  Text的文本是基于左下脚的那个点绘制的
            //所以如果想居中,那么就是 mCellWidth的一半 减去 textWidth的一半 就是最终的坐标 height同理
            float x = mCellWidth * 0.5f - textWidth * 0.5f;
            float y = mCellHeight * 0.5f + textHeight * 0.5f + mCellHeight * i; //这里乘以i是从上往下循环绘制

            mPaint.setColor(mCurrentIndex == i ? Color.RED : Color.DKGRAY);
            canvas.drawText(letter, x, y, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //请求处理触摸事件
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //根据触摸后的y值 除以单元格高度 正好得到对应的索引
                float y = event.getY();
                mCurrentIndex = (int) (y / mCellHeight);
                //健壮性判断  由于Java中除法不会那么精准  所以最下面会有一点空余的缝隙空间会越界索引
                if (mCurrentIndex >= 0 && mCurrentIndex < Cheeses.LETTERS.length)
                {
//                    CommonUtils.showToast(mContext, Cheeses.LETTERS[mCurrentIndex]);
                    if (mOnLetterChangedListener != null)
                    {
                        mOnLetterChangedListener.onLetterChanged(Cheeses.LETTERS[mCurrentIndex]);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mCurrentIndex = -1;
                if (mOnLetterChangedListener != null)
                {
                    mOnLetterChangedListener.onFinishChanged();
                }
                break;
        }
        //重新绘制 触摸后颜色改变的文本
        invalidate();
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取控件每个字母的宽 也就是同控件宽度
        mCellWidth = getMeasuredWidth();
        //获取控件每个字母的高度  也就是总高度除以27个字母
        mCellHeight = getMeasuredHeight() * 1.0f / Cheeses.LETTERS.length;
    }

    /**
     * 对外的接口回调
     */
    public interface OnLetterChangedListener
    {
        void onLetterChanged(String letter);

        void onFinishChanged();
    }

    private OnLetterChangedListener mOnLetterChangedListener;

    public void setOnLetterChangedListener(OnLetterChangedListener onLetterChangedListener)
    {
        mOnLetterChangedListener = onLetterChangedListener;
    }
}
