package com.reader.readerapp.screens.update

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.reader.readerapp.components.InputField
import com.reader.readerapp.components.RatingBar
import com.reader.readerapp.components.ReaderAppBar
import com.reader.readerapp.components.RoundedButton
import com.reader.readerapp.data.DataOrException
import com.reader.readerapp.model.Item
import com.reader.readerapp.model.MBook
import com.reader.readerapp.navigation.ReaderScreens
import com.reader.readerapp.screens.home.HomeScreenviewModel
import com.reader.readerapp.utils.formatDate
import org.checkerframework.checker.units.qual.Length

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
                    Surface(modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(),
                        shape = CircleShape,
                        elevation = 4.dp) {
                        if(!viewModel.data.value.data?.isEmpty()!!){

                            ShowBookUpdate(bookInf = viewModel.data.value.data!!,book = bookID)
                        }
                    }

                        ShowSimpleForm(book = viewModel.data.value.data?.first{mBook ->
                            mBook.googleBookID == bookID

                        },navController,viewModel)
                    }
            }

        }
    }
}

@Composable
fun ShowSimpleForm(book: MBook?, navController: NavController,viewModel: HomeScreenviewModel) {

    val noteText = rememberSaveable(book?.notes) {
        mutableStateOf(book?.notes?:"")

    }
    SimpleForm(defaultValue = if(book?.notes?.isNotEmpty() == true)book?.notes.toString()
    else "No Thougts available :( ."){ note->
        noteText.value = note

    }
    val isStarted = remember{
        mutableStateOf(false)
    }

    val isFinished = remember{
        mutableStateOf(false)
    }

    val rating = remember {
        mutableStateOf(book?.rating?.toInt())
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally){
        Row(modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start){
            TextButton(onClick = { isStarted.value = true }, enabled = book?.startedReading == null) {

                if(book?.startedReading == null){

                    if(!isStarted.value){
                        Text(text = "Start Reading")
                    }else{
                        Text(text = "Started Reading!", modifier = Modifier.alpha(0.6f),
                            color = Color.Red.copy(alpha = 0.5f))
                    }
                }else{
                    Text(text = "Started Reading on: ${formatDate(book.startedReading!!)}")
                }

            }

            Spacer(modifier = Modifier.height(4.dp))
            TextButton(onClick = { isFinished.value = true }, enabled = book?.finishedReading == null) {
                if(book?.finishedReading == null){
                    if(!isFinished.value){
                        Text(text = "Mark as Read")
                    }else{
                        Text(text = "Finished Reading!",modifier = Modifier.alpha(0.6f),
                            color = Color.Red.copy(alpha = 0.5f))
                    }
                }else{
                    Text(text = "Finished Reading on: ${formatDate(book.finishedReading!!)}")
                }

            }

        }
        Text(text = "Rating")
        book?.rating?.toInt().let {
            RatingBar(rating = it!!){
                rating.value = it
                Log.d("Rating","Rating now is:${rating.value}")
            }

    }

        Spacer(modifier = Modifier.padding(15.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()){

            val context = LocalContext.current
            val changedNotes = book?.notes != noteText.value
            val changeRate = book?.rating?.toInt() != rating.value
            val isFinishedTimeStamp = if(isFinished.value) Timestamp.now() else
                book?.finishedReading
            val isStartedTimeStamp = if(isStarted.value && book?.startedReading == null)
                Timestamp.now() else book?.startedReading
            val bookUpdate = changedNotes || changeRate || isStarted.value || isFinished.value

            val bookToUpdate = hashMapOf(
                "finished_reading_at" to isFinishedTimeStamp,
                "started_reading_at" to isStartedTimeStamp,
                "rating" to rating.value,
                "notes" to noteText.value
            ).toMap()
            RoundedButton(label = "Update"){
                if(bookUpdate)
                 viewModel.updateBook(book!!,bookToUpdate, onSuccess = {
                     showToast(context,"Succesfully Updated Book")
                     navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                 },
                 onFailure = {
                     showToast(context,"Error Saving Book")
                 })
            }
            val openDialog = remember {
                mutableStateOf(false)
            }

            if(openDialog.value){
                showAlertDialog(message = "Are you Sure to delete",openDialog = openDialog){
                    viewModel.deleBook(book!!){
                        openDialog.value = false
                        navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                    }

                }
            }
            RoundedButton(label = "Delete"){
               if(openDialog.value == false)
                   openDialog.value = true
                }

            }
        }



}

@Composable
fun showAlertDialog(message: String,openDialog:MutableState<Boolean>,onYesPressed:()->Unit) {
    if(openDialog.value){
        AlertDialog(onDismissRequest = {},
            title = {Text("Delete Book?")},
        text = { Text(text = message)},
        buttons = {
            Row(modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
                ) {
                TextButton(onClick = {onYesPressed.invoke()}) {
                    Text("Yes")
                }
                TextButton(onClick = {
                    openDialog.value = false
                }) {
                  Text(text = "No")
                }
            }
        })
    }

}

fun showToast(context: Context, message: String) {
    Toast.makeText(context,message, Toast.LENGTH_LONG).show()

}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SimpleForm(modifier: Modifier = Modifier,
               loading: Boolean = false,
               defaultValue:String = "Great Book",
            onSearch:(String)->Unit){
        Column() {
            val textFieldValue = rememberSaveable(){
                mutableStateOf(defaultValue)

            }

            val keyBoardController = LocalSoftwareKeyboardController.current
            val valid = remember(textFieldValue.value){
                textFieldValue.value.trim().isNotEmpty()
            }
            
            InputField(
                modifier = modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .padding(3.dp)
                    .background(
                        Color.White,
                        CircleShape
                    )
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                valueState = textFieldValue ,
                labelID = "Enter Your Thoughts",
                enabled = true ,
                onAction = KeyboardActions{
                    if(!valid)return@KeyboardActions
                    keyBoardController?.hide()
                    onSearch(textFieldValue.value)
                }
            )

        }

}

@Composable
fun ShowBookUpdate(bookInf: List<MBook>, book: String) {
    Row() {
        Spacer(modifier = Modifier.width(43.dp))
        if(bookInf.isNotEmpty()){
            Column(modifier = Modifier.padding(4.dp),
            verticalArrangement = Arrangement.Center) {
                CardListItem(book = bookInf.first{mBook->
                    mBook.googleBookID == book

                },onPressDetails = {})
            }
        }

    }
}

@Composable
fun CardListItem(book: MBook, onPressDetails: () -> Unit) {
    Card(modifier = Modifier
        .padding(
            start = 4.dp,
            end = 4.dp,
            top = 4.dp,
            bottom = 8.dp
        )
        .clip(RoundedCornerShape(20.dp))
        .clickable {

        }, elevation = 8.dp) {
        Row{
            Image(painter = rememberAsyncImagePainter(model = book.photoUrl),
                contentDescription = "book photo",
                modifier = Modifier
                    .height(100.dp)
                    .width(120.dp)
                    .padding(4.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 120.dp,
                            topEnd = 20.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp
                        )
                    ))

            Column(modifier = Modifier.padding(start = 3.dp)) {
                Text(text = book.title.toString(), style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(start = 8.dp,end = 8.dp),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis)

                Text(text = book.authors.toString(), style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(start = 8.dp,end = 8.dp),
                    fontWeight = FontWeight.Light,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis)

                Text(text = book.publishedData.toString(), style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(start = 8.dp,end = 8.dp,top = 0.dp, bottom = 8.dp),
                    fontWeight = FontWeight.Light,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis)

            }

        }

    }
}




