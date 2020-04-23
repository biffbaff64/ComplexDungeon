/*
 *  Copyright 31/01/2019 Red7Projects.
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
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.maths.SimpleVec2;
import com.red7projects.dungeon.maths.SimpleVec2F;
import com.red7projects.dungeon.physics.Direction;
import com.red7projects.dungeon.physics.Speed;

public class SlidePanel extends BasicPanel implements UserInterfacePanel
{
    public  Actions    action;
    private boolean    isInPlace;

    public SlidePanel()
    {
        super();
    }

    @Override
    public void initialise(final TextureRegion _region, final String _nameID, final Object... args)
    {
        this.textureRegion = _region;
        this.nameID        = _nameID;

        this.isActive  = false;
        this.isInPlace = false;
        this.action    = Actions._NO_ACTION;

        this.size = new SimpleVec2(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());

        state.set(StateID._STATE_OPENING);
    }

    @Override
    public void set(SimpleVec2F xy, SimpleVec2F distance, Direction direction, Speed speed)
    {
        setPosition(xy.x, xy.y);

        this.distance.set(distance);
        this.distanceReset.set(distance);
        this.direction.set(direction);
        this.speed.set(speed);
    }

    public boolean update()
    {
        if (isActive)
        {
            switch (state.get())
            {
                case _STATE_OPENING:
                {
                    if (move())
                    {
                        state.set(StateID._UPDATE);
                    }
                }
                break;

                case _STATE_CLOSING:
                {
                    if (move())
                    {
                        deactivate();

                        state.set(StateID._STATE_CLOSED);
                    }
                }
                break;

                case _UPDATE:
                case _STATE_CLOSED:
                default:
                {
                }
                break;
            }
        }

        return !isActive;
    }

    @Override
    public void draw(final App _app)
    {
        _app.spriteBatch.draw
            (
                textureRegion,
                (_app.mapData.mapPosition.getX() + position.x),
                (_app.mapData.mapPosition.getY() + position.y)
            );
    }

    @Override
    public void setPauseTime(final int _time)
    {
    }

    @Override
    public void forceZoomOut()
    {
    }

    private boolean updateReveal()
    {
        if (move())
        {
            isInPlace = true;
            deactivate();

            return true;
        }

        return false;
    }

    private boolean updateHide()
    {
        if (move())
        {
            isInPlace = false;
            deactivate();

            return true;
        }

        return false;
    }

    private boolean move()
    {
        if (distance.x > 0)
        {
            setPosition((int) (this.position.x + (speed.getX() * direction.getX())), this.position.y);
            distance.subX((int) Math.min(distance.getX(), speed.getX()));
        }

        if (distance.getY() > 0)
        {
            setPosition(this.position.x, (int) (this.position.y + (speed.getY() * direction.getY())));
            distance.subY((int) Math.min(distance.getY(), speed.getY()));
        }

        return distance.isEmpty();
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

    public StateID getState()
    {
        return state.get();
    }

    public void setState(final StateID newState)
    {
        this.state.set(newState);
    }
}
