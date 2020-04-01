package com.red7projects.dungeon.graphics.camera;

public interface GameCamera
{
    void setPosition(float _x, float _y, float _z);

    void setPosition(float _x, float _y, float _z, float _zoom);

    void setPosition(float _x, float _y, float _z, float _zoom, boolean _shake);

    void resizeViewport(int _width, int _height, boolean _centerCamera);

    void lerpTo(float _x, float _y, float _z, float _speed);

    void lerpTo(float _x, float _y, float _z, float _speed, float _zoom, boolean _shake);

    void setCameraZoom(float _zoom);

    float getCameraZoom();

    void setZoomDefault(float _zoom);

    float getDefaultZoom();

    void reset();
}
