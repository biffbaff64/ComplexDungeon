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

package com.red7projects.dungeon.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.utils.development.Developer;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.physics.Direction;
import com.red7projects.dungeon.physics.DirectionValue;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.screens.ScreenID;

@SuppressWarnings("WeakerAccess")
public class Keyboard extends InputAdapter
{
    public boolean ctrlButtonHeld;
    private final App app;

    public Keyboard(App _app)
    {
        this.app = _app;
        ctrlButtonHeld = false;
    }

    public void update()
    {
        if (app.getHud().buttonUp.isPressed())
        {
            app.getPlayer().direction.setY(Movement._DIRECTION_UP);
        }
        else if (app.getHud().buttonDown.isPressed())
        {
            app.getPlayer().direction.setY(Movement._DIRECTION_DOWN);
        }
        else
        {
            app.getPlayer().direction.setY(Movement._DIRECTION_STILL);
        }

        if (app.getHud().buttonLeft.isPressed())
        {
            app.getPlayer().direction.setX(Movement._DIRECTION_LEFT);
        }
        else if (app.getHud().buttonRight.isPressed())
        {
            app.getPlayer().direction.setX(Movement._DIRECTION_RIGHT);
        }
        else
        {
            app.getPlayer().direction.setX(Movement._DIRECTION_STILL);
        }
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
                returnFlag = maingameKeyDown(keycode);
            }
        }

        return returnFlag;
    }

    public boolean maingameKeyDown(int keycode)
    {
        boolean returnFlag;

        if (keycode == UIButtons.defaultValueLeft)
        {
            app.getHud().buttonLeft.press();
            returnFlag = true;
        }
        else if (keycode == UIButtons.defaultValueRight)
        {
            app.getHud().buttonRight.press();
            returnFlag = true;
        }
        else if (keycode == UIButtons.defaultValueUp)
        {
            app.getHud().buttonUp.press();
            returnFlag = true;
        }
        else if (keycode == UIButtons.defaultValueDown)
        {
            app.getHud().buttonDown.press();
            returnFlag = true;
        }
        else if (keycode == UIButtons.defaultValueB)
        {
            app.getHud().buttonB.press();
            returnFlag = true;
        }
        else if (keycode == UIButtons.defaultValueA)
        {
            app.getHud().buttonA.press();
            returnFlag = true;
        }
        else
        {
            switch (keycode)
            {
                case Input.Keys.ESCAPE:
                case Input.Keys.BACK:
                {
                    app.getHud().buttonPause.press();

                    returnFlag = true;
                }
                break;

                case Input.Keys.STAR:
                {
                    if (Developer.isDevMode())
                    {
                        app.cameraUtils.resetCameraZoom();
                    }

                    returnFlag = true;
                }
                break;

                case Input.Keys.K:
                {
                    if (Developer.isDevMode())
                    {
                        app.getPlayer().kill();
                    }

                    returnFlag = true;
                }
                break;

                case Input.Keys.G:
                {
                    if (Developer.isDevMode())
                    {
                        app.gameProgress.gameCompleted = true;
                    }

                    returnFlag = true;
                }
                break;

                case Input.Keys.CONTROL_LEFT:
                case Input.Keys.CONTROL_RIGHT:
                {
                    ctrlButtonHeld = true;
                    returnFlag = true;
                }
                break;

                case Input.Keys.MENU:
                case Input.Keys.HOME:
                default:
                {
                    returnFlag = false;
                }
                break;
            }
        }

        return returnFlag;
    }

    public boolean maingameKeyUp(int keycode)
    {
        boolean returnFlag;

        if (keycode == UIButtons.defaultValueLeft)
        {
            app.getHud().buttonLeft.release();
            returnFlag = true;
        }
        else if (keycode == UIButtons.defaultValueRight)
        {
            app.getHud().buttonRight.release();
            returnFlag = true;
        }
        else if (keycode == UIButtons.defaultValueUp)
        {
            app.getHud().buttonUp.release();
            returnFlag = true;
        }
        else if (keycode == UIButtons.defaultValueDown)
        {
            app.getHud().buttonDown.release();
            returnFlag = true;
        }
        else if (keycode == UIButtons.defaultValueB)
        {
            app.getHud().buttonB.release();
            returnFlag = true;
        }
        else if (keycode == UIButtons.defaultValueA)
        {
            app.getHud().buttonA.release();
            returnFlag = true;
        }
        else
        {
            switch (keycode)
            {
                case Input.Keys.ESCAPE:
                case Input.Keys.BACK:
                {
                    app.getHud().buttonPause.release();

                    returnFlag = true;
                }
                break;

                case Input.Keys.CONTROL_LEFT:
                case Input.Keys.CONTROL_RIGHT:
                {
                    ctrlButtonHeld = false;
                    returnFlag = true;
                }
                break;

                case Input.Keys.NUM_1:
                case Input.Keys.MENU:
                case Input.Keys.HOME:
                default:
                {
                    returnFlag = false;
                }
                break;
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
                returnFlag = maingameKeyUp(keycode);
            }
        }

        return returnFlag;
    }

    @Override
    public boolean touchDown(int touchX, int touchY, int pointer, int button)
    {
        Vector2 newPoints = new Vector2(touchX, touchY);
        newPoints = app.baseRenderer.hudGameCamera.viewport.unproject(newPoints);

        int screenX = (int) (newPoints.x - app.mapData.mapPosition.getX());
        int screenY = (int) (newPoints.y - app.mapData.mapPosition.getY());

        boolean returnFlag = false;

        if (app.currentScreenID == ScreenID._TITLE_SCREEN)
        {
            returnFlag = app.inputManager.touchScreen.titleScreenTouchDown(screenX, screenY);
        }
        else if (app.currentScreenID == ScreenID._GAME_SCREEN)
        {
            returnFlag = app.inputManager.touchScreen.gameScreenTouchDown(screenX, screenY, pointer);
        }

        return returnFlag;
    }

    @Override
    public boolean touchUp(int touchX, int touchY, int pointer, int button)
    {
        Vector2 newPoints = new Vector2(touchX, touchY);
        newPoints = app.baseRenderer.hudGameCamera.viewport.unproject(newPoints);

        int screenX = (int) (newPoints.x - app.mapData.mapPosition.getX());
        int screenY = (int) (newPoints.y - app.mapData.mapPosition.getY());

        boolean returnFlag = false;

        if (app.currentScreenID == ScreenID._TITLE_SCREEN)
        {
            returnFlag = app.inputManager.touchScreen.titleScreenTouchUp(screenX, screenY);
        }
        else if (app.currentScreenID == ScreenID._GAME_SCREEN)
        {
            returnFlag = app.inputManager.touchScreen.gameScreenTouchUp(screenX, screenY);
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
//        Vector2 newPoints = new Vector2(screenX, screenY);
//        newPoints = app.baseRenderer.hudGameCamera.viewport.unproject(newPoints);
//
//        int touchX = (int) (newPoints.x - app.mapData.mapPosition.getX());
//        int touchY = (int) (newPoints.y - app.mapData.mapPosition.getY());
//
//        boolean returnFlag = false;
//
//        if (app.currentScreenID == ScreenID._GAME_SCREEN)
//        {
//            if ((app.getHud().buttonB.pointer == pointer)
//                && !app.getHud().buttonB.contains(touchX, touchY))
//            {
//                app.getHud().buttonB.release();
//                returnFlag = true;
//            }
//        }
//
//        return returnFlag;

        return false;
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

            app.inputManager.mouseWorldPosition.set(newPoints.x, newPoints.y);

            int touchX = (int) (newPoints.x - app.mapData.mapPosition.getX());
            int touchY = (int) (newPoints.y - app.mapData.mapPosition.getY());

            app.inputManager.mousePosition.set(touchX, touchY);
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
            if (Developer.isDevMode() && ctrlButtonHeld)
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

    private Movement.Dir evaluateKeyboardDirection()
    {
        Direction direction = new Direction
            (
                (int) app.inputManager._horizontalValue,
                (int) app.inputManager._verticalValue
            );

        Movement.Dir keyDir = DirectionMap.map[DirectionMap.map.length - 1].translated;

        for (DirectionValue dv : DirectionMap.map)
        {
            if ((dv.dirX == direction.getX()) && (dv.dirY == direction.getY()))
            {
                keyDir = dv.translated;
            }
        }

        app.inputManager.lastRegisteredDirection = keyDir;

        return keyDir;
    }

    public void translateXPercent()
    {
        app.inputManager._horizontalValue = app.getPlayer().direction.getX();
    }

    public void translateYPercent()
    {
        app.inputManager._verticalValue = app.getPlayer().direction.getY();
    }
}
