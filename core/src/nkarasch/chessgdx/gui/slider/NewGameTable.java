package nkarasch.chessgdx.gui.slider;

import nkarasch.chessgdx.GameCore;
import nkarasch.chessgdx.gui.MenuSkin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

class NewGameTable extends Table {

	/**
	 * Expands on button click to give the player the option to choose their
	 * color, and start a new game as the chosen color.
	 * 
	 * @param skin
	 * @param core
	 *            the foundation of the game, contains a function to revert the
	 *            game to the launch state
	 */
	NewGameTable(Skin skin, final GameCore core) {

		String[] colors = new String[] { "              white", "              black" };

		TextButton newGame = new TextButton("New Game", skin, MenuSkin.IB_NEW);
		Label mColorLabel = new Label("Color:", skin, MenuSkin.LB_LARGE);
		final List<String> mColor = new List<String>(skin, MenuSkin.LS_COLOR_CHOOSER);
		TextButton mStartGame = new TextButton("Start", skin);

		mColorLabel.setAlignment(Align.center);
		mColor.setItems(colors);

		mStartGame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (mColor.getSelectedIndex() == 0) {
					core.newGame(true);
				} else {
					core.newGame(false);
				}
			}
		});

		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();

		add(newGame).colspan(2).width((screenHeight / 3) * 0.9f);
		row().fill();
		add(mColorLabel).fill();
		add(mColor).fill();
		row();
		add(mStartGame).colspan(2).width((screenHeight / 3) * 0.9f);

		pack();

		setPosition(screenWidth + ((screenHeight / 3) * 0.05f), screenHeight - getHeight() - (getHeight() * 0.1f));
	}
}
