package fr.isen.morelli.zoo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.isen.morelli.zoo.R

@Composable
fun HomeScreen(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Image en arrière-plan
        Image(
            painter = painterResource(id = R.drawable.girafe), // Remplace par ton image
            contentDescription = "Image de fond",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Texte en haut mais centré en largeur
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp), // Garde la hauteur d’origine
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenue au Parc animalier de la Barben",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        // Barre de navigation
        BottomNavigation(
            backgroundColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            BottomNavigationItem(
                icon = { Icon(painter = painterResource(id = R.drawable.services), contentDescription = "Services", modifier = Modifier.size(30.dp)) },
                selected = selectedItem == 0,
                onClick = { selectedItem = 0; navController.navigate("services") }
            )
            BottomNavigationItem(
                icon = { Icon(painter = painterResource(id = R.drawable.animals), contentDescription = "Enclosures", modifier = Modifier.size(30.dp)) },
                selected = selectedItem == 1,
                onClick = { selectedItem = 1; navController.navigate("enclosures") }
            )
            BottomNavigationItem(
                icon = { Icon(painter = painterResource(id = R.drawable.gps), contentDescription = "GPS", modifier = Modifier.size(30.dp)) },
                selected = selectedItem == 2,
                onClick = { selectedItem = 2; navController.navigate("gps") }
            )
            BottomNavigationItem(
                icon = { Icon(painter = painterResource(id = R.drawable.planning), contentDescription = "Planning", modifier = Modifier.size(30.dp)) },
                selected = selectedItem == 3,
                onClick = { selectedItem = 3; navController.navigate("planning") }
            )
            BottomNavigationItem(
                icon = { Icon(painter = painterResource(id = R.drawable.profil), contentDescription = "Compte", modifier = Modifier.size(30.dp)) },
                selected = selectedItem == 4,
                onClick = { selectedItem = 4; navController.navigate("profil") }
            )
        }
    }
}
