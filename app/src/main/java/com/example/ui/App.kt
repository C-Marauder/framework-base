package com.example.ui

import android.app.Application
import com.xqy.androidx.framework.notification.NotificationHelper
import com.xqy.androidx.framework.security.SecurityHelper
import com.xqy.androidx.framework.template.UITemplate

class App:Application() {

    override fun onCreate() {
        super.onCreate()
//
//        UIConfig.Builder {
//            titleColor {  }//toolbar标题的颜色
//            titleSize {  }//toolbar标题的字体大小
//            navIcon {  }//导航icon
//            clearElevation {  }//是否需要阴影
//        }
        NotificationHelper.init(this)

        SecurityHelper.init(this)

    }
}