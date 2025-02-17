package areality.game;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;


import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private PhotoCube cube;     // (NEW)
    private static float angleCube = 0;     // rotational angle in degree for cube
    private static float speedCube = 0.5f; // rotational speed for cube
    private float xRot = 0.15f;
    private float yRot = 1.0f;

    public MyGLRenderer(Context context, String[] photos) {
        cube = new PhotoCube(context, photos);    // (NEW)
    }

    // Call back when the surface is first created or re-created.
    @Override
    public void onSurfaceCreated(GL10 gl,javax.microedition.khronos.egl.EGLConfig config) {
        gl.glClearColor(225.0f, 225.0f, 225.0f, .1f);  // Set color's clear-value to white
        gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
        gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
        gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
        gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
        gl.glDisable(GL10.GL_DITHER);      // Disable dithering for better performance

        // Setup Texture, each time the surface is created (NEW)
        cube.loadTexture(gl);             // Load images into textures (NEW)
        gl.glEnable(GL10.GL_TEXTURE_2D);  // Enable texture (NEW)
    }



    // Call back after onSurfaceCreated() or whenever the window's size changes
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0) height = 1;   // To prevent divide by zero
        float aspect = (float)width / height;

        // Set the viewport (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
        gl.glLoadIdentity();                 // Reset projection matrix
        // Use perspective projection
        GLU.gluPerspective(gl, 45, aspect, 0.1f, 100.f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
        gl.glLoadIdentity();                 // Reset


    }

    // Call back to draw the current frame.
    @Override
    public void onDrawFrame(GL10 gl) {
        // Clear color and depth buffers
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        //Render the Cube
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -6.0f);
        gl.glRotatef(angleCube, xRot, yRot, 0.3f);
        cube.draw(gl);
        if(speedCube > 0.5f){
            speedCube -= 0.5f;
        }else if (speedCube > 15f){
            speedCube = 15f;
        }else if (speedCube < 0){
            speedCube *= -1;
        }else{
            speedCube = 0.5f;
        }
        // Update the rotational angle after each refresh.
        angleCube += speedCube;
    }

    public void setAngle(float newAngle){
        angleCube =newAngle;
    }

    public float getAngle(){
        return angleCube;
    }

    public void setSpeedCube(float speed){
        speedCube = speed;
    }

    public void setRot(float x, float y){
        xRot = x;
        yRot = y;
        setSpeedCube((x+y)*10);
    }
}

