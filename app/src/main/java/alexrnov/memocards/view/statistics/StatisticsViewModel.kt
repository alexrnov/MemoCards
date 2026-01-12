package alexrnov.memocards.view.statistics

import alexrnov.memocards.statistics.GameDatabase
import alexrnov.memocards.statistics.GameEntity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {
	private val sortByDate = MutableLiveData<Boolean>().apply { false }
	private val sortByCards = MutableLiveData<Boolean>().apply { false }
	private val sortByErrors = MutableLiveData<Boolean>().apply { false }

	private val _texts = MutableLiveData<List<GameEntity>>().apply {

		val db: GameDatabase = Room.databaseBuilder(
			application.applicationContext,
			GameDatabase::class.java, "database_17"
		).allowMainThreadQueries().build()

		val requests = db.requests()
		value = requests.all


		/*
		// тест большого количества записей
		val testList = mutableListOf<GameEntity>()
		(0..15).forEachIndexed { index, it ->
			testList.add(GameEntity(index.toLong(), "data", 1, index))
		}
		value = testList
		*/
	}

	var texts: LiveData<List<GameEntity>> = _texts

	fun clearData() {
		_texts.value = null
	}


	fun sortByDate() {
		sortByCards.value = false
		sortByErrors.value = false

		if (sortByDate.value == true) {
			_texts.value = _texts.value?.reversed()
		} else {
			_texts.value = _texts.value?.sortedByDescending {
				val formatter = SimpleDateFormat("yyyy.MM.dd, HH:mm", Locale.getDefault())
				val date: Date? = formatter.parse(it.date)
				date
			}
			sortByDate.value = true
		}
	}

	fun sortByCards() {
		sortByDate.value = false
		sortByErrors.value = false

		if (sortByCards.value == true) {
			_texts.value = _texts.value?.reversed()
		} else {
			_texts.value = _texts.value?.sortedBy { it.cardsQuantity }
			sortByCards.value = true
		}
	}

	fun sortByErrors() {
		sortByDate.value = false
		sortByCards.value = false

		if (sortByErrors.value == true) {
			_texts.value = _texts.value?.reversed()
		} else {
			_texts.value = _texts.value?.sortedBy { it.errors }
			sortByErrors.value = true
		}
	}
}