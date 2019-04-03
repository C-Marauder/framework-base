package com.androidx.ui.network

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.os.Build

object NetWorkManager{

    private lateinit var mConnectivityManager: ConnectivityManager
    fun isConnected():Boolean{
        val mActiveNetworkInfo= mConnectivityManager.activeNetworkInfo
        return mActiveNetworkInfo!=null && mActiveNetworkInfo.isConnected
    }
    fun run(application: Application,observer:(state: NetWorkState, mNetWorkInfo: NetworkInfo?)->Unit){
        mConnectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mConnectivityManager.registerNetworkCallback(NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        addTransportType(NetworkCapabilities.TRANSPORT_WIFI_AWARE)
                    }
                }
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                .build(), object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network?) {
                    super.onAvailable(network)
                    observer(NetWorkState.CONNECTED, mConnectivityManager.activeNetworkInfo)
                }

                override fun onLost(network: Network?) {
                    super.onLost(network)
                    observer(NetWorkState.LOST,null)
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    observer(NetWorkState.UNAVAILABLE, mConnectivityManager.activeNetworkInfo)
                }
            })
        } else {
            application.registerReceiver(
                object : BroadcastReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) {
                        intent?.let {
                            it.action?.let { action ->
                                if (action == ConnectivityManager.CONNECTIVITY_ACTION) {

                                    observer(NetWorkState.CONNECTED, mConnectivityManager.activeNetworkInfo)

                                }
                            }
                        }
                    }

                },
                IntentFilter().apply {
                    this.addAction("android.net.conn.CONNECTIVITY_CHANGE")
                    this.addAction("android.net.wifi.WIFI_STATE_CHANGED")
                    this.addAction("android.net.wifi.STATE_CHANGE")
                }
            )
        }
    }

}