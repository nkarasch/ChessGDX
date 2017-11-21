package nkarasch.chessgdx.gui.settings;

import nkarasch.chessgdx.gui.MenuSkin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class SettingsRootTable extends Table {

	/**
	 * A dialog box that gives users the option to access graphics settings,
	 * sound settings, and engine settings.
	 * 
	 * @param skin
	 * @param settingsMenu
	 */
	SettingsRootTable(Skin skin, final SettingsMenuGroup settingsMenu) {

		TextButton mBtGraphics = new TextButton("Graphics", skin);
		TextButton mBtEngine = new TextButton("Engine", skin);
		TextButton mBtSound = new TextButton("Sound", skin);

		add(new Label("        Settings        ", skin, MenuSkin.LB_LARGE)).fillX();
		row().fill();

		add(mBtGraphics).fillX().row();
		add(mBtEngine).fillX().row();
		add(mBtSound).fillX().row();

		setVisible(false);

		mBtGraphics.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				setVisible(false);
				if (settingsMenu.getGraphicsSettings().isVisible()) {
					settingsMenu.getGraphicsSettings().setVisible(false);
				} else {
					settingsMenu.getGraphicsSettings().setVisible(true);
				}
			}
		});

		mBtEngine.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				setVisible(false);
				if (settingsMenu.getEngineSettings().isVisible()) {
					settingsMenu.getEngineSettings().setVisible(false);
				} else {
					settingsMenu.getEngineSettings().setVisible(true);
				}
			}
		});

		mBtSound.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				setVisible(false);
				if (settingsMenu.getSoundSettings().isVisible()) {
					settingsMenu.getSoundSettings().setVisible(false);
				} else {
					settingsMenu.getSoundSettings().setVisible(true);
				}
			}
		});

		pack();
		setPosition(Gdx.graphics.getWidth() / 2 - getWidth() / 2, Gdx.graphics.getHeight() / 2 - getHeight() / 2);
	}
}
