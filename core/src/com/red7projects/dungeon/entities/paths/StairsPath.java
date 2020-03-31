/*
 *  Copyright 12/09/2018 Red7Projects.
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

package com.red7projects.dungeon.entities.paths;

import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.maths.SimpleVec2;
import com.red7projects.dungeon.maths.SimpleVec2F;
import com.red7projects.dungeon.physics.Direction;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.physics.Speed;

@SuppressWarnings("unused")
public class StairsPath
{
    private class PathData
    {
        public final SimpleVec2F distance;
        public final Direction   direction;
        public final Speed       speed;

        PathData(SimpleVec2F _distance, Direction _direction, Speed _speed)
        {
            distance = new SimpleVec2F(_distance.x, _distance.y);
            direction = new Direction(_direction);
            speed = new Speed(_speed);
        }
    }

    private final PathData[] pathData =
        {
            new PathData
                (
                    /* Distance  */ new SimpleVec2F(96, 0),
                    /* Direction */ new Direction(Movement._DIRECTION_CUSTOM, Movement._DIRECTION_STILL),
                    /* Speed     */ new Speed(2, 0)
                ),
            new PathData
                (
                    /* Distance  */ new SimpleVec2F(0, 96),
                    /* Direction */ new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_UP),
                    /* Speed     */ new Speed(0, 2)
                ),
            new PathData
                (
                    /* Distance  */ new SimpleVec2F(96, 0),
                    /* Direction */ new Direction(Movement._DIRECTION_CUSTOM, Movement._DIRECTION_STILL),
                    /* Speed     */ new Speed(2, 0)
                ),
            new PathData
                (
                    /* Distance  */ new SimpleVec2F(0, 96),
                    /* Direction */ new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_UP),
                    /* Speed     */ new Speed(0, 2)
                ),
            new PathData
                (
                    /* Distance  */ new SimpleVec2F(96, 0),
                    /* Direction */ new Direction(Movement._DIRECTION_CUSTOM, Movement._DIRECTION_STILL),
                    /* Speed     */ new Speed(2, 0)
                ),
            new PathData
                (
                    /* Distance  */ new SimpleVec2F(0, 96),
                    /* Direction */ new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_UP),
                    /* Speed     */ new Speed(0, 2)
                ),
            new PathData
                (
                    /* Distance  */ new SimpleVec2F(96, 0),
                    /* Direction */ new Direction(Movement._DIRECTION_CUSTOM, Movement._DIRECTION_STILL),
                    /* Speed     */ new Speed(2, 0)
                ),
            new PathData
                (
                    /* Distance  */ new SimpleVec2F(0, 96),
                    /* Direction */ new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_UP),
                    /* Speed     */ new Speed(0, 2)
                ),
            new PathData
                (
                    /* Distance  */ new SimpleVec2F(96, 0),
                    /* Direction */ new Direction(Movement._DIRECTION_CUSTOM, Movement._DIRECTION_STILL),
                    /* Speed     */ new Speed(2, 0)
                ),
            // ---------------------------------------------------------------
            new PathData
                (
                    /* Distance  */ new SimpleVec2F(0, 96),
                    /* Direction */ new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN),
                    /* Speed     */ new Speed(0, 2)
                ),
            new PathData
                (
                    /* Distance  */ new SimpleVec2F(96, 0),
                    /* Direction */ new Direction(Movement._DIRECTION_CUSTOM, Movement._DIRECTION_STILL),
                    /* Speed     */ new Speed(2, 0)
                ),
            new PathData
                (
                    /* Distance  */ new SimpleVec2F(0, 96),
                    /* Direction */ new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN),
                    /* Speed     */ new Speed(0, 2)
                ),
            new PathData
                (
                    /* Distance  */ new SimpleVec2F(96, 0),
                    /* Direction */ new Direction(Movement._DIRECTION_CUSTOM, Movement._DIRECTION_STILL),
                    /* Speed     */ new Speed(2, 0)
                ),
            new PathData
                (
                    /* Distance  */ new SimpleVec2F(0, 96),
                    /* Direction */ new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN),
                    /* Speed     */ new Speed(0, 2)
                ),
            new PathData
                (
                    /* Distance  */ new SimpleVec2F(96, 0),
                    /* Direction */ new Direction(Movement._DIRECTION_CUSTOM, Movement._DIRECTION_STILL),
                    /* Speed     */ new Speed(2, 0)
                ),
            new PathData
                (
                    /* Distance  */ new SimpleVec2F(0, 96),
                    /* Direction */ new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN),
                    /* Speed     */ new Speed(0, 2)
                ),
            // ...And Repeat...
        };

    public       int        pathIndex;
    public final SimpleVec2 directionReset;

    public StairsPath()
    {
        pathIndex = 0;
        directionReset = new SimpleVec2(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL);
    }

    public void setNextPathData(GdxSprite spriteObject)
    {
        if (pathIndex >= pathData.length)
        {
            pathIndex = 0;
        }

        spriteObject.distance.set(pathData[pathIndex].distance);
        spriteObject.direction.set(pathData[pathIndex].direction);
        spriteObject.speed.set(pathData[pathIndex].speed);

        if (spriteObject.direction.getX() == Movement._DIRECTION_CUSTOM)
        {
            spriteObject.direction.setX(directionReset.x);
        }

        pathIndex++;
    }
}
