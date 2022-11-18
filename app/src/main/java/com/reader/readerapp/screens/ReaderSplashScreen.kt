package com.reader.readerapp.screens

import android.view.animation.OvershootInterpolator
import androidx.annotation.RequiresPermission
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.reader.readerapp.navigation.ReaderScreens
import kotlinx.coroutines.delay
import com.reader.readerapp.components.*

@Preview
@Composable
fun SplasScreen(navController: NavController = NavController(context = LocalContext.current)){

    val scale = remember {
        Animatable(initialValue = 0f)
    }
    LaunchedEffect(key1 = true){
        scale.animateTo(targetValue = 0.9f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(8f)
                        .getInterpolation(it)
                }))

        delay(2000L)
      //  navController.navigate(ReaderScreens.LoginScreen.name)
        if(FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()){
            navController.navigate(ReaderScreens.LoginScreen.name)
        }
        else{
            navController.navigate(ReaderScreens.ReaderHomeScreen.name)
        }

    }
    
    
    Surface(modifier = Modifier
        .padding(15.dp)
        .size(330.dp)
        .scale(scale.value),
    shape = CircleShape,
    color = Color.White,
    border = BorderStroke(color = Color.LightGray, width = 2.dp)
    ) {
        Column(modifier = Modifier.padding(1.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

            ReaderLogo()

            Spacer(modifier = Modifier.height(15.dp))

            Text(text = "\"Read. Change. Yourself\"", style = MaterialTheme.typography.h5,
                color = Color.LightGray)
        }

    }
}

