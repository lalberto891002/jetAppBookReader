package com.reader.readerapp.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.reader.readerapp.model.MBook

interface FireBaseNetwork {

    suspend fun saveBookToNetWork(book: MBook, succesfully:()->Unit)

    suspend fun createUserWithEmailAndPassword(email: String, pass: String,home: () -> Unit)

    suspend  fun SigInWithEmailAndPassword(email:String,pass:String,home:()->Unit)

    fun logOut(executeAfterLogOut:()->Unit)

    suspend fun updateBookd(
        book: MBook,
        bookToUpdate: Map<String, Comparable<*>?>,
        onSucces: () -> Unit,
        onFailure: () -> Unit
    )

    suspend fun deleteBook(book:MBook,onComplete:()->Unit)

    val dbCollection: Query
    val auth: FirebaseAuth
}