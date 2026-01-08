package alexrnov.memocards.render.favorites;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import alexrnov.memocards.activities.FavoritesActivity;
import alexrnov.memocards.activities.GameActivity;
import alexrnov.memocards.cards.CardsSettings;

public class FavoritesSurfaceView extends GLSurfaceView {
	FavoritesRenderer renderer;
	private GestureDetector detector;


	public FavoritesSurfaceView(Context context) {
		super(context);
	}
	public FavoritesSurfaceView(Context context, AttributeSet attributes) {
		super(context, attributes);
	}


	public void init(Context context, CardsSettings cardsSettings) {
		setPreserveEGLContextOnPause(true); // save context OpenGL
		setEGLContextClientVersion(3);
		Log.i("memo", "init SurfaceView");
		renderer = new FavoritesRenderer(context, cardsSettings);
		setRenderer(renderer);
		detector = new GestureDetector(context, new FavoritesDetector(renderer));
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if (detector.onTouchEvent(e)) {
			return true;
		}
		return super.onTouchEvent(e);
	}

	public void setGameActivity(FavoritesActivity gameActivity) {
		renderer.setGameActivity(gameActivity);
	}
}
