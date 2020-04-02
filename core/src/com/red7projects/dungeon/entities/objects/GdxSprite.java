/*
 *  Copyright 15/09/2018 Red7Projects.
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

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.Constants;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.logging.StopWatch;
import com.red7projects.dungeon.map.MarkerTile;
import com.red7projects.dungeon.maths.SimpleVec2F;
import com.red7projects.dungeon.maths.SimpleVec3F;
import com.red7projects.dungeon.physics.Direction;
import com.red7projects.dungeon.physics.Speed;
import com.red7projects.dungeon.physics.aabb.AABB;
import com.red7projects.dungeon.physics.aabb.CollisionObject;
import com.red7projects.dungeon.types.XYSetF;

public class GdxSprite implements GameSprite, Linker, Disposable
{
    public Sprite       sprite;                   // The LibGDX Sprite this entity uses.
    public GraphicID    gid;                      // The Graphic ID of this sprite.

    // -----------------------------------------------
    // properties etc
    //
    public SimpleVec2F initXY;                     // The entity's start map coordinates
    public int         spriteNumber;
    public boolean     isMainCharacter;
    public float       frameWidth;
    public float       frameHeight;
    public float       rotateSpeed;
    public float       rotation;
    public int         zPosition;                 // Used for drawing priorities.
    public int         link;

    private Actions spriteAction;

    // -----------------------------------------------
    // Variables that could possibly be moved outside
    // of this class and into any child classes.
    //
    public Direction direction;                  // Direction of movement.
    public Direction lookingAt;                  // Direction the entity is facing.
    public XYSetF    distance;
    public XYSetF    distanceReset;
    public Speed     speed;                      // Speed of movement
    public int       strength;
    public StopWatch stopWatch;
    public StopWatch invisibilityTimer;
    public int       restingTime;

    // -----------------------------------------------
    // Animation related
    public Animation<TextureRegion> animation;
    public float                    elapsedAnimTime;
    public TextureRegion[]          animFrames;

    // -----------------------------------------------
    // Collision related
    //
    public float           rightEdge;          //
    public float           topEdge;            //
    public AABB            aabb;               // The AABB box collision handler;
    public CollisionObject collisionObject;    // The AABB collision rectangle and data.

    // IF Box2D is used...
    public Body    b2dBody;            // The Box2D physics body
    public BodyDef bodyDef;            // The Box2D body definition

    private CollisionListener collisionCallback;

    // -----------------------------------------------
    // public flags
    //
    public boolean isUpdatable;
    public boolean isDrawable;
    public boolean isRotating;
    public boolean isFlippedX;
    public boolean isFlippedY;
    public boolean isShooting;
    public boolean isAnimating;
    public boolean isCarrying;
    public boolean isEnemy;
    public boolean isLinked;
    public boolean canFlip;
    public boolean isSelected;
    public boolean hasSelectionRing;

    // -----------------------------------------------
    // protected variables
    //
    protected       boolean preUpdateCommonDone;
    protected final App     app;

    // TODO: 26/10/2019 - This entire class needs rewriting as I am
    //                  - sure that it can be made much more efficient.

    public GdxSprite(App _app)
    {
        this.app = _app;
    }

    public GdxSprite(GraphicID _gid, App _app)
    {
        this.app            = _app;
        this.gid            = _gid;

        sprite              = new Sprite();
        direction           = new Direction();
        lookingAt           = new Direction();
        speed               = new Speed();
        distance            = new XYSetF();
        distanceReset       = new XYSetF();
        initXY              = new SimpleVec2F();
        aabb                = new AABB();

        spriteNumber        = 0;
        rotation            = 0;

        isRotating          = false;
        isFlippedX          = false;
        isFlippedY          = false;
        isCarrying          = false;
        isSelected          = false;
        hasSelectionRing    = false;
        preUpdateCommonDone = false;
    }

    @Override
    public void create(EntityDescriptor entityDescriptor)
    {
        if (entityDescriptor._ASSET != null)
        {
            setAnimation(entityDescriptor, entityDescriptor._ANIM_RATE);
        }

        spriteNumber    = entityDescriptor._INDEX;
        isAnimating     = (entityDescriptor._FRAMES > 1);
        isDrawable      = true;
        canFlip         = true;
        strength        = Constants._MAX_STRENGTH;
        stopWatch       = StopWatch.start();

        initPosition(new SimpleVec3F
            (
                entityDescriptor._X,
                entityDescriptor._Y,
                entityDescriptor._Z
            ));

        createCollisionObject();

        isMainCharacter = entityDescriptor._MAIN_CHARACTER;
        isEnemy         = entityDescriptor._ENEMY;
        isUpdatable     = entityDescriptor._UPDATEABLE;
        isLinked        = (entityDescriptor._LINK > 0);
        link            = entityDescriptor._LINK;
    }

    /**
     * Initialise this entity
     *
     * @param entityDescriptor Entity Description
     * @see EntityDescriptor
     */
    @Override
    public void initialise(final EntityDescriptor entityDescriptor)
    {
        create(entityDescriptor);
    }

    @Override
    public void preUpdate()
    {
        if (spriteAction == Actions._RESTARTING)
        {
            sprite.setPosition(initXY.x, initXY.y);
        }

        if (!AppConfig.isUsingBOX2DPhysics)
        {
            updateCollisionCheck();
        }

        preUpdateCommonDone = true;
    }

    /**
     * Performs update tasks for
     * this entity.
     *
     * @param spriteNum index into the entity map
     */
    @Override
    public void update(final int spriteNum)
    {
    }

    /**
     * Common updates needed for all entities
     */
    @Override
    public void updateCommon()
    {
        if (isRotating)
        {
            sprite.rotate(rotateSpeed);
        }

        if (canFlip)
        {
            sprite.setFlip(isFlippedX, isFlippedY);
        }
    }

    @Override
    public void postUpdate(final int spriteNum)
    {
    }

    @Override
    public void postMove()
    {
    }

    /**
     * Update the current animation
     */
    @Override
    public void animate()
    {
    }

    @Override
    public void draw(final SpriteBatch spriteBatch)
    {
        if (isDrawable)
        {
            sprite.draw(spriteBatch);
        }
    }

    @Override
    public void createCollisionObject()
    {
        collisionObject = app.collisionUtils.newObject
            (
                (int) sprite.getX(),
                (int) sprite.getY(),
                (int) frameWidth,
                (int) frameHeight,
                GraphicID._ENTITY
            );

        collisionObject.gid          = this.gid;
        collisionObject.parentSprite = this;

        collisionObject.addObjectToList();
    }

    /**
     * make sure the collision rectangle
     * is where this sprite is.
     * Can also be used to make the collision rectangle
     * a different shape to the sprite bounding box...
     */
    @Override
    public void updateCollisionBox()
    {
        collisionObject.rectangle.x      = sprite.getX();
        collisionObject.rectangle.y      = sprite.getY();
        collisionObject.rectangle.width  = frameWidth;
        collisionObject.rectangle.height = frameHeight;

        rightEdge = sprite.getX() + frameWidth;
        topEdge   = sprite.getY() + frameHeight;
    }

    @Override
    public Rectangle getCollisionRectangle()
    {
        return collisionObject.rectangle;
    }

    /**
     * Check for any collisions.
     */
    @Override
    public void updateCollisionCheck()
    {
        if (collisionObject != null)
        {
            updateCollisionBox();

            collisionObject.clearCollision();

            if (collisionObject.action == Actions._COLLIDABLE)
            {
                if (aabb.checkAABBBoxes(collisionObject))
                {
                    if (collisionCallback != null)
                    {
                        collisionCallback.onPositiveCollision(collisionObject.contactGid);
                    }
                }

                if (collisionObject.action != Actions._COLLIDING)
                {
                    if (collisionCallback != null)
                    {
                        collisionCallback.onNegativeCollision();
                    }
                }
            }
        }
    }

    /**
     * Add a {@link CollisionListener} to this entity.
     *
     * @param listener The listener.
     */
    @Override
    public void addCollisionListener(final CollisionListener listener)
    {
        this.collisionCallback = listener;
    }

    /**
     * Set the initial (start) position for this entity.
     *
     * @param vec3F A {@link SimpleVec3F} holding x, y, z positions.
     *              The positions must be positions in the TiledMap,
     *              usually the location of the {@link MarkerTile}
     */
    @Override
    public void initPosition(final SimpleVec3F vec3F)
    {
        sprite.setSize(frameWidth, frameHeight);

        sprite.setPosition((vec3F.x * Gfx.getTileWidth()), (vec3F.y * Gfx.getTileHeight()));
        initXY.set(sprite.getX(), sprite.getY());

        sprite.setBounds(sprite.getX(), sprite.getY(), frameWidth, frameHeight);
        sprite.setOriginCenter();

        zPosition = (int) vec3F.z;
    }

    /**
     * Translates the physics body coordinates onto the sprite position
     */
    public void setPositionfromBody()
    {
        if (b2dBody != null)
        {
            sprite.setPosition
                (
                    (b2dBody.getPosition().x * Gfx._PPM) - (frameWidth / 2),
                    (b2dBody.getPosition().y * Gfx._PPM) - (frameHeight / 2)
                );
        }
    }

    public Vector3 getPosition()
    {
        return new Vector3(sprite.getX(), sprite.getY(), 0);
    }

    @Override
    public void setAction(final Actions action)
    {
        spriteAction = action;
    }

    @Override
    public Actions getSpriteAction()
    {
        return spriteAction;
    }

    @Override
    public void setAnimation(final EntityDescriptor descriptor, final float frameRate)
    {
        animFrames = new TextureRegion[descriptor._FRAMES];

        if (descriptor._SIZE != null)
        {
            frameWidth  = descriptor._SIZE.x;
            frameHeight = descriptor._SIZE.y;
        }
        else
        {
            frameWidth  = (float) (descriptor._ASSET.getRegionWidth() / descriptor._FRAMES);
            frameHeight = descriptor._ASSET.getRegionHeight();
        }

        sprite.setSize(frameWidth, frameHeight);
        sprite.setBounds(sprite.getX(), sprite.getY(), frameWidth, frameHeight);
        sprite.setOriginCenter();

        TextureRegion[][] tmpFrames = descriptor._ASSET.split((int) frameWidth, (int) frameHeight);

        int i = 0;

        for (final TextureRegion[] tmpFrame : tmpFrames)
        {
            for (final TextureRegion textureRegion : tmpFrame)
            {
                if (i < descriptor._FRAMES)
                {
                    animFrames[i++] = textureRegion;
                }
            }
        }

        animation = new Animation<>(frameRate / 6f, animFrames);
        animation.setPlayMode(descriptor._PLAYMODE);

        sprite.setRegion(animFrames[0]);
    }

    @Override
    public void setLink(int _link)
    {
        this.link = _link;
    }

    @Override
    public int getLink()
    {
        return link;
    }

    @Override
    public void action()
    {
    }

    /**
     * Clear all resources
     */
    @Override
    public void dispose()
    {
        // TODO: 26/10/2019 - Check everything is correct here
        if (collisionObject != null)
        {
            collisionObject.dispose();
        }

        sprite          = null;
        gid             = null;
        direction       = null;
        lookingAt       = null;
        distance        = null;
        distanceReset   = null;
        initXY          = null;
        speed           = null;
        spriteAction    = null;
        stopWatch       = null;
        animation       = null;
        animFrames      = null;
        b2dBody         = null;
        bodyDef         = null;
        collisionObject = null;
    }
}
