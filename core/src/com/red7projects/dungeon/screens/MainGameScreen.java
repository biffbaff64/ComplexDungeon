/*
 *  Copyright 24/04/2018 Red7Projects.
 *  <p>
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.red7projects.dungeon.screens;

import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.config.Settings;
import com.red7projects.dungeon.game.*;
import com.red7projects.dungeon.graphics.camera.Shake;
import com.red7projects.dungeon.ui.GameCompletedPanel;
import com.red7projects.dungeon.utils.development.Developer;
import com.red7projects.dungeon.utils.logging.StopWatch;
import com.red7projects.dungeon.utils.logging.Trace;

@SuppressWarnings("WeakerAccess")
//@formatter:off
public class MainGameScreen extends AbstractBaseScreen
{
    public       GameCompletedPanel completedPanel;
    public       StopWatch          retryDelay;
    public final StateManager       gameState;
    public       EndGameManager     endGameManager;
    public       GameControlLoop    gameControlLoop;

    /*
     * boolean firstTime - TRUE if MainGameSCreen has
     * just been entered, i.e. a NEW Game.
     *
     * Setting this to true allows initialise() to
     * be called from show(), one time only. If false, then
     * initialise() will be bypassed but the rest of show()
     * will be processed.
     */
    public boolean firstTime;

    /**
     * Constructor
     *
     * @param _app An instance of the game
     */
    public MainGameScreen(App _app)
    {
        super(_app);

        this.firstTime = true;
        this.gameState = new StateManager();
    }

    /**
     * Initialise the main game screen
     */
    @Override
    public void initialise()
    {
        if (firstTime)
        {
            Trace.divider();
            Trace.__FILE_FUNC("NEW GAME:");
            Trace.__FILE_FUNC("_DEVMODE: " + Developer.isDevMode());
            Trace.__FILE_FUNC("_GODMODE: " + Developer.isGodMode());
            Trace.divider();

            endGameManager  = new EndGameManager(app);
            gameControlLoop = new GameControlLoop(app);
            app.gameUtils   = new GameUtils(app);

            gameControlLoop.initialise();
            app.gameUtils.prepareNewGame();

            gameState.set(StateID._STATE_SETUP);
        }

        app.inputManager.virtualJoystick.show();

        Shake.setAllowed(app.settings.isEnabled(Settings._VIBRATIONS));
    }

    /**
     * Update the main game loop
     */
    @Override
    public void update()
    {
        switch (gameState.get())
        {
            case _STATE_SETUP:
            case _STATE_GET_READY:
            case _STATE_DEVELOPER_PANEL:
            case _STATE_SETTINGS_PANEL:
            case _STATE_PAUSED:
            case _STATE_GAME:
            case _STATE_MESSAGE_PANEL:
            case _STATE_LEVEL_RETRY:
            case _STATE_LEVEL_FINISHED:
            case _STATE_GAME_OVER:
            case _STATE_GAME_FINISHED:
            case _STATE_ANNOUNCE_MISSILE:
            case _STATE_TELEPORTING:
            case _STATE_END_GAME:
            {
                gameControlLoop.update(gameState);
            }
            break;

            case _STATE_PREPARE_GAME_END:
            {
                gameState.set(gameState.getStored());
            }
            break;

            case _STATE_CLOSING:
            {
            }
            break;

            default:
            {
                Trace.__FILE_FUNC();
                Trace.dbg("Unsupported game state: " + gameState.get());
            }
        }
    }

    /**
     * Update and Render the game, and
     * step the physics world.
     *
     * @param delta     Time since the last update.
     */
    @Override
    public void render(float delta)
    {
        super.update();

//        if (super.flowState.get() == StateID._STATE_GAME)
        {
            update();
        }

        super.render(delta);

        app.worldModel.worldStep();
    }

    public void draw()
    {
        if (!app.settings.isEnabled(Settings._USING_ASHLEY_ECS))
        {
            app.entityManager.drawSprites();
        }
    }

    @Override
    public StateManager getGameState()
    {
        return gameState;
    }

    /**
     * Signal MainGameScreen reset, to allow
     * correct level initialisation.
     */
    public void reset()
    {
        Trace.__FILE_FUNC();

        AppConfig.gamePaused = false;
    }

    /**
     * Called when this screen is first shown.
     * This method MUST call super.show() to
     * initialise the super class flow state.
     */
    @Override
    public void show()
    {
        Trace.__FILE_FUNC();

        super.show();

        app.currentScreenID = ScreenID._GAME_SCREEN;
        app.cameraUtils.disableAllCameras();

        AppConfig.menuScreenActive  = false;
        AppConfig.gameScreenActive  = true;
        AppConfig.finalScreenActive = false;

        initialise();

        gameState.set(StateID._STATE_SETUP);
    }

    /**
     * Called on screen exit. A call to super.hide()
     * is not essential.
     */
    @Override
    public void hide()
    {
        Trace.__FILE_FUNC();

        super.hide();

        AppConfig.gameScreenActive = false;
    }

    /**
     * Tidy up game object before exit
     * to MainMenuScreen.
     */
    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

        super.dispose();

        app.entityManager.dispose();
        app.collisionUtils.dispose();
        app.getHud().dispose();

        app.gameProgress.dispose();

        app.baseRenderer.gameZoom.setZoomValue(0.0f);
        app.baseRenderer.hudZoom.setZoomValue(0.0f);

        app.hud         = null;
        completedPanel  = null;
        endGameManager  = null;
        retryDelay      = null;
        gameControlLoop = null;
    }
}
