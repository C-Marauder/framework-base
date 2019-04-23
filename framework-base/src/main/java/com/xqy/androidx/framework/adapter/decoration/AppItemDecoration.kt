package com.xqy.androidx.framework.adapter.decoration

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.xqy.androidx.framework.utils.dp

class AppItemDecoration(private val left:Int,private val right:Int,color:Int): RecyclerView.ItemDecoration() {
    private lateinit var mPaint: Paint
    init {
        mPaint = Paint().apply {
            this.flags = Paint.ANTI_ALIAS_FLAG
            this.isAntiAlias = true
            this.color = color
//            this.strokeWidth = 1.px*1f
            this.style = Paint.Style.FILL
        }
    }
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val childCount = parent.childCount
        for (i in 0 until childCount){
            val child = parent.getChildAt(i)
            c.drawLine(child.left*1f,child.bottom*1f,child.right*1f,child.bottom*1f,mPaint)

        }

    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        outRect.left = left.dp
        outRect.right = right.dp
    }
}