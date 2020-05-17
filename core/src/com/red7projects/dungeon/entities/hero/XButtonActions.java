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

import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.ui.SlidePanel;

public class XButtonActions
{
    private App app;

    public XButtonActions(App _app)
    {
        this.app = _app;
    }

    public void process()
    {
        if (!app.getHud().messageManager.doesPanelExist(GameAssets._MESSAGE_PANEL_ASSET))
        {
            if (app.getPlayer().getSpriteAction() == Actions._STANDING)
            {
                app.appState.set(StateID._STATE_MESSAGE_PANEL);
            }
        }
        else
        {
            ((SlidePanel) app.getHud().messageManager.getCurrentPanel()).activate();

            app.getHud().messageManager.closeSlidePanel();
        }
    }
}
