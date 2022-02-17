package com.veritas.TMapp.database

import androidx.room.*
import com.veritas.TMapp.server.Contacts

@Dao
interface ContactsDAO {
    @Query("SELECT * FROM TB_CONTACTS")
    fun getAll() : List<Contacts>

    @Query("SELECT * FROM TB_CONTACTS WHERE contactTargetUuid = :uuid")
    fun getContactByUUID(uuid: String): Contacts

    @Update
    fun update(contacts: Contacts)

    @Insert
    fun insertAll(vararg contacts: Contacts)

    @Insert
    fun insertContacts(contacts: Contacts)

    @Delete
    fun delete(contacts: Contacts)

    @Query("DELETE FROM TB_CONTACTS")
    fun deleteAll()
}