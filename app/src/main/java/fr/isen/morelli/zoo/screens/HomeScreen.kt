package fr.isen.morelli.zoo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import fr.isen.morelli.zoo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // Utilisation de Box pour centrer le titre
                    Box(
                        modifier = Modifier.fillMaxWidth(), // Prend toute la largeur disponible
                        contentAlignment = Alignment.Center // Centre le contenu à l'intérieur du Box
                    ) {
                        Text("Accueil : plan du parc")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top, // Le contenu va commencer en haut
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Affichage du message de bienvenue
                Text(
                    text = "Bienvenue dans notre zoo, ${user?.email ?: "Utilisateur"}",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(50.dp)) // Espacement entre le message et l'image

                // Affichage de l'image du zoo
                Image(
                    painter = painterResource(id = R.drawable.plan), // Remplacez par le nom de votre image du zoo
                    contentDescription = "Image du Zoo",
                    modifier = Modifier
                        .fillMaxWidth() // Remplir toute la largeur
                        .height(250.dp) // Ajustez la hauteur de l'image du zoo
                        .align(Alignment.CenterHorizontally) // Centrer l'image
                )

                Spacer(modifier = Modifier.height(50.dp)) // Espacement avant le bouton de déconnexion

                // Bouton de déconnexion
                Button(
                    onClick = {
                        auth.signOut()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                ) {
                    Text("Se déconnecter")
                }
            }
        }
    )
}
