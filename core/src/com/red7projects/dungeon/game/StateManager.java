
package com.red7projects.dungeon.game;

import java.util.Stack;

public class StateManager
{
    private Stack<StateID> stateStack;

    public StateManager(StateID _state)
    {
        this();

        push(_state);
    }

    public StateManager()
    {
        stateStack = new Stack<>();
    }

    public void push(StateID _state)
    {
        stateStack.push(_state);
    }

    public void pop()
    {
        stateStack.pop();
    }

    public void set(StateID _state)
    {
        if (!stateStack.isEmpty())
        {
            stateStack.pop();
        }

        stateStack.push(_state);
    }

    public StateID peek()
    {
        return stateStack.peek();
    }

    public boolean equalTo(StateID state)
    {
        return state.compareTo(peek()) == 0;
    }

    public boolean after(StateID state)
    {
        return state.compareTo(peek()) < 0;
    }
}
