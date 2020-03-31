/*
 *  Copyright 19/09/2018 Red7Projects.
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

package com.red7projects.dungeon.input.buttons;

import com.red7projects.dungeon.maths.Box;

interface GDXButton
{
    void setPosition(int x, int y);

    boolean contains(int x, int y);

    boolean contains(float x, float y);

    Box getBounds();

    void setID(ButtonID _id);

    ButtonID getID();

    void refreshBounds();
    
    void press();

    void press(int pointer);

    void pressConditional(boolean condition);

    void release();

    void set();

    void clear();

    void toggleActive();

    void togglePressed();
}
