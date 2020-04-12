/*
 *  Copyright 31/12/2018 Red7Projects.
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

import com.red7projects.dungeon.logging.Trace;
import com.red7projects.dungeon.logging.StopWatch;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.ui.GameCompletedPanel;

public class EndGameManager
{
    private final App app;

    public EndGameManager(App _app)
    {
        this.app = _app;
    }

    public boolean update()
    {
        boolean returnFlag = false;

        //
        // Player is dead, no lives left
        if ((app.getPlayer() != null) && (app.getPlayer().getSpriteAction() == Actions._DEAD))
        {
            app.getHud().hideControls(true);

            app.mainGameScreen.getGameState().set(StateID._STATE_LEVEL_RETRY);

            AppConfig.quitToMainMenu = true;

            app.mainGameScreen.retryDelay = StopWatch.start();

            returnFlag = true;
        }
        else
        {
            //
            // Waheyy!! All levels completed!
            if (app.gameProgress.isGameCompleted())
            {
                Trace.__FILE_FUNC_WithDivider("GAME COMPLETED");
                Trace.divider();

                app.getHud().hideControls(true);

                app.mainGameScreen.completedPanel = new GameCompletedPanel(app);
                app.mainGameScreen.completedPanel.setup();

                app.getHud().setStateID(StateID._STATE_GAME_FINISHED);
                app.mainGameScreen.getGameState().set(StateID._STATE_GAME_FINISHED);

                returnFlag = true;
            }
            else if (app.gameProgress.isLevelCompleted())
            {
                //
                // For this game, 'levelCompleted' is actually 'roomExited'...
                Trace.__FILE_FUNC_WithDivider("LEVEL COMPLETED");
                Trace.divider();

                app.getHud().setStateID(StateID._STATE_LEVEL_FINISHED);
                app.mainGameScreen.getGameState().set(StateID._STATE_LEVEL_FINISHED);

                returnFlag = true;
            }
            //
            // Restarting due to life lost and
            // player is resetting...
            else if (app.gameProgress.isRestarting)
            {
                if (app.getPlayer().getSpriteAction() == Actions._RESETTING)
                {
                    Trace.__FILE_FUNC_WithDivider();
                    Trace.__FILE_FUNC("LIFE LOST - TRY AGAIN");
                    Trace.divider();

                    app.mainGameScreen.retryDelay = StopWatch.start();
                    app.mainGameScreen.getGameState().set(StateID._STATE_LEVEL_RETRY);
                }

                returnFlag = true;
            }
            //
            // Forcing quit to main menu screen.
            // For example, from pause menu...
            else if (AppConfig.forceQuitToMenu)
            {
                app.mainGameScreen.getGameState().set(StateID._STATE_END_GAME);
                returnFlag = true;
            }
        }

        return returnFlag;
    }
}
