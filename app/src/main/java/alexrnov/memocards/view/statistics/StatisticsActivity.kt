package alexrnov.memocards.view.statistics

import alexrnov.memocards.R
import alexrnov.memocards.statistics.GameDatabase
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.google.android.material.appbar.MaterialToolbar

class StatisticsActivity : AppCompatActivity() {
	private lateinit var confirmClearDialog: ConstraintLayout
	private lateinit var clearButton: Button
	private lateinit var emptyTextView: TextView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_statistics)

		val toolbar = findViewById<MaterialToolbar>(R.id.statisticsAppBar)
		setSupportActionBar(toolbar)

		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.statisticsContainer)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}

		val db: GameDatabase = Room.databaseBuilder(
			application.applicationContext,
			GameDatabase::class.java, "database_17"
		).allowMainThreadQueries().build()

		val countGames = db.requests().countGames

		confirmClearDialog = findViewById(R.id.clearDialogBackground)
		clearButton = findViewById(R.id.clearStatisticsButton)
		emptyTextView = findViewById(R.id.emptyTextView)

		if (countGames < 1) {
			clearButton.isEnabled = false
			clearButton.backgroundTintList = ColorStateList.valueOf(Color.LTGRAY)
			emptyTextView.visibility = View.VISIBLE
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		outState.putBoolean("clearDialogVisibility", confirmClearDialog.visibility == View.VISIBLE)
		super.onSaveInstanceState(outState)
	}

	override fun onRestoreInstanceState(savedInstanceState: Bundle) {
		super.onRestoreInstanceState(savedInstanceState)
		val isVisible = savedInstanceState.getBoolean("clearDialogVisibility")
		confirmClearDialog.visibility = if (isVisible) View.VISIBLE else View.GONE
	}

	fun invokeConfirmDialog(view: View) {
		confirmClearDialog.visibility = View.VISIBLE
	}

	fun backToStatistics(view: View) {
		confirmClearDialog.visibility = View.GONE
	}

	fun clearStatistics(view: View) {
		val statisticsViewModel = ViewModelProvider(this).get(StatisticsViewModel::class.java)
		statisticsViewModel.clearData()

		clearButton.isEnabled = false
		clearButton.backgroundTintList = ColorStateList.valueOf(Color.LTGRAY)

		confirmClearDialog.visibility = View.GONE
		emptyTextView.visibility = View.VISIBLE

		val db: GameDatabase = Room.databaseBuilder(
			application.applicationContext,
			GameDatabase::class.java, "database_17"
		).allowMainThreadQueries().build()

		val requests = db.requests()
		requests.deleteAllEntities()
	}

	fun sortByDate(view: View) {
		val statisticsViewModel = ViewModelProvider(this).get(StatisticsViewModel::class.java)
		statisticsViewModel.sortByDate()
	}

	fun sortByCards(view: View) {
		val statisticsViewModel = ViewModelProvider(this).get(StatisticsViewModel::class.java)
		statisticsViewModel.sortByCards()
	}

	fun sortByErrors(view: View) {
		val statisticsViewModel = ViewModelProvider(this).get(StatisticsViewModel::class.java)
		statisticsViewModel.sortByErrors()
	}
}