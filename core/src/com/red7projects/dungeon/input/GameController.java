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
import com.red7projects.dungeon.config.Preferences;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.input.objects.ControllerMap;
import com.red7projects.dungeon.input.objects.Qumox3507Pad;
import com.red7projects.dungeon.input.objects.USBControllerPad;
import com.red7projects.dungeon.input.objects.XBox360Pad;
import com.red7projects.dungeon.logging.Trace;

public class GameController extends AbstractInputManager implements ControllerListener, InputProcessor
{
    private final App app;

    public GameController(App _app)
    {
        this.app = _app;
    }

    @Override
    public void setup()
    {
        AppConfig.controllersFitted = false;
        AppConfig.gameButtonsReady  = false;

        addExternalController();

        if (!AppConfig.controllersFitted)
        {
            if (Controllers.getControllers().size > 0)
            {
                Trace.__FILE_FUNC("External Controller found but is disabled in Settings");

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
    }

    @Override
    public float getControllerXPercentage()
    {
        return 0;
    }

    @Override
    public float getControllerYPercentage()
    {
        return 0;
    }

    public void addExternalController()
    {
        try {
            if (app.preferences.isEnabled(Preferences._EXTERNAL_CONTROLLER)) {
                if (Controllers.getControllers().size > 0) {
                    controller = Controllers.getControllers().first();

                    if (controller != null) {
                        Trace.__FILE_FUNC("Controller [" + controller.getName() + "] found");

                        AppConfig.controllersFitted = true;

                        createControllerMap();

                        controller.addListener(this);

                        app.inputManager.getInputMultiplexer().addProcessor(this);

                        Trace.dbg("Controller added");
                    }
                }
            }
        }
        catch (NullPointerException npe)
        {
            disableExternalControllers();
        }
    }

    public void disableExternalControllers()
    {
        controller = null;

        AppConfig.controllersFitted = false;

        //
        // NOTE: removeProcessor() checks that this processor
        // exists in the multiplexor first, so there is no
        // risk of null pointer exceptions etc.
        app.inputManager.getInputMultiplexer().removeProcessor(this);
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
        if (app.preferences.isEnabled(Preferences._EXTERNAL_CONTROLLER))
        {
            UIButtons.controllerButtonCode = buttonCode;

            if (buttonCode == controllerMap._BUTTON_A)
            {
                UIButtons.controllerAPressed = true;
            }
            else if (buttonCode == controllerMap._BUTTON_B)
            {
                UIButtons.controllerBPressed = true;
            }
            else if (buttonCode == controllerMap._BUTTON_X)
            {
                UIButtons.controllerXPressed = true;
            }
            else if (buttonCode == controllerMap._BUTTON_Y)
            {
                UIButtons.controllerYPressed = true;
            }
            else if (buttonCode == controllerMap._BUTTON_LB)
            {
                UIButtons.controllerLBPressed = true;
            }
            else if (buttonCode == controllerMap._BUTTON_RB)
            {
                UIButtons.controllerRBPressed = true;

                if (app.getHud().buttonA != null)
                {
                    app.getHud().buttonA.press();
                }
            }
            else if (buttonCode == controllerMap._BUTTON_START)
            {
                UIButtons.controllerStartPressed = true;

                if (app.getHud().buttonPause != null)
                {
                    app.getHud().buttonPause.press();
                }
            }
            else if (buttonCode == controllerMap._BUTTON_BACK)
            {
                UIButtons.controllerBackPressed = true;
            }
            else if (buttonCode == controllerMap._LEFT_TRIGGER)
            {
                UIButtons.controllerLeftFirePressed = true;
                UIButtons.controllerFirePressed = true;

                if (app.getHud().buttonB != null)
                {
                    app.getHud().buttonB.press();
                }
            }
            else if (buttonCode == controllerMap._RIGHT_TRIGGER)
            {
                UIButtons.controllerRightFirePressed = true;
                UIButtons.controllerFirePressed = true;

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
        if (app.preferences.isEnabled(Preferences._EXTERNAL_CONTROLLER))
        {
            UIButtons.controllerButtonCode = -1;

            if (buttonCode == controllerMap._BUTTON_A)
            {
                UIButtons.controllerAPressed = false;
            }
            else if (buttonCode == controllerMap._BUTTON_B)
            {
                UIButtons.controllerBPressed = false;
            }
            else if (buttonCode == controllerMap._BUTTON_X)
            {
                UIButtons.controllerXPressed = false;
            }
            else if (buttonCode == controllerMap._BUTTON_Y)
            {
                UIButtons.controllerYPressed = false;
            }
            else if (buttonCode == controllerMap._BUTTON_LB)
            {
                UIButtons.controllerLBPressed = false;
            }
            else if (buttonCode == controllerMap._BUTTON_RB)
            {
                UIButtons.controllerRBPressed = false;

                if (app.getHud().buttonA != null)
                {
                    app.getHud().buttonA.release();
                }
            }
            else if (buttonCode == controllerMap._BUTTON_START)
            {
                UIButtons.controllerStartPressed = false;
            }
            else if (buttonCode == controllerMap._BUTTON_BACK)
            {
                UIButtons.controllerBackPressed = false;
            }
            else if (buttonCode == controllerMap._LEFT_TRIGGER)
            {
                UIButtons.controllerLeftFirePressed = false;
                UIButtons.controllerFirePressed = false;

                if (app.getHud().buttonB != null)
                {
                    app.getHud().buttonB.release();
                }
            }
            else if (buttonCode == controllerMap._RIGHT_TRIGGER)
            {
                UIButtons.controllerRightFirePressed = false;
                UIButtons.controllerFirePressed = false;

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
        if (app.preferences.isEnabled(Preferences._EXTERNAL_CONTROLLER))
        {
            UIButtons.controllerAxisCode = axisCode;
            UIButtons.controllerAxisValue = value;

            if (UIButtons.controllerAxisValue > 1.0f)
            {
                UIButtons.controllerAxisValue = 1.0f;
            }

            if (UIButtons.controllerAxisValue < -1.0f)
            {
                UIButtons.controllerAxisValue = -1.0f;
            }

            if ((axisCode == controllerMap._AXIS_LEFT_X) || (axisCode == controllerMap._AXIS_RIGHT_X))
            {
                _horizontalValue = value;

                if (controllerMap.isInNegativeRange(value))
                {
                    UIButtons.controllerLeftPressed = true;
                    app.getHud().buttonLeft.press();

                    if (app.getHud().buttonLeft != null)
                    {
                        app.getHud().buttonLeft.press();
                    }
                }
                else if (controllerMap.isInPositiveRange(value))
                {
                    UIButtons.controllerRightPressed = true;
                    app.getHud().buttonRight.press();

                    if (app.getHud().buttonRight != null)
                    {
                        app.getHud().buttonRight.press();
                    }
                }
                else
                {
                    if (app.getHud().buttonLeft != null)
                    {
                        app.getHud().buttonLeft.release();
                        app.getHud().buttonRight.release();
                    }

                    UIButtons.controllerLeftPressed = false;
                    UIButtons.controllerRightPressed = false;

                    UIButtons.controllerAxisCode = -1;
                    UIButtons.controllerAxisValue = 0;
                }
            }
            else if ((axisCode == controllerMap._AXIS_LEFT_Y) || (axisCode == controllerMap._AXIS_RIGHT_Y))
            {
                _verticalValue = value;

                if (controllerMap.isInNegativeRange(value))
                {
                    UIButtons.controllerUpPressed = true;
                    app.getHud().buttonUp.press();

                    if (app.getHud().buttonUp != null)
                    {
                        app.getHud().buttonUp.press();
                    }
                }
                else if (controllerMap.isInPositiveRange(value))
                {
                    UIButtons.controllerDownPressed = true;
                    app.getHud().buttonDown.press();

                    if (app.getHud().buttonDown != null)
                    {
                        app.getHud().buttonDown.press();
                    }
                }
                else
                {
                    if (app.getHud() != null)
                    {
                        if (app.getHud().buttonUp != null)
                        {
                            app.getHud().buttonUp.release();
                            app.getHud().buttonDown.release();
                        }
                    }

                    UIButtons.controllerUpPressed = false;
                    UIButtons.controllerDownPressed = false;

                    UIButtons.controllerAxisCode = -1;
                    UIButtons.controllerAxisValue = 0;
                }
            }
            else
            {
                UIButtons.controllerUpPressed = false;
                UIButtons.controllerDownPressed = false;
                UIButtons.controllerLeftPressed = false;
                UIButtons.controllerRightPressed = false;

                app.getHud().releaseDirectionButtons();

                UIButtons.controllerAxisCode = -1;
                UIButtons.controllerAxisValue = 0;
            }
        }

        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value)
    {
        if (app.preferences.isEnabled(Preferences._EXTERNAL_CONTROLLER))
        {
            UIButtons.controllerPovDirection = value;
        }

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
        controllerMap = new ControllerMap();

        if (controller.getName().contains("xbox") && controller.getName().contains("360"))
        {
            controllerMap._MIN_RANGE            = XBox360Pad._MIN_RANGE;
            controllerMap._MAX_RANGE            = XBox360Pad._MAX_RANGE;
            controllerMap._DEAD_ZONE            = XBox360Pad._DEAD_ZONE;
            controllerMap._AXIS_LEFT_TRIGGER    = XBox360Pad._AXIS_LEFT_TRIGGER;
            controllerMap._AXIS_RIGHT_TRIGGER   = XBox360Pad._AXIS_RIGHT_TRIGGER;
            controllerMap._AXIS_LEFT_X          = XBox360Pad._AXIS_LEFT_X;
            controllerMap._AXIS_LEFT_Y          = XBox360Pad._AXIS_LEFT_Y;
            controllerMap._AXIS_RIGHT_X         = XBox360Pad._AXIS_RIGHT_X;
            controllerMap._AXIS_RIGHT_Y         = XBox360Pad._AXIS_RIGHT_Y;
            controllerMap._BUTTON_A             = XBox360Pad._BUTTON_A;
            controllerMap._BUTTON_B             = XBox360Pad._BUTTON_B;
            controllerMap._BUTTON_X             = XBox360Pad._BUTTON_X;
            controllerMap._BUTTON_Y             = XBox360Pad._BUTTON_Y;
            controllerMap._BUTTON_START         = XBox360Pad._BUTTON_START;
            controllerMap._BUTTON_BACK          = XBox360Pad._BUTTON_BACK;
            controllerMap._BUTTON_L3            = XBox360Pad._BUTTON_L3;
            controllerMap._BUTTON_R3            = XBox360Pad._BUTTON_R3;
            controllerMap._BUTTON_LB            = XBox360Pad._BUTTON_LB;
            controllerMap._BUTTON_RB            = XBox360Pad._BUTTON_RB;
            controllerMap._BUTTON_DPAD_LEFT     = XBox360Pad._BUTTON_DPAD_LEFT;
            controllerMap._BUTTON_DPAD_RIGHT    = XBox360Pad._BUTTON_DPAD_RIGHT;
            controllerMap._BUTTON_DPAD_UP       = XBox360Pad._BUTTON_DPAD_UP;
            controllerMap._BUTTON_DPAD_DOWN     = XBox360Pad._BUTTON_DPAD_DOWN;
            controllerMap._LEFT_TRIGGER         = XBox360Pad._LEFT_TRIGGER;
            controllerMap._RIGHT_TRIGGER        = XBox360Pad._RIGHT_TRIGGER;
        }
        else if ("Usb GamePad".equals(controller.getName()))
        {
            controllerMap._MIN_RANGE            = USBControllerPad._MIN_RANGE;
            controllerMap._MAX_RANGE            = USBControllerPad._MAX_RANGE;
            controllerMap._DEAD_ZONE            = USBControllerPad._DEAD_ZONE;
            controllerMap._AXIS_LEFT_TRIGGER    = USBControllerPad._AXIS_LEFT_TRIGGER;
            controllerMap._AXIS_RIGHT_TRIGGER   = USBControllerPad._AXIS_RIGHT_TRIGGER;
            controllerMap._AXIS_LEFT_X          = USBControllerPad._AXIS_LEFT_X;
            controllerMap._AXIS_LEFT_Y          = USBControllerPad._AXIS_LEFT_Y;
            controllerMap._AXIS_RIGHT_X         = USBControllerPad._AXIS_RIGHT_X;
            controllerMap._AXIS_RIGHT_Y         = USBControllerPad._AXIS_RIGHT_Y;
            controllerMap._BUTTON_A             = USBControllerPad._BUTTON_A;
            controllerMap._BUTTON_B             = USBControllerPad._BUTTON_B;
            controllerMap._BUTTON_X             = USBControllerPad._BUTTON_X;
            controllerMap._BUTTON_Y             = USBControllerPad._BUTTON_Y;
            controllerMap._BUTTON_START         = USBControllerPad._BUTTON_START;
            controllerMap._BUTTON_BACK          = USBControllerPad._BUTTON_BACK;
            controllerMap._BUTTON_L3            = USBControllerPad._BUTTON_L3;
            controllerMap._BUTTON_R3            = USBControllerPad._BUTTON_R3;
            controllerMap._BUTTON_LB            = USBControllerPad._BUTTON_LB;
            controllerMap._BUTTON_RB            = USBControllerPad._BUTTON_RB;
            controllerMap._BUTTON_DPAD_LEFT     = USBControllerPad._BUTTON_DPAD_LEFT;
            controllerMap._BUTTON_DPAD_RIGHT    = USBControllerPad._BUTTON_DPAD_RIGHT;
            controllerMap._BUTTON_DPAD_UP       = USBControllerPad._BUTTON_DPAD_UP;
            controllerMap._BUTTON_DPAD_DOWN     = USBControllerPad._BUTTON_DPAD_DOWN;
            controllerMap._LEFT_TRIGGER         = USBControllerPad._LEFT_TRIGGER;
            controllerMap._RIGHT_TRIGGER        = USBControllerPad._RIGHT_TRIGGER;
        }
        else if ("PC/PS3/Android".equals(controller.getName()))
        {
            controllerMap._MIN_RANGE            = Qumox3507Pad._MIN_RANGE;
            controllerMap._MAX_RANGE            = Qumox3507Pad._MAX_RANGE;
            controllerMap._DEAD_ZONE            = Qumox3507Pad._DEAD_ZONE;
            controllerMap._AXIS_LEFT_TRIGGER    = Qumox3507Pad._AXIS_LEFT_TRIGGER;
            controllerMap._AXIS_RIGHT_TRIGGER   = Qumox3507Pad._AXIS_RIGHT_TRIGGER;
            controllerMap._AXIS_LEFT_X          = Qumox3507Pad._AXIS_LEFT_X;
            controllerMap._AXIS_LEFT_Y          = Qumox3507Pad._AXIS_LEFT_Y;
            controllerMap._AXIS_RIGHT_X         = Qumox3507Pad._AXIS_RIGHT_X;
            controllerMap._AXIS_RIGHT_Y         = Qumox3507Pad._AXIS_RIGHT_Y;
            controllerMap._BUTTON_A             = Qumox3507Pad._BUTTON_A;
            controllerMap._BUTTON_B             = Qumox3507Pad._BUTTON_B;
            controllerMap._BUTTON_X             = Qumox3507Pad._BUTTON_X;
            controllerMap._BUTTON_Y             = Qumox3507Pad._BUTTON_Y;
            controllerMap._BUTTON_START         = Qumox3507Pad._BUTTON_START;
            controllerMap._BUTTON_BACK          = Qumox3507Pad._BUTTON_BACK;
            controllerMap._BUTTON_L3            = Qumox3507Pad._BUTTON_L3;
            controllerMap._BUTTON_R3            = Qumox3507Pad._BUTTON_R3;
            controllerMap._BUTTON_LB            = Qumox3507Pad._BUTTON_LB;
            controllerMap._BUTTON_RB            = Qumox3507Pad._BUTTON_RB;
            controllerMap._BUTTON_DPAD_LEFT     = Qumox3507Pad._BUTTON_DPAD_LEFT;
            controllerMap._BUTTON_DPAD_RIGHT    = Qumox3507Pad._BUTTON_DPAD_RIGHT;
            controllerMap._BUTTON_DPAD_UP       = Qumox3507Pad._BUTTON_DPAD_UP;
            controllerMap._BUTTON_DPAD_DOWN     = Qumox3507Pad._BUTTON_DPAD_DOWN;
            controllerMap._LEFT_TRIGGER         = Qumox3507Pad._LEFT_TRIGGER;
            controllerMap._RIGHT_TRIGGER        = Qumox3507Pad._RIGHT_TRIGGER;
        }
        else
        {
            Trace.__FILE_FUNC("ERROR: No Controller or Controller unrecognised!");
        }
    }

    @Override
    public boolean keyDown(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchDown(int touchX, int touchY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchUp(int touchX, int touchY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }
}
