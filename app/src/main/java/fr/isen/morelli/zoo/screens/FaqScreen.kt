package fr.isen.morelli.zoo.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaqScreen(navController: NavController) {
    val faqList = listOf(
        "Quels sont les horaires d'ouverture ?" to "Le zoo est ouvert de 9h à 18h tous les jours.",
        "Peut-on apporter de la nourriture ?" to "Oui, vous pouvez pique-niquer dans les espaces dédiés.",
        "Y a-t-il un parking gratuit ?" to "Oui, un parking gratuit est disponible à l'entrée.",
        "Les animaux sont-ils visibles par tous les temps ?" to "Oui, mais certains peuvent être moins actifs par mauvais temps.",
        "Quels sont les tarifs d'entrée ?" to "Le tarif adulte est de 15€, enfant 10€, et gratuit pour les moins de 3 ans.",
        "Les animaux peuvent-ils être nourris par les visiteurs ?" to "Non, pour leur santé, il est interdit de nourrir les animaux.",
        "Y a-t-il des visites guidées ?" to "Oui, des visites guidées sont proposées tous les week-ends à 14h.",
        "Le parc est-il accessible aux personnes à mobilité réduite ?" to "Oui, toutes les allées sont adaptées aux fauteuils roulants.",
        "Les chiens sont-ils autorisés ?" to "Non, pour la sécurité des animaux du parc, les chiens ne sont pas admis.",
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FAQ",textAlign = TextAlign.Center) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(faqList) { (question, answer) ->
                FaqItem(question, answer)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun FaqItem(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = question,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = answer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { expanded = !expanded },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(if (expanded) "Masquer" else "Afficher la réponse")
            }
        }
    }
}
