package it.uniroma1.lcl.crucy.GamePlay.Input.cameraController;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import it.uniroma1.lcl.crucy.Animation;

/**
 * Created by antho on 24/08/2016.
 */
public class CamController
{
    protected OrthographicCamera camera;

    protected static final float MOVE_VELOCITY = 5;
    protected static final int INTORNO_CLICK_PRECISION = 50;
    protected static final float INTORNO_ZOOM_PRECISION = 0.1f;
    protected static final float ZOOM_VELOCITY = 3;
    

    private boolean zoomTo;
    private float zoomPosition;

    private boolean move;

    private Vector3 clickCoords;

    private Vector2 previusPosition;

    public CamController(OrthographicCamera camera)
    {
        this.camera = camera;
    }

    /**
     * Move camera to x, y world coord.
     * @param x
     * @param y
     */
    public void moveCameraTo(float x, float y)
    {
        clickCoords = camera.unproject(new Vector3(x,y,0));
        move = true;
    }

    /**
     * Zoom camera at zoom specified.
     * @param zoom
     */
    public void zoomTo(float zoom)
    {
        this.zoomPosition = zoom;
        this.zoomTo = true;
    }

    /**
     * This method will be call in render method.
     */
    public void animationUpdate()
    {
        cameraTapMoveAnimation();
        updateZoom();
    }

    /**
     * Update Camera.
     */
    protected void cameraTapMoveAnimation()
    {
        // se ci troviamo in un intorno del punto e siamo in movimento fermo il movimento.
        if ((move && (camera.position.x < clickCoords.x + Gdx.graphics.getWidth()/INTORNO_CLICK_PRECISION
                && camera.position.x > clickCoords.x - Gdx.graphics.getWidth()/INTORNO_CLICK_PRECISION
                && camera.position.y < clickCoords.y + Gdx.graphics.getHeight()/INTORNO_CLICK_PRECISION
                && camera.position.y > clickCoords.y - Gdx.graphics.getHeight()/INTORNO_CLICK_PRECISION) ))
            move = false;
        // Se la variabile move è  a true dobbiamo muovere la telecamera nella posizione del tap
        if (move)
        {
            double distanceX = camera.position.x - clickCoords.x;
            double distanceY = camera.position.y - clickCoords.y;
            camera.translate( (float) -(MOVE_VELOCITY * distanceX / 2) * Gdx.graphics.getDeltaTime(),
                    (float) -(MOVE_VELOCITY * distanceY / 2) * Gdx.graphics.getDeltaTime() );
        }


    }

    protected void updateZoom()
    {
        if (camera.zoom > zoomPosition - INTORNO_ZOOM_PRECISION && zoomPosition > camera.zoom)
            zoomTo = false;
        if (camera.zoom < zoomPosition + INTORNO_ZOOM_PRECISION && zoomPosition < camera.zoom)
            zoomTo = false;
        if (zoomTo)
        {
            double distZoom = zoomPosition - camera.zoom;
            camera.zoom += Gdx.graphics.getDeltaTime() * ZOOM_VELOCITY * (distZoom);
        }
    }



    /**
     * Stop the camera movement
     */
    public void stopMoving()
    {
        move = false;
    }

    public void savePosition() { previusPosition = new Vector2(camera.position.x, camera.position.y); }

    public void comeBack() { moveCameraTo(previusPosition.x, previusPosition.y);}


}
