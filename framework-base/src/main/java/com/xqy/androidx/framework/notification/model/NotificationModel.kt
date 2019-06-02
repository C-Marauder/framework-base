package com.xqy.androidx.framework.notification.model

import androidx.appcompat.app.AppCompatActivity
import com.xqy.androidx.framework.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private val mSimpleDateFormat:DateFormat by lazy {
    SimpleDateFormat("HH:mm", Locale.CHINA)
}
data class NotificationModel(val notificationId: Int,
                             val channelName:String,
                             val channelDescription:String,
                             val notificationIcon:Int,
                             val activity:AppCompatActivity,
                             val notificationTime:String= activity.getString(R.string.app_name)+"\t"+mSimpleDateFormat.format(Date(System.currentTimeMillis())))