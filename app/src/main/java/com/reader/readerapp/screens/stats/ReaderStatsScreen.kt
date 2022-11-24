package com.reader.readerapp.screens.stats

import android.annotation.SuppressLint
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.sharp.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.reader.readerapp.components.BookCard
import com.reader.readerapp.components.ReaderAppBar
import com.reader.readerapp.model.Item
import com.reader.readerapp.model.MBook
import com.reader.readerapp.screens.home.HomeScreenviewModel
import com.reader.readerapp.utils.formatDate

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun StatsScreen(navController: NavController, viewModel: HomeScreenviewModel) {
   val books:List<MBook>
   val currentUser = viewModel.currentUser
   
   Scaffold(topBar = {
      ReaderAppBar(title = "Books Stats",
         icon = Icons.Default.ArrowBack,
         showProfile  = false,
         navController = navController){
          navController.popBackStack()
      }
   }) {

      Surface {
        val books = if(!viewModel.data.value.data.isNullOrEmpty()){
            viewModel.data.value.data!!.filter {mBook ->  
            mBook.userID == currentUser?.uid
               
            }
         }else{
            emptyList()
         }
          Column {
              Row {
                  Box(modifier = Modifier
                      .size(45.dp)
                      .padding(2.dp)){
                      Icon(imageVector = Icons.Sharp.Person, contentDescription = "Icon" )
                  }
                  Text(text = "Hi,${currentUser?.email.toString().split("@")[0].uppercase()}")
              }



              Card(modifier = Modifier
                  .fillMaxWidth()
                  .padding(4.dp),
                  shape = CircleShape,
                  elevation = 5.dp) {
                  val readBookList:List<MBook> = if(!viewModel.data.value.data.isNullOrEmpty()) {
                      books.filter { mBook ->
                          mBook.userID == currentUser?.uid && mBook.finishedReading != null
                      }
                  }else{
                      emptyList()
                  }

                  val readingBookList:List<MBook> = if(!viewModel.data.value.data.isNullOrEmpty()) {
                      books.filter { mBook ->
                          mBook.userID == currentUser?.uid && mBook.finishedReading == null
                      }
                  }else{
                      emptyList()
                  }
                  Column(modifier = Modifier.padding(start = 25.dp, top = 4.dp, bottom = 4.dp),
                      horizontalAlignment = Alignment.Start) {
                      Text(text = "Your Stats", style = MaterialTheme.typography.h5)
                      Divider()
                      Text(text = "You are Readings ${readingBookList.size} books")
                      Text(text = "You have Read ${readBookList.size} books")
                  }



              }

              if(viewModel.data.value.loading == true){
                  LinearProgressIndicator()
              }else{
                  Divider()
                  LazyColumn(modifier = Modifier
                      .fillMaxWidth()
                      .fillMaxHeight(),
                  contentPadding = PaddingValues(16.dp)){

                      val readBookList:List<MBook> = if(!viewModel.data.value.data.isNullOrEmpty()) {
                          books.filter { mBook ->
                              mBook.userID == currentUser?.uid && mBook.finishedReading != null
                          }
                      }else{
                          emptyList()
                      }
                      items(items = readBookList){book->
                          BookCardSAtat(book = book)
                      }

                  }


              }

          }


      }
   }

}

@Composable
fun BookCardSAtat(book: MBook) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(120.dp)
        .padding(4.dp),
        shape = RectangleShape,
        elevation = 4.dp) {
        Row(verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start){
            val imageUrl:String = if(book.photoUrl?.isNullOrEmpty()!!)
                "https://i.ytimg.com/vi/tuGOId6rNCo/default.jpg"
            else book.photoUrl?:""
            Image(painter =
            rememberAsyncImagePainter(model = imageUrl),
                contentDescription = "image of book",
                modifier = Modifier
                    .width(width = 80.dp)
                    .fillMaxHeight())
            Spacer(modifier = Modifier.height(10.dp))
            Column(modifier = Modifier.padding(start = 10.dp)) {

                Row(horizontalArrangement = Arrangement.SpaceBetween){

                    Text(text = book.title.toString(),
                        overflow = TextOverflow.Ellipsis)
                    if(book.rating!! >= 4){
                        Spacer(modifier = Modifier.fillMaxWidth(0.8f))
                        Icon(imageVector = Icons.Default.ThumbUp, contentDescription = "thumbsup",
                            tint = Color.Green.copy(alpha = 0.5f))
                    }else{
                        Box{}
                    }

                }


                Text(text = "Author: ${book.authors}", fontFamily = FontFamily.SansSerif,
                    fontStyle = FontStyle.Italic, overflow = TextOverflow.Clip)
                Text(text = "Started: ${formatDate(book.startedReading!!)}",softWrap = true,
                    fontFamily = FontFamily.SansSerif, fontStyle = FontStyle.Italic)
                Text(text = "Finished:${formatDate(book.finishedReading!!)}", softWrap = true,
                    fontFamily = FontFamily.SansSerif,
                    fontStyle = FontStyle.Italic,overflow = TextOverflow.Clip)
            }


        }

    }

}
