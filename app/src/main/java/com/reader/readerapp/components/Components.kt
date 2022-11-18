package com.reader.readerapp.components

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.reader.readerapp.model.Item
import com.reader.readerapp.model.MBook
import com.reader.readerapp.navigation.ReaderScreens
import com.reader.readerapp.screens.login.LoginScreenViewModel
import com.reader.readerapp.screens.search.BookSearchViewModel

@Composable
fun ReaderLogo(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.padding(bottom = 16.dp),
        text = "A. Reader", style = MaterialTheme.typography.h3,
        color = Color.Red.copy(alpha = 0.5f)
    )
}


@Composable
fun EmailInput(modifier: Modifier = Modifier,
               emailState:MutableState<String>,
               labelId:String = "Email",
               enabled:Boolean = true,
               imeAction:ImeAction = ImeAction.Next,
               onAction: KeyboardActions = KeyboardActions.Default
               ){
    InputField(modifier = modifier,
        valueState = emailState,
        labelID = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction
    )

}

@Composable
fun InputField(
    modifier: Modifier,
    valueState:MutableState<String>,
    labelID:String,
    enabled: Boolean,
    isSingleLine:Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(value = valueState.value, onValueChange = { valueState.value = it},
    label = { Text(text = labelID)},
    singleLine = isSingleLine,
    textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction,
        ),
        keyboardActions = onAction
    )
}


@Composable
fun PassWordInput(modifier: Modifier,
                  passwordState: MutableState<String>,
                  labelId: String,
                  enabled: Boolean,
                  passVisibility: MutableState<Boolean>,
                  imeAction:ImeAction = ImeAction.Done,
                  onAction: KeyboardActions = KeyboardActions.Default,
) {

    val visualTransformation = if(passVisibility.value) VisualTransformation.None else
        PasswordVisualTransformation()
    OutlinedTextField(value = passwordState.value, onValueChange = {
        passwordState.value = it
    },
        label = { Text(text = labelId)},
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction,
        ),

        keyboardActions = onAction,
        visualTransformation = visualTransformation,
        trailingIcon = {PassWordVisibility(passVisibility)}
    )

}

@Composable
fun PassWordVisibility(passVisibility: MutableState<Boolean>) {
    val visible = passVisibility.value
    IconButton(onClick = {
        passVisibility.value = !visible
    }) {
        Icons.Default.Close
    }

}

@Composable
fun SubmitButton(textId: String,
                 loading: Boolean,
                 validInputs: Boolean,
                 onClick:()-> Unit) {
    Button(onClick =  onClick,
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        enabled = !loading && validInputs,
        shape = CircleShape
    ) {
        if(loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(text = textId, modifier = Modifier.padding(5.dp))
    }


}

@Composable
fun TitleSection(modifer:Modifier = Modifier,label:String){
    Surface(modifier = modifer.padding(start = 5.dp, top = 1.dp)) {
        Column {
            Text(text = label,
                fontSize = 19.sp,
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Left)
        }

    }
}

@Composable
fun ReaderAppBar(
    title:String,
    icon:ImageVector? = null,
    showProfile:Boolean = true,
    navController: NavController,
    viewModel: LoginScreenViewModel = hiltViewModel(),
    onBAckArrowCLicked:()->Unit = {}
){
    val scope = rememberCoroutineScope()
    TopAppBar(title = {
        Row(verticalAlignment = Alignment.Top){
            if(showProfile){
                /* Image(painter = painterResource(id = R.drawable.ic_launcher_img),
                     contentDescription = "icon")*/

                Icon(imageVector =  Icons.Default.Favorite,
                    contentDescription = "Logo ICon",
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .scale(0.9f))
            }

            if(icon != null){
                Icon(imageVector = icon, contentDescription = "Arrow back",
                tint = Color.Red.copy(alpha = 0.7f),
                modifier = Modifier.clickable{
                    onBAckArrowCLicked.invoke()
                })
                Spacer(modifier = Modifier.width(40.dp) )
            }

            Text(text = title,
                color = Color.Red.copy(0.7f),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.width(150.dp) )

        }
    }, actions = {
        IconButton(onClick = {
            viewModel.LogOut(){
                navController.navigate(ReaderScreens.LoginScreen.name)
            }

        }) {
            if(showProfile) Row() {
                Icon(imageVector = Icons.Filled.Logout, contentDescription = "Logout",
                    tint = Color.Green.copy(alpha = 0.4f))
            }
            else Box(){}

        }

    }, backgroundColor = Color.Transparent, elevation = 0.dp)

}
@Composable
fun FabContent(onTap: () -> Unit) {
    FloatingActionButton(onClick = {
        onTap()
    }, shape = RoundedCornerShape(50.dp), backgroundColor = Color(0XFF92CBDF)) {
        Icon(imageVector = Icons.Default.Add,
            contentDescription = "Add a book",
            tint = MaterialTheme.colors.onSecondary )

    }
}

@Composable
fun ListCard(book:MBook = MBook("asdf","Running","Me and You","Hello world"),
             onPressDetails:(String)->Unit = {
             }){
    val context = LocalContext.current
    val resources = context.resources
    val displayMetrics = resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    val spacing = 10.dp
    Card(shape = RoundedCornerShape(29.dp),
        backgroundColor = Color.White,
        elevation = 6.dp,
        modifier = Modifier
            .padding(16.dp)
            .height(242.dp)
            .width(202.dp)
            .clickable {
                onPressDetails.invoke(book.title.toString())
            }){
        Column(modifier = Modifier.width(screenWidth.dp - (spacing  * 2)),
            horizontalAlignment = Alignment.Start) {
            Row(horizontalArrangement = Arrangement.Center){
                Image(painter = rememberAsyncImagePainter(model = "https://i.ytimg.com/vi/tuGOId6rNCo/default.jpg"),
                    contentDescription = "Image Painter",
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .padding(4.dp))
                Spacer(modifier = Modifier.width(50.dp))
                Column(modifier = Modifier.padding(top = 25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = "Fav Icon",
                        modifier = Modifier.padding(1.dp))

                    BookReating(score = 3.5)

                }
            }

            Text(text = book.title.toString(),
                modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis)

            Text(text = book.authors.toString(),
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.caption)


        }
        Row(horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom) {
            RoundedButton(label = "Reading",radius = 70)
        }
    }




}

@Composable
fun BookReating(score: Double = 4.5) {
    Surface(modifier = Modifier
        .height(70.dp)
        .padding(4.dp),
        shape = RoundedCornerShape(56.dp),
        elevation = 6.dp,
        color = Color.White) {
        Column(modifier = Modifier.padding(4.dp)) {
            Icon(imageVector = Icons.Filled.StarBorder,
                contentDescription = "Start",
                modifier = Modifier.padding(3.dp))
            Text(text = score.toString(), style = MaterialTheme.typography.subtitle1)

        }

    }

}

@Composable
fun RoundedButton(
    label:String = "Reading",
    radius:Int = 29,
    onPress:() ->Unit = {}
){
    Surface(modifier = Modifier.clip(
        RoundedCornerShape(
            bottomEndPercent = radius,
            topStartPercent = radius)),
        color = Color(0xFF92CBDF)) {

        Column(modifier = Modifier
            .width(90.dp)
            .heightIn(40.dp)
            .clickable {
                onPress.invoke()
            },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, style = TextStyle(
                color = Color.White,
                fontSize = 15.sp
            ))

        }
    }

}

@Composable
fun BookCard(navController: NavController,
             book:Item ,
             bookClicked:(Item)->Unit = {}) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(120.dp)
        .padding(4.dp)
        .clickable {
            bookClicked(book)

        },
        shape = RectangleShape,
        elevation = 4.dp) {
        Row(verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start){
            val imageUrl:String = if(book.volumeInfo.imageLinks?.smallThumbnail.isNullOrEmpty())
                "https://i.ytimg.com/vi/tuGOId6rNCo/default.jpg"
            else book.volumeInfo.imageLinks?.smallThumbnail?:""
            Image(painter =
            rememberAsyncImagePainter(model = imageUrl),
                contentDescription = "image of book",
                modifier = Modifier
                    .width(width = 80.dp)
                    .fillMaxHeight())
            Spacer(modifier = Modifier.height(10.dp))
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(text = book.volumeInfo.title.toString(),
                    style = MaterialTheme.typography.subtitle1,
                    overflow = TextOverflow.Ellipsis)
                Text(text = "Author: ${book.volumeInfo.authors}", fontFamily = FontFamily.SansSerif,
                    fontStyle = FontStyle.Italic, overflow = TextOverflow.Clip)
                Text(text = "Date: ${book.volumeInfo.publishedDate.toString()}",
                    fontFamily = FontFamily.SansSerif, fontStyle = FontStyle.Italic)
                Text(text = "[${book.volumeInfo.categories}]",fontFamily = FontFamily.SansSerif,
                    fontStyle = FontStyle.Italic,overflow = TextOverflow.Clip)
            }

        }

    }

}



@Composable
fun BooKVerticallScrollable(navController: NavController,viewModel: BookSearchViewModel) {

    Log.d("TAG","BookList: ${viewModel.list.value}")
    if(viewModel.isLoading){
        Log.d("TAG","BookList Loading...")
        LinearProgressIndicator()
    }else{
        Log.d("TAG","BookList: ${viewModel.list}")
        LazyColumn(modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)) {
            items(viewModel.list.value){ item->
                BookCard(navController = navController, book = item){
                navController.navigate(ReaderScreens.DetailsScreen.name + "/${item.id}")
                }

            }
    }


    }

}

