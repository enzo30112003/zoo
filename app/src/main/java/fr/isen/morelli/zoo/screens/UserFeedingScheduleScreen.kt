package fr.isen.morelli.zoo.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.isen.morelli.zoo.model.Enclosure
import fr.isen.morelli.zoo.repository.FirebaseRepository
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserFeedingScheduleScreen(navController: NavController, repository: FirebaseRepository) {
    // On récupère les biomes depuis le repository
    val biomes by repository.biomes.collectAsState()

    // Liste mutable pour stocker les événements de nourrissage
    val allFeedings = remember { mutableStateListOf<FeedingEvent>() }

    // Lorsque les biomes changent, on met à jour la liste des événements
    LaunchedEffect(biomes) {
        allFeedings.clear() // On vide la liste avant de la remplir
        biomes.forEach { biome ->
            biome.enclosures.forEach { enclosure ->
                enclosure.meal_time?.let { mealTime ->
                    val mealType = enclosure.meal // Récupère le type de régime alimentaire (carnivore, herbivore, etc.)
                    // Ajout du type de régime alimentaire dans l'événement
                    allFeedings.add(FeedingEvent(mealTime, enclosure.id, biome.name, mealType))
                }
            }
        }

        // On trie la liste des événements en fonction de l'heure de nourrissage
        allFeedings.sortBy { feeding ->
            LocalTime.parse(feeding.time, DateTimeFormatter.ofPattern("HH:mm"))
        }
    }

    // Structure de l'interface utilisateur avec un Scaffold
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Planning du jour : nourrissage", fontWeight = FontWeight.Bold) },

            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF1F1F1)) // Appliquer un fond gris clair
            ) {
                if (allFeedings.isEmpty()) {
                    // Affiche un indicateur de chargement si la liste est vide
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    // Affiche la liste des événements de nourrissage
                    LazyColumn(modifier = Modifier.padding(16.dp)) {
                        items(allFeedings) { event ->
                            FeedingScheduleItem(event) // Composant pour chaque événement
                        }
                    }
                }
            }
        }
    )
}

// Fonction pour obtenir la couleur en fonction du type de régime alimentaire
fun getMealColor(meal: String): Color {
    return when (meal.lowercase()) {
        "carnivore" -> Color(0xFFFF8A80) // Pastel rouge
        "herbivore" -> Color(0xFF80E27E) // Pastel vert
        "omnivore" -> Color(0xFF80D8FF) // Pastel bleu
        "frugivore" -> Color(0xFFD1C4E9) // Pastel violet
        else -> Color(0xFFB0BEC5) // Pastel gris pour les types inconnus
    }
}

// Fonction pour obtenir la couleur du texte en fonction du fond
fun getTextColor(meal: String): Color {
    return when (meal.lowercase()) {
        "carnivore" -> Color.Black // Texte noir sur fond rouge pastel
        "herbivore" -> Color.Black // Texte noir sur fond vert pastel
        "omnivore" -> Color.Black // Texte noir sur fond bleu pastel
        "frugivore" -> Color.Black // Texte noir sur fond violet pastel
        else -> Color.Black // Texte noir sur fond gris pastel (ou inconnu)
    }
}

@Composable
fun FeedingScheduleItem(event: FeedingEvent) {
    val mealColor = getMealColor(event.meal) // Obtenez la couleur basée sur le régime alimentaire
    val textColor = getTextColor(event.meal) // Obtenez la couleur du texte

    // Carte pour chaque événement de nourrissage
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = mealColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Ajoute de l'ombre
        shape = MaterialTheme.shapes.medium // Coins arrondis
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Affiche l'heure du nourrissage
                Text(
                    text = event.time,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor, // Texte dans la couleur obtenue
                    modifier = Modifier.weight(0.3f)
                )
                // Affiche l'enclos et le biome
                Column(modifier = Modifier.weight(0.7f)) {
                    Text(text = "Enclos: ${event.enclosureId}", fontSize = 16.sp, color = textColor)
                    Text(text = "Biome: ${event.biome}", fontSize = 14.sp, color = Color(0xFF616161)) // Gris foncé pour le biome
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.LightGray, thickness = 1.dp) // Séparation visuelle
        }
    }
}

// Classe représentant un événement de nourrissage
data class FeedingEvent(
    val time: String, // Heure du nourrissage
    val enclosureId: String, // ID de l'enclos
    val biome: String, // Nom du biome
    val meal: String // Type de régime alimentaire
)
