package com.example.gestor_historial_medico

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class EncryptedStorageImpl(private val secretKey: SecretKey) : SecureStorage {

    private val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    private val ivSpec = IvParameterSpec(ByteArray(16)) //Vector de inicialización de 16 bytes

    override fun saveData(key: String, data: String) {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
        val encryptedData = cipher.doFinal(data.toByteArray())
        val encodedData = Base64.encodeToString(encryptedData, Base64.DEFAULT)
    }

    override fun getData(key: String): String {
        //Recuperar encodedData
        val encodedData = ""
        val decodedData = Base64.decode(encodedData, Base64.DEFAULT)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
        val decryptedData = cipher.doFinal(decodedData)
        return String(decryptedData)
    }

    override fun deleteData(key: String) {

    }
}
