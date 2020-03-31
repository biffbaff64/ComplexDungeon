/*
 *  Copyright 14/10/2018 Red7Projects.
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

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.red7projects.dungeon.input.buttons.GameButton;
import com.red7projects.dungeon.input.objects.ControllerMap;

public abstract class AbstractInputManager implements InputProcessor
{
    public Array<GameButton> gameButtons;
    public Vector2           mousePosition;
    public Vector2           mouseWorldPosition;
    public Keyboard          keyboard;
    public TouchScreen       touchScreen;
    public GameController    gameController;
    public InputMultiplexer  inputMultiplexer;
    public Controller        controller;
    public ControllerMap     controllerMap;
    public float             _verticalValue;
    public float             _horizontalValue;

    public abstract void setup();

    public InputMultiplexer getInputMultiplexer()
    {
        return inputMultiplexer;
    }

    public GameController getGameController()
    {
        return gameController;
    }

    public abstract float getControllerXPercentage();

    public abstract float getControllerYPercentage();
}
