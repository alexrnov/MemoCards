package alexrnov.memocards.database.favorites;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface FavoritesRequests {
	@Query("SELECT COUNT(*) FROM FavoriteEntity")
	int getCountFavorites();

	@Query("SELECT * FROM FavoriteEntity")
	List<FavoriteEntity> getAll();

	@Query("SELECT id FROM FavoriteEntity ORDER BY id DESC LIMIT 1")
	long getLastCardId();

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(FavoriteEntity favoriteEntity);

	@Query("SELECT EXISTS(SELECT 1 FROM FavoriteEntity WHERE path = :path LIMIT 1)")
	boolean isPathExists(String path);

	@Transaction
	default void insertWithLimit(FavoriteEntity favoriteEntity) {
		insert(favoriteEntity);
		deleteOldest(); // оставляет только последние n-записей
	}

	@Query("DELETE FROM FavoriteEntity WHERE path = :path")
	void deleteByPath(String path);

	// вставить в таблицу новую запись и если превышено количество записей, то удалить самую старую
	@Query("DELETE FROM FavoriteEntity WHERE id NOT IN (SELECT id FROM FavoriteEntity ORDER BY id DESC LIMIT 10)")
	void deleteOldest();
}
