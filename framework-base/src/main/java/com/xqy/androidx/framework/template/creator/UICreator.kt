package com.xqy.androidx.framework.template.creator

import android.view.View
import android.view.ViewGroup
import com.xqy.androidx.framework.template.creator.model.ConstrainSetModel
import com.xqy.androidx.framework.template.creator.model.UIModel

internal abstract class UICreator<T:View>{

   lateinit var mNextUICreator: UICreator<*>
   protected abstract fun createWidget(parentView:ViewGroup, model: UIModel, constrainSetModel: ConstrainSetModel?=null)

   fun assembleWidget(parentView: ViewGroup, model: UIModel, constrainSetModel: ConstrainSetModel?=null){
      createWidget(parentView,model,constrainSetModel)
      if (::mNextUICreator.isInitialized){
         mNextUICreator.assembleWidget(parentView,model)
      }
   }

}