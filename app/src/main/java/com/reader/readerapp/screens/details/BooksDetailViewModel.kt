package com.reader.readerapp.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reader.readerapp.data.Resource
import com.reader.readerapp.model.Item
import com.reader.readerapp.model.MBook
import com.reader.readerapp.network.FireBaseNetwork
import com.reader.readerapp.repository.BookRepository
import com.reader.readerapp.repository.FireBaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksDetailViewModel @Inject constructor(private val repository: BookRepository,
                                               private val fireBaseRepository:FireBaseRepository):ViewModel() {


    suspend fun getBookDetails(idBook:String):Resource<Item>  =
        repository.getBookInfo(idBook)

     fun saveBookToNetWork(book:MBook,succesfully:()->Unit){
         viewModelScope.launch(Dispatchers.IO) {
             fireBaseRepository.saveBookToNetWork(book){
                 succesfully.invoke() }
             }
     }

}