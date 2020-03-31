/*
 *  Copyright 18/05/2018 Red7Projects.
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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.entities.hero.PlayerBullet;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.pooling.ObjectPool;

public class PlayerBulletManager implements Disposable
{
    private ObjectPool<PlayerBullet> bulletPool;

    public PlayerBulletManager(App app)
    {
        ObjectPool.ObjectPoolFactory<PlayerBullet> laserFactory = new ObjectPool.ObjectPoolFactory<PlayerBullet>()
        {
            @Override
            public PlayerBullet createObject()
            {
                return new PlayerBullet(app);
            }

            @Override
            public PlayerBullet createObject(Rectangle rectangle)
            {
                return new PlayerBullet(app);
            }

            @Override
            public PlayerBullet createObject(int x, int y, int width, int height, GraphicID type)
            {
                return new PlayerBullet(app);
            }
        };

        bulletPool = new ObjectPool<>(laserFactory, 50);
    }

    public void createBullet(GdxSprite parent, App app)
    {
        EntityDescriptor entityDescriptor = new EntityDescriptor
            (
                0,
                0,
                app.entityUtils.getInitialZPosition(GraphicID.G_ARROW),
                app.assets.getAnimationsAtlas().findRegion(GameAssets._ARROW_ASSET),
                GameAssets._ARROW_FRAMES,
                Animation.PlayMode.LOOP
            );

        entityDescriptor._INDEX         = app.entityData.entityMap.size;
        entityDescriptor._ENEMY         = app.entityUtils.setEnemyStatus(GraphicID.G_ARROW);
        entityDescriptor._UPDATEABLE    = app.entityUtils.canUpdate(GraphicID.G_ARROW);
        entityDescriptor._PARENT        = parent;

        PlayerBullet bullet = bulletPool.newObject();
        bullet.initialise(entityDescriptor);

        app.entityData.addEntity(bullet);
    }

    @Override
    public void dispose()
    {
        bulletPool = null;
    }
}
