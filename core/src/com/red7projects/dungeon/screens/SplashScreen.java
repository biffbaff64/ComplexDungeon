/*
 *  Copyright 08/11/2018 Red7Projects.
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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GfxUtils;
import com.red7projects.dungeon.logging.StopWatch;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("FieldCanBeLocal")
public class SplashScreen implements Disposable
{
    private final App         app;
    private       StopWatch   stopWatch;
    private       SpriteBatch spriteBatch;
    private       Sprite      background;
    private       Sprite[]    loadingLetters;
    private       Thread      lettersUpdater;

    public SplashScreen(final App _app)
    {
        this.app = _app;
    }

    public void initialise()
    {
        if (AppConfig.isDesktopApp())
        {
            Gdx.graphics.setUndecorated(true);
            Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        }

        spriteBatch = new SpriteBatch();

        background = new Sprite();
        background.setRegion(new Texture("data/splash_screen.png"));
        background.setPosition(0, 0);
        background.setSize(Gfx._DESKTOP_WIDTH, Gfx._DESKTOP_HEIGHT);

        createLoadingLetters();

        AppConfig.isShowingSplashScreen = true;

        stopWatch = StopWatch.start();
    }

    public void update()
    {
        if (stopWatch.time(TimeUnit.MILLISECONDS) > 1000)
        {
            AppConfig.isShowingSplashScreen = false;
        }
    }

    public void draw()
    {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        background.draw(spriteBatch);
        spriteBatch.end();
    }

    private void createLoadingLetters()
    {
        loadingLetters = new Sprite[StringUtils.length("LOADING")];

        TextureRegion[] regions = new TextureRegion[loadingLetters.length];
        GfxUtils.splitRegion
            (
                new TextureRegion(new Texture("data/loading_letters.png")),
                loadingLetters.length,
                regions,
                app
            );

        for (int i=0; i<loadingLetters.length; i++)
        {
            loadingLetters[i] = new Sprite(regions[i]);
            loadingLetters[i].setPosition((415 + (i * 64)), (720 - 494));
        }

        setLettersUpdater();
    }

    private void setLettersUpdater()
    {
        lettersUpdater = new Thread(() ->
        {
            for (Sprite sprite : loadingLetters)
            {
                sprite.draw(spriteBatch);
            }
        });

        lettersUpdater.start();
    }

    @SuppressWarnings({"EmptyMethod", "unused"})
    public void stopLettersUpdater() throws InterruptedException
    {
        try
        {
            lettersUpdater.interrupt();
            lettersUpdater.join();

            for (int i = 0; i < loadingLetters.length; i++)
            {
                loadingLetters[i] = null;
            }

            loadingLetters = null;
        }
        catch (InterruptedException ie)
        {
            //
            // Does nothing
        }
    }

    @Override
    public void dispose()
    {
        spriteBatch.dispose();
        spriteBatch = null;
        background  = null;
    }
}
