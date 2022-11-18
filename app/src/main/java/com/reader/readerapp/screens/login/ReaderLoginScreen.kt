package com.reader.readerapp.screens.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.common.base.Functions.compose
import com.google.common.base.Suppliers.compose
import com.reader.readerapp.R
import com.reader.readerapp.components.EmailInput
import com.reader.readerapp.components.PassWordInput
import com.reader.readerapp.components.ReaderLogo
import com.reader.readerapp.components.SubmitButton
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reader.readerapp.navigation.ReaderScreens

@Composable
fun LoginScreen(navController: NavController,
    viewModel: LoginScreenViewModel) {

    val showLogginForm  = rememberSaveable{ mutableStateOf(true) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {

            ReaderLogo()
            if(showLogginForm.value) {
                UserForm(loading = false, isCreateAccount = false){email,pass->

                    viewModel.SigInWithEmailAndPassword(email,pass){
                        navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                    }
                }
            }
            else {
                UserForm(loading = false, isCreateAccount = true){email,pass->
                   viewModel.createUserWithEmailAndPassword(email,pass){
                       navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                   }
                }
            }

        }
        Spacer(modifier = Modifier.height(if(showLogginForm.value) 15.dp else 30.dp))
        Row(modifier = Modifier.padding(top = if(showLogginForm.value) 15.dp else 75.dp ,
            start = 15.dp,
            end = 15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){
            val text = if(showLogginForm.value) "Sing Up" else "Login"
            Text(text = "New User?")
            Text(text = text,
                Modifier
                    .clickable {
                        showLogginForm.value = !showLogginForm.value
                    }
                    .padding(start = 5.dp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.secondaryVariant)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun UserForm(
    loading:Boolean = false,
    isCreateAccount:Boolean = false,
    onDone:(String,String)-> Unit = {email,pw ->
        Log.d("onDone","Email: $email and PW: $pw")
    }

){
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")

    }
    val passVisibility = rememberSaveable{
        mutableStateOf(false)
    }
    val passwordFocusRequest = FocusRequester.Default

    val keyBoardController = LocalSoftwareKeyboardController.current

    val valid = remember(email.value,password.value) {
        email.value.trim().isNotEmpty() && password.value.isNotEmpty()
    }

    val modifier = Modifier
        .height(250.dp)
        .background(MaterialTheme.colors.background)
        .verticalScroll(rememberScrollState())
    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally) {
        if(isCreateAccount) Text(text = stringResource(id = R.string.create_acc),
        modifier = Modifier.padding(start = 6.dp, end = 6.dp)) else Text(text = "")
        EmailInput(emailState = email, enabled = !loading,
            onAction = KeyboardActions {
            passwordFocusRequest.requestFocus()

        })

        PassWordInput(
            modifier = Modifier.focusRequester(passwordFocusRequest),
            passwordState = password,
            labelId = "Password",
            enabled =!loading,
            passVisibility = passVisibility,
            onAction = KeyboardActions{
                if(!valid) return@KeyboardActions
                onDone(email.value.trim(),password.value.trim())
                keyBoardController?.hide()
            },

        )

        SubmitButton(
            textId = if(isCreateAccount) "Create Account" else "Login",
            loading = loading,
            validInputs = valid,

        ){
            onDone(email.value.trim(),password.value.trim())
            keyBoardController?.hide()
        }

    }


}


