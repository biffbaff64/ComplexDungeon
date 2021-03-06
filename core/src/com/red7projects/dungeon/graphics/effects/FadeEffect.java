/*
 ******************************************************************************
 * Copyright 27/03/2017 See AUTHORS file.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ****************************************************************************
 */

package com.red7projects.dungeon.graphics.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.utils.logging.StopWatch;

import java.util.concurrent.TimeUnit;

public class FadeEffect
{
    public static int      direction;
    public static float    speed;
    public static boolean  isActive;

    private static StopWatch    timer;
    private static float        width;
    private static float        height;
    private static Color        colour;
    private static NinePatch    image;
    private static App          app;

    private FadeEffect() {}

    public static void createEffect(App _app, float _width, float _height)
    {
        app = _app;

        image = new NinePatch(app.assets.getObjectsAtlas().findRegion("bar9patch"), 1, 1, 1, 1);

        timer       = StopWatch.start();
        direction   = Movement._DIRECTION_STILL;
        colour      = Color.BLACK;
        speed       = 0;
        width       = _width;
        height      = _height;
        isActive    = false;
    }

    public static boolean update()
    {
        if (isActive)
        {
            int _FADE_SPEED = 15;

            if (timer.time(TimeUnit.MILLISECONDS) >= _FADE_SPEED)
            {
                colour.a += (speed * direction);

                timer.reset();
            }

            return ((direction == Movement._DIRECTION_DOWN) && (colour.a < 0.0f))
                    || ((direction == Movement._DIRECTION_UP) && (colour.a > 1.0f));
        }

        return true;
    }

    public static void render()
    {
        if (isActive)
        {
            if (colour.a >= 0)
            {
                image.setColor(colour);
                image.draw(app.spriteBatch, 0, 0, width, height);
            }
        }
    }

    public static void end()
    {
        direction   = Movement._DIRECTION_STILL;
        isActive    = false;
    }

    /**
     * Fades from clear to Black.
     */
    public static void triggerFadeOut()
    {
        direction   = Movement._DIRECTION_UP;
        colour.a    = 0.0f;
        speed       = 0.1f;
        isActive    = true;
    }

    /**
     * Fades from Black to clear.
     */
    public static void triggerFadeIn()
    {
        direction   = Movement._DIRECTION_DOWN;
        colour.a    = 1.0f;
        speed       = 0.1f;
        isActive    = true;
    }
}
