package com.yfz.infinitescroll;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;

/**
 * 制作者：游丰泽
 * 简介：无限滚动背景view，仿小红书登陆页背景
 */
public class ScrollFragmentView extends View {
    private Context mContext;
    private Drawable mDrawable;
    private Matrix mMatrix;
    private Paint mPaint;
    private Bitmap mBitmap;
    //第一张背景已上移的高度
    private float mScrollCurrentY =0;
    //第二章背景已上移的高度
    private float mScrollNextY =0;
    //背景刷新频率,通过延迟消息实现.数值越大,刷新频率越低,会有卡顿的感觉
    private final long mRefreshRate = 5L;
    //背景每帧上移高度
    private float mRefreshHeightPerFrameRate =4;

    public ScrollFragmentView(Context context) {
        super(context);
        init(context);
    }
    public ScrollFragmentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public ScrollFragmentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context){
        this.mContext=context;
        this.mDrawable=getResources().getDrawable(R.drawable.icon_background);//背景
        this.mMatrix=new Matrix();//矩阵
        this.mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);//画笔，绘制蒙版和drawable
        this.mPaint.setColor(Color.YELLOW);
        this.mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
        this.setWillNotDraw(false);//允许不设置背景情况下调用onDraw
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mMatrix.reset();
        mMatrix.preTranslate(0, -1*mScrollNextY);
        canvas.drawBitmap(mBitmap,mMatrix,mPaint);

        mMatrix.reset();
        mMatrix.postTranslate(0, -1*mScrollCurrentY);
        canvas.drawBitmap(mBitmap,mMatrix,mPaint);

        getHandler().postDelayed(scrollRunnable, mRefreshRate); //不断的循环延迟消息，达到平滑移动效果
    }

    private Runnable scrollRunnable = new Runnable() {
        @Override
        public void run() {
            mScrollCurrentY=mScrollCurrentY+mRefreshHeightPerFrameRate;
            if(mScrollCurrentY>=getHeight()){
                mScrollNextY=getHeight();
            }else {

            }
            if(mScrollCurrentY<=-1*mBitmap.getHeight()){
                mScrollCurrentY=0;
            }

            Log.d("TAG", "run: "+mScrollCurrentY+" "+mScrollNextY+" "+mBitmap.getHeight()+" "+getHeight());
            invalidate();
        }
    };
}
