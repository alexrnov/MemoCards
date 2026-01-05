package alexrnov.memocards.statistics;

import java.util.List;

import androidx.room.Dao;
//import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface GameRequests {
	@Query("SELECT * FROM GameEntity")
	List<GameEntity> getAll();

	@Insert
	void insert(GameEntity gameEntity);
}
