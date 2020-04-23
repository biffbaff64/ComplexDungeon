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
import com.red7projects.dungeon.input.buttons.GDXButton;
import com.red7projects.dungeon.input.objects.ControllerType;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.utils.logging.Trace;

@SuppressWarnings("WeakerAccess")
public class InputManager
{
    public Array<GDXButton>  gameButtons;
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

    public boolean setup()
    {
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(app.stage);

        currentRegisteredDirection  = Movement.Dir._STILL;
        lastRegisteredDirection     = Movement.Dir._STILL;

        //
        // Initialise virtual controllers if enabled.
        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            Trace.dbg("Initialising _VIRTUAL Controller Type");

            virtualJoystick = new VirtualJoystick(app);
            virtualJoystick.create();
            virtualJoystick.addToStage();
        }

        //
        // Redefine the mouse cursor as crosshairs
        // if Mouse Control is required.
        if (AppConfig.availableInputs.contains(ControllerType._MOUSE, true))
        {
            Trace.dbg("Initialising _MOUSE Controller Type");

            Pixmap pixmap = new Pixmap(Gdx.files.internal("data/crosshairs.png"));
            Cursor cursor = Gdx.graphics.newCursor(pixmap, (pixmap.getWidth() / 2), (pixmap.getHeight() / 2));
            Gdx.graphics.setCursor(cursor);
            pixmap.dispose();
        }

        //
        // Initialise external controllers if enabled.
        if (AppConfig.availableInputs.contains(ControllerType._EXTERNAL, true))
        {
            Trace.dbg("Initialising _EXTERNAL Controller Type");

            gameController = new GameController(app);

            if (!gameController.setup())
            {
                gameController = null;
            }
        }

        mousePosition       = new Vector2();
        mouseWorldPosition  = new Vector2();
        keyboard            = new Keyboard(app);
        touchScreen         = new TouchScreen(app);
        gameButtons         = new Array<>();

        inputMultiplexer.addProcessor(keyboard);

        UIButtons.setup(app);

        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        Gdx.input.setCatchKey(Input.Keys.MENU, true);
        Gdx.input.setInputProcessor(inputMultiplexer);

        return true;
    }

    public float getControllerXPercentage()
    {
        float xPercent = 0.0f;

        if (app.getHud() != null)
        {
            if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
            {
                if (virtualJoystick != null)
                {
                    xPercent = virtualJoystick.getXPercent();
                }
            }
            else
            {
                if (AppConfig.availableInputs.contains(ControllerType._EXTERNAL, true))
                {
                    xPercent = _horizontalValue;
                }
                else
                {
                    if (AppConfig.availableInputs.contains(ControllerType._KEYBOARD, true))
                    {
                        keyboard.translateXPercent();

                        xPercent = app.getPlayer().direction.getX();
                    }
                }
            }
        }

        return xPercent;
    }

    public float getControllerYPercentage()
    {
        float yPercent = 0.0f;

        if (app.getHud() != null)
        {
            if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
            {
                if (virtualJoystick != null)
                {
                    yPercent = virtualJoystick.getYPercent();
                }
            }
            else
            {
                if (AppConfig.availableInputs.contains(ControllerType._EXTERNAL, true))
                {
                    yPercent = _verticalValue;

                    switch (gameController.controller.getName())
                    {
                        case "PC/PS3/Android":
                        case "Controller (Inno GamePad..)":
                        {
                            yPercent *= -1;
                        }
                        break;

                        default:
                            break;

                    }
                }
                else
                {
                    if (AppConfig.availableInputs.contains(ControllerType._KEYBOARD, true))
                    {
                        keyboard.translateYPercent();

                        yPercent = app.getPlayer().direction.getY();
                    }
                }
            }
        }

        return yPercent;
    }
}
