package alexrnov.memocards.view.activity

import alexrnov.memocards.Initialization.appStorage
import alexrnov.memocards.R
import alexrnov.memocards.cards.CardsSettings
import alexrnov.memocards.database.favorites.FavoriteEntity
import alexrnov.memocards.database.favorites.FavoritesDatabase
import alexrnov.memocards.render.game.GameSurfaceView
import alexrnov.memocards.database.statistics.GameDatabase
import android.app.ActivityManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.room.Room.databaseBuilder
import alexrnov.memocards.databinding.ActivityGameBinding
import alexrnov.memocards.database.statistics.GameEntity
import alexrnov.memocards.view.binding.ExitDialogData
import android.graphics.Color
import android.view.Gravity
import android.widget.FrameLayout
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GameActivity : AppCompatActivity() {
    private lateinit var gameSurfaceView: GameSurfaceView
    private var exitDialog: ConstraintLayout? = null

    private lateinit var snackBarContainer: ConstraintLayout
    private var exitDialogData = ExitDialogData()

    private lateinit var favoritesDatabase: FavoritesDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        favoritesDatabase = databaseBuilder(
            applicationContext,
            FavoritesDatabase::class.java, "database_18"
        ).allowMainThreadQueries().build()

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

        //Log.i("memo", "frontCardsSize = $frontCardsSize, backCardsNumber = $backCardsSize")

        Log.i("memo", "GameActivity onCreate")
        val cardsSettings = CardsSettings(
            frontCardsSize = frontCardsSize,
            backCardsSize = backCardsSize,
            material = material,
            cardsQuantity = appStorage.getInt("cardsQuantity", 12)
        )

        val binding: ActivityGameBinding = DataBindingUtil.setContentView(this, R.layout.activity_game)

        val gameOver = appStorage.getBoolean("gameOver", false)

        if (!gameOver) {
            exitDialogData.update(
                title = getString(R.string.exit_dialog_title_pause),
                dialogText = getString(R.string.exit_dialog_text_pause)
            )
        } else {
            val errors = appStorage.getInt("errors", 0)
            exitDialogData.update(
                title = getString(R.string.exit_dialog_title_statistics),
                dialogText = "${getString(R.string.exit_dialog_text_statistics)} $errors"
            )
        }

        binding.exitDialogData = exitDialogData

        if (!isSupportedOpenGLES()) {
            return
        }

        val newGame = appStorage.getBoolean("newGame", true)
        if (newGame) {
            Log.i("memo", "set params")
            appStorage.edit {
                putInt("firstCardId", -1)
                putInt("firstCardIndex", -1)
                putInt("secondCardIndex", -1)
                putStringSet("openCards", emptySet<String>())
                putInt("errors", 0)
            }
        } else {
            Log.i("memo", "not set params")
        }
        gameSurfaceView = findViewById(R.id.oglView)
        gameSurfaceView.init(applicationContext, cardsSettings)
        gameSurfaceView.setGameActivity(this)
        snackBarContainer = findViewById(R.id.snackBarContainer)

        exitDialog = findViewById(R.id.exitDialogBackground)
        onBackPressedDispatcher.addCallback(this, callback)

        // добавить прозрачность для статусбара и меню навигации
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onResume() {
        Log.i("memo", "GameActivity onResume")
        val currentValue = appStorage.getString("material", "unknown")
        Log.i("memo", "onResume, currentValue = $currentValue")
        super.onResume()
    }

    override fun onPause() {
        Log.i("memo", "GameActivity onPause")
        super.onPause()
    }

    override fun onStart() {
        Log.i("memo", "GameActivity onStart")
        super.onStart()
    }

    override fun onStop() {
        Log.i("memo", "GameActivity onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.i("memo", "GameActivity onDestroy")
        super.onDestroy()
    }

    override fun onRestart() {
        Log.i("memo", "GameActivity onRestart")
        super.onRestart()
    }

    /**
     * Поскольку minSdk = 24 (Android 7.0), а поддержка OpenGL 3.0 начинается
     * c API level 18 (Jelly Bean), в шейдерах используется версия 3.0
     */
    private fun isSupportedOpenGLES(): Boolean {
        val info = (getSystemService(ACTIVITY_SERVICE) as ActivityManager)
            .deviceConfigurationInfo ?: return false
        return info.reqGlEsVersion >= 0x30000
    }

    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (exitDialog?.visibility == View.VISIBLE) {
                exitDialog?.visibility = View.GONE
            } else {
                exitDialog?.visibility = View.VISIBLE
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("exitDialogVisibility", exitDialog?.visibility == View.VISIBLE)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val isVisible = savedInstanceState.getBoolean("exitDialogVisibility")
        exitDialog?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun toMainMenu(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        // при возврате в главное меню - отчистить стек переходов
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun backToGame(view: View) {
        exitDialog?.visibility = View.GONE
    }

    fun addCardToFavorites(path: String) {
        val requests = favoritesDatabase.requests()

        //requests.deleteAllEntities()

        if (!requests.isPathExists(path)) {
            Log.i("memo", "insert")

            val lastCardId = requests.lastCardId
            val favoriteEntity = FavoriteEntity(lastCardId + 1, path)

            requests.insert(favoriteEntity)
        } else {
            Log.i("memo", "path is exist")
        }
    }

    fun finishGame(errors: Int) {
        appStorage.edit {
            putBoolean("gameOver", true)
        }
        exitDialogData.update(
            title = getString(R.string.exit_dialog_title_statistics),
            dialogText = "${getString(R.string.exit_dialog_text_statistics)} $errors"
        )
        exitDialog?.visibility = View.VISIBLE



        val cardsQuantity = appStorage.getInt("cardsQuantity", 12)
        val openCards = appStorage.getStringSet("openCards", emptySet<String>())
        val errors = appStorage.getInt("errors", 0)
        if (cardsQuantity == openCards?.size) {
            Log.i("memo", "game success")


            val db: GameDatabase = databaseBuilder(
                applicationContext,
                GameDatabase::class.java, "database_17"
            ).allowMainThreadQueries().build()

            val requests = db.requests()

            val sdf = SimpleDateFormat("yyyy.MM.dd, HH:mm", Locale.getDefault())
            val currentTimeString = sdf.format(Date())

            val lastGameId = requests.lastGameId
            val gameEntity = GameEntity(lastGameId + 1, currentTimeString, cardsQuantity, errors)
            requests.insertWithLimit(gameEntity)
        } else {
            Log.i("memo", "game not end")
        }
    }

    fun showSnackBar() {
        val snackBar = Snackbar.make(snackBarContainer, getString(R.string.add_card_to_favorites_text), Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        val params = snackBarView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT // Или фиксированная ширина, например, 800
        params.setMargins(20, 0, 20, 50) // Отступы: слева, сверху, справа, снизу
        snackBarView.layoutParams = params

        snackBar.setTextColor(Color.argb(255, 255, 255, 95))
        snackBar.setBackgroundTint(Color.argb(255, 119, 119, 119))
        snackBar.show()
    }
}