package com.xqy.androidx.ui.dialog

data class DialogConfig(val buttonTextSize:Float,
                        val contentTextSize:Float,
                        val cancelTextColor:Int,
                        val confirmTextColor:Int,
                        val contentTextColor:Int,
                        val onCancelClick:()->Unit,
                        val onConfirmClick:()->Boolean)