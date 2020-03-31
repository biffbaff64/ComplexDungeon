/*
 *  Copyright 31/01/2019 Red7Projects.
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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.FontUtils;

@SuppressWarnings("unused")
public class UIUtils
{
    private static App app;

    public static void setup(App _app)
    {
        app = _app;
    }

    public static Table createTable(int x, int y, int width, int height, Skin skin)
    {
        Table table = new Table(skin);
        table.setSize(width, height);
        table.setPosition(x, y);

        return table;
    }

    public static Image createImage(TextureRegion region)
    {
        TextureRegionDrawable drawable = new TextureRegionDrawable(region);

        return new Image(drawable);
    }

    public static Image createImage(String imageName, TextureAtlas atlasLoader)
    {
        TextureRegion region = atlasLoader.findRegion(imageName);
        TextureRegionDrawable drawable = new TextureRegionDrawable(region);

        return new Image(drawable);
    }

    public static Drawable createDrawable(String imageName, TextureAtlas atlasLoader)
    {
        TextureRegion region = atlasLoader.findRegion(imageName);

        return new TextureRegionDrawable(region);
    }

    public static ScrollPane createScrollPane(ScrollPaneObject paneObject)
    {
        ScrollPane scrollPane = new ScrollPane(paneObject.table, paneObject.skin);

        scrollPane.setName(paneObject.name);

        return scrollPane;
    }

    public static ScrollPane createScrollPane(Table table, Skin skin, String name)
    {
        ScrollPane scrollPane = new ScrollPane(table, skin);

        scrollPane.setName(name);

        return scrollPane;
    }

    public static Label addLabel(String labelText, int x, int y, int size, Color color, String fontName)
    {
        FontUtils fontUtils = new FontUtils();

        Label.LabelStyle label1Style = new Label.LabelStyle();
        label1Style.font = fontUtils.createFont(fontName, size, Color.WHITE);
        label1Style.fontColor = color;

        Label label = new Label(labelText, label1Style);
        label.setStyle(label1Style);
        label.setAlignment(Align.center);
        label.setPosition(x, y);

        return label;
    }

    public static Label addLabel(String labelText, int x, int y, Color color, Skin skin)
    {
        Label label = makeLabel(labelText, x, y, color, skin);

        app.stage.addActor(label);

        return label;
    }

    public static TextField addTextField(String string, int x, int y, Color color, boolean disabled, Skin skin)
    {
        TextField textField = makeTextField(string, x, y, color, disabled, skin);

        app.stage.addActor(textField);

        return textField;
    }

    public static TextField addTextField(String string, int x, int y, int size, boolean disabled, Color color, String fontName)
    {
        FontUtils fontUtils = new FontUtils();

        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = fontUtils.createFont(fontName, size, Color.WHITE);
        style.fontColor = color;

        TextField textField = new TextField(string, style);
        textField.setAlignment(Align.center);
        textField.setPosition(x, y);
        textField.setDisabled(disabled);

        app.stage.addActor(textField);

        return textField;
    }

    public static ImageButton addButton(String upButton, String downButton, int x, int y)
    {
        Image imageUp;
        Image imageDown;

        imageUp = new Image(app.assets.getButtonsAtlas().findRegion(upButton));
        imageDown = new Image(app.assets.getButtonsAtlas().findRegion(downButton));

        ImageButton imageButton = new ImageButton(imageUp.getDrawable(), imageDown.getDrawable());

        imageButton.setPosition(x, y);
        imageButton.setVisible(true);

        app.stage.addActor(imageButton);

        return imageButton;
    }

    public static TextButton addButton(String string, int x, int y, Color color, boolean disabled, Skin skin)
    {
        TextButton textButton = makeButton(string, x, y, color, disabled, skin);

        app.stage.addActor(textButton);

        return textButton;
    }

    public static TextButton addButton(String text, int x, int y, int textSize, String fontName)
    {
        FontUtils fontUtils = new FontUtils();

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = fontUtils.createFont(fontName, textSize, Color.WHITE);

        TextButton textButton = new TextButton(text, style);
        textButton.setStyle(style);
        textButton.align(Align.center);
        textButton.setDisabled(false);
        textButton.setPosition(x, y);

        app.stage.addActor(textButton);

        return textButton;
    }

    public static Slider addSlider(int x, int y, Skin skin)
    {
        Slider slider = makeSlider(x, y, skin);

        app.stage.addActor(slider);

        return slider;
    }

    public static CheckBox addCheckBox(int x, int y, Color color, Skin skin)
    {
        CheckBox checkBox = makeCheckBox(x, y, color, skin);

        app.stage.addActor(checkBox);

        return checkBox;
    }

    public static SelectBox addSelectBox(int x, int y, Skin skin)
    {
        SelectBox<String> list = new SelectBox<>(skin);

        list.setPosition(x, y);

        final String[] strings =
            {
                "A", "B", "C", "D", "E",
                "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O",
                "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y",
                "Z",
                "SPACE",
                "ENTER",
                "UP ARROW", "DOWN ARROW", "LEFT ARROW", "RIGHT ARROW",
                "LEFT SHIFT", "RIGHT SHIFT",
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "F1", "F2", "F3", "F4", "F5", "F6",
                "F7", "F8", "F9", "F10", "F11", "F12",
            };

        list.setItems(strings);

        app.stage.addActor(list);

        return list;
    }

    // ---------------------------------------------------------------------------------

    public static Table makeTable(int x, int y, int width, int height, Skin skin)
    {
        Table table = new Table(skin);
        table.setSize(width, height);
        table.setPosition(x, y);

        return table;
    }

    public static Label makeLabel(String string, int x, int y, Color color, Skin skin)
    {
        Label label = new Label(string, skin);
        Label.LabelStyle style = label.getStyle();
        style.fontColor = color;

        label.setStyle(style);
        label.setAlignment(Align.center);

        label.setPosition(x, y);

        return label;
    }

    public static TextField makeTextField(String string, int x, int y, Color color, boolean disabled, Skin skin)
    {
        TextField textField = new TextField(string, skin);
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(textField.getStyle());
        textFieldStyle.fontColor = color;

        textField.setStyle(textFieldStyle);
        textField.setAlignment(Align.center);
        textField.setDisabled(disabled);

        textField.setPosition(x, y);

        return textField;
    }

    public static TextButton makeButton(String string, int x, int y, Color color, boolean disabled, Skin skin)
    {
        TextButton textButton = new TextButton(string, skin);
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(textButton.getStyle());
        style.fontColor = color;

        textButton.setStyle(style);
        textButton.align(Align.center);
        textButton.setDisabled(disabled);

        textButton.setPosition(x, y);

        return textButton;
    }

    public static Slider makeSlider(int x, int y, Skin skin)
    {
        Slider slider = new Slider(0, 10, 1, false, skin);
        Slider.SliderStyle style = slider.getStyle();

        slider.setPosition(x, y);
        slider.setSize(280, 30);

        return slider;
    }

    public static CheckBox makeCheckBox(int x, int y, Color color, Skin skin)
    {
        TextureRegion regionOn = app.assets.getButtonsAtlas().findRegion("toggle_on");
        TextureRegion regionOff = app.assets.getButtonsAtlas().findRegion("toggle_off");

        CheckBox checkBox = new CheckBox("", skin);
        CheckBox.CheckBoxStyle style = checkBox.getStyle();

        style.fontColor = color;
        style.checkboxOn = new TextureRegionDrawable(regionOn);
        style.checkboxOff = new TextureRegionDrawable(regionOff);

        checkBox.setSize(regionOn.getRegionWidth(), regionOn.getRegionHeight());
        checkBox.setStyle(style);
        checkBox.setPosition(x, y);

        return checkBox;
    }

    public static Image makeAchievementsImage(String imageName)
    {
        TextureRegion region = app.assets.getAchievementsAtlas().findRegion(imageName);
        TextureRegionDrawable drawable = new TextureRegionDrawable(region);

        return new Image(drawable);
    }

    public static Image makeObjectsImage(String imageName)
    {
        TextureRegion region = app.assets.getObjectsAtlas().findRegion(imageName);
        TextureRegionDrawable drawable = new TextureRegionDrawable(region);

        return new Image(drawable);
    }

    public static Image makeTextImage(String imageName)
    {
        TextureRegion region = app.assets.getTextAtlas().findRegion(imageName);
        TextureRegionDrawable drawable = new TextureRegionDrawable(region);

        return new Image(drawable);
    }

    public static Drawable makeButtonDrawable(String imageName)
    {
        TextureRegion region = app.assets.getButtonsAtlas().findRegion(imageName);

        return new TextureRegionDrawable(region);
    }

    public static Drawable makeDrawable(String imageName)
    {
        TextureRegion region = app.assets.getObjectsAtlas().findRegion(imageName);

        return new TextureRegionDrawable(region);
    }
}
