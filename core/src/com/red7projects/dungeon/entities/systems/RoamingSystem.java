package com.red7projects.dungeon.entities.systems;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.utils.logging.StopWatch;

import java.util.concurrent.TimeUnit;

public class RoamingSystem implements DecisionSystem
{
    public float speedX;
    public float speedY;
    public boolean isAdjustingTarget;
    public boolean rotationAllowed;
    public boolean checkMoveAllowed;

    private Vector2 destination;
    private final App app;

    public RoamingSystem(App app)
    {
        this.app = app;
    }

    @Override
    public void update(final GdxSprite parentSprite, final GdxSprite target)
    {
        Vector2 vector2 = getTargetPosition(parentSprite, target);

        faceTarget(vector2.x, vector2.y, parentSprite);
    }

    @Override
    public Vector2 getTargetPosition(final GdxSprite parentSprite, final GdxSprite target)
    {
        float targetX;
        float targetY;

        if (isAdjustingTarget)
        {
            targetX = destination.x;
            targetY = destination.y;

            if (parentSprite.invisibilityTimer.time(TimeUnit.MILLISECONDS) > 3500)
            {
                isAdjustingTarget = false;
            }
        }
        else
        {
            targetX = (target.sprite.getX() + (target.frameWidth / 2));
            targetY = (target.sprite.getY() + (target.frameHeight / 2));
        }

        return new Vector2(targetX, targetY);
    }

    @Override
    public void setAdjustedTarget(final GdxSprite parentSprite)
    {
        int trys = 0;

        do
        {
            destination.set
                (
                    MathUtils.random(Gfx.getMapWidth()),
                    MathUtils.random(Gfx.getMapHeight())
                );

            trys++;
        }
        while ((trys < 1000) && (app.getPlayer().viewBox.contains(destination)));

        parentSprite.collisionObject.action = Actions._INVISIBLE;
        parentSprite.invisibilityTimer = StopWatch.start();
    }

    @Override
    public void faceTarget(final float targetX, final float targetY, final GdxSprite parentSprite)
    {
        Vector2 vector2 = getTargetVector(targetX, targetY, parentSprite);

        if (rotationAllowed)
        {
            parentSprite.sprite.setRotation(vector2.angle() - 90);
        }

        if (parentSprite.getSpriteAction() == Actions._RUNNING)
        {
            calculateMove(vector2.nor(), parentSprite);
        }
    }

    @Override
    public Vector2 getTargetVector(final float targetX, final float targetY, final GdxSprite parentSprite)
    {
        Vector2 targetPos = new Vector2(targetX, targetY);
        Vector2 parentPos = new Vector2(parentSprite.sprite.getX(), parentSprite.sprite.getY());

        return new Vector2(targetPos.sub(parentPos));
    }

    @Override
    public void calculateMove(final Vector2 vector2, final GdxSprite parentSprite)
    {
        float xMove = (speedX * vector2.x);
        float yMove = (speedY * vector2.y);

        if (checkMoveAllowed)
        {
            xMove = checkXMovement(xMove, parentSprite);
            yMove = checkYMovement(yMove, parentSprite);
        }

        parentSprite.speed.set(xMove, yMove);

        if (parentSprite.speed.isEmpty())
        {
            parentSprite.setAction(Actions._STANDING);
        }
    }

    @Override
    public float checkXMovement(final float _xMove, final GdxSprite gdxSprite)
    {
        return 0;
    }

    @Override
    public float checkYMovement(final float _yMove, final GdxSprite gdxSprite)
    {
        return 0;
    }

    @Override
    public float distanceRemaining(final GdxSprite parentSprite, final Vector2 destination)
    {
        return new Vector2(parentSprite.sprite.getX(), parentSprite.sprite.getY()).dst(destination);
    }
}
