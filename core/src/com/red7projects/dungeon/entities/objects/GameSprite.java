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

package com.red7projects.dungeon.entities.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.maths.SimpleVec3F;

public interface GameSprite
{
    void initialise(EntityDescriptor entityDescriptor);

    void create(EntityDescriptor entityDescriptor);

    void initPosition(SimpleVec3F vec3F);

    void preUpdate();

    void update(int spriteNum);

    void updateCommon();

    void postUpdate(int spriteNum);

    void postMove();

    void draw(SpriteBatch spriteBatch);

    void animate();

    Rectangle getCollisionRectangle();

    void setAnimation(EntityDescriptor entityDescriptor, float frameRate);

    void createCollisionObject();

    void addCollisionListener(CollisionListener listener);

    void updateCollisionCheck();

    void updateCollisionBox();

    void setAction(Actions action);

    Actions getSpriteAction();
}
