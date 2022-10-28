package  com.allballapp.android.core.util

import android.annotation.SuppressLint
import java.math.BigInteger
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

//val text = "https://2501.pecadev.net/api/auth/login;{\"username\":\"user1\",\"password\":\"passw0rd\",\"otp\":\"584192\",\"fingerprint\":\"2043631D-3B06-445D-8B9C-AF75650AE8E0\"}"

object CryptoUtil {

    private const val ALGORITHM = "AES/ECB/PKCS5Padding"

    //private val key = SecretKeySpec("17539217539217539217539217539217".toByteArray(), "AES")
    private var key: SecretKeySpec? = null

    @SuppressLint("GetInstance")
    fun encrypt(strToEncrypt: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val ciphertext: ByteArray = cipher.doFinal(strToEncrypt.toByteArray())
        return android.util.Base64.encodeToString(ciphertext, android.util.Base64.DEFAULT)
    }

    fun decrypt(dataToDecrypt: ByteArray): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val cipherText = cipher.doFinal(dataToDecrypt)
        return android.util.Base64.encodeToString(cipherText, android.util.Base64.DEFAULT)
    }

    fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    fun setSeed(otpSeed: String) {
        key = SecretKeySpec(otpSeed.toByteArray(), "AES")
    }

    fun generateGet(request: String): String {
        return encrypt(md5(request))
    }

    fun generatePost(request: String, body: String): String {
        return encrypt(md5("$request;$body;"))
    }

    fun isActive() = key != null
}
