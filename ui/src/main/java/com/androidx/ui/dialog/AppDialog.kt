package com.androidx.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment

class AppDialog: AppCompatDialogFragment() {
    private lateinit var mBuilder: Builder
    companion object {
        fun newBuilder()=Builder()

        private fun getInstance(builder: Builder):AppDialog{
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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.let {
            it.setCanceledOnTouchOutside(mBuilder.isCancel)

            it.window?.let {window->
                window.setBackgroundDrawableResource(android.R.color.transparent)
                if (mBuilder.dimAmount!=-1f){
                    window.setDimAmount(mBuilder.dimAmount)

                }
            }
        }
        return inflater.inflate(mBuilder.layoutResId,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBuilder.listener(view)
    }

    class Builder{
        internal var isCancel:Boolean = true
        internal var layoutResId:Int = 0
        internal var listener:(view:View)->Unit={}
        internal var dimAmount:Float = -1f
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

        fun build()=AppDialog.getInstance(this)
    }

}