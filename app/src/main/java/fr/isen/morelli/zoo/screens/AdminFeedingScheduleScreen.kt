package fr.isen.morelli.zoo.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.isen.morelli.zoo.model.Enclosure
import fr.isen.morelli.zoo.model.Biome
import fr.isen.morelli.zoo.repository.FirebaseRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminFeedingScheduleScreen(navController: NavController, repository: FirebaseRepository) {
    val biomes by repository.biomes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Modifier les horaires de nourrissage") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (biomes.isEmpty()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    LazyColumn {
                        items(biomes) { biome ->
                            BiomeFeedingCard(biome, onUpdate = { updatedEnclosure ->
                                repository.updateEnclosure(updatedEnclosure)
                            })
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun BiomeFeedingCard(
    biome: Biome,
    onUpdate: (Enclosure) -> Unit
) {
    val biomeColor = try {
        Color(android.graphics.Color.parseColor(biome.color))
    } catch (e: Exception) {
        Color.Gray
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = biomeColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = biome.name, fontSize = 20.sp, color = Color.White)
            biome.enclosures.forEach { enclosure ->
                FeedingScheduleItem(enclosure, biomeColor, onUpdate)
            }
        }
    }
}

@Composable
fun FeedingScheduleItem(
    enclosure: Enclosure,
    biomeColor: Color, // 🔥 Passer la couleur du biome au bouton
    onUpdate: (Enclosure) -> Unit
) {
    var mealTime by remember { mutableStateOf(enclosure.meal_time ?: "") }
    val context = LocalContext.current // 🔥 Pour afficher le Toast

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Enclos: ${enclosure.id}", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = mealTime,
                onValueChange = { mealTime = it },
                label = { Text("Heure de nourrissage") }
            )
            Button(
                onClick = {
                    val updatedEnclosure = enclosure.copy(meal_time = mealTime)
                    onUpdate(updatedEnclosure)

                    // 🔥 Affichage du Toast ici
                    Toast.makeText(context, "Modification enregistrée", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = biomeColor) // 🔥 Couleur du bouton = couleur du biome
            ) {
                Text("Enregistrer", color = Color.White) // 🔥 Texte blanc pour le contraste
            }
        }
    }
}
