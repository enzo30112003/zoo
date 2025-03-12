package fr.isen.morelli.zoo.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase
import fr.isen.morelli.zoo.model.Enclosure
import fr.isen.morelli.zoo.repository.FirebaseRepository
import androidx.compose.ui.res.painterResource
import fr.isen.morelli.zoo.R

@Composable
fun ModifyEnclosuresScreen(navController: NavController) {
    val repository = remember { FirebaseRepository() }
    val biomes by repository.biomes.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Gérer les Enclos") }) },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                biomes.forEach { biome ->
                    items(biome.enclosures) { enclosure ->
                        EnclosureManagementItem(enclosure)
                    }
                }
            }
        }
    )
}

@Composable
fun EnclosureManagementItem(enclosure: Enclosure) {
    var isOpen by remember { mutableStateOf(true) }
    var inMaintenance by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Enclos: ${enclosure.id}", style = MaterialTheme.typography.h6)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Statut: ${if (isOpen) "Ouvert" else "Fermé"}")
                Icon(
                    painter = painterResource(
                        id = if (isOpen) R.drawable.open else R.drawable.close
                    ),
                    contentDescription = null,
                    tint = if (isOpen) Color.Green else Color.Red,
                    modifier = Modifier.size(24.dp)
                )
                Switch(checked = isOpen, onCheckedChange = { newValue ->
                    isOpen = newValue
                    updateEnclosureStatus(enclosure.id, "isOpen", newValue)
                })
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Maintenance: ${if (inMaintenance) "Activée" else "Désactivée"}")
                Icon(
                    painter = painterResource(
                        id = if (inMaintenance) R.drawable.maintenance else R.drawable.nomaintenance
                    ),
                    contentDescription = null,
                    tint = if (inMaintenance) Color.Yellow else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Switch(checked = inMaintenance, onCheckedChange = { newValue ->
                    inMaintenance = newValue
                    updateEnclosureStatus(enclosure.id, "inMaintenance", newValue)
                })
            }
        }
    }
}

fun updateEnclosureStatus(enclosureId: String, field: String, value: Boolean) {
    val database = FirebaseDatabase.getInstance().getReference("enclosures/$enclosureId/$field")
    database.setValue(value)
}
