package com.reader.readerapp.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.reader.readerapp.model.MUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginScreenViewModel:ViewModel() {
    val loadingState = MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading:LiveData<Boolean> = _loading

    fun createUserWithEmailAndPassword(
        email: String,
        pass: String,
        home: () -> Unit){
        if(_loading.value == false){
            _loading.value = true
            viewModelScope.launch {
                auth.createUserWithEmailAndPassword(email,pass)
                    .addOnCompleteListener{task->
                        if(task.isSuccessful){
                            val displayName = task.result.user?.email?.split("@")?.get(0)
                            createUser(displayName)
                            home()
                        }else{
                            Log.d("FB","CreateUserWithEmailPAss: ${task?.result.toString()}")
                        }

                    }

            }
        }


    }

    private fun createUser(displayName: String?) {
        val userID = auth.currentUser?.uid
        val user = MUser(userId = userID.toString(), displayName = displayName.toString(),
            avatarUrl = "", quote = "life is awsome", profession = "Android developer").toMap()
        FirebaseFirestore.getInstance().collection("users").add(user)
    }


    fun SigInWithEmailAndPassword(email:String,pass:String,home:()->Unit) = viewModelScope.launch{
        try{
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
        }catch (ex: RuntimeExecutionException){
            Log.d("FB","SIngin email and pass:${ex.message}")
        }
    }

}