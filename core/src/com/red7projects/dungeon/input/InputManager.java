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
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.development.Developer;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.input.buttons.GameButton;
import com.red7projects.dungeon.input.objects.ControllerMap;
import com.red7projects.dungeon.input.objects.ControllerType;
import com.red7projects.dungeon.screens.ScreenID;

@SuppressWarnings("WeakerAccess")
public class InputManager implements AbstractInputManager, InputProcessor
{
    public Array<GameButton> gameButtons;
    public Vector2           mousePosition;
    public Vector2           mouseWorldPosition;
    public Keyboard          keyboard;
    public VirtualJoystick   virtualJoystick;
    public TouchScreen       touchScreen;
    public GameController    gameController;
    public InputMultiplexer  inputMultiplexer;
    public float             _horizontalValue;
    public float             _verticalValue;

    protected App app;

    public InputManager(App _app)
    {
        this.app = _app;

        setup();
    }

    @Override
    public boolean setup()
    {
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(app.stage);
        inputMultiplexer.addProcessor(this);

        if (AppConfig.isAndroidApp() || AppConfig.isAndroidOnDesktop())
        {
            touchScreen     = new TouchScreen(app);
            virtualJoystick = new VirtualJoystick(app);

            virtualJoystick.create();
            virtualJoystick.addToStage();
        }

        if (AppConfig.isDesktopApp())
        {
            gameController = new GameController(app);

            if (!gameController.setup())
            {
                gameController = null;

                Pixmap pixmap = new Pixmap(Gdx.files.internal("data/crosshairs.png"));
                Cursor cursor = Gdx.graphics.newCursor(pixmap, (pixmap.getWidth() / 2), (pixmap.getHeight() / 2));
                Gdx.graphics.setCursor(cursor);
                pixmap.dispose();
            }
        }

        if (AppConfig.isDesktopApp() || AppConfig.isAndroidOnDesktop())
        {
            mousePosition      = new Vector2();
            mouseWorldPosition = new Vector2();
            keyboard           = new Keyboard(app);
        }

        gameButtons = new Array<>();

        UIButtons.setup(app);

        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        Gdx.input.setCatchKey(Input.Keys.MENU, true);
        Gdx.input.setInputProcessor(inputMultiplexer);

        return true;
    }

    @Override
    public float getControllerXPercentage()
    {
        float xPercent = 0.0f;

        if (app.getHud() != null)
        {
            if (AppConfig.controlMode == ControllerType._VIRTUAL)
            {
                if (virtualJoystick != null)
                {
                    xPercent = virtualJoystick.getXPercent();
                }
            }
            else
            {
                //
                // This applies to controllerTypes _EXTERNAL and _KEYBOARD
                xPercent = _horizontalValue;
            }
        }

        return xPercent;
    }

    @Override
    public float getControllerYPercentage()
    {
        float yPercent = 0.0f;

        if (app.getHud() != null)
        {
            if (AppConfig.controlMode == ControllerType._VIRTUAL)
            {
                if (virtualJoystick != null)
                {
                    yPercent = virtualJoystick.getYPercent();
                }
            }
            else
            {
                if (AppConfig.controlMode == ControllerType._EXTERNAL)
                {
                    yPercent = _verticalValue;

                    if ("PC/PS3/Android".equals(AppConfig.usedController))
                    {
                        yPercent *= -1;
                    }
                }
                else
                {
                    if (AppConfig.controlMode == ControllerType._KEYBOARD)
                    {
                        yPercent = _verticalValue;
                    }
                }
            }
        }

        return yPercent;
    }

    @Override
    public boolean keyDown(int keycode)
    {
        boolean returnFlag = false;

        if (keycode == Input.Keys.BACK)
        {
            if (AppConfig.gameScreenActive)
            {
                app.getHud().buttonPause.press();
            }
        }

        if (AppConfig.isDesktopApp())
        {
            if (app.currentScreenID == ScreenID._GAME_SCREEN)
            {
                returnFlag = keyboard.maingameKeyDown(keycode);
            }
        }

        return returnFlag;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        boolean returnFlag = false;

        if (keycode == Input.Keys.BACK)
        {
            UIButtons.systemBackButton.release();

            if (AppConfig.gameScreenActive)
            {
                app.getHud().buttonPause.release();
            }
        }

        if (AppConfig.isDesktopApp())
        {
            if (app.currentScreenID == ScreenID._GAME_SCREEN)
            {
                returnFlag = keyboard.maingameKeyUp(keycode);
            }
        }

        return returnFlag;
    }

    @Override
    public boolean touchDown(int touchX, int touchY, int pointer, int button)
    {
        Vector2 newPoints = new Vector2(touchX, touchY);
        newPoints = app.baseRenderer.hudGameCamera.viewport.unproject(newPoints);

        int screenX = (int) (newPoints.x - app.mapData.mapPosition.x);
        int screenY = (int) (newPoints.y - app.mapData.mapPosition.y);

        boolean returnFlag = false;

        if (app.currentScreenID == ScreenID._TITLE_SCREEN)
        {
            returnFlag = touchScreen.titleScreenTouchDown(screenX, screenY);
        }
        else if (app.currentScreenID == ScreenID._GAME_SCREEN)
        {
            returnFlag = touchScreen.gameScreenTouchDown(screenX, screenY, pointer);
        }

        return returnFlag;
    }

    @Override
    public boolean touchUp(int touchX, int touchY, int pointer, int button)
    {
        Vector2 newPoints = new Vector2(touchX, touchY);
        newPoints = app.baseRenderer.hudGameCamera.viewport.unproject(newPoints);

        int screenX = (int) (newPoints.x - app.mapData.mapPosition.x);
        int screenY = (int) (newPoints.y - app.mapData.mapPosition.y);

        boolean returnFlag = false;

        if (app.currentScreenID == ScreenID._TITLE_SCREEN)
        {
            returnFlag = touchScreen.titleScreenTouchUp(screenX, screenY);
        }
        else if (app.currentScreenID == ScreenID._GAME_SCREEN)
        {
            returnFlag = touchScreen.gameScreenTouchUp(screenX, screenY);
        }

        return returnFlag;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        Vector2 newPoints = new Vector2(screenX, screenY);
        newPoints = app.baseRenderer.hudGameCamera.viewport.unproject(newPoints);

        int touchX = (int) (newPoints.x - app.mapData.mapPosition.x);
        int touchY = (int) (newPoints.y - app.mapData.mapPosition.y);

        boolean returnFlag = false;

        if (app.currentScreenID == ScreenID._GAME_SCREEN)
        {
            if (app.getHud().buttonUp.isPressed)
            {
                if ((app.getHud().buttonUp.pointer == pointer)
                        && !app.getHud().buttonUp.contains(touchX, touchY))
                {
                    app.getHud().buttonUp.release();
                    returnFlag = true;
                }
            }

            if ((app.getHud().buttonB.pointer == pointer)
                    && !app.getHud().buttonB.contains(touchX, touchY))
            {
                app.getHud().buttonB.release();
                returnFlag = true;
            }

            if ((app.getHud().buttonLeft.pointer == pointer)
                    && !app.getHud().buttonLeft.contains(touchX, touchY))
            {
                app.getHud().buttonLeft.release();
                returnFlag = true;
            }

            if ((app.getHud().buttonRight.pointer == pointer)
                    && !app.getHud().buttonRight.contains(touchX, touchY))
            {
                app.getHud().buttonRight.release();
                returnFlag = true;
            }
        }

        return returnFlag;
    }

    /**
     * Process a movement of the mouse pointer.
     * Not called if any mouse button pressed.
     * Not called on iOS builds.
     *
     * @param screenX - new X coordinate.
     * @param screenY - new Y coordinate.
     * @return boolean indicating whether or not the input
     * was processed.
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        if (AppConfig.gameScreenActive)
        {
            Vector2 newPoints = new Vector2(screenX, screenY);
            newPoints = app.baseRenderer.hudGameCamera.viewport.unproject(newPoints);

            mouseWorldPosition.set(newPoints.x, newPoints.y);

            int touchX = (int) (newPoints.x - app.mapData.mapPosition.x);
            int touchY = (int) (newPoints.y - app.mapData.mapPosition.y);

            mousePosition.set(touchX, touchY);
        }

        return false;
    }

    /**
     * React to the mouse wheel scrolling
     *
     * @param amount - scroll amount.
     *               - amount < 0 == scroll down.
     *               - amount > 0 == scroll up.
     * @return boolean indicating whether or not the input
     * was processed.
     */
    @Override
    public boolean scrolled(int amount)
    {
        if (AppConfig.gameScreenActive)
        {
            if (Developer.isDevMode() && keyboard.ctrlButtonHeld)
            {
                if (amount < 0)
                {
                    app.baseRenderer.gameZoom.out(0.10f);
                }
                else if (amount > 0)
                {
                    app.baseRenderer.gameZoom.in(0.10f);
                }
            }
        }

        return false;
    }
}
