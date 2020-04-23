/*
 *
 *  * *****************************************************************************
 *  *  Copyright 27/03/2017 See AUTHORS file.
 *  *  <p>
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *  <p>
 *  *  http://www.apache.org/licenses/LICENSE-2.0
 *  *  <p>
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *  * ***************************************************************************
 *
 *
 */

package com.red7projects.dungeon.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.camera.Zoom;
import com.red7projects.dungeon.maths.SimpleVec2;
import com.red7projects.dungeon.maths.SimpleVec2F;
import com.red7projects.dungeon.physics.Direction;
import com.red7projects.dungeon.physics.Speed;
import com.red7projects.dungeon.utils.logging.StopWatch;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * A Stationary panel which zooms in and out
 * <p>
 * Extends {@link BasicPanel}
 */
public class ZoomPanel extends BasicPanel implements UserInterfacePanel
{
    private final static int    _DEFAULT_PAUSE_TIME = 1500;
    private final static float  _DEFAULT_SPEED      = 0.04f;
    private final static float  _DEFAULT_SPEED_INC  = 0.01f;
    private final static float  _MAXIMUM_ZOOM       = 8.0f;
    private final static float  _MINIMUM_ZOOM       = 0.1f;
    private final static float  _PAUSED_ZOOM        = 0.75f;

    private StopWatch    stopWatch;
    private float        zoomSpeed;
    private boolean      isFinished;
    private boolean      canPause;       // enable / disable pausing
    private int          pauseTime;      // How long to pause for. Default is 1500ms.
    private boolean      bounceBack;     // bounces back out of view after pausing
    private Zoom         zoom;

    public ZoomPanel()
    {
        super();
    }

    @Override
    public void initialise(final TextureRegion _region, final String _nameID, @NotNull final Object ... args)
    {
        textureRegion   = _region;
        nameID          = _nameID;
        canPause        = Boolean.parseBoolean(args[0].toString());
        pauseTime       = _DEFAULT_PAUSE_TIME;
        bounceBack      = Boolean.parseBoolean(args[1].toString());
        isFinished      = false;
        stopWatch       = StopWatch.start();
        zoom            = new Zoom();
        zoomSpeed       = _DEFAULT_SPEED;

        state.set(StateID._STATE_ZOOM_IN);
        
        position.x = (float) ((Gfx._VIEW_WIDTH - textureRegion.getRegionWidth()) / 2);
        position.y = (float) ((Gfx._VIEW_HEIGHT - textureRegion.getRegionHeight()) / 2);
    }

    @Override
    public void set(SimpleVec2F xy, SimpleVec2F distance, Direction direction, Speed speed) {}

    @Override
    public boolean update()
    {
        if (!isFinished)
        {
            switch (state.get())
            {
                //
                // Zoom the panel into view
                case _STATE_ZOOM_IN:
                {
                    zoom.in(zoomSpeed);

                    if (zoom.getZoomValue() > _PAUSED_ZOOM)
                    {
                        if (canPause)
                        {
                            state.set(StateID._STATE_PAUSED);
                            stopWatch.reset();
                        }
                        else
                        {
                            if (bounceBack)
                            {
                                state.set(StateID._STATE_ZOOM_OUT);
                            }
                            else
                            {
                                state.set(StateID._STATE_CLOSING);
                            }
                        }
                    }

                    zoomSpeed += _DEFAULT_SPEED_INC;
                }
                break;

                case _STATE_PANEL_UPDATE:
                {
                    //
                    // Do nothing while until the caller changes
                    // the state.
                }
                break;

                //
                // Pause for a short while before continuing
                case _STATE_PAUSED:
                {
                    if (stopWatch.time(TimeUnit.MILLISECONDS) >= pauseTime)
                    {
                        if (bounceBack)
                        {
                            state.set(StateID._STATE_ZOOM_OUT);
                        }
                        else
                        {
                            state.set(StateID._STATE_CLOSING);
                        }

                        zoomSpeed = _DEFAULT_SPEED;
                    }
                }
                break;

                //
                // Shrink the panel back down to its original size
                case _STATE_ZOOM_OUT:
                {
                    zoom.out(zoomSpeed);

                    if (zoom.getZoomValue() <= _MINIMUM_ZOOM)
                    {
                        dispose();

                        isFinished = true;
                    }

                    zoomSpeed += _DEFAULT_SPEED_INC;
                }
                break;

                //
                // Alternative ending, panel continues zooming in
                // to fill the screen before closing.
                case _STATE_CLOSING:
                {
                    zoom.in(zoomSpeed);

                    if (zoom.getZoomValue() > _MAXIMUM_ZOOM)
                    {
                        dispose();

                        isFinished = true;
                    }

                    zoomSpeed += _DEFAULT_SPEED_INC;
                }
                break;

                default:
                    break;
            }
        }

        return isFinished;
    }

    @Override
    public void draw(final App _app)
    {
        if (!isFinished)
        {
            _app.spriteBatch.draw
                (
                    textureRegion,
                    (_app.mapData.mapPosition.getX() + position.getX()),
                    (_app.mapData.mapPosition.getY() + position.getY()),
                    (float) textureRegion.getRegionWidth() / 2,
                    (float) textureRegion.getRegionHeight() / 2,
                    textureRegion.getRegionWidth(),
                    textureRegion.getRegionHeight(),
                    zoom.getZoomValue(),
                    zoom.getZoomValue(),
                    0.0f
                );
        }
    }

    @Override
    public void forceZoomOut()
    {
        zoomSpeed = _DEFAULT_SPEED;

        setState(StateID._STATE_ZOOM_OUT);
    }

    @Override
    public SimpleVec2 getSize()
    {
        return size;
    }

    @Override
    public SimpleVec2F getPosition()
    {
        return position;
    }

    @Override
    public void setPosition(final float x, final float y)
    {
        position.set(x, y);
    }

    @Override
    public void setPauseTime(final int _time)
    {
        pauseTime = _time;
    }

    public StateID getState()
    {
        return state.get();
    }

    public void setState(final StateID newState)
    {
        this.state.set(newState);
    }
}
