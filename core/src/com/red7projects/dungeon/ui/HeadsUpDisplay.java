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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.StringBuilder;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.Constants;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GfxUtils;
import com.red7projects.dungeon.input.buttons.ButtonID;
import com.red7projects.dungeon.input.buttons.GDXButton;
import com.red7projects.dungeon.input.buttons.GameButton;
import com.red7projects.dungeon.input.buttons.Switch;
import com.red7projects.dungeon.input.objects.ControllerType;
import com.red7projects.dungeon.map.Room;
import com.red7projects.dungeon.utils.FontUtils;
import com.red7projects.dungeon.utils.development.DebugRenderer;
import com.red7projects.dungeon.utils.development.Developer;
import com.red7projects.dungeon.utils.development.DeveloperPanel;
import com.red7projects.dungeon.utils.logging.Trace;

import java.util.Locale;

// TODO: 24/10/2018 - This class is too big, trim it down.

public class HeadsUpDisplay implements Disposable
{
    //@formatter:off
    private static final int _X1            = 0;
    private static final int _X2            = 1;
    private static final int _Y             = 2;
    private static final int _WIDTH         = 3;
    private static final int _HEIGHT        = 4;

    private static final int _JOYSTICK      = 0;
    private static final int _BUTTON_X      = 1;
    private static final int _BUTTON_Y      = 2;
    private static final int _BUTTON_B      = 3;
    private static final int _BUTTON_A      = 4;
    private static final int _PAUSE         = 5;
    private static final int _COINS         = 6;
    private static final int _GEMS          = 7;
    private static final int _LIVES         = 8;
    private static final int _HEALTH        = 9;
    private static final int _VILLAGERS     = 10;
    private static final int _COMPASS       = 11;

    private static final int[][] displayPos = new int[][]
        {
            {  40,  820,   50,  240,  240},             // Joystick

            {1069,   22,   85,   96,   96},             // X
            {1128,   22,  141,   96,   96},             // Y
            {1188,   22,   85,   96,   96},             // B (Attack)
            {1128,   79,   29,   96,   96},             // A (Action)

            {1179, 1179,  630,   66,   66},             // Pause Button

            //
            // Y is distance from the TOP of the screen
            { 990,  990,   28,    0,    0},             // Coins total
            { 990,  990,   70,    0,    0},             // Gems total
            {  75,   75,   47,    0,    0},             // Life bar
            {  75,   75,   89,    0,    0},             // Health bar

            //
            // Y is distance from the TOP of the screen
            { 835,  835,   53,    0,    0},             // Villagers
            {1172, 1172,  101,    0,    0},             // Compass
        };
    //@formatter:on

    public MessageManager   messageManager;
    public PausePanel       pausePanel;
    public StateID          hudStateID;
    public GDXButton        buttonUp;
    public GDXButton        buttonDown;
    public GDXButton        buttonLeft;
    public GDXButton        buttonRight;
    public GDXButton        buttonA;
    public GDXButton        buttonB;
    public GDXButton        buttonX;
    public GDXButton        buttonY;
    public Switch           buttonPause;
    public Switch           buttonDevOptions;
    public ItemBar          itemBar;
    public int              itemPanelIndex;

    private ProgressBar     healthBar;
    private ProgressBar     livesBar;
    private Texture         scorePanel;
    private BitmapFont      hudFont;
    private BitmapFont      bigFont;
    private BitmapFont      midFont;
    private BitmapFont      smallFont;
    private DeveloperPanel  developerPanel;
    private TextureRegion[] compassTexture;

    private float originX;
    private float originY;
    private App   app;

    public HeadsUpDisplay(App _app)
    {
        Trace.__FILE_FUNC();

        this.app = _app;
    }

    public void createHud()
    {
        Trace.__FILE_FUNC();

        AppConfig.hudExists = false;

        createHUDButtons();

        scorePanel                = app.assets.loadSingleAsset(GameAssets._HUD_PANEL_ASSET, Texture.class);
        GameAssets.hudPanelWidth  = scorePanel.getWidth();
        GameAssets.hudPanelHeight = scorePanel.getHeight();

        messageManager = new MessageManager(app);
        pausePanel     = new PausePanel(app);

        //
        // The player strength for the current life
        healthBar = new ProgressBar(1, 0, 0, Constants._MAX_PROGRESSBAR_LENGTH, "bar9", app);
        healthBar.height = 19;
        healthBar.scale = 3.0f;
        healthBar.setColor(Color.GREEN);

        //
        // The number of lives the player has
        livesBar = new ProgressBar(1, 0, 0, Constants._MAX_PROGRESSBAR_LENGTH, "bar9", app);
        livesBar.height = 19;
        livesBar.scale = 3.0f;
        livesBar.setColor(Color.GREEN);

        compassTexture = new TextureRegion[5];
        GfxUtils.splitRegion
            (
                app.assets.getObjectsAtlas().findRegion("compass"),
                5,
                compassTexture,
                app
            );

        itemBar = new ItemBar(app);
        itemPanelIndex = 0;

        FontUtils fontUtils = new FontUtils();

        hudFont   = fontUtils.createFont(GameAssets._HUD_PANEL_FONT, 25);
        bigFont   = fontUtils.createFont(GameAssets._HUD_PANEL_FONT, 20);
        midFont   = fontUtils.createFont(GameAssets._HUD_PANEL_FONT, 15);
        smallFont = fontUtils.createFont(GameAssets._HUD_PANEL_FONT, 10);

        AppConfig.hudExists = true;

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
                hudStateID = StateID._STATE_PANEL_UPDATE;
            }
            break;

            case _STATE_PANEL_UPDATE:
            {
                if (buttonPause.isPressed())
                {
                    AppConfig.pause();
                    buttonPause.release();
                }

                updateBars();
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

                if (buttonPause.isPressed())
                {
                    AppConfig.unPause();
                    buttonPause.release();
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
            if ((buttonDevOptions != null) && buttonDevOptions.isPressed() && !AppConfig.developerPanelActive)
            {
                AppConfig.developerPanelActive = true;

                developerPanel = new DeveloperPanel(app, (int) originX, (int) originY);
                developerPanel.setup();

                buttonDevOptions.release();
                app.appState.set(StateID._STATE_DEVELOPER_PANEL);
                hudStateID = StateID._STATE_DEVELOPER_PANEL;
            }
        }
    }

    public void render(boolean _canDrawControls)
    {
        if (AppConfig.hudExists)
        {
            originX = (app.baseRenderer.hudGameCamera.camera.position.x - (float) (Gfx._HUD_WIDTH / 2));
            originY = (app.baseRenderer.hudGameCamera.camera.position.y - (float) (Gfx._HUD_HEIGHT / 2));

            drawPanels();
            drawItems();
            drawCompass();
            drawMessages();

            if (_canDrawControls && app.gameProgress.gameSetupDone)
            {
                drawControls();
            }

            //
            // Draw the Pause panel if activated
            if (hudStateID == StateID._STATE_PAUSED)
            {
                pausePanel.draw(originX, originY);
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
                originY + (Gfx._HUD_HEIGHT - GameAssets.hudPanelHeight)
            );

        itemBar.draw(originX, originY);
    }

    /**
     * Draws the counters, totals, etc.
     */
    private void drawItems()
    {
        healthBar.draw
            (
                (int) (originX + displayPos[_HEALTH][_X1]),
                (int) (originY + (Gfx._HUD_HEIGHT - displayPos[_HEALTH][_Y]))
            );

        livesBar.draw
            (
                (int) (originX + displayPos[_LIVES][_X1]),
                (int) (originY + (Gfx._HUD_HEIGHT - displayPos[_LIVES][_Y]))
            );

        hudFont.setColor(Color.WHITE);

        hudFont.draw
            (
                app.spriteBatch,
                String.format(Locale.UK, "%d", app.gameProgress.coinCount.getTotal()),
                originX + displayPos[_COINS][_X1],
                originY + (Gfx._HUD_HEIGHT - displayPos[_COINS][_Y])
            );

        hudFont.draw
            (
                app.spriteBatch,
                String.format(Locale.UK, "%d", app.gameProgress.gemCount.getTotal()),
                originX + displayPos[_GEMS][_X1],
                originY + (Gfx._HUD_HEIGHT - displayPos[_GEMS][_Y])
            );

        hudFont.draw
            (
                app.spriteBatch,
                String.format(Locale.UK, "%3d", app.gameProgress.rescuedVillagers.getTotal()),
                originX + displayPos[_VILLAGERS][_X1],
                originY + (Gfx._HUD_HEIGHT - displayPos[_VILLAGERS][_Y])
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
                    originY + (Gfx._HUD_HEIGHT - displayPos[_COMPASS][_Y])
                );
        }

        if (!app.getRoomSystem().activeRoom.compassPoints[Room._E].isEmpty())
        {
            app.spriteBatch.draw
                (
                    compassTexture[Room._E + 1],
                    originX + displayPos[_COMPASS][_X1],
                    originY + (Gfx._HUD_HEIGHT - displayPos[_COMPASS][_Y])
                );
        }

        if (!app.getRoomSystem().activeRoom.compassPoints[Room._S].isEmpty())
        {
            app.spriteBatch.draw
                (
                    compassTexture[Room._S + 1],
                    originX + displayPos[_COMPASS][_X1],
                    originY + (Gfx._HUD_HEIGHT - displayPos[_COMPASS][_Y])
                );
        }

        if (!app.getRoomSystem().activeRoom.compassPoints[Room._W].isEmpty())
        {
            app.spriteBatch.draw
                (
                    compassTexture[Room._W + 1],
                    originX + displayPos[_COMPASS][_X1],
                    originY + (Gfx._HUD_HEIGHT - displayPos[_COMPASS][_Y])
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
                sb.append(" : GOD MODE");
            }

            DebugRenderer.drawText(sb.toString(), originX + 50, originY + 50);

            sb.clear();
            sb.append(app.getRoomSystem().getActiveRoomName().toUpperCase());
            sb.append(" : FPS: ").append(Gdx.graphics.getFramesPerSecond());
            sb.append(" : ZOOM: ").append(app.baseRenderer.tiledGameCamera.camera.zoom);
            DebugRenderer.drawText(sb.toString(), originX + 50, originY + 25);

            sb.clear();
            sb.append("'O' - Dev Options");
            DebugRenderer.drawText(sb.toString(), originX + 1100, originY + 75);

            sb.clear();
            sb.append("PHYSICS: ").append(AppConfig.isUsingBOX2DPhysics ? "BOX2D" : "CUSTOM");
            DebugRenderer.drawText(sb.toString(), originX + 1100, originY + 50);

            sb.clear();
            sb.append("ECS: ").append(AppConfig.isUsingAshleyECS ? "ASHLEY" : "CUSTOM");
            DebugRenderer.drawText(sb.toString(), originX + 1100, originY + 25);
        }
    }

    private void drawControls()
    {
        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            if (!AppConfig.gamePaused && (app.appState.peek() != StateID._STATE_MESSAGE_PANEL))
            {
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

                ((GameButton) buttonA).draw();
                ((GameButton) buttonB).draw();
                ((GameButton) buttonX).draw();
                ((GameButton) buttonY).draw();
            }
        }
    }

    private void drawMessages()
    {
        if (!AppConfig.gamePaused)
        {
            if ((app.appState.peek() == StateID._STATE_GAME_FINISHED)
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
        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            buttonB.setVisible(true);
            buttonA.setVisible(true);
            buttonX.setVisible(true);
            buttonY.setVisible(true);

            if (app.inputManager.virtualJoystick != null)
            {
                app.inputManager.virtualJoystick.show();
            }
        }

        if (Developer.isDevMode())
        {
            buttonDevOptions.setVisible(true);
        }
    }

    public void hideControls(boolean canHidePause)
    {
        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            buttonB.setVisible(false);
            buttonA.setVisible(false);
            buttonX.setVisible(false);
            buttonY.setVisible(false);

            if (app.inputManager.virtualJoystick != null)
            {
                app.inputManager.virtualJoystick.hide();
            }
        }

        if (Developer.isDevMode())
        {
            buttonDevOptions.setVisible(true);
        }
    }

    public int getItemPanelIndex()
    {
        return itemPanelIndex;
    }

    public void setItemPanelIndex(int _index)
    {
        itemPanelIndex = _index;
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
        buttonUp.release();
        buttonDown.release();
        buttonLeft.release();
        buttonRight.release();
    }

    private void createHUDButtons()
    {
        Trace.__FILE_FUNC();

        originX = (app.baseRenderer.hudGameCamera.camera.position.x - (float) (Gfx._HUD_WIDTH / 2));
        originY = (app.baseRenderer.hudGameCamera.camera.position.y - (float) (Gfx._HUD_HEIGHT / 2));

        buttonUp    = new Switch();
        buttonDown  = new Switch();
        buttonLeft  = new Switch();
        buttonRight = new Switch();

        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            buttonA = new GameButton
                (
                    app.assets.getButtonsAtlas().findRegion("button_drop"),
                    app.assets.getButtonsAtlas().findRegion("button_drop_pressed"),
                    (int) originX + displayPos[_BUTTON_A][_X1],
                    (int) originY + displayPos[_BUTTON_A][_Y],
                    ButtonID._A,
                    app
                );

            buttonB = new GameButton
                (
                    app.assets.getButtonsAtlas().findRegion("button_fire"),
                    app.assets.getButtonsAtlas().findRegion("button_fire_pressed"),
                    (int) originX + displayPos[_BUTTON_B][_X1],
                    (int) originY + displayPos[_BUTTON_B][_Y],
                    ButtonID._B,
                    app
                );

            buttonX = new GameButton
                (
                    app.assets.getButtonsAtlas().findRegion("button_x"),
                    app.assets.getButtonsAtlas().findRegion("button_x_pressed"),
                    (int) originX + displayPos[_BUTTON_X][_X1],
                    (int) originY + displayPos[_BUTTON_X][_Y],
                    ButtonID._X,
                    app
                );

            buttonY = new GameButton
                (
                    app.assets.getButtonsAtlas().findRegion("button_y"),
                    app.assets.getButtonsAtlas().findRegion("button_y_pressed"),
                    (int) originX + displayPos[_BUTTON_Y][_X1],
                    (int) originY + displayPos[_BUTTON_Y][_Y],
                    ButtonID._Y,
                    app
                );
        }
        else
        {
            buttonA = new Switch();
            buttonB = new Switch();
            buttonX = new Switch();
            buttonY = new Switch();
        }

        buttonPause = new Switch();

        if (Developer.isDevMode())
        {
            buttonDevOptions = new Switch();
        }

        hideControls(false);

        AppConfig.gameButtonsReady = true;
    }

    /**
     * Release all used HUD assets
     */
    @Override
    public void dispose()
    {
        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            buttonUp    = null;
            buttonDown  = null;
            buttonLeft  = null;
            buttonRight = null;

            //
            // These buttons will be on-screen if a _VIRTUAL controller
            // is defined, otherwise they will be switches and do not
            // need disposing.
            ((GameButton) buttonA).dispose();
            ((GameButton) buttonB).dispose();
            ((GameButton) buttonX).dispose();
            ((GameButton) buttonY).dispose();
        }

        buttonA = null;
        buttonB = null;
        buttonX = null;
        buttonY = null;
        buttonDevOptions = null;

        buttonPause = null;

        if (app.inputManager.virtualJoystick != null)
        {
            app.inputManager.virtualJoystick.remove();
        }

        app.assets.unloadAsset(GameAssets._HUD_PANEL_ASSET);

        messageManager.dispose();
        pausePanel.dispose();
        bigFont.dispose();
        midFont.dispose();
        smallFont.dispose();

        healthBar.dispose();
        livesBar.dispose();

        healthBar = null;
        livesBar  = null;

        messageManager = null;
        pausePanel     = null;
        scorePanel     = null;
    }
}
