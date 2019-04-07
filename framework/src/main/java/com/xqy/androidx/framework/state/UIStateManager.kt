package com.xqy.androidx.framework.state

import android.os.Looper
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.ConcurrentHashMap

object UIStateManager {

    private val mUIStates:ConcurrentHashMap<String,MutableLiveData<UIState>> by lazy {
        ConcurrentHashMap<String,MutableLiveData<UIState>>()
    }
    internal fun saveUIState(mUIStateKey: String,uiState: MutableLiveData<UIState>){
        mUIStates[mUIStateKey] = uiState
    }
    fun changeUIState(mUIStateKey:String,state: UIState){
        mUIStates[mUIStateKey]?.let {
            if (Thread.currentThread() == Looper.getMainLooper().thread){
                it.value = state
            }else{
                it.postValue( state)
            }


        }
    }
}