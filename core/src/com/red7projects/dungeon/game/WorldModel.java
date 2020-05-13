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

package com.red7projects.dungeon.game;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.config.Settings;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.physics.box2d.BodyBuilder;
import com.red7projects.dungeon.physics.box2d.Box2DContactListener;
import com.red7projects.dungeon.physics.box2d.Box2DEntityHelper;
import com.red7projects.dungeon.utils.development.Developer;

@SuppressWarnings("WeakerAccess")
//@formatter:off
public class WorldModel
{
    public World                box2DWorld;
    public Box2DDebugRenderer   b2dr;
    public Box2DContactListener box2DContactListener;
    public Box2DEntityHelper    box2DEntityHelper;
    public BodyBuilder          bodyBuilder;
    public Matrix4              debugMatrix;

    private final App app;

    public WorldModel(App _app)
    {
        this.app = _app;

        box2DWorld = new World
            (
                new Vector2
                    (
                        (Gfx._WORLD_GRAVITY.x * Gfx._PPM),
                        (Gfx._WORLD_GRAVITY.y * Gfx._PPM)
                    ),
                false
            );

        b2dr = new Box2DDebugRenderer
            (
                true,
                true,
                true,
                true,
                false,
                false
            );

        box2DEntityHelper    = new Box2DEntityHelper();
        bodyBuilder          = new BodyBuilder(app);
        box2DContactListener = new Box2DContactListener(app);

        box2DWorld.setContactListener(box2DContactListener);

        setDebugMatrix();
    }

    public void setDebugMatrix()
    {
        if (Developer.isDevMode() && app.settings.isEnabled(Settings._B2D_RENDERER))
        {
            debugMatrix = app.spriteBatch.getProjectionMatrix().cpy().scale(Gfx._PPM, Gfx._PPM, 0);
        }
    }

    public void drawDebugMatrix()
    {
        if ((b2dr != null)
            && (box2DWorld != null)
            && (debugMatrix != null)
            && Developer.isDevMode()
            && AppConfig.isUsingBOX2DPhysics)
        {
            b2dr.render(box2DWorld, debugMatrix);
        }
    }

    public void worldStep()
    {
        box2DWorld.step
            (
                Gfx._STEP_TIME,
                Gfx._VELOCITY_ITERATIONS,
                Gfx._POSITION_ITERATIONS
            );
    }
}
