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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.config.Settings;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.Sfx;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.input.UIButtons;
import com.red7projects.dungeon.input.buttons.Switch;
import com.red7projects.dungeon.ui.Scene2DUtils;
import com.red7projects.dungeon.ui.UIPage;
import com.red7projects.dungeon.utils.development.Developer;
import com.red7projects.dungeon.utils.logging.StopWatch;
import com.red7projects.dungeon.utils.logging.Trace;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("WeakerAccess")
public class MenuPage implements UIPage, Disposable
{
    public Switch buttonStart;
    public Switch buttonOptions;
    public Switch buttonGoogle;
    public Switch buttonExit;

    private       Texture       background;
    private       Texture       foreground;
    private       Array<Switch> buttons;
    private       StopWatch     stopWatch;
    private final App           app;
    private       Image         decoration;
    private       Image         buttonBar;
    private       Label         javaHeapLabel;
    private       Label         nativeHeapLabel;

    private ImageButton imageButton1Player;
    private ImageButton imageButtonOptions;
    private ImageButton imageButtonExit;
    private ImageButton imageButtonGoogle;

    private float buttonBarDelay;
    private int[] buttonBarYPos;
    private int buttonBarIndex;

    MenuPage(App _app)
    {
        this.app = _app;

        foreground = app.assets.loadSingleAsset("data/title_background.png", Texture.class);
        background = app.assets.loadSingleAsset("data/night_sky.png", Texture.class);

        addMenu();
        addClickListeners();

        buttonStart    = new Switch();
        buttonOptions  = new Switch();
        buttonExit     = new Switch();
        buttonGoogle   = new Switch();

        buttons = new Array<>();
        buttons.add(buttonStart);
        buttons.add(buttonOptions);
        buttons.add(buttonExit);
        buttons.add(buttonGoogle);

        this.stopWatch = StopWatch.start();

        buttonBarYPos = new int[3];
        buttonBarYPos[0] = (int) imageButton1Player.getY();
        buttonBarYPos[1] = (int) imageButtonOptions.getY();
        buttonBarYPos[2] = (int) imageButtonExit.getY();
    }

    @Override
    public void reset()
    {
        buttonBarDelay = 0;
        buttonBarIndex = 0;
    }

    @Override
    public void show()
    {
        showItems(true);

        stopWatch.reset();
        reset();
    }

    @Override
    public void hide()
    {
        showItems(false);
    }

    @Override
    public boolean update()
    {
        if ((buttonBarDelay += Gdx.graphics.getDeltaTime()) >= 0.10f)
        {
            if (UIButtons.controllerUpPressed)
            {
                if (--buttonBarIndex < 0)
                {
                    buttonBarIndex = 2;
                }
            }
            else if (UIButtons.controllerDownPressed)
            {
                if (++buttonBarIndex > 2)
                {
                    buttonBarIndex = 0;
                }
            }

            buttonBar.setY(buttonBarYPos[buttonBarIndex]);
            buttonBarDelay = 0;
        }

        updateGoogleButton();

        return false;
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        if (background != null)
        {
            spriteBatch.draw(background, 0, 0);
        }

        if (foreground != null)
        {
            spriteBatch.draw(foreground, 0, 0);
        }

        menuPageDebug();
    }

    private void addMenu()
    {
        Scene2DUtils scene2DUtils = new Scene2DUtils(app);

        imageButton1Player  = scene2DUtils.addButton("buttonStart", "buttonStart_pressed", 924, (Gfx._VIEW_HEIGHT - 899));
        imageButtonOptions  = scene2DUtils.addButton("buttonOptions", "buttonOptions_pressed", 1050, (Gfx._VIEW_HEIGHT - 1063));
        imageButtonExit     = scene2DUtils.addButton("buttonExit", "buttonExit_pressed", 1150, (Gfx._VIEW_HEIGHT - 1218));

        imageButton1Player.setZIndex(1);
        imageButtonOptions.setZIndex(1);
        imageButtonExit.setZIndex(1);

        if (Developer.isDevMode() && app.settings.isEnabled(Settings._MENU_HEAPS))
        {
            Trace.dbg("Adding Heap Usage debug...");

            javaHeapLabel   = scene2DUtils.addLabel("JAVA HEAP: ", 40, (Gfx._VIEW_HEIGHT - 400), 20, Color.WHITE, GameAssets._PRO_WINDOWS_FONT);
            nativeHeapLabel = scene2DUtils.addLabel("NATIVE HEAP: ", 40, (Gfx._VIEW_HEIGHT - 425), 20, Color.WHITE, GameAssets._PRO_WINDOWS_FONT);

            app.stage.addActor(javaHeapLabel);
            app.stage.addActor(nativeHeapLabel);
            javaHeapLabel.setZIndex(1);
            nativeHeapLabel.setZIndex(1);
        }

        buttonBar = scene2DUtils.makeObjectsImage("menu_glow_green01");
        buttonBar.setPosition(640, imageButton1Player.getY());
        buttonBar.setZIndex(0);
        app.stage.addActor(buttonBar);

        addDateSpecificItems(scene2DUtils);
    }

    private void addDateSpecificItems(Scene2DUtils scene2DUtils)
    {
        Date     date     = new Date(TimeUtils.millis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (calendar.get(Calendar.MONTH) == Calendar.NOVEMBER)
        {
            if (calendar.get(Calendar.DAY_OF_MONTH) == 11)
            {
                decoration = scene2DUtils.makeObjectsImage("poppy");
                decoration.setPosition((Gfx._VIEW_WIDTH - 100), 40);
                app.stage.addActor(decoration);
            }
        }
        else
        {
            if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER)
            {
                switch (calendar.get(Calendar.DAY_OF_MONTH))
                {
                    case 24:
                    case 25:
                    case 26:
                    {
                        decoration = scene2DUtils.makeObjectsImage("xmas_tree");
                        decoration.setPosition((Gfx._VIEW_WIDTH - 200), 40);
                        app.stage.addActor(decoration);
                    }
                    break;

                    default:
                        break;
                }
            }
        }
    }

    private void addClickListeners()
    {
        imageButton1Player.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                Sfx.inst().startSound(Sfx.SFX_BEEP);

                buttonStart.press();
            }
        });

        imageButtonOptions.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                Sfx.inst().startSound(Sfx.SFX_BEEP);

                buttonOptions.press();
            }
        });

        imageButtonExit.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                Sfx.inst().startSound(Sfx.SFX_BEEP);

                buttonExit.press();
            }
        });
    }

    /**
     * Creates the Google button if needed.
     * Also removes the button if the game
     * has signed in to google play services.
     */
    private void updateGoogleButton()
    {
        if ((imageButtonGoogle == null) && !app.googleServices.isSignedIn())
        {
            createGoogleButton();
        }

        if ((imageButtonGoogle != null) && app.googleServices.isSignedIn())
        {
            imageButtonGoogle.addAction(Actions.removeActor());
            imageButtonGoogle = null;
        }
    }

    /**
     * Create the 'Sign in with Google' button.
     * This button will be shown if auto sign-in
     * fails, allowing the player to manually sign
     * in to Google Play Services.
     */
    private void createGoogleButton()
    {
        if (app.googleServices.isEnabled() && !app.googleServices.isSignedIn())
        {
            Scene2DUtils scene2DUtils = new Scene2DUtils(app);

            imageButtonGoogle = scene2DUtils.addButton
                    (
                            "btn_google_signin_dark",
                            "btn_google_signin_dark_pressed",
                            1040,
                            30
                    );

            imageButtonGoogle.setZIndex(1);

            imageButtonGoogle.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    Sfx.inst().startSound(Sfx.SFX_BEEP);

                    buttonGoogle.press();
                }
            });
        }
    }

    /**
     * Sets visibility of all rlevant actors.
     *
     * @param _visible boolean visibility setting.
     */
    private void showItems(boolean _visible)
    {
        buttonBar.setVisible(_visible);

        imageButton1Player.setVisible(_visible);
        imageButtonOptions.setVisible(_visible);
        imageButtonExit.setVisible(_visible);

        if (decoration != null)
        {
            decoration.setVisible(_visible);
        }

        if (imageButtonGoogle != null)
        {
            imageButtonGoogle.setVisible(_visible);
        }

        if (Developer.isDevMode() && app.settings.isEnabled(Settings._MENU_HEAPS))
        {
            if (javaHeapLabel != null)
            {
                javaHeapLabel.setVisible(_visible);
            }

            if (nativeHeapLabel != null)
            {
                nativeHeapLabel.setVisible(_visible);
            }
        }
    }

    @Override
    public void dispose()
    {
        imageButton1Player.addAction(Actions.removeActor());
        imageButtonOptions.addAction(Actions.removeActor());
        imageButtonExit.addAction(Actions.removeActor());

        imageButton1Player  = null;
        imageButtonOptions  = null;
        imageButtonExit     = null;

        if (Developer.isDevMode() && app.settings.isEnabled(Settings._MENU_HEAPS))
        {
            if (javaHeapLabel != null)
            {
                javaHeapLabel.addAction(Actions.removeActor());
                javaHeapLabel = null;
            }

            if (nativeHeapLabel != null)
            {
                nativeHeapLabel.addAction(Actions.removeActor());
                nativeHeapLabel = null;
            }
        }

        buttonBar.addAction(Actions.removeActor());
        buttonBar = null;

        if (decoration != null)
        {
            decoration.addAction(Actions.removeActor());
            decoration = null;
        }

        app.assets.unloadAsset("data/title_background.png");
        app.assets.unloadAsset("data/night_sky.png");

        foreground = null;
        stopWatch  = null;

        if (buttons != null)
        {
            buttons.clear();
            buttons = null;
        }
    }

    private void menuPageDebug()
    {
        if (Developer.isDevMode() && app.settings.isEnabled(Settings._MENU_HEAPS))
        {
            if (javaHeapLabel != null)
            {
                javaHeapLabel.setText
                    (
                        String.format
                            (
                                Locale.UK,
                                "JAVA HEAP: %3.2fMB",
                                ((((float) Gdx.app.getJavaHeap()) / 1024) / 1024)
                            )
                    );
            }

            if (nativeHeapLabel != null)
            {
                nativeHeapLabel.setText
                    (
                        String.format
                            (
                                Locale.UK,
                                "NATIVE HEAP: %3.2fMB",
                                ((((float) Gdx.app.getNativeHeap()) / 1024) / 1024)
                            )
                    );
            }
        }
    }
}
