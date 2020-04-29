/*
 *  Copyright 28/04/2018 Red7Projects.
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

package com.red7projects.dungeon.entities.hero;

import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.StateID;

public class YButtonActions
{
    private App app;

    public YButtonActions(App _app)
    {
        this.app = _app;
    }

    public void process()
    {
        if (app.mainGameScreen.gameState.get().equals(StateID._STATE_GAME))
        {
            app.getHud().setObjectivesPanelIndex((app.getHud().getObjectivesPanelIndex() + 1) % 2);
        }
    }
}
