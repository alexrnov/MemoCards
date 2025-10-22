package alexrnov.memocards.activities

import alexrnov.memocards.Initialization.appStorage
import alexrnov.memocards.R
import alexrnov.memocards.cards.CardsSettings
import alexrnov.memocards.render.SurfaceView
import android.app.ActivityManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

class GameActivity : AppCompatActivity() {
    private var surfaceView: SurfaceView? = null

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


        //Log.i("memo", "frontCardsSize = $frontCardsSize, backCardsNumber = $backCardsSize")

        Log.i("memo", "GameActivity onCreate")
        val cardsSettings = CardsSettings(
            frontCardsSize = frontCardsSize,
            backCardsSize = backCardsSize,
            material = material,
            cardsQuantity = appStorage.getInt("cardsQuantity", 12)
        )

        setContentView(R.layout.activity_gl)

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
            }
        } else {
            Log.i("memo", "not set params")
        }
        surfaceView = findViewById(R.id.oglView)
        surfaceView?.init(applicationContext, cardsSettings)
        surfaceView?.setGameActivity(this)

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

    @Synchronized
    public fun fullscreen() {

    }
}