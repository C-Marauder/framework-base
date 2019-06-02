package com.xqy.androidx.framework.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.xqy.androidx.framework.R
import com.xqy.androidx.framework.utils.dp
import kotlinx.android.synthetic.main.confirm_dialog.*

class AppDialog: AppCompatDialogFragment() {
    private lateinit var mBuilder: Builder
    companion object {
        fun newBuilder()= Builder()

        private fun getInstance(builder: Builder): AppDialog {
            return AppDialog().apply {
                this.mBuilder = builder
            }
        }

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object :AppCompatDialog(context, theme){
            override fun onBackPressed() {
                if (mBuilder.isCancel){
                    super.onBackPressed()
                }

            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            it.setCanceledOnTouchOutside(mBuilder.isCancel)

            it.window?.let {window->

                val mRadiusDrawable = GradientDrawable()
                mRadiusDrawable.cornerRadius =mBuilder.radius.dp *1f
                mRadiusDrawable.setColor(Color.WHITE)
                window.setBackgroundDrawable(mRadiusDrawable)
                val displayMetrics = resources.displayMetrics
                val mWidth = if (mBuilder.dialogWidth==0){
                    displayMetrics.widthPixels*8/9
                }else{
                    mBuilder.dialogWidth
                }
                val mHeight = if (mBuilder.dialogHeight == 0){
                    -2
                } else{
                    mBuilder.dialogHeight
                }
                window.setLayout(mWidth,mHeight)
                if (mBuilder.dimAmount!=-1f){
                    window.setDimAmount(mBuilder.dimAmount)

                }
            }

        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val mLayoutResId = if (mBuilder.isConfirmType){
            R.layout.confirm_dialog
        }else{
            mBuilder.layoutResId
        }

        return inflater.inflate(mLayoutResId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (this.mBuilder.isConfirmType){
            this.mBuilder.dialogConfig?.let {config->
                contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP,config.contentTextSize)
                cancelView.setTextSize(TypedValue.COMPLEX_UNIT_SP,config.buttonTextSize)
                confirmView.setTextSize(TypedValue.COMPLEX_UNIT_SP,config.buttonTextSize)
                contentView.setTextColor(config.contentTextColor)
                cancelView.setTextColor(config.cancelTextColor)
                confirmView.setTextColor(config.confirmTextColor)
                cancelView.setOnClickListener {
                    config.onCancelClick()
                    dismiss()
                }
                confirmView.setOnClickListener {
                    val isDismiss = config.onConfirmClick()
                    if (isDismiss){
                        dismiss()
                    }

                }
            }
        }
        mBuilder.listener(view)
    }

    class Builder{
        internal var isCancel:Boolean = true
        internal var layoutResId:Int = 0
        internal var listener:(view:View)->Unit={}
        internal var dimAmount:Float = -1f
        internal var isConfirmType:Boolean = false
        internal var dialogConfig: DialogConfig?=null
        internal var dialogWidth:Int=0
        internal var dialogHeight:Int=0
        internal var radius:Int = 8.dp
        fun setAlpha(alpha:Float)=apply {
            dimAmount = alpha
        }
        fun handleListener(listener:(view:View)->Unit)=apply {
            this.listener = listener
        }
        fun isCancel(isCancel:Boolean)=apply {
            this.isCancel = isCancel
        }
        fun layoutResId(layoutResId:Int)=apply {
            this.layoutResId = layoutResId
        }
        fun isDefaultConfirmType(isConfirmType:Boolean,dialogConfig: DialogConfig) =apply {
            this.isConfirmType = isConfirmType
            this.dialogConfig =dialogConfig
        }
        fun dialogWidth(width:Int)=apply {
            this.dialogWidth =width
        }
        fun dialogHeight(height:Int) =apply {
            this.dialogHeight =height
        }
        fun dialogRadius(radius:Int)=apply {
            this.radius = radius
        }

        fun build()= getInstance(this)
    }

}