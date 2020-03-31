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
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;

public class OrthoGameCamera implements GameCamera, Disposable
{
    public Viewport             viewport;
    public OrthographicCamera   camera;
    public String               name;
    public boolean              isInUse;

    private float     defaultZoom;
    private final App app;

    public OrthoGameCamera(float _sceneWidth, float _sceneHeight, String _name, App _app)
    {
        this.name    = _name;
        this.app     = _app;
        this.isInUse = false;

        camera = new OrthographicCamera(_sceneWidth, _sceneHeight);
        camera.position.set(_sceneWidth / 2, _sceneHeight / 2, 0);

        viewport = new StretchViewport(_sceneWidth * Gfx._PPM, _sceneHeight * Gfx._PPM, camera);
        viewport.apply();
    }

    @Override
    public void setPosition(float _x, float _y, float _z)
    {
        if (isInUse)
        {
            camera.position.x = _x;
            camera.position.y = _y;
            camera.position.z = _z;
            camera.update();

            app.spriteBatch.setProjectionMatrix(camera.combined);
        }
    }

    @Override
    public void setPosition(float _x, float _y, float _z, float _zoom)
    {
        if (isInUse)
        {
            camera.position.x = _x;
            camera.position.y = _y;
            camera.position.z = _z;
            camera.zoom += _zoom;
            camera.update();

            app.spriteBatch.setProjectionMatrix(camera.combined);
        }
    }

    @Override
    public void setPosition(float _x, float _y, float _z, float _zoom, boolean _shake)
    {
        if (isInUse)
        {
            camera.position.x = _x;
            camera.position.y = _y;
            camera.position.z = _z;
            camera.zoom += _zoom;

            if (_shake)
            {
                Shake.update(Gdx.graphics.getDeltaTime(), camera, app);
            }

            camera.update();

            app.spriteBatch.setProjectionMatrix(camera.combined);
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
        camera   = null;
        viewport = null;
        name     = null;
    }
}
