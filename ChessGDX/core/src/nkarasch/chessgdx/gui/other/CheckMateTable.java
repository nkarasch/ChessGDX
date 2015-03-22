package nkarasch.chessgdx.gui.other;

import nkarasch.chessgdx.gui.MenuSkin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class CheckMateTable extends Table {

	private Label mCheckmate;

	/**
	 * A label stored in a table that updates the user to whether the board is
	 * in a check or checkmate state.
	 * 
	 * @param skin
	 */
	public CheckMateTable(Skin skin) {
		mCheckmate = new Label("", skin, MenuSkin.LB_LARGE_NOBG);
		add(mCheckmate);
		pack();
		setPosition(Gdx.graphics.getWidth() / 2 - getWidth() / 2, Gdx.graphics.getHeight() - getHeight());
	}

	/**
	 * Called every time a move is made to update the label informing the user
	 * the board is either in check or checkmate.
	 * 
	 * @param mate
	 *            is the board in a checkmate state
	 * @param check
	 *            is the board in a check state
	 */
	public void setState(boolean mate, boolean check) {

		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();

		if (!check && !mate) {
			setPosition(screenWidth / 2 - getWidth() / 2, screenHeight - getHeight());
			addAction(Actions.fadeOut(0.5f));
		}

		if (check && !mate) {
			mCheckmate.setText("Check");
			pack();
			setPosition(screenWidth / 2 - getWidth() / 2, screenHeight - getHeight());
			addAction(Actions.fadeIn(0.5f));
		}

		if (mate) {
			mCheckmate.setText("Checkmate");
			pack();
			setPosition(screenWidth / 2 - getWidth() / 2, screenHeight - getHeight());
			setTransform(true);
			setOrigin(getWidth() / 2, getHeight() / 2);
			addAction(Actions.fadeIn(0.5f));
			addAction(Actions.moveTo(screenWidth / 2 - getWidth() / 2, screenHeight / 2 - getHeight() / 2, 1.0f));
			addAction(Actions.scaleTo(1.5f, 1.5f, 2.0f));
		}
	}
}
