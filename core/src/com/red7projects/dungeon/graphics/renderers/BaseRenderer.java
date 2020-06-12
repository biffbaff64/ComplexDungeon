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
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.config.Settings;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.camera.OrthoGameCamera;
import com.red7projects.dungeon.graphics.camera.Zoom;
import com.red7projects.dungeon.map.tiled.tiles.AnimatedTiledMapTile;
import com.red7projects.dungeon.maths.SimpleVec3F;
import com.red7projects.dungeon.screens.ScreenID;
import com.red7projects.dungeon.utils.logging.Trace;

public class BaseRenderer implements Disposable
{
    public OrthoGameCamera hudGameCamera;
    public OrthoGameCamera spriteGameCamera;
    public OrthoGameCamera tiledGameCamera;
    public Zoom            gameZoom;
    public Zoom            hudZoom;
    public boolean         isDrawingStage;

    private WorldRenderer worldRenderer;
    private HUDRenderer   hudRenderer;
    private SimpleVec3F   cameraPos;

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
        Trace.__FILE_FUNC();

        AppConfig.camerasReady = false;

        tiledGameCamera  = new OrthoGameCamera(Gfx._GAME_SCENE_WIDTH, Gfx._GAME_SCENE_HEIGHT, "Tiled Cam", app);
        spriteGameCamera = new OrthoGameCamera(Gfx._GAME_SCENE_WIDTH, Gfx._GAME_SCENE_HEIGHT, "Sprite Cam", app);
        hudGameCamera    = new OrthoGameCamera(Gfx._HUD_SCENE_WIDTH, Gfx._HUD_SCENE_HEIGHT, "Hud Cam", app);

        tiledGameCamera.setStretchViewport();
        spriteGameCamera.setStretchViewport();
        hudGameCamera.setStretchViewport();

        tiledGameCamera.setZoomDefault(Gfx._DEFAULT_ZOOM);
        spriteGameCamera.setZoomDefault(Gfx._DEFAULT_ZOOM);
        hudGameCamera.setZoomDefault(Gfx._DEFAULT_ZOOM);

        hudGameCamera.camera.position.set(0, 0, 0);
        hudGameCamera.camera.update();

        gameZoom      = new Zoom();
        hudZoom       = new Zoom();
        worldRenderer = new WorldRenderer(app);
        hudRenderer   = new HUDRenderer(app);
        cameraPos     = new SimpleVec3F();

        isDrawingStage         = false;
        AppConfig.camerasReady = true;
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
                    && app.appState.after(StateID._STATE_SETUP)
                    && !app.settings.isEnabled(Settings._SCROLL_DEMO))
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

        app.spriteBatch.enableBlending();

        // ----- Draw the TiledMap, if enabled -----
        if (tiledGameCamera.isInUse)
        {
            tiledGameCamera.viewport.apply();
            app.spriteBatch.setProjectionMatrix(tiledGameCamera.camera.combined);
            app.spriteBatch.begin();

            cameraPos.x = (float) (app.mapData.mapPosition.getX() + (Gfx._VIEW_WIDTH / 2));
            cameraPos.y = (float) (app.mapData.mapPosition.getY() + (Gfx._VIEW_HEIGHT / 2));
            cameraPos.z = 0;

            if (tiledGameCamera.isLerpingEnabled)
            {
                tiledGameCamera.lerpTo(cameraPos, Gfx._LERP_SPEED, gameZoom.getZoomValue(), true);
            }
            else
            {
                tiledGameCamera.setPosition(cameraPos, gameZoom.getZoomValue(), true);
            }

            app.mapData.render(tiledGameCamera.camera);

            AnimatedTiledMapTile.updateAnimationBaseTime();

            //
            // Deleted but, for future reference, the
            // MarkerTile layer was drawn here...

            app.spriteBatch.end();
        }

        // ----- Draw the game sprites, if enabled -----
        if (spriteGameCamera.isInUse)
        {
            spriteGameCamera.viewport.apply();
            app.spriteBatch.setProjectionMatrix(spriteGameCamera.camera.combined);
            app.spriteBatch.begin();

            if (AppConfig.gameScreenActive)
            {
                if (spriteGameCamera.isLerpingEnabled)
                {
                    spriteGameCamera.lerpTo(cameraPos, Gfx._LERP_SPEED, gameZoom.getZoomValue(), true);
                }
                else
                {
                    spriteGameCamera.setPosition(cameraPos, gameZoom.getZoomValue(), false);
                }
            }
            else
            {
                cameraPos.x = (float) app.mapData.mapPosition.getX();
                cameraPos.y = (float) app.mapData.mapPosition.getY();
                cameraPos.z = 0;

                spriteGameCamera.setPosition(cameraPos, gameZoom.getZoomValue(),false);
            }

            if (!app.settings.isEnabled(Settings._USING_ASHLEY_ECS))
            {
                if (!AppConfig.developerPanelActive)
                {
                    worldRenderer.render(app.spriteBatch, spriteGameCamera);
                }
            }

            app.spriteBatch.end();
        }

        // ----- Draw the HUD and any related objects, if enabled -----
        if (hudGameCamera.isInUse)
        {
            hudGameCamera.viewport.apply();
            app.spriteBatch.setProjectionMatrix(hudGameCamera.camera.combined);
            app.spriteBatch.begin();

            cameraPos.setEmpty();

            hudGameCamera.setPosition(cameraPos, hudZoom.getZoomValue(), false);
            hudRenderer.render(app.spriteBatch, hudGameCamera);

            app.spriteBatch.end();
        }

        // ----- Draw the Stage, if enabled -----
        if (isDrawingStage)
        {
            app.stage.act(Math.min(Gdx.graphics.getDeltaTime(), Gfx._STEP_TIME));
            app.stage.draw();
        }

        app.worldModel.drawDebugMatrix();
    }

    @Override
    public void dispose()
    {
        tiledGameCamera.dispose();
        spriteGameCamera.dispose();
        hudGameCamera.dispose();

        gameZoom = null;
        hudZoom  = null;

        worldRenderer = null;
        hudRenderer   = null;
    }
}
