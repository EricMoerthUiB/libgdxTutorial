package inf112.skeleton.app;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * RoboRally sprites by http://www.yeoldewebsiteknight.co.uk/roborally
 */
public class Tiled extends InputAdapter implements ApplicationListener{

    // player properties
    private Sprite sprite;
    private SpriteBatch spriteBatch;
    private Texture playerTexture;
    private Vector2 playerPosition = new Vector2();

    // map properties
    private TiledMap map;
    private final int MAP_SIZE_X = 5;
    private final int MAP_SIZE_Y = 5;

    // layer
    private TiledMapTileLayer boardLayer;
    private TiledMapTileLayer collidableLayer;
    private TiledMapTileLayer holeLayer;
    private TiledMapTileLayer flagLayer;


    // camera properties
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    // win-lose messages
    private BitmapFont infoFont;

    @Override
    public void create() {

        // register input processor
        Gdx.input.setInputProcessor(this);

        // load the map including the layers
        map = new TmxMapLoader().load("assets/example.tmx");
        boardLayer = (TiledMapTileLayer) map.getLayers().get("Board");
        collidableLayer = (TiledMapTileLayer) map.getLayers().get("Collidable");
        holeLayer = (TiledMapTileLayer) map.getLayers().get("Hole");
        flagLayer = (TiledMapTileLayer) map.getLayers().get("Flag");

        // load player and position it in the lower left corner
        playerTexture = new Texture("uib.png");
        playerPosition.set(0, 0);
        spriteBatch = new SpriteBatch();
        sprite = new Sprite(playerTexture);
        sprite.setScale(1 / 3f);

        // create an orthographic camera, shows us 5x5 units of the world
        camera = new OrthographicCamera();
        camera.setToOrtho(false, MAP_SIZE_X, MAP_SIZE_Y);
        camera.position.x = 2.5f;
        camera.update();

        // set the unit scale to 1/300f (1 unit == 300 pixels)
        renderer = new OrthogonalTiledMapRenderer(map, 1 / 300f);
        renderer.setView(camera);

        // define win-lose messages
        infoFont = new BitmapFont();
        infoFont.setColor(new Color(1, 1, 0, 1)); // RGBA
    }

    @Override
    public void dispose() {
    }

    @Override
    public void render() {
        // clear the screen
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        // compute ratios
        float xAspectRatio = Gdx.graphics.getWidth()/(float)MAP_SIZE_X;
        float yAspectRatio = Gdx.graphics.getHeight()/(float)MAP_SIZE_Y;

        // render map
        renderer.render();

        // update current player position
        sprite.setPosition((playerPosition.x-1)*xAspectRatio, (playerPosition.y-1)*yAspectRatio);

        // render player sprite
        spriteBatch.begin();
        sprite.draw(spriteBatch);
        spriteBatch.end();

        // check what field the player is standing on
        if(holeLayer.getCell((int)playerPosition.x, (int)playerPosition.y) != null){
            spriteBatch.begin();
            infoFont.draw(spriteBatch, "LOST!", (playerPosition.x + 0.3f)*xAspectRatio, (playerPosition.y + 0.3f)*yAspectRatio);
            spriteBatch.end();
        }

        if(flagLayer.getCell((int)playerPosition.x, (int)playerPosition.y) != null){
            spriteBatch.begin();
            infoFont.draw(spriteBatch, "WON!", (playerPosition.x + 0.3f)*xAspectRatio, (playerPosition.y + 0.3f)*yAspectRatio);
            spriteBatch.end();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public boolean keyUp(int keycode) {

        if(keycode == Input.Keys.LEFT) {

            if(playerPosition.x > 0)
                playerPosition.x -= 1;
        }
        if(keycode == Input.Keys.RIGHT) {

            if(playerPosition.x < MAP_SIZE_X-1)
                playerPosition.x += 1;
        }
        if(keycode == Input.Keys.UP) {

            if(playerPosition.y < MAP_SIZE_Y-1)
                playerPosition.y += 1;
        }
        if(keycode == Input.Keys.DOWN) {

            if(playerPosition.y > 0)
                playerPosition.y -= 1;
        }

        return false;
    }
}
