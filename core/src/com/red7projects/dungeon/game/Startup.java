/*
 *  Copyright 31/01/2019 Red7Projects.
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

package com.red7projects.dungeon.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.red7projects.dungeon.assets.AssetLoader;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.config.Preferences;
import com.red7projects.dungeon.config.Settings;
import com.red7projects.dungeon.development.DebugRenderer;
import com.red7projects.dungeon.development.Developer;
import com.red7projects.dungeon.entities.EntityData;
import com.red7projects.dungeon.graphics.CameraUtils;
import com.red7projects.dungeon.graphics.ScreenUtils;
import com.red7projects.dungeon.graphics.camera.Shake;
import com.red7projects.dungeon.graphics.renderers.BaseRenderer;
import com.red7projects.dungeon.input.InputManager;
import com.red7projects.dungeon.logging.Trace;
import com.red7projects.dungeon.map.MapData;
import com.red7projects.dungeon.map.MapUtils;
import com.red7projects.dungeon.map.RoomManager;
import com.red7projects.dungeon.screens.MainGameScreen;
import com.red7projects.dungeon.screens.MainMenuScreen;

//@formatter:off
public class Startup
{
    private App app;

    Startup(App _app)
    {
        this.app = _app;
    }

    void startApp()
    {
        //
        // Initialise DEBUG classes
        //noinspection LibGDXLogLevel
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Trace.openLogfile(AppConfig._DEBUG_LOG_FILE);
        Trace.__FILE_FUNC_WithDivider();

        Settings.initialise();

        app.preferences = new Preferences();
        app.assets      = new AssetLoader();
        app.spriteBatch = new SpriteBatch();

        AppConfig.setup(app);

        app.googleServices.setup(app);
        app.googleServices.createApiClient();

        app.screenUtils    = new ScreenUtils(app);
        app.cameraUtils    = new CameraUtils(app);
        app.baseRenderer   = new BaseRenderer(app);
        app.worldModel     = new WorldModel(app);
        app.entityData     = new EntityData(app);
        app.inputManager   = new InputManager(app);
        app.mapData        = new MapData(app);
        app.mapUtils       = new MapUtils(app);
        app.gameListener   = new GameListener(app);
        app.highScoreUtils = new HighScoreUtils(app);
        app.roomManager    = new RoomManager();
        app.gameProgress   = new GameProgress(app);
        app.mainMenuScreen = new MainMenuScreen(app);
        app.mainGameScreen = new MainGameScreen(app);

        Sfx.inst().setup(app);
        DebugRenderer.setup(app);

        AppConfig.freshInstallCheck();
        Shake.setAllowed(false);

        //
        // Turn OFF the splash screen
        AppConfig.isShowingSplashScreen = false;

        Trace.divider();
    }

    public void close()
    {
        if (Developer.isDevMode() && app.preferences.isEnabled(Preferences._DISABLE_MENU_SCREEN))
        {
            app.setScreen(app.mainGameScreen);
        }
        else
        {
            app.setScreen(app.mainMenuScreen);
        }
    }
}