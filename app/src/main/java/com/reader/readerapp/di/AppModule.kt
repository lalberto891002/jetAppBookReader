package com.reader.readerapp.di

import com.google.firebase.firestore.FirebaseFirestore
import com.reader.readerapp.network.BooksApi
import com.reader.readerapp.repository.BookRepository
import com.reader.readerapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideBookRepository(api:BooksApi) = BookRepository(api)

    @Singleton
    @Provides
    fun provideBooksApi():BooksApi =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(BooksApi::class.java)


    @Singleton
    @Provides
    fun provideFireStore() = FirebaseFirestore.getInstance()



}