package com.reader.readerapp.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.reader.readerapp.data.DataOrException
import com.reader.readerapp.model.MBook
import com.reader.readerapp.repository.FireBaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenviewModel @Inject constructor(val repository: FireBaseRepository):ViewModel() {

    val data:MutableState<DataOrException<List<MBook>,Boolean,Exception>> = mutableStateOf(
        DataOrException(
            listOf(),true,Exception("")
        )
    )

    var currentUser = repository.auth.currentUser

    init {
        getBooks()
    }
    fun getBooks(){
            repository.auth.currentUser?.email
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getAllBooksFromDataBase()
            if(!data.value.data.isNullOrEmpty()) data.value.loading = false
        }
    Log.d("TAG","getBooksFromDB:${data.value.data}")
    }

}