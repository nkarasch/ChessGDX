package nkarasch.chessgdx.gui.other;

import nkarasch.chessgdx.gui.MenuSkin;
import chesspresso.Chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class PromotionTable extends Table {

	private short mSelected;

	/**
	 * GUI element allowing the user to select queen, bishop, knight, or rook to
	 * promote a pawn to.
	 * 
	 * @param skin
	 */
	public PromotionTable(Skin skin) {

		mSelected = Chess.NO_PIECE;

		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();

		ImageButton queenButton = new ImageButton(skin, MenuSkin.IB_QUEEN_PROMOTION);
		ImageButton bishopButton = new ImageButton(skin, MenuSkin.IB_BISHOP_PROMOTION);
		ImageButton knightButton = new ImageButton(skin, MenuSkin.IB_KNIGHT_PROMOTION);
		ImageButton rookButton = new ImageButton(skin, MenuSkin.IB_ROOK_PROMOTION);

		queenButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mSelected = Chess.QUEEN;
			}
		});

		bishopButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mSelected = Chess.BISHOP;
			}
		});

		knightButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mSelected = Chess.KNIGHT;
			}
		});

		rookButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mSelected = Chess.ROOK;
			}
		});

		add(queenButton).width(screenHeight / 12).height(screenHeight / 6);
		add(bishopButton).width(screenHeight / 12).height(screenHeight / 6);
		add(knightButton).width(screenHeight / 12).height(screenHeight / 6);
		add(rookButton).width(screenHeight / 12).height(screenHeight / 6);
		row();
		add(new Label("Choose Promotion", skin, MenuSkin.LB_LARGE)).colspan(4);

		pack();
		setPosition(screenWidth / 2 - getWidth() / 2, screenHeight / 2 - getHeight() / 2);

		setVisible(false);
	}

	/**
	 * @return Chesspresso notation short representing the piece type the user
	 *         chose to promote their pawn to
	 */
	public short getSelected() {
		return mSelected;
	}

	/**
	 * Hides the promotion dialogs
	 */
	public void disable() {
		mSelected = Chess.NO_PIECE;
		setVisible(false);
	}
}
