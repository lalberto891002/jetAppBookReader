package com.reader.readerapp.repository

import android.util.Log
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.reader.readerapp.data.DataOrException
import com.reader.readerapp.model.Book
import com.reader.readerapp.model.MBook
import com.reader.readerapp.model.MUser
import com.reader.readerapp.network.FireBaseNetwork
import com.reader.readerapp.network.FireBaseNetworkImpl
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireBaseRepository @Inject constructor(val api:FireBaseNetwork) {

    val queryBook:Query = api.dbCollection
    val auth  = api.auth
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


    suspend fun getAllBooksFromDataBase(): DataOrException<List<MBook>, Boolean, Exception> {
        val dataOrException = DataOrException<List<MBook>,Boolean,Exception>()

        try{
            dataOrException.loading = true
            dataOrException.data = queryBook.get().await().documents.map {
                documentSnapshot ->
                documentSnapshot.toObject(MBook::class.java)!!
            }

            if(!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false
        }
        catch (e:Exception){
            dataOrException.e = e
            Log.d("TAG",e.message.toString())
        }

        return dataOrException
    }

}

