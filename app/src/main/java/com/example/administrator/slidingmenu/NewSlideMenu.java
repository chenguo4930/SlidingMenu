package com.example.administrator.slidingmenu;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


/**
 * @author Dinosaur
 * @date 2018/10/25
 */
public class NewSlideMenu extends FrameLayout {

    private int down_x, down_y;
    private View mLeftView;
    private View mMainView;
    private int mLeftViewWidth;
    private int mMainViewWidth;
    private int mCurrentState = STATE_CLOSE;

    //这个是自动打开窗口或关闭的速度；
    int autoMoveSpeed = 3;
    public static final int STATE_OPEN = 0;
    public static final int STATE_CLOSE = 1;
    public static final int STATE_DRAGING = 2;

    public NewSlideMenu(@NonNull Context context) {
        this(context, null);
    }

    public NewSlideMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewSlideMenu(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 这里做一些初始化的一个操作；
     */
    private void init() {

    }

    public int getState() {
        return mCurrentState;
    }

    private ViewDragHelper mDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(@NonNull View view, int i) {
            System.out.println("SlideMenu:tryCaptureView: " + view);
            return mMainView == view;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            System.out.println("SlideMenu:clampViewPositionVertical: " + child + "top: " + top + " dy" + dy);
            return 0;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            if (child == mMainView) {
                if (left <= 0) {
                    left = 0;
                }
                if (left >= mLeftViewWidth) {
                    left = mLeftViewWidth;
                }
            }

            if (child == mLeftView) {
                if (left < -mLeftViewWidth) {
                    left = -mLeftViewWidth;
                }
                if (left > 0) {
                    left = 0;
                }
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            System.out.println("SlideMenu:onViewPositionChanged: " + changedView + " left+" + left + " top" + top + " dx" + dx + " dy" + dy);
            //super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView == mMainView) {
                if (mLeftView.getLeft() < 0) {
                    //这里我们移动左边的面板；
                    mLeftView.offsetLeftAndRight(dx);
                } else {
                    if (dx < 0) {
                        mLeftView.offsetLeftAndRight(dx);
                    }
                }
            }
            //这里我们讲相应的百分比暴露出来；
            dispatchDrag();
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            System.out.println("SlideMenu:onViewReleased: " + releasedChild + " xvel " + xvel + " yvel " + yvel);
            super.onViewReleased(releasedChild, xvel, yvel);
            //这里我们做关闭的一个逻辑；
            if (xvel > 1000) {
                //这里我们做打开的操作；
                open();
            } else if (mMainView.getLeft() > mLeftViewWidth / 2 && xvel > 0) {
                open();
            } else {
                close();
            }
        }
    });

    /**
     * 这里我们讲派发事件的一个操作；
     */
    private void dispatchDrag() {

        int left = mMainView.getLeft();
        float percent = left * 1.0f / mLeftViewWidth;

        if (percent == 0) {
            mCurrentState = STATE_CLOSE;
        } else if (percent == 1) {
            mCurrentState = STATE_OPEN;
        } else {
            mCurrentState = STATE_DRAGING;
        }

        String state = "";
        switch (mCurrentState) {
            case STATE_OPEN:
                state = "STATE_OPEN";
                break;
            case STATE_CLOSE:
                state = "STATE_CLOSE";
                //让MainView里面的内容再次可以滑动；
                // mMainView.resetNeedEvent();
                break;
            case STATE_DRAGING:
                state = "STATE_DRAGING";
                break;
            default:
        }

        System.out.println("percent:" + percent);
    }


    int lastLeft = 0;

    /**
     * 关闭侧滑的一个操作；
     */
    public void close() {

        int start = mMainView.getLeft();
        int end = 0;

        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        lastLeft = mMainView.getLeft();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int animatedValue = (Integer) animation.getAnimatedValue();

                System.out.println("SlideMenu:close:lastLeft " + lastLeft + " animatedValue: " + animatedValue);
                int eval = animatedValue - lastLeft;
                mMainView.offsetLeftAndRight(eval);

                mLeftView.offsetLeftAndRight(eval);
                lastLeft = animatedValue;
                dispatchDrag();
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentState = STATE_CLOSE;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        //这里我们要根据速度计算时间；
        int time = (start - end) / autoMoveSpeed;
        valueAnimator.setDuration(time);
        valueAnimator.start();
    }

    /**
     * 打开侧滑的一个操作；
     */
    public void open() {

        int start = mMainView.getLeft();
        int end = mLeftViewWidth;

        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        lastLeft = mMainView.getLeft();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (Integer) animation.getAnimatedValue();
                int eval = animatedValue - lastLeft;
                mMainView.offsetLeftAndRight(eval);
                mLeftView.offsetLeftAndRight(eval);
                lastLeft = animatedValue;
                dispatchDrag();
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentState = STATE_OPEN;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        int time = (end - start) / autoMoveSpeed;
        valueAnimator.setDuration(time);
        valueAnimator.start();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //这里我们获取起两个孩子；

        mLeftView = getChildAt(0);
        mMainView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLeftViewWidth = mLeftView.getMeasuredWidth();
        mMainViewWidth = mMainView.getMeasuredWidth();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //这里我们是需要根据相应的移动到指定的位置；
        //左边的View的初始的位置；
        mLeftView.layout(-mLeftViewWidth, top, 0, bottom);
    }


    float lastTouchX = 0;
    float lastIntercepterTouchX = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
//        switch (e.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                down_x = (int) e.getX();
//                down_y = (int) e.getY();
//
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                int x = (int) (e.getX() - down_x);
//                int y = (int) (e.getY() - down_y);
//                down_x = (int) e.getX();
//                down_y = (int) e.getY();
//
//                if (Math.abs(x) - Math.abs(y) > 0){
//                    Log.e("---","--dispatchTouchEvent-->0");
//                    return false;
//                }
//
//                break;
//        }
        //打开的状态，将事件传递给menu

        //关闭的状态并且是右边滑动，将事件交给SlideMenu;

        //拖动的状态将事件交给SlideMenu;


        //其他的状态默认处理；
       /* if (mCurrentState == STATE_OPEN) {

            mMainView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    close();
                }
            });
            return mLeftView.dispatchTouchEvent(ev);
        }*/


        return super.dispatchTouchEvent(e);

        //boolean b = onInterceptTouchEvent(ev);
        // System.out.println("onInterceptTouchEvent: "+b+" onTouchEvent: "+t);
    }


//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent e) {
//        switch (e.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                down_x = (int) e.getX();
//                down_y = (int) e.getY();
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                int x = (int) (e.getX() - down_x);
//                int y = (int) (e.getY() - down_y);
//                down_x = (int) e.getX();
//                down_y = (int) e.getY();
//
//                if (Math.abs(x) - Math.abs(y) > 0) {
//                    Log.e("---", "---->0");
//                    return true;
//                }
//                break;
//            default:
//        }
//        return super.onTouchEvent(e);
//    }

     /*   switch (ev.getAction()) {
            case  MotionEvent.ACTION_DOWN:
                lastIntercepterTouchX=ev.getRawX();
                break;
            case  MotionEvent.ACTION_MOVE:

                float rawX = ev.getRawX();

                float dx = rawX - lastIntercepterTouchX;

                System.out.println("SlideMenu:onInterceptTouchEvent "+dx+" mCurrentState: "+mCurrentState);

                if (mCurrentState ==STATE_CLOSE && dx>0) {

                    //这里表示右边滑动；
                    boolean s = mDragHelper.shouldInterceptTouchEvent(ev);

                    System.out.println("SlideMenu:onInterceptTouchEvent "+"2333"+" shouldIntercept: "+s);

                    return true;
                }
                break;
            case  MotionEvent.ACTION_UP:
                lastIntercepterTouchX=0;
                break;
        }*/


       /* if (mCurrentState == STATE_CLOSE) {

            mDragHelper.shouldInterceptTouchEvent(ev);
            return true;
        }else{
            mDragHelper.shouldInterceptTouchEvent(ev);
            return false;
        }*/;

       /*if(!mMainView.isNeedEvent()){
            return true;
       }*/
//       return mDragHelper.shouldInterceptTouchEvent(ev);
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mDragHelper.processTouchEvent(event);
        return true;
    }
}
