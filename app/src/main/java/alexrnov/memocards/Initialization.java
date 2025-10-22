package alexrnov.memocards;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
public class Initialization extends Application {
	public static SharedPreferences appStorage;

	@Override
	public void onCreate() {
		super.onCreate();

		final String packageName = this.getApplicationContext().getPackageName();
		appStorage = this.getSharedPreferences(packageName, MODE_PRIVATE);
		Log.i("memo", "init sp");
	}
}
