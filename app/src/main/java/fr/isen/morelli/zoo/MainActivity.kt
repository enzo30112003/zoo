package fr.isen.morelli.zoo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZooTheme {
                // Créez une instance de FirebaseRepository
                val repository = FirebaseRepository()

                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
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
                            EnclosuresScreen(navController = navController)
                        }
                        composable("rateanimals") {
                            RateEnclosuresScreen(navController = navController)
                        }
                        composable("modifyenclosures") {
                            // Passez repository à ModifyEnclosuresScreen
                            ModifyEnclosuresScreen(navController = navController, repository = repository)
                        }
                    }
                }
            }
        }
    }
}