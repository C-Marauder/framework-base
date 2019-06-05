package com.example.ui

import android.app.Application
import com.xqy.androidx.framework.notification.NotificationHelper
import com.xqy.androidx.framework.security.SecurityHelper
import com.xqy.androidx.framework.template.UITemplate

class App:Application() {

    override fun onCreate() {
        super.onCreate()

        UITemplate.Builder {

            clearElevation {  true}//是否需要阴影
        }
        NotificationHelper.init(this)

        SecurityHelper.init(this)

    }
}