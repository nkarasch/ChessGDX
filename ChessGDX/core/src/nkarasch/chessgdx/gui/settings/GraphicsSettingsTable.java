package nkarasch.chessgdx.gui.settings;

import nkarasch.chessgdx.GameCore;
import nkarasch.chessgdx.gui.MenuSkin;
import nkarasch.chessgdx.util.GraphicsSettings;
import nkarasch.chessgdx.util.GraphicsSettings.CustomDisplayMode;
import nkarasch.chessgdx.util.PreferenceStrings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

class GraphicsSettingsTable extends Table {

	private GraphicsSettings mGraphicsSettings;

	/**
	 * Options dialog to let the user choose their display resolution, whether
	 * to run in fullscreen/windowed mode, and whether to enable vsync.
	 * 
	 * @param skin
	 */
	GraphicsSettingsTable(Skin skin) {

		mGraphicsSettings = new GraphicsSettings();

		final Preferences pref = Gdx.app.getPreferences(GameCore.TITLE);

		final SelectBox<CustomDisplayMode> sbResolution = new SelectBox<CustomDisplayMode>(skin);
		final CheckBox cbFullscreen = new CheckBox("", skin);
		final CheckBox cbVSync = new CheckBox("", skin);
		TextButton tbCancel = new TextButton("Cancel", skin);
		TextButton tbAccept = new TextButton("Accept? (requires restart)", skin);

		add(new Label("GraphicsSettings", skin, MenuSkin.LB_LARGE)).colspan(2).center();
		row().fill();

		add(new Label("Resolution", skin)).left();
		add(sbResolution).left();
		row().fill();

		add(new Label("Fullscreen", skin)).left();
		add(cbFullscreen).left();
		row().fill();

		add(new Label("VSync", skin)).left();
		add(cbVSync).left();
		row().fill();

		add(tbCancel).left();
		add(tbAccept).left();

		sbResolution.setItems(mGraphicsSettings.getDisplayModes());
		sbResolution.setSelected(mGraphicsSettings.getLaunchDisplayMode());
		if (Gdx.graphics.isFullscreen()) {
			cbFullscreen.setChecked(true);
		}
		if (pref.getBoolean(PreferenceStrings.VSYNC)) {
			cbVSync.setChecked(true);
		}
		tbCancel.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				setVisible(false);
			}
		});

		tbAccept.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				pref.putInteger(PreferenceStrings.DISPLAY_WIDTH, sbResolution.getSelected().width);
				pref.putInteger(PreferenceStrings.DISPLAY_HEIGHT, sbResolution.getSelected().height);
				pref.putBoolean(PreferenceStrings.FULLSCREEN, cbFullscreen.isChecked());
				pref.putBoolean(PreferenceStrings.VSYNC, cbVSync.isChecked());
				pref.flush();
				GameCore.restart();
			}
		});

		background(skin.get(MenuSkin.TEX_LOW_ALPHA_GREY, TextureRegionDrawable.class));
		pack();

		setPosition(Gdx.graphics.getWidth() / 2 - getWidth() / 2, Gdx.graphics.getHeight() / 2 - getHeight() / 2);
		setVisible(false);
	}
}
