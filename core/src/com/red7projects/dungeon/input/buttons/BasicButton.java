/*
 *  Copyright 27/04/2018 Red7Projects.
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

package com.red7projects.dungeon.input.buttons;

import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.maths.Box;

@SuppressWarnings({"WeakerAccess"})
public class BasicButton implements GDXButton, Disposable
{
    public boolean isPressed;
    public boolean isActive;
    public boolean isSwitch;
    public boolean isDisabled;
    public boolean hasSound;

    public Box buttonRect;

    public int x;
    public int y;
    public int width;
    public int height;
    public int pointer;
    public ButtonID buttonID;

    protected final App app;

    public BasicButton(App _app, ButtonID _id)
    {
        this.app        = _app;
        this.buttonID   = _id;

        this.isPressed  = false;
        this.isDisabled = false;
        this.hasSound   = true;
        this.isActive   = true;
        this.isSwitch   = false;

        this.buttonRect = new Box();
    }

    @Override
    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;

        refreshBounds();
    }

    @Override
    public boolean contains(int x, int y)
    {
        return isActive && buttonRect.contains((float) x, (float) y);
    }

    @Override
    public boolean contains(float x, float y)
    {
        return isActive && buttonRect.contains(x, y);
    }

    @Override
    public void refreshBounds()
    {
        buttonRect.set(x, y, width, height);
    }

    @Override
    public Box getBounds()
    {
        return buttonRect;
    }

    @Override
    public void setID(final ButtonID _id)
    {
        buttonID = _id;
    }

    @Override
    public ButtonID getID()
    {
        return buttonID;
    }

    @Override
    public void press()
    {
        isPressed = true;
    }

    @Override
    public void press(int _ptr)
    {
        this.press();
        this.pointer = _ptr;
    }

    @Override
    public void pressConditional(boolean condition)
    {
        isPressed = condition;
    }

    @Override
    public void release()
    {
        isPressed = false;
    }

    @Override
    public void set()
    {
        isPressed = true;
    }

    @Override
    public void clear()
    {
        isPressed = false;
    }

    @Override
    public void toggleActive()
    {
        isActive = !isActive;
    }

    @Override
    public void togglePressed()
    {
        isPressed = !isPressed;
    }

    @Override
    public void dispose()
    {
    }
}
