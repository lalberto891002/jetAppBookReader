package com.reader.readerapp.screens.update

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.reader.readerapp.components.ReaderAppBar
import com.reader.readerapp.data.DataOrException
import com.reader.readerapp.model.MBook
import com.reader.readerapp.screens.home.HomeScreenviewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UpdateScreen(bookID: String, navController: NavHostController,
viewModel: HomeScreenviewModel = hiltViewModel()) {
    val bookInfo = produceState<DataOrException<List<MBook>,Boolean,Exception>>(initialValue = DataOrException(data = listOf(),true,Exception(""))
    ){
        value = viewModel.data.value
    }.value
    Scaffold(topBar = {
        ReaderAppBar(title = "Update Book",
            navController = navController,
            icon = Icons.Default.ArrowBack,
            showProfile = false){
            navController.popBackStack()
        }
    }) {
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)) {
            Column(modifier = Modifier.padding(top = 3.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {

                Log.d("INFO","BookUpdateScreen:${viewModel.data.value.data.toString()}")

                if(bookInfo.loading == true){
                    LinearProgressIndicator()
                    bookInfo.loading = false
                }
                else{
                    if(!viewModel.data.value.data?.isEmpty()!!){
                        Text(text=viewModel.data.value.data?.get(0)?.title.toString())
                    }

                }

            }

        }
    }



}