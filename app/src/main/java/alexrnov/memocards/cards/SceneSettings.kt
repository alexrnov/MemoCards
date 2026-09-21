package alexrnov.memocards.cards

data class SceneSettings(
	val frontCardsSize: Int,
	val backCardsSize: Int,
	val material: String,
	val cardsQuantity: Int,
	val rotationSpeed: Float,
	val backgroundColor: String
)