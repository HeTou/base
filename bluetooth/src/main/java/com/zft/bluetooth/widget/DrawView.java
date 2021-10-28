package com.zft.bluetooth.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class DrawView extends View {
    /***控件的宽度*/
    int vWidht;
    /***控件的高度*/
    int vHeight;

    /***一半的高度*/
    float halfHeight;
    /***一半的宽度*/
    float halfWidth;

    /***可显示的圆的最大的半径*/
    float maxRadius;

    Paint defaultPaint;

    public DrawView(Context context) {
        super(context);
        init();

    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        defaultPaint = new Paint();
        defaultPaint.setColor(0xffcccccc);
        defaultPaint.setStrokeWidth(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.vWidht = w;
        this.vHeight = h;
//      算出控件的中心点
        this.halfWidth = w / 2f;
        this.halfHeight = h / 2f;

        this.maxRadius = Math.min(halfHeight, halfWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(halfWidth, halfHeight);

//      绘制辅助线
        canvas.drawLine(-halfWidth, 0, halfWidth, 0, defaultPaint);
        canvas.drawLine(0, -halfHeight, 0, halfHeight, defaultPaint);
    }
}
