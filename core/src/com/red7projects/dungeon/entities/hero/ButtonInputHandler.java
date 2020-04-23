/*
 *  Copyright 28/04/2018 Red7Projects.
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

package com.red7projects.dungeon.entities.hero;

import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.input.DirectionMap;
import com.red7projects.dungeon.input.objects.ControllerType;
import com.red7projects.dungeon.physics.Movement;

public class ButtonInputHandler implements Disposable
{
    AButtonActions aButtonActions;
    BButtonActions bButtonActions;
    XButtonActions xButtonActions;
    YButtonActions yButtonActions;

    private final App app;

    public ButtonInputHandler(App _app)
    {
        this.app = _app;

        aButtonActions = new AButtonActions(app);
        bButtonActions = new BButtonActions(app);
        xButtonActions = new XButtonActions(app);
        yButtonActions = new YButtonActions(app);
    }

    public void checkButtons()
    {
        //
        // A Button
        if (app.getHud().buttonA.isPressed())
        {
            if (app.getPlayer().actionButton.getActionMode() == Actions._OFFER_ABXY_A)
            {
                aButtonActions.process();
                app.getHud().buttonA.release();
            }
        }

        //
        // B Button
        if (app.getHud().buttonB.isPressed())
        {
            bButtonActions.process();
        }

        //
        // X Button
        if (app.getHud().buttonX.isPressed())
        {
            xButtonActions.process();
            app.getHud().buttonX.release();
        }

        //
        // Y Button
        if (app.getHud().buttonY.isPressed())
        {
            if (app.getPlayer().actionButton.getActionMode() == Actions._OFFER_ABXY_Y)
            {
                yButtonActions.process();
                app.getHud().buttonY.release();
            }
        }

        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            if (app.inputManager.virtualJoystick != null)
            {
                //
                // Updates button presses depending
                // upon joystick knob position
                app.inputManager.virtualJoystick.update();

                setDirection(app.inputManager.lastRegisteredDirection);
            }
        }

        if (AppConfig.availableInputs.contains(ControllerType._EXTERNAL, true))
        {
            setDirection(app.inputManager.lastRegisteredDirection);
        }

        if (AppConfig.availableInputs.contains(ControllerType._MOUSE, true))
        {
        }

        if (AppConfig.availableInputs.contains(ControllerType._KEYBOARD, true))
        {
            app.inputManager.keyboard.update();
        }

        boolean directionButtonPressed = false;

        //
        // UP button
        if (app.getPlayer().direction.getY() == Movement._DIRECTION_UP)
        {
            directionButtonPressed = true;

            if (app.getPlayer().collision.isBlockedTop())
            {
                app.getPlayer().isMovingY = false;
                app.getHud().buttonUp.release();
            }
            else
            {
                app.getPlayer().direction.setY(Movement._DIRECTION_UP);
                app.getPlayer().isMovingY = true;
                app.getPlayer().setAction(Actions._RUNNING);
                app.getPlayer().isFlippedY = false;
            }
        }

        //
        // DOWN button
        if (app.getPlayer().direction.getY() == Movement._DIRECTION_DOWN)
        {
            directionButtonPressed = true;

            if (app.getPlayer().collision.isBlockedBottom())
            {
                app.getPlayer().isMovingY = false;
                app.getHud().buttonDown.release();
            }
            else
            {
                app.getPlayer().direction.setY(Movement._DIRECTION_DOWN);
                app.getPlayer().isMovingY = true;
                app.getPlayer().setAction(Actions._RUNNING);
                app.getPlayer().isFlippedY = false;
            }
        }

        if (!app.getPlayer().isMovingY)
        {
            app.getPlayer().direction.setY(Movement._DIRECTION_STILL);
            app.getPlayer().speed.setY(0);
        }

        //
        // Check the RIGHT button
        if (app.getPlayer().direction.getX() == Movement._DIRECTION_RIGHT)
        {
            directionButtonPressed = true;

            if (app.getPlayer().collision.isBlockedRight())
            {
                app.getPlayer().isMovingX = false;
                app.getHud().buttonRight.release();
            }
            else
            {
                app.getPlayer().direction.setX(Movement._DIRECTION_RIGHT);
                app.getPlayer().isMovingX = true;
                app.getPlayer().setAction(Actions._RUNNING);
                app.getPlayer().isFlippedX = false;
            }
        }
        //
        // Check the LEFT button
        else if (app.getPlayer().direction.getX() == Movement._DIRECTION_LEFT)
        {
            directionButtonPressed = true;

            if (app.getPlayer().collision.isBlockedLeft())
            {
                app.getPlayer().isMovingX = false;
                app.getHud().buttonLeft.release();
            }
            else
            {
                app.getPlayer().direction.setX(Movement._DIRECTION_LEFT);
                app.getPlayer().isMovingX = true;
                app.getPlayer().setAction(Actions._RUNNING);
                app.getPlayer().isFlippedX = true;
            }
        }

        if (!app.getPlayer().isMovingX)
        {
            app.getPlayer().direction.setX(Movement._DIRECTION_STILL);
            app.getPlayer().speed.setX(0);
        }

        //
        // No direction buttons pressed
        if (!directionButtonPressed)
        {
            if ((app.getPlayer().getSpriteAction() != Actions._HURT)
                && (app.getPlayer().getSpriteAction() != Actions._FIGHTING))
            {
                app.getPlayer().isMovingX = false;
                app.getPlayer().isMovingY = false;

                app.getPlayer().setAction(Actions._STANDING);
                app.getPlayer().speed.set(0, 0);

                app.getPlayer().direction.standStill();
            }
        }
    }

    private void setDirection(Movement.Dir _direction)
    {
        for (int i = 0; i< DirectionMap.map.length; i++)
        {
            if (DirectionMap.map[i].translated == _direction)
            {
                app.getPlayer().direction.set
                    (
                        DirectionMap.map[i].dirX,
                        DirectionMap.map[i].dirY
                    );
            }
        }
    }

    @Override
    public void dispose()
    {
    }
}
