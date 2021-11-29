package com.zft.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/***
 * 线性图表
 */
public class LineView extends View {


    public float CIRCLE_DEGREE;

    public Animation anim;

    public int backGroundColor = Color.parseColor("#00000000");

    public LinearGradient bgGradient;

    public float chartEndY = dip2px(350);

    public float chartStartY = this.paddingTop;

    public float intervalWith = 0.0F;

    public boolean isSet = true;

    public float lineCounts = 11.0F;

    /***
     * 折现数据
     */
    public ArrayList<String> lineDataList;

    public Context mContext;

    public float mCurrentDegree = 0.0F;

    public float mHeight;

    public float mWidth;

    public float maxLineData;

    public float minLineData;

    public OnDataErrorListener onDataErrorListener;

    public float paddingLift = dip2px(10);

    public float paddingRight = dip2px(4);

    public float paddingTop = dip2px(20);

    public Paint paintBlueCircle;

    public Paint paintBlueLine;

    public Paint paintBlueText;

    public Paint paintDottedLineWhite;

    public Paint paintDurationText;

    public Paint paintEndLine;

    public Paint paintGreenCircle;

    public Paint paintGreenLine;

    public Paint paintGreentext;

    public Paint paintLiftText;

    public Paint paintLine;

    public Paint paintLineWhite;

    public Paint paintRect;

    public Paint paintRedCircle;

    public Paint paintRedLine;

    public Paint paintRedText;

    public Paint paintRightText;

    public Paint paintRightTextBlack;

    public Paint paintRightTextWhite;

    public Paint paintWhiteCircle;

    public Paint paintWhiteRect;

    public Paint paintYearText;

    public double symbol;

    public int totalTime;

    public int value;

    public ArrayList<String> yearsAllDataList;

    public ArrayList<String> yearsDataList;

    public Paint zxbPaint;

    public LineView(Context context) {
        super(context);
    }

    public LineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public LineView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        this.mContext = paramContext;
        initPaint();
    }

    /***
     * 查找最大值
     * @param paramArrayList
     * @return
     */
    private float getMaxString(ArrayList<String> paramArrayList) {
        float f;
        if (paramArrayList.size() > 0) {
            f = Float.parseFloat(paramArrayList.get(0));
        } else {
            f = 0.0F;
        }
        byte b = 1;
        while (b < paramArrayList.size()) {
            float f1 = Float.parseFloat(paramArrayList.get(b));
            float f2 = f;
            if (f1 > f)
                f2 = f1;
            b++;
            f = f2;
        }
        return f;
    }

    private float getMinString(ArrayList<String> paramArrayList) {
        float f1;
        if (paramArrayList.size() > 0) {
            f1 = Float.parseFloat(paramArrayList.get(0));
        } else {
            f1 = 0.0F;
        }
        byte b = 1;
        float f2;
        for (f2 = f1; b < paramArrayList.size(); f2 = f1) {
            float f = Float.parseFloat(paramArrayList.get(b));
            f1 = f2;
            if (f < f2)
                f1 = f;
            b++;
        }
        return f2;
    }

    private int getTextHeight(Paint paramPaint, String paramString) {
        Rect rect = new Rect();
        paramPaint.getTextBounds(paramString, 0, paramString.length(), rect);
        return rect.height();
    }

    private int getTextWidth(Paint paramPaint, String paramString) {
        return (int) paramPaint.measureText(paramString);
    }

    private void initPaint() {
        this.paintRect = new Paint(1);
        this.paintRect.setColor(Color.parseColor("#177EE6"));
        this.paintLine = a(this.paintRect, Paint.Style.FILL, 1);
        this.paintLine.setColor(Color.parseColor("#444444"));
        this.paintLine.setStrokeWidth(1.0F);
        this.paintLineWhite = a(this.paintLine, Paint.Style.STROKE, 1);
        this.paintLineWhite.setColor(Color.parseColor("#F6F6F6"));
        this.paintLineWhite.setStrokeWidth(1.0F);
        this.paintDottedLineWhite = a(this.paintLineWhite, Paint.Style.STROKE, 1);
        this.paintDottedLineWhite.setColor(Color.parseColor("#F6F6F6"));
        this.paintDottedLineWhite.setStrokeWidth(1.0F);
        this.paintDottedLineWhite.setStyle(Paint.Style.STROKE);
        this.paintDottedLineWhite.setPathEffect((PathEffect) new DashPathEffect(new float[]{3.0F, 2.0F}, 0.0F));
        this.paintBlueLine = new Paint(1);
        this.paintBlueLine.setColor(Color.parseColor("#049FF8"));
        this.paintBlueLine.setStrokeWidth(dip2px(1));
        this.paintBlueLine.setStrokeJoin(Paint.Join.ROUND);
        this.paintBlueCircle = a(this.paintBlueLine, Paint.Style.STROKE, 1);
        this.paintBlueCircle.setColor(Color.parseColor("#177EE6"));
        this.paintBlueCircle.setStrokeWidth(2.0F);
        this.paintWhiteRect = a(this.paintBlueCircle, Paint.Style.FILL, 1);
        this.paintWhiteRect.setColor(Color.parseColor("#FFFFFF"));
        this.paintWhiteRect.setAntiAlias(true);
        this.paintWhiteRect.setStrokeWidth(2.0F);
        this.paintYearText = a(this.paintWhiteRect, Paint.Style.FILL, 1);
        this.paintYearText.setColor(Color.parseColor("#AAAAAA"));
        this.paintYearText.setTextSize(sp2px(8));
        this.paintYearText.setStrokeWidth(2.0F);
        this.paintDurationText = a(this.paintYearText, Paint.Align.CENTER, 1);
        this.paintDurationText.setColor(Color.parseColor("#AAAAAA"));
        this.paintDurationText.setTextSize(sp2px(8));
        this.paintDurationText.setStrokeWidth(2.0F);
        this.paintBlueText = a(this.paintDurationText, Paint.Align.CENTER, 1);
        this.paintBlueText.setColor(Color.parseColor("#177EE6"));
        this.paintBlueText.setTextSize(sp2px(12));
        this.paintBlueText.setStrokeWidth(2.0F);
        this.paintLiftText = a(this.paintBlueText, Paint.Align.CENTER, 1);
        this.paintLiftText.setColor(Color.parseColor("#AAAAAA"));
        this.paintLiftText.setTextSize(sp2px(11));
        this.paintLiftText.setStrokeWidth(2.0F);
        this.paintRightText = a(this.paintLiftText, Paint.Align.RIGHT, 1);
        this.paintRightText.setColor(Color.parseColor("#AAAAAA"));
        this.paintRightText.setTextSize(sp2px(8));
        this.paintRightText.setStrokeWidth(2.0F);
        this.paintRightTextWhite = a(this.paintRightText, Paint.Align.LEFT, 1);
        this.paintRightTextWhite.setColor(Color.parseColor("#FFFFFF"));
        this.paintRightTextWhite.setTextSize(sp2px(11));
        this.paintRightTextWhite.setStrokeWidth(2.0F);
        this.paintRightTextBlack = a(this.paintRightTextWhite, Paint.Align.LEFT, 1);
        this.paintRightTextBlack.setColor(Color.parseColor("#333333"));
        this.paintRightTextBlack.setTextSize(sp2px(11));
        this.paintRightTextBlack.setStrokeWidth(2.0F);
        this.paintEndLine = a(this.paintRightTextBlack, Paint.Align.LEFT, 1);
        this.paintEndLine.setColor(Color.parseColor("#FFFFFF"));
        this.paintEndLine.setStrokeWidth(dip2px(1));
        this.paintRedLine = a(this.paintEndLine, Paint.Style.STROKE, 1);
        this.paintRedLine.setColor(Color.parseColor("#DC313E"));
        this.paintRedLine.setStrokeWidth(dip2px(2));
        this.paintRedText = a(this.paintRedLine, Paint.Style.STROKE, 1);
        this.paintRedText.setColor(Color.parseColor("#DC313E"));
        this.paintRedText.setTextSize(sp2px(12));
        this.paintRedText.setStrokeWidth(2.0F);
        this.paintRedCircle = a(this.paintRedText, Paint.Align.CENTER, 1);
        this.paintRedCircle.setColor(Color.parseColor("#DC313E"));
        this.paintRedCircle.setStrokeWidth(dip2px(4));
        this.paintGreenLine = a(this.paintRedCircle, Paint.Style.FILL, 1);
        this.paintGreenLine.setColor(Color.parseColor("#00E367"));
        this.paintGreenLine.setStrokeWidth(dip2px(2));
        this.paintGreentext = a(this.paintGreenLine, Paint.Style.STROKE, 1);
        this.paintGreentext.setColor(Color.parseColor("#00E367"));
        this.paintGreentext.setTextSize(sp2px(12));
        this.paintGreentext.setStrokeWidth(2.0F);
        this.paintGreenCircle = a(this.paintGreentext, Paint.Align.CENTER, 1);
        this.paintGreenCircle.setColor(Color.parseColor("#00E367"));
        this.paintGreenCircle.setStrokeWidth(dip2px(4));
        this.paintWhiteCircle = a(this.paintGreenCircle, Paint.Style.FILL, 1);
        this.paintWhiteCircle.setColor(Color.parseColor("#FFFFFF"));
        this.paintWhiteCircle.setStrokeWidth(dip2px(2));
        this.paintBlueCircle = a(this.paintWhiteCircle, Paint.Style.STROKE, 1);
        this.paintBlueCircle.setColor(Color.parseColor("#049FF8"));
        this.paintBlueCircle.setStrokeWidth(dip2px(1));
        this.paintBlueCircle.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (this.mHeight <= dip2px(200))
            this.mHeight = dip2px(200);
        this.chartEndY = this.mHeight - dip2px(30);
        ArrayList<String> arrayList = this.lineDataList;
        if (arrayList != null && arrayList.size() > 0) {
            this.minLineData = getMinString(this.lineDataList);
            this.maxLineData = getMaxString(this.lineDataList);
        }
        arrayList = this.lineDataList;
        if (arrayList != null && arrayList.size() > 1) {
            final int LIST_SIZE = this.lineDataList.size();
//          间歇
            this.intervalWith = (this.mWidth - this.paddingLift - this.paddingRight) / 40.0F * 25.0F / (this.lineDataList.size() - 1);
            float maxDiff = this.maxLineData - this.minLineData;
            Path path = new Path();
//          倒数第二个的值
            float f2 = Float.parseFloat(this.lineDataList.get(LIST_SIZE - 2));

            float f3 = this.minLineData;
            float f4 = this.chartEndY;
            f2 = (f2 - f3) / maxDiff;
            f4 -= (f4 - this.chartStartY) * f2;
            if (this.isSet) {
                f3 = Float.parseFloat(this.lineDataList.get(LIST_SIZE - 1));
                float f = this.minLineData;
                f2 = this.chartEndY;
                maxDiff = (f3 - f) / maxDiff;
                this.CIRCLE_DEGREE = f2 - (f2 - this.chartStartY) * maxDiff - f4;
                startAnimation(this.anim);
            } else {
                path.moveTo(this.paddingLift, this.mCurrentDegree + f4);
                f3 = this.paddingLift;
                f2 = this.intervalWith;
                maxDiff = (LIST_SIZE - 1);
                path.lineTo(f2 * maxDiff + f3 + dip2px(20), this.mCurrentDegree + f4);
                f3 = getTextWidth(this.paintRightTextBlack, (String) b(this.lineDataList, 1));
                f2 = getTextHeight(this.paintRightTextBlack, (String) b(this.lineDataList, 1));
                RectF rectF = new RectF();
                float f = this.paddingLift;
                rectF.left = this.intervalWith * maxDiff + f + dip2px(20);
                rectF.top = this.mCurrentDegree + f4 - dip2px(8);
                f = this.paddingLift;
                rectF.right = this.intervalWith * maxDiff + f + dip2px(28) + f3;
                rectF.bottom = this.mCurrentDegree + f4 + f2;
                canvas.drawRoundRect(rectF, dip2px(8), dip2px(8), this.paintWhiteRect);
                String str = (String) b(this.lineDataList, 1);
                f3 = this.paddingLift;
                f2 = dip2px(18);
                canvas.drawText(str, this.intervalWith * maxDiff + f3 + f2 + dip2px(6), this.mCurrentDegree + f4 + dip2px(4), this.paintRightTextBlack);
                f2 = this.paddingLift;
                maxDiff = this.intervalWith * maxDiff + f2;
                f4 += this.mCurrentDegree;
                canvas.drawPath(path, this.paintEndLine);
                if (maxDiff > 0.0F && f4 > 0.0F) {
                    canvas.drawCircle(maxDiff, f4, dip2px(3), this.paintWhiteCircle);
                    canvas.drawCircle(maxDiff, f4, dip2px(2), this.paintBlueCircle);
                }
            }
        }
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            this.mWidth = (right - left);
            this.mHeight = (bottom - top);
            setBackgroundColor(this.backGroundColor);
        }
        super.onLayout(changed, left, top, right, bottom);
    }


    public void setValues(ArrayList<String> paramArrayList1, ArrayList<String> paramArrayList2, boolean paramBoolean, int paramInt) {
        if (paramArrayList1 != null && paramArrayList1.size() > 0) {
            this.lineDataList = paramArrayList1;
            this.yearsDataList = paramArrayList2;
            this.isSet = true;
            postInvalidate();
        } else {
            this.onDataErrorListener.onError();
        }
    }


    public int dip2px(int paramInt) {
        float f = (getContext().getResources().getDisplayMetrics()).density;
        return (int) ((paramInt * f) + 0.5D);
    }

    public void setOnDataErrorListener(OnDataErrorListener paramOnDataErrorListener) {
        this.onDataErrorListener = paramOnDataErrorListener;
    }

    public int sp2px(int paramInt) {
        float f = (getContext().getResources().getDisplayMetrics()).scaledDensity;
        return (int) (paramInt * f + 0.5F);
    }


    public interface OnDataErrorListener {
        void onError();
    }

    public interface OnDownClickListener {
        void onClick(int param1Int, float param1Float1, float param1Float2);
    }

    public static Paint a(Paint paramPaint, Paint.Align paramAlign, int paramInt) {
        paramPaint.setTextAlign(paramAlign);
        return new Paint(paramInt);
    }

    public static Paint a(Paint paramPaint, Paint.Style paramStyle, int paramInt) {
        paramPaint.setStyle(paramStyle);
        return new Paint(paramInt);
    }


    public static Object b(ArrayList paramArrayList, int paramInt) {
        return paramArrayList.get(paramArrayList.size() - paramInt);
    }
}
