package com.red7projects.dungeon.config;

import com.badlogic.gdx.utils.StringBuilder;

public class GameOption
{
    public String prefName;
    public String value;
    public String defaultValue;

    public GameOption()
    {
        prefName = "";
        value = "";
        defaultValue = "";
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(prefName);
        sb.append(" : ").append(value);
        sb.append(" : ").append(defaultValue);

        return sb.toString();
    }
}
