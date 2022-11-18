package com.reader.readerapp.screens.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reader.readerapp.data.Resource
import com.reader.readerapp.model.Item
import com.reader.readerapp.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(private val repository: BookRepository):ViewModel(){
    val list:MutableState<List<Item>> = mutableStateOf(listOf())

    var isLoading:Boolean by mutableStateOf(true)

    init {
        loadBooks()
    }

    fun loadBooks(){
        searchBooks("android")
    }

    fun searchBooks(query:String){

        viewModelScope.launch() {
            if(query.isEmpty()){
                return@launch
            }

          try{
              isLoading = true
             when(val response = repository.getBooks(query)) {
                 is Resource.Succes -> {
                     list.value = response.data!!
                     if(list.value. isNotEmpty()){
                         isLoading = false

                     }
                 }
                 is Resource.Error ->{
                     isLoading = false
                     Log.e("Network","Failed getting books")
                 }
                 else ->{
                    isLoading = false
                 }
             }
          }
          catch (e:Exception){
              Log.d("TAG","exception: ${e.message.toString()}")
          }
        }

    }

}