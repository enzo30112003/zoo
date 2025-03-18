package fr.isen.morelli.zoo.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase
import fr.isen.morelli.zoo.R
import fr.isen.morelli.zoo.model.Biome
import fr.isen.morelli.zoo.model.Enclosure
import fr.isen.morelli.zoo.repository.FirebaseRepository
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateEnclosuresScreen(navController: NavController) {
    val repository = remember { FirebaseRepository() }
    val biomes by repository.biomes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Noter un Enclos") },
            )

        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text("Choisissez un enclos :", style = MaterialTheme.typography.headlineMedium)

                if (biomes.isEmpty()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    LazyColumn {
                        items(biomes) { biome ->
                            BiomeSection(biome, navController)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun BiomeSection(biome: Biome, navController: NavController) {
    val biomeColor = try {
        Color(android.graphics.Color.parseColor(biome.color)) // Convertir HEX en couleur
    } catch (e: Exception) {
        Color.Gray // Sécurité en cas de couleur invalide
    }

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Card(
            colors = CardDefaults.cardColors(containerColor = biomeColor),
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Biome : ${biome.name}",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )

                biome.enclosures.forEach { enclosure ->
                    ExpandableEnclosureItem(biome.id, enclosure, navController)
                }
            }
        }
    }
}

@Composable
fun ExpandableEnclosureItem(biomeId: String, enclosure: Enclosure, navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    var rating by remember { mutableStateOf(3) }
    var comment by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = !expanded } // Gère l'expansion
        ,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val enclosureId = "${biomeId}_${enclosure.id}"

            Text(text = "Enclos: ${enclosure.id}", style = MaterialTheme.typography.titleMedium, color = Color.White)

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))

                // Notation
                Text("Note :", style = MaterialTheme.typography.bodyLarge)
                RatingBar(rating) { rating = it }

                // Commentaire
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Laisser un commentaire") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Bouton d'envoi
                Button(
                    onClick = { submitRating(enclosureId, rating, comment, navController) },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Envoyer")
                }
            }
        }
    }
}

@Composable
fun RatingBar(currentRating: Int, onRatingChanged: (Int) -> Unit) {
    Row {
        (1..5).forEach { index ->
            IconButton(onClick = { onRatingChanged(index) }) {
                Icon(
                    painter = painterResource(id = if (index <= currentRating) R.drawable.yellowstar else R.drawable.whitestar),
                    contentDescription = "Note $index",
                    tint = Color.Yellow
                )
            }
        }
    }
}

fun submitRating(enclosureId: String, rating: Int, comment: String, navController: NavController) {
    val database = FirebaseDatabase.getInstance().getReference("ratings").push()
    val ratingData = mapOf(
        "enclosureId" to enclosureId, // 🔹 On stocke le nouvel ID complet (biome_enclos)
        "rating" to rating,
        "comment" to comment
    )
    database.setValue(ratingData)
        .addOnSuccessListener { navController.popBackStack() }
        .addOnFailureListener {
            // Gérer l'échec (peut ajouter un Snackbar pour afficher une erreur)
        }
}

