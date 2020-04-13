/*
 * ***********************************************************
 *  ** Copyright ${DATE} Red7Projects.
 *  ** <p>
 *  ** Licensed under the Apache License, Version 2.0 (the "License");
 *  ** you may not use this file except in compliance with the License.
 *  ** You may obtain a copy of the License at
 *  ** <p>
 *  ** http://www.apache.org/licenses/LICENSE-2.0
 *  ** <p>
 *  ** Unless required by applicable law or agreed to in writing, software
 *  ** distributed under the License is distributed on an "AS IS" BASIS,
 *  ** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  ** See the License for the specific language governing permissions and
 *  ** limitations under the License.
 *  **
 *  ** Created by ${USER}.
 *  **********************************************************
 *
 */

package com.red7projects.dungeon.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.red7projects.dungeon.utils.logging.Meters;
import com.red7projects.dungeon.utils.logging.Stats;
import com.red7projects.dungeon.utils.logging.Trace;

public class FontUtils
{
    public BitmapFont createFont(String fontFile, int size, Color color)
    {
        BitmapFont font;

        try
        {
            font = createFont(fontFile, size);
            font.setColor(color);
        }
        catch (Exception e)
        {
            Trace.__FILE_FUNC(e.getMessage());
            Stats.incMeter(Meters._FONT_LOAD_FAILURE.get());

            font = new BitmapFont();
        }

        return font;
    }

    public BitmapFont createFont(String fontFile, int size)
    {
        BitmapFont font;

        try
        {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontFile));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

            parameter.size = size;

            font = generator.generateFont(parameter);
            font.setColor(Color.WHITE);
        }
        catch (Exception e)
        {
            Trace.__FILE_FUNC(e.getMessage());
            Stats.incMeter(Meters._FONT_LOAD_FAILURE.get());

            font = new BitmapFont();
        }

        return font;
    }

    public BitmapFont createBorderedFont(String fontFile, int size, Color colour, int borderSize, Color borderColor)
    {
        BitmapFont font;

        try
        {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontFile));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

            parameter.size = size;
            parameter.borderColor = borderColor;
            parameter.borderWidth = borderSize;

            font = generator.generateFont(parameter);
            font.setColor(colour);

            generator.dispose();
        }
        catch (Exception e)
        {
            Trace.__FILE_FUNC(e.getMessage());
            Stats.incMeter(Meters._BORDERED_FONT_LOAD_FAILURE.get());

            font = new BitmapFont();
        }

        return font;
    }
}
