/*
 *  Copyright 25/04/2018 Red7Projects.
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

package com.red7projects.dungeon.entities.hero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.config.Settings;
import com.red7projects.dungeon.entities.managers.PlayerBulletManager;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.Constants;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.map.Room;
import com.red7projects.dungeon.map.TileID;
import com.red7projects.dungeon.maths.Box;
import com.red7projects.dungeon.maths.Point;
import com.red7projects.dungeon.physics.DirectionAnim;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.physics.Speed;
import com.red7projects.dungeon.physics.aabb.CollisionRect;
import com.red7projects.dungeon.utils.development.Developer;
import com.red7projects.dungeon.utils.logging.Meters;
import com.red7projects.dungeon.utils.logging.Stats;
import com.red7projects.dungeon.utils.logging.Trace;

import java.util.concurrent.TimeUnit;

@SuppressWarnings({"WeakerAccess"})
public class MainPlayer extends GdxSprite
{
    private static final float _VIEWBOX_WIDTH  = (Gfx._VIEW_WIDTH + Gfx._VIEW_HALF_WIDTH);
    private static final float _VIEWBOX_HEIGHT = (Gfx._VIEW_HEIGHT + Gfx._VIEW_HALF_HEIGHT);
    private static final float _PLAYER_X_SPEED = 6;
    private static final float _PLAYER_Y_SPEED = 6;

    public TextureRegion[]     altAnimFrames;
    public Animation           altAnim;
    public Actions             previousAction;
    public ButtonInputHandler  buttons;
    public CollisionHandler    collision;
    public ActionButtonHandler actionButton;
    public PlayerBulletManager bulletManager;
    public Speed               maxMoveSpeed;
    public Box                 viewBox;
    public GdxSprite           platformSprite;

    public float   elapsedAltAnimTime;
    public boolean localIsDrawable;
    public boolean isHurting;
    public boolean isMovingX;
    public boolean isMovingY;
    public boolean isGrabbing;
    public boolean isCasting;
    public boolean isOnPlatform;
    public boolean isOnFloorButton;
    public boolean isPossessed;
    public boolean canOfferButton;
    public boolean canOpenMessagePanel;

    private TextureRegion[] abxy;
    private CollisionRect   tileRectangle;
    private App             app;

    public MainPlayer(App _app)
    {
        super(GraphicID.G_PLAYER, _app);

        this.app = _app;
    }

    @Override
    public void initialise(EntityDescriptor descriptor)
    {
        Trace.__FILE_FUNC();

        create(descriptor);

        collisionObject.bodyCategory = Gfx.CAT_PLAYER;
        collisionObject.collidesWith = Gfx.CAT_MOBILE_ENEMY
            | Gfx.CAT_FIXED_ENEMY
            | Gfx.CAT_ENEMY_WEAPON
            | Gfx.CAT_COLLECTIBLE
            | Gfx.CAT_OBSTACLE
            | Gfx.CAT_EXIT_BOX
            | Gfx.CAT_INTERACTIVE
            | Gfx.CAT_PLATFORM
            | Gfx.CAT_VILLAGER
            | Gfx.CAT_DOOR;

        isHurting           = false;
        isShooting          = false;
        isGrabbing          = false;
        isCasting           = false;
        isOnPlatform        = false;
        isOnFloorButton     = false;
        isPossessed         = false;
        canOfferButton      = false;
        canOpenMessagePanel = false;

        buttons       = new ButtonInputHandler(app);
        collision     = new CollisionHandler(app);
        actionButton  = new ActionButtonHandler(app);
        bulletManager = new PlayerBulletManager(app);
        tileRectangle = new CollisionRect(gid);
        viewBox       = new Box();
        maxMoveSpeed  = new Speed();

        final TextureRegion abxyTexture = app.assets.getAnimationsAtlas().findRegion(GameAssets._ABXY_ASSET);
        abxy = new TextureRegion[4];

        TextureRegion[] tmpFrames = abxyTexture.split
            (
                (abxyTexture.getRegionWidth() / 4),
                abxyTexture.getRegionHeight()
            )[0];

        System.arraycopy(tmpFrames, 0, abxy, 0, 4);

        setup();
    }

    /**
     * Set up the player, ready for play. This method is separate from initialise()
     * because it does not create objects, and is called on initialisation and also
     * when restarting after losing a life.
     */
    public void setup()
    {
        Trace.__FILE_FUNC();

        direction.set(Movement._DIRECTION_STILL, Movement._DIRECTION_STILL);
        lookingAt.set(direction);

        maxMoveSpeed.set(_PLAYER_X_SPEED, _PLAYER_Y_SPEED);
        strength = Constants._MAX_STRENGTH;

        isMovingX           = false;
        isMovingY           = false;
        isRotating          = false;
        isFlippedX          = false;
        isFlippedY          = false;
        canFlip             = false;
        isHurting           = false;
        isShooting          = false;
        isCasting           = false;
        isGrabbing          = false;
        localIsDrawable     = false;
        isDrawable          = true;
        canOpenMessagePanel = true;

        sprite.setRotation(0);
        sprite.setPosition(initXY.getX(), initXY.getY());
        collisionObject.clearCollision();

        setAction(Actions._SPAWNING);
    }

    @Override
    public void preUpdate()
    {
        if (getSpriteAction() == Actions._RESTARTING)
        {
            sprite.setPosition(app.mapData.checkPoint.getX(), app.mapData.checkPoint.getY());

            setAction(Actions._SPAWNING);
        }
        else
        {
            TileID tileID = app.collisionUtils.getTileAtPosition
                (collision.getBottomPoint(Point._CENTRE).getX() / Gfx.getTileWidth(),
                    collision.getBottomPoint(Point._CENTRE).getY() / Gfx.getTileHeight());

            if (tileID == TileID._ESCALATOR_LEFT_TILE)
            {
                sprite.translateX(_PLAYER_X_SPEED * Movement._DIRECTION_LEFT);
            }

            if (tileID == TileID._ESCALATOR_RIGHT_TILE)
            {
                sprite.translateX(_PLAYER_X_SPEED * Movement._DIRECTION_RIGHT);
            }

            if (tileID == TileID._ESCALATOR_UP_TILE)
            {
                sprite.translateY(_PLAYER_Y_SPEED * Movement._DIRECTION_UP);
            }

            if (tileID == TileID._ESCALATOR_DOWN_TILE)
            {
                sprite.translateY(_PLAYER_Y_SPEED * Movement._DIRECTION_DOWN);
            }
        }

        super.preUpdate();
    }

    @Override
    public void update(int spriteNum)
    {
        if (app.appState.peek() == StateID._STATE_PAUSED)
        {
            setAction(Actions._PAUSED);
        }

        actionButton.update();

        updateMainPlayer();

        animate();

        updateCommon();
    }

    private void updateMainPlayer()
    {
        switch (getSpriteAction())
        {
            case _DYING:
            case _EXPLODING:
            case _RESETTING:
            case _RESTARTING:
            case _WAITING:
            case _DEAD:
            case _PAUSED:
            case _KILLED:
            case _CHANGING_ROOM:
            {
            }
            break;

            case _SPAWNING:
            {
                elapsedAltAnimTime += Gdx.graphics.getDeltaTime();

                if (altAnim.isAnimationFinished(elapsedAltAnimTime))
                {
                    setAction(Actions._STANDING);

                    altAnim       = null;
                    altAnimFrames = null;
                }
                else
                {
                    localIsDrawable = (altAnim.getKeyFrameIndex(elapsedAltAnimTime) >= GameAssets._PLAYER_APPEAR_FRAME);
                }
            }
            break;

            case _STANDING:
            {
                if (direction.hasDirection())
                {
                    lookingAt.set(direction);
                }

                buttons.checkButtons();
            }
            break;

            case _HURT:
            {
                isHurting = false;
                setAction(Actions._STANDING);
            }
            break;

            case _RIDING:
            case _RUNNING:
            {
                lookingAt.set(direction);

                buttons.checkButtons();

                movePlayer();
            }
            break;

            case _FIGHTING:
            {
                if (animation.isAnimationFinished(elapsedAnimTime))
                {
                    setAction(Actions._STANDING);
                }
            }
            break;

            default:
            {
                Trace.__FILE_FUNC_WithDivider();
                Trace.dbg("Unsupported player action: " + getSpriteAction());

                Stats.incMeter(Meters._BAD_PLAYER_ACTION.get());
            }
            break;
        }
    }

    @Override
    public void postUpdate(int spriteNum)
    {
        super.postUpdate(spriteNum);

        isOnFloorButton = (collision.isNextTo(GraphicID.G_FLOOR_BUTTON) > 0);

//        if (collision.isNextTo(GraphicID.G_VILLAGER) > 0)
//        {
//            if (getSpriteAction() == Actions._STANDING)
//            {
//                if (canOpenMessagePanel)
//                {
//                    buttons.xButtonActions.process();
//                    canOpenMessagePanel = false;
//                }
//            }
//        }

        // TEMP
        if (strength <= 0)
        {
            strength = Constants._MAX_STRENGTH;
        }
    }

    @Override
    public void animate()
    {
        final DirectionAnim[] runningAnims =
            {
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_UP,     GameAssets._RUN_UP_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_DOWN,   GameAssets._RUN_DOWN_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL,  GameAssets._RUN_LEFT_ASSET),

                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_UP,    GameAssets._RUN_UP_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_DOWN,  GameAssets._RUN_DOWN_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL, GameAssets._RUN_RIGHT_ASSET),

                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_UP,    GameAssets._RUN_UP_ASSET),
                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN,  GameAssets._RUN_DOWN_ASSET),
            };

        final DirectionAnim[] idleAnims =
            {
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_UP,     GameAssets._IDLE_UP_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_DOWN,   GameAssets._IDLE_DOWN_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL,  GameAssets._IDLE_LEFT_ASSET),

                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_UP,    GameAssets._IDLE_UP_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_DOWN,  GameAssets._IDLE_DOWN_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL, GameAssets._IDLE_RIGHT_ASSET),

                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_UP,    GameAssets._IDLE_UP_ASSET),
                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN,  GameAssets._IDLE_DOWN_ASSET),
            };

        final DirectionAnim[] fightAnims =
            {
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_UP,     GameAssets._FIGHT_UP_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_DOWN,   GameAssets._FIGHT_DOWN_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL,  GameAssets._FIGHT_LEFT_ASSET),

                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_UP,    GameAssets._FIGHT_UP_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_DOWN,  GameAssets._FIGHT_DOWN_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL, GameAssets._FIGHT_RIGHT_ASSET),

                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_UP,    GameAssets._FIGHT_UP_ASSET),
                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN,  GameAssets._FIGHT_DOWN_ASSET),
            };

        final DirectionAnim[] dyingAnims =
            {
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_UP,     GameAssets._DYING_UP_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_DOWN,   GameAssets._DYING_DOWN_LEFT_ASSET),
                new DirectionAnim(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL,  GameAssets._DYING_LEFT_ASSET),

                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_UP,    GameAssets._DYING_UP_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_DOWN,  GameAssets._DYING_DOWN_RIGHT_ASSET),
                new DirectionAnim(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL, GameAssets._DYING_RIGHT_ASSET),

                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_UP,    GameAssets._DYING_UP_ASSET),
                new DirectionAnim(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN,  GameAssets._DYING_DOWN_ASSET),
            };

        switch (getSpriteAction())
        {
            case _RUNNING:
            {
                EntityDescriptor descriptor = new EntityDescriptor();

                descriptor._FRAMES   = GameAssets._PLAYER_RUN_FRAMES;
                descriptor._PLAYMODE = Animation.PlayMode.LOOP;
                descriptor._SIZE     = GameAssets.getAssetSize(GraphicID.G_PLAYER);

                String asset = runningAnims[0].animation;

                for (DirectionAnim directionAnim : runningAnims)
                {
                    if ((direction.getX() == directionAnim.dirX)
                        && (direction.getY() == directionAnim.dirY))
                    {
                        asset = directionAnim.animation;
                    }
                }

                descriptor._ASSET = app.assets.getAnimationsAtlas().findRegion(asset);

                setAnimation(descriptor, 0.5f);
            }
            break;

            case _PAUSED:
            case _WAITING:
            case _STANDING:
            case _RIDING:
            {
                EntityDescriptor descriptor = new EntityDescriptor();

                descriptor._FRAMES   = GameAssets._PLAYER_STAND_FRAMES;
                descriptor._PLAYMODE = Animation.PlayMode.LOOP;
                descriptor._SIZE     = GameAssets.getAssetSize(GraphicID.G_PLAYER);

                String asset = idleAnims[0].animation;

                for (DirectionAnim directionAnim : idleAnims)
                {
                    if ((lookingAt.getX() == directionAnim.dirX)
                        && (lookingAt.getY() == directionAnim.dirY))
                    {
                        asset = directionAnim.animation;
                    }
                }

                descriptor._ASSET = app.assets.getAnimationsAtlas().findRegion(asset);

                setAnimation(descriptor, 1.0f);
            }
            break;

            case _FIGHTING:
            {
                EntityDescriptor descriptor = new EntityDescriptor();

                descriptor._FRAMES   = GameAssets._PLAYER_FIGHT_FRAMES;
                descriptor._PLAYMODE = Animation.PlayMode.LOOP_PINGPONG;
                descriptor._SIZE     = GameAssets.getAssetSize(GraphicID.G_PLAYER_FIGHT);

                String asset = fightAnims[0].animation;

                for (DirectionAnim directionAnim : fightAnims)
                {
                    if ((lookingAt.getX() == directionAnim.dirX)
                        && (lookingAt.getY() == directionAnim.dirY))
                    {
                        asset = directionAnim.animation;
                    }
                }

                descriptor._ASSET = app.assets.getAnimationsAtlas().findRegion(asset);

                setAnimation(descriptor, 0.5f);
            }
            break;

            case _CHANGING_ROOM:
            case _SPAWNING:
            case _HURT:
            {
            }
            break;

            case _LAST_RITES:
            case _DYING:
            {
                EntityDescriptor descriptor = new EntityDescriptor();

                descriptor._FRAMES   = GameAssets._PLAYER_DYING_FRAMES;
                descriptor._PLAYMODE = Animation.PlayMode.NORMAL;
                descriptor._SIZE     = GameAssets.getAssetSize(GraphicID.G_PLAYER_FIGHT);

                String asset = dyingAnims[0].animation;

                for (DirectionAnim directionAnim : dyingAnims)
                {
                    if ((lookingAt.getX() == directionAnim.dirX)
                        && (lookingAt.getY() == directionAnim.dirY))
                    {
                        asset = directionAnim.animation;
                    }
                }

                descriptor._ASSET = app.assets.getAnimationsAtlas().findRegion(asset);

                setAnimation(descriptor, 0.5f);
            }
            break;

            default:
            {
                Trace.__FILE_FUNC_WithDivider();
                Trace.dbg("Unsupported player action: " + getSpriteAction());

                Stats.incMeter(Meters._BAD_PLAYER_ACTION.get());
            }
            break;
        }

        if (localIsDrawable)
        {
            sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
            elapsedAnimTime += Gdx.graphics.getDeltaTime();
        }
    }

    @Override
    public void updateCollisionBox()
    {
        collisionObject.rectangle.x      = sprite.getX() + (frameWidth / 3);
        collisionObject.rectangle.y      = sprite.getY() + (frameHeight / 5);
        collisionObject.rectangle.width  = frameWidth / 3;
        collisionObject.rectangle.height = frameHeight / 2;

        viewBox.x      = (int) (sprite.getX() - (_VIEWBOX_WIDTH / 2));
        viewBox.y      = (int) (sprite.getY() - (_VIEWBOX_HEIGHT / 2));
        viewBox.width  = (int) _VIEWBOX_WIDTH;
        viewBox.height = (int) _VIEWBOX_HEIGHT;

        if (app.settings.isEnabled((Settings._SPAWNPOINTS)))
        {
            tileRectangle.x      = (((collisionObject.rectangle.x + (frameWidth / 2)) / Gfx.getTileWidth()));
            tileRectangle.y      = ((collisionObject.rectangle.y - Gfx.getTileHeight()) / Gfx.getTileHeight());
            tileRectangle.width  = Gfx.getTileWidth();
            tileRectangle.height = Gfx.getTileHeight();
        }

        rightEdge = collisionObject.rectangle.x + collisionObject.rectangle.width;
        topEdge   = collisionObject.rectangle.y + collisionObject.rectangle.height;
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        if (localIsDrawable)
        {
            super.draw(spriteBatch);
        }

        if (getSpriteAction() == Actions._SPAWNING)
        {
            spriteBatch.draw
                (
                    app.entityUtils.getKeyFrame(altAnim, elapsedAltAnimTime, false),
                    this.sprite.getX(),
                    this.sprite.getY()
                );
        }
        else
        {
            if (actionButton.getActionMode() == Actions._OFFER_ABXY_A)
            {
                spriteBatch.draw
                    (
                        abxy[0],
                        this.sprite.getX() + (this.frameWidth / 3),
                        this.sprite.getY() + this.frameHeight
                    );
            }
            else if (actionButton.getActionMode() == Actions._OFFER_ABXY_B)
            {
                spriteBatch.draw
                    (
                        abxy[1],
                        this.sprite.getX() + (this.frameWidth / 3),
                        this.sprite.getY() + this.frameHeight
                    );
            }
            else if (actionButton.getActionMode() == Actions._OFFER_ABXY_X)
            {
                spriteBatch.draw
                    (
                        abxy[2],
                        this.sprite.getX() + (this.frameWidth / 3),
                        this.sprite.getY() + this.frameHeight
                    );
            }
            else if (actionButton.getActionMode() == Actions._OFFER_ABXY_Y)
            {
                spriteBatch.draw
                    (
                        abxy[3],
                        this.sprite.getX() + (this.frameWidth / 3),
                        this.sprite.getY() + this.frameHeight
                    );
            }
            else if (isOnFloorButton && app.gameProgress.keyCount.isEmpty() && !app.getHud().messageManager.isEnabled())
            {
                app.getHud().messageManager.addZoomMessage
                    (
                        GameAssets._KEY_NEEDED_MSG_ASSET,
                        5000,
                        (Gfx._VIEW_WIDTH - GameAssets.getAssetSize(GraphicID._KEY_NEEDED).getX()) / 2,
                        300
                    );
            }
        }
    }

    @Override
    public void setAction(Actions newAction)
    {
        if (getSpriteAction() != newAction)
        {
            previousAction = getSpriteAction();

            switch (newAction)
            {
                case _SPAWNING:
                {
                    //
                    // Spawn frames are the same size as standard
                    // LJM frames, so no need to modify frame sizes
                    altAnimFrames = new TextureRegion[GameAssets._PLAYER_SPAWN_FRAMES];
                    altAnim       = app.entityUtils.createAnimation
                        (
                            GameAssets._PLAYER_SPAWN_ASSET,
                            altAnimFrames,
                            GameAssets._PLAYER_SPAWN_FRAMES,
                            Animation.PlayMode.NORMAL
                        );

                    altAnim.setFrameDuration(0.3f / 6.0f);
                    elapsedAltAnimTime = 0.0f;
                }
                break;

                case _STANDING:
                {
                    canOpenMessagePanel = true;
                }
                break;

                case _PAUSED:
                case _WAITING:
                case _TELEPORTING:
                case _CHANGING_ROOM:
                case _DEAD:
                case _NO_ACTION:
                case _RUNNING:
                case _FIGHTING:
                case _LAST_RITES:
                case _HURT:
                case _DYING:
                case _RIDING:
                {
                }
                break;

                default:
                {
                    Trace.__FILE_FUNC("Unsupported player action: " + newAction);

                    Stats.incMeter(Meters._BAD_PLAYER_ACTION.get());
                }
                break;
            }

            super.setAction(newAction);
            elapsedAnimTime = 0;
        }
    }

    @SuppressWarnings("unused")
    public void hurt(GraphicID spriteHittingGid)
    {
        if (!app.settings.isEnabled(Settings._GOD_MODE))
        {
            if (!isHurting && (getSpriteAction() != Actions._HURT))
            {
                strength = (Math.max(0, strength - 1));

                isHurting = true;
                setAction(Actions._HURT);
            }
        }
    }

    public void kill()
    {
    }

    public void handleDying()
    {
        if (app.gameProgress.playerLifeOver)
        {
            app.gameProgress.lives.setToMinimum();
        }
        else
        {
            if (!Developer.isGodMode())
            {
                app.gameProgress.lives.subtract(1);
            }
        }

        // Restart if this player has more lives left...
        if (app.gameProgress.lives.getTotal() > 0)
        {
            setAction(Actions._RESETTING);
            isDrawable                    = false;
            app.gameProgress.isRestarting = true;
            app.mapData.checkPoint.set(sprite.getX(), sprite.getY());
        }
        else
        {
            setAction(Actions._DEAD);

            app.gameProgress.isRestarting = false;

            if (app.gameProgress.playerLifeOver)
            {
                app.gameProgress.lives.setToMinimum();
            }
        }
    }

    /**
     * Handles the player movement.
     */
    private void movePlayer()
    {
        if (isMovingX)
        {
            if (speed.getX() > maxMoveSpeed.getX())
            {
                // Slow down if going too fast
                speed.x -= 1;
            }
            else if ((stopWatch.time(TimeUnit.MILLISECONDS) >= (100 + (35 * speed.getX())))
                && (speed.getX() < maxMoveSpeed.getX()))
            {
                // Bring the player xSpeed up to max
                speed.x += 1;
                stopWatch.reset();
            }
            else if (speed.getX() == 0)
            {
                // minimum xSpeed
                speed.setX(1);
            }
        }
        else
        {
            speed.setX(0);
        }

        if (isMovingY)
        {
            if (speed.getY() > maxMoveSpeed.getY())
            {
                // Slow down if going too fast
                speed.y -= 1;
            }
            else if ((stopWatch.time(TimeUnit.MILLISECONDS) >= (100 + (35 * speed.getY())))
                && (speed.getY() < maxMoveSpeed.getY()))
            {
                // Bring the player xSpeed up to max
                speed.y += 1;
                stopWatch.reset();
            }
            else if (speed.getY() == 0)
            {
                // minimum xSpeed
                speed.setY(1);
            }
        }
        else
        {
            speed.setY(0);
        }

        if (isMovingX || isMovingY)
        {
            sprite.translate
                (
                    (speed.getX() * app.inputManager.getControllerXPercentage()),
                    (speed.getY() * app.inputManager.getControllerYPercentage())
                );

            if (getSpriteAction() != Actions._CHANGING_ROOM)
            {
                handleRoomExit();
            }
        }

        postMove();
    }

    private void handleRoomExit()
    {
        int roomStart = Room._UNDEFINED;

        if ((app.getPlayer().direction.getY() == Movement._DIRECTION_UP)
            && (app.getPlayer().sprite.getY() + app.getPlayer().frameHeight) > (Gfx.getMapHeight() - 180))
        {
            app.getRoomSystem().moveUp();
            direction.set(Movement._DIRECTION_STILL, Movement._DIRECTION_UP);
            roomStart = Room._S;
        }
        else if ((app.getPlayer().direction.getY() == Movement._DIRECTION_DOWN)
            && (app.getPlayer().sprite.getY() < 0))
        {
            app.getRoomSystem().moveDown();
            direction.set(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN);
            roomStart = Room._N;
        }

        if ((app.getPlayer().direction.getX() == Movement._DIRECTION_LEFT)
            && (app.getPlayer().sprite.getX() < 0))
        {
            app.getRoomSystem().moveLeft();
            direction.set(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL);
            roomStart = Room._E;
        }
        else if ((app.getPlayer().direction.getX() == Movement._DIRECTION_RIGHT)
            && (app.getPlayer().sprite.getX() + app.getPlayer().frameWidth) > Gfx.getMapWidth())
        {
            app.getRoomSystem().moveRight();
            direction.set(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL);
            roomStart = Room._W;
        }

        if (roomStart != Room._UNDEFINED)
        {
            app.getRoomSystem().playerStart = roomStart;
            app.gameProgress.levelCompleted = true;
            lookingAt.set(direction);

            setAction(Actions._CHANGING_ROOM);
        }
    }

    @Override
    public void dispose()
    {
        super.dispose();

        buttons.dispose();
        collision.dispose();
        actionButton.dispose();

        tileRectangle = null;
        buttons       = null;
        collision     = null;
        actionButton  = null;
    }
}
