package com.red7projects.dungeon.config;

import com.badlogic.gdx.utils.StringBuilder;

public class GameOption
{
    public String  prefName;
    public boolean state;
    public boolean defaultState;

    public GameOption()
    {
        this.prefName     = "";
        this.state        = false;
        this.defaultState = false;
    }

    public GameOption(String _name, boolean _value, boolean _default)
    {
        this.prefName     = _name;
        this.state        = _value;
        this.defaultState = _default;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(prefName);
        sb.append(" : ").append(state);
        sb.append(" : ").append(defaultState);

        return sb.toString();
    }
}
