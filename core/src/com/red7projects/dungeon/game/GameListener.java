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

package com.red7projects.dungeon.game;

import com.badlogic.gdx.controllers.Controllers;
import com.red7projects.dungeon.config.AppConfig;

public class GameListener
{
    public enum Stack
    {
        _SCORE,
        _KEY,
        _COIN,
        _GEM,
        _SHIELD
    }

    private int scoreStack;
    private int keysStack;
    private int coinsStack;
    private int gemsStack;
    private int shieldStack;

    private final App app;

    public GameListener(App _app)
    {
        this.app = _app;

        this.scoreStack = 0;
        this.keysStack = 0;
        this.coinsStack = 0;
        this.gemsStack = 0;
        this.shieldStack = 0;
    }

    public void update()
    {
        AppConfig.controllersFitted = (Controllers.getControllers().size > 0);

        switch (app.mainGameScreen.gameState.get())
        {
            case _STATE_PAUSED:
            case _STATE_GAME:
            case _STATE_MESSAGE_PANEL:
            {
                updateStacks();
            }
            break;

            default:
                break;
        }
    }

    public void stackPush(Stack stack, int amount)
    {
        switch (stack)
        {
            case _SHIELD:
            {
                shieldStack += amount;
            }
            break;

            case _COIN:
            {
                coinsStack += amount;
            }
            break;

            case _GEM:
            {
                gemsStack += amount;
            }
            break;

            case _KEY:
            {
                keysStack += amount;
            }
            break;

            case _SCORE:
            {
                scoreStack += amount;
            }
            break;

            default:
                break;
        }
    }

    private void updateStacks()
    {
        int amount;

        if (scoreStack > 0)
        {
            amount = GameUtils.getCount(scoreStack);

            app.gameProgress.addScore(amount);
            scoreStack -= amount;
        }

        if (coinsStack > 0)
        {
            amount = GameUtils.getCount(coinsStack);

            app.gameProgress.addCoins(amount);
            coinsStack -= amount;
        }

        if (gemsStack > 0)
        {
            amount = GameUtils.getCount(gemsStack);

            app.gameProgress.addGems(amount);
            gemsStack -= amount;
        }

        if (keysStack > 0)
        {
            amount = GameUtils.getCount(keysStack);

            app.gameProgress.addKeys(amount);
            keysStack -= amount;
        }

        if (shieldStack > 0)
        {
            amount = GameUtils.getCount(shieldStack);

            app.getHud().getHealthBar().add(amount);
            shieldStack -= amount;
        }
    }
}
