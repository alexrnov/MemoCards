package alexrnov.memocards.database.statistics;

import androidx.room.Database;
import androidx.room.RoomDatabase;

// exportSchema = false чтобы не возникала ошибка при сборке apk
@Database(entities = {GameEntity.class}, version = 1, exportSchema = false)
public abstract class GameDatabase extends RoomDatabase {
	public abstract GameRequests requests();
}
