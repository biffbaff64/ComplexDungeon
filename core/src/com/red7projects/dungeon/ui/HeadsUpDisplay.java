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

package com.red7projects.dungeon.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.StringBuilder;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.config.Preferences;
import com.red7projects.dungeon.development.DebugRenderer;
import com.red7projects.dungeon.development.Developer;
import com.red7projects.dungeon.development.DeveloperPanel;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.Constants;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.graphics.FontUtils;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GfxUtils;
import com.red7projects.dungeon.input.VirtualJoystick;
import com.red7projects.dungeon.input.buttons.ButtonID;
import com.red7projects.dungeon.input.buttons.GameButton;
import com.red7projects.dungeon.input.buttons.Switch;
import com.red7projects.dungeon.logging.Trace;
import com.red7projects.dungeon.map.Room;

import java.util.Locale;

// TODO: 24/10/2018 - This class is too big, trim it down.

public class HeadsUpDisplay implements Disposable
{
    private static final int _X1     = 0;
    private static final int _X2     = 1;
    private static final int _Y      = 2;
    private static final int _WIDTH  = 3;
    private static final int _HEIGHT = 4;
    private static final int _X_DIR  = 3;
    private static final int _Y_DIR  = 4;

    private static final int _JOYSTICK    = 0;
    private static final int _BUTTON_X    = 1;
    private static final int _BUTTON_Y    = 2;
    private static final int _ATTACK      = 3;    // BUTTON_B
    private static final int _ACTION      = 4;    // BUTTON_A
    private static final int _PAUSE       = 5;
    private static final int _DEV_OPTIONS = 6;
    private static final int _HUD_MENU    = 7;
    private static final int _COINS       = 8;
    private static final int _GEMS        = 9;
    private static final int _LIVES       = 10;
    private static final int _HEALTH      = 11;
    private static final int _VILLAGERS   = 12;
    private static final int _HUD_KEY     = 13;
    private static final int _COMPASS     = 14;

    private static final int[][] displayPos = new int[][]
        {
            {40, 1640, 40, 240, 240},             // Joystick

            {2138, 44, 161, 96, 96},             // X
            {2256, 44, 273, 96, 96},             // Y
            {2376, 44, 161, 96, 96},             // Attack
            {2256, 158, 48, 96, 96},             // Action

            {2387, 2387, 1264, 66, 66},             // Pause Button
            {2486, 2486, 1134, 66, 66},             // Dev Options
            {2486, 2486, 1134, 66, 66},             // HUD Menu

            //
            // Y is distance from the TOP of the screen
            {1455, 1455, 40, 0, 0},             // Coins total
            {1455, 1455, 150, 0, 0},             // Gems total
            {1920, 1920, 72, 0, 0},             // Life bar
            {1920, 1920, 186, 0, 0},             // Health bar

            //
            // Y is distance from the TOP of the screen
            {382, 382, 96, 0, 0},             // Villagers
            {668, 668, 191, 0, 0},             // Hud Key
            {882, 882, 240, 0, 0},             // Compass
        };

    public Switch         buttonUp;
    public Switch         buttonDown;
    public Switch         buttonLeft;
    public Switch         buttonRight;
    public GameButton     buttonA;
    public GameButton     buttonB;
    public GameButton     buttonX;
    public GameButton     buttonY;
    public GameButton     buttonPause;
    public GameButton     buttonDevOptions;
    public GameButton     buttonHUDMenu;
    public MessageManager messageManager;
    public PausePanel     pausePanel;
    public StateID        hudStateID;

    private ProgressBar     healthBar;
    private ProgressBar     livesBar;
    private Texture         scorePanel;
    private BitmapFont      hudFont;
    private BitmapFont      bigFont;
    private BitmapFont      midFont;
    private BitmapFont      smallFont;
    private TextureRegion   hudKey;
    private TextureRegion[] compassTexture;
    private DeveloperPanel  developerPanel;

    private float   originX;
    private float   originY;
    private boolean showHUDControls;
    private App     app;

    public HeadsUpDisplay(App _app)
    {
        Trace.__FILE_FUNC();

        this.app = _app;
    }

    public void createHud()
    {
        Trace.__FILE_FUNC();

        AppConfig.hudExists = false;

        app.baseRenderer.hudGameCamera.camera.zoom = 0;
        app.baseRenderer.hudZoom.setZoomValue(0);

        createHUDButtons();

        scorePanel                = app.assets.loadSingleAsset(GameAssets._HUD_PANEL_ASSET, Texture.class);
        GameAssets.hudPanelWidth  = scorePanel.getWidth();
        GameAssets.hudPanelHeight = scorePanel.getHeight();

        messageManager = new MessageManager(app);
        pausePanel     = new PausePanel(app);

        //
        // The player strength for the current life
        healthBar = new ProgressBar(1, 0, 0, Constants._MAX_PROGRESSBAR_LENGTH, "bar9", app);
        healthBar.setHeight(31);
        healthBar.setColor(Color.GREEN);
        healthBar.setScale(4.0f);

        //
        // The number of lives the player has
        livesBar = new ProgressBar(1, 0, 0, Constants._MAX_PROGRESSBAR_LENGTH, "bar9", app);
        livesBar.setHeight(31);
        livesBar.setColor(Color.GREEN);
        livesBar.setScale(4.0f);

        hudKey = app.assets.getObjectsAtlas().findRegion(GameAssets._GOLD_KEY_ASSET);

        compassTexture = new TextureRegion[5];
        GfxUtils.splitRegion
            (
                app.assets.getObjectsAtlas().findRegion("compass"),
                5,
                compassTexture,
                app
            );

        FontUtils fontUtils = new FontUtils();

        hudFont   = fontUtils.createFont(GameAssets._HUD_PANEL_FONT, 50);
        bigFont   = fontUtils.createFont(GameAssets._HUD_PANEL_FONT, 40);
        midFont   = fontUtils.createFont(GameAssets._HUD_PANEL_FONT, 30);
        smallFont = fontUtils.createFont(GameAssets._HUD_PANEL_FONT, 20);

        AppConfig.canDrawButtonBoxes = app.preferences.isEnabled(Preferences._BUTTON_BOXES);
        AppConfig.hudExists          = true;

        hudStateID = StateID._STATE_PANEL_START;
    }

    public void update()
    {
        switch (hudStateID)
        {
            //
            // This state can be used for visually setting up the HUD,
            // or anything else done before HUD update.
            case _STATE_PANEL_START:
            {
                if (app.baseRenderer.hudGameCamera.camera.zoom >= app.baseRenderer.hudGameCamera.getDefaultZoom())
                {
                    app.baseRenderer.hudGameCamera.camera.zoom = app.baseRenderer.hudGameCamera.getDefaultZoom();

                    hudStateID = StateID._STATE_PANEL_UPDATE;
                }
                else
                {
                    app.baseRenderer.hudZoom.in(0.10f);
                }
            }
            break;

            case _STATE_PANEL_UPDATE:
            {
                if (buttonPause.isPressed)
                {
                    AppConfig.pause();
                    buttonPause.release();
                    showHUDControls = false;
                }

                updateBars();
                updateBarColours();
                updateDeveloperItems();

                if (messageManager.isEnabled())
                {
                    messageManager.updateMessage();
                }
            }
            break;

            case _STATE_DEVELOPER_PANEL:
            {
                if (!AppConfig.developerPanelActive)
                {
                    developerPanel = null;
                }
            }
            break;

            case _STATE_PAUSED:
            {
                pausePanel.update();

                if (buttonPause.isPressed)
                {
                    AppConfig.unPause();
                    buttonPause.release();
                    showHUDControls = true;
                }
            }
            break;

            default:
            {
            }
            break;
        }
    }

    private void updateBars()
    {
        if (app.getPlayer() != null)
        {
            healthBar.setTotal(app.getPlayer().strength);
        }
    }

    private void updateBarColours()
    {
        if (healthBar.getTotal() < (float) (Constants._MAX_PROGRESSBAR_LENGTH / 3))
        {
            if (healthBar.getTotal() < (float) (Constants._MAX_PROGRESSBAR_LENGTH / 8))
            {
                healthBar.setColor(Color.RED);
            }
            else
            {
                healthBar.setColor(Color.ORANGE);
            }
        }
        else
        {
            healthBar.setColor(Color.GREEN);
        }
    }

    private void updateDeveloperItems()
    {
        if (Developer.isDevMode() && !AppConfig.gamePaused)
        {
            // DevOptions button which activates the Dev Settings panel
            if ((buttonDevOptions != null) && buttonDevOptions.isPressed && !AppConfig.developerPanelActive)
            {
                AppConfig.developerPanelActive = true;

                developerPanel = new DeveloperPanel(app);
                developerPanel.setup();

                buttonDevOptions.release();
                app.mainGameScreen.getGameState().set(StateID._STATE_DEVELOPER_PANEL);
                hudStateID = StateID._STATE_DEVELOPER_PANEL;
            }
        }
    }

    public void render(OrthographicCamera camera, boolean _canDrawControls)
    {
        if (AppConfig.hudExists)
        {
            originX = (camera.position.x - (float) (Gfx._VIEW_WIDTH / 2));
            originY = (camera.position.y - (float) (Gfx._VIEW_HEIGHT / 2));

            drawPanels();
            drawItems();
            drawCompass();
            drawMessages();

            if (_canDrawControls && app.gameProgress.gameSetupDone)
            {
                drawControls(camera);
            }

            //
            // Draw the Pause panel if activated
            if (hudStateID == StateID._STATE_PAUSED)
            {
                pausePanel.draw(app.spriteBatch, camera, originX, originY);
            }

            drawOnScreenDebug();
        }
    }

    /**
     * Draws the HUD background panels.
     */
    private void drawPanels()
    {
        app.spriteBatch.draw
            (
                scorePanel,
                originX,
                originY + (Gfx._VIEW_HEIGHT - GameAssets.hudPanelHeight)
            );
    }

    /**
     * Draws the counters, totals, etc.
     */
    private void drawItems()
    {
        healthBar.draw
            (
                (int) (originX + displayPos[_HEALTH][_X1]),
                (int) (originY + (Gfx._VIEW_HEIGHT - displayPos[_HEALTH][_Y]))
            );

        livesBar.draw
            (
                (int) (originX + displayPos[_LIVES][_X1]),
                (int) (originY + (Gfx._VIEW_HEIGHT - displayPos[_LIVES][_Y]))
            );

        //
        // Draw the Jailkey panel, if collected.
        // Jailkeys are hidden in one of the mystery treasure chests
        // found in each room.
        if (app.gameProgress.keyCount.getTotal() > 0)
        {
            app.spriteBatch.draw
                (
                    hudKey,
                    originX + displayPos[_HUD_KEY][_X1],
                    originY + (Gfx._VIEW_HEIGHT - displayPos[_HUD_KEY][_Y])
                );
        }

        hudFont.setColor(Color.YELLOW);

        hudFont.draw
            (
                app.spriteBatch,
                String.format(Locale.UK, "%d", app.gameProgress.coinCount.getTotal()),
                originX + displayPos[_COINS][_X1],
                originY + (Gfx._VIEW_HEIGHT - displayPos[_COINS][_Y])
            );

        hudFont.draw
            (
                app.spriteBatch,
                String.format(Locale.UK, "%d", app.gameProgress.gemCount.getTotal()),
                originX + displayPos[_GEMS][_X1],
                originY + (Gfx._VIEW_HEIGHT - displayPos[_GEMS][_Y])
            );

        hudFont.draw
            (
                app.spriteBatch,
                String.format(Locale.UK, "%3d", app.gameProgress.rescuedVillagers.getTotal()),
                originX + displayPos[_VILLAGERS][_X1],
                originY + (Gfx._VIEW_HEIGHT - displayPos[_VILLAGERS][_Y])
            );
    }

    private void drawCompass()
    {
        if (!app.getRoomSystem().activeRoom.compassPoints[Room._N].isEmpty())
        {
            app.spriteBatch.draw
                (
                    compassTexture[Room._N + 1],
                    originX + displayPos[_COMPASS][_X1],
                    originY + (Gfx._VIEW_HEIGHT - displayPos[_COMPASS][_Y])
                );
        }

        if (!app.getRoomSystem().activeRoom.compassPoints[Room._E].isEmpty())
        {
            app.spriteBatch.draw
                (
                    compassTexture[Room._E + 1],
                    originX + displayPos[_COMPASS][_X1],
                    originY + (Gfx._VIEW_HEIGHT - displayPos[_COMPASS][_Y])
                );
        }

        if (!app.getRoomSystem().activeRoom.compassPoints[Room._S].isEmpty())
        {
            app.spriteBatch.draw
                (
                    compassTexture[Room._S + 1],
                    originX + displayPos[_COMPASS][_X1],
                    originY + (Gfx._VIEW_HEIGHT - displayPos[_COMPASS][_Y])
                );
        }

        if (!app.getRoomSystem().activeRoom.compassPoints[Room._W].isEmpty())
        {
            app.spriteBatch.draw
                (
                    compassTexture[Room._W + 1],
                    originX + displayPos[_COMPASS][_X1],
                    originY + (Gfx._VIEW_HEIGHT - displayPos[_COMPASS][_Y])
                );
        }
    }

    private void drawOnScreenDebug()
    {
        if (Developer.isDevMode())
        {
            StringBuilder sb = new StringBuilder("DEV MODE");

            if (Developer.isGodMode())
            {
                sb.append("   :   GOD MODE");
            }

            sb.append("   :   FPS: ").append(Gdx.graphics.getFramesPerSecond());
            sb.append("   :   ZOOM: ").append(app.baseRenderer.tiledGameCamera.camera.zoom);
            sb.append("   :   ").append(app.getRoomSystem().getActiveRoomName());
            sb.append("   :   ").append("mysteryChestsAvailable: ").append(app.getRoomSystem().activeRoom.mysteryChestsAvailable);
            sb.append("   :   MOUSE: ").append(app.inputManager.mouseWorldPosition.toString());

            DebugRenderer.drawText(sb.toString(), originX + 400, originY + 50);
        }
    }

    private void drawControls(OrthographicCamera camera)
    {
        if (!AppConfig.gamePaused)
        {
            if (showHUDControls)
            {
                if (app.preferences.isEnabled(Preferences._SHOW_GAME_BUTTONS))
                {
                    buttonB.draw(app.spriteBatch, camera);
                    buttonA.draw(app.spriteBatch, camera);
                    buttonX.draw(app.spriteBatch, camera);
                    buttonY.draw(app.spriteBatch, camera);
                }

                if (app.inputManager.virtualJoystick != null)
                {
                    app.inputManager.virtualJoystick.getTouchpad().setPosition
                        (
                            originX + displayPos[_JOYSTICK][_X1],
                            originY + displayPos[_JOYSTICK][_Y]
                        );

                    app.inputManager.virtualJoystick.getTouchpad().setBounds
                        (
                            originX + displayPos[_JOYSTICK][_X1],
                            originY + displayPos[_JOYSTICK][_Y],
                            app.inputManager.virtualJoystick.getTouchpad().getWidth(),
                            app.inputManager.virtualJoystick.getTouchpad().getHeight()
                        );
                }

                if (Developer.isDevMode())
                {
                    buttonDevOptions.draw(app.spriteBatch, camera);
                }
            }
        }

        buttonPause.draw(app.spriteBatch, camera);
    }

    private void drawMessages()
    {
        if (!AppConfig.gamePaused)
        {
            if ((app.mainGameScreen.getGameState().get() == StateID._STATE_GAME_FINISHED)
                && (app.mainGameScreen.completedPanel != null))
            {
                app.mainGameScreen.completedPanel.draw();
            }
            else if (messageManager.messageActive)
            {
                messageManager.draw();
            }
        }
    }

    public void showControls()
    {
        showHUDControls = true;

        if (app.preferences.isEnabled(Preferences._SHOW_GAME_BUTTONS))
        {
            buttonB.isDrawable = true;
            buttonA.isDrawable = true;
            buttonX.isDrawable = true;
            buttonY.isDrawable = true;

            if (app.inputManager.virtualJoystick != null)
            {
                app.inputManager.virtualJoystick.show();
            }
        }

        buttonPause.isDrawable = true;
    }

    public void hideControls()
    {
        showHUDControls = false;

        if (app.preferences.isEnabled(Preferences._SHOW_GAME_BUTTONS))
        {
            buttonB.isDrawable = false;
            buttonA.isDrawable = false;
            buttonX.isDrawable = false;
            buttonY.isDrawable = false;

            if (app.inputManager.virtualJoystick != null)
            {
                app.inputManager.virtualJoystick.hide();
            }
        }

        buttonPause.isDrawable = false;
    }

    public void setStateID(final StateID id)
    {
        this.hudStateID = id;
    }

    public ProgressBar getHealthBar()
    {
        return healthBar;
    }

    public ProgressBar getLivesBar()
    {
        return livesBar;
    }

    public void releaseDirectionButtons()
    {
        if (buttonUp != null)
        {
            buttonUp.release();
        }
        if (buttonDown != null)
        {
            buttonDown.release();
        }
        if (buttonLeft != null)
        {
            buttonLeft.release();
        }
        if (buttonRight != null)
        {
            buttonRight.release();
        }
    }

    private void createHUDButtons()
    {
        Trace.__FILE_FUNC();

        buttonUp    = new Switch(app);
        buttonDown  = new Switch(app);
        buttonLeft  = new Switch(app);
        buttonRight = new Switch(app);

        TextureAtlas textureAtlas = app.assets.getButtonsAtlas();

        buttonB = new GameButton
            (
                (textureAtlas.findRegion("button_fire")),
                (textureAtlas.findRegion("button_fire_pressed")),
                displayPos[_ATTACK][_X1], displayPos[_ATTACK][_Y],
                ButtonID._B,
                app
            );

        buttonA = new GameButton
            (
                (textureAtlas.findRegion("button_drop")),
                (textureAtlas.findRegion("button_drop_pressed")),
                displayPos[_ACTION][_X1], displayPos[_ACTION][_Y],
                ButtonID._A,
                app
            );

        buttonX = new GameButton
            (
                (textureAtlas.findRegion("button_x")),
                (textureAtlas.findRegion("button_x_pressed")),
                displayPos[_BUTTON_X][_X1], displayPos[_BUTTON_X][_Y],
                ButtonID._X,
                app
            );

        buttonY = new GameButton
            (
                (textureAtlas.findRegion("button_y")),
                (textureAtlas.findRegion("button_y_pressed")),
                displayPos[_BUTTON_Y][_X1], displayPos[_BUTTON_Y][_Y],
                ButtonID._Y,
                app
            );

        buttonPause = new GameButton
            (
                (textureAtlas.findRegion("hud_pause_button")),
                (textureAtlas.findRegion("hud_pause_button_pressed")),
                displayPos[_PAUSE][_X1], displayPos[_PAUSE][_Y],
                ButtonID._PAUSE,
                app
            );

        if (Developer.isDevMode())
        {
            buttonDevOptions = new GameButton
                (
                    (textureAtlas.findRegion("button_d")),
                    (textureAtlas.findRegion("button_d_pressed")),
                    displayPos[_DEV_OPTIONS][_X1], displayPos[_DEV_OPTIONS][_Y],
                    ButtonID._DEV,
                    app
                );

            buttonDevOptions.hasSound = false;
        }

        buttonUp.hasSound    = false;
        buttonDown.hasSound  = false;
        buttonLeft.hasSound  = false;
        buttonRight.hasSound = false;
        buttonB.hasSound     = false;
        buttonA.hasSound     = false;
        buttonX.hasSound     = false;
        buttonY.hasSound     = false;

        hideControls();

        AppConfig.gameButtonsReady = true;
    }

    /**
     * Release all used HUD assets
     */
    @Override
    public void dispose()
    {
        buttonA.dispose();
        buttonA = null;

        buttonB.dispose();
        buttonB = null;

        buttonX.dispose();
        buttonX = null;

        buttonY.dispose();
        buttonY = null;

        if (Developer.isDevMode())
        {
            buttonDevOptions = null;
        }

        messageManager.dispose();
        messageManager = null;

        pausePanel.dispose();
        pausePanel = null;

        app.inputManager.virtualJoystick.remove();

        app.assets.unloadAsset(GameAssets._HUD_PANEL_ASSET);

        scorePanel = null;

        bigFont.dispose();
        midFont.dispose();
        smallFont.dispose();
    }
}
