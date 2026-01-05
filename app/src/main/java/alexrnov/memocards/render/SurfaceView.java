package alexrnov.memocards.render;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import alexrnov.memocards.activities.GameActivity;
import alexrnov.memocards.cards.CardsSettings;

public class SurfaceView extends GLSurfaceView {
    SceneRenderer renderer;
    private GestureDetector detector;

    public SurfaceView(Context context) {
        super(context);
    }
    public SurfaceView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    public void init(Context context, CardsSettings cardsSettings) {
        setPreserveEGLContextOnPause(true); // save context OpenGL
        setEGLContextClientVersion(3);
        Log.i("memo", "init SurfaceView");
        renderer = new SceneRenderer(context, cardsSettings);
        setRenderer(renderer);
        detector = new GestureDetector(context, new CustomGestureDetector(renderer));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (detector.onTouchEvent(e)) {
            return true;
        }
        return super.onTouchEvent(e);
    }

    public void setGameActivity(GameActivity gameActivity) {
        renderer.setGameActivity(gameActivity);
    }
}