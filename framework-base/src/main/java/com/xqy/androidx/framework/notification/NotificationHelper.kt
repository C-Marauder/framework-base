package com.xqy.androidx.framework.notification

import android.animation.ObjectAnimator
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.*
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import com.xqy.androidx.framework.R
import com.xqy.androidx.framework.databinding.NotificationBinding
import com.xqy.androidx.framework.notification.model.NotificationModel
import com.xqy.androidx.framework.utils.appLog
import com.xqy.androidx.framework.utils.dp


class NotificationHelper private constructor(private val mApplication: Application) {

    private val mNotificationManager: NotificationManager by lazy {
        mApplication.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    }

    companion object {
        private const val CHANNEL_ID: String = "0x11"
        lateinit var mInstance: NotificationHelper
        fun init(mApplication: Application) {
            if (!::mInstance.isInitialized) {
                mInstance = NotificationHelper(mApplication)
            }
        }


    }

    private lateinit var mNotificationDataBinding: NotificationBinding
    private val mWindowManager: WindowManager by lazy {
        mApplication.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
    private val params: WindowManager.LayoutParams by lazy {

        WindowManager.LayoutParams().apply {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                TYPE_APPLICATION_OVERLAY
            } else {
                TYPE_SYSTEM_OVERLAY
            }
            flags = FLAG_NOT_TOUCHABLE or FLAG_LAYOUT_IN_SCREEN or FLAG_NOT_FOCUSABLE
            format = PixelFormat.TRANSLUCENT
            gravity = Gravity.TOP or Gravity.CENTER
            width = mApplication.resources.displayMetrics.widthPixels - 32.dp
            height = WRAP_CONTENT
            y = (-96).dp


        }
    }


    fun sendNotification(mNotificationModel: NotificationModel) {
        with(mWindowManager) {
            if (!::mNotificationDataBinding.isInitialized) {
                mNotificationDataBinding = DataBindingUtil.inflate(
                    mNotificationModel.activity.layoutInflater,
                    R.layout.notification, null, false
                )

            }
            mNotificationDataBinding.notification = mNotificationModel
            with(mNotificationDataBinding.root){
                addView(this, params)
                ObjectAnimator.ofFloat(this,"translationY",24.dp*1f).apply {
                    duration = 150
                    interpolator = DecelerateInterpolator()
                    start()
                }
            }
        }
        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, mNotificationModel.channelName, importance).apply {
                description = mNotificationModel.channelDescription
                enableLights(true)
                enableVibration(true)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                setShowBadge(false)
            }
            mNotificationManager.createNotificationChannel(channel)
            val notifyIntent = Intent(mApplication, mNotificationModel.activity::class.java)
            val notifyPendingIntent = PendingIntent.getActivity(
                mApplication, 0, notifyIntent, 0
            )
            Notification.Builder(mApplication, CHANNEL_ID)
                .setSmallIcon(mNotificationModel.notificationIcon)
                .setContentTitle(mNotificationModel.channelName)
                .setContentText(mNotificationModel.channelDescription)
                .setContentIntent(notifyPendingIntent)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .build()


        } else {
            NotificationCompat.Builder(mApplication, CHANNEL_ID)
                .setSmallIcon(mNotificationModel.notificationIcon)
                .setContentTitle(mNotificationModel.channelName)
                .setContentText(mNotificationModel.channelDescription)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(mNotificationModel.channelDescription)
                )
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build()
        }


        with(NotificationManagerCompat.from(mApplication)) {
            notify(mNotificationModel.notificationId, notification)
        }

    }
}