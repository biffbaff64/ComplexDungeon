/*
 *  Copyright 20/05/2018 Red7Projects.
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

package com.red7projects.dungeon.input.buttons;

import com.badlogic.gdx.graphics.Color;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.GfxUtils;
import com.red7projects.dungeon.maths.Box;

public class Switch extends BasicButton
{
    public Switch(App _app)
    {
        this( 0, 0, 10, 10, _app);

        isSwitch = true;
    }

    public Switch(App _app, ButtonID _id)
    {
        super(_app, _id);
    }

    public Switch(int _x, int _y, int _width, int _height, App _app)
    {
        super(_app, ButtonID._DEFAULT);

        this.x          = _x;
        this.y          = _y;
        this.width      = _width;
        this.height     = _height;
        this.buttonRect = new Box(x, y, width, height);
        this.isPressed  = false;
        this.isSwitch   = true;
        this.isActive   = true;
    }

    @SuppressWarnings("unused")
    public void draw()
    {
        if (AppConfig.canDrawButtonBoxes)
        {
            GfxUtils.drawRect(x, y, width, height, 2, Color.WHITE);
        }
    }
}
