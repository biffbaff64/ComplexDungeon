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
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.physics.aabb.CollisionObject;
import com.red7projects.dungeon.utils.logging.Trace;

public class Door extends GdxSprite
{
    public boolean isLocked;

    private CollisionObject leftBox;
    private CollisionObject rightBox;
    private TextureRegion lockedDoorImage;
    private App app;

    public Door(final GraphicID _gid, final App _app)
    {
        super(_gid, _app);

        this.app = _app;
    }

    @Override
    public void initialise(final EntityDescriptor entityDescriptor)
    {
        Trace.__FILE_FUNC();

        create(entityDescriptor);

        setAction(Actions._CLOSED);

        sprite.setRegion(animFrames[0]);

        animation.setFrameDuration(0.3f/ 6f);

        isAnimating = false;

        lockDoor(false);

//        collisionObject.type = GraphicID._INTERACTIVE;
        collisionObject.bodyCategory = Gfx.CAT_DOOR;
        collisionObject.collidesWith = Gfx.CAT_WEAPON | Gfx.CAT_PLAYER | Gfx.CAT_MOBILE_ENEMY;

        leftBox = app.collisionUtils.newObject
            (
                (int) sprite.getX(),
                (int) sprite.getY() + Gfx.getTileHeight(),
                (int) frameWidth / 8,
                (int) frameHeight - Gfx.getTileHeight(),
                GraphicID._OBSTACLE
            );

        leftBox.gid = GraphicID._WALL;
        leftBox.bodyCategory = Gfx.CAT_WALL;
        leftBox.collidesWith = collisionObject.collidesWith;
        leftBox.addObjectToList();

        rightBox = app.collisionUtils.newObject
            (
                (int) sprite.getX() + ((int) (frameWidth - (frameWidth / 8))),
                (int) sprite.getY() + Gfx.getTileHeight(),
                (int) frameWidth / 8,
                (int) frameHeight - Gfx.getTileHeight(),
                GraphicID._OBSTACLE
            );

        rightBox.gid = GraphicID._WALL;
        rightBox.bodyCategory = Gfx.CAT_WALL;
        rightBox.collidesWith = collisionObject.collidesWith;
        rightBox.addObjectToList();
    }

    @Override
    public void update(final int spriteNum)
    {
        switch (getSpriteAction())
        {
            case _OPEN:
            {
                if (!collisionObject.rectangle.isTouchingAnEntity(collisionObject.index))
                {
                    setAction(Actions._CLOSING);

                    isAnimating = true;
                    elapsedAnimTime = 0;
                    animation.setPlayMode(Animation.PlayMode.REVERSED);
                }
            }
            break;

            case _CLOSED:
            {
                if (collisionObject.rectangle.isTouchingAnEntity(collisionObject.index))
                {
                    setAction(Actions._OPENING);

                    gid = GraphicID.G_DOOR;
                    isAnimating = true;
                    elapsedAnimTime = 0;
                    animation.setPlayMode(Animation.PlayMode.NORMAL);
                }
            }
            break;

            case _OPENING:
            {
                if (animation.isAnimationFinished(elapsedAnimTime))
                {
                    collisionObject.collidesWith &= ~Gfx.CAT_MOBILE_ENEMY;
                    isAnimating = false;
                    setAction(Actions._OPEN);
                }
            }
            break;

            case _CLOSING:
            {
                if (animation.isAnimationFinished(elapsedAnimTime))
                {
                    collisionObject.collidesWith |= Gfx.CAT_MOBILE_ENEMY;
                    isAnimating = false;
                    setAction(Actions._CLOSED);
                }
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unsupported spriteAction: " + getSpriteAction());
            }
            break;
        }

        animate();

        updateCommon();
    }

    @Override
    public void updateCollisionBox()
    {
        collisionObject.rectangle.x         = sprite.getX() + 3;
        collisionObject.rectangle.y         = (sprite.getY() - (float) (Gfx.getTileHeight() / 2));
        collisionObject.rectangle.width     = frameWidth - 6;
        collisionObject.rectangle.height    = frameHeight + Gfx.getTileHeight();

        rightEdge = collisionObject.rectangle.x + collisionObject.rectangle.width;
        topEdge = collisionObject.rectangle.y + collisionObject.rectangle.height;
    }

    @Override
    public void animate()
    {
        switch (getSpriteAction())
        {
            case _OPEN:
            {
                sprite.setRegion(animFrames[GameAssets._DOOR_FRAMES - 1]);
            }
            break;

            case _CLOSED:
            {
                if (isLocked)
                {
                    sprite.setRegion(lockedDoorImage);
                }
                else
                {
                    sprite.setRegion(animFrames[0]);
                }
            }
            break;

            case _OPENING:
            case _CLOSING:
            {
                if (isAnimating)
                {
                    sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, false));
                    elapsedAnimTime += Gdx.graphics.getDeltaTime();
                }
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unsupported spriteAction: " + getSpriteAction());
            }
            break;
        }
    }

    public void lockDoor(boolean setLocked)
    {
        if (lockedDoorImage == null)
        {
            lockedDoorImage = app.assets.getAnimationsAtlas().findRegion(GameAssets._LOCKED_DOOR_ASSET);
        }

        this.isLocked   = setLocked;
        this.gid        = isLocked ? GraphicID.G_LOCKED_DOOR : GraphicID.G_DOOR;
    }
}
