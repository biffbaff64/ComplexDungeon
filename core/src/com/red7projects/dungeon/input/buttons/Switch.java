/*
 *  Copyright 20/05/2018 Red7Projects.
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

/**
 * A Simple ON/OFF Switch class
 */
public class Switch implements GDXButton
{
    public boolean isPressed;
    public boolean isDisabled;
    public int pointer;

    public Switch()
    {
        isPressed  = false;
        isDisabled = false;
    }

    @Override
    public void press()
    {
        if (!isDisabled)
        {
            isPressed = true;
        }
    }

    @Override
    public void press(int _ptr)
    {
        press();
        pointer = _ptr;
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
    public void toggleDisabled()
    {
        isDisabled = !isDisabled;
    }

    @Override
    public void togglePressed()
    {
        isPressed = !isPressed;
    }
}
