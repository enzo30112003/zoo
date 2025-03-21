package fr.isen.morelli.zoo.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import fr.isen.morelli.zoo.repository.FirebaseRepository
import fr.isen.morelli.zoo.model.Rating
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.ui.text.font.FontWeight


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnclosureRatingsScreen(navController: NavController, repository: FirebaseRepository) {
    val ratings = remember { repository.getEnclosureRatings() } // Récupérer les avis de Firebase

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Avis et Commentaires des Enclos") })
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(ratings) { rating ->
                    RatingItem(rating) // Afficher chaque évaluation
                }
            }
        }
    )
}

@Composable
fun RatingItem(rating: Rating) {
    // Ajouter un encadrement autour de chaque évaluation avec une Card
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.dp, Color.Gray) // Encadrement gris autour de la carte
            .padding(16.dp) // Espacement interne à l'intérieur du Card
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Enclos : ${rating.enclosureId}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Note : ${rating.rating}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Commentaire : ${rating.comment}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
