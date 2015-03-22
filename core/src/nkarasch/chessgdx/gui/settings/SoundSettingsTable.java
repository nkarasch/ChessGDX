package nkarasch.chessgdx.gui.settings;

import nkarasch.chessgdx.GameCore;
import nkarasch.chessgdx.gui.MenuSkin;
import nkarasch.chessgdx.util.PreferenceStrings;
import nkarasch.chessgdx.util.SoundManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

class SoundSettingsTable extends Table {

	/**
	 * Lets the user adjust the music volume and sound effect volume.
	 * 
	 * @param skin
	 */
	SoundSettingsTable(Skin skin) {
		final Preferences pref = Gdx.app.getPreferences(GameCore.TITLE);

		final Slider ambienceVolume = new Slider(0, 100, 5, false, skin);
		final Slider effectsVolume = new Slider(0, 100, 5, false, skin);

		final Label ambienceVolumeDisplay = new Label(convertFloat(pref.getFloat(PreferenceStrings.MUSIC_VOLUME)), skin);
		final Label effectsVolumeDisplay = new Label(convertFloat(pref.getFloat(PreferenceStrings.EFFECTS_VOLUME)), skin);

		TextButton tbCancel = new TextButton("Cancel", skin);
		TextButton tbAccept = new TextButton("Accept", skin);

		ambienceVolume.setValue(pref.getFloat(PreferenceStrings.MUSIC_VOLUME));
		effectsVolume.setValue(pref.getFloat(PreferenceStrings.EFFECTS_VOLUME));

		add(new Label("Sound Settings", skin, MenuSkin.LB_LARGE)).colspan(3).center();
		row().fill();

		add(new Label("Ambience Volume ", skin)).left();
		add(ambienceVolume).left();
		add(ambienceVolumeDisplay).left();
		row().fill();

		add(new Label("Effects Volume", skin)).left();
		add(effectsVolume).left();
		add(effectsVolumeDisplay).left();
		row().fill();

		add(tbCancel).left();
		add(tbAccept).left();

		ambienceVolume.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				ambienceVolumeDisplay.setText(convertFloat(ambienceVolume.getValue()));
			}
		});

		effectsVolume.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				effectsVolumeDisplay.setText(convertFloat(effectsVolume.getValue()));
			}
		});

		tbCancel.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				setVisible(false);
			}
		});

		tbAccept.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				pref.putFloat(PreferenceStrings.MUSIC_VOLUME, ambienceVolume.getValue());
				pref.putFloat(PreferenceStrings.EFFECTS_VOLUME, effectsVolume.getValue());
				pref.flush();
				SoundManager.getInstance().updateVolume(ambienceVolume.getValue(), effectsVolume.getValue());
				setVisible(false);
			}
		});

		background(skin.get(MenuSkin.TEX_LOW_ALPHA_GREY, TextureRegionDrawable.class));
		pack();

		setPosition(Gdx.graphics.getWidth() / 2 - getWidth() / 2, Gdx.graphics.getHeight() / 2 - getHeight() / 2);
		setVisible(false);
	}

	/**
	 * Attempts to pad the volume slider strings enough to stop the menu from
	 * expanding whenever an extra digit is added. Volume can be set from 0-100.
	 * 
	 * @param number
	 * @return float value as a padded string
	 */
	private String convertFloat(float number) {

		String padding;

		if (number < 10) {
			padding = "   ";
		} else if (number < 100) {
			padding = "  ";
		} else {
			padding = "";
		}

		return padding + String.valueOf((int) number);
	}
}
