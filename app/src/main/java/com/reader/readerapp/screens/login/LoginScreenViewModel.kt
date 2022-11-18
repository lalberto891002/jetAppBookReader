package com.reader.readerapp.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.reader.readerapp.model.MUser
import com.reader.readerapp.network.FireBaseNetwork
import com.reader.readerapp.repository.FireBaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor( private val repository: FireBaseRepository
) :ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading:LiveData<Boolean> = _loading

    fun createUserWithEmailAndPassword(
        email: String,
        pass: String,
        home: () -> Unit){
        if(_loading.value == false){
            _loading.value = true
            viewModelScope.launch {
                repository.createUserWithEmailAndPassword(email,pass,home)
                _loading.value = false
            }
        }


    }

    fun SigInWithEmailAndPassword(email:String,pass:String,home:()->Unit) = viewModelScope.launch{
        try{
            repository.SigInWithEmailAndPassword(email,pass,home)
        }catch (ex: RuntimeExecutionException){
            Log.d("FB","SIngin email and pass:${ex.message}")
        }
    }

    fun LogOut(onExecuteAfterLogut:()->Unit) = repository.logOut {
        onExecuteAfterLogut()
    }

}