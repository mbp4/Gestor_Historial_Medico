package com.example.gestor_historial_medico

object SecureStorageManager : SecureStorage {
    private val storage: SecureStorage = EncryptedStorageImpl(KeyGeneratorUtil.getKeyFromKeystore())

    override fun saveData(key: String, data: String) {
        storage.saveData(key, data)
    }

    override fun getData(key: String): String? {
        return storage.getData(key)
    }

    override fun deleteData(key: String) {
        storage.deleteData(key)
    }
}
