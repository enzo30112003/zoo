package fr.isen.morelli.zoo.repository

import android.util.Log
import com.google.firebase.database.*
import fr.isen.morelli.zoo.model.Animal
import fr.isen.morelli.zoo.model.Biome
import fr.isen.morelli.zoo.model.Enclosure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FirebaseRepository {
    private val database = FirebaseDatabase.getInstance().reference

    private val _biomes = MutableStateFlow<List<Biome>>(emptyList())
    val biomes: StateFlow<List<Biome>> = _biomes

    init {
        fetchBiomes()
    }

    private fun fetchBiomes() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val biomeList = snapshot.children.mapNotNull { biomeSnapshot ->
                    val id = biomeSnapshot.key ?: return@mapNotNull null
                    val name = biomeSnapshot.child("name").getValue(String::class.java) ?: "Nom inconnu"
                    val color = biomeSnapshot.child("color").getValue(String::class.java) ?: "#FFFFFF"

                    val enclosures = biomeSnapshot.child("enclosures").children.mapNotNull { enclosureSnapshot ->
                        val enclosureId = enclosureSnapshot.child("id").getValue(String::class.java) ?: return@mapNotNull null
                        val idBiomes = enclosureSnapshot.child("id_biomes").getValue(String::class.java) ?: ""
                        val meal = enclosureSnapshot.child("meal").getValue(String::class.java) ?: ""

                        val animals = enclosureSnapshot.child("animals").children.mapNotNull { animalSnapshot ->
                            val animalId = animalSnapshot.child("id").getValue(String::class.java) ?: return@mapNotNull null
                            val idAnimal = animalSnapshot.child("id_animal").getValue(String::class.java) ?: ""
                            val idEnclos = animalSnapshot.child("id_enclos").getValue(String::class.java) ?: ""
                            val animalName = animalSnapshot.child("name").getValue(String::class.java) ?: "Animal inconnu"

                            Animal(animalId, idAnimal, idEnclos, animalName)
                        }

                        Enclosure(enclosureId, idBiomes, meal, animals)
                    }

                    Biome(id, name, color, enclosures)
                }
                _biomes.value = biomeList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Erreur de chargement : ${error.message}")
            }
        })
    }
}
