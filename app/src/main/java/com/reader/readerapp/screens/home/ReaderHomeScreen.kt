package com.reader.readerapp.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.reader.readerapp.components.FabContent
import com.reader.readerapp.components.ListCard
import com.reader.readerapp.components.ReaderAppBar
import com.reader.readerapp.components.TitleSection
import com.reader.readerapp.model.MBook
import com.reader.readerapp.navigation.ReaderScreens
import java.io.Reader


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController = NavController(LocalContext.current),
               viewModel: HomeScreenviewModel) {
    Scaffold(topBar = {
                      ReaderAppBar(title = "A.Reader", navController = navController )
    },
    floatingActionButton = {
        FabContent {
            navController.navigate(ReaderScreens.SearchScreen.name)
        }
    }) {

        Surface(modifier = Modifier.fillMaxSize()) {
            HomeContent(navController = navController,viewModel)

        }
    }
}
@Preview
@Composable
fun HomeContent(navController: NavController = NavController(LocalContext.current),viewModel: HomeScreenviewModel = hiltViewModel()) {

//    val books = listOf(
//        MBook(id = "dadfa", title = "Hello Again", authors = "All of us", notes = null),
//        MBook(id = "dadfa", title = " Again", authors = "All of us", notes = null),
//        MBook(id = "dadfa", title = "Hello ", authors = "The world us", notes = null),
//        MBook(id = "dadfa", title = "Hello Again", authors = "All of us", notes = null),
//        MBook(id = "dadfa", title = "Hello Again", authors = "All of us", notes = null)
//    )

    var books = emptyList<MBook>()
    val contentUser = viewModel.currentUser?.email?.split("@")?.get(0)
    val currentUser = viewModel.currentUser
    if(!viewModel.data.value.data.isNullOrEmpty()){
        books = viewModel.data.value?.data?.toList()!!.filter {
            mBook ->
            mBook.userID == currentUser?.uid
        }

        Log.d("Books","List of Books:$books")
    }

    Column(modifier = Modifier.padding(2.dp),
    verticalArrangement = Arrangement.Top) {
        Row(modifier = Modifier.align(alignment = Alignment.Start)) {
            TitleSection(label = "You are reading \n" + "activity right now")
            Spacer(modifier = Modifier.fillMaxWidth(0.7f))
            Column {
                Icon(imageVector = Icons.Filled.AccountCircle ,
                    contentDescription = "Profile",
                modifier = Modifier
                    .clickable {
                        navController.navigate(ReaderScreens.ReaderStatsScreen.name)
                    }
                    .size(45.dp),

                tint = MaterialTheme.colors.secondaryVariant)
                Text(text = contentUser!!,
                modifier = Modifier.padding(2.dp),
                style = MaterialTheme.typography.overline,
                fontSize = 13.sp,
                color = Color.Red,
                maxLines = 1,
                overflow = TextOverflow.Clip
                )
                Divider()
            }
        }
        ReadingRightNowArea(books = books, navController = navController)
        TitleSection(label = "Reading List")

        BookListArea(listOfBooks = books, navController = navController)
    }
}

@Composable
fun BookListArea(listOfBooks: List<MBook>, navController: NavController) {
    HorizontalScrollableComponent(listOfBooks){book->
        navController.navigate(ReaderScreens.UpdateScreen.name + "/${book}")

    }
}

@Composable
fun HorizontalScrollableComponent(listOfBooks: List<MBook>,onCardPress:(String)->Unit) {
    val scrollState = rememberScrollState()

    Row(modifier = Modifier
        .fillMaxWidth()
        .heightIn(280.dp)
        .horizontalScroll(scrollState)) {
        for(book in listOfBooks){
            ListCard(book = book){
                onCardPress(it)
            }
        }

    }
}


@Composable
fun ReadingRightNowArea(books:List<MBook>, navController: NavController){
    Row(){
        HorizontalScrollableComponent(books){
            Log.d("TAG","Pressed:$it")
        }
    }

}




