package com.zft.bluetooth.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.base.baselib.common.utils.ColorUtil;

/***
 *
 * 人体正常体温平均在36～37℃之间（腋窝），
 * 超出37.3℃就是发热，
 * 37.3～38℃是低烧，
 * 38.1～40℃是高烧。
 * 40℃ 以上随时有生命危险。
 *
 *      35~36   低温
 *      36~37   正常
 *      37.3~38 低烧
 *      38.1~40 高烧
 *      40~41   生命危险
 *
 */
public class TemperatureView extends DrawView {


    Paint mArcPaint;
    float mArcStrokeWidth;

    float mArcRadius;


    /***夹角角度*/
    float mArcEmptyAngle = 55;
    //  圆弧开始的角度
    final float mArcStartAngle = 0;
    //  圆弧的度数
    float mArcSweepAngle = 360 - mArcEmptyAngle * 2;

    float ratato = 90 + mArcEmptyAngle;

    /***温度占比*/
    float temp1 = 1f * (36 - 35) / (41 - 35);
    float temp2 = (37.3f - 36) / (41 - 35);
    float temp3 = (38.1f - 37.3f) / (41 - 35);
    float temp4 = (40 - 38.1f) / (41 - 35);
    float temp5 = 1f * (41 - 40) / (41 - 35);

    float temps[] = new float[]{
            temp1,
            temp2,
            temp3,
            temp4,
            temp5,
    };
    int colors[] = new int[]{
            0xFF64e5e3,
            0xff78d4f8,
            0xffc3c3e4,
            0xfff755e2,
            0xffec8fbf,
            0xfffe8e8d
    };
    String texts[] = new String[]{
            "36℃",
            "37.3℃",
            "38.1℃",
            "40℃",
            "41℃",
    };

    /***以下是进度*/
    private float mProgressStrokeWidth;
    private float mProgressRadius;
    private Paint mProgressPaint;
    private Paint mTextPaint;

    //  每个区域的占比
    private float[] radio;

    private Handler mHandler = new Handler();
    /***
     * 范围35 ~ 41, 超出则算边界值
     */
    private float value = 0;
    private float valueRadio;
    private float changeValueRadio;
    private float unit;
    private boolean isTure;
    private Paint mCenterTextPaint;


    public TemperatureView(Context context) {
        super(context);
        init();

    }

    public TemperatureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public TemperatureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        radio = new float[temps.length + 1];
        for (int i = 0; i < temps.length; i++) {
            if (i == 0) {
                radio[i] = mArcSweepAngle * temps[i] / 2;
            } else {
                radio[i] = mArcSweepAngle * temps[i] / 2 + mArcSweepAngle * temps[i - 1] / 2 + radio[i - 1];
                if (i == temps.length - 1) {
//                    radio[temps.length] =
                    radio[i + 1] = mArcSweepAngle;

                }
            }
        }

    }

    private void initPaint() {
        mArcPaint = new Paint();
        mArcPaint.setStrokeWidth(mArcStrokeWidth);
        mArcPaint.setColor(0xffff0000);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);
        mArcPaint.setStyle(Paint.Style.STROKE);

        mProgressPaint = new Paint();
        mProgressPaint.setStrokeWidth(mProgressStrokeWidth);
        mProgressPaint.setColor(0xffcccccc);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setTextSize(20);
//        mProgressPaint.setDither(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setTextSize(mProgressStrokeWidth * 2 / 3);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(0xffcccccc);

        mCenterTextPaint = new Paint();
        mCenterTextPaint.setTextSize((mArcStrokeWidth));
        mCenterTextPaint.setColor(0xff000000);
        mCenterTextPaint.setAntiAlias(true);
        mCenterTextPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mArcStrokeWidth = maxRadius / 6f;
        mArcRadius = maxRadius - mArcStrokeWidth / 2f - maxRadius / 2;
        mProgressStrokeWidth = mArcStrokeWidth * 2 / 4;
        mProgressRadius = mArcRadius + mProgressStrokeWidth * 1.8f;
        float v = mArcSweepAngle % 2;
        if (v == 0) {
            mArcSweepAngle++;
        }
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.rotate(ratato);
//      绘制表盘
        drawWacthDial(canvas);
//      绘制进度
        drawProgress(canvas);
//      绘制文字
        drawProgressText(canvas);
        canvas.restore();
        drawCenterText(canvas);
    }

    /***
     * 绘制中心文字
     * @param canvas
     */
    private void drawCenterText(Canvas canvas) {
        canvas.drawText(String.format("%s℃", value + ""), 0, 0 + mArcStrokeWidth / 2, mCenterTextPaint);
    }

    /***
     * 绘制文字
     * @param canvas
     */
    private void drawProgressText(Canvas canvas) {
        float startAngle = mArcStartAngle;

        for (int i = 0; i < temps.length; i++) {
            float temp = temps[i];
            String text = texts[i];
            final float sweepAngle = mArcSweepAngle * temp;
            canvas.save();
            canvas.rotate(startAngle + sweepAngle + 90);
            canvas.drawText(text, 0, -mProgressRadius - (mProgressStrokeWidth ), mTextPaint);
            canvas.restore();
            startAngle += sweepAngle;
        }
    }

    private void drawProgress(Canvas canvas) {
        float startAngle = mArcStartAngle;
        float radioSweepAngle = changeValueRadio * mArcSweepAngle;

        for (float i = 0; i < mArcSweepAngle; i = 2 + i) {
            int startColor = 0;
            int endColor = 0;
            float offset = 1;

            if (i <= radioSweepAngle) {
                if (i < radio[0]) {
                    offset = (i - 0) / radio[0];
                    startColor = colors[0];
                    endColor = colors[0];
                } else if (i < radio[1]) {
                    offset = (i - radio[0]) / (radio[1] - radio[0]);
                    startColor = colors[0];
                    endColor = colors[1];
                } else if (i < radio[2]) {
                    offset = (i - radio[1]) / (radio[2] - radio[1]);
                    startColor = colors[1];
                    endColor = colors[2];
                } else if (i < radio[3]) {
                    offset = (i - radio[2]) / (radio[3] - radio[2]);
                    startColor = colors[2];
                    endColor = colors[3];
                } else if (i < radio[4]) {
                    offset = (i - radio[3]) / (radio[4] - radio[3]);
                    startColor = colors[3];
                    endColor = colors[4];
                } else if (i < radio[5]) {
                    offset = (i - radio[4]) / (radio[5] - radio[4]);
                    startColor = colors[4];
                    endColor = colors[4];
                }
                int evaluator = ColorUtil.evaluator(offset, startColor, endColor);
                if(evaluator==-1){
                    evaluator = startColor;
                }
                mProgressPaint.setColor(evaluator);
            } else {
                mProgressPaint.setColor(0xffcccccc);
            }
//            startColor = 0xfffffff;
//            endColor = colors[1];


            RectF oval = new RectF(-mProgressRadius, -mProgressRadius, mProgressRadius, mProgressRadius);
            canvas.drawArc(oval, startAngle + i, 1, false, mProgressPaint);
        }
    }

    /***
     * 绘制表盘
     * @param canvas
     */
    private void drawWacthDial(Canvas canvas) {

        float startAngle = mArcStartAngle;
        for (int i = 0; i < temps.length; i++) {
            float temp = temps[i];
            int color = colors[i];

            final float sweepAngle = mArcSweepAngle * temp;
//          中间留间隔
            float sweepAngle2 = sweepAngle;
            if (i < temps.length - 1) {
                sweepAngle2 = sweepAngle - 1;
            }
            mArcPaint.setColor(color);
            RectF oval = new RectF(-mArcRadius, -mArcRadius, mArcRadius, mArcRadius);
            canvas.drawArc(oval, startAngle, sweepAngle2, false, mArcPaint);
            startAngle += sweepAngle;
        }
    }

    public void setValue(float value) {
        this.value = value;
//        算出占比
        if (value < 35) {
            value = 35;
        } else if (value > 41) {
            value = 41;
        }
        valueRadio = (value - 35) / (41 - 35);

        mHandler.removeCallbacksAndMessages(null);
        isTure = valueRadio - changeValueRadio >= 0 ? true : false;
        unit = (valueRadio - changeValueRadio) / 50;
        mHandler.postDelayed(mRunable, DELAY);
    }

    final long DELAY = 10;
    Runnable mRunable = new Runnable() {
        @Override
        public void run() {
            changeValueRadio += unit;
            if ((isTure && changeValueRadio >= valueRadio) || (!isTure && changeValueRadio <= valueRadio)) {
                mHandler.removeCallbacksAndMessages(null);
            } else {
                mHandler.postDelayed(mRunable, DELAY);
            }
            invalidate();
        }
    };
}
