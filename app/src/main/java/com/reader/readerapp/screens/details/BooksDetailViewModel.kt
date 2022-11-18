package com.reader.readerapp.screens.details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reader.readerapp.data.Resource
import com.reader.readerapp.model.Item
import com.reader.readerapp.model.VolumeInfo
import com.reader.readerapp.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksDetailViewModel @Inject constructor(private val repository: BookRepository):ViewModel() {


    suspend fun getBookDetails(idBook:String):Resource<Item>  =
        repository.getBookInfo(idBook)
}