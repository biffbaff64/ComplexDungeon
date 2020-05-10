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

import com.red7projects.dungeon.entities.characters.FloorSwitch;
import com.red7projects.dungeon.entities.characters.TreasureChest;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.GraphicID;

@SuppressWarnings("WeakerAccess")
public class AButtonActions
{
    private App app;

    public AButtonActions(App _app)
    {
        this.app = _app;
    }

    public void process()
    {
        if (app.getPlayer().actionButton.getFutureActionMode() == Actions._PRESS_FLOOR_SWITCH)
        {
            if (app.getPlayer().collisionObject.contactSprite instanceof FloorSwitch)
            {
                app.getPlayer().collisionObject.contactSprite.setAction(Actions._RUNNING);
            }
        }
        else if (app.getPlayer().actionButton.getFutureActionMode() == Actions._OPEN_TREASURE_CHEST)
        {
            if ((app.getPlayer().collisionObject.contactSprite instanceof TreasureChest)
                && (app.getPlayer().collisionObject.contactSprite.gid == GraphicID.G_TREASURE_CHEST)
                && (app.getPlayer().collisionObject.contactSprite.getSpriteAction() == Actions._STANDING))
            {
                app.getPlayer().collisionObject.contactSprite.isAnimating = true;
                app.getPlayer().collisionObject.contactSprite.setAction(Actions._OPENING);
            }
        }
    }
}
