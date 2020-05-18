
package com.red7projects.dungeon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.config.Settings;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.Sfx;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.game.StateManager;
import com.red7projects.dungeon.graphics.camera.OrthoGameCamera;
import com.red7projects.dungeon.utils.FontUtils;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.ui.*;
import com.red7projects.dungeon.utils.development.Developer;
import com.red7projects.dungeon.utils.development.DeveloperTests;

@SuppressWarnings("WeakerAccess")
public class OptionsPage implements UIPage
{
    private ImageButton buttonStats;
    private ImageButton buttonPrivacy;
    private ImageButton buttonKeySettings;
    private ImageButton buttonTests;

    private CheckBox  musicCheckBox;
    private TextField musicLabel;
    private Slider    musicSlider;
    private CheckBox  fxCheckBox;
    private TextField fxLabel;
    private Slider    fxSlider;

    private Texture   foreground;
    private Skin      skin;
    private StateManager panelState;

    private StatsPanel         statsPanel;
    private PrivacyPolicyPanel privacyPanel;
    private DeveloperTests     testPanel;
    private ScreenID           activePanel;

    private boolean justFinishedStatsPanel;
    private boolean justFinishedPrivacyPanel;
    private boolean justFinishedTestPanel;
    private boolean setupCompleted;

    private final App app;

    public OptionsPage(App _app)
    {
        this.app = _app;

        activePanel = ScreenID._SETTINGS_SCREEN;
        panelState = new StateManager();
    }

    @Override
    public boolean update()
    {
        switch (panelState.get())
        {
            case _STATE_OPENING:
            {
                panelState.set(StateID._STATE_SETUP);
            }
            break;

            case _STATE_SETUP:
            {
                create();

                panelState.set(StateID._STATE_PANEL_UPDATE);
            }
            break;

            case _STATE_PANEL_UPDATE:
            {
                if (AppConfig.optionsPageActive)
                {
                    if (activePanel == ScreenID._TEST_PANEL)
                    {
                        testPanel.updatePanel();
                    }
                }

                if (justFinishedStatsPanel)
                {
                    if (statsPanel != null)
                    {
                        statsPanel.dispose();
                    }

                    justFinishedStatsPanel = false;
                    statsPanel = null;
                    activePanel = ScreenID._SETTINGS_SCREEN;

                    showActors(true);
                }

                if (justFinishedPrivacyPanel)
                {
                    if (privacyPanel != null)
                    {
                        privacyPanel.dispose();
                    }

                    justFinishedPrivacyPanel = false;
                    privacyPanel = null;
                    activePanel = ScreenID._SETTINGS_SCREEN;

                    showActors(true);
                }

                if (justFinishedTestPanel)
                {
                    if (testPanel != null)
                    {
                        testPanel.clearUp();
                    }

                    justFinishedTestPanel = false;
                    testPanel = null;
                    activePanel = ScreenID._SETTINGS_SCREEN;

                    showActors(true);
                }
            }
            break;

            default:
                break;
        }

        return false;
    }

    public void draw(SpriteBatch spriteBatch, OrthoGameCamera camera, float originX, float originY)
    {
        switch (activePanel)
        {
            case _STATS_SCREEN:             statsPanel.draw(spriteBatch);      break;
            case _PRIVACY_POLICY_SCREEN:    privacyPanel.draw(spriteBatch);    break;
            case _TEST_PANEL:               testPanel.draw(spriteBatch);       break;

            case _SETTINGS_SCREEN:
            default:
            {
                if (foreground != null)
                {
                    spriteBatch.draw(foreground, originX, originY);
                }
            }
            break;
        }
    }

    @Override
    public void reset()
    {
    }

    public void show()
    {
        setupCompleted = false;
        activePanel = ScreenID._SETTINGS_SCREEN;

        panelState.set(StateID._STATE_OPENING);
    }

    private void create()
    {
        foreground = app.assets.loadSingleAsset("data/options_foreground.png", Texture.class);

        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        populateTable();

        createButtonListeners();
        createSliderListeners();
        createCheckboxListeners();

        updateSettingsOnEntry();

        activePanel = ScreenID._SETTINGS_SCREEN;

        AppConfig.optionsPageActive    = true;
        AppConfig.developerPanelActive = false;

        setupCompleted = true;
    }

    public void hide()
    {
        if (AppConfig.optionsPageActive)
        {
            buttonStats.addAction(Actions.removeActor());
            buttonPrivacy.addAction(Actions.removeActor());
            buttonStats = null;
            buttonPrivacy = null;

            musicLabel.addAction(Actions.removeActor());
            musicSlider.addAction(Actions.removeActor());
            musicCheckBox.addAction(Actions.removeActor());
            musicLabel = null;
            musicSlider = null;
            musicCheckBox = null;

            fxLabel.addAction(Actions.removeActor());
            fxSlider.addAction(Actions.removeActor());
            fxCheckBox.addAction(Actions.removeActor());
            fxLabel = null;
            fxSlider = null;
            fxCheckBox = null;

            if (buttonKeySettings != null)
            {
                buttonKeySettings.addAction(Actions.removeActor());
            }
            buttonKeySettings = null;

            if (buttonTests != null)
            {
                buttonTests.addAction(Actions.removeActor());
            }
            buttonTests = null;

            app.assets.unloadAsset("data/options_foreground.png");

            foreground = null;
            skin = null;
            statsPanel = null;
            privacyPanel = null;
            testPanel = null;

            AppConfig.optionsPageActive = false;
        }
    }

    private void populateTable()
    {
        final float originX = (app.baseRenderer.hudGameCamera.camera.position.x - (float) (Gfx._HUD_WIDTH / 2));
        final float originY = (app.baseRenderer.hudGameCamera.camera.position.y - (float) (Gfx._HUD_HEIGHT / 2));

        Scene2DUtils scene2DUtils = new Scene2DUtils(app);

        // ----------
        musicCheckBox = scene2DUtils.addCheckBox
            (
                "toggle_on_small",
                "toggle_off_small",
                (int) originX + 450,
                (int) originY + (Gfx._HUD_HEIGHT - 235),
                Color.WHITE,
                skin
            );

        // ----------
        musicSlider = scene2DUtils.addSlider((int) originX + 525, (int) originY + (Gfx._HUD_HEIGHT - 235), skin);
        Slider.SliderStyle style = musicSlider.getStyle();
        style.background = new TextureRegionDrawable(app.assets.getButtonsAtlas().findRegion("slider_background"));
        style.knob = new TextureRegionDrawable(app.assets.getButtonsAtlas().findRegion("slider_knob"));
        musicSlider.setStyle(style);
        musicSlider.setSize(500, 20);

        // ----------
        musicLabel = scene2DUtils.addTextField("0%", (int) originX + 1050, (int) originY + (Gfx._HUD_HEIGHT - 240), Color.WHITE, true, skin);
        TextField.TextFieldStyle labelStyle = musicLabel.getStyle();
        FontUtils fontUtils = new FontUtils();
        labelStyle.font = fontUtils.createFont(GameAssets._ACME_FONT, 20);
        musicLabel.setStyle(labelStyle);
        musicLabel.setSize(60, 40);

        // ----------
        fxCheckBox = scene2DUtils.addCheckBox
            (
                "toggle_on_small",
                "toggle_off_small",
                (int) originX + 450,
                (int) originY + (Gfx._HUD_HEIGHT - 300),
                Color.WHITE,
                skin
            );

        // ----------
        fxSlider = scene2DUtils.addSlider((int) originX + 525, (int) originY + (Gfx._HUD_HEIGHT - 300), skin);
        fxSlider.setStyle(style);
        fxSlider.setSize(500, 20);

        // ----------
        fxLabel = scene2DUtils.addTextField("0%", (int) originX + 1050, (int) originY + (Gfx._HUD_HEIGHT - 310), Color.WHITE, true, skin);
        fxLabel.setStyle(labelStyle);
        fxLabel.setSize(60, 40);

        // ----------
        buttonStats = scene2DUtils.addButton("new_stats_button", "new_stats_button_pressed", (int) originX + 965, (int) originY + (Gfx._HUD_HEIGHT - 500));
        buttonPrivacy = scene2DUtils.addButton("new_privacy_policy_button", "new_privacy_policy_button_pressed", (int) originX + 965, (int) originY + (Gfx._VIEW_HEIGHT - 575));

        // ----------
        if (AppConfig.isDesktopApp())
        {
            buttonKeySettings = scene2DUtils.addButton
                (
                    "button_keyboard",
                    "button_keyboard_pressed",
                    (int) originX + 350,
                    (int) originY + (Gfx._HUD_HEIGHT - 420)
                );
        }

        if (Developer.isDevMode())
        {
            buttonTests = scene2DUtils.addButton
                (
                    "new_test_access_button",
                    "new_test_access_button_pressed",
                    (int) originX + 1070,
                    (int) originY + (Gfx._HUD_HEIGHT - 65)
                );
        }

        showActors(true);
    }

    private void updateSettings()
    {
        app.settings.prefs.putBoolean(Settings._MUSIC_ENABLED, (Sfx.inst().getMusicVolume() != Sfx._SILENT));
        app.settings.prefs.putBoolean(Settings._SOUNDS_ENABLED, (Sfx.inst().getFXVolume() != Sfx._SILENT));

        Sfx.inst().setMusicVolume((int) musicSlider.getValue());
        Sfx.inst().setFXVolume((int) fxSlider.getValue());

        app.settings.prefs.flush();
    }

    private void updateSettingsOnEntry()
    {
        musicSlider.setValue(Sfx.inst().getMusicVolume());
        musicLabel.setText("" + ((int) musicSlider.getValue() * 10) + "%");
        musicCheckBox.setChecked(Sfx.inst().getMusicVolume() > 0);

        fxSlider.setValue(Sfx.inst().getFXVolume());
        fxLabel.setText("" + ((int) fxSlider.getValue() * 10) + "%");
        fxCheckBox.setChecked(Sfx.inst().getFXVolume() > 0);
    }

    private void showActors(boolean _visibilty)
    {
        if (buttonKeySettings != null)
        {
            buttonKeySettings.setVisible(_visibilty);
        }

        buttonStats.setVisible(_visibilty);
        buttonPrivacy.setVisible(_visibilty);

        if (Developer.isDevMode())
        {
            buttonTests.setVisible(_visibilty);
        }

        musicLabel.setVisible(_visibilty);
        musicSlider.setVisible(_visibilty);
        musicCheckBox.setVisible(_visibilty);

        fxLabel.setVisible(_visibilty);
        fxSlider.setVisible(_visibilty);
        fxCheckBox.setVisible(_visibilty);
    }

    private void createButtonListeners()
    {
        if (buttonStats != null)
        {
            buttonStats.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    Sfx.inst().startSound(Sfx.SFX_BEEP);

                    if (statsPanel == null)
                    {
                        showActors(false);
                        justFinishedStatsPanel = false;
                        activePanel = ScreenID._STATS_SCREEN;

                        statsPanel = new StatsPanel(app);
                        statsPanel.setXOffset(0);
                        statsPanel.setYOffset(0);
                        statsPanel.open();
                    }
                }
            });
        }

        if (buttonPrivacy != null)
        {
            buttonPrivacy.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    Sfx.inst().startSound(Sfx.SFX_BEEP);

                    if (privacyPanel == null)
                    {
                        showActors(false);
                        justFinishedPrivacyPanel = false;
                        activePanel = ScreenID._PRIVACY_POLICY_SCREEN;

                        privacyPanel = new PrivacyPolicyPanel(app);
                        privacyPanel.xOffset = 0;
                        privacyPanel.yOffset = 0;
                        privacyPanel.open();
                    }
                }
            });
        }

        if (buttonKeySettings != null)
        {
            buttonKeySettings.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    Sfx.inst().startSound(Sfx.SFX_BEEP);
                }
            });
        }

        if (Developer.isDevMode())
        {
            if (buttonTests != null)
            {
                buttonTests.addListener(new ClickListener()
                {
                    public void clicked(InputEvent event, float x, float y)
                    {
                        Sfx.inst().startSound(Sfx.SFX_BEEP);

                        if (testPanel == null)
                        {
                            showActors(false);
                            justFinishedTestPanel = false;
                            activePanel = ScreenID._TEST_PANEL;

                            testPanel = new DeveloperTests(app);
                            testPanel.setup();
                        }
                    }
                });
            }
        }
    }

    private void createCheckboxListeners()
    {
        if (musicCheckBox != null)
        {
            musicCheckBox.addListener(new ChangeListener()
            {
                /**
                 * @param event the {@link ChangeEvent}
                 * @param actor The event target, which is the actor
                 *              that emitted the change event.
                 */
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    if (setupCompleted)
                    {
                        Sfx.inst().startSound(Sfx.SFX_BEEP);

                        if (musicCheckBox.isChecked() && (musicSlider.getValue() == 0))
                        {
                            musicSlider.setValue(musicSlider.getMaxValue() / 10);
                            Sfx.inst().setMusicVolume((int) musicSlider.getValue());
                        }
                        else if (!musicCheckBox.isChecked() && (musicSlider.getValue() > 0))
                        {
                            musicSlider.setValue(0);
                            Sfx.inst().setMusicVolume(Sfx._SILENT);
                        }
                    }
                }
            });
        }

        if (fxCheckBox != null)
        {
            fxCheckBox.addListener(new ChangeListener()
            {
                /**
                 * @param event the {@link ChangeEvent}
                 * @param actor The event target, which is the actor that emitted the change event.
                 */
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    if (setupCompleted)
                    {
                        Sfx.inst().startSound(Sfx.SFX_BEEP);

                        if (fxCheckBox.isChecked() && (fxSlider.getValue() == 0))
                        {
                            fxSlider.setValue(fxSlider.getMaxValue() / 10);
                            Sfx.inst().setFXVolume((int) fxSlider.getValue());
                        }
                        else if (!fxCheckBox.isChecked() && (fxSlider.getValue() > 0))
                        {
                            fxSlider.setValue(0);
                            Sfx.inst().setFXVolume(Sfx._SILENT);
                        }
                    }
                }
            });
        }
    }

    private void createSliderListeners()
    {
        if (musicSlider != null)
        {
            musicSlider.addListener(new ChangeListener()
            {
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    if (setupCompleted)
                    {
                        musicLabel.setText("" + ((int) musicSlider.getValue() * 10) + "%");
                        Sfx.inst().setMusicVolume((int) musicSlider.getValue());

                        musicCheckBox.setChecked(musicSlider.getValue() > 0);

                        updateSettings();
                    }
                }
            });
        }

        if (fxSlider != null)
        {
            fxSlider.addListener(new ChangeListener()
            {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor)
                {
                    if (setupCompleted)
                    {
                        fxLabel.setText("" + ((int) fxSlider.getValue() * 10) + "%");
                        Sfx.inst().setFXVolume((int) fxSlider.getValue());

                        long id = Sfx.inst().startSound(Sfx.SFX_TEST_SOUND);
                        Sfx.inst().sounds[Sfx.SFX_TEST_SOUND].setVolume(id, Sfx.inst().getUsableFxVolume());

                        fxCheckBox.setChecked(fxSlider.getValue() > 0);

                        updateSettings();
                    }
                }
            });
        }
    }

    @Override
    public void dispose()
    {
    }
}
