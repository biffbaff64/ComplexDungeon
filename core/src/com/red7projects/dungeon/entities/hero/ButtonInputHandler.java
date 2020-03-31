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
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.input.JoystickMap;
import com.red7projects.dungeon.physics.Movement;

public class ButtonInputHandler implements Disposable
{
    private AButtonActions aButtonActions;
    private BButtonActions bButtonActions;
    private XButtonActions xButtonActions;
    private YButtonActions yButtonActions;

    private final App app;

    ButtonInputHandler(App _app)
    {
        this.app = _app;

        this.aButtonActions = new AButtonActions(app);
        this.bButtonActions = new BButtonActions(app);
        this.xButtonActions = new XButtonActions(app);
        this.yButtonActions = new YButtonActions(app);
    }

    void checkButtons()
    {
        //
        // ATTACK Button
        if (app.getHud().buttonB.isPressed)
        {
            app.getPlayer().setAction(Actions._FIGHTING);
        }

        //
        // A Button
        if (app.getHud().buttonA.isPressed)
        {
            if (app.getPlayer().actionButton.getActionMode() == Actions._OFFER_ABXY_A)
            {
                aButtonActions.process();
                app.getHud().buttonA.release();
            }
        }

        //
        // X Button
        if (app.getHud().buttonX.isPressed)
        {
            if (app.getPlayer().actionButton.getActionMode() == Actions._OFFER_ABXY_A)
            {
                xButtonActions.process();
                app.getHud().buttonX.release();
            }
        }

        //
        // Y Button
        if (app.getHud().buttonY.isPressed)
        {
            if (app.getPlayer().actionButton.getActionMode() == Actions._OFFER_ABXY_A)
            {
                yButtonActions.process();
                app.getHud().buttonY.release();
            }
        }

        if (app.getHud().getJoystick() != null)
        {
            //
            // Updates button presses depending
            // upon joystick knob position
            app.getHud().getJoystick().update();

            setDirection(app.getHud().getJoystick().lastRegisteredDirection);
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

    private void setDirection(Movement.Dir joyDir)
    {
        JoystickMap joystickMap = new JoystickMap();

        for (int i=0; i<joystickMap.map.length; i++)
        {
            if (joystickMap.map[i].translated == joyDir)
            {
                app.getPlayer().direction.set
                    (
                        joystickMap.map[i].dirX,
                        joystickMap.map[i].dirY
                    );
            }
        }
    }

    @Override
    public void dispose()
    {
    }
}
