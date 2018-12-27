package com.qacorn.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


import com.qacorn.utils.ScreenUtility;


/**
 * Created by Administrator on 2015/8/17.
 */
public class VolumeTurningButton extends View implements GestureDetector.OnGestureListener {

    private static final float PROGRESS_STROKE_WIDTH = 1F / 16F, PROGRESS_STROKE_RADIUS = 3F / 8F, DIAL_PLATE_SHADOW_OFFSET = 35F / 480F, DIAL_PLATE_SHADOW_RADIUS = 1F / 3F, DIAL_PLATE_RIM_WIDTH = 8F / 480F, DIAL_PLATE_OUTLINE_WIDTH = 3F / 480F, DIAL_PLATE_INDICATOR_WIDTH = 25F / 480F, DIAL_PLATE_RADIUS = 140F / 480F, DIAL_PLATE_OUTLINE_RADIUS = 128F / 480F, DIAL_PLATE_INDICATOR_RADIUS = 110F / 480F, DIAL_PLATE_RIM_RADIUS = 136F / 480F,INVALID_TOUCH_RADIUS = 1F/8F,PROGRESS_DASH_WIDTH = 3F/480F,PROGRESS_BLANK_WIDTH = 9F/480F;


    private int side;// 控件边长
    private int screenW, screenH;// 屏幕尺寸

    private float progressStrokeWidth, progressStrokeRadius, dialPlateShadowOffset, dialPlateShadowRadius, dialPlateRimWidth, dialPlateOutlineWidth, dialPlateIndicatorWidth, dialPlateRadius, dialPlateRimRadius, dialPlateOutlineRadius, dialPlateIndicatorRadius, dialPlateIndicatorOffset,invalidTouchRadius,invalidCoordinateOffset,progressDashWidth,progressBlankWidth;
    private float initXCoordinate, initYCoordinate;
    private float centerX, centerY;
    private float dialPlateShadowCenterX, dialPlateShadowCenterY;
    private float startAngle = 135f, sweepAngle = 270f;

    private double rotateDegree = 0;
    private boolean clockWise = true;

    private Context context;
    private Paint progressPaint,secondProgressPaint,shadowPaint,rimPaint,dialPlatePaint,outlinePaint,indicatorDotPaint;
    private Paint greenCirclePaint;
    private RectF externalRect,indicatorRect;
    private DashPathEffect dashPathEffect;

    private GestureDetector gestureDetector;

    public VolumeTurningButton(Context context) {
        super(context);
        this.context = context;
        init();
    }


    public VolumeTurningButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public VolumeTurningButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    /*
    * 参数计算
    */
    private void calculation() {
        screenW = ScreenUtility.getScreenWidth((Activity) context);
        screenH = ScreenUtility.getScreenHeight((Activity) context);
        side = screenW/2;
        progressStrokeWidth = side * PROGRESS_STROKE_WIDTH;
        progressStrokeRadius = side * PROGRESS_STROKE_RADIUS;
        centerX = side / 2;
        centerY = side / 2;
        dialPlateShadowOffset = side * DIAL_PLATE_SHADOW_OFFSET;
        dialPlateShadowCenterX = centerX + dialPlateShadowOffset;
        dialPlateShadowCenterY = centerY + dialPlateShadowOffset;
        dialPlateShadowRadius = side * DIAL_PLATE_SHADOW_RADIUS;
        dialPlateRimWidth = side * DIAL_PLATE_RIM_WIDTH;
        dialPlateOutlineWidth = side * DIAL_PLATE_OUTLINE_WIDTH;
        dialPlateIndicatorWidth = side * DIAL_PLATE_INDICATOR_WIDTH;
        dialPlateRadius = side * DIAL_PLATE_RADIUS;
        dialPlateRimRadius = side * DIAL_PLATE_RIM_RADIUS;
        dialPlateOutlineRadius = side * DIAL_PLATE_OUTLINE_RADIUS;
        dialPlateIndicatorRadius = side * DIAL_PLATE_INDICATOR_RADIUS;
        dialPlateIndicatorOffset = (float) (dialPlateIndicatorRadius * Math.sqrt(2) / 2);
        initXCoordinate = centerX - dialPlateIndicatorOffset;
        initYCoordinate = centerY + dialPlateIndicatorOffset;
        invalidTouchRadius = side*INVALID_TOUCH_RADIUS;
        invalidCoordinateOffset = (float) (invalidTouchRadius * Math.sqrt(2) / 2);
        progressDashWidth = side*PROGRESS_DASH_WIDTH;
        progressBlankWidth = side*PROGRESS_BLANK_WIDTH;

    }

    private void init() {
        gestureDetector = new GestureDetector(context, this);
        calculation();
        dashPathEffect = new DashPathEffect(new float[]{progressDashWidth, progressBlankWidth}, 0);
        externalRect = new RectF(centerX - progressStrokeRadius, centerY - progressStrokeRadius, centerX + progressStrokeRadius, centerY + progressStrokeRadius);
        indicatorRect = new RectF(centerX - dialPlateIndicatorRadius, centerY - dialPlateIndicatorRadius, centerX + dialPlateIndicatorRadius, centerY + dialPlateIndicatorRadius);
        secondProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
        secondProgressPaint.setStyle(Paint.Style.STROKE);
        secondProgressPaint.setStrokeWidth(progressStrokeWidth);
        secondProgressPaint.setColor(0xFF9F9F9F);
        secondProgressPaint.setPathEffect(dashPathEffect);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(progressStrokeWidth);
        progressPaint.setPathEffect(dashPathEffect);
        progressPaint.setShader(new SweepGradient(centerX, centerY, new int[]{Color.RED, Color.YELLOW, 0xFF006400, Color.YELLOW, Color.RED}, new float[]{0, 0.25f, 0.45f, 0.75f, 1}));

        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        shadowPaint.setShader(new RadialGradient(dialPlateShadowCenterX, dialPlateShadowCenterY, dialPlateShadowRadius, 0xff454344, Color.TRANSPARENT, Shader.TileMode.MIRROR));
        shadowPaint.setStyle(Paint.Style.FILL);

        dialPlatePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dialPlatePaint.setColor(Color.WHITE);
        dialPlatePaint.setStyle(Paint.Style.FILL);


        rimPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rimPaint.setColor(0xFFDEDEDE);
        rimPaint.setStyle(Paint.Style.STROKE);
        rimPaint.setStrokeWidth(dialPlateRimWidth);

        outlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outlinePaint.setColor(0xFF12BF6F);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(dialPlateOutlineWidth);

        indicatorDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        indicatorDotPaint.setStyle(Paint.Style.STROKE);
        indicatorDotPaint.setStrokeWidth(dialPlateIndicatorWidth);
        indicatorDotPaint.setColor(0xFF12BF6F);

        greenCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        greenCirclePaint.setColor(0xFF12BF6F);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(externalRect, startAngle, sweepAngle, false, secondProgressPaint);
        canvas.drawCircle(dialPlateShadowCenterX, dialPlateShadowCenterY, dialPlateShadowRadius, shadowPaint);
        canvas.drawCircle(centerX, centerY, dialPlateRadius, dialPlatePaint);
        canvas.drawCircle(centerX, centerY, dialPlateRimRadius, rimPaint);
        canvas.drawCircle(centerX, centerY, dialPlateOutlineRadius, outlinePaint);
        drawProgress(canvas, rotateDegree);

        //canvas.drawCircle(initXCoordinate,initYCoordinate,30,greenCirclePaint);

    }

    private void drawProgress(Canvas canvas, double rotateDegree) {
        canvas.drawArc(externalRect, startAngle, (float) (rotateDegree), false, progressPaint);
        canvas.drawArc(indicatorRect, (float) (startAngle + rotateDegree), 1, false, indicatorDotPaint);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                initXCoordinate = event.getX();
                initYCoordinate = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float xCoordinate=event.getX();
                float yCoordinate=event.getY();
                double distance = Math.pow(xCoordinate-centerX,2) + Math.pow(yCoordinate-centerY,2);

                //check the touch point is in the valid areas.
                boolean validAreas = distance>Math.pow(invalidTouchRadius,2)&&distance<Math.pow(side/2,2);
                float invalidLeftXCoordinate = centerX-invalidCoordinateOffset;
                float invalidRightXCoordinate = centerX+invalidCoordinateOffset;
                float invalidUpperYCoordinate = centerY+invalidCoordinateOffset;

                boolean belowPlate = xCoordinate>invalidLeftXCoordinate&&xCoordinate<invalidRightXCoordinate&&yCoordinate>invalidUpperYCoordinate&&yCoordinate<side;
                //check gesture clockwise or counterclockwise
                if ((!belowPlate)&&validAreas){
                    if (xCoordinate<invalidLeftXCoordinate){
                        if (yCoordinate-initYCoordinate<0){
                            clockWise = true;
                        }else if (yCoordinate-initYCoordinate>0){
                            clockWise = false;
                        }
                    }
                    if (xCoordinate>invalidRightXCoordinate) {
                        if (yCoordinate - initYCoordinate > 0) {
                            clockWise = true;
                        } else if (yCoordinate - initYCoordinate < 0) {
                            clockWise = false;
                        }
                    }

                    if (yCoordinate<centerY){
                        if (xCoordinate-initXCoordinate>0){
                            clockWise = true;
                        }else if (xCoordinate-initXCoordinate<0){
                            clockWise = false;
                        }
                    }

                }else{
                    rotateDegree += 0;
                }

                if (clockWise){
                    rotateDegree += getTouchDegrees(initXCoordinate,initYCoordinate,xCoordinate, yCoordinate);
                }else if(!clockWise){
                    rotateDegree -= getTouchDegrees(initXCoordinate,initYCoordinate,xCoordinate, yCoordinate);
                }

                if (rotateDegree<0){
                    rotateDegree = 0;
                }
                if (rotateDegree>270){
                    rotateDegree = 270;
                }
                    updateByThread();
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;

    }





    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 强制长宽一致
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        Log.i("MyGesture", "onDown");
//        initXCoordinate = e.getX();
//        initYCoordinate = e.getY();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.i("MyGesture", "onScroll:");
        float preXCoordinate = e1.getX();
        float preYCoordinate = e1.getY();
        float xCoordinate = e2.getX();
        float yCoordinate = e2.getY();
        rotateDegree = getTouchDegrees(preXCoordinate,preYCoordinate,xCoordinate, yCoordinate);
        updateByThread();

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    private double getTouchDegrees(float initXCoordinate, float initYCoordinate,float xCoordinate, float yCoordinate) {


        double squareSideB = Math.pow(initXCoordinate - centerX, 2) + Math.pow(initYCoordinate - centerY, 2);
        double squareSideC = Math.pow(xCoordinate - centerX, 2) + Math.pow(yCoordinate - centerY, 2);
        double squareSideA = Math.pow(xCoordinate - initXCoordinate, 2) + Math.pow(yCoordinate - initYCoordinate, 2);

        double cosA = (squareSideB + squareSideC - squareSideA) / (2 * Math.sqrt(squareSideB) * Math.sqrt(squareSideC));
        Log.e("cosA", String.valueOf(cosA));
        double angle = Math.acos(cosA);
        return angle;
    }

    private void updateByThread()
    {
        new Thread()
        {

            @Override
            public void run() {
                super.run();
                postInvalidate();
            }


        }.start();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);//这句话的作用 告诉父view，我的单击事件我自行处理，不要阻碍我。
        return super.dispatchTouchEvent(ev);
    }
}


