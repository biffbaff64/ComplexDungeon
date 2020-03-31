/*
 *  Copyright 16/12/2018 Red7Projects.
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

package com.red7projects.dungeon.entities.objects;

import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.GraphicID;

public class GenericCollisionListener implements CollisionListener
{
    private       GdxSprite parent;
    private final App       app;

    public  GenericCollisionListener(GdxSprite _parent, App _app)
    {
        this.parent = _parent;
        this.app = _app;
    }

    /**
     * onPositiveCollision() and onNegativeCollision are
     * called BEFORE the main update method.
     * Collision related responses can be set here and handled
     * in the update() method.
     */
    @Override
    public void onPositiveCollision(final GraphicID spriteHittingGid)
    {
        if (spriteHittingGid == GraphicID.G_LASER)
        {
            parent.setAction(Actions._KILLED);
        }
        else
        {
            if (spriteHittingGid == GraphicID.G_PLAYER)
            {
                parent.setAction(Actions._HURT);
            }
        }
    }

    @Override
    public void onNegativeCollision()
    {
    }

    @Override
    public void dispose()
    {
        parent = null;
    }
}
