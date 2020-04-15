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

package com.red7projects.dungeon.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.maths.SimpleVec2;

public interface BasicUIPanel
{
    void open();

    void close();

    void setup();

    void updatePanel();

    void draw(SpriteBatch spriteBatch);

    void populateTable();

    void createButtonListeners();

    void activate();

    void deactivate();

    void setXOffset(int _offset);

    void setYOffset(int _offset);

    boolean nameExists(String _nameID);

    String getNameID();

    StateID getState();

    void setState(StateID _state);
}
