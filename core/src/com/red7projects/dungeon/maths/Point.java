package com.red7projects.dungeon.maths;

public enum Point
{
    _UP(0),
    _DOWN(1),
    _LEFT(2),
    _RIGHT(3),
    _CENTRE(4);

    public int value;

    Point(int _value)
    {
        this.value = _value;
    }
}
