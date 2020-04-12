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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.development.DebugRenderer;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.camera.OrthoGameCamera;
import com.red7projects.dungeon.maths.SimpleVec2;
import com.red7projects.dungeon.physics.Direction;
import com.red7projects.dungeon.physics.Movement;

@SuppressWarnings("WeakerAccess")
//@formatter:off
public class WorldRenderer implements GameScreenRenderer
{
    private static final float _SPEED = 0.0002f;

    private       SimpleVec2 bgPosition;
    private       Direction  bgDirection;
    private       Texture    background;
    private       float      bgDistance;
    private final App        app;

    public WorldRenderer(App _app)
    {
        this.app = _app;
    }

    @Override
    public void render(SpriteBatch spriteBatch, OrthoGameCamera gameCamera)
    {
        StateID stateID;

        if (AppConfig.gameScreenActive)
        {
            stateID = app.mainGameScreen.getGameState().get();
        }
        else
        {
            stateID = app.mainMenuScreen.getGameState().get();
        }

        // DO NOT Change the game state in here!!
        switch (stateID)
        {
            case _STATE_TITLE_SCREEN:
            {
                app.baseRenderer.gameZoom.stop();
                app.mainMenuScreen.draw(spriteBatch, gameCamera);
            }
            break;

            case _STATE_SETUP:
            case _STATE_GET_READY:
            case _STATE_PAUSED:
            case _STATE_LEVEL_RETRY:
            case _STATE_LEVEL_FINISHED:
            case _STATE_GAME:
            case _STATE_MESSAGE_PANEL:
            case _STATE_ANNOUNCE_MISSILE:
            case _STATE_SETTINGS_PANEL:
            case _STATE_TELEPORTING:
            case _STATE_DEBUG_HANG:
            {
                app.baseRenderer.gameZoom.stop();
                app.mainGameScreen.draw();

                DebugRenderer.drawBoxes();
            }
            break;

            case _STATE_CLOSING:
            case _STATE_GAME_OVER:
            case _STATE_END_GAME:
            {
            }
            break;

            default:
                break;
        }
    }

    public void setBackground(String asset)
    {
        background      = app.assets.loadSingleAsset(asset, Texture.class);
        bgPosition      = new SimpleVec2();
        bgDirection     = new Direction(Movement._DIRECTION_RIGHT, Movement._DIRECTION_UP);
        bgDistance      = _SPEED * 100;
    }

    public void renderBackground(SpriteBatch spriteBatch, OrthoGameCamera gameCamera)
    {
        float xOrigin = gameCamera.camera.position.x - (float) (Gfx._VIEW_WIDTH / 2);
        float yOrigin = gameCamera.camera.position.y - (float) (Gfx._VIEW_HEIGHT / 2);

        spriteBatch.draw
            (
                background,
                xOrigin, yOrigin,
                bgPosition.getX(), bgPosition.getY(),
                Gfx._VIEW_WIDTH, Gfx._VIEW_HEIGHT
            );
    }

    public void moveBackgroundWindow()
    {
        app.baseRenderer.backgroundZoom.stop();

        bgDistance -= _SPEED;

        if (bgDirection.getY() == Movement._DIRECTION_UP)
        {
            app.baseRenderer.backgroundZoom.out(_SPEED);

            if (bgDistance <= 0)
            {
                bgDistance = _SPEED * 200;
                bgDirection.toggleY();
            }
        }
        else
        {
            app.baseRenderer.backgroundZoom.in(_SPEED);

            if (bgDistance <= 0)
            {
                bgDistance = _SPEED * 200;
                bgDirection.toggleY();
            }
        }
    }
}
