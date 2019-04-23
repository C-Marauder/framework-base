package com.xqy.androidx.framework.security

import android.app.Application
import android.util.Base64
import android.util.Log
import com.xqy.androidx.framework.utils.appLog
import com.xqy.androidx.framework.utils.readBytesFromFile
import com.xqy.androidx.framework.utils.readFromFile
import com.xqy.androidx.framework.utils.saveToFile
import java.io.FileNotFoundException
import java.security.*
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.net.ssl.SSLContext

class SecurityHelper private constructor() {
    private lateinit var secretKey: SecretKeySpec
    private lateinit var mPublicKey: PublicKey
    private lateinit var mPrivateKey: PrivateKey
    companion object {
        private const val TAG: String = "SecurityHelper"
        private const val RSA_TAG: String = "RSA"
        private const val AES_TAG: String = "AES"
        private lateinit var mApplication: Application
        private const val RAS_PUB_KEY: String = "pubKey"
        private const val RAS_PRIVATE_KEY: String = "privateKey"
        private const val RAS_HTTP_PUB_KEY:String = "rasHttpPubKey"
        fun init(application: Application) {
            mApplication = application
        }
        private lateinit var mHTTPSTrustManager:HTTPSTrustManager
        fun createSSL(application: Application,crtName:String){
            mApplication = application
            val certificateFactory = CertificateFactory.getInstance("X.509")
            val caInput = mApplication.assets.open(crtName).buffered()
            val ca:Certificate
            try {
                ca = certificateFactory.generateCertificate(caInput)
                if (!::mHTTPSTrustManager.isInitialized ){
                    mHTTPSTrustManager = HTTPSTrustManager(ca)
                    SSLContext.getInstance("TLSv1","AndroidOpenSSL")
                        .init(null, arrayOf(mHTTPSTrustManager),null)
                }
            }catch (e:Exception){
                caInput.close()
            }
        }
        val mInstance: SecurityHelper by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            SecurityHelper()
        }
    }

    fun encryptByAES(psd: String, content: String, e: String? = null): String {
        if (!::secretKey.isInitialized) {
            val keyGenerator = KeyGenerator.getInstance(AES_TAG)
            keyGenerator.init(128, SecureRandom(psd.toByteArray()))
            val enCodeFormat = keyGenerator.generateKey().encoded
            secretKey = SecretKeySpec(enCodeFormat, AES_TAG)
        }

        //创建密码器
        val cipher = if (e.isNullOrEmpty()) {
            Cipher.getInstance(AES_TAG).apply {
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
                Cipher.getInstance(AES_TAG).apply {
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
        } catch (e: Exception) {
            appLog("decode fail-->:${e.message}")
        }

        return null

    }

    fun savePubKey(publicKey: ByteArray){
        mApplication.saveToFile(RAS_HTTP_PUB_KEY,publicKey)
    }
    fun saveBase64PubKey(publicKey: String){
        val decodePubKey = Base64.decode(publicKey,Base64.DEFAULT)
        mApplication.saveToFile(RAS_HTTP_PUB_KEY,decodePubKey)
    }
    fun encryptByRsa(content: String): String? {
        try {
            val publicKey = mApplication.readBytesFromFile(RAS_HTTP_PUB_KEY)
            val pubKey = generateRSAPubKey(publicKey)
            return Base64.encodeToString(Cipher.getInstance("RSA/ECB/PKCS1Padding")
                .apply { init(Cipher.ENCRYPT_MODE, pubKey) }
                .doFinal(content.toByteArray()), Base64.DEFAULT)
        }catch (e:FileNotFoundException){
            Log.e(TAG,"there is no publicKey,please save publicKey before encode content")
        }

        return null
    }

    fun generateRSAKeyPair(): KeyPair {
        val mKeyPairGenerator = KeyPairGenerator.getInstance(RSA_TAG)
        mKeyPairGenerator.initialize(1024)
        return mKeyPairGenerator.genKeyPair()
    }

    fun encryptToLocalByRsa(content: String,filename:String):String {
        try {
            if (!::mPublicKey.isInitialized){
                val pubKeyBytes: ByteArray = mApplication.readBytesFromFile(RAS_PUB_KEY)
                val keySpec = X509EncodedKeySpec(pubKeyBytes)
                val kf = KeyFactory.getInstance(RSA_TAG)
                mPublicKey = kf.generatePublic(keySpec)
            }

        } catch (e: FileNotFoundException) {
            val keyPair = generateRSAKeyPair()
            mPublicKey = keyPair.public
            mPrivateKey = keyPair.private
            mApplication.saveToFile(RAS_PRIVATE_KEY, mPrivateKey.encoded)
            mApplication.saveToFile(RAS_PUB_KEY, mPublicKey.encoded)
        }

        val encodeContent = Base64.encodeToString(Cipher.getInstance("RSA/ECB/PKCS1Padding")
            .apply { init(Cipher.ENCRYPT_MODE, mPublicKey) }
            .doFinal(content.toByteArray()), Base64.DEFAULT)
         mApplication.saveToFile(filename,encodeContent)

        return encodeContent
    }


    fun decryptFromLocalByRsa(filename: String): String? {

        try {
            val encodeContent = mApplication.readFromFile(filename)
            val decode = Base64.decode(encodeContent, Base64.DEFAULT)
            if (!::mPrivateKey.isInitialized) {
                val privateKeyBytes = mApplication.readBytesFromFile(RAS_PRIVATE_KEY)
                val keySpec = PKCS8EncodedKeySpec(privateKeyBytes)
                val kf = KeyFactory.getInstance(RSA_TAG)
                mPrivateKey = kf.generatePrivate(keySpec)
            }
            return String(Cipher.getInstance("RSA/ECB/PKCS1Padding")
                .apply { init(Cipher.DECRYPT_MODE, mPrivateKey) }
                .doFinal(decode))

        } catch (e: FileNotFoundException) {
            Log.e(TAG, "can't find RAS privateKey")
        }
        return null
    }
//
//    fun encryptByRsa(content: String, publicKey: PublicKey): String {
//
//        return Base64.encodeToString(Cipher.getInstance("RSA/ECB/PKCS1Padding")
//            .apply { init(Cipher.ENCRYPT_MODE, publicKey) }
//            .doFinal(content.toByteArray()), Base64.DEFAULT)
//    }
//
//    fun decryptByRsa(content: String, publicKey: PrivateKey): String {
//        val decode = Base64.decode(content, Base64.DEFAULT)
//
//        return String(Cipher.getInstance("RSA/ECB/PKCS1Padding")
//            .apply { init(Cipher.DECRYPT_MODE, publicKey) }
//            .doFinal(decode))
//    }

//    fun encryptByRsa(content: String, publicKey: ByteArray): String {
//
//        val mPublicKey = generateRSAPubKey(publicKey)
//        return Base64.encodeToString(Cipher.getInstance("RSA/ECB/PKCS1Padding")
//            .apply { init(Cipher.ENCRYPT_MODE, mPublicKey) }
//            .doFinal(content.toByteArray()), Base64.DEFAULT)
//    }
//
//    fun decryptByRsa(content: String, privateKey: ByteArray): String {
//
//        val mPrivateKey = generateRSAPrivateKey(privateKey)
//        return Base64.encodeToString(Cipher.getInstance("RSA/ECB/PKCS1Padding")
//            .apply { init(Cipher.DECRYPT_MODE, mPublicKey) }
//            .doFinal(content.toByteArray()), Base64.DEFAULT)
//    }

    private fun generateRSAPubKey(publicKey: ByteArray): PublicKey {
        val keySpec = X509EncodedKeySpec(publicKey)
        val kf = KeyFactory.getInstance(RSA_TAG)
        return kf.generatePublic(keySpec)
    }
    private fun generateRSAPrivateKey(privateKey: ByteArray): PrivateKey {
        val keySpec = PKCS8EncodedKeySpec(privateKey)
        val kf = KeyFactory.getInstance(RSA_TAG)
        return kf.generatePrivate(keySpec)
    }


}