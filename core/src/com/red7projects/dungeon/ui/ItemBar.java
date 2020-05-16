package com.red7projects.dungeon.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.GfxUtils;

public class ItemBar
{
    private static final int _X1            = 0;
    private static final int _X2            = 1;
    private static final int _Y             = 2;
    private static final int _WIDTH         = 3;
    private static final int _HEIGHT        = 4;

    private static final int _RUNES_PANEL   = 0;
    private static final int _BOOKS_PANEL   = 1;
    private static final int _POTIONS_PANEL = 2;

    private static final int _COLLECT_PANEL = 0;
    private static final int _ITEMS_INDEX   = 1;
    private static final int _ITEM1         = 1;
    private static final int _ITEM2         = 2;
    private static final int _ITEM3         = 3;
    private static final int _ITEM4         = 4;
    private static final int _ITEM5         = 5;
    private static final int _ITEM6         = 6;
    private static final int _ITEM7         = 7;
    private static final int _ITEM8         = 8;

    private static final int[][] displayPos = new int[][]
        {
            //
            { 749,  749,   34,    0,    0},  // Collection Panel

            //
            //
            { 772,  772,   76,   96,   96},
            { 904,  904,   76,   96,   96},
            {1036, 1036,   76,   96,   96},
            {1164, 1164,   76,   96,   96},
            {1298, 1298,   76,   96,   96},
            {1430, 1430,   76,   96,   96},
            {1562, 1562,   76,   96,   96},
            {1695, 1695,   76,   96,   96},
        };

    private TextureRegion[]     objectivesPanel;
    private TextureRegion[][]   itemTextures;
    private TextureRegion[][]   itemGreyTextures;

    private final App app;

    public ItemBar(App _app)
    {
        this.app = _app;

        create();
    }

    public void update()
    {
    }

    public void draw(float originX, float originY)
    {
        if (app.mainGameScreen.gameControlLoop.messagePanel == null)
        {
            app.spriteBatch.draw
                (
                    objectivesPanel[app.getHud().objectivesPanelIndex],
                    originX + displayPos[_COLLECT_PANEL][_X1],
                    originY + displayPos[_COLLECT_PANEL][_Y]
                );

            drawItems(originX, originY);
        }
    }

    private void drawItems(float originX, float originY)
    {
        TextureRegion textureRegion;

        for (int i = 0; i < 8; i++)
        {
            if (app.gameProgress.collectItems[app.getHud().objectivesPanelIndex][i])
            {
                textureRegion = itemTextures[app.getHud().objectivesPanelIndex][i];
            }
            else
            {
                textureRegion = itemGreyTextures[app.getHud().objectivesPanelIndex][i];
            }

            app.spriteBatch.draw
                (
                    textureRegion,
                    originX + displayPos[_ITEMS_INDEX + i][_X1],
                    originY + displayPos[_ITEMS_INDEX + i][_Y],
                    textureRegion.getRegionWidth() * 1.5f,
                    textureRegion.getRegionHeight() * 1.5f
                );
        }
    }

    private void create()
    {
        itemTextures    = new TextureRegion[3][8];
        itemTextures[0] = new TextureRegion[8];
        itemTextures[1] = new TextureRegion[8];
        itemTextures[2] = new TextureRegion[8];

        GfxUtils.splitRegion
            (
                app.assets.animationRegion(GameAssets._RUNES_ASSET),
                GameAssets._RUNES_FRAMES,
                itemTextures[_RUNES_PANEL],
                app
            );

        GfxUtils.splitRegion
            (
                app.assets.animationRegion(GameAssets._BOOKS_ASSET),
                GameAssets._BOOKS_FRAMES,
                itemTextures[_BOOKS_PANEL],
                app
            );

        GfxUtils.splitRegion
            (
                app.assets.animationRegion(GameAssets._POTIONS_ASSET),
                GameAssets._POTIONS_FRAMES,
                itemTextures[_POTIONS_PANEL],
                app
            );

        itemGreyTextures    = new TextureRegion[3][8];
        itemGreyTextures[0] = new TextureRegion[8];
        itemGreyTextures[1] = new TextureRegion[8];
        itemGreyTextures[2] = new TextureRegion[8];

        GfxUtils.splitRegion
            (
                app.assets.animationRegion(GameAssets._GREY_RUNES_ASSET),
                GameAssets._RUNES_FRAMES,
                itemGreyTextures[_RUNES_PANEL],
                app
            );

        GfxUtils.splitRegion
            (
                app.assets.animationRegion(GameAssets._GREY_BOOKS_ASSET),
                GameAssets._BOOKS_FRAMES,
                itemGreyTextures[_BOOKS_PANEL],
                app
            );

        GfxUtils.splitRegion
            (
                app.assets.animationRegion(GameAssets._GREY_POTIONS_ASSET),
                GameAssets._POTIONS_FRAMES,
                itemGreyTextures[_POTIONS_PANEL],
                app
            );

        objectivesPanel    = new TextureRegion[3];
        objectivesPanel[0] = app.assets.objectRegion("runes_panel");
        objectivesPanel[1] = app.assets.objectRegion("books_panel");
        objectivesPanel[2] = app.assets.objectRegion("potions_panel");
    }
}
