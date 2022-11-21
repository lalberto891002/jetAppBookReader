package com.reader.readerapp.network

import android.util.Log
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.reader.readerapp.model.MBook
import com.reader.readerapp.model.MUser
import javax.inject.Inject

class FireBaseNetworkImpl @Inject constructor(private val firebaseStore: FirebaseFirestore)
    :FireBaseNetwork {
    override val auth: FirebaseAuth = Firebase.auth
    override val dbCollection = firebaseStore.collection("books")


    override suspend fun saveBookToNetWork(book: MBook, succesfully: () -> Unit) {
        if (book.toString().isNotEmpty()) {
            dbCollection.add(book)
                .addOnSuccessListener { documentRef ->
                    val docId = documentRef.id
                    dbCollection.document(docId)
                        .update(hashMapOf("id" to docId) as Map<String, Any>)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                succesfully.invoke()
                            }
                        }.addOnFailureListener { error ->
                            Log.w("TAG", "SAve to firebase error updating do: $error")
                        }
                }
        }
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        pass: String,
        home: () -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email,pass)
            .addOnCompleteListener{task->
                if(task.isSuccessful){
                    val displayName = task.result.user?.email?.split("@")?.get(0)
                    val userID = auth.currentUser?.uid
                    val user = MUser(userId = userID.toString(), displayName = displayName.toString(),
                        avatarUrl = "", quote = "life is awsome", profession = "Android developer").toMap()
                    firebaseStore.collection("users").add(user)
                    home()
                }else{
                    try{
                        Log.d("FB","CreateUserWithEmailPAss: ${task?.result.toString()}")
                    }catch (ex1: RuntimeExecutionException){
                        Log.d("FB","SIngin email and pass:${ex1.message}")
                    }

                }

            }
    }


    override suspend fun SigInWithEmailAndPassword(email: String, pass: String, home: () -> Unit) {
        auth.signInWithEmailAndPassword(email,pass)
            .addOnCompleteListener {task ->
                if(task.isSuccessful){
                    Log.d("FB","SIngin email and pass: yayayay${task.result}")
                    home()
                }else{
                    try{
                        Log.d("FB","SIngin email and pass:${task.result}")
                    }catch (ex1: RuntimeExecutionException){
                        Log.d("FB","SIngin email and pass:${ex1.message}")
                    }

                }
            }
    }

    override fun logOut(executeAfterLogOut: () -> Unit) {
        FirebaseAuth.getInstance().signOut().run {
            executeAfterLogOut()
        }
    }




}
