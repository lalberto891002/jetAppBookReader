package com.reader.readerapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.reader.readerapp.screens.SplasScreen
import com.reader.readerapp.screens.details.BookDetailsScreen
import com.reader.readerapp.screens.details.BooksDetailViewModel
import com.reader.readerapp.screens.home.HomeScreen
import com.reader.readerapp.screens.login.LoginScreen
import com.reader.readerapp.screens.login.LoginScreenViewModel
import com.reader.readerapp.screens.search.BookSearchViewModel
import com.reader.readerapp.screens.search.SearchScreen
import com.reader.readerapp.screens.stats.StatsScreen


@Composable
fun ReaderNavigation()  {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name) {
        composable(ReaderScreens.SplashScreen.name){
            SplasScreen(navController = navController)
        }
        composable(ReaderScreens.ReaderHomeScreen.name){
            HomeScreen(navController = navController)
        }
        composable(ReaderScreens.LoginScreen.name){
           val viewModel = hiltViewModel<LoginScreenViewModel>()
            LoginScreen(navController = navController, viewModel = viewModel)
        }

        composable(ReaderScreens.ReaderStatsScreen.name){
            StatsScreen(navController = navController)
        }

        composable(ReaderScreens.SearchScreen.name){
            val viewModel = hiltViewModel<BookSearchViewModel>()
            SearchScreen(navController = navController, viewModel = viewModel)
        }

        val route = ReaderScreens.DetailsScreen.name
        composable("$route/{bookId}",
            arguments = listOf(
                navArgument(name = "bookId"){
                    type = NavType.StringType
                }
            )){ navBack->
            var bookId = ""
            navBack.arguments?.let {
                bookId =  navBack.arguments!!.getString("bookId").toString()
            }
            val viewModel = hiltViewModel<BooksDetailViewModel>()


            BookDetailsScreen(navController = navController,
                viewModel = viewModel,bookId = bookId)
        }




    }
}



