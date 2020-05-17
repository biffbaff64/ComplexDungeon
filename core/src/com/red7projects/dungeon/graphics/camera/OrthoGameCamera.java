/*
 *  Copyright 09/05/2018 Red7Projects.
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

package com.red7projects.dungeon.graphics.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.maths.SimpleVec3F;
import com.red7projects.dungeon.utils.logging.Trace;

public class OrthoGameCamera implements GameCamera, Disposable
{
    public Viewport           viewport;
    public OrthographicCamera camera;
    public String             name;
    public boolean            isInUse;
    public boolean            isLerpingEnabled;

    private       float   defaultZoom;
    public        Vector3 lerpVector;
    private final App     app;

    public OrthoGameCamera(float _sceneWidth, float _sceneHeight, String _name, App _app)
    {
        Trace.__FILE_FUNC(_name);

        this.name             = _name;
        this.app              = _app;
        this.isInUse          = false;
        this.isLerpingEnabled = false;

        lerpVector = new Vector3();

        camera = new OrthographicCamera(_sceneWidth, _sceneHeight);
        camera.position.set(_sceneWidth / 2, _sceneHeight / 2, 0);
    }

    public void setStretchViewport()
    {
        viewport = new StretchViewport(camera.viewportWidth * Gfx._PPM, camera.viewportHeight * Gfx._PPM, camera);
        viewport.apply();
    }

    public void setFitViewport()
    {
        viewport = new FitViewport(camera.viewportWidth * Gfx._PPM, camera.viewportHeight * Gfx._PPM, camera);
        viewport.apply();
    }

    @Override
    public void setPosition(SimpleVec3F _position)
    {
        if (isInUse)
        {
            camera.position.x = _position.x;
            camera.position.y = _position.y;
            camera.position.z = _position.z;

            camera.update();
        }
    }

    @Override
    public void setPosition(SimpleVec3F _position, float _zoom)
    {
        if (isInUse)
        {
            camera.position.x = _position.x;
            camera.position.y = _position.y;
            camera.position.z = _position.z;
            camera.zoom += _zoom;

            camera.update();
        }
    }

    @Override
    public void setPosition(SimpleVec3F _position, float _zoom, boolean _shake)
    {
        if (isInUse)
        {
            camera.position.x = _position.x;
            camera.position.y = _position.y;
            camera.position.z = _position.z;
            camera.zoom += _zoom;

            if (_shake)
            {
                Shake.update(Gdx.graphics.getDeltaTime(), camera, app);
            }

            camera.update();
        }
    }

    @Override
    public Vector3 getPosition()
    {
        return camera.position;
    }

    @Override
    public void updatePosition()
    {
        if (isInUse)
        {
            Vector3 position = camera.position;

            // a = current camera position
            // b = target
            // a = (b - a) * lerp

            position.x = camera.position.x + (app.getPlayer().getPosition().x - camera.position.x) * 0.1f;
            position.y = camera.position.y + (app.getPlayer().getPosition().y - camera.position.y) * 0.1f;

            camera.position.set(position);
            camera.update();
        }
    }

    @Override
    public void lerpTo(SimpleVec3F _position, float _speed)
    {
        if (isInUse && isLerpingEnabled)
        {
            lerpVector.set(_position.x, _position.y, _position.z);

            camera.position.lerp(lerpVector, _speed);
            camera.update();
        }
    }

    @Override
    public void lerpTo(SimpleVec3F _position, float _speed, float _zoom, boolean _shake)
    {
        if (isInUse && isLerpingEnabled)
        {
            lerpVector.set(_position.x, _position.y, _position.z);

            camera.position.lerp(lerpVector, _speed);
            camera.zoom += _zoom;

            if (_shake)
            {
                Shake.update(Gdx.graphics.getDeltaTime(), camera, app);
            }

            camera.update();
        }
    }

    @Override
    public void resizeViewport(int _width, int _height, boolean _centerCamera)
    {
        viewport.update(_width, _height, _centerCamera);
        camera.update();
    }

    @Override
    public void setCameraZoom(float _zoom)
    {
        camera.zoom = _zoom;
    }

    @Override
    public float getCameraZoom()
    {
        return camera.zoom;
    }

    @Override
    public void setZoomDefault(float _zoom)
    {
        camera.zoom = _zoom;
        defaultZoom = _zoom;
    }

    @Override
    public float getDefaultZoom()
    {
        return defaultZoom;
    }

    @Override
    public void reset()
    {
        camera.zoom = Gfx._DEFAULT_ZOOM;
        camera.position.set(0, 0, 0);
        camera.update();
    }

    @Override
    public void dispose()
    {
        camera     = null;
        viewport   = null;
        name       = null;
        lerpVector = null;
    }
}
