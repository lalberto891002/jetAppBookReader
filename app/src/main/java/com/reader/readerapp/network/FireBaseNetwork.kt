package com.reader.readerapp.network

import com.reader.readerapp.model.MBook

interface FireBaseNetwork {

    suspend fun saveBookToNetWork(book: MBook, succesfully:()->Unit)

    suspend fun createUserWithEmailAndPassword(email: String, pass: String,home: () -> Unit)

    suspend  fun SigInWithEmailAndPassword(email:String,pass:String,home:()->Unit)

    fun logOut(executeAfterLogOut:()->Unit)
}