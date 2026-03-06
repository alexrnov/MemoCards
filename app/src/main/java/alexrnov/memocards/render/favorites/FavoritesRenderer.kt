package alexrnov.memocards.render.favorites

import alexrnov.memocards.Initialization
import alexrnov.memocards.view.activity.FavoritesActivity
import alexrnov.memocards.cards.Card
import alexrnov.memocards.cards.CardsCreator
import alexrnov.memocards.cards.setComposition
import alexrnov.memocards.database.favorites.FavoritesDatabase
import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import androidx.core.content.edit
import androidx.room.Room.databaseBuilder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.sin

class FavoritesRenderer(private val context: Context) : GLSurfaceView.Renderer {
	private var gameActivity: FavoritesActivity? = null
	private var ky = 0.30f // coefficient for camera rotation

	private val viewMatrix = FloatArray(16)
	private val projectionMatrix = FloatArray(16)

	private var cards: Map<Int, Card> = mapOf()

	private val rotationCameraRadius = 2.2f

	private var zCamera = 3.0f
	private val scale = 1.0f

	private var reset = false

	var screenWidth: Int = 0
	var screenHeight: Int = 0

	private var isPortrait = true

	private lateinit var favoritesDatabase: FavoritesDatabase

	override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
		Log.i("memo", "onSurfaceCreated")

		favoritesDatabase = databaseBuilder(
			context,
			FavoritesDatabase::class.java, "database_18"
		).allowMainThreadQueries().build()
		val requests = favoritesDatabase.requests()
		val favorites = requests.all
		favorites.forEach {
			Log.i("memo", "render card, id = ${it.id}, path = ${it.path}")
		}
		val favoritesPaths = favorites.mapNotNull { it.path }
		val cardsCreator = CardsCreator()
		cards = cardsCreator.createCardsFromDB(context, scale, favoritesPaths)

		cameraPosition(-350.0f)

		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.0f)
		GLES20.glHint(GLES20.GL_GENERATE_MIPMAP_HINT, GLES20.GL_FASTEST)
	}

	override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
		isPortrait = width < height
		Log.i("memo", "onSurfaceChanged")
		GLES20.glViewport(0, 0, width, height) // set screen size
		this.screenWidth = width
		this.screenHeight = height

		val aspect = width.toFloat() / height.toFloat()
		val k = 1f / 30 // coefficient is selected empirically

		if (isPortrait) { // portrait orientation
			Matrix.frustumM(projectionMatrix, 0, -1f * k, 1f * k, (1 / -aspect) * k, (1 / aspect) * k, 0.1f, 40f)
		} else { // landscape orientation
			Matrix.frustumM(projectionMatrix, 0, -aspect * k, aspect * k, -1f * k, 1f * k, 0.1f, 40f)
		}

		calibrateCamera(width, height)
		setComposition(cards, portrait = isPortrait)

		cards.forEach { card ->
			card.value.openCard()
		}
	}

	override fun onDrawFrame(gl: GL10?) {
		Log.i("memo", "onDraw")
		if (reset) {
			reset = false

			val requests = favoritesDatabase.requests()

			val favorites = requests.all
			favorites.forEach {
				Log.i("memo", "id = ${it.id}, path = ${it.path}")
			}
			val favoritesPaths = favorites.mapNotNull { it.path }
			val cardsCreator = CardsCreator()
			cards = cardsCreator.createCardsFromDB(context, scale, favoritesPaths)

			setComposition(cards, portrait = isPortrait)

			cards.forEach { card ->
				card.value.openCard()
			}
		}

		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
		GLES20.glEnable(GLES20.GL_DEPTH_TEST) // enable depth test

		GLES20.glEnable(GLES20.GL_CULL_FACE) // разрешить отбрасывание
		GLES20.glCullFace(GLES20.GL_BACK) // отбрасывать заднюю грань примитивов при рендеринге

		cards.forEach { index, card ->
			card.draw(viewMatrix, projectionMatrix)
		}
	}

	@Synchronized
	fun cameraPosition(yDistance: Float) {
		if ((!(ky < -0.5) || !(yDistance < 0.0)) && (!(ky > 0.5) || !(yDistance >= 0.0))) {
			ky = ky + yDistance * 0.001f
		}

		val yCamera = (rotationCameraRadius * sin(ky.toDouble())).toFloat()

		Matrix.setLookAtM(viewMatrix, 0, 0.0f, -yCamera, zCamera, 0f, 0.0f, 0f, 0f, 1.0f, 0.0f)
	}

	@Synchronized
	fun openCard(x: Float, y: Float) {
		val (index, card) = getSelectCard(x, y)
		if (index == null || card == null) {
			return
		}
	}

	@Synchronized
	fun removeFavoriteCard(x: Float, y: Float) {
		val (index, card) = getSelectCard(x, y)
		Log.i("memo", "index = $index, card = ${card}, card frontPath = ${card?.frontPath}")

		val favoritesDatabase = databaseBuilder(
			context,
			FavoritesDatabase::class.java, "database_18"
		).allowMainThreadQueries().build()
		val requests = favoritesDatabase.requests()
		card?.let {
			requests.deleteByPath(it.frontPath)
			reset = true
		}
	}

	private fun getSelectCard(x: Float, y: Float): Pair<Int?, Card?>  {
		val xPass = x
		val yPass = screenHeight - y

		var index: Int? = null
		var card: Card? = null

		cards.forEach { currentIndex, currentCard ->
			val vertices = currentCard.getVertices(projectionMatrix, screenWidth, screenHeight, scale, 0.500000f, 0.888800f, 0.001000f)
			
			val yMin = vertices.yMin
			val yMax = vertices.yMax
			val	xMin = vertices.xMax
			val	xMax = vertices.xMin

			if (xPass >= xMin && xPass <= xMax && yPass >= yMin && yPass <= yMax) {
				index = currentIndex
				card = currentCard
				return@forEach
			}
		}
		return Pair(index, card)
	}

	private fun calibrateCamera(screenWidth: Int, screenHeight: Int) {
		val card = cards.getValue(0)
		val (cardsByWidth, cardsByHeight) = cardsByScreen(cards.size, screenWidth < screenHeight)

		card.position(0.0f, 0.0f, 0.0f, 45.0f)
		var isScale = false
		var cardWidth: Float
		var cardHeight: Float

		while (!isScale) {
			card.defineView(viewMatrix, projectionMatrix)
			val objectSize = card.getSize(projectionMatrix, screenWidth, screenHeight, scale, 0.500000f, 0.888800f, 0.001000f)
			cardWidth = objectSize.width
			cardHeight = objectSize.height

			if (screenWidth / cardWidth > cardsByWidth
				&& screenHeight / cardHeight > cardsByHeight
			) {
				isScale = true
			} else {
				zCamera = zCamera + 0.1f
				cameraPosition(0f)
			}
		}
		Log.i("memo", "zCamera = " + zCamera)
	}

	private fun cardsByScreen(cardsQuantity: Int, isPortrait: Boolean): Pair<Float, Float> {
		return when (cardsQuantity) {
			12 -> if (isPortrait) Pair(3f, 4.5f) else Pair(6.5f, 2.3f)
			16 -> if (isPortrait) Pair(4.5f, 4.5f) else Pair(8.0f, 2.3f)
			20 -> if (isPortrait) Pair(4.5f, 4.5f) else Pair(11.2f, 2.3f)
			30 -> if (isPortrait) Pair(5.5f, 6.0f) else Pair(11.2f, 3.4f)
			else -> if (isPortrait) Pair(3f, 4.5f) else Pair(6.5f, 2.3f)
		}
	}

	fun setGameActivity(gameActivity: FavoritesActivity) {
		this.gameActivity = gameActivity
	}
}