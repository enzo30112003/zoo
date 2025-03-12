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
    val animals: List<Animal> = emptyList()
)

data class Biome(
    val id: String = "",
    val name: String = "",
    val color: String = "#FFFFFF",
    val enclosures: List<Enclosure> = emptyList()
)
