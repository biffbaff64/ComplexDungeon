package com.red7projects.dungeon.ui;
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

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.maths.SimpleVec2;
import com.red7projects.dungeon.maths.SimpleVec2F;
import com.red7projects.dungeon.physics.Direction;
import com.red7projects.dungeon.physics.Speed;

public interface UserInterfacePanel
{
    void initialise(TextureRegion _region, String _nameID, Object... args);

    void set(SimpleVec2F xy, SimpleVec2F distance, Direction direction, Speed speed);

    boolean update();

    void draw(App _app);

    SimpleVec2 getSize();

    SimpleVec2F getPosition();

    void setPosition(float x, float y);

    void setPauseTime(int _time);

    void forceZoomOut();

    StateID getState();

    void setState(StateID _state);

    boolean nameExists(String _nameID);

    String getNameID();

    void dispose();
}
