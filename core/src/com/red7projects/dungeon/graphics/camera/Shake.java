/*
 *  Copyright 21/12/2018 Red7Projects.
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

package com.red7projects.dungeon.graphics.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.red7projects.dungeon.game.App;

@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal"})
public class Shake
{
    public static boolean screenShakeEnabled;

    private static float shakeRadius;
    private static float randomAngle;
    private static float elapsedTime;
    private static float shakeDuration;
    private static float shakeIntensity;
    private static boolean screenShakeAllowed;

    public static void start(App app)
    {
        start(30, 450, 10, app);
    }

    public static void start(float radius, float duration, float intensity, App app)
    {
        if (screenShakeAllowed)
        {
            if (!screenShakeEnabled)
            {
                shakeDuration = duration / 1000f;
                shakeRadius = radius;
                shakeIntensity = intensity;

                elapsedTime = 0;
                randomAngle = MathUtils.random.nextFloat() % 360f;
                screenShakeEnabled = true;
            }
        }
    }

    public static void update(float delta, OrthographicCamera camera, App app)
    {
        if (!screenShakeAllowed)
        {
            reset();
        }
        else
        {
            if(screenShakeEnabled)
            {
                // Only shake when required.
                if (elapsedTime < shakeDuration)
                {
                    // Calculate the amount of shake based on how long it has been shaking already
                    float currentPower = shakeIntensity * camera.zoom * ((shakeDuration - elapsedTime) / shakeDuration);

                    float x = (MathUtils.random.nextFloat() - 0.5f) * currentPower;
                    float y = (MathUtils.random.nextFloat() - 0.5f) * currentPower;

                    camera.translate(-x, -y);

                    // Increase the elapsedTime time by the delta provided.
                    elapsedTime += delta;
                }
                else
                {
                    reset();
                }
            }
        }
    }

    public static void reset()
    {
        screenShakeEnabled = false;
    }

    public static void setAllowed(boolean allowed)
    {
        screenShakeAllowed = allowed;
    }
}
