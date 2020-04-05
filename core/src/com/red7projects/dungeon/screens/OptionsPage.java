/*
 *  Copyright 04/06/2018 Red7Projects.
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
import com.red7projects.dungeon.config.Preferences;
import com.red7projects.dungeon.development.Developer;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.Sfx;
import com.red7projects.dungeon.graphics.FontUtils;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.tests.RoomTests;
import com.red7projects.dungeon.ui.*;

@SuppressWarnings("WeakerAccess")
public class OptionsPage implements UIPage
{
    private ImageButton buttonExit;
    private ImageButton buttonStats;
    private ImageButton buttonPrivacy;
    private ImageButton buttonStoryLine;
    private ImageButton buttonKeySettings;
    private ImageButton buttonTests;

    private CheckBox  musicCheckBox;
    private CheckBox  fxCheckBox;
    private TextField musicLabel;
    private TextField fxLabel;
    private Slider    musicSlider;
    private Slider    fxSlider;
    private Texture   background;
    private Texture   foreground;
    private Skin      skin;

    private StatsPanel         statsPanel;
    private PrivacyPolicyPanel privacyPanel;
    private InstructionsPanel  storyPanel;
    private ScreenID           activePanel;

    private boolean justFinishedStatsPanel;
    private boolean justFinishedPrivacyPanel;
    private boolean justFinishedStoryPanel;
    private boolean setupCompleted;

    private final App app;

    public OptionsPage(App _app)
    {
        this.app = _app;
    }

    @Override
    public boolean update()
    {
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

        if (justFinishedStoryPanel)
        {
            if (storyPanel != null)
            {
                storyPanel.dispose();
            }

            justFinishedStoryPanel = false;
            storyPanel = null;
            activePanel = ScreenID._SETTINGS_SCREEN;

            showActors(true);
        }

        return false;
    }

    public void draw(SpriteBatch spriteBatch)
    {
        switch (activePanel)
        {
            case _STATS_SCREEN:             statsPanel.draw(spriteBatch);      break;
            case _PRIVACY_POLICY_SCREEN:    privacyPanel.draw(spriteBatch);    break;
            case _INSTRUCTIONS_SCREEN:      storyPanel.draw(spriteBatch);      break;

            default:
            {
                if (background != null)
                {
                    spriteBatch.draw(background, 0, 0);
                }

                if (foreground != null)
                {
                    spriteBatch.draw(foreground, 0, 0);
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

        background = app.assets.loadSingleAsset("data/night_sky.png", Texture.class);
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
            buttonExit.addAction(Actions.removeActor());
            buttonStats.addAction(Actions.removeActor());
            buttonPrivacy.addAction(Actions.removeActor());
            buttonStoryLine.addAction(Actions.removeActor());
            buttonExit = null;
            buttonStats = null;
            buttonPrivacy = null;
            buttonStoryLine = null;

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

            app.assets.unloadAsset("data/settings_screen_template.png");

            background = null;
            foreground = null;
            skin = null;
            statsPanel = null;
            privacyPanel = null;

            AppConfig.optionsPageActive = false;
        }
    }

    private void populateTable()
    {
        Scene2DUtils scene2DUtils = new Scene2DUtils(app);

        // ----------
        musicCheckBox = scene2DUtils.addCheckBox(900, (Gfx._VIEW_HEIGHT - 470), Color.WHITE, skin);

        // ----------
        musicSlider = scene2DUtils.addSlider(1050, (Gfx._VIEW_HEIGHT - 470), skin);
        Slider.SliderStyle style = musicSlider.getStyle();
        style.background = new TextureRegionDrawable(app.assets.getButtonsAtlas().findRegion("slider_background"));
        style.knob = new TextureRegionDrawable(app.assets.getButtonsAtlas().findRegion("slider_knob"));
        musicSlider.setStyle(style);
        musicSlider.setSize(1000, 40);

        // ----------
        musicLabel = scene2DUtils.addTextField("0%", 2100, (Gfx._VIEW_HEIGHT - 480), Color.WHITE, true, skin);
        TextField.TextFieldStyle labelStyle = musicLabel.getStyle();
        FontUtils fontUtils = new FontUtils();
        labelStyle.font = fontUtils.createFont(GameAssets._ACME_FONT, 40);
        musicLabel.setStyle(labelStyle);
        musicLabel.setSize(120, 80);

        // ----------
        fxCheckBox = scene2DUtils.addCheckBox(900, (Gfx._VIEW_HEIGHT - 600), Color.WHITE, skin);

        // ----------
        fxSlider = scene2DUtils.addSlider(1050, (Gfx._VIEW_HEIGHT - 600), skin);
        fxSlider.setStyle(style);
        fxSlider.setSize(1000, 40);

        // ----------
        fxLabel = scene2DUtils.addTextField("0%", 2100, (Gfx._VIEW_HEIGHT - 620), Color.WHITE, true, skin);
        fxLabel.setStyle(labelStyle);
        fxLabel.setSize(120, 80);

        // ----------
        buttonStats = scene2DUtils.addButton("new_stats_button", "new_stats_button_pressed", 1930, (Gfx._VIEW_HEIGHT - 1000));
        buttonPrivacy = scene2DUtils.addButton("new_privacy_policy_button", "new_privacy_policy_button_pressed", 1930, (Gfx._VIEW_HEIGHT - 1150));
        buttonStoryLine = scene2DUtils.addButton("new_objectives_button", "new_objectives_button_pressed", 1930, (Gfx._VIEW_HEIGHT - 1300));
        buttonExit = scene2DUtils.addButton("new_back_button", "new_back_button_pressed", 20, (Gfx._VIEW_HEIGHT - 160));

        buttonStats.setSize(420, 128);
        buttonPrivacy.setSize(420, 128);
        buttonStoryLine.setSize(420, 128);
        buttonExit.setSize(256, 128);

        // ----------
        if (AppConfig.isDesktopApp())
        {
            buttonKeySettings = scene2DUtils.addButton
                (
                    "button_keyboard",
                    "button_keyboard_pressed",
                    700,
                    (Gfx._VIEW_HEIGHT - 840)
                );

            buttonKeySettings.setSize(420, 128);
        }

        if (Developer.isDevMode())
        {
            buttonTests = scene2DUtils.addButton
                (
                    "new_test_access_button",
                    "new_test_access_button_pressed",
                    1000,
                    (Gfx._VIEW_HEIGHT - 840)
                );

            buttonTests.setSize(420, 128);
        }

        showActors(true);
    }

    private void updateSettings()
    {
        app.preferences.prefs.putBoolean(Preferences._MUSIC_ENABLED, (Sfx.inst().getMusicVolume() != Sfx.inst()._SILENT));
        app.preferences.prefs.putBoolean(Preferences._SOUNDS_ENABLED, (Sfx.inst().getFXVolume() != Sfx.inst()._SILENT));

        Sfx.inst().setMusicVolume((int) musicSlider.getValue());
        Sfx.inst().setFXVolume((int) fxSlider.getValue());

//        app.preferences.prefs.putBoolean(Preferences._ON_SCREEN_CONTROLLER, (AppConfig.isAndroidApp() || AppConfig.isAndroidOnDesktop()));
//        app.preferences.prefs.putBoolean(Preferences._SHOW_GAME_BUTTONS, (AppConfig.isAndroidApp() || AppConfig.isAndroidOnDesktop()));
//        app.preferences.prefs.putBoolean(Preferences._EXTERNAL_CONTROLLER, AppConfig.isDesktopApp());

        app.preferences.prefs.flush();

//        if (app.preferences.isEnabled(Preferences._EXTERNAL_CONTROLLER))
//        {
//            if (!AppConfig.controllersFitted && (app.inputManager.getGameController() != null))
//            {
//                app.inputManager.getGameController().setup();
//            }
//        }
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

        buttonExit.setVisible(_visibilty);
        buttonStats.setVisible(_visibilty);
        buttonPrivacy.setVisible(_visibilty);
        buttonStoryLine.setVisible(_visibilty);

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
                    Sfx.inst().startSound(Sfx.inst().SFX_BEEP);

                    if (statsPanel == null)
                    {
                        showActors(false);
                        justFinishedStatsPanel = false;
                        activePanel = ScreenID._STATS_SCREEN;

                        statsPanel = new StatsPanel(app);
                        statsPanel.setXOffset(0);
                        statsPanel.setYOffset(0);
                        statsPanel.open();

                        buttonExit.setVisible(true);
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
                    Sfx.inst().startSound(Sfx.inst().SFX_BEEP);

                    if (privacyPanel == null)
                    {
                        showActors(false);
                        justFinishedPrivacyPanel = false;
                        activePanel = ScreenID._PRIVACY_POLICY_SCREEN;

                        privacyPanel = new PrivacyPolicyPanel(app);
                        privacyPanel.xOffset = 0;
                        privacyPanel.yOffset = 0;
                        privacyPanel.open();

                        buttonExit.setVisible(true);
                    }
                }
            });
        }

        if (buttonStoryLine != null)
        {
            buttonStoryLine.addListener(new ClickListener()
              {
                  public void clicked(InputEvent event, float x, float y)
                  {
                      Sfx.inst().startSound(Sfx.inst().SFX_BEEP);

                      if (storyPanel == null)
                      {
                          showActors(false);
                          justFinishedStoryPanel = false;
                          activePanel = ScreenID._INSTRUCTIONS_SCREEN;

                          storyPanel = new InstructionsPanel(app);
                          storyPanel.xOffset = 0;
                          storyPanel.yOffset = 0;
                          storyPanel.open();

                          buttonExit.setVisible(true);
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
                    Sfx.inst().startSound(Sfx.inst().SFX_BEEP);
                }
            });
        }

        if (buttonExit != null)
        {
            buttonExit.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    Sfx.inst().startSound(Sfx.inst().SFX_BEEP);

                    updateSettings();

                    switch (activePanel)
                    {
                        case _SETTINGS_SCREEN:
                        {
                            hide();
                        }
                        break;

                        case _STATS_SCREEN:
                        {
                            justFinishedStatsPanel = true;
                        }
                        break;

                        case _INSTRUCTIONS_SCREEN:
                        {
                            justFinishedStoryPanel = true;
                        }
                        break;

                        case _PRIVACY_POLICY_SCREEN:
                        {
                            justFinishedPrivacyPanel = true;
                        }
                        break;

                        default:
                            break;
                    }
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
                        RoomTests.validateRooms(app);
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
                        Sfx.inst().startSound(Sfx.inst().SFX_BEEP);

                        if (musicCheckBox.isChecked() && (musicSlider.getValue() == 0))
                        {
                            musicSlider.setValue(musicSlider.getMaxValue() / 10);
                            Sfx.inst().setMusicVolume((int) musicSlider.getValue());
                        }
                        else if (!musicCheckBox.isChecked() && (musicSlider.getValue() > 0))
                        {
                            musicSlider.setValue(0);
                            Sfx.inst().setMusicVolume(Sfx.inst()._SILENT);
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
                        Sfx.inst().startSound(Sfx.inst().SFX_BEEP);

                        if (fxCheckBox.isChecked() && (fxSlider.getValue() == 0))
                        {
                            fxSlider.setValue(fxSlider.getMaxValue() / 10);
                            Sfx.inst().setFXVolume((int) fxSlider.getValue());
                        }
                        else if (!fxCheckBox.isChecked() && (fxSlider.getValue() > 0))
                        {
                            fxSlider.setValue(0);
                            Sfx.inst().setFXVolume(Sfx.inst()._SILENT);
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

                        long id = Sfx.inst().startSound(Sfx.inst().SFX_TEST_SOUND);
                        Sfx.inst().sounds[Sfx.inst().SFX_TEST_SOUND].setVolume(id, Sfx.inst().getUsableFxVolume());

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
