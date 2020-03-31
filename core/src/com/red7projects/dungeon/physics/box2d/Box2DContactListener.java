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

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.red7projects.dungeon.game.App;

public class Box2DContactListener implements ContactListener
{
    public Box2DContactListener(App app)
    {
        super();

        app.box2DWorld.setContactListener(this);
    }

    /**
     * Called when contact begins between two fixtures
     *
     * @param contact   the contact manager, which holds
     *                  details of the two fixtures involved
     */
    @Override
    public void beginContact(Contact contact)
    {
//        Fixture fixtureA = contact.getFixtureA();
//        Fixture fixtureB = contact.getFixtureB();
//
//        ((BodyIdentity) fixtureA.getBody().getUserData()).entity.startContact(fixtureA, fixtureB);
//        ((BodyIdentity) fixtureB.getBody().getUserData()).entity.startContact(fixtureB, fixtureA);
    }

    /**
     * Called when contact ends between two fixtures
     *
     * @param contact   the contact manager, which holds
     *                  details of the two fixtures involved
     */
    @Override
    public void endContact(Contact contact)
    {
//        Fixture fixtureA = contact.getFixtureA();
//        Fixture fixtureB = contact.getFixtureB();
//
//        ((BodyIdentity) fixtureA.getBody().getUserData()).entity.finishContact(fixtureB);
//        ((BodyIdentity) fixtureB.getBody().getUserData()).entity.finishContact(fixtureA);
    }

    // =================================================================================
    // Both PreSolve and PostSolve give you a b2Contact pointer, so we have
    // access to the same points and normal information as BeginContact.
    // PreSolve gives us a chance to change the characteristics of the contact
    // before the collision response is calculated, or even to cancel the
    // response altogether, and from PostSolve we can find out what the
    // collision response was.
    // =================================================================================

    @Override
    public void preSolve(Contact contact, Manifold oldManifold)
    {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse)
    {
    }
}
