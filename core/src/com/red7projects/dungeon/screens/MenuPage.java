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
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.config.Settings;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.Sfx;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.game.StateManager;
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
    public Switch buttonHiscores;
    public Switch buttonOptions;
    public Switch buttonCredits;
    public Switch buttonExit;
    public Switch buttonGoogle;

    private Texture         foreground;
    private Array<Switch>   buttons;
    private StopWatch       stopWatch;
    public  StateManager    menuState;
    private App             app;
    private Image           decoration;
    private Image           buttonBar;
    private Label           javaHeapLabel;
    private Label           nativeHeapLabel;

    private static int _START       = 0;
    private static int _HISCORES    = 1;
    private static int _OPTIONS     = 2;
    private static int _CREDITS     = 3;
    private static int _EXIT        = 4;
    private static int _GOOGLE      = 5;
    private static int _NUM_BUTTONS = 6;

    private ImageButton[] imageButtons;

    private float buttonBarDelay;
    private int[] buttonBarYPos;
    private int buttonBarIndex;
    private int timerDelay;
    private boolean flashState;
    private float flashInterval;
    private int flashIndex;

    MenuPage(App _app)
    {
        this.app = _app;

        foreground = app.assets.loadSingleAsset("data/title_background.png", Texture.class);

        addMenu();
        addClickListeners();

        buttonStart    = new Switch();
        buttonHiscores = new Switch();
        buttonOptions  = new Switch();
        buttonCredits  = new Switch();
        buttonExit     = new Switch();
        buttonGoogle   = new Switch();

        buttons = new Array<>();
        buttons.add(buttonStart);
        buttons.add(buttonHiscores);
        buttons.add(buttonOptions);
        buttons.add(buttonCredits);
        buttons.add(buttonExit);
        buttons.add(buttonGoogle);

        menuState = new StateManager();

        buttonBarYPos = new int[_NUM_BUTTONS];
        buttonBarYPos[0] = (int) imageButtons[_START].getY();
        buttonBarYPos[1] = (int) imageButtons[_HISCORES].getY();
        buttonBarYPos[2] = (int) imageButtons[_OPTIONS].getY();
        buttonBarYPos[3] = (int) imageButtons[_CREDITS].getY();
        buttonBarYPos[4] = (int) imageButtons[_EXIT].getY();
    }

    @Override
    public void reset()
    {
        buttonBarDelay = 0;
        buttonBarIndex = 0;

        menuState.set(StateID._STATE_MENU_UPDATE);
    }

    @Override
    public void show()
    {
        showItems(true);

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
        if (menuState.get().equals(StateID._STATE_MENU_UPDATE))
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
        }

        return false;
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        if (foreground != null)
        {
            spriteBatch.draw(foreground, 0, 0);
        }

        showMenuPageDebug();
    }

    private void addMenu()
    {
        Scene2DUtils scene2DUtils = new Scene2DUtils(app);

        imageButtons = new ImageButton[_NUM_BUTTONS];

        imageButtons[_START]    = scene2DUtils.addButton("buttonStart", "buttonStart_pressed", 980, (Gfx._VIEW_HEIGHT - 720));
        imageButtons[_HISCORES] = scene2DUtils.addButton("buttonHiscores", "buttonHiscores_pressed", 1050, (Gfx._VIEW_HEIGHT - 840));
        imageButtons[_OPTIONS]  = scene2DUtils.addButton("buttonOptions", "buttonOptions_pressed", 1060, (Gfx._VIEW_HEIGHT - 960));
        imageButtons[_CREDITS]  = scene2DUtils.addButton("buttonCredits", "buttonCredits_pressed", 1070, (Gfx._VIEW_HEIGHT - 1080));
        imageButtons[_EXIT]     = scene2DUtils.addButton("buttonExit", "buttonExit_pressed", 1150, (Gfx._VIEW_HEIGHT - 1200));

        imageButtons[_START].setZIndex(2);
        imageButtons[_HISCORES].setZIndex(2);
        imageButtons[_OPTIONS].setZIndex(2);
        imageButtons[_CREDITS].setZIndex(2);
        imageButtons[_EXIT].setZIndex(2);

        if (Developer.isDevMode() && app.settings.isEnabled(Settings._MENU_HEAPS))
        {
            Trace.dbg("Adding Heap Usage debug...");

            javaHeapLabel   = scene2DUtils.addLabel("JAVA HEAP: ", 40, (Gfx._VIEW_HEIGHT - 400), 20, Color.WHITE, GameAssets._PRO_WINDOWS_FONT);
            nativeHeapLabel = scene2DUtils.addLabel("NATIVE HEAP: ", 40, (Gfx._VIEW_HEIGHT - 425), 20, Color.WHITE, GameAssets._PRO_WINDOWS_FONT);

            app.stage.addActor(javaHeapLabel);
            app.stage.addActor(nativeHeapLabel);
            javaHeapLabel.setZIndex(2);
            nativeHeapLabel.setZIndex(2);
        }

        buttonBar = scene2DUtils.makeObjectsImage("menu_arrows_green");
        buttonBar.setPosition(640, imageButtons[_START].getY());
        buttonBar.setZIndex(1);
        buttonBar.setTouchable(Touchable.disabled);
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
    }

    private void addClickListeners()
    {
        imageButtons[_START].addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                Sfx.inst().startSound(Sfx.SFX_BEEP);

                buttonStart.press();
                buttonBar.setVisible(false);

                setFlashing(_START);
            }
        });

        imageButtons[_HISCORES].addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                Sfx.inst().startSound(Sfx.SFX_BEEP);

                buttonHiscores.press();
                buttonBar.setVisible(false);

                setFlashing(_HISCORES);
            }
        });

        imageButtons[_OPTIONS].addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                Sfx.inst().startSound(Sfx.SFX_BEEP);

                buttonOptions.press();
                buttonBar.setVisible(false);

                setFlashing(_OPTIONS);
            }
        });

        imageButtons[_CREDITS].addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                Sfx.inst().startSound(Sfx.SFX_BEEP);

                buttonCredits.press();
                buttonBar.setVisible(false);

                setFlashing(_CREDITS);
            }
        });

        imageButtons[_EXIT].addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                Sfx.inst().startSound(Sfx.SFX_BEEP);

                buttonExit.press();
                buttonBar.setVisible(false);

                setFlashing(_EXIT);
            }
        });
    }

    private void setFlashing(int button)
    {
//        menuState.set(StateID._STATE_FLASHING);
//        stopWatch = StopWatch.start();
//        timerDelay = 5000;
//        flashState = true;
//        flashInterval = 0;
//        flashIndex = button;
    }

    /**
     * Creates the Google button if needed.
     * Also removes the button if the game
     * has signed in to google play services.
     */
    private void updateGoogleButton()
    {
        if (AppConfig.isAndroidApp())
        {
            if ((imageButtons[_GOOGLE] == null) && !app.googleServices.isSignedIn())
            {
                createGoogleButton();
            }

            if ((imageButtons[_GOOGLE] != null) && app.googleServices.isSignedIn())
            {
                imageButtons[_GOOGLE].addAction(Actions.removeActor());
                imageButtons[_GOOGLE] = null;
            }
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
        if (AppConfig.isAndroidApp())
        {
            if (app.googleServices.isEnabled() && !app.googleServices.isSignedIn())
            {
                Scene2DUtils scene2DUtils = new Scene2DUtils(app);

                imageButtons[_GOOGLE] = scene2DUtils.addButton
                    (
                        "btn_google_signin_dark",
                        "btn_google_signin_dark_pressed",
                        1040,
                        30
                    );

                imageButtons[_GOOGLE].setZIndex(1);

                imageButtons[_GOOGLE].addListener(new ClickListener()
                {
                    public void clicked(InputEvent event, float x, float y)
                    {
                        Sfx.inst().startSound(Sfx.SFX_BEEP);

                        buttonGoogle.press();
                    }
                });
            }
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

        imageButtons[_START].setVisible(_visible);
        imageButtons[_HISCORES].setVisible(_visible);
        imageButtons[_OPTIONS].setVisible(_visible);
        imageButtons[_CREDITS].setVisible(_visible);
        imageButtons[_EXIT].setVisible(_visible);

        if (decoration != null)
        {
            decoration.setVisible(_visible);
        }

        if (imageButtons[_GOOGLE] != null)
        {
            imageButtons[_GOOGLE].setVisible(_visible);
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
        imageButtons[_START].addAction(Actions.removeActor());
        imageButtons[_HISCORES].addAction(Actions.removeActor());
        imageButtons[_OPTIONS].addAction(Actions.removeActor());
        imageButtons[_CREDITS].addAction(Actions.removeActor());
        imageButtons[_EXIT].addAction(Actions.removeActor());

        imageButtons[_START] = null;
        imageButtons[_HISCORES] = null;
        imageButtons[_OPTIONS] = null;
        imageButtons[_CREDITS] = null;
        imageButtons[_EXIT] = null;

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

    private void showMenuPageDebug()
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
