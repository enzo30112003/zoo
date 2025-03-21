package fr.isen.morelli.zoo.screens

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.isen.morelli.zoo.service.LocationService
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScreen(navController: NavController, destinationName: String, destLat: String, destLon: String) {
    val context = LocalContext.current
    val locationService = remember { LocationService(context) }

    var userLatitude by remember { mutableStateOf(0.0) }
    var userLongitude by remember { mutableStateOf(0.0) }
    var azimuth by remember { mutableStateOf(0f) }
    var smoothedAzimuth by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        locationService.getCurrentLocation { lat, lon ->
            userLatitude = lat
            userLongitude = lon
        }

        locationService.getCompassDirection { angle ->
            smoothedAzimuth = (smoothedAzimuth * 0.85f) + (angle * 0.15f) // 🔹 Filtrage pour éviter les secousses
            azimuth = smoothedAzimuth
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Navigation vers $destinationName") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 Infos sur la navigation
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Destination : $destinationName", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Direction : ${getDirectionToPoint(userLatitude, userLongitude, destLat.toDouble(), destLon.toDouble())}")
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 🔹 Affichage de la boussole
            Compass(azimuth)

            Spacer(modifier = Modifier.weight(1f)) // 🔹 Fait descendre le bouton plus bas

            // 🔹 Bouton de retour
            Button(
                onClick = { navController.navigate("gps") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Retour à la sélection")
            }
        }
    }
}

// 🔹 Dessin de la boussole avec une flèche améliorée
@Composable
fun Compass(angle: Float) {
    Box(
        modifier = Modifier
            .size(340.dp) // 🔹 Taille de la boussole
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // 🔹 Rotation de la flèche pour pointer vers le nord
            rotate(angle) {  // Utilisation de l'angle sans inverser
                // Dessin de la boussole beige
                drawCircle(
                    color = Color(0xFFD8C99B), // Beige
                    radius = size.minDimension / 2
                )

                // 🔹 Dessin de la flèche élégante
                val path = Path().apply {
                    moveTo(size.width / 2, size.height / 6) // Pointe de la flèche
                    lineTo(size.width / 2 - 8.dp.toPx(), size.height / 2) // Base gauche de la flèche
                    lineTo(size.width / 2 + 8.dp.toPx(), size.height / 2) // Base droite de la flèche
                    close()
                }
                drawPath(path, color = Color(0xFFB52A3D)) // Flèche rouge bordeaux
            }
        }

        // 🔹 Ajout des points cardinaux bien positionnés
        val directions = listOf("E", "SE", "S", "SO", "O", "NO", "N", "NE")
        val angles = listOf(0, 45, 90, 135, 180, 225, 270, 315)

        directions.forEachIndexed { index, label ->
            val rotation = angles[index].toFloat()
            Box(
                modifier = Modifier
                    .offset(
                        x = (120 * cos(Math.toRadians(rotation.toDouble()))).dp,
                        y = (120 * sin(Math.toRadians(rotation.toDouble()))).dp
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = label, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}

// 🔹 Fonction de direction textuelle
fun getDirectionToPoint(userLat: Double, userLon: Double, pointLat: Double, pointLon: Double): String {
    val deltaLat = pointLat - userLat
    val deltaLon = pointLon - userLon

    return when {
        deltaLat > 0 && deltaLon > 0 -> "Nord-Est"
        deltaLat > 0 && deltaLon < 0 -> "Nord-Ouest"
        deltaLat < 0 && deltaLon > 0 -> "Sud-Est"
        deltaLat < 0 && deltaLon < 0 -> "Sud-Ouest"
        deltaLat > 0 -> "Nord"
        deltaLat < 0 -> "Sud"
        deltaLon > 0 -> "Est"
        deltaLon < 0 -> "Ouest"
        else -> "Vous êtes arrivé à destination"
    }
}
