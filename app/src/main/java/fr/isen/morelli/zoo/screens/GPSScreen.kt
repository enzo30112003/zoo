package fr.isen.morelli.zoo.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.isen.morelli.zoo.model.GPSPoint
import fr.isen.morelli.zoo.repository.FirebaseRepository
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GPSScreen(navController: NavController, repository: FirebaseRepository) {
    val gpsPoints by repository.gpsPoints.collectAsState()

    // Afficher un indicateur de chargement tant que les points GPS ne sont pas disponibles
    if (gpsPoints.isEmpty()) {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Sélectionnez votre destination") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
        return
    }

    // Grouper les points GPS par couleur (si la liste n'est pas vide)
    val groupedByColor = gpsPoints.groupBy { it.color }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Sélectionnez votre destination") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            LazyColumn {
                // Afficher chaque groupe par couleur
                groupedByColor.forEach { (color, points) ->
                    items(points) { point ->
                        GPSPointItem(point, navController, color)
                    }
                }
            }
        }
    }
}

@Composable
fun GPSPointItem(point: GPSPoint, navController: NavController, color: String) {
    val validColor = try {
        // Nettoyer la couleur (enlever les espaces indésirables)
        Color(android.graphics.Color.parseColor(color.trim()))
    } catch (e: IllegalArgumentException) {
        // Si la couleur est invalide, utiliser une couleur par défaut
        Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                // Naviguer vers l'écran de navigation avec les coordonnées GPS
                navController.navigate("navigation/${point.name}/${point.lat}/${point.lon}")
            },
        colors = CardDefaults.cardColors(containerColor = validColor) // Appliquer la couleur valide ou par défaut
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = point.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "Lat: ${point.lat}, Lon: ${point.lon}")
        }
    }
}

