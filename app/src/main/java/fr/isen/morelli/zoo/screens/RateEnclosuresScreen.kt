package fr.isen.morelli.zoo.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase
import fr.isen.morelli.zoo.R
import fr.isen.morelli.zoo.model.Enclosure
import fr.isen.morelli.zoo.repository.FirebaseRepository

@Composable
fun RateEnclosuresScreen(navController: NavController) {
    val repository = remember { FirebaseRepository() }
    val biomes by repository.biomes.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Noter un Enclos") }) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text("Choisissez un enclos :", style = MaterialTheme.typography.h6)

                LazyColumn {
                    items(biomes.flatMap { it.enclosures }) { enclosure ->
                        ExpandableEnclosureItem(enclosure, navController)
                    }
                }
            }
        }
    )
}

@Composable
fun ExpandableEnclosureItem(enclosure: Enclosure, navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    var rating by remember { mutableStateOf(3) }
    var comment by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = !expanded } // Gère l'expansion
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Enclos: ${enclosure.id}", style = MaterialTheme.typography.h6)

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))

                // Notation
                Text("Note :", style = MaterialTheme.typography.subtitle1)
                RatingBar(rating) { rating = it }

                // Commentaire
                TextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Laisser un commentaire") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Bouton d'envoi
                Button(
                    onClick = { submitRating(enclosure, rating, comment, navController) },
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
                    contentDescription = "Note $index"
                )
            }
        }
    }
}

fun submitRating(enclosure: Enclosure, rating: Int, comment: String, navController: NavController) {
    val database = FirebaseDatabase.getInstance().getReference("ratings").push()
    val ratingData = mapOf(
        "enclosureId" to enclosure.id,
        "rating" to rating,
        "comment" to comment
    )
    database.setValue(ratingData)
        .addOnSuccessListener { navController.popBackStack() }
        .addOnFailureListener {
            // Gérer l'échec
        }
}
