package fr.isen.morelli.zoo.model

data class Animal(
    val id: String = "",
    val id_animal: String = "",
    val id_enclos: String = "",
    val name: String = ""
)

data class Enclosure(
    val id: String = "",
    val id_biomes: String = "",
    val meal: String = "",
    val meal_time: String = "",
    val isopen: Boolean = true, // ✅ Ajout de isOpen
    val inmaintenance: Boolean = false, // 🛠️ Ajout de inMaintenance
    val animals: List<Animal> = emptyList()
)

data class Biome(
    val id: String = "",
    val name: String = "",
    val color: String = "#FFFFFF",
    val enclosures: List<Enclosure> = emptyList()
)

data class GPSPoint(
    val name: String, // Nom de l'animal ou du point d'intérêt
    val lat: String,  // Latitude
    val lon: String, // Longitude
    val color: String
)

data class Rating(
    val enclosureId: String = "",
    val rating: Int = 0,
    val comment: String = ""
)
