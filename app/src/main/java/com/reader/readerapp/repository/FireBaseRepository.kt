package com.reader.readerapp.repository

import android.util.Log
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.reader.readerapp.model.MBook
import com.reader.readerapp.model.MUser
import com.reader.readerapp.network.FireBaseNetwork
import javax.inject.Inject

class FireBaseRepository @Inject constructor(private val api:FireBaseNetwork) {

     suspend fun saveBookToNetWork(book: MBook, succesfully: () -> Unit) {
      api.saveBookToNetWork(book,succesfully)
    }

     suspend fun createUserWithEmailAndPassword(
        email: String,
        pass: String,
        home: () -> Unit
    ) {
        api.createUserWithEmailAndPassword(email,pass,home)
    }


     suspend fun SigInWithEmailAndPassword(email: String, pass: String, home: () -> Unit) {
        api.SigInWithEmailAndPassword(email,pass,home)
    }

     fun logOut(executeAfterLogOut: () -> Unit) {
       api.logOut(executeAfterLogOut)
    }


}

