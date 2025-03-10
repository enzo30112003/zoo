package fr.isen.morelli.zoo.screens

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.compose.ui.res.painterResource
import fr.isen.morelli.zoo.R

@Composable
fun SignUpScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val auth = Firebase.auth

    Column(
        modifier = Modifier
            .fillMaxSize() // Remplir toute la taille de l'écran
            .padding(16.dp)
    ) {
        // Afficher l'image du logo en haut
        Spacer(modifier = Modifier.height(50.dp))
        Image(
            painter = painterResource(id = R.drawable.logo), // Remplacez par le nom de votre logo
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth() // L'image occupe toute la largeur
                .height(250.dp) // Ajustez la hauteur pour rendre le logo plus grand
                .align(Alignment.CenterHorizontally) // Centrer l'image horizontalement
        )

        // Spacer pour ajuster l'espace sous le logo
        Spacer(modifier = Modifier.height(50.dp)) // Ajustez cette valeur pour remonter ou descendre le contenu

        // Titre de la page
        Text("Créer un compte", style = MaterialTheme.typography.h5)

        Spacer(modifier = Modifier.height(16.dp))

        // Formulaire d'inscription
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

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmez le mot de passe") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                errorMessage = "Veuillez remplir tous les champs."
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                errorMessage = "Adresse e-mail invalide."
            } else if (password.length < 6) {
                errorMessage = "Le mot de passe doit contenir au moins 6 caractères."
            } else if (password != confirmPassword) {
                errorMessage = "Les mots de passe ne correspondent pas."
            } else {
                // Inscription avec Firebase
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navController.navigate("home") // Rediriger vers l'accueil
                        } else {
                            errorMessage = "Erreur: ${task.exception?.message}"
                        }
                    }
            }
        }) {
            Text("S'inscrire")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colors.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lien vers l'écran de connexion
        TextButton(onClick = { navController.navigate("login") }) {
            Text("Déjà un compte ? Se connecter")
        }
    }
}
