package fr.isen.morelli.zoo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import fr.isen.morelli.zoo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mon Profil", textAlign = TextAlign.Center) })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profilpicture),
                    contentDescription = "Photo de profil",
                    modifier = Modifier
                        .size(300.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Bouton FAQ
                Button(onClick = { navController.navigate("faq") }) {
                    Text("FAQ")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bouton pour voir les avis et commentaires
                Button(onClick = {
                    // Naviguer vers l'écran des avis et commentaires
                    navController.navigate("enclosure_ratings")
                }) {
                    Text("Voir les Avis et Commentaires")
                }

                Spacer(modifier = Modifier.weight(1f))

                // Bouton Se Déconnecter
                Button(
                    onClick = {
                        auth.signOut()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Se déconnecter", color = Color.White)
                }
            }
        }
    )
}
