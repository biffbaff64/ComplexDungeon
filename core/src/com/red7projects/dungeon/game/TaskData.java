package com.red7projects.dungeon.game;

public class TaskData
{
    public TaskType _taskType;
    public String   _description;

    public TaskData(TaskType _type, String _text)
    {
        this._taskType    = _type;
        this._description = _text;
    }
}
