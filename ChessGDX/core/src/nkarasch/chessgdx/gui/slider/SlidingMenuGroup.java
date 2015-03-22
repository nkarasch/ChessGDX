package nkarasch.chessgdx.gui.slider;

import nkarasch.chessgdx.GameCore;
import nkarasch.chessgdx.gui.MenuSkin;
import nkarasch.chessgdx.gui.other.ExitConfirmTable;
import nkarasch.chessgdx.gui.settings.SettingsMenuGroup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class SlidingMenuGroup extends Group {

	private boolean mOpen;
	private MoveHistoryTable mMoveHistory;

	/**
	 * Group of all of the components that are part of the sliding menu and side
	 * panel.
	 * 
	 * @param skin
	 * @param exitConfirm
	 *            exit confirmation dialog
	 * @param settings
	 *            settings menu group
	 * @param core
	 *            base of game, used by the NewGameTable for resetting the game
	 *            back to the launch state.
	 */
	public SlidingMenuGroup(Skin skin, final ExitConfirmTable exitConfirm, final SettingsMenuGroup settings, GameCore core) {

		int screenWidth = Gdx.graphics.getWidth();
		final int screenHeight = Gdx.graphics.getHeight();

		final Image background = new Image(skin.get(MenuSkin.TEX_LOW_ALPHA_GREY, TextureRegionDrawable.class));
		final SliderMenuTable sliderMenu = new SliderMenuTable(skin);
		mMoveHistory = new MoveHistoryTable(skin);
		final NewGameTable newGameDialog = new NewGameTable(skin, core);

		background.setSize(screenHeight / 3, screenHeight);
		background.setPosition(screenWidth, 0);

		addActor(background);
		addActor(sliderMenu);
		addActor(mMoveHistory);
		addActor(newGameDialog);

		sliderMenu.getSliderButton().addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				if (!mOpen) {
					addAction(Actions.moveBy(-screenHeight / 3, 0, 0.5f));
					mOpen = true;
				} else {
					addAction(Actions.moveBy(screenHeight / 3, 0, 0.5f));
					mOpen = false;
				}
			}
		});

		sliderMenu.getSettingsButton().addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (settings.getSettingsRoot().isVisible()) {
					settings.getSettingsRoot().setVisible(false);
				} else {
					settings.getSettingsRoot().setVisible(true);
				}
			}
		});

		sliderMenu.getExitButton().addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				exitConfirm.setVisible(true);
			}
		});
	}

	/**
	 * @return dialog that displays the move history of the current game
	 */
	public MoveHistoryTable getMoveHistory() {
		return mMoveHistory;
	}
}
