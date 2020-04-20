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
    private boolean _isPressed;
    private boolean _isDisabled;
    private int     _pointer;

    public Switch()
    {
        _isPressed  = false;
        _isDisabled = false;
    }

    @Override
    public void update()
    {
    }

    @Override
    public void press()
    {
        if (!_isDisabled)
        {
            _isPressed = true;
        }
    }

    @Override
    public void release()
    {
        _isPressed = false;
    }

    @Override
    public boolean isPressed()
    {
        return _isPressed;
    }

    @Override
    public boolean isDisabled()
    {
        return _isDisabled;
    }

    @Override
    public void setDisabled(boolean _state)
    {
        _isDisabled = _state;
    }

    @Override
    public void setVisible(boolean _state)
    {
    }

    @Override
    public boolean isVisible()
    {
        return false;
    }

    @Override
    public void toggleDisabled()
    {
        _isDisabled = !_isDisabled;
    }

    @Override
    public void togglePressed()
    {
        _isPressed = !_isPressed;
    }
}
