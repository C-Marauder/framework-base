package com.xqy.androidx.ui

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class SecurityHelper private constructor() {
    private lateinit var secretKey: SecretKeySpec


    companion object {
        val mInstance: SecurityHelper by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {

            SecurityHelper()
        }
    }

    fun encryptByAES(psd: String, content: String, e: String? = null): String {
        if (!::secretKey.isInitialized) {
            val keyGenerator = KeyGenerator.getInstance("AES")
            keyGenerator.init(128, SecureRandom(psd.toByteArray()))
            val enCodeFormat = keyGenerator.generateKey().encoded
            secretKey = SecretKeySpec(enCodeFormat, "AES")
        }

        //创建密码器
        val cipher = if (e.isNullOrEmpty()) {
            Cipher.getInstance("AES").apply {
                this.init(Cipher.ENCRYPT_MODE, secretKey)//初始化
            }

        } else {
            Cipher.getInstance("AES/CBC/PKCS5Padding").apply {
                val iv = IvParameterSpec(e.toByteArray())
                this.init(Cipher.ENCRYPT_MODE, secretKey, iv)//初始化
            }
        }
        val byteContent = content.toByteArray()
        val result = cipher.doFinal(byteContent)//加密
        return Base64.encodeToString(result, Base64.DEFAULT)
    }

    fun decryptByAES(encodeContent: String, e: String? = null): String? {
        try {
            //创建密码器
            val cipher = if (e.isNullOrEmpty()) {
                Cipher.getInstance("AES").apply {
                    this.init(Cipher.DECRYPT_MODE, secretKey)//初始化
                }

            } else {
                Cipher.getInstance("AES/CBC/PKCS5Padding").apply {
                    val iv = IvParameterSpec(e.toByteArray())
                    this.init(Cipher.DECRYPT_MODE, secretKey, iv)//初始化
                }
            }
            val decodeContent = Base64.decode(encodeContent, Base64.DEFAULT)
            val result = cipher.doFinal(decodeContent)//加密
            return String(result)
        }catch (e:Exception){
            appLog("解密失败-->:${e.message}")
        }

        return null

    }


}