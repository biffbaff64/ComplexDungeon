/*
 *  Copyright 23/12/2018 Red7Projects.
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

import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.entities.EntityManager;
import com.red7projects.dungeon.entities.EntityUtils;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.logging.Trace;
import com.red7projects.dungeon.map.MapCreator;
import com.red7projects.dungeon.map.PathUtils;
import com.red7projects.dungeon.physics.CollisionUtils;
import com.red7projects.dungeon.physics.aabb.AABBData;
import com.red7projects.dungeon.ui.HeadsUpDisplay;

@SuppressWarnings("WeakerAccess")
public class GameUtils
{
    private       MapCreator mapCreator;
    private       boolean    isFirstTime;
    private final App        app;

    public GameUtils(App _app)
    {
        Trace.__FILE_FUNC();

        this.app = _app;

        isFirstTime = true;
    }

    /**
     * Set up everything for a NEW game only.
     */
    public void prepareNewGame()
    {
        Trace.__FILE_FUNC_WithDivider();

        if (isFirstTime)
        {
            //
            // Initialise the room that the game will start in.
            app.getRoomSystem().initialise();

            //
            // Make sure all progress counters are initialised.
            app.gameProgress.resetProgress();

            //
            // Create collision and entity controllers.
            app.collisionUtils = new CollisionUtils(app);
            app.entityUtils    = new EntityUtils(app);
            app.entityManager  = new EntityManager(app);
            app.hud            = new HeadsUpDisplay(app);
            mapCreator         = new MapCreator(app);

            app.cameraUtils.disableAllCameras();
            app.baseRenderer.hudGameCamera.isInUse = true;
            app.baseRenderer.isDrawingStage        = true;

            app.entityData.createData();        // Create the entity maps - nothing is added yet
            AABBData.createData();              // Create collision box map, ready for entities

            app.entityManager.initialiseManagerList();

            app.getHud().createHud();
            app.mapData.update();
        }

        isFirstTime = false;

        Trace.finishedMessage();
    }

    /**
     * Gets the current level ready for playing.
     *
     * @param firstTime TRUE if first call from power-up.
     */
    public void prepareCurrentLevel(boolean firstTime)
    {
        Trace.__FILE_FUNC_WithDivider();
        Trace.dbg("Game Level: " + app.getLevel());
        Trace.dbg("firstTime: " + firstTime);
        Trace.dbg("isRestarting: " + app.gameProgress.isRestarting);
        Trace.dbg("levelCompleted: " + app.gameProgress.levelCompleted);
        Trace.divider(120);
        Trace.dbg("placementTiles: " + mapCreator.placementTiles.size);
        Trace.dbg("entityMap     : " + app.entityData.entityMap.size);
        Trace.dbg("collisionMap  : " + AABBData.boxes().size);
        Trace.dbg("managerList   : " + app.entityData.managerList.size());
        Trace.divider(120);

        if (app.gameProgress.isRestarting)
        {
            restartCurrentLevel();
        }
        else if (firstTime || app.gameProgress.levelCompleted)
        {
            setupForNewLevel(firstTime);
        }

        AppConfig.optionsPageActive    = false;
        AppConfig.developerPanelActive = false;
        AppConfig.gamePaused           = false;
        AppConfig.quitToMainMenu       = false;
        AppConfig.forceQuitToMenu      = false;

        app.gameProgress.isRestarting   = false;
        app.gameProgress.levelCompleted = false;

        //
        // Fetch the player start position from
        // the map room data
        if (app.getPlayer() != null)
        {
            app.getPlayer().initXY.set(app.getRoomSystem().getStartPosition());
            app.getPlayer().initXY.x *= Gfx.getTileWidth();
            app.getPlayer().initXY.y *= Gfx.getTileHeight();

            //
            // Centre the camera on the player
            app.mapUtils.positionAt((int) app.getPlayer().sprite.getX(), (int) app.getPlayer().sprite.getY());
        }
        else
        {
            app.mapUtils.positionAt
                (
                    (int) app.getRoomSystem().getStartPosition().x,
                    (int) app.getRoomSystem().getStartPosition().y
                );
        }

        //
        // Reset the bars.
        if (firstTime)
        {
            app.getHud().getHealthBar().setToMaximum();
            app.getHud().getLivesBar().setToMaximum();
            app.getHud().update();
        }

        Trace.finishedMessage();
    }

    /**
     * Set up the current level ready for starting.
     */
    public void setupForNewLevel(boolean firstTime)
    {
        Trace.__FILE_FUNC();

        app.mapData.initialiseRoom();                  // Load tiled map and create renderer
        mapCreator.createMap();                        // Process the tiled map data

        app.entityManager.initialiseForLevel();

        //
        // Create entity paths if any relevant data
        // exists in the tilemap data.
        app.pathUtils = new PathUtils();
        app.pathUtils.setup(app);

        Trace.finishedMessage();
    }

    public void restartCurrentLevel()
    {
        Trace.__FILE_FUNC();

        //
        // Reset positions etc.
        app.entityUtils.resetAllPositions();

        app.getPlayer().setup();

        if (app.gameProgress.levelCompleted)
        {
            app.entityManager.updateIndexes();
        }

        Trace.finishedMessage();
    }

    /**
     * Actions to perform when a level
     * has been completed.
     * <p>
     * Remove all entities/pickups/etc from the level, but
     * make sure that the main player is untouched.
     */
    public void closeCurrentLevel()
    {
        Trace.__FILE_FUNC();

        AABBData.boxes().setSize(2);
        app.entityData.entityMap.setSize(1);
        mapCreator.placementTiles.clear();

        app.mapData.enemyFreeZones.clear();
        app.mapData.currentMap.dispose();

        Trace.finishedMessage();
    }

    public static int getCount(int currentTotal)
    {
        int count;

        if (currentTotal >= 1000)
        {
            count = 100;
        }
        else if (currentTotal >= 100)
        {
            count = 10;
        }
        else if (currentTotal >= 50)
        {
            count = 5;
        }
        else
        {
            count = 1;
        }

        return count;
    }

    public static int countNonZero(int[] array)
    {
        int count = 0;

        for (int arr : array)
        {
            if (arr > 0)
            {
                count++;
            }
        }

        return count;
    }

    public static int countNonZero(byte[] array)
    {
        int count = 0;

        for (int arr : array)
        {
            if (arr > 0)
            {
                count++;
            }
        }

        return count;
    }

    public MapCreator getMapCreator()
    {
        return mapCreator;
    }
}
