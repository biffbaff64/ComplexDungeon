/*
 *  Copyright 24/04/2018 Red7Projects.
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

package com.red7projects.dungeon.physics.box2d;

import com.badlogic.gdx.physics.box2d.*;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.physics.aabb.CollisionObject;
import com.red7projects.dungeon.physics.aabb.CollisionRect;

@SuppressWarnings({"WeakerAccess"})
public class BodyBuilder
{
    private final App app;

    public BodyBuilder(App _app)
    {
        this.app = _app;
    }

    /**
     * Creates a Dynamic Box2D body which can be assigned to a GdxSprite.
     *
     * A dynamic body is fully simulated. They can be moved manually by the user,
     * but normally they move according to forces. A dynamic body can collide with
     * all body types. A dynamic body always has finite, non-zero mass. If you try to
     * set the mass of a dynamic body to zero, it will automatically acquire a mass
     * of one kilogram and it won’t rotate.
     *
     * @param entity      The GdxSprite of this entity
     * @param density     Object density
     * @param restitution The object restitution.
     */
    public void createDynamicCircle(GdxSprite entity, float density, float friction, float restitution)
    {
        entity.bodyDef = new BodyDef();
        entity.bodyDef.type = BodyDef.BodyType.DynamicBody;
        entity.bodyDef.fixedRotation = true;
        entity.bodyDef.position.set
            (
                (entity.sprite.getX() + (entity.frameWidth / 2)) / Gfx._PPM,
                (entity.sprite.getY() + (entity.frameHeight / 2)) / Gfx._PPM
            );

        entity.b2dBody = app.worldModel.box2DWorld.createBody(entity.bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius((entity.frameWidth / 2) / Gfx._PPM);

        FixtureDef fixtureDef           = new FixtureDef();
        fixtureDef.shape                = shape;
        fixtureDef.density              = density;
        fixtureDef.restitution          = restitution;
        fixtureDef.friction             = friction;
        fixtureDef.filter.categoryBits  = entity.collisionObject.bodyCategory;
        fixtureDef.filter.maskBits      = entity.collisionObject.collidesWith;

        entity.b2dBody.createFixture(fixtureDef);
        entity.b2dBody.setUserData(new BodyIdentity(entity, entity.collisionObject.gid, entity.collisionObject.type));

        shape.dispose();
    }

    public void createDynamicPolygon(GdxSprite entity, float density, float friction, float restitution)
    {
        entity.bodyDef = new BodyDef();
        entity.bodyDef.type = BodyDef.BodyType.DynamicBody;
        entity.bodyDef.fixedRotation = true;
        entity.bodyDef.position.set
            (
                (entity.sprite.getX() + (entity.frameWidth / 2)) / Gfx._PPM,
                (entity.sprite.getY() + (entity.frameHeight / 2)) / Gfx._PPM
            );

        entity.b2dBody = app.worldModel.box2DWorld.createBody(entity.bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox
            (
                ((entity.frameWidth / 2) / Gfx._PPM),
                ((entity.frameHeight / 2) / Gfx._PPM)
            );

        FixtureDef fixtureDef           = new FixtureDef();
        fixtureDef.shape                = shape;
        fixtureDef.density              = density;
        fixtureDef.restitution          = restitution;
        fixtureDef.friction             = friction;
        fixtureDef.filter.categoryBits  = entity.collisionObject.bodyCategory;
        fixtureDef.filter.maskBits      = entity.collisionObject.collidesWith;

        entity.b2dBody.createFixture(fixtureDef);
        entity.b2dBody.setUserData(new BodyIdentity(entity, entity.collisionObject.gid, entity.collisionObject.type));

        shape.dispose();
    }

    /**
     * Creates a Kinematic Box2D body which can be assigned to a GdxSprite.
     * <p>
     * A kinematic body moves under simulation according to its velocity. Kinematic bodies
     * do not respond to forces. They can be moved manually by the user, but normally a
     * kinematic body is moved by setting its velocity. A kinematic body behaves as if it has
     * infinite mass, however, Box2D stores zero for the mass and the inverse mass. Kinematic
     * bodies do not collide with other kinematic or static bodies.
     *
     * @param entity      The GdxSprite of this entity
     * @param density     Object density
     * @param restitution The object restitution.
     */
    public void createKinematicBody(GdxSprite entity, float density, float restitution)
    {
        entity.bodyDef = new BodyDef();
        entity.bodyDef.type = BodyDef.BodyType.DynamicBody;
        entity.bodyDef.fixedRotation = true;
        entity.bodyDef.position.set
            (
                (entity.sprite.getX() + (entity.frameWidth / 2)) / Gfx._PPM,
                (entity.sprite.getY() + (entity.frameHeight / 2)) / Gfx._PPM
            );

        entity.b2dBody = app.worldModel.box2DWorld.createBody(entity.bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox
            (
                ((entity.frameWidth / 2) / Gfx._PPM),
                ((entity.frameHeight / 2) / Gfx._PPM)
            );

        FixtureDef fixtureDef           = new FixtureDef();
        fixtureDef.shape                = shape;
        fixtureDef.density              = density;
        fixtureDef.restitution          = restitution;
        fixtureDef.friction             = 0;
        fixtureDef.filter.categoryBits  = entity.collisionObject.bodyCategory;
        fixtureDef.filter.maskBits      = entity.collisionObject.collidesWith;

        entity.b2dBody.createFixture(fixtureDef);
        entity.b2dBody.setUserData(new BodyIdentity(entity, entity.collisionObject.gid, entity.collisionObject.type));

        shape.dispose();
    }

    /**
     * Creates a Static Box2D body which can be assigned to a GdxSprite,
     * with a density of 1.0f and restitution of 0.0f.
     *
     * A static body does not move under simulation and behaves as if it has infinite mass.
     * Internally, Box2D stores zero for the mass and the inverse mass. Static bodies can be
     * moved manually by the user. A static body has zero velocity. Static bodies do not
     * collide with other static or kinematic bodies.
     *
     * @param collisionObject The {@link CollisionObject} to extract properties from.
     *
     * @return The newly created Body.
     */
    public Body createStaticBody(CollisionObject collisionObject)
    {
        return createStaticBody(collisionObject,1f,1f,0.15f);
    }

    /**
     * Creates a Static Box2D body which can be assigned to a GdxSprite.
     * <p>
     * A static body does not move under simulation and behaves as if it has infinite mass.
     * Internally, Box2D stores zero for the mass and the inverse mass. Static bodies can be
     * moved manually by the user. A static body has zero velocity. Static bodies do not
     * collide with other static or kinematic bodies.
     *
     * @param collisionObject The {@link CollisionObject} to extract properties from.
     * @param density     Object density
     * @param restitution The object restitution.
     *
     * @return The newly created Body.
     */
    public Body createStaticBody(CollisionObject collisionObject, float density, float friction, float restitution)
    {
        BodyDef bodyDef = createStaticBodyDef(collisionObject.rectangle);

        collisionObject.bodyDef = bodyDef;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox
            (
                ((collisionObject.rectangle.getWidth() / 2) / Gfx._PPM),
                ((collisionObject.rectangle.getHeight() / 2) / Gfx._PPM)
            );

        FixtureDef fixtureDef           = new FixtureDef();
        fixtureDef.shape                = shape;
        fixtureDef.density              = density;
        fixtureDef.friction             = friction;
        fixtureDef.restitution          = restitution;
        fixtureDef.filter.maskBits      = collisionObject.collidesWith;
        fixtureDef.filter.categoryBits  = collisionObject.bodyCategory;

        Body body = app.worldModel.box2DWorld.createBody(bodyDef);
        body.setUserData(new BodyIdentity(collisionObject, collisionObject.gid, collisionObject.type));
        body.createFixture(fixtureDef);

        return body;
    }

    private BodyDef createBodyDef(BodyDef.BodyType bodyType, GdxSprite entity)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.fixedRotation = true;

        bodyDef.position.set
            (
                (entity.sprite.getX() + (entity.sprite.getWidth() / 2)) / Gfx._PPM,
                (entity.sprite.getY() + (entity.sprite.getHeight() / 2)) / Gfx._PPM
            );

        entity.bodyDef = bodyDef;

        return bodyDef;
    }

    private BodyDef createStaticBodyDef(CollisionRect collisionRect)
    {
        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = true;

        bodyDef.position.set
            (
                (collisionRect.getX() + (collisionRect.getWidth() / 2)) / Gfx._PPM,
                (collisionRect.getY() + (collisionRect.getHeight() / 2)) / Gfx._PPM
            );

        return bodyDef;
    }

    private FixtureDef createFixtureDef(Shape shape, float density, float restitution)
    {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;

        return fixtureDef;
    }
}
