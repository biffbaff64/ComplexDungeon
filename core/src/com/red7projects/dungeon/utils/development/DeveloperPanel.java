/*
 *  Copyright 24/04/2018 Red7Projects.
 *  <p>
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.red7projects.dungeon.utils.development;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.red7projects.dungeon.utils.ArrayUtils;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.config.Preferences;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.FontUtils;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.utils.logging.Stats;
import com.red7projects.dungeon.utils.logging.Trace;
import com.red7projects.dungeon.ui.BasicPanel;

import java.util.Locale;

@SuppressWarnings("FieldCanBeLocal")
public class DeveloperPanel extends BasicPanel
{
    //
    // The elements below are all initialised in quickSetup()
    private int disableEnemiesColumn;
    private int disableEnemiesRow;
    private int glProfilerRow;
    private int glProfilerColumn;
    private int androidOnDesktopColumn;
    private int androidOnDesktopRow;

    private static class DMEntry
    {
        final String string;
        final String prefName;
        final boolean isEnemy;

        DMEntry(String _string, String pref, boolean _isEnemy)
        {
            this.string = _string;
            this.prefName = pref;
            this.isEnemy = _isEnemy;
        }
    }

    private static final int _TABLE_COLUMNS = 4;

    private DMEntry[][]             devMenu;
    private Texture                 foreground;
    private CheckBox[][]            buttons;
    private TextField               heading;
    private Table                   table;
    private GLProfiler              glProfiler;
    private TextButton              exitButton;
    private TextButton              buttonResetPrefs;
    private TextButton              buttonResetHiScores;
    private TextButton              buttonResetStats;
    private TextButton              buttonGLProfiler;
    private TextButton              buttonCollisionDump;


    private       int     mapXBase;
    private       int     mapYBase;
    private       boolean previousDisableEnemies;
    private       boolean previousExternalController;
    private       boolean okToResetPrefs;
    private final App     app;

    public DeveloperPanel(App _app)
    {
        super();

        this.app = _app;

        if (com.red7projects.dungeon.utils.development.Developer.isDevMode())
        {
            glProfiler = new GLProfiler(Gdx.graphics);
        }
    }

    @Override
    public void setup()
    {
        AppConfig.gamePaused = true;

        loadDevMenu();

        validateRowsAndColumns();

        foreground = app.assets.loadSingleAsset("data/night_sky.png", Texture.class);

        mapXBase = app.mapData.mapPosition.getX();
        mapYBase = app.mapData.mapPosition.getY();

        okToResetPrefs = false;

        Skin skin = new Skin(Gdx .files.internal("data/uiskin.json"));
        skin.getFont("default-font").getData().setScale(2.0f);

        table = createTable();
        createHeading(skin);
        createButtons(skin);

        populateTable(table, skin);

        // Wrap the table in a scrollpane.
        scrollPane = new ScrollPane(table, skin);
        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setWidth((float)(Gfx._VIEW_WIDTH - 100));
        scrollPane.setHeight((float)(Gfx._VIEW_HEIGHT - 200));
        scrollPane.setPosition(mapXBase + 50, mapYBase + 50);
        scrollPane.setScrollbarsOnTop(true);

        app.stage.addActor(scrollPane);
        app.stage.addActor(heading);
        app.stage.addActor(exitButton);
        app.stage.addActor(buttonResetPrefs);
        app.stage.addActor(buttonResetHiScores);
        app.stage.addActor(buttonResetStats);
        app.stage.addActor(buttonGLProfiler);
        app.stage.addActor(buttonCollisionDump);

        updatePreferencesOnEntry();

        previousDisableEnemies = buttons[disableEnemiesRow][disableEnemiesColumn].isChecked();

        if (com.red7projects.dungeon.utils.development.Developer.isDevMode())
        {
            glProfilerUpdate();
        }
    }

    private Table createTable()
    {
        Table _table = new Table();
        _table.top().left();
        _table.pad(20, 10, 10, 10);

        Texture texture    = app.assets.loadSingleAsset("data/night_sky.png", Texture.class);
        Image   background = new Image(new TextureRegion(texture));
        _table.setBackground(background.getDrawable());

        return _table;
    }

    private void createHeading(Skin _skin)
    {
        heading = new TextField("DEVELOPER OPTIONS", _skin);
        heading.setPosition
            (
                mapXBase + ((Gfx._VIEW_WIDTH - heading.getWidth()) / 2),
                mapYBase + (Gfx._VIEW_HEIGHT - 80),
                Align.center
            );

        FontUtils fontUtils = new FontUtils();
        BitmapFont font = fontUtils.createFont(GameAssets._PRO_WINDOWS_FONT, 48, Color.WHITE);
        TextField.TextFieldStyle style = heading.getStyle();
        style.font = font;

        heading.setStyle(style);
        heading.setSize(400, 96);
        heading.setDisabled(true);
    }

    private void createButtons(Skin _skin)
    {
        exitButton          = new TextButton("Back", _skin);
        buttonResetPrefs    = new TextButton("Reset Preferences To Default", _skin);
        buttonResetHiScores = new TextButton("Reset HiScore Table", _skin);
        buttonResetStats    = new TextButton("Reset Stats Meters", _skin);
        buttonGLProfiler    = new TextButton("GLProfiler Dump", _skin);
        buttonCollisionDump = new TextButton("CollisionObject Breakdown", _skin);

        int x = 20;

        buttonResetPrefs.setPosition(mapXBase + x, mapYBase + 15);

        x += buttonResetPrefs.getWidth() + 20;

        buttonResetHiScores.setPosition(mapXBase + x, mapYBase + 15);

        x += buttonResetHiScores.getWidth() + 20;

        buttonResetStats.setPosition(mapXBase + x, mapYBase + 15);

        x += buttonResetStats.getWidth() + 20;

        buttonGLProfiler.setPosition(mapXBase + x, mapYBase + 15);

        x += buttonGLProfiler.getWidth() + 20;

        buttonCollisionDump.setPosition(mapXBase + x, mapYBase + 15);

        exitButton.setPosition(mapXBase + 20, mapYBase + (Gfx._VIEW_HEIGHT - 100));
        exitButton.setSize(128, 64);

        createButtonListeners();
    }

    private void populateTable(Table _table, Skin _skin)
    {
        Label[] label = new Label[_TABLE_COLUMNS];

        buttons = new CheckBox[devMenu.length][_TABLE_COLUMNS];

        for (int row = 0; row < devMenu.length; row++)
        {
            for (int column = 0; column < _TABLE_COLUMNS; column++)
            {
                label[column] = new Label
                    (
                        (String.format(Locale.getDefault(), "%-30s", devMenu[row][column].string)).toUpperCase(),
                        _skin
                    );
                label[column].setAlignment(Align.left);

                buttons[row][column] = new CheckBox("", _skin);
                buttons[row][column].setHeight(label[column].getHeight());
                CheckBox.CheckBoxStyle style = buttons[row][column].getStyle();
                style.checkboxOn = new TextureRegionDrawable(app.assets.getButtonsAtlas().findRegion("toggle_on"));
                style.checkboxOff = new TextureRegionDrawable(app.assets.getButtonsAtlas().findRegion("toggle_off"));
                buttons[row][column].setStyle(style);
                buttons[row][column].setChecked(app.preferences.isEnabled(devMenu[row][column].prefName));
            }

            createCheckBoxListener(row);

            for (int column = 0; column < _TABLE_COLUMNS; column++)
            {
                Label num = new Label("" + row + ": ", _skin);
                _table.add(num).padLeft(20);
                _table.add(label[column]);
                _table.add(buttons[row][column]);
            }

            _table.row();
        }

        _table.setVisible(true);
    }

    public void createButtonListeners()
    {
        exitButton.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                clearUp();
            }
        });

        buttonResetPrefs.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                resetPreferencesToDefaults();
            }
        });

        buttonResetStats.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                Stats.resetAllMeters();
            }
        });

        buttonResetHiScores.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                if (com.red7projects.dungeon.utils.development.Developer.isDevMode())
                {
                    app.highScoreUtils.resetTable();

                    Trace.__FILE_FUNC("HISCORE Table reset to defaults.");
                }
            }
        });

        buttonGLProfiler.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                if (com.red7projects.dungeon.utils.development.Developer.isDevMode())
                {
                    Trace.dbg
                        (
                            "  Drawcalls: " + glProfiler.getDrawCalls()
                                + ", Calls: " + glProfiler.getCalls()
                                + ", TextureBindings: " + glProfiler.getTextureBindings()
                                + ", ShaderSwitches:  " + glProfiler.getShaderSwitches()
                                + "vertexCount: " + glProfiler.getVertexCount().value
                        );

                    glProfiler.reset();
                }
            }
        });

        buttonCollisionDump.addListener(new ClickListener()
        {
            @Override
            public void clicked(final InputEvent event, final float x, final float y)
            {
                if (com.red7projects.dungeon.utils.development.Developer.isDevMode())
                {
                    app.collisionUtils.debugAll();
                }
            }
        });
    }

    private void createCheckBoxListener(int index)
    {
        for (int column = 0; column < _TABLE_COLUMNS; column++)
        {
            buttons[index][column].addListener(new ChangeListener()
            {
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    if (!okToResetPrefs)
                    {
                        updatePreferences();
                    }
                }
            });
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        if (foreground != null)
        {
            spriteBatch.draw(foreground, 0, 0, Gfx._VIEW_WIDTH, Gfx._VIEW_HEIGHT);
        }
    }

    private void updatePreferencesOnEntry()
    {
        if (!com.red7projects.dungeon.utils.development.Developer.isDevMode())
        {
            app.preferences.prefs.putBoolean(Preferences._MENU_HEAPS, false);
            app.preferences.prefs.flush();
        }

        updatePreferences();
    }

    private void updatePreferences()
    {
        if (buttons[disableEnemiesRow][disableEnemiesColumn].isChecked() != previousDisableEnemies)
        {
            setAllEnemiesEnableStatus(buttons[disableEnemiesRow][disableEnemiesColumn].isChecked());
        }

        for (int row = 0; row < devMenu.length; row++)
        {
            for (int column = 0; column < _TABLE_COLUMNS; column++)
            {
                app.preferences.prefs.putBoolean(devMenu[row][column].prefName, buttons[row][column].isChecked());
            }
        }

        glProfilerUpdate();

        app.preferences.prefs.flush();

        previousDisableEnemies = buttons[disableEnemiesRow][disableEnemiesColumn].isChecked();
    }

    private void updatePreferencesOnExit()
    {
        AppConfig.canDrawButtonBoxes = app.preferences.isEnabled(Preferences._BUTTON_BOXES);
    }

    private void glProfilerUpdate()
    {
        if (com.red7projects.dungeon.utils.development.Developer.isDevMode())
        {
            if (buttons[glProfilerRow][glProfilerColumn].isChecked())
            {
                // Profiling should be disabled on release software, hence
                // the warning suppression. Normally, not a good idea to
                // suppress such warnings but this if..else... is only
                // executed in Developer Mode.

                //noinspection LibGDXProfilingCode
                glProfiler.enable();
            }
            else
            {
                glProfiler.disable();
            }
        }
    }

    /**
     * Either ENABLE or DISABLE all enemy entities.
     *
     * @param _disable boolean TRUE to disable
     *                 boolean FALSE to enable.
     */
    private void setAllEnemiesEnableStatus(boolean _disable)
    {
        for (int row = 0; row < devMenu.length; row++)
        {
            for (int column = 0; column < _TABLE_COLUMNS; column++)
            {
                if (devMenu[row][column].isEnemy)
                {
                    buttons[row][column].setChecked(!_disable);
                }
            }
        }
    }

    private void resetPreferencesToDefaults()
    {
        okToResetPrefs = true;

        app.preferences.setPrefsToDefault();

        app.preferences.prefs.putBoolean(Preferences._DEV_MODE, com.red7projects.dungeon.utils.development.Developer.isDevMode());
        app.preferences.prefs.putBoolean(Preferences._GOD_MODE, Developer.isGodMode());
        app.preferences.prefs.putBoolean(Preferences._SIGN_IN_STATUS, app.googleServices.isSignedIn());
        app.preferences.prefs.putBoolean(Preferences._ANDROID_ON_DESKTOP, AppConfig.isDesktopApp());

        app.preferences.prefs.flush();

        for (int row = 0; row < devMenu.length; row++)
        {
            for (int column = 0; column < _TABLE_COLUMNS; column++)
            {
                boolean isChecked = app.preferences.isEnabled(devMenu[row][column].prefName);

                buttons[row][column].setChecked(isChecked);
            }
        }

        okToResetPrefs = false;
    }

    private void validateRowsAndColumns()
    {
        for (int row = 0; row<devMenu.length; row++)
        {
            int length = devMenu[row].length;

            for (int column = 0; column < length; column++)
            {
                String prefName = devMenu[row][column].prefName;

                switch (prefName)
                {
                    case Preferences._DISABLE_ENEMIES:
                    {
                        disableEnemiesColumn = column;
                        disableEnemiesRow    = row;
                    }
                    break;

                    case Preferences._GL_PROFILER:
                    {
                        glProfilerColumn = column;
                        glProfilerRow = row;
                    }
                    break;

                    case Preferences._ANDROID_ON_DESKTOP:
                    {
                        androidOnDesktopColumn = column;
                        androidOnDesktopRow = row;
                    }
                    break;

                    default:
                        break;
                }
            }
        }
    }

    private void loadDevMenu()
    {
        DMEntry[][] devMenuDefaults =
            {
                {
                    new DMEntry("Dev. mode", Preferences._DEV_MODE, false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                    new DMEntry("Turrets", Preferences._TURRETS, true),
                },
                {
                    new DMEntry("Invincible", Preferences._GOD_MODE, false),
                    new DMEntry("", "", false),
                    new DMEntry("Force Prefs Reset", Preferences._FORCE_PREFS_RESET, false),
                    new DMEntry("Bouncer", Preferences._BOUNCER, true),
                },
                {
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                    new DMEntry("Game Trace Output", Preferences._SHOW_TRACE, false),
                    new DMEntry("Soldiers", Preferences._SOLDIER, true),
                },
                {
                    new DMEntry("Google Sign In", Preferences._PLAY_SERVICES, false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                    new DMEntry("Mystery Chest", Preferences._MYSTERY_CHEST, false),
                },
                {
                    new DMEntry("Achievements", Preferences._ACHIEVEMENTS, false),
                    new DMEntry("", "", false),
                    new DMEntry("Show Game Buttons", Preferences._SHOW_GAME_BUTTONS, false),
                    new DMEntry("Spike Ball", Preferences._SPIKE_BALL, true),
                },
                {
                    new DMEntry("Challenges", Preferences._CHALLENGES, false),
                    new DMEntry("", "", false),
                    new DMEntry("Button Outlines", Preferences._BUTTON_BOXES, false),
                    new DMEntry("Spike Block", Preferences._SPIKE_BLOCK, true),
                },
                {
                    new DMEntry("Player", Preferences._PLAYER, false),
                    new DMEntry("", "", false),
                    new DMEntry("Sprite Boxes", Preferences._SPRITE_BOXES, false),
                    new DMEntry("Jelly Monster", Preferences._JELLY_MONSTER, true),
                },
                {
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                    new DMEntry("Tile Boxes", Preferences._TILE_BOXES, false),
                    new DMEntry("Scorpion", Preferences._SCORPION, true),
                },
                {
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                    new DMEntry("Marker Tiles", Preferences._SPAWNPOINTS, false),
                    new DMEntry("Flame Thrower", Preferences._FLAME_THROWER, true),
                },
                {
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                    new DMEntry("Map Window", Preferences._MAP_WINDOW, false),
                    new DMEntry("Decorations", Preferences._DECORATIONS, false),
                },
                {
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                    new DMEntry("LJM View Window", Preferences._LJM_VIEW_WINDOW, false),
                    new DMEntry("Villagers", Preferences._VILLAGER, false),
                },
                {
                    new DMEntry("Box2D Physics", Preferences._BOX2D_PHYSICS, false),
                    new DMEntry("", "", false),
                    new DMEntry("Anim Tiles", Preferences._ANIM_TILES, false),
                    new DMEntry("Pickups", Preferences._PICKUPS, false),
                },
                {
                    new DMEntry("B2D Renderer", Preferences._B2D_RENDERER, false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                    new DMEntry("Prisoners", Preferences._PRISONER, false),
                },
                {
                    new DMEntry("Use Ashley ECS", Preferences._USING_ASHLEY_ECS, false),
                    new DMEntry("", "", false),
                    new DMEntry("Demo Scroll", Preferences._SCROLL_DEMO, false),
                    new DMEntry("Floating Platform", Preferences._FLOATING_PLATFORM, false),
                },
                {
                    new DMEntry("Shader Program", Preferences._SHADER_PROGRAM, false),
                    new DMEntry("", "", false),
                    new DMEntry("Cull Sprites", Preferences._CULL_SPRITES, false),
                    new DMEntry("Laser Doors", Preferences._LASER_DOOR, false),
                },
                {
                    new DMEntry("GLProfiler", Preferences._GL_PROFILER, false),
                    new DMEntry("", "", false),
                    new DMEntry("Progress Bars", Preferences._PROGRESS_BARS, false),
                    new DMEntry("Doors", Preferences._DOORS, false),
                },
                {
                    new DMEntry("Android on Desktop", Preferences._ANDROID_ON_DESKTOP, false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                    new DMEntry("Storm Demon", Preferences._STORM_DEMON, true),
                },
                {
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                    new DMEntry("Disable Menu Screen", Preferences._DISABLE_MENU_SCREEN, false),
                    new DMEntry("Beetles", Preferences._BEETLE, true),
                },
                {
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                    new DMEntry("Disable Enemies", Preferences._DISABLE_ENEMIES, false),
                    new DMEntry("", "", false),
                },
                {
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                    new DMEntry("Menu Page Heaps", Preferences._MENU_HEAPS, false),
                    new DMEntry("", "", false),
                },
            };

        devMenu = new DMEntry[devMenuDefaults.length][_TABLE_COLUMNS];

        devMenu = ArrayUtils.deepCopyOf(devMenuDefaults);
    }

    public void debugReport()
    {
        Trace.__FILE_FUNC_WithDivider();

        for (DMEntry[] entry : devMenu)
        {
            for (DMEntry dmEntry : entry)
            {
                if (!dmEntry.string.isEmpty())
                {
                    Trace.dbg(dmEntry.string + ": " + app.preferences.prefs.getBoolean(dmEntry.prefName));
                }
            }
        }

        Trace.finishedMessage();
    }

    private void clearActors()
    {
        Trace.__FILE_FUNC();

        table.addAction(Actions.removeActor());
        scrollPane.addAction(Actions.removeActor());
        exitButton.addAction(Actions.removeActor());
        buttonResetHiScores.addAction(Actions.removeActor());
        buttonResetPrefs.addAction(Actions.removeActor());
        buttonGLProfiler.addAction(Actions.removeActor());
        buttonResetStats.addAction(Actions.removeActor());
        buttonCollisionDump.addAction(Actions.removeActor());
        heading.addAction(Actions.removeActor());

        for (int row = 0; row < devMenu.length; row++)
        {
            for (int column = 0; column < _TABLE_COLUMNS; column++)
            {
                buttons[row][column].addAction(Actions.removeActor());
            }
        }

        Trace.finishedMessage();
    }

    private void clearUp()
    {
        Trace.__FILE_FUNC();

        updatePreferencesOnExit();

        AppConfig.developerPanelActive = false;
        AppConfig.gamePaused           = false;

        clearActors();

        Trace.finishedMessage();
    }

    @Override
    public void dispose()
    {
        super.dispose();

        foreground.dispose();
        exitButton.clear();
        buttonResetPrefs.clear();
        buttonResetHiScores.clear();
        buttonResetStats.clear();
        buttonGLProfiler.clear();
        heading.clear();
        table.clear();
        scrollPane.clear();

        foreground = null;
        exitButton = null;
        buttonResetPrefs = null;
        buttonResetHiScores = null;
        buttonResetStats = null;
        buttonGLProfiler = null;
        heading = null;
        table = null;
        scrollPane = null;
    }
}
