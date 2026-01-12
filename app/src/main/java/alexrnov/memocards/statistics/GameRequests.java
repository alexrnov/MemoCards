package alexrnov.memocards.statistics;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public interface GameRequests {
	@Query("SELECT COUNT(*) FROM GameEntity")
	int getCountGames();

	@Query("SELECT * FROM GameEntity")
	List<GameEntity> getAll();

	@Query("SELECT id FROM GameEntity ORDER BY id DESC LIMIT 1")
	long getLastUserId();

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(GameEntity gameEntity);

	@Query("DELETE FROM GameEntity")
	void deleteAllEntities();

	@Transaction
	default void insertWithLimit(GameEntity gameEntity) {
		insert(gameEntity);
		deleteOldest(); // оставляет только последние n-записей
	}

	// вставить в таблицу новую запись и если превышено количество записей, то удалить самую старую
	@Query("DELETE FROM GameEntity WHERE id NOT IN (SELECT id FROM GameEntity ORDER BY id DESC LIMIT 1000)")
	void deleteOldest();
}
