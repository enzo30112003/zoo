package fr.isen.morelli.zoo.screens

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.isen.morelli.zoo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import androidx.compose.foundation.shape.RoundedCornerShape



@Composable
fun SignUpScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().reference // Référence vers la Realtime Database

    Column(
        modifier = Modifier
            .fillMaxSize() // Remplir toute la taille de l'écran
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // Centrer les éléments horizontalement
        verticalArrangement = Arrangement.Top // Organiser les éléments du haut vers le bas
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
        Spacer(modifier = Modifier.height(50.dp))

        // Titre de la page
        Text("Créer un compte", style = MaterialTheme.typography.h5)

        Spacer(modifier = Modifier.height(16.dp))

        // Formulaire d'inscription
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = (Color(0xFF914D2E)),
                unfocusedBorderColor = (Color(0xFF000000)),
                cursorColor = (Color(0xFF914D2E)),
                focusedLabelColor = (Color(0xFF914D2E)),
                unfocusedLabelColor = (Color(0xFF000000))
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = (Color(0xFF914D2E)),
                unfocusedBorderColor = (Color(0xFF000000)),
                cursorColor = (Color(0xFF914D2E)),
                focusedLabelColor = (Color(0xFF914D2E)),
                unfocusedLabelColor = (Color(0xFF000000))
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmez le mot de passe") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = (Color(0xFF914D2E)),
                unfocusedBorderColor = (Color(0xFF000000)),
                cursorColor = (Color(0xFF914D2E)),
                focusedLabelColor = (Color(0xFF914D2E)),
                unfocusedLabelColor = (Color(0xFF000000))
            )

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
                // Inscription avec Firebase Authentication
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            val role = "user"

                            val userData = mapOf(
                                "email" to email,
                                "role" to role
                            )

                            userId?.let {
                                // Ajout des données dans Firebase Realtime Database
                                database.child("users").child(it).setValue(userData)
                                    .addOnSuccessListener {
                                        // Après l'enregistrement, on redirige vers la page Login
                                        navController.navigate("login") {
                                            popUpTo("signup") { inclusive = true }
                                        }
                                    }
                                    .addOnFailureListener {
                                        errorMessage = "Erreur lors de l'enregistrement"
                                    }
                            }
                        } else {
                            errorMessage = "Erreur : ${task.exception?.message}"
                        }
                    }
            }
        },colors = ButtonDefaults.buttonColors(
            backgroundColor = (Color(0xFF914D2E)),
            contentColor = Color.White
        ),shape = RoundedCornerShape(50.dp)) {
            Text("S'inscrire")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colors.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lien vers l'écran de connexion
        TextButton(onClick = { navController.navigate("login") },
            colors = ButtonDefaults.textButtonColors(
                contentColor = (Color(0xFF914D2E))
            )) {
            Text("Déjà un compte ? Se connecter")
        }
    }
}


