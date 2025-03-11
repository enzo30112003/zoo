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
            TopAppBar(title = { Text("Mon Profil",textAlign = TextAlign.Center) })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween, // 🔹 Place les éléments correctement
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 🔹 Cercle avec l'image de profil
                Image(
                    painter = painterResource(id = R.drawable.profilimage), // 🔹 Remplace avec ton image PNG
                    contentDescription = "Photo de profil",
                    modifier = Modifier
                        .size(300.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // 🔹 Bouton FAQ
                Button(
                    onClick = { navController.navigate("faq") }
                ) {
                    Text("FAQ")
                }

                Spacer(modifier = Modifier.weight(1f)) // 🔹 Pousse le bouton "Se déconnecter" vers le bas

                // 🔹 Bouton Se Déconnecter
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
