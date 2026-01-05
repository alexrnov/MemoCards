package alexrnov.memocards.activities

import alexrnov.memocards.Initialization.appStorage
import alexrnov.memocards.R
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.edit

class SettingsActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

	private lateinit var woodRadioButton: RadioButton
	private lateinit var stoneRadioButton: RadioButton
	private lateinit var plasticRadioButton: RadioButton
	private lateinit var patternRadioButton: RadioButton
	private lateinit var materialRadioGroup: RadioGroup

	private lateinit var cardsQuantityRadioGroup: RadioGroup
	private lateinit var lowCardsRadioButton: RadioButton
	private lateinit var mediumCardsRadioButton: RadioButton
	private lateinit var manyCardsRadioButton: RadioButton
	private lateinit var maxCardsRadioButton: RadioButton

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		enableEdgeToEdge()
		setContentView(R.layout.activity_settings)

		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}

		woodRadioButton = findViewById(R.id.woodRadioButton)
		stoneRadioButton = findViewById(R.id.stoneRadioButton)
		plasticRadioButton = findViewById(R.id.plasticRadioButton)
		patternRadioButton = findViewById(R.id.patternRadioButton)
		materialRadioGroup = findViewById(R.id.materialRadioGroup)


		cardsQuantityRadioGroup = findViewById(R.id.cardsQuantityRadioGroup)
		lowCardsRadioButton = findViewById(R.id.lowCardsRadioButton)
		mediumCardsRadioButton = findViewById(R.id.mediumCardsRadioButton)
		manyCardsRadioButton = findViewById(R.id.manyCardsRadioButton)
		maxCardsRadioButton = findViewById(R.id.maxCardsRadioButton)


		defineMaterialRadioButtons()
		defineCardsQuantityRadioButtons()
		addListeners()
	}

	private fun addListeners() {
		materialRadioGroup.setOnCheckedChangeListener { group, checkedId ->
			val material = when (checkedId) {
				woodRadioButton.id -> "wood"
				stoneRadioButton.id -> "stone"
				plasticRadioButton.id -> "plastic"
				else -> "pattern"
			}
			saveToStorage("material", material)
		}

		cardsQuantityRadioGroup.setOnCheckedChangeListener { group, checkedId ->
			val cardsQuantity = when (checkedId) {
				lowCardsRadioButton.id -> 12
				mediumCardsRadioButton.id -> 16
				manyCardsRadioButton.id -> 20
				maxCardsRadioButton.id -> 30
				else -> 12
			}
			Log.i("memo", "cardsQuantity = $cardsQuantity")
			appStorage.edit {
				putInt("cardsQuantity", cardsQuantity)
			}
		}
	}

	private fun saveToStorage(key: String, value: String) {
		appStorage.edit {
			putString(key, value)
		}
	}

	private fun defineMaterialRadioButtons() {
		val currentValue = appStorage.getString("material", "wood")
		when (currentValue) {
			"wood" -> materialRadioGroup.check(woodRadioButton.id)
			"stone" -> materialRadioGroup.check(stoneRadioButton.id)
			"plastic" -> materialRadioGroup.check(plasticRadioButton.id)
			"pattern" -> materialRadioGroup.check(patternRadioButton.id)
		}
	}

	private fun  defineCardsQuantityRadioButtons() {
		val currentValue = appStorage.getInt("cardsQuantity", 12)
		when (currentValue) {
			12 -> cardsQuantityRadioGroup.check(lowCardsRadioButton.id)
			16 -> cardsQuantityRadioGroup.check(mediumCardsRadioButton.id)
			20 -> cardsQuantityRadioGroup.check(manyCardsRadioButton.id)
			30 -> cardsQuantityRadioGroup.check(maxCardsRadioButton.id)
		}
	}

	override fun onCheckedChanged(
		buttonView: CompoundButton,
		isChecked: Boolean
	) {
		Log.i("memo", "change")
	}
}