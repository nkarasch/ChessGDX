package nkarasch.chessgdx.gui.slider;

import nkarasch.chessgdx.gui.MenuSkin;
import nkarasch.chessgdx.logiccontroller.backend.ChessPieceMove;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;

public class MoveHistoryTable extends Table {

	private static int SMALL_LABEL_SIZE = 27;
	private static int BUTTON_SIZE = 64;

	private Array<ChessPieceMove> mWhiteHistory;
	private Array<ChessPieceMove> mBlackHistory;

	private Array<Label> mWhiteMoves;
	private Array<Label> mBlackMoves;

	/**
	 * Displays the move history for both black and white.
	 * 
	 * @param skin
	 */
	MoveHistoryTable(Skin skin) {

		mWhiteHistory = new Array<ChessPieceMove>();
		mBlackHistory = new Array<ChessPieceMove>();
		mWhiteMoves = new Array<Label>();
		mBlackMoves = new Array<Label>();

		int screenHeight = Gdx.graphics.getHeight();

		setWidth(screenHeight / 3);
		int numRows = ((screenHeight - BUTTON_SIZE * 3) / SMALL_LABEL_SIZE) - 1;

		for (int i = 0; i < numRows; i++) {
			Label blackLabel = new Label("     ", skin, MenuSkin.LB_SMALL_WHITE);
			Label whiteLabel = new Label("     ", skin, MenuSkin.LB_SMALL_WHITE);

			blackLabel.setAlignment(Align.center);
			whiteLabel.setAlignment(Align.center);

			mWhiteMoves.add(whiteLabel);
			mBlackMoves.add(blackLabel);
		}

		Label whiteHeader = new Label("White", skin, MenuSkin.LB_LARGE_WHITE);
		Label blackHeader = new Label("Black", skin, MenuSkin.LB_LARGE_WHITE);

		whiteHeader.setAlignment(Align.center);
		blackHeader.setAlignment(Align.center);

		add(whiteHeader).width(screenHeight / 6);
		add(blackHeader).width(screenHeight / 6);

		for (int i = 0; i < numRows; i++) {
			row();
			add(mWhiteMoves.get(i)).center().fillX();
			add(mBlackMoves.get(i)).center().fillX();
		}

		pack();
		setHeight(screenHeight - BUTTON_SIZE * 3);
		setPosition(Gdx.graphics.getWidth(), (screenHeight - getHeight() - BUTTON_SIZE * 4));
	}

	/**
	 * Adds a move to the list.
	 * 
	 * @param move
	 *            ChessPieceMove instance that contains information about the
	 *            players color, pieces starting position, and move destination.
	 */
	public void addMove(ChessPieceMove move) {
		if (move.isWhite()) {
			mWhiteHistory.add(move);
		} else {
			mBlackHistory.add(move);
		}

		int j = 0;
		for (int i = mWhiteHistory.size - 1; i >= 0; i--) {
			mWhiteMoves.get(j).setText(mWhiteHistory.get(i).toString());
			j++;
			if (j > mWhiteMoves.size - 1) {
				break;
			}
		}

		j = 0;
		for (int i = mBlackHistory.size - 1; i >= 0; i--) {
			mBlackMoves.get(j).setText(mBlackHistory.get(i).toString());
			j++;
			if (j > mBlackMoves.size - 1) {
				break;
			}
		}
	}

	/**
	 * Clears move history list and removes the displayed text. Called when a
	 * new game is made.
	 */
	public void resetHistory() {
		mWhiteHistory.clear();
		mBlackHistory.clear();

		for (Label label : mBlackMoves) {
			label.setText("     ");
		}

		for (Label label : mWhiteMoves) {
			label.setText("     ");
		}
	}
}
