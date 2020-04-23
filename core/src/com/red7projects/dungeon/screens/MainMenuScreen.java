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

package com.red7projects.dungeon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.config.Version;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.Sfx;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.game.StateManager;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.camera.OrthoGameCamera;
import com.red7projects.dungeon.graphics.effects.StarField;
import com.red7projects.dungeon.input.UIButtons;
import com.red7projects.dungeon.ui.ExitPanel;
import com.red7projects.dungeon.ui.UIPage;
import com.red7projects.dungeon.utils.logging.Trace;

import java.util.ArrayList;

/**
 * Main class for handling all actions on the front end screen.
 */
//@formatter:off
public class MainMenuScreen extends AbstractBaseScreen
{
    private static final int _MENU_PAGE      = 0;
    private static final int _HISCORE_PAGE   = 1;
    private static final int _CREDITS_PAGE   = 2;
    private static final int _NUM_MAIN_PAGES = 3;
    private static final int _OPTIONS_PAGE   = 3;
    private static final int _EXIT_PAGE      = 4;

    private ExitPanel         exitPanel;
    private MenuPage          menuPage;
    private OptionsPage       optionsPage;
    private StateManager      gameState;
    private Texture           background;
    private Texture           overlay1;
    private Texture           overlay2;
    private StarField         starField;
    private ArrayList<UIPage> panels;
    private int               currentPage;

    public MainMenuScreen(final App _app)
    {
        super(_app);

        this.gameState = new StateManager();
    }

    @Override
    public void initialise()
    {
        Trace.__FILE_FUNC();

        app.inputManager.virtualJoystick.hide();

        optionsPage = new OptionsPage(app);
        menuPage    = new MenuPage(app);
        panels      = new ArrayList<>();
        starField   = new StarField(app);

        panels.add(_MENU_PAGE, menuPage);
        panels.add(_HISCORE_PAGE, new HiscorePage(app));
        panels.add(_CREDITS_PAGE, new CreditsPage(app));
        panels.add(_OPTIONS_PAGE, optionsPage);

        app.googleServices.signInSilently();

        AppConfig.isPoweringUp = false;

        app.mapData.mapPosition.set(0, 0);
    }

    /**
     * Update and draw the screen.
     *
     * @param delta elapsed time since last update
     */
    @Override
    public void render(final float delta)
    {
        super.update();

        if (gameState.get() == StateID._STATE_TITLE_SCREEN)
        {
            StateID tempState;

            if ((tempState = update(gameState).get()) != StateID._STATE_TITLE_SCREEN)
            {
                gameState.set(tempState);
            }

            super.render(delta);
        }
    }

    private StateManager update(final StateManager state)
    {
        if (!Sfx.inst().isTunePlaying(Sfx.inst().MUSIC_TITLE))
        {
            Sfx.inst().playTitleTune(true);
        }

        if (state.get() == StateID._STATE_TITLE_SCREEN)
        {
            switch (currentPage)
            {
                case _MENU_PAGE:
                case _HISCORE_PAGE:
                case _CREDITS_PAGE:
                {
                    if (panels.get(currentPage).update())
                    {
                        panels.get(currentPage).reset();

                        changePageTo((currentPage + 1) % _NUM_MAIN_PAGES);
                    }
                }
                break;

                case _OPTIONS_PAGE:
                {
                    optionsPage.update();

                    if (!AppConfig.optionsPageActive)
                    {
                        changePageTo(_MENU_PAGE);
                    }
                }
                break;

                case _EXIT_PAGE:
                {
                    int option = exitPanel.update();

                    if (option == ExitPanel._YES_PRESSED)
                    {
                        exitPanel.dispose();
                        AppConfig.shutDownActive = true;

                        Gdx.app.exit();
                    }
                    else if (option == ExitPanel._NO_PRESSED)
                    {
                        exitPanel.close();
                        exitPanel = null;

                        currentPage = _MENU_PAGE;

                        panels.get(currentPage).show();
                    }
                }
                break;

                default:
                {
                    // TODO: 09/01/2019 - Add error handling here for illegal panel
                }
                break;
            }

            //
            // If currently showing Hiscore or Credits pages, return to menupage
            // if the screen is tapped (or controller start button pressed)
            if (UIButtons.fullScreenButton.isPressed() || UIButtons.controllerFirePressed || UIButtons.controllerStartPressed)
            {
                if ((currentPage == _HISCORE_PAGE) || (currentPage == _CREDITS_PAGE))
                {
                    changePageTo(_MENU_PAGE);

                    UIButtons.controllerFirePressed  = false;
                    UIButtons.controllerStartPressed = false;
                }

                UIButtons.fullScreenButton.release();
            }

            //
            // Start button(s) check
            if ((menuPage.buttonStart != null) && menuPage.buttonStart.isPressed())
            {
                Trace.divider('#', 100);
                Trace.dbg(" ***** START PRESSED ***** ");
                Trace.divider('#', 100);

                Sfx.inst().playTitleTune(false);

                menuPage.buttonStart.release();

                app.mainGameScreen.reset();
                app.mainGameScreen.firstTime = true;
                app.setScreen(app.mainGameScreen);
            }
            else
            {
                // If we're still on the title screen...
                if (state.get() == StateID._STATE_TITLE_SCREEN)
                {
                    //
                    // Check OPTIONS button, open settings page if pressed
                    if ((menuPage.buttonOptions != null) && menuPage.buttonOptions.isPressed())
                    {
                        changePageTo(_OPTIONS_PAGE);

                        menuPage.buttonOptions.release();
                    }

                    //
                    // Check EXIT button, open exit panel if pressed
                    if ((menuPage.buttonExit != null) && menuPage.buttonExit.isPressed())
                    {
                        panels.get(currentPage).hide();

                        exitPanel = new ExitPanel(app);
                        exitPanel.open();

                        currentPage = _EXIT_PAGE;

                        menuPage.buttonExit.release();
                    }

                    //
                    // Check GOOGLE SIGN-IN button
                    if ((menuPage.buttonGoogle != null) && menuPage.buttonGoogle.isPressed())
                    {
                        menuPage.buttonGoogle.release();

                        if (!app.googleServices.isSignedIn())
                        {
                            app.googleServices.signIn();
                        }
                    }
                }
            }
        }
        else
        {
            Trace.__FILE_FUNC();
            Trace.dbg("Unsupported game state: " + state.get());
        }

        return state;
    }

    /**
     * Draw the currently active page.
     *
     * @param spriteBatch The spritebatch to use.
     * @param gameCamera  The camera to use.
     */
    public void draw(final SpriteBatch spriteBatch, final OrthoGameCamera gameCamera)
    {
        if (gameState.get() == StateID._STATE_TITLE_SCREEN)
        {
            switch (currentPage)
            {
                case _MENU_PAGE:
                case _HISCORE_PAGE:
                case _CREDITS_PAGE:
                case _OPTIONS_PAGE:
                case _EXIT_PAGE:
                {
                    if ("Sprite Cam".equals(gameCamera.name))
                    {
                        if (background != null)
                        {
                            spriteBatch.draw
                                    (
                                        background,
                                        gameCamera.camera.position.x - (float) Gfx._VIEW_HALF_WIDTH,
                                        gameCamera.camera.position.y - (float) Gfx._VIEW_HALF_HEIGHT,
                                        Gfx._VIEW_WIDTH,
                                        Gfx._VIEW_HEIGHT
                                    );
                        }

                        starField.render();

                        if (overlay1 != null)
                        {
                            spriteBatch.draw
                                (
                                    overlay1,
                                    gameCamera.camera.position.x - (float) Gfx._VIEW_HALF_WIDTH,
                                    gameCamera.camera.position.y - (float) Gfx._VIEW_HALF_HEIGHT,
                                    Gfx._VIEW_WIDTH,
                                    Gfx._VIEW_HEIGHT
                                );
                        }

                        if (overlay2 != null)
                        {
                            spriteBatch.draw
                                (
                                    overlay2,
                                    gameCamera.camera.position.x - (float) Gfx._VIEW_HALF_WIDTH,
                                    gameCamera.camera.position.y - (float) Gfx._VIEW_HALF_HEIGHT,
                                    Gfx._VIEW_WIDTH,
                                    Gfx._VIEW_HEIGHT
                                );
                        }
                    }
                    else
                    {
                        if (exitPanel == null)
                        {
                            panels.get(currentPage).draw(spriteBatch);
                        }
                        else
                        {
                            exitPanel.draw(spriteBatch);
                        }
                    }
                }
                break;

                default:
                {
                    Trace.__FILE_FUNC("ERROR: Illegal panel: " + currentPage);
                }
                break;
            }
        }
    }

    @Override
    public void show()
    {
        Trace.__FILE_FUNC();

        if (gameState == null)
        {
            this.gameState = new StateManager();
        }

        app.currentScreenID = ScreenID._TITLE_SCREEN;
        gameState.set(StateID._STATE_TITLE_SCREEN);

        super.show();

        AppConfig.menuScreenActive  = true;
        AppConfig.gameScreenActive  = false;
        AppConfig.finalScreenActive = false;

        initialise();

        app.cameraUtils.resetCameraZoom();
        app.cameraUtils.disableAllCameras();
        app.baseRenderer.spriteGameCamera.isInUse = true;
        app.baseRenderer.hudGameCamera.isInUse    = true;
        app.baseRenderer.isDrawingStage           = true;

        currentPage = (app.highScoreUtils.canAddNewEntry()) ? _HISCORE_PAGE : _MENU_PAGE;

        panels.get(currentPage).show();

        Version.appDetails(app);
    }

    @Override
    public void hide()
    {
        Trace.__FILE_FUNC();

        super.hide();

        dispose();
    }

    /**
     * Gets the current game state.
     *
     * @return StateManager value.
     */
    @Override
    public StateManager getGameState()
    {
        return gameState;
    }

    @Override
    public void loadImages()
    {
        background = app.assets.loadSingleAsset("data/full_moon_scene.png", Texture.class);
        overlay1 = app.assets.loadSingleAsset("data/title_overlay1.png", Texture.class);
        overlay2 = app.assets.loadSingleAsset("data/title_overlay2.png", Texture.class);
    }

    /**
     * Clear up all used resources
     */
    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

        super.dispose();

        hideAllPages();

        app.assets.unloadAsset("data/full_moon_scene.png");
        app.assets.unloadAsset("data/title_overlay1.png");
        app.assets.unloadAsset("data/title_overlay2.png");

        background = null;
        overlay1 = null;
        overlay2 = null;

        starField.dispose();
        starField = null;

        exitPanel   = null;
        optionsPage = null;
        gameState   = null;
    }

    /**
     * Calls the hide method for main front end pages.
     */
    private void hideAllPages()
    {
        if (panels != null)
        {
            for (final UIPage page : panels)
            {
                page.hide();
                page.dispose();
            }

            panels.clear();
            panels = null;
        }
    }

    private void changePageTo(final int _nextPage)
    {
        if (panels.get(currentPage) != null)
        {
            panels.get(currentPage).hide();
        }

        currentPage = _nextPage;

        if (panels.get(_nextPage) != null)
        {
            panels.get(currentPage).show();
        }
    }
}
