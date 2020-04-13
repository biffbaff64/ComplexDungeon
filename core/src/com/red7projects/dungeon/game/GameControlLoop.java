/*
 *  Copyright 18/11/2018 Red7Projects.
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

package com.red7projects.dungeon.game;

import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.utils.logging.Trace;
import com.red7projects.dungeon.screens.MainGameScreen;
import com.red7projects.dungeon.ui.MessagePanel;

import java.util.concurrent.TimeUnit;

public class GameControlLoop extends AbstractControlLoop
{
    private MessagePanel messagePanel;

    public GameControlLoop(App _app)
    {
        super(_app);
    }

    @Override
    public void initialise()
    {
    }

    @Override
    public void update(StateManager gameState)
    {
        switch (gameState.get())
        {
            //
            // Initialise the current level.
            // If the level is restarting, that will
            // also be handled here.
            case _STATE_SETUP:
            {
                stateSetup();
            }
            break;

            //
            // Display and update the 'Get Ready' message.
            case _STATE_GET_READY:
            {
                stateGetReady();
            }
            break;

            case _STATE_DEVELOPER_PANEL:
            case _STATE_SETTINGS_PANEL:
            case _STATE_PAUSED:
            case _STATE_GAME:
            {
                stateGame();
            }
            break;

            case _STATE_MESSAGE_PANEL:
            {
                stateMessagePanel();
            }
            break;

            //
            // Player lost a life.
            // Trying again.
            case _STATE_LEVEL_RETRY:
            {
                stateSetForRetry();
            }
            break;

            //
            // Missile base destroyed, on to the next one
            case _STATE_LEVEL_FINISHED:
            {
                stateSetForLevelFinished();
            }
            break;

            //
            // 'GAME OVER' Message, LJM has lost all lives.
            case _STATE_GAME_OVER:
            {
                stateSetForGameOverMessage();
            }
            break;

            //
            // All Levels finished, Earth is saved, LJM is a Hero!!!
            case _STATE_GAME_FINISHED:
            {
                stateSetForGameFinished();
            }
            break;

            //
            // Update during the 'Missile Launched' message,
            // and also when LJM is teleporting
            case _STATE_ANNOUNCE_MISSILE:
            case _STATE_TELEPORTING:
            {
                app.entityManager.updateSprites();
                app.getHud().update();
            }
            break;

            //
            // Back to MainMenuScreen
            case _STATE_END_GAME:
            {
                stateSetForEndGame();
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unsupported gameState: " + gameState.get());
            }
            break;
        }
    }

    /**
     * Initialise the current level.
     * If the level is restarting, that will
     * also be handled here
     */
    private void stateSetup()
    {
        Trace.megaDivider("_STATE_SETUP");

        // All cameras ON
        app.cameraUtils.enableAllCameras();
        app.baseRenderer.isLerpingEnabled = false;

        app.gameUtils.prepareCurrentLevel(scr().firstTime);

        if (scr().firstTime)
        {
            // Start the Game screen tune playing
            Sfx.inst().playGameTune(true);

            // Initialise the 'Get Ready' message
            app.getHud().messageManager.enable();
            app.getHud().messageManager.addZoomMessage
                (
                    GameAssets._GETREADY_MSG_ASSET,
                    1500
                );
        }

        scr().gameState.set(StateID._STATE_GET_READY);

        app.gameProgress.gameSetupDone = true;
    }

    /**
     * Display and update the 'Get Ready' message
     */
    private void stateGetReady()
    {
        app.getHud().update();

        if (!app.getHud().messageManager.doesPanelExist(GameAssets._GETREADY_MSG_ASSET))
        {
            Trace.__FILE_FUNC("----- START GAME (GET READY) -----");

            scr().gameState.set(StateID._STATE_GAME);
            app.getHud().setStateID(StateID._STATE_PANEL_UPDATE);

            if (!scr().firstTime)
            {
                if ((app.getLives() == Constants._MAX_LIVES) && (app.getLevel() == 1))
                {
                    app.getHud().messageManager.enable();
                    app.getHud().messageManager.addZoomMessage
                        (
                            GameAssets._WELCOME_MSG_ASSET,
                            5000,
                            552,
                            (Gfx._VIEW_HEIGHT - 270)
                        );
                }
            }

            app.getHud().showControls();

            scr().firstTime = false;

            if (app.getPlayer() != null)
            {
                app.getPlayer().setup();
            }
        }
    }

    /**
     * Update the game for states:-
     * _STATE_DEVELOPER_PANEL
     * _STATE_SETTINGS_PANEL
     * _STATE_PAUSED
     * _STATE_GAME
     */
    private void stateGame()
    {
        app.getHud().update();

        if (scr().gameState.get() == StateID._STATE_DEVELOPER_PANEL)
        {
            if (!AppConfig.developerPanelActive)
            {
                scr().gameState.set(StateID._STATE_GAME);
                app.getHud().setStateID(StateID._STATE_PANEL_UPDATE);
            }
        }
        else
        {
            app.baseRenderer.isLerpingEnabled = (scr().gameState.get() == StateID._STATE_GAME);

            app.mapUtils.update();
            app.entityManager.updateSprites();
            app.entityManager.tidySprites();

            if (!scr().endGameManager.update())
            {
                //
                // Tasks to perform if the game has not ended
                if (app.getPlayer() != null)
                {
                    if ((app.getPlayer().isMovingX || app.getPlayer().isMovingY)
                        && (app.getHud().messageManager.doesPanelExist(GameAssets._WELCOME_MSG_ASSET)))
                    {
                        app.getHud().messageManager.getCurrentPanel().forceZoomOut();
                    }
                }

                if (scr().gameState.get() == StateID._STATE_PAUSED)
                {
                    if (!AppConfig.gamePaused)
                    {
                        scr().gameState.set(StateID._STATE_GAME);
                    }
                }
                else
                {
                    if (scr().gameState.get() == StateID._STATE_MESSAGE_PANEL)
                    {
                        messagePanel = new MessagePanel(app);
                        messagePanel.create();
                    }
                }
            }
        }
    }

    /**
     * Handles the message panel which appears when the
     * player speaks to a villager/guide.
     */
    private void stateMessagePanel()
    {
        app.getHud().update();
        app.mapUtils.update();

        messagePanel.update();
    }

    /**
     * Handles the preparation for retrying the current
     * level, after LJM loses a life.
     */
    private void stateSetForRetry()
    {
        app.getHud().update();
        app.mapUtils.update();

        if (scr().retryDelay.time(TimeUnit.MILLISECONDS) > 2000)
        {
            if (AppConfig.quitToMainMenu)
            {
                scr().gameState.set(StateID._STATE_GAME_OVER);

                app.getHud().hideControls(true);
                app.getHud().messageManager.enable();
                app.getHud().messageManager.addZoomMessage(GameAssets._GAMEOVER_MSG_ASSET, 3000);
            }
            else
            {
                scr().gameState.set(StateID._STATE_SETUP);
            }

            scr().retryDelay = null;
        }
    }

    /**
     * Handles finishing the current level and
     * moving on to the next one.
     * <p>
     * NOTE: "Level finished" for this game is actually "room exit".
     */
    private void stateSetForLevelFinished()
    {
        app.gameUtils.closeCurrentLevel();

        app.getHud().update();
        app.mapUtils.update();

        scr().reset();
        scr().gameState.set(StateID._STATE_SETUP);
    }

    /**
     * Game Over, due to losing all lives.
     * (Waits for the 'Game Over' message to disappear.
     */
    private void stateSetForGameOverMessage()
    {
        app.getHud().update();

        if (!app.getHud().messageManager.doesPanelExist(GameAssets._GAMEOVER_MSG_ASSET))
        {
            scr().gameState.set(StateID._STATE_END_GAME);
        }
    }

    /**
     * Game Over, due to all levels being completed.
     */
    private void stateSetForGameFinished()
    {
        app.getHud().update();

        if (scr().completedPanel.update())
        {
            scr().completedPanel.dispose();
            scr().completedPanel = null;
            scr().gameState.set(StateID._STATE_END_GAME);
        }
    }

    /**
     * Game Ended, hand control back to MainMenuScreen.
     */
    private void stateSetForEndGame()
    {
        Trace.megaDivider("***** GAME OVER *****");

        Sfx.inst().playGameTune(false);

        app.gameProgress.closeLastGame();

        scr().dispose();

        app.setScreen(app.mainMenuScreen);

        scr().gameState.set(StateID._STATE_CLOSING);
    }

    private MainGameScreen scr()
    {
        return app.mainGameScreen;
    }
}
