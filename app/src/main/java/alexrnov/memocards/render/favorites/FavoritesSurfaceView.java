package alexrnov.memocards.render.favorites;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import alexrnov.memocards.view.activity.FavoritesActivity;
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


	public void init(Context context) {
		setPreserveEGLContextOnPause(true); // save context OpenGL
		setEGLContextClientVersion(3);
		renderer = new FavoritesRenderer(context);
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
