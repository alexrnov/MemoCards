package alexrnov.memocards.activities

import alexrnov.memocards.Initialization.appStorage
import alexrnov.memocards.R
import alexrnov.memocards.cards.CardsSettings
import alexrnov.memocards.render.game.GameSurfaceView
import alexrnov.memocards.statistics.GameDatabase
import alexrnov.memocards.statistics.GameEntity
import android.app.ActivityManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.room.Room.databaseBuilder
import java.text.SimpleDateFormat

import java.util.Date
import java.util.Locale

class GameActivity : AppCompatActivity() {
    private var gameSurfaceView: GameSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        val db: GameDatabase = databaseBuilder(
            applicationContext,
            GameDatabase::class.java, "database_17"
        ).allowMainThreadQueries().build()


        val requests = db.requests()
        Log.i("memo", "size in activity = " + requests.all.size)


       // val gameEntity = GameEntity(0, "2026.01.07 12:04", 12, 5)
       // requests.insert(gameEntity)
        Log.i("memo", "size after = " + requests.all.size)
        requests.all.forEach {
            Log.i("memo", "${it.id}, ${it.date}, ${it.cardsQuantity}, ${it.errors}")
        }

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

        setContentView(R.layout.activity_game)

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
        gameSurfaceView?.init(applicationContext, cardsSettings)
        gameSurfaceView?.setGameActivity(this)

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
        // todo перенести в диалоговое окно при выходе
        Log.i("memo", "GameActivity onDestroy")
        val cardsQuantity = appStorage.getInt("cardsQuantity", 12)
        val openCards = appStorage.getStringSet("openCards", emptySet<String>())
        val errors = appStorage.getInt("errors", 0)
        if (cardsQuantity == openCards?.size) {
            Log.i("memo", "game success")
        } else {
            Log.i("memo", "game not end")
        }

        val sdf = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
        val currentTimeString = sdf.format(Date())

        Log.i("memo", "currentTimeString = $currentTimeString, $cardsQuantity, $errors")
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

    @Synchronized
    public fun fullscreen() {

    }
}