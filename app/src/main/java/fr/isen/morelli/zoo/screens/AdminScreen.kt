package fr.isen.morelli.zoo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import fr.isen.morelli.zoo.R
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Espace Admin", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Texte de bienvenue
                Text(
                    text = "Bienvenue ${user?.email ?: "Utilisateur"}",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(40.dp)) // Espacement

                // Affichage de l'image du zoo
                Image(
                    painter = painterResource(id = R.drawable.plan),
                    contentDescription = "Image du Zoo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(30.dp)) // Espacement avant les boutons

                // Bouton pour gérer les enclos (avec navigation)
                Button(
                    onClick = { navController.navigate("modifyenclosures") }, // Redirige vers ModifyEnclosuresScreen
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Gérer les enclos")
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Bouton pour modifier les horaires
                Button(
                    onClick = {  navController.navigate("modifyenclosures") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Modifier les horaires")
                }

                Spacer(modifier = Modifier.weight(1f)) // Pousse le bouton de déconnexion vers le bas

                // Bouton de déconnexion
                Button(
                    onClick = {
                        auth.signOut()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Se déconnecter")
                }
            }
        }
    )
}
