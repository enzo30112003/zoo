package fr.isen.morelli.zoo.repository

import android.util.Log
import com.google.firebase.database.*
import fr.isen.morelli.zoo.model.Biome
import fr.isen.morelli.zoo.model.Enclosure
import fr.isen.morelli.zoo.model.Animal
import fr.isen.morelli.zoo.model.GPSPoint
import fr.isen.morelli.zoo.model.Rating
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FirebaseRepository {
    private val database = FirebaseDatabase.getInstance().reference

    // Pour les biomes et enclos
    private val _biomes = MutableStateFlow<List<Biome>>(emptyList())
    val biomes: StateFlow<List<Biome>> = _biomes

    private val _enclosuresStatus = MutableStateFlow<Map<String, Pair<Boolean, Boolean>>>(emptyMap())
    val enclosuresStatus: StateFlow<Map<String, Pair<Boolean, Boolean>>> = _enclosuresStatus

    // Pour les points GPS
    private val _gpsPoints = MutableStateFlow<List<GPSPoint>>(emptyList())
    val gpsPoints: StateFlow<List<GPSPoint>> = _gpsPoints

    init {
        fetchBiomes()
        fetchGPSPoints() // Récupérer les points GPS au démarrage
    }

    // Récupérer les biomes et enclos
    private fun fetchBiomes() {
        database.child("biomes").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    Log.e("Firebase", "Aucune donnée trouvée")
                    return
                }
                Log.d("Firebase", "Données reçues : ${snapshot.value}")

                val biomeList = snapshot.children.mapNotNull { biomeSnapshot ->
                    val biomeId = biomeSnapshot.key ?: ""
                    val biome = biomeSnapshot.getValue(Biome::class.java)?.copy(
                        id = biomeId,
                        color = biomeSnapshot.child("color").getValue(String::class.java) ?: "#FFFFFF",
                        enclosures = biomeSnapshot.child("enclosures").children.mapNotNull { enclosureSnapshot ->
                            val enclosureId = enclosureSnapshot.key ?: ""
                            enclosureSnapshot.getValue(Enclosure::class.java)?.copy(
                                id = enclosureId,
                                id_biomes = biomeId,
                                meal = enclosureSnapshot.child("meal").getValue(String::class.java) ?: "",
                                meal_time = enclosureSnapshot.child("meal_time").getValue(String::class.java) ?: "",
                                isopen = enclosureSnapshot.child("isopen").getValue(Boolean::class.java) ?: true,
                                inmaintenance = enclosureSnapshot.child("inmaintenance").getValue(Boolean::class.java) ?: false,
                                animals = enclosureSnapshot.child("animals").children.mapNotNull { animalSnapshot ->
                                    animalSnapshot.getValue(Animal::class.java)?.copy(
                                        id = animalSnapshot.key ?: "",
                                        name = animalSnapshot.child("name").getValue(String::class.java) ?: "",
                                        id_enclos = enclosureId,
                                        id_animal = animalSnapshot.child("id_animal").getValue(String::class.java) ?: ""
                                    )
                                }
                            )
                        }
                    )
                    biome
                }
                _biomes.value = biomeList

                // Mise à jour de enclosuresStatus avec une clé unique "biomeId_enclosureId"
                val statusMap = biomeList.flatMap { biome ->
                    biome.enclosures.map { enclosure ->
                        val uniqueKey = "${biome.id}_${enclosure.id}"
                        uniqueKey to Pair(enclosure.isopen, enclosure.inmaintenance)
                    }
                }.toMap()
                _enclosuresStatus.value = statusMap
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Erreur : ${error.message}")
            }
        })
    }

    // Récupérer les points GPS
    private fun fetchGPSPoints() {
        database.child("gps").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    Log.e("Firebase", "Aucune donnée GPS trouvée")
                    return
                }

                val points = snapshot.children.mapNotNull { animalSnapshot ->
                    val name = animalSnapshot.key ?: ""
                    val lat = animalSnapshot.child("lat").getValue(String::class.java) ?: ""
                    val lon = animalSnapshot.child("lon").getValue(String::class.java) ?: ""
                    val color = animalSnapshot.child("color").getValue(String::class.java) ?: "#FFFFFF"  // Valeur par défaut si la couleur est absente
                    GPSPoint(name, lat, lon, color)
                }
                _gpsPoints.value = points
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Erreur lors de la récupération des points GPS : ${error.message}")
            }
        })
    }

    // Mettre à jour un enclos
    fun updateEnclosure(enclosure: Enclosure) {
        val enclosureRef = database
            .child("biomes")
            .child(enclosure.id_biomes)
            .child("enclosures")
            .child(enclosure.id)

        val updates = mapOf(
            "meal" to enclosure.meal,
            "meal_time" to enclosure.meal_time,
            "isopen" to enclosure.isopen,
            "inmaintenance" to enclosure.inmaintenance
        )
        enclosureRef.updateChildren(updates)
            .addOnSuccessListener {
                Log.d("Firebase", "Enclos ${enclosure.id} mis à jour avec succès")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Erreur lors de la mise à jour de l'enclos ${enclosure.id}", e)
            }
    }

    // Récupérer les avis et commentaires (ajouté ici)
    fun getEnclosureRatings(): List<Rating> {
        val ratingsList = mutableListOf<Rating>()
        database.child("ratings").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (ratingSnapshot in snapshot.children) {
                        val rating = ratingSnapshot.getValue(Rating::class.java)
                        rating?.let { ratingsList.add(it) }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Erreur lors de la récupération des avis et commentaires : ${error.message}")
            }
        })
        return ratingsList
    }
}
