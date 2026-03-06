package alexrnov.memocards.cards

import alexrnov.enginegl.Textures
import alexrnov.memocards.Initialization.appStorage
import alexrnov.memocards.enginegl.Object3D
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import kotlin.Int
import kotlin.random.Random

class CardsCreator {
	private val cardQuality: Int get() = appStorage.getInt("cardsQuantity", 12)
	private val cardPairs: Int get() = cardQuality / 2

	fun createCards(context: Context, scale: Float, cardsSettings: CardsSettings): Map<Int, Card> {
		val frontPictures = frontPictures(cardsSettings)

		val cardsAsPaths = if (cardsSettings.material == "pattern"
				|| cardsSettings.material == "plastic") {
			val backPicture = getBackPicture(cardsSettings)
			getCardsWithOneBackground(frontPictures, backPicture)
		} else {
			val backPictures = getBackPictures(cardsSettings)
			getCardsWithDifferentBackground(frontPictures, backPictures)
		}

		/*
		cardsAsPaths.forEach {
			println("card = $it")
		}


		 */
		appStorage.edit { putStringSet("cards", cardsAsPaths) }

		val cardsWithTextures = getCardsWithTextures(context, cardsAsPaths, scale)
		return cardsWithTextures
	}

	fun recoveryCards(context: Context, scale: Float): Map<Int, Card> {
		val cards = appStorage.getStringSet("cards", emptySet())
		cards?.let {
			val cardsWithTextures = getCardsWithTextures(context, it, scale)
			return cardsWithTextures
		}
		return emptyMap()
	}

	private fun frontPictures(cardsSettings: CardsSettings): List<String> {
		val frontCardsSize = cardsSettings.frontCardsSize
		val frontNumbers = (1..frontCardsSize).shuffled(Random.Default).take(cardPairs)
		val frontPictures: MutableList<String> = mutableListOf()
		(0..cardPairs - 1).forEach {
			frontPictures.add("front/${frontNumbers[it]}.jpg")
		}
		return frontPictures
	}

	private fun getBackPicture(cardsSettings: CardsSettings): String {
		val material = cardsSettings.material
		val backCardsSize = cardsSettings.backCardsSize
		val backNumber = (1..backCardsSize).random()
		return "back/$material/${backNumber}.jpg"
	}

	private fun getCardsWithOneBackground(
		frontPictures: List<String>,
		backPicture: String
	): Set<String> {
		val cardsWithPaths: MutableList<String> = mutableListOf()
		for (i in 0..cardPairs - 1) {
			cardsWithPaths.add("${i}:${frontPictures[i]}:$backPicture")
			cardsWithPaths.add("${i}:${frontPictures[i]}:$backPicture")
		}
		return cardsWithPaths
			.shuffled(Random.Default)
			.mapIndexed { index, it -> "$index:$it" }
			.toSet()
	}

	private fun getBackPictures(cardsSettings: CardsSettings): ArrayDeque<String> {
		val backCardsSize = cardsSettings.backCardsSize
		val material = cardsSettings.material
		val backNumbers = (1..backCardsSize).shuffled(Random.Default).take(cardQuality)
		val backPictures = ArrayDeque<String>()
		(0..backNumbers.size - 1).forEach {
			backPictures.add("back/$material/${backNumbers[it]}.jpg")
		}
		return backPictures
	}

	private fun getCardsWithDifferentBackground(
		frontPictures: List<String>,
		backPictures: ArrayDeque<String>
	): Set<String> {
		val cardsWithPaths: MutableList<String> = mutableListOf()
		for (i in 0..cardPairs - 1) {
			cardsWithPaths.add("${i}:${frontPictures[i]}:${backPictures.removeFirst()}")
			cardsWithPaths.add("${i}:${frontPictures[i]}:${backPictures.removeFirst()}")
		}
		return cardsWithPaths
			.shuffled(Random.Default)
			.mapIndexed { index, it -> "$index:$it" }
			.toSet()
	}

	private fun getCardsWithTextures(
		context: Context,
		cardsWithPaths: Set<String>,
		scale: Float
	): Map<Int, Card> {
		val cardsWithTextures: MutableMap<Int, Card> = mutableMapOf()

		cardsWithPaths.forEach { card ->
			val cardData = card.split(":")
			Log.i("memo", "create card = $card")
			val frontTextureId = Textures.loadTextureWithMipMapFromAsset(context, cardData[2])
			val firstBackTextureId = Textures.loadTextureWithMipMapFromAsset(context, cardData[3])
			val card = createCard(context, cardData[1].toInt(), frontTextureId, firstBackTextureId, scale, cardData[2])
			cardsWithTextures.put(cardData[0].toInt(), card)
		}
		return cardsWithTextures
	}

	private fun createCard(context: Context, id: Int, pic: Int, backPic: Int, scale: Float, frontPath: String): Card {
		return Card(
			id,
			Object3D(context, "objects/front.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", pic, scale),
			Object3D(context, "objects/back.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", backPic, scale),
			frontPath
		)
	}

	/**
	 * Создать карты на основе данных БД (избранные карты)
	 */
	fun createCardsFromDB(context: Context, scale: Float, frontPaths: List<String>): Map<Int, Card> {
		val cardsAsPaths = getCardsWithOneBackgroundFromDB(frontPaths)

		cardsAsPaths.forEach {
			println("favorite card = $it")
		}

		val cardsWithTextures = getCardsWithTextures(context, cardsAsPaths, scale)
		return cardsWithTextures
	}

	private fun getCardsWithOneBackgroundFromDB(
		frontPictures: List<String>,
	): Set<String> {
		val backPicture = "back/pattern/1.jpg"
		val clearPicture = "back/pattern/2.jpg"
		val cardsWithPaths: MutableList<String> = mutableListOf()
		for (i in 0..11) {
			if (i < frontPictures.size) {
				cardsWithPaths.add("${i}:${frontPictures[i]}:$backPicture")
			} else {
				cardsWithPaths.add("${i}:$clearPicture:$backPicture")
			}
		}
		return cardsWithPaths
			.mapIndexed { index, it -> "$index:$it" }
			.toSet()
	}
}