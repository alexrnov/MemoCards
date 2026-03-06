package alexrnov.memocards.view.activity

import alexrnov.memocards.Initialization.appStorage
import alexrnov.memocards.R
import alexrnov.memocards.cards.CardsSettings
import alexrnov.memocards.render.favorites.FavoritesSurfaceView
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class FavoritesActivity : AppCompatActivity() {
	private var favoritesSurfaceView: FavoritesSurfaceView? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContentView(R.layout.activity_favorites)

		favoritesSurfaceView = findViewById(R.id.favoritesOglView)
		favoritesSurfaceView?.init(applicationContext, )
		favoritesSurfaceView?.setGameActivity(this)

		// добавить прозрачность для статусбара и меню навигации
		window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
	}
}