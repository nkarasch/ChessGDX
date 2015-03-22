package nkarasch.chessgdx.logiccontroller.frontend;

import nkarasch.chessgdx.logiccontroller.backend.ChessLogicController;
import nkarasch.chessgdx.logiccontroller.backend.ChessPieceMovePair;
import nkarasch.chessgdx.perspectiveview.gameobjects.ChessPiece;
import nkarasch.chessgdx.perspectiveview.gameobjects.GridSpaceHighlighter;
import nkarasch.chessgdx.perspectiveview.gameobjects.GridSpaceHighlighter.GridState;
import nkarasch.chessgdx.util.SoundManager;
import nkarasch.chessgdx.view.renderers.OverlayRenderer;
import chesspresso.move.Move;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.utils.Array;

public class BoardController extends InputAdapter {

	private static final int DELAY_SECONDS = 3;

	private ObjectPicker mObjectPicker;
	private VisibleBoardState mViewBoardState;
	private PerspectiveCamera mCamera;
	private ChessLogicController mLogicController;
	private OverlayRenderer mOverlay;

	private int mButtonPressed;
	private Array<Short> mLegalMoves;
	private long mLastMoveTime;
	private boolean mPlayerIsWhite;

	// TODO low priority, make this less disgusting

	/**
	 * Handles all interactions between the visible board and the background
	 * logic.
	 * 
	 * @param camera
	 * @param logicController
	 *            handles all behind the behind the scenes chess logic
	 * @param overlay
	 *            the Scene2D GUI
	 */
	public BoardController(PerspectiveCamera camera, ChessLogicController logicController, OverlayRenderer overlay) {
		this.mCamera = camera;
		this.mLogicController = logicController;
		this.mOverlay = overlay;
		mPlayerIsWhite = true;
		mObjectPicker = new ObjectPicker();
		mViewBoardState = new VisibleBoardState();
	}

	/**
	 * This update is called to handle the AI's turn and check for responses
	 * from the promotion dialog
	 */
	public void update() {
		if (!isPlayerTurn() && !mLogicController.isMate()) {
			mOverlay.getEngineThinkingImage().setVisible(true);
			if (System.currentTimeMillis() - mLastMoveTime > DELAY_SECONDS * 1000) {
				ChessPieceMovePair bestMove;
				if ((bestMove = mLogicController.getBestMove()) != null) {
					mViewBoardState.performMove(bestMove);
					SoundManager.getInstance().playMove();
					mOverlay.getEngineThinkingImage().setVisible(false);
				}
			}
		}

		if (mOverlay.getPromotionDialog().isVisible()) {
			if (mOverlay.getPromotionDialog().getSelected() != 0) {
				mViewBoardState.getPromotionTo().gridSpace.setEffect(GridState.FADE_GREEN);
				mLastMoveTime = mViewBoardState.performMove(mLogicController.performMove(mViewBoardState.getPromotionFrom().indexLocation,
						mViewBoardState.getPromotionTo().indexLocation, mOverlay.getPromotionDialog().getSelected()));
				mOverlay.getPromotionDialog().disable();
				mViewBoardState.setPromotionFrom(null);
				mViewBoardState.setPromotionTo(null);
			}
		}
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		mButtonPressed = button;
		if (button == Input.Buttons.LEFT && isPlayerTurn() && mViewBoardState.getPromotionFrom() == null) {
			if (mViewBoardState.getUserFrom() == null) {
				// pick chessPiece
				mViewBoardState.setUserFrom(mObjectPicker.getPickedChessPiece(mCamera.position, mCamera.getPickRay(screenX, screenY),
						mViewBoardState.getGridPairs().values().toArray(), mPlayerIsWhite));
				if (mViewBoardState.getUserFrom() != null) {
					mLegalMoves = mLogicController.getAllMoves().get(mViewBoardState.getUserFrom().indexLocation);
					mViewBoardState.getUserFrom().chessPiece.onClicked();
					SoundManager.getInstance().playClicked();
				}
			}
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		// move attempt made
		if (button == Input.Buttons.LEFT) {
			short move = isLegalMove(screenX, screenY);
			if (move != Move.ILLEGAL_MOVE) {
				if (Move.isPromotion(move)) {
					if (!mOverlay.getPromotionDialog().isVisible()) {
						mOverlay.getPromotionDialog().setVisible(true);
						mViewBoardState.setPromotionFrom(mViewBoardState.getUserFrom());
						mViewBoardState.setPromotionTo(mViewBoardState.getUserTo());
					}
				} else {
					mViewBoardState.getUserTo().gridSpace.setEffect(GridState.FADE_GREEN);
					mLastMoveTime = mViewBoardState.performMove(mLogicController.performMove(mViewBoardState.getUserFrom().indexLocation,
							mViewBoardState.getUserTo().indexLocation, (short) 0));
					SoundManager.getInstance().playReleased();
				}

			} else {
				if (mViewBoardState.getUserTo() != null) {
					mViewBoardState.getUserTo().gridSpace.setEffect(GridState.FADE_RED);
				}
			}

		}

		mViewBoardState.setUserFrom(null);
		mViewBoardState.setUserTo(null);

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		if (mViewBoardState.getUserTo() != null) {
			mViewBoardState.getUserTo().gridSpace.setEffect(GridState.NONE);
		}

		if (mViewBoardState.getUserFrom() != null) {
			mViewBoardState.setUserTo(mObjectPicker.getPickedGridSpace(mCamera.position, mCamera.getPickRay(screenX, screenY),
					mViewBoardState.getGridPairs().values().toArray()));
			if (mViewBoardState.getUserTo() != null) {
				if (mViewBoardState.getUserFrom() != mViewBoardState.getUserTo()) {
					mViewBoardState.getUserTo().gridSpace.setEffect(GridState.ILLEGAL_HOVER);
					if (isLegalGrid(mViewBoardState.getUserTo().indexLocation)) {
						mViewBoardState.getUserTo().gridSpace.setEffect(GridState.LEGAL_HOVER);
					}
				}
			}
		}

		if (mButtonPressed != Input.Buttons.LEFT) {
			return super.touchDragged(screenX, screenY, pointer);
		} else {
			return true;
		}
	}

	/**
	 * @param screenX
	 *            vertical position in which screen was clicked
	 * @param screenY
	 *            vertical position in which screen was clicked
	 * @return the Chesspresso notation move if legal, Move.ILLEGAL_MOVE is not.
	 */
	private short isLegalMove(int screenX, int screenY) {
		if (mViewBoardState.getUserFrom() != null) {
			mViewBoardState.getUserFrom().chessPiece.onReleased();
			mViewBoardState.setUserTo(mObjectPicker.getPickedGridSpace(mCamera.position, mCamera.getPickRay(screenX, screenY),
					mViewBoardState.getGridPairs().values().toArray()));
			if (mViewBoardState.getUserFrom() != mViewBoardState.getUserTo() && mViewBoardState.getUserTo() != null) {
				for (int i = 0; i < mLegalMoves.size; i++) {
					if (Move.getToSqi(mLegalMoves.get(i)) == mViewBoardState.getUserTo().indexLocation) {
						short move = mLegalMoves.get(i);
						return move;
					}
				}
			}
		}

		return Move.ILLEGAL_MOVE;
	}

	/**
	 * @param toLocation
	 *            mouse released location in 0-63 format
	 * @return is a move by the currently selected piece to the given location
	 *         legal
	 */
	private boolean isLegalGrid(int toLocation) {
		if (mLegalMoves != null) {
			for (int i = 0; i < mLegalMoves.size; i++) {
				if (Move.getToSqi(mLegalMoves.get(i)) == toLocation) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Called at launch to add the viewable chess pieces and grid highlighters
	 * so they can be interacted with by the behind the scenes game logic.
	 * 
	 * @param indexLocation
	 *            Chesspresso 0-63 notation location these components are
	 *            attributed to
	 * @param gridSpace
	 *            grid space highlighter for this location
	 * @param chessPiece
	 *            chess piece in this location, can be null
	 */
	public void registerPair(int indexLocation, GridSpaceHighlighter gridSpace, ChessPiece chessPiece) {
		mViewBoardState.registerPair(indexLocation, gridSpace, chessPiece);
	}

	/**
	 * @return is it currently the players turn
	 */
	private boolean isPlayerTurn() {
		if (mPlayerIsWhite == mLogicController.isWhiteTurn()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param isWhite
	 *            is the player playing as white
	 */
	public void setPlayerColor(boolean isWhite) {
		mPlayerIsWhite = isWhite;
	}

	/**
	 * Moves all chess pieces back to their starting positions
	 */
	public void resetList() {
		mLastMoveTime = System.currentTimeMillis();
		mViewBoardState.resetList();
	}
}
