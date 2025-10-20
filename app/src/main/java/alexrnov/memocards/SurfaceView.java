package alexrnov.memocards;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class SurfaceView extends GLSurfaceView {
    SceneRenderer renderer;
    private GestureDetector detector;

    public SurfaceView(Context context) {
        super(context);
    }
    public SurfaceView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    public void init(int versionGLES, Context context) {
        setPreserveEGLContextOnPause(true); // save context OpenGL
        // Tell the OGLView container that we want to create an OpenGL ES 2.0/3.0
        // compatible context and install an OpenGL ES 2.0/3.0 compatible render
        setEGLContextClientVersion(versionGLES);
        renderer = new SceneRenderer(versionGLES, context);
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

    public SceneRenderer getSceneRenderer() {
        return renderer;
    }
}