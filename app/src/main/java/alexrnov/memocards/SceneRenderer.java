package alexrnov.memocards;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static alexrnov.enginegl.Textures.loadTextureWithMipMapFromAsset;
import static alexrnov.enginegl.Textures.loadTextureWithMipMapFromRaw;

import alexrnov.enginegl.CardVertices;
import alexrnov.enginegl.MeanValue;
import alexrnov.enginegl.ObjectSize;
import alexrnov.memocards.enginegl.Object3D;


public class SceneRenderer implements GLSurfaceView.Renderer {

    private int versionGL; // support version of OpenGL ES
    private float ky = 0.30f; // coefficient for camera rotation

    private float[] viewMatrix = new float[16];
    private float[] projectionMatrix = new float[16];

    private boolean changeView = false; // this flag check if camera was moved

    private Context context;

    private float f = 0.0f;

    private float smoothedDeltaRealTime_ms = 16.0f; // initial value, Optionally you can save the new computed value (will change with each hardware) in Preferences to optimize the first drawing frames
    private float movAverageDeltaTime_ms = smoothedDeltaRealTime_ms; // mov Average start with default value
    private long lastRealTimeMeasurement_ms; // temporal storage for last time measurement
    // smooth constant elements to play with
    private static final float movAveragePeriod = 5; // #frames involved in average calc (suggested values 5-100)
    private static final float smoothFactor = 0.1f; // adjusting ratio (suggested values 0.01-0.5)


    private MeanValue meanValue = new MeanValue((short) 5000);

    private float totalVirtualRealTime_ms = 0;
    private float speedAdjustments_ms = 0; // to introduce a virtual Time for the animation (reduce or increase animation speed)
    private float totalAnimationTime_ms=0;
    private float fixedStepAnimation_ms = 20; // 20ms for a 50FPS descriptive animation
    private float interpolationRatio = 0;

    private float delta;

    private Object3D[] cards = new Object3D[24];

    private final float rotationCameraRadius = 2.2f;

    //private float zCamera = 5.3f;

    private float zCamera = 3.0f;
    private float scale = 1.0f;

    private boolean firstView = true;
    private int i = 0;

    int screenWidth;
    int screenHeight;

    public SceneRenderer(int versionGL, Context context) {
        this.versionGL = versionGL;
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.i("memo", "init onSurfaceCreated");
        int textureID = loadTextureWithMipMapFromRaw(context, R.raw.back_2);

        int front_5 = loadTextureWithMipMapFromAsset(context, "front/front_5.jpg");
        int front_10 = loadTextureWithMipMapFromAsset(context, "front/front_10.jpg");
        int front_11 = loadTextureWithMipMapFromAsset(context, "front/front_11.jpg");
        int front_12 = loadTextureWithMipMapFromAsset(context, "front/front_12.jpg");
        int front_13 = loadTextureWithMipMapFromAsset(context, "front/front_13.jpg");
        int front_14 = loadTextureWithMipMapFromAsset(context, "front/front_14.jpg");

        cards[0] = new Object3D(context, "objects/front.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", front_12, scale);
        cards[1] = new Object3D(context, "objects/back.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", textureID, scale);

        cards[2] = new Object3D(context, "objects/front.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", front_14, scale);
        cards[3] = new Object3D(context, "objects/back.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", textureID, scale);

        cards[4] = new Object3D(context, "objects/front.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", front_12, scale);
        cards[5] = new Object3D(context, "objects/back.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", textureID, scale);

        cards[6] = new Object3D(context, "objects/front.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", front_14, scale);
        cards[7] = new Object3D(context, "objects/back.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", textureID, scale);

        cards[8] = new Object3D(context, "objects/front.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", front_10, scale);
        cards[9] = new Object3D(context, "objects/back.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", textureID, scale);

        cards[10] = new Object3D(context, "objects/front.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", front_11, scale);
        cards[11] = new Object3D(context, "objects/back.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", textureID, scale);

        cards[12] = new Object3D(context, "objects/front.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", front_13, scale);
        cards[13] = new Object3D(context, "objects/back.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", textureID, scale);

        cards[14] = new Object3D(context, "objects/front.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", front_5, scale);
        cards[15] = new Object3D(context, "objects/back.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", textureID, scale);

        cards[16] = new Object3D(context, "objects/front.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", front_10, scale);
        cards[17] = new Object3D(context, "objects/back.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", textureID, scale);


        cards[18] = new Object3D(context, "objects/front.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", front_5, scale);
        cards[19] = new Object3D(context, "objects/back.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", textureID, scale);

        cards[20] = new Object3D(context, "objects/front.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", front_11, scale);
        cards[21] = new Object3D(context, "objects/back.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", textureID, scale);

        cards[22] = new Object3D(context, "objects/front.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", front_13, scale);
        cards[23] = new Object3D(context, "objects/back.obj", "shaders/card_v.glsl", "shaders/card_f.glsl", textureID, scale);

        cameraPosition(-350.0f);

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // implementation prioritizes performance
        GLES20.glHint(GLES20.GL_GENERATE_MIPMAP_HINT, GLES20.GL_FASTEST);

        for (Object3D card: cards) {
            card.setRotate(true);
        }
    }

    // screen orientation change handler, also called when returning to the app
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height); // set screen size
        this.screenWidth = width;
        this.screenHeight = height;

        float aspect = (float) width / (float) height;
        float k = 1f / 30; // coefficient is selected empirically

        if (width < height) { // portrait orientation
            Matrix.frustumM(projectionMatrix, 0, -1f * k, 1f * k,
                    (1/-aspect) * k, (1/aspect) * k, 0.1f, 40f);
        } else { // landscape orientation
            Matrix.frustumM(projectionMatrix, 0, -aspect * k,
                    aspect * k, -1f * k, 1f * k, 0.1f, 40f);
        }


        for (Object3D card: cards) {

        }

        Log.i("memo", "width = " + width + ", height = " + height);

        if (width < height) {
            calibrateView(width, height);

            cards[0].position(-1.05f, 2.7f, 0.0f, 45.0f);
            cards[1].position(-1.05f, 2.7f, 0.0f, 45.0f);


            cards[2].position(0f, 2.7f, 0f, 45.0f);
            cards[3].position(0f, 2.7f, 0f, 45.0f);

            cards[4].position(1.05f, 2.7f, 0.0f, 45.0f);
            cards[5].position(1.05f, 2.7f, 0.0f, 45.0f);


            cards[6].position(-1.05f, 0.9f, 0.0f, 45.0f);
            cards[7].position(-1.05f, 0.9f, 0.0f, 45.0f);

            cards[8].position(0f, 0.9f, 0f, 45.0f);
            cards[9].position(0f, 0.9f, 0f, 45.0f);

            cards[10].position(1.05f, 0.9f, 0.0f, 45.0f);
            cards[11].position(1.05f, 0.9f, 0.0f, 45.0f);


            cards[12].position(-1.05f, -0.9f, 0.0f, 45.0f);
            cards[13].position(-1.05f, -0.9f, 0.0f, 45.0f);

            cards[14].position(0f, -0.9f, 0.0f, 45.0f);
            cards[15].position(0f, -0.9f, 0.0f, 45.0f);

            cards[16].position(1.05f, -0.9f, 0.0f, 45.0f);
            cards[17].position(1.05f, -0.9f, 0.0f, 45.0f);


            cards[18].position(-1.05f, -2.7f, 0.0f, 45.0f);
            cards[19].position(-1.05f, -2.7f, 0.0f, 45.0f);

            cards[20].position(0f, -2.7f, 0f, 45.0f);
            cards[21].position(0f, -2.7f, 0f, 45.0f);

            cards[22].position(1.05f, -2.7f, 0.0f, 45.0f);
            cards[23].position(1.05f, -2.7f, 0.0f, 45.0f);

/*
            cards[1].defineView(viewMatrix, projectionMatrix);

            cards[2].defineView(viewMatrix, projectionMatrix);
            cards[3].defineView(viewMatrix, projectionMatrix);

            cards[4].defineView(viewMatrix, projectionMatrix);
            cards[5].defineView(viewMatrix, projectionMatrix);


            cards[6].defineView(viewMatrix, projectionMatrix);
            cards[7].defineView(viewMatrix, projectionMatrix);

            cards[8].defineView(viewMatrix, projectionMatrix);
            cards[9].defineView(viewMatrix, projectionMatrix);

            cards[10].defineView(viewMatrix, projectionMatrix);
            cards[11].defineView(viewMatrix, projectionMatrix);


            cards[12].defineView(viewMatrix, projectionMatrix);
            cards[13].defineView(viewMatrix, projectionMatrix);

            cards[14].defineView(viewMatrix, projectionMatrix);
            cards[15].defineView(viewMatrix, projectionMatrix);

            cards[16].defineView(viewMatrix, projectionMatrix);
            cards[17].defineView(viewMatrix, projectionMatrix);


            cards[18].defineView(viewMatrix, projectionMatrix);
            cards[19].defineView(viewMatrix, projectionMatrix);

            cards[20].defineView(viewMatrix, projectionMatrix);
            cards[21].defineView(viewMatrix, projectionMatrix);

            cards[22].defineView(viewMatrix, projectionMatrix);
            cards[23].defineView(viewMatrix, projectionMatrix);

 */
        } else {
            calibrateViewLandscape(width, height);
            cards[0].position(-2.65f, 0.92f, 0.0f, 45.0f);
            cards[1].position(-2.65f, 0.92f, 0.0f, 45.0f);

            cards[2].position(-1.59f, 0.92f, 0f, 45.0f);
            cards[3].position(-1.59f, 0.92f, 0f, 45.0f);

            cards[4].position(-0.53f, 0.92f, 0.0f, 45.0f);
            cards[5].position(-0.53f, 0.92f, 0.0f, 45.0f);


            cards[6].position(0.53f, 0.92f, 0.0f, 45.0f);
            cards[7].position(0.53f, 0.92f, 0.0f, 45.0f);

            cards[8].position(1.59f, 0.92f, 0f, 45.0f);
            cards[9].position(1.59f, 0.92f, 0f, 45.0f);

            cards[10].position(2.65f, 0.92f, 0.0f, 45.0f);
            cards[11].position(2.65f, 0.92f, 0.0f, 45.0f);



            cards[12].position(-2.65f, -0.92f, 0.0f, 45.0f);
            cards[13].position(-2.65f, -0.92f, 0.0f, 45.0f);

            cards[14].position(-1.59f, -0.92f, 0f, 45.0f);
            cards[15].position(-1.59f, -0.92f, 0f, 45.0f);

            cards[16].position(-0.53f, -0.92f, 0.0f, 45.0f);
            cards[17].position(-0.53f, -0.92f, 0.0f, 45.0f);


            cards[18].position(0.53f, -0.92f, 0.0f, 45.0f);
            cards[19].position(0.53f, -0.92f, 0.0f, 45.0f);

            cards[20].position(1.59f, -0.92f, 0f, 45.0f);
            cards[21].position(1.59f, -0.92f, 0f, 45.0f);

            cards[22].position(2.65f, -0.92f, 0.0f, 45.0f);
            cards[23].position(2.65f, -0.92f, 0.0f, 45.0f);

        }
        Log.i("memo", "zCamera after = " + zCamera);

    }

    // called when the frame is redrawn
    @Override
    public void onDrawFrame(GL10 gl) {
        delta = meanValue.add(interpolationRatio);
        //f = f + 1.1f;
        f = f + delta * 2;
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST); // enable depth test
        if (firstView) {
            i = i + 1;
            if (i > 250) {
                for (Object3D card: cards) {
                    card.setRotate(true);
                }
                firstView = false;
            }
        }


        Log.i("memo", "i = " + i);



        for (Object3D card: cards) {
            if (card.isRotate()) {
                card.rotate(delta, 0.0f, -0.5f, 0.0f);
            }
            card.defineView(viewMatrix, projectionMatrix);
            card.draw();
        }

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        defineDeltaTime();
    }

    public synchronized void cameraPosition(float yDistance) {
        if ((!(ky < -0.5) || !(yDistance < 0.0)) && (!(ky > 0.5) || !(yDistance >= 0.0))) {
            ky = ky + yDistance * 0.001f;
        }

        float yCamera = (float) (rotationCameraRadius * Math.sin(ky));

        Matrix.setLookAtM(viewMatrix, 0, 0.0f, -yCamera, zCamera,
        0f, 0.0f, 0f, 0f, 1.0f, 0.0f);
    }

    public synchronized void openCard(float x, float y) {
        if (firstView) return;
        //int width = 1440;
        //int height = 3040;
        Log.i("memo", "width = " + screenWidth + ", height = " + screenHeight);
        Log.i("memo", "x = " + x + ", y = " + (screenHeight - y));

        float xPass = x;
        float yPass = screenHeight - y;


        Log.i("memo", "xPass = " + xPass + ", yPass = " + yPass);
        Log.i("memo", "-------------");
        for (Object3D card: cards) {
            CardVertices vertices = card.getVertices(projectionMatrix, screenWidth, screenHeight, scale, 0.500000f, 0.888800f, 0.001000f);
            float xMin;
            float xMax;
            float yMin = vertices.component3();
            float yMax = vertices.component4();
            if (!card.isFront()) {
                xMin = vertices.component1();
                xMax = vertices.component2();
            } else {
                xMin = vertices.component2();
                xMax = vertices.component1();
            }
            //Log.i("memo", "xMin = " + xMin + ", xMax = " + xMax + ", yMin = " + yMin + "yMax = " + yMax);
            if (xPass >= xMin && xPass <= xMax && yPass >= yMin && yPass <= yMax && !card.isRotate()) {
                Log.i("memo", "press");
                card.setRotate(true);
            }
        }
    }

    /**
     * The method checks if all cubes are initialized.
     * @return <value>true</value> - if all cubes is init,
     * <value>else</value> - in another case
     */
    public synchronized boolean isLoad() {
        return false;
    }

    /**
     * Set current color for cube.
     * @param i - id of cube
     * @param color - current color of cube
     */
    public synchronized void setColor(int i, float[] color) {

    }


    private void defineDeltaTime() {
        totalVirtualRealTime_ms += smoothedDeltaRealTime_ms + speedAdjustments_ms;
        while (totalVirtualRealTime_ms > totalAnimationTime_ms) {
            totalAnimationTime_ms += fixedStepAnimation_ms;
        }

        interpolationRatio = (totalAnimationTime_ms - totalVirtualRealTime_ms)
                / fixedStepAnimation_ms;

        long currTimePick_ms = SystemClock.uptimeMillis();
        float realTimeElapsed_ms;
        if (lastRealTimeMeasurement_ms > 0) {
            realTimeElapsed_ms = (currTimePick_ms - lastRealTimeMeasurement_ms);
        } else {
            realTimeElapsed_ms = smoothedDeltaRealTime_ms; // just the first time
        }
        movAverageDeltaTime_ms = (realTimeElapsed_ms + movAverageDeltaTime_ms
                * (movAveragePeriod-1)) / movAveragePeriod;

        // Calc a better approximation for smooth stepTime
        smoothedDeltaRealTime_ms = smoothedDeltaRealTime_ms +
                (movAverageDeltaTime_ms - smoothedDeltaRealTime_ms) * smoothFactor;

        lastRealTimeMeasurement_ms = currTimePick_ms;
    }

    private void calibrateView(int screenWidth, int screenHeight) {
        Log.i("memo", "zCamera = " + zCamera);
        cards[0].position(0.0f, 0.0f, 0.0f, 45.0f);
        boolean isScale = false;
        float cardWidth;
        float cardHeight;
        float wk;
        float hk;

        int i = 0;
        while (!isScale) {
            i++;
            cards[0].defineView(viewMatrix, projectionMatrix);
            ObjectSize objectSize = cards[0].getSize(projectionMatrix, screenWidth, screenHeight, scale, 0.500000f, 0.888800f, 0.001000f);
            cardWidth = objectSize.component1();
            cardHeight = objectSize.component2();
            //Log.i("memo", "x = " + x + ", y = " + y);

            wk = screenWidth / cardWidth;
            hk = screenHeight / cardHeight;
            //Log.i("memo", "wk = " + wk + ", hk = " + hk);
            //Log.i("memo", "--------------");
            if (wk > 3 && hk > 4.5) {
                isScale = true;
            } else {
                zCamera = zCamera + 0.1f;
                cameraPosition(0f);
            }
        }
        Log.i("memo", "i = " + i);
    }

    private void calibrateViewLandscape(int screenWidth, int screenHeight) {
        Log.i("memo", "zCamera = " + zCamera);
        cards[0].position(0.0f, 0.0f, 0.0f, 45.0f);
        boolean isScale = false;
        float cardWidth;
        float cardHeight;
        float wk;
        float hk;

        int j = 0;
        while (!isScale) {
            j++;
            cards[0].defineView(viewMatrix, projectionMatrix);
            ObjectSize objectSize = cards[0].getSize(projectionMatrix, screenWidth, screenHeight, scale, 0.500000f, 0.888800f, 0.001000f);
            cardWidth = objectSize.component1();
            cardHeight = objectSize.component2();
            //Log.i("memo", "x = " + x + ", y = " + y);

            wk = screenWidth / cardWidth;
            hk = screenHeight / cardHeight;
            //Log.i("memo", "wk = " + wk + ", hk = " + hk);
            //Log.i("memo", "--------------");
            if (wk > 6.5 && hk > 2.2) {
                isScale = true;
            } else {
                zCamera = zCamera + 0.1f;
                cameraPosition(0f);
            }
        }
        Log.i("memo", "j = " + j);
    }
}