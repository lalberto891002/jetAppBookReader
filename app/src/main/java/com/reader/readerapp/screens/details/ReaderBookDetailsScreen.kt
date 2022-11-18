package com.reader.readerapp.screens.details

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.reader.readerapp.components.ReaderAppBar
import com.reader.readerapp.components.RoundedButton
import com.reader.readerapp.data.Resource
import com.reader.readerapp.model.Item
import com.reader.readerapp.model.MBook
import com.reader.readerapp.model.VolumeInfo
import com.reader.readerapp.navigation.ReaderScreens

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "ProduceStateDoesNotAssignValue")
@Composable
fun BookDetailsScreen(navController: NavController,
                      viewModel: BooksDetailViewModel, bookId: String) {

  val bookInfo = produceState<Resource<Item>>(initialValue = Resource.loading()){
      value = viewModel.getBookDetails(idBook = bookId)
  }
    Scaffold(
        topBar = {
            ReaderAppBar(title = "Detail Book",
                icon = Icons.Default.ArrowBack,
                navController = navController ,
                showProfile = false){navController.navigate(ReaderScreens.SearchScreen.name)}
        }) {
            if(bookInfo.value is Resource.loading)
                LinearProgressIndicator()
            else {
                if(bookInfo.value is Resource.Succes)
                    BookDetailedView(bookInfo.value
                        .data?.volumeInfo?:VolumeInfo(),navController,bookId)

            } }


}

@Composable
fun BookDetailedView(book: VolumeInfo = VolumeInfo(), navController: NavController, bookId: String){


    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics
    val cleanDescrition  = HtmlCompat.fromHtml(book.description?:"No Available Description",HtmlCompat.FROM_HTML_MODE_LEGACY)

    val screenHeight = displayMetrics.heightPixels / displayMetrics.density
    val containerHeight = screenHeight - screenHeight/1.5
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 5.dp, start = 5.dp, end = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Card(shape = CircleShape){
            Image(painter = rememberAsyncImagePainter(model = book.imageLinks?.thumbnail?:"")
                , contentDescription = "bookImage",
                modifier = Modifier
                    .size(90.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(text = book.title.toString(), style = MaterialTheme.typography.h5,
            overflow = TextOverflow.Ellipsis)
        Text(text ="Authors:${book.authors}", overflow = TextOverflow.Clip)
        Text(text ="Page Count :${book.pageCount}", overflow = TextOverflow.Clip)

        Text(text = "Categories: ${book.categories}", overflow = TextOverflow.Clip)
        Text(text = "Published: ${book.publishedDate}", overflow = TextOverflow.Clip)

        Spacer(modifier = Modifier
            .height(5.dp)
          )
        Card(border = BorderStroke(width = 1.dp, color = Color.Gray),
        modifier = Modifier
            .fillMaxWidth()
            .height(containerHeight.dp)
            .verticalScroll(rememberScrollState())
            .weight(weight = 1f, fill = false)
            .padding(start = 8.dp)) {
            Text(text = cleanDescrition.toString(),
                modifier = Modifier.padding(4.dp), overflow = TextOverflow.Clip)
        }
        Row(modifier = Modifier
            .padding(9.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            RoundedButton(label = "Save"){
                val bookSavable = MBook(
                    title = book.title.toString(),
                    authors = book.authors.toString(),
                    description = book.description.toString(),
                    categories = book.categories.toString(),
                    notes = "",
                    photoUrl = book.imageLinks?.thumbnail.toString(),
                    publishedData = book.publishedDate,
                    pageCount = book.pageCount.toString(),
                    rating = 0.0,
                    googleBookID = bookId,
                    userID = FirebaseAuth.getInstance().currentUser?.uid.toString()
                )
                saveToFireBase(bookSavable,navController,context)

            }
            RoundedButton(label = "Cancel"){
                navController.popBackStack()
            }

        }

    }

}


fun saveToFireBase(book: MBook, navController: NavController, context: Context) {
    val db = FirebaseFirestore.getInstance()
    val dbCOllection = db.collection("books")
    if(book.toString().isNotEmpty()){
        dbCOllection.add(book)
            .addOnSuccessListener {
                documentRef->
            val docId = documentRef.id
            dbCOllection.document(docId)
                .update(hashMapOf("id" to docId) as Map<String, Any>)
                .addOnCompleteListener{
                    task->
                    if(task.isSuccessful){
                        Toast.makeText(context,"Book saved succesfully",
                            Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                }.addOnFailureListener{
                    error->
                    Log.w("TAG","SAve to firebase error updating do: $error")
                }
            }
    }
    else{

    }

}


