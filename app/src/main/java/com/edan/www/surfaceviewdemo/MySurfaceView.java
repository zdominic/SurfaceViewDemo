package com.edan.www.surfaceviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 创建者     Zhangyu
 * 创建时间   2018/7/17 13:53
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author
 * 更新时间   $Date
 * 更新描述   ${TODO}
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;//绘图的画布
    private boolean mIsDrawing;//控制绘画线程的标志位
    private Path mPath;
    private Paint mPaint;


    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mSurfaceHolder = getHolder();      //获取SurfaceHolder对象
        //添加回调
        mSurfaceHolder.addCallback(this);  //注册SurfaceHolder的回调方法
        setFocusable(true);
        setFocusableInTouchMode(true);
        setZOrderOnTop(true);       //避免黑屏
        //初始化画笔
        mPaint=new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(6);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.YELLOW);

        mPath = new Path();
        this.setKeepScreenOn(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    /**
     * 每30帧刷新一次屏幕
     **/
    public static final int TIME_IN_FRAME = 100;

    @Override
    public void run() {
        while (mIsDrawing) {
            /**取得更新之前的时间**/
            long startTime = System.currentTimeMillis();
            /**在这里加上线程安全锁**/
            synchronized (mSurfaceHolder) {
                draw();
            }

            /**取得更新结束的时间**/
            long endTime = System.currentTimeMillis();

            /**计算出一次更新的毫秒数**/
            int diffTime = (int) (endTime - startTime);

            /**确保每次更新时间为30帧**/
            while (diffTime <= TIME_IN_FRAME) {
                diffTime = (int) (System.currentTimeMillis() - startTime);
                /**线程等待**/
                Thread.yield();
            }
        }
    }

    private int x;
    private int y;

    //绘图操作
    private void draw() {
        x += 1;
        y = (int) (100 * Math.sin(x * 2 * Math.PI / 180) + 400);

        mPath.lineTo(x, y);

        try{
            /**拿到当前画布 然后锁定**/
            mCanvas=mSurfaceHolder.lockCanvas();//获取Canvas对象进行绘制
            //SurfaceView背景
            mCanvas.drawColor(Color.GRAY);
            mCanvas.drawPath(mPath,mPaint);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (mCanvas!=null){
                /**绘制结束后解锁显示在屏幕上**/
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);   //保证绘制的画布内容提交
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return true;//表示此View拦截处理触摸事件
    }


    /**
     * 清屏
     * @return true
     */
    public boolean reDraw(){
        mPath.reset();
        return true;
    }
}