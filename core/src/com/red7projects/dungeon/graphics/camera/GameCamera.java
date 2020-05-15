package com.red7projects.dungeon.graphics.camera;

import com.red7projects.dungeon.maths.SimpleVec3F;

public interface GameCamera
{
    void setPosition(SimpleVec3F _position);

    void setPosition(SimpleVec3F _position, float _zoom);

    void setPosition(SimpleVec3F _position, float _zoom, boolean _shake);

    void updatePosition();

    void lerpTo(SimpleVec3F _position, float _speed);

    void lerpTo(SimpleVec3F _position, float _speed, float _zoom, boolean _shake);

    void resizeViewport(int _width, int _height, boolean _centerCamera);

    void setCameraZoom(float _zoom);

    float getCameraZoom();

    void setZoomDefault(float _zoom);

    float getDefaultZoom();

    void reset();
}
