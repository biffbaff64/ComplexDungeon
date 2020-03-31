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

package com.red7projects.dungeon.entities.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.red7projects.dungeon.entities.components.AbilityComponent;
import com.red7projects.dungeon.entities.systems.EnemyAttackSystem;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.maths.SquareF;
import com.red7projects.dungeon.physics.Movement;

@SuppressWarnings("WeakerAccess")
//@formatter:off
public class BaseEnemy extends GdxSprite implements IBaseEnemy
{
    public SquareF           movementBounds;
    public AbilityComponent  abilityComponent;
    public Color             originalColor;
    public EnemyAttackSystem attackSystem;
    public Vector2           destination;
    public TextureRegion[]   altAnimFrames;
    public Animation         altAnim;

    public float   elapsedAltAnimTime;
    public boolean localIsDrawable;
    public boolean isVertical;
    public boolean justTurnedAroundX;
    public boolean justTurnedAroundY;
    public boolean justBegunStanding;

    public BaseEnemy(GraphicID graphicID, App _app)
    {
        super(graphicID, _app);
    }

    @Override
    public void initialise(final EntityDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        setAction(Actions._INIT_SPAWN);

        justBegunStanding = false;
        justTurnedAroundX = false;
        justTurnedAroundY = false;
    }

    @Override
    public void animate()
    {
        if (isAnimating)
        {
            elapsedAnimTime += Gdx.graphics.getDeltaTime();
            sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        if (localIsDrawable)
        {
            super.draw(spriteBatch);
        }

        if ((altAnim != null)
            && ((getSpriteAction() == Actions._SPAWNING)
            || (getSpriteAction() == Actions._TELEPORTING)))
        {
            spriteBatch.draw
                (
                    app.entityUtils.getKeyFrame(altAnim, elapsedAltAnimTime, false),
                    sprite.getX(),
                    sprite.getY()
                );
        }
    }

    @Override
    public void checkMovementBounds()
    {
        if (movementBounds != null)
        {
            if (direction.getX() != Movement._DIRECTION_STILL)
            {
                justTurnedAroundX = false;

                if (direction.getX() == Movement._DIRECTION_LEFT)
                {
                    if ((sprite.getX() < 0)
                        || (sprite.getX() <= movementBounds.left))
                    {
                        justTurnedAroundX = true;
                    }
                }
                else if (direction.getX() == Movement._DIRECTION_RIGHT)
                {
                    if ((sprite.getX() > Gfx.getMapWidth())
                        || (sprite.getX() >= movementBounds.right))
                    {
                        justTurnedAroundX = true;
                    }
                }

                if (justTurnedAroundX)
                {
                    onMovementBoundsTurn(direction.getX());

                    direction.toggleX();
                }
            }

            if (direction.getY() != Movement._DIRECTION_STILL)
            {
                justTurnedAroundY = false;

                if (direction.getY() == Movement._DIRECTION_UP)
                {
                    if ((sprite.getY() >= Gfx.getMapHeight())
                        || (sprite.getY() >= movementBounds.top))
                    {
                        justTurnedAroundY = true;
                    }
                }
                else if (direction.getY() == Movement._DIRECTION_DOWN)
                {
                    if ((sprite.getY() < 0)
                        || (sprite.getY() <= movementBounds.bottom))
                    {
                        justTurnedAroundY = true;
                    }
                }

                if (justTurnedAroundY)
                {
                    onMovementBoundsTurn(direction.getY());

                    direction.toggleY();
                }
            }
        }
    }

    @Override
    public void onMovementBoundsTurn(int edgeSide)
    {
    }

    @Override
    public void setHorizontalMovementBounds()
    {
        if (direction.getX() == Movement._DIRECTION_LEFT)
        {
            movementBounds = new SquareF
                (
                    // left
                    (sprite.getX() - distance.getX()),
                    // top
                    (sprite.getY() + frameHeight),
                    // right
                    sprite.getX(),
                    // bottom
                    sprite.getY()
                );
        }
        else    // Movement._DIRECTION_RIGHT
        {
            movementBounds = new SquareF
                (
                    // left
                    sprite.getX(),
                    // top
                    (sprite.getY() + frameHeight),
                    // right
                    (sprite.getX() + distance.getX()),
                    // bottom
                    sprite.getY()
                );
        }

        isVertical = false;
    }

    @Override
    public void setVerticalMovementBounds()
    {
        if (direction.getY() == Movement._DIRECTION_UP)
        {
            movementBounds = new SquareF
                (
                    // left
                    sprite.getX(),
                    // top
                    (sprite.getY() + distance.getY()),
                    // right
                    (sprite.getX() + frameWidth),
                    // bottom
                    sprite.getY()
                );
        }
        else    // Movement._DIRECTION_DOWN
        {
            movementBounds = new SquareF
                (
                    // left
                    sprite.getX(),
                    // top
                    sprite.getY(),
                    // right
                    (sprite.getX() + frameWidth),
                    // bottom
                    (sprite.getY() - distance.getY())
                );
        }

        isVertical = true;
    }

    protected void initSpawning()
    {
        altAnimFrames = new TextureRegion[20];
        altAnim       = app.entityUtils.createAnimation
            (
                "spawn64x64",
                altAnimFrames,
                20,
                Animation.PlayMode.NORMAL
            );

        altAnim.setFrameDuration(0.3f / 6.0f);

        elapsedAltAnimTime = 0.0f;
        localIsDrawable    = false;
        isAnimating        = true;

        sprite.setAlpha(1.0f);

        setAction(Actions._SPAWNING);
    }

    protected void updateSpawning()
    {
        elapsedAltAnimTime += Gdx.graphics.getDeltaTime();

        if ((altAnim == null) || altAnim.isAnimationFinished(elapsedAltAnimTime))
        {
            setAction(Actions._STANDING);

            altAnim             = null;
            altAnimFrames       = null;
            localIsDrawable     = true;
            justBegunStanding   = true;
        }
        else
        {
            localIsDrawable = (altAnim.getKeyFrameIndex(elapsedAltAnimTime) >= 12);
        }
    }

    public void debugMovementBounds()
    {
        // Top
        app.spriteBatch.draw
            (
                app.assets.getObjectsAtlas().findRegion("solid_green32x32"),
                (int) movementBounds.left,
                (int) movementBounds.top,
                (int) (movementBounds.right - movementBounds.left),
                4
            );

        // Left Side
        app.spriteBatch.draw
            (
                app.assets.getObjectsAtlas().findRegion("solid_yellow32x32"),
                (int) movementBounds.left,
                (int) movementBounds.bottom,
                4,
                (int) (movementBounds.top - movementBounds.bottom)
            );

        // Right Side
        app.spriteBatch.draw
            (
                app.assets.getObjectsAtlas().findRegion("solid_yellow32x32"),
                (int) movementBounds.right,
                (int) movementBounds.bottom,
                4,
                (int) (movementBounds.top - movementBounds.bottom)
            );


        // Bottom
        app.spriteBatch.draw
            (
                app.assets.getObjectsAtlas().findRegion("solid_red32x32"),
                (int) movementBounds.left,
                (int) movementBounds.bottom,
                (int) (movementBounds.right - movementBounds.left),
                4
            );
    }
}
