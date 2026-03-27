package alexrnov.memocards;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import alexrnov.memocards.database.favorites.FavoritesDatabase;
import alexrnov.memocards.database.favorites.FavoritesRequests;
import alexrnov.memocards.database.statistics.GameDatabase;
import alexrnov.memocards.database.statistics.GameRequests;

public class Initialization extends Application {
	public static SharedPreferences appStorage;

	@Override
	public void onCreate() {
		super.onCreate();

		final String packageName = this.getApplicationContext().getPackageName();
		appStorage = this.getSharedPreferences(packageName, MODE_PRIVATE);
		Log.i("memo", "init sp");

		AsyncTask.execute(() -> {
			GameDatabase db = Room.databaseBuilder(this.getApplicationContext(), GameDatabase.class, "database_17").addCallback(dbCallbackStatistics).build();
			GameRequests dao = db.requests();
			int size = dao.getAll().size(); // фактически база будет создана при этой инструкции
		});

		AsyncTask.execute(() -> {
			FavoritesDatabase db = Room.databaseBuilder(this.getApplicationContext(), FavoritesDatabase.class, "database_18").addCallback(dbCallbackFavorites).build();
			FavoritesRequests dao = db.requests();
			int size = dao.getAll().size(); // фактически база будет создана при этой инструкции
		});
	}

	RoomDatabase.Callback dbCallbackStatistics = new RoomDatabase.Callback() {
		/** метод вызывается при создании базы данных */
		public void onCreate(@NonNull SupportSQLiteDatabase db) {
			try (ScheduledExecutorService ignored = Executors.newSingleThreadScheduledExecutor()) {
				Log.i("memo", "CREATE DATABASE STATISTICS");
			} catch (Exception e) {
				Log.i("memo", Objects.requireNonNull(e.getMessage()));
			}
		}

	};

	RoomDatabase.Callback dbCallbackFavorites = new RoomDatabase.Callback() {
		/** метод вызывается при создании базы данных */
		public void onCreate(@NonNull SupportSQLiteDatabase db) {
			try (ScheduledExecutorService ignored = Executors.newSingleThreadScheduledExecutor()) {
				Log.i("memo", "CREATE DATABASE FAVORITES");
			} catch (Exception e) {
				Log.i("memo", Objects.requireNonNull(e.getMessage()));
			}
		}

	};

}
