/*
 *
 *  * *****************************************************************************
 *  *  Copyright 27/03/2017 See AUTHORS file.
 *  *  <p>
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *  <p>
 *  *  http://www.apache.org/licenses/LICENSE-2.0
 *  *  <p>
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *  * ***************************************************************************
 *
 *
 */

package com.red7projects.dungeon.graphics.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.config.Preferences;
import com.red7projects.dungeon.development.Developer;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.camera.OrthoGameCamera;
import com.red7projects.dungeon.graphics.camera.Zoom;
import com.red7projects.dungeon.screens.ScreenID;

public class BaseRenderer implements Disposable
{
    public OrthoGameCamera  hudGameCamera;
    public OrthoGameCamera  spriteGameCamera;
    public OrthoGameCamera  tiledGameCamera;
    public OrthoGameCamera  backgroundCamera;
    public Zoom             backgroundZoom;
    public Zoom             gameZoom;
    public Zoom             hudZoom;
    public boolean          isDrawingStage;

    private WorldRenderer   worldRenderer;
    private HUDRenderer     hudRenderer;

    private final App app;

    public BaseRenderer(App _app)
    {
        this.app = _app;

        createCameras();
    }

    /**
     * Create all game cameras and
     * associated viewports.
     */
    private void createCameras()
    {
        AppConfig.camerasReady = false;

        Gfx.setPPM(64.0f);  // TODO: 08/12/2019 - ????

        backgroundCamera    = new OrthoGameCamera(Gfx._SCENE_WIDTH, Gfx._SCENE_HEIGHT, "Background Cam", app);
        tiledGameCamera     = new OrthoGameCamera(Gfx._SCENE_WIDTH, Gfx._SCENE_HEIGHT, "Tiled Cam", app);
        spriteGameCamera    = new OrthoGameCamera(Gfx._SCENE_WIDTH, Gfx._SCENE_HEIGHT, "Sprite Cam", app);
        hudGameCamera       = new OrthoGameCamera(Gfx._SCENE_WIDTH, Gfx._SCENE_HEIGHT, "Hud Cam", app);

        backgroundCamera.setZoomDefault(Gfx._DEFAULT_SCALE);
        tiledGameCamera.setZoomDefault(Gfx._DEFAULT_ZOOM);
        spriteGameCamera.setZoomDefault(Gfx._DEFAULT_ZOOM);
        hudGameCamera.setZoomDefault(Gfx._DEFAULT_SCALE);

        backgroundZoom  = new Zoom();
        gameZoom        = new Zoom();
        hudZoom         = new Zoom();

        worldRenderer   = new WorldRenderer(app);
        hudRenderer     = new HUDRenderer(app);

        worldRenderer.setBackground(GameAssets._GAME_BACKGROUND);

        AppConfig.camerasReady = true;
        isDrawingStage         = false;
    }

    /**
     * Process all cameras.
     */
    public void render()
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //
        // Set the positioning reference point for the cameras. Cameras
        // will centre on the main character.
        if (app.currentScreenID == ScreenID._GAME_SCREEN)
        {
            if ((app.getPlayer() != null)
                && app.mainGameScreen.getGameState().after(StateID._STATE_SETUP)
                && !app.preferences.isEnabled(Preferences._SCROLL_DEMO))
            {
                app.mapUtils.positionAt
                    (
                        (int) app.getPlayer().sprite.getX(),
                        (int) app.getPlayer().sprite.getY()
                    );
            }
        }
        else
        {
            app.mapData.mapPosition.set(0, 0);
        }

        app.worldModel.setDebugMatrix();

        app.spriteBatch.enableBlending();
        app.spriteBatch.begin();

        // ----- Draw the Background, if enabled -----
        if (backgroundCamera.isInUse)
        {
            backgroundCamera.setPosition
                (
                    (float) (app.mapData.mapPosition.x + (Gfx._VIEW_WIDTH / 2)),
                    (float) (app.mapData.mapPosition.y + (Gfx._VIEW_HEIGHT / 2)),
                    0,
                    backgroundZoom.getZoomValue(),
                    false
                );

            worldRenderer.moveBackgroundWindow();
            worldRenderer.renderBackground(app.spriteBatch, backgroundCamera);
        }

        // ----- Draw the TiledMap, if enabled -----
        if (tiledGameCamera.isInUse)
        {
            tiledGameCamera.setPosition
                (
                    (float) (app.mapData.mapPosition.x + (Gfx._VIEW_WIDTH / 2)),
                    (float) (app.mapData.mapPosition.y + (Gfx._VIEW_HEIGHT / 2)),
                    0,
                    gameZoom.getZoomValue(),
                    true
                );

            app.mapData.render(tiledGameCamera.camera);

            if (app.preferences.isEnabled(Preferences._ANIM_TILES))
            {
                AnimatedTiledMapTile.updateAnimationBaseTime();
            }

            developmentRendering();
        }

        // ----- Draw the game sprites, if enabled -----
        if (spriteGameCamera.isInUse)
        {
            if (AppConfig.gameScreenActive)
            {
                spriteGameCamera.setPosition
                    (
                        (float) (app.mapData.mapPosition.x + (Gfx._VIEW_WIDTH / 2)),
                        (float) (app.mapData.mapPosition.y + (Gfx._VIEW_HEIGHT / 2)),
                        0,
                        gameZoom.getZoomValue(),
                        true
                    );
            }
            else
            {
                spriteGameCamera.setPosition
                    (
                        (float) app.mapData.mapPosition.x,
                        (float) app.mapData.mapPosition.y,
                        0,
                        gameZoom.getZoomValue(),
                        false
                    );
            }

            if (!app.preferences.isEnabled(Preferences._USING_ASHLEY_ECS))
            {
                if (!AppConfig.developerPanelActive)
                {
                    worldRenderer.render(app.spriteBatch, spriteGameCamera);
                }
            }
        }

        // ----- Draw the HUD and any related objects, if enabled -----
        // TODO: 14/02/2019 - Update the HUD to use Scene2D.ui stage.
        if (hudGameCamera.isInUse)
        {
            hudGameCamera.setPosition
                (
                    (float) (app.mapData.mapPosition.x + (Gfx._VIEW_WIDTH / 2)),
                    (float) (app.mapData.mapPosition.y + (Gfx._VIEW_HEIGHT / 2)),
                    0,
                    hudZoom.getZoomValue(),
                    false
                );

            hudRenderer.render(app.spriteBatch, hudGameCamera);
        }

        app.spriteBatch.end();

        // ----- Draw the Stage, if enabled -----
        if (isDrawingStage)
        {
            app.stage.act(Math.min(Gdx.graphics.getDeltaTime(), Gfx._STEP_TIME));
            app.stage.draw();
        }

        if (app.preferences.isEnabled(Preferences._BOX2D_PHYSICS))
        {
            app.worldModel.drawDebugMatrix();
        }
    }

    private void developmentRendering()
    {
        if (Developer.isDevMode())
        {
            if (app.preferences.isEnabled(Preferences._SPAWNPOINTS))
            {
                app.mapData.mapRenderer.renderTileLayer(app.mapData.markerTilesLayer);
            }
        }
    }

    @Override
    public void dispose()
    {
        backgroundCamera.dispose();
        tiledGameCamera.dispose();
        spriteGameCamera.dispose();
        hudGameCamera.dispose();

        backgroundZoom = null;
        gameZoom = null;
        hudZoom = null;

        worldRenderer = null;
        hudRenderer = null;
    }
}
