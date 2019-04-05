package com.xqy.androidx.ui

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.nio.file.Files

inline fun <reified T : RecyclerView> T.initRecyclerView(): DelegateAdapter {
    this.setHasFixedSize(true)
    val mLayoutManager = VirtualLayoutManager(this.context)
    this.layoutManager = mLayoutManager
    val delegateAdapter = DelegateAdapter(mLayoutManager, true)
    this.adapter = delegateAdapter
    return delegateAdapter
}

/**
 * 状态栏透明
 */
inline fun <reified T : AppCompatActivity> T.transparentStatusBar() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT

    }


}

/**
 * 隐藏键盘
 */
inline fun <reified T : Application> T.hideSoftKeyboard(view: View) {
    val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

inline fun <reified T : Fragment> T.getInstance(vararg pair: Pair<String, Any>): T {
    val fragment = this::class.java.newInstance()
    if (!pair.isNullOrEmpty()) {
        val bundle = Bundle()
        pair.forEach {
            val value = it.second
            val key = it.first
            when (value) {
                is String -> bundle.putString(key, value)
                is Int -> bundle.putInt(key, value)
                is Boolean -> bundle.putBoolean(key, value)
                is Parcelable -> bundle.putParcelable(key, value)
                is ArrayList<*> -> {
                    if (value[0] is String) {
                        bundle.putStringArrayList(key, value as ArrayList<String>?)
                    } else {
                        bundle.putParcelableArrayList(key, value as ArrayList<out Parcelable>?)
                    }
                }
            }
        }
        fragment.arguments = bundle

    }
    return fragment
}

inline fun <reified T : Application> T.appColor(resId: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.getColor(resId)
    } else {
        ContextCompat.getColor(this, resId)
    }
}


val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.dp: Int get() = Math.round(this * Resources.getSystem().displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)

val Int.sp: Float get() = this * Resources.getSystem().displayMetrics.scaledDensity + 0.5f

inline fun <reified T : Application> T.toast(msg: String, gravity: Int = Gravity.BOTTOM, isLong: Boolean = true) {
    val type = if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    Toast.makeText(this, msg, type).apply {
        if (gravity != Gravity.BOTTOM) {
            this.setGravity(gravity, 0, 0)
        }
    }.show()
}

inline fun <reified T> T.alertMsg(
    msg: String,
    bgColor: Int = android.R.color.black,
    msgColor: Int = android.R.color.white,
    duration: Int = Snackbar.LENGTH_LONG,
    crossinline onDismiss: () -> Unit
) {
    val snackbar = when (this) {
        is AppCompatActivity -> {
            val contentView = this.findViewById<View>(android.R.id.content)
            Snackbar.make(contentView, msg, Snackbar.LENGTH_LONG)
        }
        is Fragment -> this.view?.let {
            Snackbar.make(it, msg, duration)
        }

        else -> throw Exception("${T::class.java.simpleName} is not subType of AppCompatActivity or Fragment")
    }
    if (bgColor != android.R.color.black) {
        snackbar!!.view.setBackgroundResource(bgColor)
    }
    snackbar!!.view.findViewById<TextView>(R.id.snackbar_text).apply {

        this.setTextColor(ContextCompat.getColor(this.context, msgColor))
    }
    val callback = object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)
            onDismiss()
            snackbar.removeCallback(this)
        }

    }
    snackbar.apply {
        //this.duration = duration
        this.addCallback(callback)


    }.show()

}

inline fun <reified T> T.appLog(log: Any) {
    Log.e(T::class.java.simpleName, log.toString())
}

inline fun <reified T : Application> T.saveToFile(fileName: String, content: String) {
    openFileOutput(fileName, Context.MODE_PRIVATE).use {
        it.write(content.toByteArray())
    }

}

inline fun <reified T : Application> T.readFromFile(fileName: String): String {
    var content = ""
    openFileInput(fileName).use {
        it.reader().forEachLine { text ->
            content = text + content
        }
    }

    return content
}

/**
 * 内部内存不足时，可能会被系统删除
 */
inline fun <reified T : Application> T.saveToCache(fileName: String, cache: String) {
  File( this.cacheDir,fileName).writeText(cache)

}

inline fun <reified T : Application> T.getFromCache(fileName: String): String? {

    return File(this.cacheDir,fileName).apply {
        appLog(this.absolutePath)
    }.readText()

    //return content

}

/* Checks if external storage is available for read and write */
inline fun <reified T> T.isSDCardUsable(usable: () -> Unit, unUsable: () -> Unit) {
    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
        usable()
    } else {
        unUsable()
    }
}

inline fun <reified T : Application> T.saveToSDCardPubDir(fileName: String, content: String) {
    isSDCardUsable({
        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName).writeText(content)
    }, {
        appLog("SDCard is unavailable")
    })

}

inline fun <reified T : Application> T.getFromSDCardPubDir(fileName: String): String? {
    var content: String? = null
    isSDCardUsable({
        content =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName).readText()
    }, {
        appLog("SDCard is unavailable")
    })
    return content
}

inline fun <reified T : Application> T.saveToSDCardPrivateDir(type: String, fileName: String, content: String) {
    isSDCardUsable({
        File(getExternalFilesDir(type), fileName).writeText(content)
    }, {
        appLog("SDCard is unavailable")
    })

}

inline fun <reified T : Application> T.getFromSDCardPrivateDir(type: String, fileName: String): String? {
    var content: String? = null
    isSDCardUsable({
        content = File(getExternalFilesDir(type), fileName).readText()
    }, {
        appLog("SDCard is unavailable")
    })
    return content
}

inline fun <reified T : Application> T.deleteCache(fileName: String) {
    val file = File(this.cacheDir.absolutePath).apply {
        appLog(this.absolutePath+"/$fileName")
    }
    if (file.isFile){
        file.deleteOnExit()
    }else{
        file.listFiles().forEachIndexed { index, childFile ->
            childFile.deleteOnExit()
        }

    }

}

inline fun <reified T : Application> T.deleteAllFile() {
    cacheDir.deleteOnExit()
    isSDCardUsable(
        {

            //getExternalFilesDir(type)?.deleteOnExit()
        }, {}
    )
}