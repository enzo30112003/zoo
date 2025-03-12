package fr.isen.morelli.zoo.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.isen.morelli.zoo.model.Biome
import fr.isen.morelli.zoo.repository.FirebaseRepository


@Composable
fun EnclosuresScreen(navController: NavController) {
    val repository = remember { FirebaseRepository() }
    val biomes by repository.biomes.collectAsState()

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
                // 🔥 Bouton pour aller vers RateAnimalsScreen
                Button(
                    onClick = { navController.navigate("rateanimals") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Envie de noter vos animaux préférés ?")
                }

                if (biomes.isEmpty()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(biomes) { biome ->
                            BiomeCard(biome)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun BiomeCard(biome: Biome) {
    Card(
        backgroundColor = Color(android.graphics.Color.parseColor(biome.color)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = biome.name, fontSize = 20.sp, color = Color.White)

            biome.enclosures.forEach { enclosure ->
                EnclosureCard(enclosure)
            }
        }
    }
}

@Composable
fun EnclosureCard(enclosure: fr.isen.morelli.zoo.model.Enclosure) {
    Card(
        backgroundColor = Color.LightGray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = enclosure.id, fontSize = 18.sp)
            enclosure.animals.forEach { animal ->
                Text(text = "- ${animal.name}", fontSize = 16.sp, color = Color.DarkGray)
            }
        }
    }
}
