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

package com.red7projects.dungeon.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.input.buttons.GameButton;
import com.red7projects.dungeon.input.objects.ControllerType;
import com.red7projects.dungeon.physics.Movement;

@SuppressWarnings("WeakerAccess")
public class InputManager implements AbstractInputManager
{
    public Array<GameButton> gameButtons;
    public Vector2           mousePosition;
    public Vector2           mouseWorldPosition;
    public Keyboard          keyboard;
    public VirtualJoystick   virtualJoystick;
    public TouchScreen       touchScreen;
    public GameController    gameController;
    public InputMultiplexer  inputMultiplexer;
    public Movement.Dir      currentRegisteredDirection;
    public Movement.Dir      lastRegisteredDirection;
    public float             _horizontalValue;
    public float             _verticalValue;

    protected App app;

    public InputManager(App _app)
    {
        this.app = _app;

        setup();
    }

    @Override
    public boolean setup()
    {
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(app.stage);

        currentRegisteredDirection  = Movement.Dir._STILL;
        lastRegisteredDirection     = Movement.Dir._STILL;

        if (AppConfig.isAndroidApp() || AppConfig.isAndroidOnDesktop())
        {
            virtualJoystick = new VirtualJoystick(app);

            virtualJoystick.create();
            virtualJoystick.addToStage();
        }

        if (AppConfig.isDesktopApp())
        {
            gameController = new GameController(app);

            if (!gameController.setup())
            {
                gameController = null;

                Pixmap pixmap = new Pixmap(Gdx.files.internal("data/crosshairs.png"));
                Cursor cursor = Gdx.graphics.newCursor(pixmap, (pixmap.getWidth() / 2), (pixmap.getHeight() / 2));
                Gdx.graphics.setCursor(cursor);
                pixmap.dispose();
            }
        }

        if (AppConfig.isDesktopApp() || AppConfig.isAndroidOnDesktop())
        {
            mousePosition      = new Vector2();
            mouseWorldPosition = new Vector2();
            keyboard           = new Keyboard(app);

            inputMultiplexer.addProcessor(keyboard);
        }

        touchScreen = new TouchScreen(app);
        gameButtons = new Array<>();

        UIButtons.setup(app);

        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        Gdx.input.setCatchKey(Input.Keys.MENU, true);
        Gdx.input.setInputProcessor(inputMultiplexer);

        return true;
    }

    @Override
    public float getControllerXPercentage()
    {
        float xPercent = 0.0f;

        if (app.getHud() != null)
        {
            if (AppConfig.controlMode == ControllerType._VIRTUAL)
            {
                if (virtualJoystick != null)
                {
                    xPercent = virtualJoystick.getXPercent();
                }
            }
            else
            {
                if (AppConfig.controlMode == ControllerType._EXTERNAL)
                {
                    xPercent = _horizontalValue;
                }
                else
                {
                    if (AppConfig.controlMode == ControllerType._KEYBOARD)
                    {
                        keyboard.translateXPercent();

                        xPercent = _horizontalValue;
                    }
                }
            }
        }

        return xPercent;
    }

    @Override
    public float getControllerYPercentage()
    {
        float yPercent = 0.0f;

        if (app.getHud() != null)
        {
            if (AppConfig.controlMode == ControllerType._VIRTUAL)
            {
                if (virtualJoystick != null)
                {
                    yPercent = virtualJoystick.getYPercent();
                }
            }
            else
            {
                if (AppConfig.controlMode == ControllerType._EXTERNAL)
                {
                    yPercent = _verticalValue;

                    if ("PC/PS3/Android".equals(AppConfig.usedController))
                    {
                        yPercent *= -1;
                    }
                }
                else
                {
                    if (AppConfig.controlMode == ControllerType._KEYBOARD)
                    {
                        keyboard.translateYPercent();

                        yPercent = _verticalValue;
                    }
                }
            }
        }

        return yPercent;
    }
}
