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

package com.red7projects.dungeon.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.red7projects.dungeon.assets.Assets;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.config.Preferences;
import com.red7projects.dungeon.development.Developer;
import com.red7projects.dungeon.development.DeveloperPanel;
import com.red7projects.dungeon.entities.EntityData;
import com.red7projects.dungeon.entities.EntityManager;
import com.red7projects.dungeon.entities.utils.EntityUtils;
import com.red7projects.dungeon.entities.hero.MainPlayer;
import com.red7projects.dungeon.google.AdsController;
import com.red7projects.dungeon.google.PlayServices;
import com.red7projects.dungeon.graphics.CameraUtils;
import com.red7projects.dungeon.graphics.renderers.BaseRenderer;
import com.red7projects.dungeon.input.GameController;
import com.red7projects.dungeon.input.InputManager;
import com.red7projects.dungeon.input.TouchScreen;
import com.red7projects.dungeon.input.VirtualJoystick;
import com.red7projects.dungeon.logging.Trace;
import com.red7projects.dungeon.map.*;
import com.red7projects.dungeon.physics.CollisionUtils;
import com.red7projects.dungeon.physics.box2d.BodyBuilder;
import com.red7projects.dungeon.physics.box2d.Box2DContactListener;
import com.red7projects.dungeon.physics.box2d.Box2DEntityHelper;
import com.red7projects.dungeon.screens.MainGameScreen;
import com.red7projects.dungeon.screens.MainMenuScreen;
import com.red7projects.dungeon.screens.ScreenID;
import com.red7projects.dungeon.ui.HeadsUpDisplay;

@SuppressWarnings({"WeakerAccess"})
//@formatter:off
public abstract class App extends com.badlogic.gdx.Game
{
    // =======================================================
    // Global access references
    //
    public World          box2DWorld;
    public Stage          stage;
    public SpriteBatch    spriteBatch;
    public Assets         assets;
    public ScreenID       currentScreenID;
    public BaseRenderer   baseRenderer;
    public GameListener   gameListener;
    public WorldModel     worldModel;
    public Preferences    preferences;
    public CameraUtils    cameraUtils;
    public MapUtils       mapUtils;
    public MainMenuScreen mainMenuScreen;
    public MainGameScreen mainGameScreen;
    public GameProgress   gameProgress;
    public AdsController  adsController;
    public PlayServices   googleServices;

    public InputManager    inputManager;

    //
    // Globals to be made available when MainGameScreen is active.
    // These must be released when MainGameScreen is destroyed.
    public HeadsUpDisplay       hud;
    public RoomManager          roomManager;

    // TODO: 28/02/2019 - Try and remove the need for as many of these as possible
    public Box2DContactListener box2DContactListener;
    public Box2DEntityHelper    box2DEntityHelper;
    public BodyBuilder          bodyBuilder;
    public MapData              mapData;
    public CollisionUtils       collisionUtils;
    public PathUtils            pathUtils;
    public HighScoreUtils       highScoreUtils;
    public EntityManager        entityManager;
    public EntityUtils          entityUtils;
    public EntityData           entityData;
    public GameUtils            gameUtils;

    public MainPlayer getPlayer()
    {
        MainPlayer player = null;

        if ((entityData.entityMap != null)
            && (entityData.entityMap.size > 0)
            && (entityData.entityMap.get(entityManager._playerIndex) != null)
            && (entityData.entityMap.get(entityManager._playerIndex) instanceof MainPlayer))
        {
            player = ((MainPlayer) entityData.entityMap.get(entityManager._playerIndex));
        }

        return player;
    }

    /**
     * Returns the current number of lives left.
     *
     * @return the lives.
     */
    public int getLives()
    {
        int lives;

        if (Developer.isDevMode() && preferences.prefs.getBoolean(Preferences._GOD_MODE))
        {
            lives = Constants._MAX_LIVES;
        }
        else
        {
            lives = gameProgress.lives.getTotal();
        }

        return lives;
    }

    /**
     * Return the current game level.
     *
     * @return the level.
     */
    public int getLevel()
    {
        int level;

        if (Developer.isDevMode() && preferences.prefs.getBoolean(Preferences._TEST_LEVEL))
        {
            level = 1;
        }
        else
        {
            level =  gameProgress.playerLevel;
        }

        return level;
    }

    public HeadsUpDisplay getHud()
    {
        return hud;
    }

    public RoomManager getRoomSystem()
    {
        return roomManager;
    }

    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

        mainGameScreen.dispose();
        mainMenuScreen.dispose();

        AppConfig.dispose();
    }
}
