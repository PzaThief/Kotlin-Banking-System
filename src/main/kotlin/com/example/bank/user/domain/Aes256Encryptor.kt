package com.example.bank.user.domain

import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Aes256Encryptor {

    // Note: 운영를 위해서는 properties에서 동적으로 가져오게 해야하나, 편의을 위해 임시 처리
    private const val KEY = "tbtSjPFa8FUVHSruchxwRhqP9E8aIB3Q"
    private val secretKeySpec = SecretKeySpec(KEY.toByteArray(), "AES")
    private const val IV = "s9OyvGWv35rF2GXY"
    private val ivParameterSpec = IvParameterSpec(IV.toByteArray())
    private const val CIPHER_SPEC = "AES/CBC/PKCS5PADDING"

    fun encrypt(payload: String): String {
        val cipher = Cipher.getInstance(CIPHER_SPEC)
            .apply { init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec) }
        val encrypted = cipher.doFinal(payload.toByteArray())

        return Base64.getEncoder().encode(encrypted).toString()
    }

    fun decrypt(payload: String): String {
        val cipher = Cipher.getInstance(CIPHER_SPEC)
            .apply { init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec) }
        val bytesOfPayload = Base64.getDecoder().decode(payload)

        return cipher.doFinal(bytesOfPayload).toString()
    }
}