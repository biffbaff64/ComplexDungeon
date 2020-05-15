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

package com.red7projects.dungeon.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.red7projects.dungeon.entities.objects.BaseEnemy;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.entities.objects.GenericCollisionListener;
import com.red7projects.dungeon.entities.systems.EnemyAttackSystem;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;

import java.util.concurrent.TimeUnit;

public class Turret extends BaseEnemy
{
    private final App app;

    public Turret(final GraphicID _gid, final App _app)
    {
        super(_gid, _app);

        this.app = _app;
    }

    @Override
    public void initialise(final EntityDescriptor entityDescriptor)
    {
        super.initialise(entityDescriptor);

        collisionObject.bodyCategory = Gfx.CAT_FIXED_ENEMY;
        collisionObject.collidesWith = Gfx.CAT_NOTHING;

        isRotating = true;
        rotateSpeed = -1.0f;
        isDrawable = true;
        localIsDrawable = true;

        attackSystem = new EnemyAttackSystem(this, app);

        setAction(Actions._STANDING);
    }

    @Override
    public void update(final int spriteNum)
    {

        if ((collisionObject.action != Actions._COLLIDING)
            && (stopWatch.time(TimeUnit.MILLISECONDS) > restingTime)
            && (MathUtils.random(100) < 5))
        {
            attackSystem.shoot();

            isShooting = true;
            stopWatch.reset();
        }

        animate();

        updateCommon();
    }

    @Override
    public void animate()
    {
        elapsedAnimTime += Gdx.graphics.getDeltaTime();
        sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, false));
    }
}
