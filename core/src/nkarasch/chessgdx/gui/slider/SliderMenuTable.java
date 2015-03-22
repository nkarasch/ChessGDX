package nkarasch.chessgdx.gui.slider;

import nkarasch.chessgdx.gui.MenuSkin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

class SliderMenuTable extends Table {

	private ImageButton mSettingsButton;
	private ImageButton mSliderButton;
	private ImageButton mExitButton;

	/**
	 * A menu containing a settings button, exit button, and a button to expand
	 * ("slide") the side panel.
	 * 
	 * @param skin
	 */
	SliderMenuTable(Skin skin) {

		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();

		mSliderButton = new ImageButton(skin, MenuSkin.IB_SLIDER);
		mSettingsButton = new ImageButton(skin, MenuSkin.IB_SETTINGS);
		mExitButton = new ImageButton(skin, MenuSkin.IB_EXIT);

		add(mSliderButton).size(screenHeight / 20, screenHeight / 20);
		row().fill();
		add(mSettingsButton).size(screenHeight / 20, screenHeight / 20);
		row().fill();
		add(mExitButton).size(screenHeight / 20, screenHeight / 20);

		pack();

		setPosition(screenWidth - getWidth(), 0);
	}

	/**
	 * @return the button used to open the settings root dialog on click
	 */
	public Button getSettingsButton() {
		return mSettingsButton;
	}

	/**
	 * @return the button used to expand the side panel
	 */
	public Button getSliderButton() {
		return mSliderButton;
	}

	/**
	 * @return the button used to open the exit confirmation dialog
	 */
	public Button getExitButton() {
		return mExitButton;
	}
}
