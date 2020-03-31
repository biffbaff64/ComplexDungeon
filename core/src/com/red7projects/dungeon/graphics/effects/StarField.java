/*
 *  Copyright 03/06/2018 Red7Projects.
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

package com.red7projects.dungeon.graphics.effects;

import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.game.App;

import java.util.ArrayList;

/**
 * 3D Starfield Manager.
 *
 * @author Richard Ikin. This code is adapted from similar
 *         code that I have seen in the past. I cannot
 *         remember who the author of that code was and,
 *         when his/her identity is established, this
 *         javaDoc will be updated to add credits.
 */
@SuppressWarnings({"WeakerAccess"})
public class StarField implements Disposable
{
    public final float speed    = 90f;
    public final int   numStars = 5000;

    private ArrayList<StarObject> stars;
    private App app;

    public StarField(App _app)
    {
        this.app    = _app;
        this.stars  = new ArrayList<>();

        for (int i=0; i<numStars; i++)
        {
            stars.add(new StarObject(app));
        }
    }

    public void render()
    {
        for (StarObject star : stars)
        {
            star.render(speed);
        }
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose()
    {
        stars.clear();
        stars = null;
        app = null;
    }
}
