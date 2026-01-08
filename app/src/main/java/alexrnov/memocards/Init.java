package alexrnov.memocards;

import android.app.Application;
import android.os.AsyncTask;

public class Init extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		AsyncTask.execute(() -> {

		});
	}
}
