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

package com.red7projects.dungeon.entities;

import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.config.Settings;
import com.red7projects.dungeon.entities.components.EntityManagerComponent;
import com.red7projects.dungeon.entities.ecs.ashley.core.Engine;
import com.red7projects.dungeon.entities.managers.*;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.entities.systems.RenderSystem;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.physics.aabb.AABBData;
import com.red7projects.dungeon.utils.development.Developer;
import com.red7projects.dungeon.utils.logging.Trace;

@SuppressWarnings({"WeakerAccess"})
//@formatter:off
public class EntityManager implements Disposable
{
    public int          _playerIndex;
    public boolean      _playerReady;
    public RenderSystem renderSystem;

    private Engine engine;
    private App    app;

    public EntityManager(App _app)
    {
        Trace.__FILE_FUNC();

        this.app = _app;

        renderSystem = new RenderSystem(app);
    }

    public void initialiseManagerList()
    {
        Trace.__FILE_FUNC();

        // ----------
        app.entityData.managerList.add(new VillageManager(app));
        app.entityData.managerList.add(new PrisonManager(app));
        // ----------
        app.entityData.managerList.add(new PickupManager(app));
        app.entityData.managerList.add(new InteractiveManager(app));
        app.entityData.managerList.add(new DecorationsManager(app));
        // ----------
        app.entityData.managerList.add(new SoldierManager(app));
        app.entityData.managerList.add(new MonstersManager(app));
        // ----------
        app.entityData.managerList.add(new BlocksManager(app));
        app.entityData.managerList.add(new FlamesManager(app));
        app.entityData.managerList.add(new PowerBeamManager(app));
        app.entityData.managerList.add(new TurretManager(app));
        // ----------
    }

    public void initialisePlayer()
    {
        PlayerManager playerManager = new PlayerManager(app);
        playerManager.setSpawnPoint();
        playerManager.createPlayer();
    }

    public void initialiseForLevel()
    {
        Trace.__FILE_FUNC();

        EntityStats.initialise();

        for (final EntityManagerComponent component : app.entityData.managerList)
        {
            if (component.isPlaceable())
            {
                component.init();
                component.create();
            }
        }

        updateIndexes();
        establishLinks();

        Trace.finishedMessage();
    }

    public void updateSprites()
    {
        if (entityUpdateAllowed() && !AppConfig.gamePaused)
        {
            GdxSprite entity;

            //
            // Update all non-player entities.
            for (int i = 0; i < app.entityData.entityMap.size; i++)
            {
                entity = app.entityData.entityMap.get(i);

                if ((entity != null)
                    && entity.isUpdatable
                    && (entity.getSpriteAction() != Actions._DEAD)
                    && (entity.gid != GraphicID.G_PLAYER))
                {
                    entity.preUpdate();
                    entity.update(entity.spriteNumber);
                    entity.postUpdate(entity.spriteNumber);
                }
            }

            //
            // Main Player, updated after all other entities.
            // Updated last to allow for possible reacting to
            // other entities actions.
            if (!Settings.isEnabled(Settings._SCROLL_DEMO))
            {
                if (_playerReady
                    && app.getPlayer().isUpdatable
                    && (app.getPlayer().getSpriteAction() != Actions._DEAD))
                {
                    app.getPlayer().preUpdate();
                    app.getPlayer().update(_playerIndex);
                    app.getPlayer().postUpdate(_playerIndex);
                }
            }

            //
            // Update the enabled entity managers. These updates will check
            // to see if any entities need re-spawning etc.
            if (!app.gameProgress.levelCompleted)
            {
                for (final EntityManagerComponent system : app.entityData.managerList)
                {
                    system.update();
                }
            }
        }
    }

    /**
     * Draw all game entities
     */
    public void drawSprites()
    {
        renderSystem.drawSprites();
    }

    /**
     * Entity Tidy actions.
     * These are actions performed at the end
     * of each update.
     */
    public void tidySprites()
    {
        if (app.entityData.entityMap != null)
        {
            GdxSprite entity;

            for (int i = 0; i < app.entityData.entityMap.size; i++)
            {
                entity = app.entityData.entityMap.get(i);

                if (entity != null)
                {
                    if (entity.getSpriteAction() == Actions._DEAD)
                    {
                        //noinspection StatementWithEmptyBody
                        if (entity.gid == GraphicID.G_PLAYER)
                        {
                            // TODO: 07/07/2019
                        }
                        else
                        {
                            if ((entity.gid != GraphicID.G_NO_ID) && (entity.collisionObject != null))
                            {
                                entity.collisionObject.kill();
                                app.entityData.removeEntity(i);
                                entity.dispose();
                            }
                        }

                        updateIndexes();
                    }
                }
            }

            app.collisionUtils.tidy();
        }
    }

    /**
     * Update the indexes into the entity map
     * for the main entities
     */
    public void updateIndexes()
    {
        GdxSprite entity;

        _playerIndex = 0;

        for (int i = 0; i < app.entityData.entityMap.size; i++)
        {
            entity = app.entityData.entityMap.get(i);

            if (entity != null)
            {
                if (entity.gid == GraphicID.G_PLAYER)
                {
                    _playerIndex = i;
                }
            }
        }
    }

    public void establishLinks()
    {
        for (GdxSprite gdxSprite : app.entityData.entityMap)
        {
            if (gdxSprite.isLinked)
            {
                for (int j=0; j<app.entityData.entityMap.size; j++)
                {
                    GdxSprite sprite = app.entityData.entityMap.get(j);

                    if (sprite.spriteNumber != gdxSprite.spriteNumber)
                    {
                        if (sprite.link == gdxSprite.link)
                        {
                            sprite.link = gdxSprite.spriteNumber;
                            gdxSprite.link = sprite.spriteNumber;
                        }
                    }
                }
            }
        }
    }

    /**
     * Entity update allowed check.
     *
     * @return boolean true/false
     */
    public boolean entityUpdateAllowed()
    {
        return (AppConfig.entitiesExist && !AppConfig.quitToMainMenu);
    }

    /**
     * Free up all resources used
     */
    @Override
    public void dispose()
    {
        app.entityData.dispose();

        renderSystem = null;
    }

    /**
     * Dump Entity debug info to logcat.
     */
    public void debug()
    {
        if (Developer.isDevMode())
        {
            Trace.__FILE_FUNC_WithDivider();
            Trace.dbg("entityMap.size   : " + app.entityData.entityMap.size);
            Trace.dbg("collisionBox.size: " + AABBData.boxes().size);

            Trace.divider();

            StringBuilder sb = new StringBuilder();
            GdxSprite gdxSprite;

            int entityIndex = 0;

            GraphicID previousGID = GraphicID.G_NO_ID;

            for (int i = 0; i < AABBData.boxes().size; i++)
            {
                if (AABBData.boxes().get(i).gid != previousGID)
                {
                    Trace.divider();
                }

                sb.setLength(0);

                sb.append("collisionBox[").append(i).append("].gid: ");
                sb.append(AABBData.boxes().get(i).gid);
                sb.append(":.type: ");
                sb.append(AABBData.boxes().get(i).type);

                if (AABBData.boxes().get(i).type != GraphicID._ENTITY)
                {
                    sb.append("  --  ");
                    sb.append(" NO ENTITY ATTACHED");
                }
                else
                {
                    gdxSprite = app.entityData.entityMap.get(entityIndex);

                    if (gdxSprite != null)
                    {
                        sb.append("  --  ");
                        sb.append("entityMap[").append(entityIndex).append("].gid: ");

                        sb.append(gdxSprite.gid);

                        entityIndex++;
                    }
                }

                Trace.dbg(sb.toString());
            }

            Trace.divider();
        }
    }
}
