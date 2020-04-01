/*
 *  Copyright 27/04/2018 Red7Projects.
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

package com.red7projects.dungeon.entities.systems;

import com.red7projects.dungeon.config.Preferences;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;

public class RenderSystem
{
    private final App app;

    public RenderSystem(App _app)
    {
        this.app = _app;
    }

    /**
     * Draw all sprites to the scene.
     * Uses a Z-coord system: 0 at front, MAX at rear.
     */
    public void drawSprites()
    {
        if (app.entityManager.entityUpdateAllowed())
        {
            GdxSprite entity;

            for (int z = Gfx._MAXIMUM_Z_DEPTH; z >= 0; z--)
            {
                for (int i = 0; i < app.entityData.entityMap.size; i++)
                {
                    entity = app.entityData.entityMap.get(i);

                    if (entity != null)
                    {
                        if (entity.zPosition == z)
                        {
                            if (isInViewWindow(entity) && entity.isUpdatable && entity.isDrawable)
                            {
                                entity.draw(app.spriteBatch);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks for the supplied sprite being inside
     * the scene window.
     *
     * @param sprObj    The sprite to check.
     *
     * @return          TRUE if inside the window.
     */
    private boolean isInViewWindow(GdxSprite sprObj)
    {
        if (app.preferences.isEnabled(Preferences._CULL_SPRITES))
        {
            return app.mapData.viewportBox.overlaps(sprObj.sprite.getBoundingRectangle());
        }

        return true;
    }
}
