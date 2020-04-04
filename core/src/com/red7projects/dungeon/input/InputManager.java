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
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.config.Preferences;
import com.red7projects.dungeon.development.Developer;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.input.objects.ControllerType;
import com.red7projects.dungeon.screens.ScreenID;

public class InputManager extends AbstractInputManager
{
    private final App app;

    public InputManager(App _app)
    {
        this.app = _app;

        setup();
    }

    @Override
    public void setup()
    {
        mousePosition      = new Vector2();
        mouseWorldPosition = new Vector2();
        keyboard           = new Keyboard(app);
        touchScreen        = new TouchScreen(app);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(app.stage);
        inputMultiplexer.addProcessor(this);

        gameButtons = new Array<>();

        if (AppConfig.isDesktopApp() && !AppConfig.isAndroidOnDesktop())
        {
            if (app.preferences.isEnabled(Preferences._EXTERNAL_CONTROLLER))
            {
                gameController = new GameController(app);
                gameController.setup();
            }

            Pixmap pixmap = new Pixmap(Gdx.files.internal("data/crosshairs.png"));
            int xHotspot = pixmap.getWidth() / 2;
            int yHotspot = pixmap.getHeight() / 2;
            Cursor cursor   = Gdx.graphics.newCursor(pixmap, xHotspot, yHotspot);
            Gdx.graphics.setCursor(cursor);
            pixmap.dispose();        }

        UIButtons.createButtons(app);
        UIButtons.overrideControllerIfNotFitted(app);

        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        Gdx.input.setCatchKey(Input.Keys.MENU, true);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public float getControllerXPercentage()
    {
        float xPercent = 0.0f;

        if ((app.getHud() != null) && (app.getHud().getJoystick() != null))
        {
            if (AppConfig.controlMode == ControllerType._VIRTUAL)
            {
                xPercent = app.getHud().getJoystick().getXPercent();
            }
            else
            {
                if (AppConfig.controlMode == ControllerType._EXTERNAL)
                {
                    xPercent = gameController._horizontalValue;
                }
                else
                {
                    xPercent = 0.0f;
                }
            }
        }

        return xPercent;
    }

    @Override
    public float getControllerYPercentage()
    {
        float yPercent = 0.0f;

        if ((app.getHud() != null) && (app.getHud().getJoystick() != null))
        {
            if (AppConfig.controlMode == ControllerType._VIRTUAL)
            {
                yPercent = app.getHud().getJoystick().getYPercent();
            }
            else
            {
                if (AppConfig.controlMode == ControllerType._EXTERNAL)
                {
                    yPercent = gameController._verticalValue;

                    if ("PC/PS3/Android".equals(AppConfig.usedController))
                    {
                        yPercent *= -1;
                    }
                }
                else
                {
                    yPercent = 0.0f;
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
