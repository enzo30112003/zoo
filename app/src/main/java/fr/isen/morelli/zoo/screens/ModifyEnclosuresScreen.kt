package fr.isen.morelli.zoo.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.isen.morelli.zoo.R
import fr.isen.morelli.zoo.model.Enclosure
import fr.isen.morelli.zoo.repository.FirebaseRepository
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.Color

@Composable
fun ModifyEnclosuresScreen(navController: NavController, repository: FirebaseRepository) {
    val biomes by repository.biomes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Gérer les Enclos") })
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
                            Text(text = biome.name, fontSize = 20.sp, modifier = Modifier.padding(8.dp))
                            biome.enclosures.forEach { enclosure ->
                                EnclosureAdminCard(enclosure) { updatedEnclosure ->
                                    repository.updateEnclosure(updatedEnclosure)
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun EnclosureAdminCard(enclosure: Enclosure, onStatusChange: (Enclosure) -> Unit) {
    Card(
        backgroundColor = Color.LightGray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = enclosure.id, fontSize = 20.sp, modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(
                        id = getEnclosureStatusIcon(enclosure.isopen, enclosure.inmaintenance)
                    ),
                    contentDescription = "Statut enclos",
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        onStatusChange(enclosure.copy(isopen = !enclosure.isopen))
                    }
                ) {
                    Text(if (enclosure.isopen) "Fermer" else "Ouvrir")
                }

                Button(
                    onClick = {
                        onStatusChange(enclosure.copy(inmaintenance = !enclosure.inmaintenance))
                    }
                ) {
                    Text(if (enclosure.inmaintenance) "Fin Maintenance" else "Mettre en maintenance")
                }
            }
        }
    }
}

@Composable
fun getEnclosureStatusIcon(isopen: Boolean, inmaintenance: Boolean): Int {
    return when {
        !isopen -> R.drawable.close // 🚫 Fermé
        inmaintenance -> R.drawable.maintenance // 🛠️ En maintenance
        else -> R.drawable.open // ✅ Ouvert
    }
}