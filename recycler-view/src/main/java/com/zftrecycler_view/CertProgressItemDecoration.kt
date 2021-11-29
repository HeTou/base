package com.zftrecycler_view

import android.graphics.*
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.base.baselib.common.utils.ScreenUtil
import com.base.baselib.common.utils.Utils

class CertProgressItemDecoration : RecyclerView.ItemDecoration() {


    /** doing的下标，后台传递的数值从1开始 */
    var activeStepIndex = 5

    /** 除了虚线之外的Paint */
    private val paint = Paint()

    /** 虚线相关 */
    private val dashPath = Path()
    private val dashPaint = Paint()

    init {
        paint.strokeWidth = ScreenUtil.dp2px(Utils.getApp(), 1F).toFloat()
        paint.color = Color.WHITE
        paint.isAntiAlias = true

        dashPaint.strokeWidth = ScreenUtil.dp2px(Utils.getApp(), 1F).toFloat()
        dashPaint.style = Paint.Style.STROKE
        dashPaint.isAntiAlias = true
        dashPaint.pathEffect = DashPathEffect(floatArrayOf(ScreenUtil.dp2px(Utils.getApp(), 6F).toFloat(), ScreenUtil.dp2px(Utils.getApp(), 3F).toFloat()), 0F)
    }

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        // item左边的部分距离左边68dp，所以左边的时间轴部分设置为68dp
        // 其他三个方面都不需要额外处理
        outRect?.set(ScreenUtil.dp2px(Utils.getApp(), 68F), 0, 0, 0)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {}

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        parent ?: return
        c ?: return

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)

            // 需要绘制的矩形坐标
            val left = 0F
            val top = child.top.toFloat()
            val right = ScreenUtil.dp2px(Utils.getApp(), 68F).toFloat()
            val bottom = child.bottom.toFloat()

            // 绘制背景
            // 由于整个RecyclerView是没有设置颜色的，颜色都由item进行绘制
            // 但是我们设置了item view左边的inset，所以这里需要绘制上背景颜色
            paint.color = Color.WHITE
            c.drawRect(left, top, right, bottom, paint)

            val recyclerAdapter = parent.adapter
            // 当前要绘制的data的下标
            val dataIndex = i
            // data的总数
            val dataCount = parent.childCount

            // 时间轴的中心
            val centerX = (right - left) / 2
            // 时间轴icon所在的centerY值
            // doing状态icon size为22dp
            val bPartTop = ScreenUtil.dp2px(Utils.getApp(), 11F) + top
            // 当前item的上半根线条
            val lastDataIndex = dataIndex - 1
            if (dataIndex != 0) {
                if (lastDataIndex < activeStepIndex) {
                    paint.color = parent.context.getColor(R.color.black_666666)
                    c.drawLine(centerX, top, centerX, bPartTop, paint)
                } else if (lastDataIndex != dataCount - 1) {
                    dashPaint.color = parent.context.getColor(R.color.black_999999)
                    dashPath.reset()
                    dashPath.moveTo(centerX, top)
                    dashPath.lineTo(centerX, bPartTop)
                    c.drawPath(dashPath, dashPaint)
                }
            }
            // 当前item的下半根线条
            if (dataIndex < activeStepIndex) {
                paint.color = parent.context.getColor(R.color.black_666666)
                c.drawLine(centerX, bPartTop, centerX, bottom, paint)
            } else if (dataIndex != dataCount - 1) {
                dashPaint.color = parent.context.getColor(R.color.black_999999)
                dashPath.reset()
                dashPath.moveTo(centerX, bPartTop)
                dashPath.lineTo(centerX, bottom)
                c.drawPath(dashPath, dashPaint)
            }
            // 画icon
            val bitmap = when {
                dataIndex < activeStepIndex -> BitmapFactory.decodeResource(parent.context.resources, R.drawable.time_line1)
                dataIndex == activeStepIndex -> BitmapFactory.decodeResource(parent.context.resources, R.drawable.time_line2)
                else -> BitmapFactory.decodeResource(parent.context.resources, R.drawable.time_line1)
            }
            val bitmapX = centerX - (bitmap.width shr 1)
            val bitmapY = bPartTop - (bitmap.height shr 1)
            c.drawBitmap(bitmap, bitmapX, bitmapY, paint)
        }
    }
}