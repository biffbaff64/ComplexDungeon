/*
 *  Copyright 24/04/2018 Red7Projects.
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

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.Sfx;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.game.StateManager;
import com.red7projects.dungeon.graphics.effects.FadeEffect;
import com.red7projects.dungeon.input.UIButtons;
import com.red7projects.dungeon.input.buttons.GDXButton;
import com.red7projects.dungeon.input.buttons.GameButton;

@SuppressWarnings({"WeakerAccess"})
public abstract class AbstractBaseScreen extends ScreenAdapter implements Disposable, BaseScreen
{
    protected final App app;
    protected final StateManager flowState;

    public AbstractBaseScreen(App _app)
    {
        super();

        this.flowState  = new StateManager();
        this.app        = _app;
    }

    @Override
    public void update()
    {
        Sfx.inst().update();

        if (FadeEffect.isActive)
        {
            if (FadeEffect.update())
            {
                FadeEffect.end();
                flowState.set(StateID._STATE_GAME);
            }
        }
        else
        {
            if (AppConfig.gameScreenActive)
            {
                app.mapData.update();
                app.gameListener.update();
            }

            //
            // Update any buttons that are animating/Scaling etc
            for (GDXButton button : app.inputManager.gameButtons)
            {
                button.update();
            }
        }
    }

    @Override
    public void triggerFadeIn()
    {
        FadeEffect.triggerFadeIn();
        flowState.set(StateID._STATE_FADE_IN);
    }

    @Override
    public void triggerFadeOut()
    {
        FadeEffect.triggerFadeOut();
        flowState.set(StateID._STATE_FADE_OUT);
    }

    @Override
    public void show()
    {
        flowState.set(StateID._STATE_GAME);

        loadImages();

        UIButtons.releaseAll();
    }

    @Override
    public void hide()
    {
    }

    @Override
    public void render(float delta)
    {
        if (!AppConfig.isShowingSplashScreen)
        {
            app.baseRenderer.render();
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch, OrthographicCamera camera)
    {
    }

    @Override
    public void resize(int width, int height)
    {
        app.baseRenderer.tiledGameCamera.resizeViewport(width, height, true);
        app.baseRenderer.spriteGameCamera.resizeViewport(width, height, true);
        app.baseRenderer.hudGameCamera.resizeViewport(width, height, true);
    }

    @Override
    public void pause()
    {
        app.preferences.prefs.flush();
    }

    @Override
    public void resume()
    {
    }

    @Override
    public void dispose()
    {
    }

    @Override
    public void loadImages()
    {
    }
}
