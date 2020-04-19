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
import com.badlogic.gdx.utils.Array;
import com.red7projects.dungeon.utils.development.Developer;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.input.objects.ControllerPos;
import com.red7projects.dungeon.input.objects.ControllerType;
import com.red7projects.dungeon.utils.logging.Stats;
import com.red7projects.dungeon.utils.logging.Trace;

public abstract class AppConfig
{

    // =================================================================
    //
    public static final String _PREFS_FILE_NAME = "com.red7projects.dungeon.preferences";
    public static final String _DEBUG_LOG_FILE  = "debuglogfile.txt";

    public static boolean        isShowingSplashScreen;
    public static boolean        quitToMainMenu;            // Game over, back to menu screen
    public static boolean        forceQuitToMenu;           // Quit to main menu, forced via pause mode for example.
    public static boolean        gamePaused;                // TRUE / FALSE Game Paused flag
    public static boolean        isPoweringUp;              //
    public static boolean        camerasReady;              //
    public static boolean        shutDownActive;            //
    public static boolean        isUsingBOX2DPhysics;       //
    public static boolean        isUsingAshleyECS;          //
    public static boolean        entitiesExist;             // Set true when all entities have been created
    public static boolean        hudExists;                 // Set true when HUD has finished setup
    public static boolean        menuScreenActive;          //
    public static boolean        gameScreenActive;          //
    public static boolean        finalScreenActive;         //
    public static boolean        optionsPageActive;         //
    public static boolean        developerPanelActive;      //
    public static boolean        debugConsoleActive;        //
    public static boolean        canDrawButtonBoxes;        //
    public static boolean        controllersFitted;         //
    public static boolean        gameButtonsReady;          // TRUE When all game buttons have been defined
    public static String         usedController;            // The name of the controller being used
    public static ControllerPos  virtualControllerPos;      // Virtual (on-screen) joystick position (LEFT or RIGHT)

    public static Array<ControllerType> availableInputs;

    // =================================================================
    //

    private static App app;

    public static void setup(App _app)
    {
        Trace.__FILE_FUNC();

        app = _app;

        isPoweringUp         = true;
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
        optionsPageActive    = false;
        developerPanelActive = false;
        debugConsoleActive   = false;
        canDrawButtonBoxes   = false;
        controllersFitted    = false;
        gameButtonsReady     = false;
        usedController       = "None";

        availableInputs = new Array<>();

        if (isAndroidApp())
        {
            availableInputs.add(ControllerType._VIRTUAL);

            virtualControllerPos = ControllerPos._LEFT;
        }
        else
        {
            availableInputs.add(ControllerType._EXTERNAL);
            availableInputs.add(ControllerType._KEYBOARD);
            availableInputs.add(ControllerType._MOUSE);

            virtualControllerPos = ControllerPos._HIDDEN;
        }

        Stats.setup();

        app.preferences.setup(_PREFS_FILE_NAME);

        //
        // Set Debug Mode from the _DEV_MODE Environment variable.
        Developer.setMode(_app);

        isUsingBOX2DPhysics = app.preferences.isEnabled(Preferences._BOX2D_PHYSICS);
        isUsingAshleyECS    = app.preferences.isEnabled((Preferences._USING_ASHLEY_ECS));

        if (Developer.isDevMode())
        {
            Trace.divider();
            Trace.dbg("Android App         : " + isAndroidApp());
            Trace.dbg("Desktop App         : " + isDesktopApp());
            Trace.dbg("Android On Desktop  : " + isAndroidOnDesktop());
            Trace.divider();
            Trace.dbg("isUsingAshleyECS    : " + isUsingAshleyECS);
            Trace.dbg("isUsingBOX2DPhysics : " + isUsingBOX2DPhysics);
            Trace.divider();
            Trace.dbg("_DESKTOP_WIDTH      : " + Gfx._DESKTOP_WIDTH);
            Trace.dbg("_DESKTOP_HEIGHT     : " + Gfx._DESKTOP_HEIGHT);
            Trace.dbg("_VIEW_WIDTH         : " + Gfx._VIEW_WIDTH);
            Trace.dbg("_VIEW_HEIGHT        : " + Gfx._VIEW_HEIGHT);
            Trace.divider();
            Trace.dbg("controllerPos       : " + virtualControllerPos);
            Trace.dbg("controllersFitted   : " + controllersFitted);
            Trace.dbg("usedController      : " + usedController);
            Trace.divider();
        }
    }

    public static void freshInstallCheck()
    {
        Trace.__FILE_FUNC();

        if (!app.preferences.prefs.getBoolean(Preferences._INSTALLED))
        {
            Trace.dbg("FRESH INSTALL.");

            app.preferences.setPrefsToDefault();
            Stats.resetAllMeters();
            app.preferences.enable(Preferences._INSTALLED);
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

    /**
     * @return TRUE if Android version is being
     *          tested on desktop build
     */
    public static boolean isAndroidOnDesktop()
    {
        return app.preferences.isEnabled(Preferences._ANDROID_ON_DESKTOP);
    }

    public static void dispose()
    {
        availableInputs.clear();

        usedController = null;
        availableInputs = null;
        virtualControllerPos = null;
    }
}
