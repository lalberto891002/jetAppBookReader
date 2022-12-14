package com.reader.readerapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.reader.readerapp.screens.SplasScreen
import com.reader.readerapp.screens.details.BookDetailsScreen
import com.reader.readerapp.screens.details.BooksDetailViewModel
import com.reader.readerapp.screens.home.HomeScreen
import com.reader.readerapp.screens.home.HomeScreenviewModel
import com.reader.readerapp.screens.login.LoginScreen
import com.reader.readerapp.screens.login.LoginScreenViewModel
import com.reader.readerapp.screens.search.BookSearchViewModel
import com.reader.readerapp.screens.search.SearchScreen
import com.reader.readerapp.screens.stats.StatsScreen
import com.reader.readerapp.screens.update.UpdateScreen


@Composable
fun ReaderNavigation()  {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name) {
        composable(ReaderScreens.SplashScreen.name){
            SplasScreen(navController = navController)
        }
        composable(ReaderScreens.ReaderHomeScreen.name){
            val viewModel = hiltViewModel<HomeScreenviewModel>()
            HomeScreen(navController = navController,viewModel = viewModel)
        }
        composable(ReaderScreens.LoginScreen.name){
           val viewModel = hiltViewModel<LoginScreenViewModel>()
            LoginScreen(navController = navController, viewModel = viewModel)
        }

        composable(ReaderScreens.ReaderStatsScreen.name){
            val viewModel = hiltViewModel<HomeScreenviewModel>()
            StatsScreen(navController = navController,viewModel = viewModel)
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

        val routeUpdate = ReaderScreens.UpdateScreen.name
        composable("$routeUpdate/{bookItemId}",
        arguments = listOf(
            navArgument(name = "bookItemId"){
                type = NavType.StringType
            })
        ){ navBack->
            var bookID = ""
            navBack.arguments?.let {
                bookID = navBack.arguments!!.getString("bookItemId").toString()
            }
            val viewModel = hiltViewModel<HomeScreenviewModel>()
            UpdateScreen(bookID = bookID, navController,viewModel = viewModel)
        }


    }
}



