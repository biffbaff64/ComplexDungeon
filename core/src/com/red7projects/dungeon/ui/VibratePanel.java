/*
 *  Copyright 04/02/2019 Red7Projects.
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

package com.red7projects.dungeon.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.game.StateManager;
import com.red7projects.dungeon.maths.SimpleVec2;
import com.red7projects.dungeon.maths.SimpleVec2F;
import com.red7projects.dungeon.physics.Direction;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.physics.Speed;

@SuppressWarnings("unused")
public class VibratePanel extends BasicPanel implements UserInterfacePanel
{
    private StateManager state;

    private boolean isFinished;

    public VibratePanel(final App _app)
    {
        super();
    }

    @Override
    public void initialise(final TextureRegion _region, final String _nameID, final Object ... args)
    {
        textureRegion   = _region;
        nameID          = _nameID;

        state           = new StateManager();
        isFinished      = false;
        distanceReset   = distance;
        distance.set(0, (int) args[0]);
        speed.set(0, (float) args[1]);
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
                case _STATE_SETUP:
                {
                    distance = distanceReset;
                    direction.setY(Movement._DIRECTION_DOWN);

                    state.set(StateID._STATE_PANEL_UPDATE);
                }
                break;

                case _STATE_PANEL_UPDATE:
                {
                    if (distance.y <= 0)
                    {
                        direction.setY(direction.getY() * -1);
                        distance.set(0, (distanceReset.y * 2));
                    }

                    position.y += (speed.getY() * direction.getY());
                }
                break;

                default:
                {
                }
                break;
            }
        }

        return false;
    }

    @Override
    public void draw(final App _app)
    {
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
    }

    @Override
    public void forceZoomOut()
    {
    }
}
