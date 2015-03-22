package nkarasch.chessgdx.gui.settings;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SettingsMenuGroup extends Group {

	private SoundSettingsTable mSoundSettings;
	private GraphicsSettingsTable mGraphicsSettings;
	private EngineSettingsTable mEngineSettings;
	private SettingsRootTable mSettingsRoot;

	/**
	 * A group containing the settings dialog root menu, sound settings root
	 * menu, engine settings root menu, and graphics settings root menu
	 * 
	 * @param skin
	 */
	public SettingsMenuGroup(Skin skin) {
		mGraphicsSettings = new GraphicsSettingsTable(skin);
		mEngineSettings = new EngineSettingsTable(skin);
		mSoundSettings = new SoundSettingsTable(skin);
		mSettingsRoot = new SettingsRootTable(skin, this);

		addActor(mGraphicsSettings);
		addActor(mEngineSettings);
		addActor(mSoundSettings);
		addActor(mSettingsRoot);
	}

	/**
	 * @return sound settings dialog box
	 */
	SoundSettingsTable getSoundSettings() {
		return mSoundSettings;
	}

	/**
	 * @return graphics settings dialog box
	 */
	GraphicsSettingsTable getGraphicsSettings() {
		return mGraphicsSettings;
	}

	/**
	 * @return engine settings dialog box
	 */
	EngineSettingsTable getEngineSettings() {
		return mEngineSettings;
	}

	/**
	 * @return root menu that lets the user select Graphics Settings, Engine
	 *         Settings, or Sound Settings
	 */
	public SettingsRootTable getSettingsRoot() {
		return mSettingsRoot;
	}
}
