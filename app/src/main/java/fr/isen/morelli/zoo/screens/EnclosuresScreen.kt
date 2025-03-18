package fr.isen.morelli.zoo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.isen.morelli.zoo.model.Biome
import fr.isen.morelli.zoo.model.Enclosure
import fr.isen.morelli.zoo.repository.FirebaseRepository
import fr.isen.morelli.zoo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnclosuresScreen(navController: NavController, repository: FirebaseRepository) {
    val biomes by repository.biomes.collectAsState()
    val enclosuresStatus by repository.enclosuresStatus.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Liste des Biomes, Enclos et Animaux") })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // 🔹 Bouton pour noter les enclos préférés
                Button(
                    onClick = { navController.navigate("rateanimals") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Envie de noter vos enclos préférés ?")
                }

                if (biomes.isEmpty()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(biomes) { biome ->
                            BiomeCard(biome, enclosuresStatus)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun BiomeCard(biome: Biome, enclosuresStatus: Map<String, Pair<Boolean, Boolean>>) {
    val biomeColor = try {
        Color(android.graphics.Color.parseColor(biome.color)) // Convertir HEX en couleur
    } catch (e: Exception) {
        Color.Gray // Sécurité en cas de couleur invalide
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
                EnclosureCard(enclosure, enclosuresStatus)
            }
        }
    }
}

@Composable
fun EnclosureCard(enclosure: Enclosure, enclosuresStatus: Map<String, Pair<Boolean, Boolean>>) {
    val uniqueKey = "${enclosure.id_biomes}_${enclosure.id}"
    val status = enclosuresStatus[uniqueKey]
    val isopen = status?.first ?: true
    val inmaintenance = status?.second ?: false

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.LightGray), // Fond gris pour l'enclos
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = enclosure.id, fontSize = 18.sp)

                Spacer(modifier = Modifier.width(8.dp))

                val iconRes = when {
                    !isopen -> R.drawable.close
                    inmaintenance -> R.drawable.maintenance
                    else -> R.drawable.open
                }

                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = "Statut de l'enclos",
                    modifier = Modifier.size(24.dp)
                )
            }

            enclosure.animals.forEach { animal ->
                Text(text = "- ${animal.name}", fontSize = 16.sp)
            }
        }
    }
}
