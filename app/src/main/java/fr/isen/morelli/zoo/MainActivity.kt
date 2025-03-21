package fr.isen.morelli.zoo

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.isen.morelli.zoo.repository.FirebaseRepository
import fr.isen.morelli.zoo.screens.*
import fr.isen.morelli.zoo.ui.theme.ZooTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZooTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val repository = FirebaseRepository() // Instance de FirebaseRepository

                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(navController = navController)
                        }
                        composable("signup") {
                            SignUpScreen(navController = navController)
                        }
                        composable("home") {
                            HomeScreen(navController = navController)
                        }
                        composable("admin_home") {
                            AdminScreen(navController = navController)
                        }
                        composable("services") {
                            ServicesScreen(navController = navController)
                        }
                        composable("profil") {
                            ProfilScreen(navController = navController)
                        }
                        composable("faq") {
                            FaqScreen(navController = navController)
                        }
                        composable("enclosures") {
                            EnclosuresScreen(navController = navController, repository = repository)
                        }
                        composable("rateanimals") {
                            RateEnclosuresScreen(navController = navController)
                        }
                        composable("modifyenclosures") {
                            ModifyEnclosuresScreen(navController = navController, repository = repository)
                        }
                        composable("admin_feeding") {
                            AdminFeedingScheduleScreen(navController = navController, repository = repository)
                        }
                        composable("user_feeding") {
                            UserFeedingScheduleScreen(navController = navController, repository = repository)
                        }
                        composable("gps") { GPSScreen(navController = navController, repository = repository) }
                        composable("navigation/{name}/{lat}/{lon}") { backStackEntry ->
                            val name = backStackEntry.arguments?.getString("name") ?: ""
                            val lat = backStackEntry.arguments?.getString("lat") ?: "0.0"
                            val lon = backStackEntry.arguments?.getString("lon") ?: "0.0"
                            NavigationScreen(navController = navController, destinationName = name, destLat = lat, destLon = lon)
                        }
                        composable("enclosure_ratings") {
                            EnclosureRatingsScreen(navController, repository) // Passer l'instance de FirebaseRepository
                        }
                    }
                }
            }
        }
    }
}
