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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.objects.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Iterator;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Box2DMapObjectParser implements Disposable
{
    private Aliases aliases;

    private float unitScale = 1;

    private boolean ignoreMapUnitScale = false;

    private float tileWidth = 1;
    private float tileHeight = 1;

    private ObjectMap<String, Body> bodies = new ObjectMap<>();

    private ObjectMap<String, Fixture> fixtures = new ObjectMap<>();

    private ObjectMap<String, Joint> joints = new ObjectMap<>();

    public Box2DMapObjectParser()
    {
        this(new Aliases());
    }

    public Box2DMapObjectParser(Aliases aliases)
    {
        this.aliases = aliases;
    }

    public Box2DMapObjectParser(float unitScale)
    {
        this(unitScale, 1, 1);
    }

    public Box2DMapObjectParser(float unitScale, float tileWidth, float tileHeight)
    {
        this(new Aliases(), unitScale, tileWidth, tileHeight);
    }

    public Box2DMapObjectParser(Aliases aliases, float unitScale)
    {
        this(aliases, unitScale, 1, 1);
    }

    public Box2DMapObjectParser(Aliases aliases, float unitScale, float tileWidth, float tileHeight)
    {
        this.aliases = aliases;
        this.unitScale = unitScale;
        ignoreMapUnitScale = true;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public World load(World world, Map map)
    {
        if (!ignoreMapUnitScale)
        {
            unitScale = (Float) getProperty(map.getProperties(), aliases.unitScale, unitScale, Float.class);
        }

        tileWidth = (Integer) getProperty(map.getProperties(), "tilewidth", tileWidth, Integer.class);
        tileHeight = (Integer) getProperty(map.getProperties(), "tileheight", tileHeight, Integer.class);

        MapLayers layers = map.getLayers();

        for (MapLayer mapLayer : layers)
        {
            load(world, mapLayer);
        }

        return world;
    }

    @SuppressWarnings("UnusedReturnValue")
    public World load(World world, MapLayer layer)
    {
        MapObjects mapObjects = layer.getObjects();

        for (MapObject object : mapObjects)
        {
            if (!ignoreMapUnitScale)
            {
                unitScale = (Float) getProperty(layer.getProperties(), aliases.unitScale, unitScale, Float.class);
            }

            if (object.getProperties().get("type", String.class).equals(aliases.object))
            {
                createBody(world, object);
                createFixture(object);
            }
        }

        for (MapObject object : mapObjects)
        {
            if (!ignoreMapUnitScale)
            {
                unitScale = (Float) getProperty(layer.getProperties(), aliases.unitScale, unitScale, Float.class);
            }

            if (object.getProperties().get("type", String.class).equals(aliases.body))
            {
                createBody(world, object);
            }
        }

        for (MapObject object : mapObjects)
        {
            if (!ignoreMapUnitScale)
            {
                unitScale = (Float) getProperty(layer.getProperties(), aliases.unitScale, unitScale, Float.class);
            }

            if (object.getProperties().get("type", String.class).equals(aliases.fixture))
            {
                createFixture(object);
            }
        }

        for (MapObject object : mapObjects)
        {
            if (!ignoreMapUnitScale)
            {
                unitScale = (Float) getProperty(layer.getProperties(), aliases.unitScale, unitScale, Float.class);
            }

            if (object.getProperties().get("type", String.class).equals(aliases.joint))
            {
                createJoint(object);
            }
        }

        return world;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Body createBody(World world, MapObject mapObject)
    {
        MapProperties properties = mapObject.getProperties();

        String type = properties.get("type", String.class);

        if (!type.equals(aliases.body) && !type.equals(aliases.object))
        {
            throw new IllegalArgumentException("type of " + mapObject + " is  \"" + type + "\" instead of \"" + aliases.body + "\" or \"" + aliases.object + "\"");
        }

        BodyDef bodyDef = new BodyDef();

        bodyDef.type = properties.get(aliases.bodyType, String.class) != null ? properties.get(aliases.bodyType, String.class).equals(aliases.dynamicBody) ? BodyDef.BodyType.DynamicBody : properties.get(aliases.bodyType, String.class).equals(aliases.kinematicBody) ? BodyDef.BodyType.KinematicBody : properties.get(aliases.bodyType, String.class).equals(aliases.staticBody) ? BodyDef.BodyType.StaticBody : bodyDef.type : bodyDef.type;
        bodyDef.active = (Boolean) getProperty(properties, aliases.active, bodyDef.active, Boolean.class);
        bodyDef.allowSleep = (Boolean) getProperty(properties, aliases.allowSleep, bodyDef.allowSleep, Boolean.class);
        bodyDef.angle = (Float) getProperty(properties, aliases.angle, bodyDef.angle, Float.class);
        bodyDef.angularDamping = (Float) getProperty(properties, aliases.angularDamping, bodyDef.angularDamping, Float.class);
        bodyDef.angularVelocity = (Float) getProperty(properties, aliases.angularVelocity, bodyDef.angularVelocity, Float.class);
        bodyDef.awake = (Boolean) getProperty(properties, aliases.awake, bodyDef.awake, Boolean.class);
        bodyDef.bullet = (Boolean) getProperty(properties, aliases.bullet, bodyDef.bullet, Boolean.class);
        bodyDef.fixedRotation = (Boolean) getProperty(properties, aliases.fixedRotation, bodyDef.fixedRotation, Boolean.class);
        bodyDef.gravityScale = (Float) getProperty(properties, aliases.gravityunitScale, bodyDef.gravityScale, Float.class);
        bodyDef.linearDamping = (Float) getProperty(properties, aliases.linearDamping, bodyDef.linearDamping, Float.class);
        bodyDef.linearVelocity.set((Float) getProperty(properties, aliases.linearVelocityX, bodyDef.linearVelocity.x, Float.class), (Float) getProperty(properties, aliases.linearVelocityY, bodyDef.linearVelocity.y, Float.class));
        bodyDef.position.set((Integer) getProperty(properties, "x", bodyDef.position.x, Integer.class) * unitScale, (Integer) getProperty(properties, "y", bodyDef.position.y, Integer.class) * unitScale);

        Body body = world.createBody(bodyDef);

        String name = mapObject.getName();

        if (bodies.containsKey(name))
        {
            int duplicate = 1;

            while (bodies.containsKey(name + duplicate))
            {
                duplicate++;
            }
            name += duplicate;
        }

        bodies.put(name, body);

        return body;
    }

    public Fixture createFixture(MapObject mapObject)
    {
        MapProperties properties = mapObject.getProperties();

        String type = properties.get("type", String.class);

        Body body = bodies.get(type.equals(aliases.object) ? mapObject.getName() : properties.get(aliases.body, String.class));

        if (!type.equals(aliases.fixture) && !type.equals(aliases.object))
        {
            throw new IllegalArgumentException("type of " + mapObject + " is  \"" + type + "\" instead of \"" + aliases.fixture + "\" or \"" + aliases.object + "\"");
        }

        FixtureDef fixtureDef = new FixtureDef();
        Shape      shape;

        if (mapObject instanceof RectangleMapObject)
        {
            shape = new PolygonShape();
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            rectangle.x *= unitScale;
            rectangle.y *= unitScale;
            rectangle.width *= unitScale;
            rectangle.height *= unitScale;
            ((PolygonShape) shape).setAsBox(rectangle.width / 2, rectangle.height / 2, new Vector2(rectangle.x - body.getPosition().x + rectangle.width / 2, rectangle.y - body.getPosition().y + rectangle.height / 2), body.getAngle());
        }
        else if (mapObject instanceof PolygonMapObject)
        {
            shape = new PolygonShape();
            Polygon polygon = ((PolygonMapObject) mapObject).getPolygon();
            polygon.setPosition(polygon.getX() * unitScale - body.getPosition().x, polygon.getY() * unitScale - body.getPosition().y);
            polygon.setScale(unitScale, unitScale);
            ((PolygonShape) shape).set(polygon.getTransformedVertices());
        }
        else if (mapObject instanceof PolylineMapObject)
        {
            shape = new ChainShape();
            Polyline polyline = ((PolylineMapObject) mapObject).getPolyline();
            polyline.setPosition(polyline.getX() * unitScale - body.getPosition().x, polyline.getY() * unitScale - body.getPosition().y);
            polyline.setScale(unitScale, unitScale);
            ((ChainShape) shape).createChain(polyline.getTransformedVertices());
        }
        else if (mapObject instanceof CircleMapObject)
        {
            shape = new CircleShape();
            Circle circle = ((CircleMapObject) mapObject).getCircle();
            circle.setPosition(circle.x * unitScale - body.getPosition().x, circle.y * unitScale - body.getPosition().y);
            circle.radius *= unitScale;
            ((CircleShape) shape).setPosition(new Vector2(circle.x, circle.y));
            shape.setRadius(circle.radius);
        }
        else if (mapObject instanceof EllipseMapObject)
        {
            Ellipse ellipse = ((EllipseMapObject) mapObject).getEllipse();

            if (ellipse.width == ellipse.height)
            {
                CircleMapObject circleMapObject = new CircleMapObject(ellipse.x, ellipse.y, ellipse.width / 2);
                circleMapObject.setName(mapObject.getName());
                circleMapObject.getProperties().putAll(mapObject.getProperties());
                circleMapObject.setColor(mapObject.getColor());
                circleMapObject.setVisible(mapObject.isVisible());
                circleMapObject.setOpacity(mapObject.getOpacity());
                return createFixture(circleMapObject);
            }

            IllegalArgumentException exception = new IllegalArgumentException("Cannot parse " + mapObject.getName() + " because " + mapObject.getClass().getSimpleName() + "s that are not circles are not supported");
            Gdx.app.error(getClass().getSimpleName(), exception.getMessage(), exception);
            throw exception;
        }
        else if (mapObject instanceof TextureMapObject)
        {
            IllegalArgumentException exception = new IllegalArgumentException("Cannot parse " + mapObject.getName() + " because " + mapObject.getClass().getSimpleName() + "s are not supported");
            Gdx.app.error(getClass().getSimpleName(), exception.getMessage(), exception);
            throw exception;
        }
        else
        {
            throw new AssertionError(mapObject + " is a not known subclass of " + MapObject.class.getName());
        }

        fixtureDef.shape = shape;
        fixtureDef.density = (Float) getProperty(properties, aliases.density, fixtureDef.density, Float.class);
        fixtureDef.filter.categoryBits = (Short) getProperty(properties, aliases.categoryBits, fixtureDef.filter.categoryBits, Short.class);
        fixtureDef.filter.groupIndex = (Short) getProperty(properties, aliases.groupIndex, fixtureDef.filter.groupIndex, Short.class);
        fixtureDef.filter.maskBits = (Short) getProperty(properties, aliases.maskBits, fixtureDef.filter.maskBits, Short.class);
        fixtureDef.friction = (Float) getProperty(properties, aliases.friciton, fixtureDef.friction, Float.class);
        fixtureDef.isSensor = (Boolean) getProperty(properties, aliases.isSensor, fixtureDef.isSensor, Boolean.class);
        fixtureDef.restitution = (Float) getProperty(properties, aliases.restitution, fixtureDef.restitution, Float.class);

        Fixture fixture = body.createFixture(fixtureDef);

        shape.dispose();

        String name = mapObject.getName();
        if (fixtures.containsKey(name))
        {
            int duplicate = 1;
            while (fixtures.containsKey(name + duplicate))
            {
                duplicate++;
            }
            name += duplicate;
        }

        fixtures.put(name, fixture);

        return fixture;
    }

    @SuppressWarnings({"UnusedReturnValue", "IfCanBeSwitch"})
    public Joint createJoint(MapObject mapObject)
    {
        MapProperties properties = mapObject.getProperties();

        JointDef jointDef = null;

        String type = properties.get("type", String.class);
        if (!type.equals(aliases.joint))
        {
            throw new IllegalArgumentException("type of " + mapObject + " is  \"" + type + "\" instead of \"" + aliases.joint + "\"");
        }

        String jointType = properties.get(aliases.jointType, String.class);

        // boxes all possible values
        if (jointType.equals(aliases.distanceJoint))
        {
            DistanceJointDef distanceJointDef = new DistanceJointDef();
            distanceJointDef.dampingRatio = (Float) getProperty(properties, aliases.dampingRatio, distanceJointDef.dampingRatio, Float.class);
            distanceJointDef.frequencyHz = (Float) getProperty(properties, aliases.frequencyHz, distanceJointDef.frequencyHz, Float.class);
            distanceJointDef.length = (Float) getProperty(properties, aliases.length, distanceJointDef.length, Float.class) * (tileWidth + tileHeight) / 2 * unitScale;
            distanceJointDef.localAnchorA.set((Float) getProperty(properties, aliases.localAnchorAX, distanceJointDef.localAnchorA.x, Float.class) * tileWidth * unitScale, (Float) getProperty(properties, aliases.localAnchorAY, distanceJointDef.localAnchorA.y, Float.class) * tileHeight * unitScale);
            distanceJointDef.localAnchorB.set((Float) getProperty(properties, aliases.localAnchorBX, distanceJointDef.localAnchorB.x, Float.class) * tileWidth * unitScale, (Float) getProperty(properties, aliases.localAnchorBY, distanceJointDef.localAnchorB.y, Float.class) * tileHeight * unitScale);

            jointDef = distanceJointDef;
        }
        else if (jointType.equals(aliases.frictionJoint))
        {
            FrictionJointDef frictionJointDef = new FrictionJointDef();
            frictionJointDef.localAnchorA.set((Float) getProperty(properties, aliases.localAnchorAX, frictionJointDef.localAnchorA.x, Float.class) * tileWidth * unitScale, (Float) getProperty(properties, aliases.localAnchorAY, frictionJointDef.localAnchorA.y, Float.class) * tileHeight * unitScale);
            frictionJointDef.localAnchorB.set((Float) getProperty(properties, aliases.localAnchorBX, frictionJointDef.localAnchorB.x, Float.class) * tileWidth * unitScale, (Float) getProperty(properties, aliases.localAnchorBY, frictionJointDef.localAnchorB.y, Float.class) * tileHeight * unitScale);
            frictionJointDef.maxForce = (Float) getProperty(properties, aliases.maxForce, frictionJointDef.maxForce, Float.class);
            frictionJointDef.maxTorque = (Float) getProperty(properties, aliases.maxTorque, frictionJointDef.maxTorque, Float.class);

            jointDef = frictionJointDef;
        }
        else if (jointType.equals(aliases.gearJoint))
        {
            GearJointDef gearJointDef = new GearJointDef();
            gearJointDef.joint1 = joints.get(properties.get(aliases.joint1, String.class));
            gearJointDef.joint2 = joints.get(properties.get(aliases.joint2, String.class));
            gearJointDef.ratio = (Float) getProperty(properties, aliases.ratio, gearJointDef.ratio, Float.class);

            jointDef = gearJointDef;
        }
        else if (jointType.equals(aliases.mouseJoint))
        {
            MouseJointDef mouseJointDef = new MouseJointDef();
            mouseJointDef.dampingRatio = (Float) getProperty(properties, aliases.dampingRatio, mouseJointDef.dampingRatio, Float.class);
            mouseJointDef.frequencyHz = (Float) getProperty(properties, aliases.frequencyHz, mouseJointDef.frequencyHz, Float.class);
            mouseJointDef.maxForce = (Float) getProperty(properties, aliases.maxForce, mouseJointDef.maxForce, Float.class);
            mouseJointDef.target.set((Float) getProperty(properties, aliases.targetX, mouseJointDef.target.x, Float.class) * tileWidth * unitScale, (Float) getProperty(properties, aliases.targetY, mouseJointDef.target.y, Float.class) * tileHeight * unitScale);

            jointDef = mouseJointDef;
        }
        else if (jointType.equals(aliases.prismaticJoint))
        {
            PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
            prismaticJointDef.enableLimit = (Boolean) getProperty(properties, aliases.enableLimit, prismaticJointDef.enableLimit, Boolean.class);
            prismaticJointDef.enableMotor = (Boolean) getProperty(properties, aliases.enableMotor, prismaticJointDef.enableMotor, Boolean.class);
            prismaticJointDef.localAnchorA.set((Float) getProperty(properties, aliases.localAnchorAX, prismaticJointDef.localAnchorA.x, Float.class) * tileWidth * unitScale, (Float) getProperty(properties, aliases.localAnchorAY, prismaticJointDef.localAnchorA.y, Float.class) * tileHeight * unitScale);
            prismaticJointDef.localAnchorB.set((Float) getProperty(properties, aliases.localAnchorBX, prismaticJointDef.localAnchorB.x, Float.class) * tileWidth * unitScale, (Float) getProperty(properties, aliases.localAnchorBY, prismaticJointDef.localAnchorB.y, Float.class) * tileHeight * unitScale);
            prismaticJointDef.localAxisA.set((Float) getProperty(properties, aliases.localAxisAX, prismaticJointDef.localAxisA.x, Float.class), (Float) getProperty(properties, aliases.localAxisAY, prismaticJointDef.localAxisA.y, Float.class));
            prismaticJointDef.lowerTranslation = (Float) getProperty(properties, aliases.lowerTranslation, prismaticJointDef.lowerTranslation, Float.class) * (tileWidth + tileHeight) / 2 * unitScale;
            prismaticJointDef.maxMotorForce = (Float) getProperty(properties, aliases.maxMotorForce, prismaticJointDef.maxMotorForce, Float.class);
            prismaticJointDef.motorSpeed = (Float) getProperty(properties, aliases.motorSpeed, prismaticJointDef.motorSpeed, Float.class);
            prismaticJointDef.referenceAngle = (Float) getProperty(properties, aliases.referenceAngle, prismaticJointDef.referenceAngle, Float.class);
            prismaticJointDef.upperTranslation = (Float) getProperty(properties, aliases.upperTranslation, prismaticJointDef.upperTranslation, Float.class) * (tileWidth + tileHeight) / 2 * unitScale;

            jointDef = prismaticJointDef;
        }
        else if (jointType.equals(aliases.pulleyJoint))
        {
            PulleyJointDef pulleyJointDef = new PulleyJointDef();
            pulleyJointDef.groundAnchorA.set((Float) getProperty(properties, aliases.groundAnchorAX, pulleyJointDef.groundAnchorA.x, Float.class) * tileWidth * unitScale, (Float) getProperty(properties, aliases.groundAnchorAY, pulleyJointDef.groundAnchorA.y, Float.class) * tileHeight * unitScale);
            pulleyJointDef.groundAnchorB.set((Float) getProperty(properties, aliases.groundAnchorBX, pulleyJointDef.groundAnchorB.x, Float.class) * tileWidth * unitScale, (Float) getProperty(properties, aliases.groundAnchorBY, pulleyJointDef.groundAnchorB.y, Float.class) * tileHeight * unitScale);
            pulleyJointDef.lengthA = (Float) getProperty(properties, aliases.lengthA, pulleyJointDef.lengthA, Float.class) * (tileWidth + tileHeight) / 2 * unitScale;
            pulleyJointDef.lengthB = (Float) getProperty(properties, aliases.lengthB, pulleyJointDef.lengthB, Float.class) * (tileWidth + tileHeight) / 2 * unitScale;
            pulleyJointDef.localAnchorA.set((Float) getProperty(properties, aliases.localAnchorAX, pulleyJointDef.localAnchorA.x, Float.class) * tileWidth * unitScale, (Float) getProperty(properties, aliases.localAnchorAY, pulleyJointDef.localAnchorA.y, Float.class) * tileHeight * unitScale);
            pulleyJointDef.localAnchorB.set((Float) getProperty(properties, aliases.localAnchorBX, pulleyJointDef.localAnchorB.x, Float.class) * tileWidth * unitScale, (Float) getProperty(properties, aliases.localAnchorBY, pulleyJointDef.localAnchorB.y, Float.class) * tileHeight * unitScale);
            pulleyJointDef.ratio = (Float) getProperty(properties, aliases.ratio, pulleyJointDef.ratio, Float.class);

            jointDef = pulleyJointDef;
        }
        else if (jointType.equals(aliases.revoluteJoint))
        {
            RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
            revoluteJointDef.enableLimit = (Boolean) getProperty(properties, aliases.enableLimit, revoluteJointDef.enableLimit, Boolean.class);
            revoluteJointDef.enableMotor = (Boolean) getProperty(properties, aliases.enableMotor, revoluteJointDef.enableMotor, Boolean.class);
            revoluteJointDef.localAnchorA.set((Float) getProperty(properties, aliases.localAnchorAX, revoluteJointDef.localAnchorA.x, Float.class) * tileWidth * unitScale, (Float) getProperty(properties, aliases.localAnchorAY, revoluteJointDef.localAnchorA.y, Float.class) * tileHeight * unitScale);
            revoluteJointDef.localAnchorB.set((Float) getProperty(properties, aliases.localAnchorBX, revoluteJointDef.localAnchorB.x, Float.class) * tileWidth * unitScale, (Float) getProperty(properties, aliases.localAnchorBY, revoluteJointDef.localAnchorB.y, Float.class) * tileHeight * unitScale);
            revoluteJointDef.lowerAngle = (Float) getProperty(properties, aliases.lowerAngle, revoluteJointDef.lowerAngle, Float.class);
            revoluteJointDef.maxMotorTorque = (Float) getProperty(properties, aliases.maxMotorTorque, revoluteJointDef.maxMotorTorque, Float.class);
            revoluteJointDef.motorSpeed = (Float) getProperty(properties, aliases.motorSpeed, revoluteJointDef.motorSpeed, Float.class);
            revoluteJointDef.referenceAngle = (Float) getProperty(properties, aliases.referenceAngle, revoluteJointDef.referenceAngle, Float.class);
            revoluteJointDef.upperAngle = (Float) getProperty(properties, aliases.upperAngle, revoluteJointDef.upperAngle, Float.class);

            jointDef = revoluteJointDef;
        }
        else if (jointType.equals(aliases.ropeJoint))
        {
            RopeJointDef ropeJointDef = new RopeJointDef();
            ropeJointDef.localAnchorA.set((Float) getProperty(properties, aliases.localAnchorAX, ropeJointDef.localAnchorA.x, Float.class) * tileWidth * unitScale, (Float) getProperty(properties, aliases.localAnchorAY, ropeJointDef.localAnchorA.y, Float.class) * tileHeight * unitScale);
            ropeJointDef.localAnchorB.set((Float) getProperty(properties, aliases.localAnchorBX, ropeJointDef.localAnchorB.x, Float.class) * tileWidth * unitScale, (Float) getProperty(properties, aliases.localAnchorBY, ropeJointDef.localAnchorB.y, Float.class) * tileHeight * unitScale);
            ropeJointDef.maxLength = (((Float) getProperty(properties, aliases.maxLength, ropeJointDef.maxLength, Float.class) * (tileWidth + tileHeight)) / 2) * unitScale;

            jointDef = ropeJointDef;
        }
        else if (jointType.equals(aliases.weldJoint))
        {
            WeldJointDef weldJointDef = new WeldJointDef();
            weldJointDef.localAnchorA.set((Float) getProperty(properties, aliases.localAnchorAX, weldJointDef.localAnchorA.x, Float.class) * tileWidth * unitScale, (Float) getProperty(properties, aliases.localAnchorAY, weldJointDef.localAnchorA.y, Float.class) * tileHeight * unitScale);
            weldJointDef.localAnchorB.set((Float) getProperty(properties, aliases.localAnchorBX, weldJointDef.localAnchorB.x, Float.class) * tileWidth * unitScale, (Float) getProperty(properties, aliases.localAnchorBY, weldJointDef.localAnchorB.y, Float.class) * tileHeight * unitScale);
            weldJointDef.referenceAngle = (Float) getProperty(properties, aliases.referenceAngle, weldJointDef.referenceAngle, Float.class);

            jointDef = weldJointDef;
        }
        else if (jointType.equals(aliases.wheelJoint))
        {
            WheelJointDef wheelJointDef = new WheelJointDef();
            wheelJointDef.dampingRatio = (Float) getProperty(properties, aliases.dampingRatio, wheelJointDef.dampingRatio, Float.class);
            wheelJointDef.enableMotor = (Boolean) getProperty(properties, aliases.enableMotor, wheelJointDef.enableMotor, Boolean.class);
            wheelJointDef.frequencyHz = (Float) getProperty(properties, aliases.frequencyHz, wheelJointDef.frequencyHz, Float.class);
            wheelJointDef.localAnchorA.set((Float) getProperty(properties, aliases.localAnchorAX, wheelJointDef.localAnchorA.x, Float.class) * tileWidth * unitScale, (Float) getProperty(properties, aliases.localAnchorAY, wheelJointDef.localAnchorA.y, Float.class) * tileHeight * unitScale);
            wheelJointDef.localAnchorB.set((Float) getProperty(properties, aliases.localAnchorBX, wheelJointDef.localAnchorB.x, Float.class) * tileWidth * unitScale, (Float) getProperty(properties, aliases.localAnchorBY, wheelJointDef.localAnchorB.y, Float.class) * tileHeight * unitScale);
            wheelJointDef.localAxisA.set((Float) getProperty(properties, aliases.localAxisAX, wheelJointDef.localAxisA.x, Float.class), (Float) getProperty(properties, aliases.localAxisAY, wheelJointDef.localAxisA.y, Float.class));
            wheelJointDef.maxMotorTorque = (Float) getProperty(properties, aliases.maxMotorTorque, wheelJointDef.maxMotorTorque, Float.class);
            wheelJointDef.motorSpeed = (Float) getProperty(properties, aliases.motorSpeed, wheelJointDef.motorSpeed, Float.class);

            jointDef = wheelJointDef;
        }

        assert jointDef != null;
        jointDef.bodyA = bodies.get(properties.get(aliases.bodyA, String.class));
        jointDef.bodyB = bodies.get(properties.get(aliases.bodyB, String.class));
        jointDef.collideConnected = (Boolean) getProperty(properties, aliases.collideConnected, jointDef.collideConnected, Boolean.class);

        Joint joint = jointDef.bodyA.getWorld().createJoint(jointDef);

        String name = mapObject.getName();
        if (joints.containsKey(name))
        {
            int duplicate = 1;
            while (joints.containsKey(name + duplicate))
            {
                duplicate++;
            }
            name += duplicate;
        }

        joints.put(name, joint);

        return joint;
    }

    private Object getProperty(MapProperties properties, String property, Object defaultValue, Class<?> clazz)
    {
        if (clazz == Float.class)
        {
            return (properties.get(property, String.class) != null) ? Float.parseFloat(properties.get(property, String.class)) : defaultValue;
        }
        else if (clazz == Integer.class)
        {
            return (properties.get(property, String.class) != null) ? properties.get(property, Integer.class) : defaultValue;
        }
        else if (clazz == Short.class)
        {
            return (properties.get(property, String.class) != null) ? properties.get(property, Short.class) : defaultValue;
        }
        else if (clazz == Boolean.class)
        {
            return (properties.get(property, String.class) != null) ? Boolean.parseBoolean(properties.get(property, String.class)) : defaultValue;
        }
        return defaultValue;
    }

    public String getHierarchy(Map map)
    {
        StringBuilder hierarchy = new StringBuilder(map.getClass().getSimpleName());

        hierarchy.append(" ");

        Iterator<String> keys = map.getProperties().getKeys();

        while (keys.hasNext())
        {
            String key = keys.next();

            hierarchy.append(key);
            hierarchy.append(": ");
            hierarchy.append(map.getProperties().get(key));
            hierarchy.append(" ");
        }

        for (MapLayer layer : map.getLayers())
        {
            hierarchy.append(layer.getName());
            hierarchy.append(" (");
            hierarchy.append(layer.getClass().getSimpleName());
            hierarchy.append("): ");

            String layerHierarchy = getHierarchy(layer).replace("\n", "\n\t\t");
            layerHierarchy = layerHierarchy.endsWith("\n\t\t") ? layerHierarchy.substring(0, layerHierarchy.lastIndexOf("\n\t\t")) : layerHierarchy;

            hierarchy.append(layerHierarchy);
        }

        return hierarchy.toString();
    }

    public String getHierarchy(MapLayer layer)
    {
        StringBuilder hierarchy = new StringBuilder();

        for (MapObject object : layer.getObjects())
        {
            hierarchy.append(object.getName());
            hierarchy.append(" (");
            hierarchy.append(object.getClass().getSimpleName());
            hierarchy.append("):\n");

            Iterator<String> keys = object.getProperties().getKeys();

            while (keys.hasNext())
            {
                String key = keys.next();

                hierarchy.append("\t");
                hierarchy.append(key);
                hierarchy.append(": ");
                hierarchy.append(object.getProperties().get(key));
                hierarchy.append("\n");
            }
        }

        return hierarchy.toString();
    }

    public float getUnitScale()
    {
        return unitScale;
    }

    public void setUnitScale(float unitScale)
    {
        this.unitScale = unitScale;
    }

    public boolean isIgnoreMapUnitScale()
    {
        return ignoreMapUnitScale;
    }

    public void setIgnoreMapUnitScale(boolean ignoreMapUnitScale)
    {
        this.ignoreMapUnitScale = ignoreMapUnitScale;
    }

    public float getTileWidth()
    {
        return tileWidth;
    }

    public void setTileWidth(float tileWidth)
    {
        this.tileWidth = tileWidth;
    }

    public float getTileHeight()
    {
        return tileHeight;
    }

    public void setTileHeight(float tileHeight)
    {
        this.tileHeight = tileHeight;
    }

    public Aliases getAliases()
    {
        return aliases;
    }

    public void setAliases(Aliases aliases)
    {
        this.aliases = aliases;
    }

    public ObjectMap<String, Body> getBodies()
    {
        return bodies;
    }

    public ObjectMap<String, Fixture> getFixtures()
    {
        return fixtures;
    }

    public ObjectMap<String, Joint> getJoints()
    {
        return joints;
    }

    @SuppressWarnings("WeakerAccess")
    public static class Aliases
    {
        public final String bodyType         = "bodyType";
        public final String dynamicBody      = "DynamicBody";
        public final String kinematicBody    = "KinematicBody";
        public final String staticBody       = "StaticBody";
        public final String active           = "active";
        public final String allowSleep       = "allowSleep";
        public final String angle            = "angle";
        public final String angularDamping   = "angularDamping";
        public final String angularVelocity  = "angularVelocity";
        public final String awake            = "awake";
        public final String bullet           = "bullet";
        public final String fixedRotation    = "fixedRotation";
        public final String gravityunitScale = "gravityunitScale";
        public final String linearDamping    = "linearDamping";
        public final String linearVelocityX  = "linearVelocityX";
        public final String linearVelocityY  = "linearVelocityY";
        public final String density          = "density";
        public final String categoryBits     = "categoryBits";
        public final String groupIndex       = "groupIndex";
        public final String maskBits         = "maskBits";
        public final String friciton         = "friction";
        public final String isSensor         = "isSensor";
        public final String restitution      = "restitution";
        public final String body             = "body";
        public final String fixture          = "fixture";
        public final String joint            = "joint";
        public final String jointType        = "jointType";
        public final String distanceJoint    = "DistanceJoint";
        public final String frictionJoint    = "FrictionJoint";
        public final String gearJoint        = "GearJoint";
        public final String mouseJoint       = "MouseJoint";
        public final String prismaticJoint   = "PrismaticJoint";
        public final String pulleyJoint      = "PulleyJoint";
        public final String revoluteJoint    = "RevoluteJoint";
        public final String ropeJoint        = "RopeJoint";
        public final String weldJoint        = "WeldJoint";
        public final String wheelJoint       = "WheelJoint";
        public final String bodyA            = "bodyA";
        public final String bodyB            = "bodyB";
        public final String collideConnected = "collideConnected";
        public final String dampingRatio     = "dampingRatio";
        public final String frequencyHz      = "frequencyHz";
        public final String length           = "length";
        public final String localAnchorAX    = "localAnchorAX";
        public final String localAnchorAY    = "localAnchorAY";
        public final String localAnchorBX    = "localAnchorBX";
        public final String localAnchorBY    = "localAnchorBY";
        public final String maxForce         = "maxForce";
        public final String maxTorque        = "maxTorque";
        public final String joint1           = "joint1";
        public final String joint2           = "joint2";
        public final String ratio            = "ratio";
        public final String targetX          = "targetX";
        public final String targetY          = "targetY";
        public final String enableLimit      = "enableLimit";
        public final String enableMotor      = "enableMotor";
        public final String localAxisAX      = "localAxisAX";
        public final String localAxisAY      = "localAxisAY";
        public final String lowerTranslation = "lowerTranslation";
        public final String maxMotorForce    = "maxMotorForce";
        public final String motorSpeed       = "motorSpeed";
        public final String referenceAngle   = "referenceAngle";
        public final String upperTranslation = "upperTranslation";
        public final String groundAnchorAX   = "groundAnchorAX";
        public final String groundAnchorAY   = "groundAnchorAY";
        public final String groundAnchorBX   = "groundAnchorBX";
        public final String groundAnchorBY   = "groundAnchorBY";
        public final String lengthA          = "lengthA";
        public final String lengthB          = "lengthB";
        public final String lowerAngle       = "lowerAngle";
        public final String maxMotorTorque   = "maxMotorTorque";
        public final String upperAngle       = "upperAngle";
        public final String maxLength        = "maxLength";
        public final String object           = "object";
        public final String unitScale        = "unitScale";
    }

    @Override
    public void dispose()
    {
        bodies.clear();
        fixtures.clear();
        joints.clear();

        bodies = null;
        fixtures = null;
        joints = null;

        aliases = null;
    }
}
