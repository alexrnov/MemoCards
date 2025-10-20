package alexrnov.memocards;

import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import alexrnov.enginegl.commonGL.PositionUtils;

public class CustomGestureDetector implements android.view.GestureDetector.OnGestureListener {
    private PositionUtils positionUtils = new PositionUtils();
    private SceneRenderer renderer;

    public CustomGestureDetector(SceneRenderer sceneRenderer) {
        renderer = sceneRenderer;
    }

    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        renderer.openCard(e.getX(), e.getY());
        return true;
    }

    @Override
    public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        renderer.cameraPosition(distanceY); // camera rotation
        return true;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
        return false;
    }
}
