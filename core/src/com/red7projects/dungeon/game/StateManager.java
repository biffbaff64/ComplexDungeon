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

public class StateManager
{
    private StateID gameState;
    private StateID previousState;
    private StateID storedState;

    public StateManager(StateID state)
    {
        this();

        set(state);
    }

    public StateManager()
    {
        gameState       = StateID._INACTIVE;
        previousState   = StateID._INACTIVE;
        storedState     = StateID._INACTIVE;
    }

    public void set(StateID state)
    {
        previousState   = gameState;
        gameState       = state;
    }

    public StateID get()
    {
        return gameState;
    }

    public StateID getPrevious()
    {
        return previousState;
    }

    public StateID getStored()
    {
        return storedState;
    }

    public void setStored(StateID _state)
    {
        storedState = _state;
    }

    public boolean hasChanged(StateID state)
    {
        return state.compareTo(gameState) != 0;
    }

    public boolean equalTo(StateID state)
    {
        return state.compareTo(gameState) == 0;
    }

    public boolean differentTo(StateID state)
    {
        return state.compareTo(gameState) != 0;
    }

    public boolean before(StateID state)
    {
        return state.compareTo(gameState) > 0;
    }

    public boolean greaterThanOrEqualTo(StateID state)
    {
        return state.compareTo(gameState) <= 0;
    }

    public boolean after(StateID state)
    {
        return state.compareTo(gameState) < 0;
    }

    public boolean isPaused()
    {
        return gameState == StateID._STATE_PAUSED;
    }
}
