/*
 *  Copyright 01/06/2018 Red7Projects.
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

package com.red7projects.dungeon.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.ui.UIPage;
import com.red7projects.dungeon.utils.logging.StopWatch;

import java.util.concurrent.TimeUnit;

public class CreditsPage implements UIPage, Disposable
{
    private Texture   foreground;
    private StopWatch stopWatch;
    private App       app;

    CreditsPage(App _app)
    {
        this.app = _app;

        foreground = app.assets.loadSingleAsset("data/credits_foreground.png", Texture.class);

        this.stopWatch = StopWatch.start();
    }

    @Override
    public boolean update()
    {
        if (stopWatch == null)
        {
            return false;
        }

        return (stopWatch.time(TimeUnit.MILLISECONDS) >= 5000);
    }

    @Override
    public void reset()
    {
    }

    @Override
    public void show()
    {
        stopWatch.reset();
    }

    @Override
    public void hide()
    {
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        if (foreground != null)
        {
            spriteBatch.draw(foreground, 0, 0);
        }
    }

    @Override
    public void dispose()
    {
        app.assets.unloadAsset("data/credits_foreground.png");

        stopWatch  = null;
        foreground = null;
        app        = null;
    }
}
