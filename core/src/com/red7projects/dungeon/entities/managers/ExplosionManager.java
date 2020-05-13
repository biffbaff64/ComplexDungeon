/*
 *  Copyright 19/06/2018 Red7Projects.
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
import com.red7projects.dungeon.entities.characters.Explosion;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;

public class ExplosionManager
{
    class ExplosionInfo
    {
        final GraphicID graphicID;
        final float     scale;

        ExplosionInfo(GraphicID _gid, float _scale)
        {
            this.graphicID = _gid;
            this.scale = _scale;
        }
    }

    private final ExplosionInfo[] explosionTypes =
        {
            new ExplosionInfo(GraphicID.G_EXPLOSION12,  0.18f),
            new ExplosionInfo(GraphicID.G_EXPLOSION32,  0.50f),
            new ExplosionInfo(GraphicID.G_EXPLOSION64,  1.00f),
            new ExplosionInfo(GraphicID.G_EXPLOSION128, 2.00f),
            new ExplosionInfo(GraphicID.G_EXPLOSION256, 4.00f),
        };

    public void createExplosion(GraphicID _gid, GdxSprite _parent, App _app)
    {
        int index = 0;

        for (int i=0; i<explosionTypes.length; i++)
        {
            if (explosionTypes[i].graphicID == _gid)
            {
                index = i;
            }
        }

        EntityDescriptor entityDescriptor = new EntityDescriptor
            (
                (int) _parent.sprite.getX() / Gfx.getTileWidth(),
                (int) _parent.sprite.getY() / Gfx.getTileHeight(),
                _app.entityUtils.getInitialZPosition(_gid),
                _app.assets.getAnimationsAtlas().findRegion(GameAssets._EXPLOSION64_ASSET),
                GameAssets._EXPLOSION64_FRAMES,
                Animation.PlayMode.LOOP
            );

        entityDescriptor._INDEX         = _app.entityData.entityMap.size;
        entityDescriptor._PARENT        = _parent;
        entityDescriptor._SIZE          = GameAssets.getAssetSize(_gid);

        Explosion explosion = new Explosion(_gid, _app);
        explosion.initialise(entityDescriptor);
        explosion.sprite.setScale(explosionTypes[index].scale);

        _app.entityData.addEntity(explosion);
    }
}
