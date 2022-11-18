package com.reader.readerapp.repository

import com.reader.readerapp.data.DataOrException
import com.reader.readerapp.data.Resource
import com.reader.readerapp.model.Book
import com.reader.readerapp.model.Item
import com.reader.readerapp.network.BooksApi
import javax.inject.Inject

class BookRepository @Inject constructor(private val api:BooksApi){


    suspend fun getBooks(searchQuery:String):Resource<List<Item>> {
      return  try {
            Resource.loading(data = "Loading...")
            val itemList = api.getAllBooks(searchQuery).items
            Resource.Succes(data = itemList)
        }catch (e:Exception){
            Resource.Error(message = e.message.toString())
        }

    }


    suspend fun getBookInfo(bookId:String):Resource<Item>{
        return try{
            Resource.loading(data = "loading...")
            val item  = api.getBookInfo(bookId)
            Resource.Succes(item)
        }catch (e:Exception){
           Resource.Error(message = e.message.toString())
        }

    }

}