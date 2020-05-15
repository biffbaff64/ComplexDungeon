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

package com.red7projects.dungeon.config;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.input.objects.ControllerPos;
import com.red7projects.dungeon.input.objects.ControllerType;
import com.red7projects.dungeon.utils.development.Developer;
import com.red7projects.dungeon.utils.logging.Stats;
import com.red7projects.dungeon.utils.logging.Trace;

public abstract class AppConfig
{

    // =================================================================
    //
    public static final String _PREFS_FILE_NAME = "com.red7projects.dungeon.preferences";
    public static final String _DEBUG_LOG_FILE  = "debuglogfile.txt";

    public static boolean        quitToMainMenu;            // Game over, back to menu screen
    public static boolean        forceQuitToMenu;           // Quit to main menu, forced via pause mode for example.
    public static boolean        gamePaused;                // TRUE / FALSE Game Paused flag
    public static boolean        camerasReady;              // TRUE when all cameras have been created.
    public static boolean        shutDownActive;            // TRUE if game is currently processing EXIT request.
    public static boolean        isUsingBOX2DPhysics;       // ...
    public static boolean        isUsingAshleyECS;          // ...
    public static boolean        entitiesExist;             // Set true when all entities have been created
    public static boolean        hudExists;                 // Set true when HUD has finished setup
    public static boolean        controllersFitted;         // TRUE if external controllers are fitted/connected.
    public static boolean        gameButtonsReady;          // TRUE When all game buttons have been defined
    public static String         usedController;            // The name of the controller being used
    public static ControllerPos  virtualControllerPos;      // Virtual (on-screen) joystick position (LEFT or RIGHT)

    public static boolean        isShowingSplashScreen;     //
    public static boolean        menuScreenActive;          //
    public static boolean        gameScreenActive;          //
    public static boolean        finalScreenActive;         //
    public static boolean        hiscoresPageActive;        //
    public static boolean        optionsPageActive;         //
    public static boolean        creditsPageActive;         //
    public static boolean        developerPanelActive;      //
    public static boolean        debugConsoleActive;        //

    public static Array<ControllerType> availableInputs;

    // =================================================================
    //

    private static App app;

    public static void setup(App _app)
    {
        Trace.__FILE_FUNC();

        app = _app;

        quitToMainMenu       = false;
        forceQuitToMenu      = false;
        gamePaused           = false;
        camerasReady         = false;
        shutDownActive       = false;
        entitiesExist        = false;
        hudExists            = false;
        menuScreenActive     = false;
        gameScreenActive     = false;
        finalScreenActive    = false;
        hiscoresPageActive   = false;
        optionsPageActive    = false;
        creditsPageActive    = false;
        developerPanelActive = false;
        debugConsoleActive   = false;
        controllersFitted    = false;
        gameButtonsReady     = false;
        usedController       = "None";

        //
        // Set _DEVMODE from the _DEV_MODE Environment variable.
        // Set _LAPTOP from the _MACHINE Environment variable.
        Developer.setMode(_app);

        availableInputs = new Array<>();

        if (isAndroidApp() || (Developer.isDevMode() && Developer.isLaptop()))
        {
            availableInputs.add(ControllerType._VIRTUAL);

            virtualControllerPos = ControllerPos._LEFT;
        }
        else
        {
            availableInputs.add(ControllerType._EXTERNAL);
            availableInputs.add(ControllerType._KEYBOARD);

            virtualControllerPos = ControllerPos._HIDDEN;

            Pixmap pixmap = new Pixmap(Gdx.files.internal("data/crosshairs.png"));
            Cursor cursor = Gdx.graphics.newCursor(pixmap, (pixmap.getWidth() / 2), (pixmap.getHeight() / 2));
            Gdx.graphics.setCursor(cursor);
            pixmap.dispose();
        }

        // ###################
        app.settings.disable(Settings._BOX2D_PHYSICS);
        app.settings.disable(Settings._B2D_RENDERER);
        app.settings.disable(Settings._USING_ASHLEY_ECS);
        // ###################

        Stats.setup();

        isUsingBOX2DPhysics = app.settings.isEnabled(Settings._BOX2D_PHYSICS);
        isUsingAshleyECS    = app.settings.isEnabled(Settings._USING_ASHLEY_ECS);

        if (Developer.isDevMode())
        {
            Trace.divider();
            Trace.dbg("Android App         : " + isAndroidApp());
            Trace.dbg("Desktop App         : " + isDesktopApp());
            Trace.divider();
            Trace.dbg("isDevMode()         : " + Developer.isDevMode());
            Trace.dbg("isGodMode()         : " + Developer.isGodMode());
            Trace.divider();
            Trace.dbg("isUsingAshleyECS    : " + isUsingAshleyECS);
            Trace.dbg("isUsingBOX2DPhysics : " + isUsingBOX2DPhysics);
            Trace.divider();
            Trace.dbg("_DESKTOP_WIDTH      : " + Gfx._DESKTOP_WIDTH);
            Trace.dbg("_DESKTOP_HEIGHT     : " + Gfx._DESKTOP_HEIGHT);
            Trace.dbg("_VIEW_WIDTH         : " + Gfx._VIEW_WIDTH);
            Trace.dbg("_VIEW_HEIGHT        : " + Gfx._VIEW_HEIGHT);
            Trace.divider();
            Trace.dbg("_VIRTUAL?           : " + availableInputs.contains(ControllerType._VIRTUAL, true));
            Trace.dbg("_EXTERNAL?          : " + availableInputs.contains(ControllerType._EXTERNAL, true));
            Trace.dbg("_KEYBOARD?          : " + availableInputs.contains(ControllerType._KEYBOARD, true));
            Trace.dbg("controllerPos       : " + virtualControllerPos);
            Trace.dbg("controllersFitted   : " + controllersFitted);
            Trace.dbg("usedController      : " + usedController);
            Trace.divider();
        }
    }

    public static void freshInstallCheck()
    {
        Trace.__FILE_FUNC();

        if (!app.settings.isEnabled(Settings._INSTALLED))
        {
            Trace.dbg("FRESH INSTALL.");

            Trace.dbg("Initialising all App settings to default values.");
            app.settings.resetToDefaults();

            Trace.dbg("Setting all Statistical logging meters to zero.");
            Stats.resetAllMeters();

            app.settings.enable(Settings._INSTALLED);
        }
    }

    /**
     * Pause the game
     */
    public static void pause()
    {
        app.mainGameScreen.getGameState().set(StateID._STATE_PAUSED);
        app.getHud().hudStateID = StateID._STATE_PAUSED;
        gamePaused = true;
    }

    /**
     * Un-pause the game
     */
    public static void unPause()
    {
        app.mainGameScreen.getGameState().set(StateID._STATE_GAME);
        app.getHud().hudStateID = StateID._STATE_PANEL_UPDATE;
        gamePaused = false;
    }

    /**
     * @return TRUE if the app is running on Desktop
     */
    public static boolean isDesktopApp()
    {
        return (Gdx.app.getType() == Application.ApplicationType.Desktop);
    }

    /**
     * @return TRUE if the app is running on Android
     */
    public static boolean isAndroidApp()
    {
        return (Gdx.app.getType() == Application.ApplicationType.Android);
    }

    public static void dispose()
    {
        availableInputs.clear();

        usedController = null;
        availableInputs = null;
        virtualControllerPos = null;
    }
}
