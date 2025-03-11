package fr.isen.morelli.zoo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fr.isen.morelli.zoo.R

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val auth = Firebase.auth
    val database = FirebaseDatabase.getInstance().reference // Référence vers la Realtime Database

    Column(
        modifier = Modifier
            .fillMaxSize() // Prend toute la hauteur de l'écran
            .padding(16.dp)
    ) {
        // Afficher le logo en haut de l'écran
        Spacer(modifier = Modifier.height(50.dp))

        Image(
            painter = painterResource(id = R.drawable.logo), // Remplacez par le nom de votre logo
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth() // Remplir toute la largeur
                .height(300.dp) // Ajustez la hauteur du logo selon vos besoins
                .align(Alignment.CenterHorizontally) // Centrer l'image
        )

        // Ajouter un Spacer pour pousser le contenu vers le bas
        Spacer(modifier = Modifier.height(100.dp)) // Espace pour pousser vers le bas

        // Formulaire de connexion (email et mot de passe)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mot de passe") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (email.isBlank() || password.isBlank()) {
                    errorMessage = "Veuillez remplir tous les champs."
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    errorMessage = "Adresse e-mail invalide."
                } else if (password.length < 6) {
                    errorMessage = "Le mot de passe doit contenir au moins 6 caractères."
                } else {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userId = auth.currentUser?.uid
                                userId?.let {
                                    // Récupérer les données de l'utilisateur depuis la base de données
                                    database.child("users").child(userId).get()
                                        .addOnSuccessListener { snapshot ->
                                            val role = snapshot.child("role").value.toString()
                                            // Rediriger en fonction du rôle
                                            if (role == "admin") {
                                                navController.navigate("admin_home") // Redirection vers la page admin
                                            } else {
                                                navController.navigate("home") // Redirection vers la page utilisateur
                                            }
                                        }
                                        .addOnFailureListener {
                                            errorMessage = "Erreur lors de la récupération du rôle"
                                        }
                                }
                            } else {
                                errorMessage = "Erreur: ${task.exception?.message}"
                            }
                        }
                }
            }) {
                Text("Se connecter")
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }

            // Lien vers l'écran d'inscription
            TextButton(onClick = { navController.navigate("signup") }) {
                Text("Pas encore de compte ? S'inscrire")
            }
        }
    }
}
