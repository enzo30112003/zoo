package fr.isen.morelli.zoo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.isen.morelli.zoo.ui.theme.ZooTheme
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fr.isen.morelli.zoo.screens.HomeScreen
import fr.isen.morelli.zoo.screens.AdminScreen
import fr.isen.morelli.zoo.screens.EnclosuresScreen
import fr.isen.morelli.zoo.screens.FaqScreen
import fr.isen.morelli.zoo.screens.LoginScreen
import fr.isen.morelli.zoo.screens.ModifyEnclosuresScreen
import fr.isen.morelli.zoo.screens.ProfilScreen
import fr.isen.morelli.zoo.screens.RateEnclosuresScreen
import fr.isen.morelli.zoo.screens.ServicesScreen
import fr.isen.morelli.zoo.screens.SignUpScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZooTheme {
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
                            ModifyEnclosuresScreen(navController = navController)
                        }

                    }
                    //TextButton(onClick = { navController.navigate("signup") }) {
                       // Text("Pas encore de compte ? S'inscrire")
                    //}
                        // Vous pouvez ajouter plus d'écrans comme "home" ici
                    }
                }
            }
        }
    }

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ZooTheme {
        Greeting("Android")
    }
}

