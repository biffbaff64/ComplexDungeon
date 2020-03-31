package com.red7projects.dungeon.entities.systems;

import com.badlogic.gdx.math.Vector2;
import com.red7projects.dungeon.entities.objects.GdxSprite;

public interface DecisionSystem
{
    void update(GdxSprite gdxSprite, GdxSprite target);

    Vector2 getTargetPosition(GdxSprite gdxSprite, GdxSprite target);

    void setAdjustedTarget(GdxSprite gdxSprite);

    void faceTarget(float targetX, float targetY, GdxSprite gdxSprite);

    Vector2 getTargetVector(float targetX, float targetY, GdxSprite gdxSprite);

    void calculateMove(Vector2 vector2, GdxSprite gdxSprite);

    float checkXMovement(float _xMove, GdxSprite gdxSprite);

    float checkYMovement(float _yMove, GdxSprite gdxSprite);

    float distanceRemaining(GdxSprite parentSprite, Vector2 destination);
}
