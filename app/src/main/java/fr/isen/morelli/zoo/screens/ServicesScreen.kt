package fr.isen.morelli.zoo.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.isen.morelli.zoo.R

data class Service(val name: String, val locations: List<String>, val iconRes: Int)

val services = listOf(
    Service("Urgence", listOf("Numéro d'urgence au Parc:                06 31 29 35 98"), R.drawable.emergency),
    Service("Toilettes", listOf("Entrée principale", "le Plateau", "les Clairières (Chien des buissons)"), R.drawable.toilettes),
    Service("Point d'eau", listOf("le Plateau", "le Plateau (Girafe)", "le Vallon des cascades (Mouflon)", "les Clairières (Lynx)", "les Clairières (Wallaby)"), R.drawable.waterpoint),
    Service("Boutique", listOf("Entrée principale"), R.drawable.shop),
    Service("Gare", listOf("Entrée principale (Gare des cascades)", "le Plateau (Gare du plateau)"), R.drawable.trainstation),
    Service("Lodge", listOf("le Plateau"), R.drawable.lodge),
    Service("Tente pédagogique", listOf("le Plateau (Zèbre)"), R.drawable.tente),
    Service("Restaurant", listOf("Entrée principale (Restaurant du parc)", "Entrée principale (Petit café)", "le Plateau (Paillote)", "Le Vallon des cascades (Café nomade)"), R.drawable.restaurant),
    Service("Espace de pique-nique", listOf("le Plateau", "le Vallon des cascades"), R.drawable.pique_nique),
    Service("Plateau de jeux", listOf("le Plateau"), R.drawable.gamearea),
    Service("Point de vue", listOf("le Plateau (Girafe)", "le Belvédère (Rhinocéros)", "le Belvédère (Suricate)"), R.drawable.vuepoint)
)

val brownColor = Color(0xFF9B5D5D) // Marron
val greenColor = Color(0xFFC0EA9E) // Vert

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(navController: NavController) {
    var expandedService by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Services du Zoo", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(modifier = Modifier.padding(padding).padding(12.dp)) {
                itemsIndexed(services) { index, (serviceName, locations, iconRes) ->
                    val backgroundColor = if (index % 2 == 0) brownColor else greenColor

                    ServiceItem(
                        serviceName = serviceName,
                        locations = locations,
                        iconRes = iconRes,
                        isExpanded = expandedService == serviceName,
                        onClick = {
                            expandedService = if (expandedService == serviceName) null else serviceName
                        },
                        backgroundColor = backgroundColor
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceItem(
    serviceName: String,
    locations: List<String>,
    iconRes: Int,
    isExpanded: Boolean,
    onClick: () -> Unit,
    backgroundColor: Color
) {
    val uriHandler = LocalUriHandler.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(8.dp, shape = MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .animateContentSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = serviceName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                locations.forEach { location ->
                    val isPhoneNumber = location.trim().matches(Regex(".*\\d{2} \\d{2} \\d{2} \\d{2} \\d{2}.*"))

                    Text(
                        text = "• $location",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isPhoneNumber) Color.Yellow else Color.White,
                        modifier = Modifier
                            .padding(start = 32.dp, top = 4.dp)
                            .clickable(enabled = isPhoneNumber) {
                                if (isPhoneNumber) {
                                    val phoneNumber = location.filter { it.isDigit() }
                                    uriHandler.openUri("tel:$phoneNumber")
                                }
                            }
                    )
                }
            }
        }
    }
}