package com.reader.readerapp.screens.search

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.reader.readerapp.components.BooKVerticallScrollable
import com.reader.readerapp.components.InputField
import com.reader.readerapp.components.ReaderAppBar
import com.reader.readerapp.navigation.ReaderScreens

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchScreen(navController:NavController,viewModel: BookSearchViewModel = hiltViewModel()){
    Scaffold(
        topBar = {
          ReaderAppBar(title = "Search Books",
              icon = Icons.Default.ArrowBack,
              navController = navController ,
              showProfile = false){navController.navigate(ReaderScreens.ReaderHomeScreen.name)}
        },
        
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            SearchForm(
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ){query->
                viewModel.searchBooks(query)


            }
            BooKVerticallScrollable(navController = navController,viewModel)

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(
    viewModel: BookSearchViewModel,
    modifier:Modifier = Modifier,
    loading:Boolean = false,
    hint:String = "Search",
    onSearch:(String) ->Unit = {})
{
    Column() {
        val searchQueryState = rememberSaveable {
            mutableStateOf("")

        }
        val valid = searchQueryState.value.trim().isNotEmpty()
        val keyBoardController = LocalSoftwareKeyboardController.current
        InputField(modifier = modifier, valueState = searchQueryState,
            labelID = "Search" , enabled = true, onAction = KeyboardActions{
                if(!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyBoardController?.hide()

            },
        )

    }


}

