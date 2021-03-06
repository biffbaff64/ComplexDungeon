/*
 *  Copyright 08/06/2018 Red7Projects.
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

package com.red7projects.dungeon.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.Sfx;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.input.buttons.ButtonID;
import com.red7projects.dungeon.input.buttons.GameButton;
import com.red7projects.dungeon.maths.SimpleVec2F;

public class PausePanel extends BasicPanel implements Disposable
{
    public GameButton buttonMusicVolume;
    public GameButton buttonFXVolume;
    public GameButton buttonHome;
    public GameButton buttonResume;

    private final App app;

    private static final int _EXIT       = 0;
    private static final int _EXIT_PAUSE = 1;
    private static final int _MUSIC      = 2;
    private static final int _FX         = 3;

    private static final int[][] displayPos =
        {
            {489, (Gfx._HUD_HEIGHT - 350), 302, 45},     // Main Menu
            {552, (Gfx._HUD_HEIGHT - 412), 175, 39},     // Exit Pause
            {523, (Gfx._HUD_HEIGHT - 475), 230, 39},     // Music
            {570, (Gfx._HUD_HEIGHT - 541), 142, 39},     // FX
        };

    private Texture background;

    public PausePanel(App _app)
    {
        this.app = _app;

        setup();
    }

    @Override
    public void open()
    {
        AppConfig.pause();
    }

    @Override
    public void close()
    {
        AppConfig.unPause();
    }

    public void update()
    {
        if (buttonMusicVolume.isPressed() && (Sfx.inst().getMusicVolume() > 0))
        {
            Sfx.inst().saveMusicVolume();
            Sfx.inst().setMusicVolume(0);
        }
        else
        {
            if (!buttonMusicVolume.isPressed() && (Sfx.inst().getMusicVolume() == 0))
            {
                Sfx.inst().setMusicVolume(Sfx.inst().getMusicVolumeSave());
            }
        }

        if (buttonFXVolume.isPressed() && (Sfx.inst().getFXVolume() > 0))
        {
            Sfx.inst().saveFXVolume();
            Sfx.inst().setFXVolume(0);
        }
        else
        {
            if (!buttonFXVolume.isPressed() && (Sfx.inst().getFXVolume() == 0))
            {
                Sfx.inst().setFXVolume(Sfx.inst().getFXVolumeSave());
            }
        }

        if (buttonResume.isPressed())
        {
            app.getHud().buttonPause.press();
            buttonResume.release();
        }

        if (buttonHome.isPressed())
        {
            setQuitToTitle();

            AppConfig.forceQuitToMenu = true;
        }
    }

    public void draw(float originX, float originY)
    {
        app.spriteBatch.draw(background, originX, originY);

        buttonMusicVolume.draw();
        buttonFXVolume.draw();
        buttonHome.draw();
        buttonResume.draw();
    }

    @Override
    public void setup()
    {
        UIUtils.setup(app);
        SimpleVec2F offset = UIUtils.getHUDOffset();

        background = app.assets.loadSingleAsset("data/pause_panel_background.png", Texture.class);

        buttonHome = new GameButton
            (
                app.assets.getButtonsAtlas().findRegion("buttonHome"),
                app.assets.getButtonsAtlas().findRegion("buttonHomePressed"),
                (int) offset.getX() + displayPos[_EXIT][0],
                (int) offset.getY() + displayPos[_EXIT][1],
                ButtonID._DEFAULT,
                app
            );

        buttonMusicVolume = new GameButton
            (
                app.assets.getButtonsAtlas().findRegion("buttonMusicOn"),
                app.assets.getButtonsAtlas().findRegion("buttonMusicOff"),
                (int) offset.getX() + displayPos[_MUSIC][0],
                (int) offset.getY() + displayPos[_MUSIC][1],
                ButtonID._DEFAULT,
                app
            );

        buttonFXVolume = new GameButton
            (
                app.assets.getButtonsAtlas().findRegion("buttonFXOn"),
                app.assets.getButtonsAtlas().findRegion("buttonFXOff"),
                (int) offset.getX() + displayPos[_FX][0],
                (int) offset.getY() + displayPos[_FX][1],
                ButtonID._DEFAULT,
                app
            );

        buttonResume = new GameButton
            (
                app.assets.getButtonsAtlas().findRegion("buttonResumeOn"),
                app.assets.getButtonsAtlas().findRegion("buttonResumeOff"),
                (int) offset.getX() + displayPos[_EXIT_PAUSE][0],
                (int) offset.getY() + displayPos[_EXIT_PAUSE][1],
                ButtonID._DEFAULT,
                app
            );

        buttonHome.setSize(displayPos[_EXIT][2], displayPos[_EXIT][3]);
        buttonMusicVolume.setSize(displayPos[_MUSIC][2], displayPos[_MUSIC][3]);
        buttonFXVolume.setSize(displayPos[_FX][2], displayPos[_FX][3]);
        buttonResume.setSize(displayPos[_EXIT_PAUSE][2], displayPos[_EXIT_PAUSE][3]);
    }

    private void setQuitToTitle()
    {
        AppConfig.quitToMainMenu = true;

        if (app.getPlayer() != null)
        {
            app.getPlayer().setAction(Actions._DEAD);
        }

        app.gameProgress.toMinimum();
        app.getHud().getHealthBar().setToMinimum();

        buttonHome.release();
        app.getHud().buttonPause.press();
    }

    @Override
    public void dispose()
    {
        app.assets.unloadAsset("data/pause_panel_background.png");
        background = null;

        buttonHome.dispose();
        buttonMusicVolume.dispose();
        buttonFXVolume.dispose();
        buttonResume.dispose();

        buttonHome        = null;
        buttonMusicVolume = null;
        buttonFXVolume    = null;
        buttonResume      = null;
    }
}
