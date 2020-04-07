/*
 *  Copyright 10/06/2018 Red7Projects.
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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.red7projects.dungeon.config.Preferences;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.physics.Movement;

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
//@formatter:off
public class VirtualJoystick
{
    private static final float PAD_X        = 25;
    private static final float PAD_Y        = 25;
    private static final float PAD_WIDTH    = 320;
    private static final float PAD_HEIGHT   = 320;

    private Touchpad                touchpad;
    private Touchpad.TouchpadStyle  touchpadStyle;
    private Skin                    touchpadSkin;
    private Drawable                touchBackground;
    private Drawable                touchKnob;
    private final App               app;

    public VirtualJoystick(App _app)
    {
        this.app = _app;
    }

    public void create()
    {
        touchpadSkin = new Skin();
        touchpadSkin.add("background", new Texture("data/packedimages/input/touch_background.png"));
        touchpadSkin.add("ball", new Texture("data/packedimages/input/joystick_ball.png"));

        touchpadStyle = new Touchpad.TouchpadStyle();

        touchBackground = touchpadSkin.getDrawable("background");
        touchKnob = touchpadSkin.getDrawable("ball");

        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;

        touchpad = new Touchpad(1, touchpadStyle);
        touchpad.setBounds(PAD_X, PAD_Y, PAD_WIDTH, PAD_HEIGHT);
        touchpad.setResetOnTouchUp(true);

//        app.inputManager.currentRegisteredDirection = evaluateJoypadDirection();
//        app.inputManager.lastRegisteredDirection = app.inputManager.currentRegisteredDirection;
    }

    public void addToStage()
    {
        app.stage.addActor(touchpad);
    }

    public void update()
    {
        app.getHud().releaseDirectionButtons();

        switch (evaluateJoypadDirection())
        {
            case _UP:
            {
                app.getHud().buttonUp.press();
            }
            break;

            case _DOWN:
            {
                app.getHud().buttonDown.press();
            }
            break;

            case _LEFT:
            {
                app.getHud().buttonLeft.press();
            }
            break;

            case _RIGHT:
            {
                app.getHud().buttonRight.press();
            }
            break;

            case _UP_LEFT:
            {
                app.getHud().buttonUp.press();
                app.getHud().buttonLeft.press();
            }
            break;

            case _UP_RIGHT:
            {
                app.getHud().buttonUp.press();
                app.getHud().buttonRight.press();
            }
            break;

            case _DOWN_LEFT:
            {
                app.getHud().buttonDown.press();
                app.getHud().buttonLeft.press();
            }
            break;

            case _DOWN_RIGHT:
            {
                app.getHud().buttonDown.press();
                app.getHud().buttonRight.press();
            }
            break;

            case _STILL:
            default:
            {
            }
            break;
        }
    }

    public void show()
    {
        if (touchpad != null)
        {
            touchpad.addAction(Actions.show());
        }
    }

    public void hide()
    {
        if (touchpad != null)
        {
            touchpad.addAction(Actions.hide());
        }
    }

    public float getXPercent()
    {
        return touchpad.getKnobPercentX();
    }

    public float getYPercent()
    {
        return touchpad.getKnobPercentY();
    }

    public Touchpad getTouchpad()
    {
        return touchpad;
    }

    private Movement.Dir evaluateJoypadDirection()
    {
        Movement.Dir joyDir;

        //
        // The default angle for joystick goes round anti-clockwise,
        // so modify so that the result is now clockwise.
        int angle = Math.abs((int) (InputUtils.getJoystickAngle(app) - 360));

        joyDir = DirectionMap.map[angle / 10].translated;

        app.inputManager.lastRegisteredDirection = joyDir;

        return joyDir;
    }

    public void remove()
    {
        if (touchpad != null)
        {
            touchpad.addAction(Actions.removeActor());
            touchpad = null;
        }
    }
}
