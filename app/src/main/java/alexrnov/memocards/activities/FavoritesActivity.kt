package alexrnov.memocards.activities

import alexrnov.memocards.Initialization.appStorage
import alexrnov.memocards.R
import alexrnov.memocards.cards.CardsSettings
import alexrnov.memocards.render.favorites.FavoritesSurfaceView
import alexrnov.memocards.render.game.GameSurfaceView
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class FavoritesActivity : AppCompatActivity() {
	private var favoritesSurfaceView: FavoritesSurfaceView? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val frontCardsSize: Int? = getResources().assets.list("front")?.size
		//val size = getAssets().list("front")?.size

		if (frontCardsSize == null) {
			return
		}

		val material = appStorage.getString("material", "pattern") ?: "pattern"
		val backCardsSize: Int? = getResources().assets.list("back/$material")?.size
		if (backCardsSize == null) {
			return
		}

		val cardsSettings = CardsSettings(
			frontCardsSize = frontCardsSize,
			backCardsSize = backCardsSize,
			material = material,
			cardsQuantity = appStorage.getInt("cardsQuantity", 12)
		)

		setContentView(R.layout.activity_favorites)

		favoritesSurfaceView = findViewById(R.id.favoritesOglView)
		favoritesSurfaceView?.init(applicationContext, cardsSettings)
		favoritesSurfaceView?.setGameActivity(this)

		// добавить прозрачность для статусбара и меню навигации
		window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
	}
}