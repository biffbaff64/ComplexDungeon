/*
 *  Copyright 03/05/2018 Red7Projects.
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

package com.red7projects.dungeon.physics.aabb;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.game.Actions;

public class AABB implements Disposable
{
    private Rectangle   topRectangle;
    private Rectangle   midRectangle;
    private Rectangle   botRectangle;

    public AABB()
    {
        super();

        this.topRectangle = new Rectangle();
        this.midRectangle = new Rectangle();
        this.botRectangle = new Rectangle();
    }

    public boolean checkAABBBoxes(CollisionObject boxA)
    {
        boolean isHitting;
        boolean collisionDetected = false;

        if (AABBData.boxes().size > 0)
        {
            if (boxA.index > 0)
            {
                float boxHeight = boxA.rectangle.height / 3;

                topRectangle.set
                    (
                        (boxA.rectangle.x + (boxA.rectangle.width / 4)),
                        (boxA.rectangle.y + (boxHeight * 2)),
                        (boxA.rectangle.width / 2),
                        boxHeight
                    );

                midRectangle.set
                    (
                        boxA.rectangle.x,
                        (boxA.rectangle.y + boxHeight),
                        boxA.rectangle.width,
                        boxHeight
                    );

                botRectangle.set
                    (
                        (boxA.rectangle.x + (boxA.rectangle.width / 4)),
                        boxA.rectangle.y,
                        (boxA.rectangle.width / 2),
                        boxHeight
                    );

                isHitting = false;

                for (CollisionObject boxB : AABBData.boxes())
                {
                    if (boxB.index > 0)
                    {
                        if (((boxA.collidesWith & boxB.bodyCategory) != 0)
                            && ((boxB.collidesWith & boxA.bodyCategory) != 0)
                            && ((boxA.gid != boxB.gid) || (boxA.index != boxB.index))
                            && ((boxA.type != boxB.type) || (boxA.index != boxB.index)))
                        {
                            if (Intersector.overlaps(boxA.rectangle, boxB.rectangle))
                            {
                                if (Intersector.overlaps(topRectangle, boxB.rectangle))
                                {
                                    isHitting           = true;
                                    boxA.idTop          = boxB.gid;
                                    boxA.boxHittingTop  = boxB.index;
                                    boxA.contactMask    |= AABBData._TOP;
                                }

                                if (Intersector.overlaps(midRectangle, boxB.rectangle))
                                {
                                    if ((midRectangle.x >= boxB.rectangle.x)
                                        && (midRectangle.x <= (boxB.rectangle.x + boxB.rectangle.width))
                                        && ((midRectangle.x + midRectangle.width) > (boxB.rectangle.x + boxB.rectangle.width)))
                                    {
                                        isHitting           = true;
                                        boxA.idLeft         = boxB.gid;
                                        boxA.boxHittingLeft = boxB.index;
                                        boxA.contactMask    |= AABBData._LEFT;
                                    }

                                    if ((midRectangle.x < boxB.rectangle.x)
                                        && ((midRectangle.x + midRectangle.width) >= boxB.rectangle.x)
                                        && ((midRectangle.x + midRectangle.width) <= (boxB.rectangle.x + boxB.rectangle.width)))
                                    {
                                        isHitting               = true;
                                        boxA.idRight            = boxB.gid;
                                        boxA.boxHittingRight    = boxB.index;
                                        boxA.contactMask        |= AABBData._RIGHT;
                                    }
                                }

                                if (Intersector.overlaps(botRectangle, boxB.rectangle)
                                    && (boxB.rectangle.y <= botRectangle.y))
                                {
                                    isHitting               = true;
                                    boxA.idBottom           = boxB.gid;
                                    boxA.boxHittingBottom   = boxB.index;
                                    boxA.contactMask        |= AABBData._BOTTOM;
                                }
                            }
                        }

                        if (isHitting)
                        {
                            collisionDetected = true;
                            isHitting = false;

                            //
                            // Collision objects for non-sprites
                            // don't have parent sprites
                            if (boxB.parentSprite != null)
                            {
                                boxA.contactSprite = boxB.parentSprite;
                            }

                            boxA.contactGid         = boxB.gid;
                            boxA.isContactObstacle  = boxB.isObstacle;
                            boxA.action             = Actions._COLLIDING;
                            boxB.action             = Actions._COLLIDING;
                        }
                    }
                }
            }
        }

        return collisionDetected;
    }

    @Override
    public void dispose()
    {
        topRectangle    = null;
        midRectangle    = null;
        botRectangle    = null;
    }
}
