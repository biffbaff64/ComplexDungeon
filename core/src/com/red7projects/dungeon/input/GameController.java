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

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.input.objects.*;
import com.red7projects.dungeon.utils.logging.Trace;

@SuppressWarnings("WeakerAccess")
public class GameController implements ControllerListener, InputProcessor
{
    public Controller controller;

    protected App app;

    public GameController(App _app)
    {
        this.app = _app;
    }

    public boolean setup()
    {
        AppConfig.controllersFitted = false;
        AppConfig.gameButtonsReady  = false;

        addExternalController();

        if (!AppConfig.controllersFitted)
        {
            if (Controllers.getControllers().size > 0)
            {
                Trace.__FILE_FUNC("Controllers.getControllers().size: " + Controllers.getControllers().size);

                for (Controller _controller : new Array.ArrayIterator<>(Controllers.getControllers()))
                {
                    Trace.dbg(_controller.getName());
                }

                Controllers.getControllers().clear();
            }
            else
            {
                Trace.__FILE_FUNC("No External Controller found");
            }

            Controllers.clearListeners();
        }

        return AppConfig.controllersFitted;
    }

    public void addExternalController()
    {
        try
        {
            if (Controllers.getControllers().size > 0)
            {
                controller = Controllers.getControllers().first();

                if (controller != null)
                {
                    Trace.__FILE_FUNC("Controller [" + controller.getName() + "] found");

                    AppConfig.controllersFitted = true;

                    createControllerMap();

                    controller.addListener(this);

                    app.inputManager.inputMultiplexer.addProcessor(this);

                    Trace.dbg("Controller added");
                }
            }
            else
            {
                Trace.dbg("Controllers.getControllers().size: ", Controllers.getControllers().size);
            }
        }
        catch (NullPointerException npe)
        {
            Trace.__FILE_FUNC("::Failure");

            disableExternalControllers();

            if (AppConfig.isAndroidApp() || AppConfig.isAndroidOnDesktop())
            {
                Trace.dbg("::Switched to _VIRTUAL");

                AppConfig.controlMode   = ControllerType._VIRTUAL;
                AppConfig.controllerPos = ControllerPos._LEFT;
            }
            else
            {
                Trace.dbg("::Switched to _KEYBOARD");

                AppConfig.controlMode   = ControllerType._KEYBOARD;
                AppConfig.controllerPos = ControllerPos._HIDDEN;
            }
        }
    }

    public void disableExternalControllers()
    {
        try
        {
            controller = null;

            AppConfig.controllersFitted = false;

            app.inputManager.inputMultiplexer.removeProcessor(this);
        }
        catch (NullPointerException npe)
        {
            Trace.__FILE_FUNC("WARNING!: " + npe.getMessage());
        }
    }

    @Override
    public void connected(Controller controller)
    {
        AppConfig.controllersFitted = true;
    }

    @Override
    public void disconnected(Controller controller)
    {
        AppConfig.controllersFitted = false;
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode)
    {
        UIButtons.controllerButtonCode = buttonCode;

        if (buttonCode == ControllerMap._BUTTON_A)
        {
            UIButtons.controllerAPressed = true;
        }
        else if (buttonCode == ControllerMap._BUTTON_B)
        {
            UIButtons.controllerBPressed = true;
        }
        else if (buttonCode == ControllerMap._BUTTON_X)
        {
            UIButtons.controllerXPressed = true;
        }
        else if (buttonCode == ControllerMap._BUTTON_Y)
        {
            UIButtons.controllerYPressed = true;
        }
        else if (buttonCode == ControllerMap._BUTTON_LB)
        {
            UIButtons.controllerLBPressed = true;
        }
        else if (buttonCode == ControllerMap._BUTTON_RB)
        {
            UIButtons.controllerRBPressed = true;

            if (AppConfig.hudExists)
            {
                if (app.getHud().buttonA != null)
                {
                    app.getHud().buttonA.press();
                }
            }
        }
        else if (buttonCode == ControllerMap._BUTTON_START)
        {
            UIButtons.controllerStartPressed = true;

            if (AppConfig.hudExists)
            {
                if (app.getHud().buttonPause != null)
                {
                    app.getHud().buttonPause.press();
                }
            }
        }
        else if (buttonCode == ControllerMap._BUTTON_BACK)
        {
            UIButtons.controllerBackPressed = true;
        }
        else if (buttonCode == ControllerMap._LEFT_TRIGGER)
        {
            UIButtons.controllerLeftFirePressed = true;
            UIButtons.controllerFirePressed     = true;

            if (AppConfig.hudExists)
            {
                if (app.getHud().buttonB != null)
                {
                    app.getHud().buttonB.press();
                }
            }
        }
        else if (buttonCode == ControllerMap._RIGHT_TRIGGER)
        {
            UIButtons.controllerRightFirePressed = true;
            UIButtons.controllerFirePressed      = true;

            if (AppConfig.hudExists)
            {
                if (app.getHud().buttonB != null)
                {
                    app.getHud().buttonB.press();
                }
            }
        }

        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode)
    {
        UIButtons.controllerButtonCode = -1;

        if (buttonCode == ControllerMap._BUTTON_A)
        {
            UIButtons.controllerAPressed = false;
        }
        else if (buttonCode == ControllerMap._BUTTON_B)
        {
            UIButtons.controllerBPressed = false;
        }
        else if (buttonCode == ControllerMap._BUTTON_X)
        {
            UIButtons.controllerXPressed = false;
        }
        else if (buttonCode == ControllerMap._BUTTON_Y)
        {
            UIButtons.controllerYPressed = false;
        }
        else if (buttonCode == ControllerMap._BUTTON_LB)
        {
            UIButtons.controllerLBPressed = false;
        }
        else if (buttonCode == ControllerMap._BUTTON_RB)
        {
            UIButtons.controllerRBPressed = false;

            if (AppConfig.hudExists)
            {
                if (app.getHud().buttonA != null)
                {
                    app.getHud().buttonA.release();
                }
            }
        }
        else if (buttonCode == ControllerMap._BUTTON_START)
        {
            UIButtons.controllerStartPressed = false;
        }
        else if (buttonCode == ControllerMap._BUTTON_BACK)
        {
            UIButtons.controllerBackPressed = false;
        }
        else if (buttonCode == ControllerMap._LEFT_TRIGGER)
        {
            UIButtons.controllerLeftFirePressed = false;
            UIButtons.controllerFirePressed     = false;

            if (AppConfig.hudExists)
            {
                if (app.getHud().buttonB != null)
                {
                    app.getHud().buttonB.release();
                }
            }
        }
        else if (buttonCode == ControllerMap._RIGHT_TRIGGER)
        {
            UIButtons.controllerRightFirePressed = false;
            UIButtons.controllerFirePressed      = false;

            if (AppConfig.hudExists)
            {
                if (app.getHud().buttonB != null)
                {
                    app.getHud().buttonB.release();
                }
            }
        }

        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value)
    {
        UIButtons.controllerAxisCode  = axisCode;
        UIButtons.controllerAxisValue = value;

        if (UIButtons.controllerAxisValue > 1.0f)
        {
            UIButtons.controllerAxisValue = 1.0f;
        }

        if (UIButtons.controllerAxisValue < -1.0f)
        {
            UIButtons.controllerAxisValue = -1.0f;
        }

        if ((axisCode == ControllerMap._AXIS_LEFT_X) || (axisCode == ControllerMap._AXIS_RIGHT_X))
        {
            app.inputManager._horizontalValue = value;

            if (ControllerMap.isInNegativeRange(value))
            {
                UIButtons.controllerLeftPressed = true;

                if (AppConfig.hudExists)
                {
                    if (app.getHud().buttonLeft != null)
                    {
                        app.getHud().buttonLeft.press();
                    }
                }
            }
            else if (ControllerMap.isInPositiveRange(value))
            {
                UIButtons.controllerRightPressed = true;

                if (AppConfig.hudExists)
                {
                    if (app.getHud().buttonRight != null)
                    {
                        app.getHud().buttonRight.press();
                    }
                }
            }
            else
            {
                if (AppConfig.hudExists)
                {
                    if (app.getHud().buttonLeft != null)
                    {
                        app.getHud().buttonLeft.release();
                        app.getHud().buttonRight.release();
                    }
                }

                UIButtons.controllerLeftPressed  = false;
                UIButtons.controllerRightPressed = false;

                UIButtons.controllerAxisCode  = -1;
                UIButtons.controllerAxisValue = 0;
            }
        }
        else if ((axisCode == ControllerMap._AXIS_LEFT_Y) || (axisCode == ControllerMap._AXIS_RIGHT_Y))
        {
            app.inputManager._verticalValue = value;

            if (ControllerMap.isInNegativeRange(value))
            {
                UIButtons.controllerUpPressed = true;

                if (AppConfig.hudExists)
                {
                    if (app.getHud().buttonUp != null)
                    {
                        app.getHud().buttonUp.press();
                    }
                }
            }
            else if (ControllerMap.isInPositiveRange(value))
            {
                UIButtons.controllerDownPressed = true;

                if (AppConfig.hudExists)
                {
                    if (app.getHud().buttonDown != null)
                    {
                        app.getHud().buttonDown.press();
                    }
                }
            }
            else
            {
                if (AppConfig.hudExists)
                {
                    if (app.getHud().buttonUp != null)
                    {
                        app.getHud().buttonUp.release();
                        app.getHud().buttonDown.release();
                    }
                }

                UIButtons.controllerUpPressed   = false;
                UIButtons.controllerDownPressed = false;

                UIButtons.controllerAxisCode  = -1;
                UIButtons.controllerAxisValue = 0;
            }
        }
        else
        {
            UIButtons.controllerUpPressed    = false;
            UIButtons.controllerDownPressed  = false;
            UIButtons.controllerLeftPressed  = false;
            UIButtons.controllerRightPressed = false;

            if (AppConfig.hudExists)
            {
                app.getHud().releaseDirectionButtons();
            }

            UIButtons.controllerAxisCode  = -1;
            UIButtons.controllerAxisValue = 0;
        }

        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value)
    {
        UIButtons.controllerPovDirection = value;

        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value)
    {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value)
    {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value)
    {
        return false;
    }

    public Controller getController()
    {
        return controller;
    }

    private void createControllerMap()
    {
        Trace.__FILE_FUNC();

        ControllerMap._MIN_RANGE          = DefaultControllerMap._MIN_RANGE;
        ControllerMap._MAX_RANGE          = DefaultControllerMap._MAX_RANGE;
        ControllerMap._DEAD_ZONE          = DefaultControllerMap._DEAD_ZONE;
        ControllerMap._AXIS_LEFT_TRIGGER  = DefaultControllerMap._AXIS_LEFT_TRIGGER;
        ControllerMap._AXIS_RIGHT_TRIGGER = DefaultControllerMap._AXIS_RIGHT_TRIGGER;
        ControllerMap._AXIS_LEFT_X        = DefaultControllerMap._AXIS_LEFT_X;
        ControllerMap._AXIS_LEFT_Y        = DefaultControllerMap._AXIS_LEFT_Y;
        ControllerMap._AXIS_RIGHT_X       = DefaultControllerMap._AXIS_RIGHT_X;
        ControllerMap._AXIS_RIGHT_Y       = DefaultControllerMap._AXIS_RIGHT_Y;
        ControllerMap._BUTTON_A           = DefaultControllerMap._BUTTON_A;
        ControllerMap._BUTTON_B           = DefaultControllerMap._BUTTON_B;
        ControllerMap._BUTTON_X           = DefaultControllerMap._BUTTON_X;
        ControllerMap._BUTTON_Y           = DefaultControllerMap._BUTTON_Y;
        ControllerMap._BUTTON_START       = DefaultControllerMap._BUTTON_START;
        ControllerMap._BUTTON_BACK        = DefaultControllerMap._BUTTON_BACK;
        ControllerMap._BUTTON_L3          = DefaultControllerMap._BUTTON_L3;
        ControllerMap._BUTTON_R3          = DefaultControllerMap._BUTTON_R3;
        ControllerMap._BUTTON_LB          = DefaultControllerMap._BUTTON_LB;
        ControllerMap._BUTTON_RB          = DefaultControllerMap._BUTTON_RB;
        ControllerMap._BUTTON_DPAD_LEFT   = DefaultControllerMap._BUTTON_DPAD_LEFT;
        ControllerMap._BUTTON_DPAD_RIGHT  = DefaultControllerMap._BUTTON_DPAD_RIGHT;
        ControllerMap._BUTTON_DPAD_UP     = DefaultControllerMap._BUTTON_DPAD_UP;
        ControllerMap._BUTTON_DPAD_DOWN   = DefaultControllerMap._BUTTON_DPAD_DOWN;
        ControllerMap._LEFT_TRIGGER       = DefaultControllerMap._LEFT_TRIGGER;
        ControllerMap._RIGHT_TRIGGER      = DefaultControllerMap._RIGHT_TRIGGER;

        Trace.finishedMessage();
    }

    @Override
    public boolean keyDown(final int keycode)
    {
        return false;
    }

    @Override
    public boolean keyUp(final int keycode)
    {
        return false;
    }

    @Override
    public boolean keyTyped(final char character)
    {
        return false;
    }

    @Override
    public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button)
    {
        return false;
    }

    @Override
    public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button)
    {
        return false;
    }

    @Override
    public boolean touchDragged(final int screenX, final int screenY, final int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(final int screenX, final int screenY)
    {
        return false;
    }

    @Override
    public boolean scrolled(final int amount)
    {
        return false;
    }
}
