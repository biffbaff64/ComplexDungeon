/*
 *  Copyright 31/01/2019 Red7Projects.
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

package com.red7projects.dungeon.input;

import com.badlogic.gdx.math.Vector2;
import com.red7projects.dungeon.game.App;

public class InputUtils
{
    public static float getJoystickAngle(App app)
    {
        return getJoystickVector(app).angle();
    }

    public static Vector2 getJoystickVector(App app)
    {
        float xPerc = app.inputManager.virtualJoystick.getTouchpad().getKnobPercentX();
        float yPerc = app.inputManager.virtualJoystick.getTouchpad().getKnobPercentY();

        Vector2 vector2 = new Vector2(xPerc, yPerc);

        return vector2.rotate90(-1);
    }
}
