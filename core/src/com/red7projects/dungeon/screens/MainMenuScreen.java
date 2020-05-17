
package com.red7projects.dungeon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
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
import com.red7projects.dungeon.input.objects.ControllerType;
import com.red7projects.dungeon.ui.ExitPanel;
import com.red7projects.dungeon.ui.Scene2DUtils;
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

    private ImageButton       buttonExit;
    private ExitPanel         exitPanel;
    private MenuPage          menuPage;
    private OptionsPage       optionsPage;
    private Texture           background;
    private Texture           overlay1;
    private Texture           overlay2;
    private StarField         starField;
    private ArrayList<UIPage> panels;
    private int               currentPage;

    public MainMenuScreen(final App _app)
    {
        super(_app);
    }

    @Override
    public void initialise()
    {
        Trace.__FILE_FUNC();

        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            app.inputManager.virtualJoystick.hide();
        }

        Scene2DUtils scene2DUtils = new Scene2DUtils(app);
        buttonExit = scene2DUtils.addButton("new_back_button", "new_back_button_pressed", 20, 1280);
        buttonExit.setVisible(false);
        buttonExit.setTouchable(Touchable.disabled);

        menuPage    = new MenuPage(app);
        optionsPage = new OptionsPage(app);
        panels      = new ArrayList<>();
        starField   = new StarField(app);

        panels.add(_MENU_PAGE, menuPage);
        panels.add(_HISCORE_PAGE, new HiscorePage(app));
        panels.add(_CREDITS_PAGE, new CreditsPage(app));
        panels.add(_OPTIONS_PAGE, optionsPage);

        if (AppConfig.isAndroidApp())
        {
            app.googleServices.signInSilently();
        }

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

        if (app.appState.get() == StateID._STATE_TITLE_SCREEN)
        {
            StateID tempState;

            if ((tempState = update(app.appState).get()) != StateID._STATE_TITLE_SCREEN)
            {
                app.appState.set(tempState);
            }

            super.render(delta);
        }
    }

    private StateManager update(final StateManager state)
    {
        if (!Sfx.inst().isTunePlaying(Sfx.MUSIC_TITLE))
        {
            Sfx.inst().playTitleTune(true);
        }

        if (state.get() == StateID._STATE_TITLE_SCREEN)
        {
            panels.get(currentPage).update();

            switch (currentPage)
            {
                case _MENU_PAGE:
                case _CREDITS_PAGE:
                case _HISCORE_PAGE:
                case _OPTIONS_PAGE:
                break;

                case _EXIT_PAGE:
                {
                    int option = exitPanel.getExitOption();

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
                    Trace.__FILE_FUNC();
                    Trace.dbg("Unsupported UI Page: " + currentPage);
                }
                break;
            }

            //
            // If currently showing Hiscore or Credits pages, return to menupage
            // if the screen is tapped (or controller start button pressed)
            if (UIButtons.fullScreenButton.isPressed()
                || UIButtons.controllerFirePressed
                || UIButtons.controllerStartPressed
                || buttonExit.isPressed()
                || ((menuPage.buttonStart != null) && menuPage.buttonStart.isPressed()))
            {
                if (currentPage != _MENU_PAGE)
                {
                    changePageTo(_MENU_PAGE);

                    UIButtons.controllerFirePressed  = false;
                    UIButtons.controllerStartPressed = false;
                }
                else
                {
                    if (menuPage.menuState.get().equals(StateID._STATE_MENU_UPDATE))
                    {
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

                    }

                    UIButtons.fullScreenButton.release();
                }
            }
            else
            {
                if (menuPage.menuState.get().equals(StateID._STATE_MENU_UPDATE))
                {
                    // If we're still on the title screen...
                    if (state.get() == StateID._STATE_TITLE_SCREEN)
                    {
                        //
                        // Check HISCORES button, open hiscores page if pressed
                        if ((menuPage.buttonHiscores != null) && menuPage.buttonHiscores.isPressed())
                        {
                            changePageTo(_HISCORE_PAGE);

                            menuPage.buttonHiscores.release();
                        }

                        //
                        // Check OPTIONS button, open settings page if pressed
                        if ((menuPage.buttonOptions != null) && menuPage.buttonOptions.isPressed())
                        {
                            changePageTo(_OPTIONS_PAGE);

                            menuPage.buttonOptions.release();
                        }

                        //
                        // Check CREDITS button, open credits page if pressed
                        if ((menuPage.buttonCredits != null) && menuPage.buttonCredits.isPressed())
                        {
                            changePageTo(_CREDITS_PAGE);

                            menuPage.buttonCredits.release();
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

                        if (AppConfig.isAndroidApp())
                        {
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
     * @param camera  The camera to use.
     */
    public void draw(final SpriteBatch spriteBatch, final OrthoGameCamera camera)
    {
        if (app.appState.get() == StateID._STATE_TITLE_SCREEN)
        {
            float originX = (camera.camera.position.x - (float) (Gfx._SMALL_HUD_WIDTH / 2));
            float originY = (camera.camera.position.y - (float) (Gfx._SMALL_HUD_HEIGHT / 2));

            switch (currentPage)
            {
                case _MENU_PAGE:
                case _HISCORE_PAGE:
                case _CREDITS_PAGE:
                case _OPTIONS_PAGE:
                case _EXIT_PAGE:
                {
                    app.spriteBatch.draw(background, originX, originY);
                    app.spriteBatch.draw(overlay1, originX, originY);
                    app.spriteBatch.draw(overlay2, originX, originY);

                    if (exitPanel == null)
                    {
                        panels.get(currentPage).draw(spriteBatch, camera, originX, originY);
                    }
                    else
                    {
                        exitPanel.draw(spriteBatch);
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

        app.currentScreenID = ScreenID._TITLE_SCREEN;
        app.appState.set(StateID._STATE_TITLE_SCREEN);

        super.show();

        AppConfig.menuScreenActive  = true;
        AppConfig.gameScreenActive  = false;
        AppConfig.finalScreenActive = false;

        app.cameraUtils.resetCameraZoom();
        app.cameraUtils.disableAllCameras();
        app.baseRenderer.spriteGameCamera.isInUse = true;
        app.baseRenderer.hudGameCamera.isInUse    = true;
        app.baseRenderer.isDrawingStage           = true;

        initialise();

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

        buttonExit.addAction(Actions.removeActor());
        buttonExit = null;

        background = null;
        overlay1 = null;
        overlay2 = null;

        starField.dispose();
        starField = null;

        exitPanel   = null;
        optionsPage = null;
    }

    /**
     * Calls the hide method for main front end pages.
     */
    private void hideAllPages()
    {
        if (panels != null)
        {
            for (UIPage page : panels)
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

            buttonExit.setVisible(currentPage != _MENU_PAGE);
            buttonExit.setTouchable((currentPage == _MENU_PAGE) ? Touchable.disabled : Touchable.enabled);
        }
    }
}
