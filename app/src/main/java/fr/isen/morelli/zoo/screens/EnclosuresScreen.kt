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
fun EnclosuresScreen(navController: NavController) {
    val repository = remember { FirebaseRepository() }
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
                Button(
                    onClick = { navController.navigate("rateenclosures") },
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
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(android.graphics.Color.parseColor(biome.color))),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
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
    val isopen = enclosure.isopen
    val inmaintenance = enclosure.inmaintenance

    Card(
        colors = CardDefaults.cardColors(containerColor = if (!isopen) Color.Red else if (inmaintenance) Color.Yellow else Color.LightGray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = enclosure.id, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                if (!isopen) {
                    Image(painter = painterResource(R.drawable.close), contentDescription = "Fermé")
                } else if (inmaintenance) {
                    Image(painter = painterResource(R.drawable.maintenance), contentDescription = "En Maintenance")
                }
            }
            enclosure.animals.forEach { animal ->
                Text(text = "- ${animal.name}", fontSize = 16.sp, color = Color.DarkGray)
            }
        }
    }
}