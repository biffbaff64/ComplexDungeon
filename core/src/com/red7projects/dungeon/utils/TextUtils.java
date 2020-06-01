package com.red7projects.dungeon.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.game.App;

public class TextUtils
{
    private BitmapFont font;
    private App        app;

    public TextUtils(App _app)
    {
        this.app = _app;
    }

    public void setFont(String _fontAsset, int _size, Color _colour)
    {
        FontUtils fontUtils = new FontUtils();
        font = fontUtils.createFont(_fontAsset, _size, _colour);
    }

    public void drawText(String _message, float _x, float _y)
    {
        if (font == null)
        {
            setFont(GameAssets._PRO_WINDOWS_FONT, 20, Color.WHITE);
        }

        font.draw(app.spriteBatch, _message, _x, _y);
    }
}
