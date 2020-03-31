/*
 *  Copyright 26/04/2018 Red7Projects.
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

package com.red7projects.dungeon.entities.managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.config.Preferences;
import com.red7projects.dungeon.entities.hero.MainPlayer;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.GraphicID;

public class PlayerManager
{
    private EntityDescriptor entityDescriptor;
    private App app;

    public PlayerManager(final App _app)
    {
        this.app = _app;
    }

    public void createPlayer()
    {
        app.entityManager._playerIndex = 0;
        app.entityManager._playerReady = false;

        //
        // Player is always enabled on Android builds,
        // but can be disabled on Desktop builds for debug purposes.
        if (AppConfig.isAndroidApp() || app.preferences.isEnabled(Preferences._PLAYER))
        {
            MainPlayer mainPlayer = new MainPlayer(app);
            mainPlayer.initialise(entityDescriptor);

            app.entityData.addEntity(mainPlayer);
            app.entityManager.updateIndexes();
            app.entityManager._playerReady  = true;

            mainPlayer.addCollisionListener(mainPlayer.collision);
        }
        else
        {
            app.entityManager._playerIndex  = 0;
            app.entityManager._playerReady  = false;
        }
    }

    /**
     * Set the player tile into the map
     */
    public void setSpawnPoint()
    {
        entityDescriptor                    = new EntityDescriptor();
        entityDescriptor._ASSET             = app.assets.getAnimationsAtlas().findRegion(GameAssets._IDLE_DOWN_ASSET);
        entityDescriptor._FRAMES            = GameAssets._PLAYER_STAND_FRAMES;
        entityDescriptor._PLAYMODE          = Animation.PlayMode.LOOP;
        entityDescriptor._X                 = (int) app.getRoomSystem().getStartPosition().x;
        entityDescriptor._Y                 = (int) app.getRoomSystem().getStartPosition().y;
        entityDescriptor._Z                 = app.entityUtils.getInitialZPosition(GraphicID.G_PLAYER);
        entityDescriptor._INDEX             = app.entityData.entityMap.size;
        entityDescriptor._ENEMY             = app.entityUtils.setEnemyStatus(GraphicID.G_PLAYER);
        entityDescriptor._UPDATEABLE        = app.entityUtils.canUpdate(GraphicID.G_PLAYER);
        entityDescriptor._SIZE              = GameAssets.getAssetSize(GraphicID.G_PLAYER);
        entityDescriptor._MAIN_CHARACTER    = true;
    }
}
