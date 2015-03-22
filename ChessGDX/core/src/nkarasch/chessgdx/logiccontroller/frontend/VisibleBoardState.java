package nkarasch.chessgdx.logiccontroller.frontend;

import nkarasch.chessgdx.logiccontroller.backend.ChessPieceMovePair;
import nkarasch.chessgdx.logiccontroller.backend.ChessPieceMovePair.MoveType;
import nkarasch.chessgdx.perspectiveview.gameobjects.ChessPiece;
import nkarasch.chessgdx.perspectiveview.gameobjects.GridSpaceHighlighter;

import com.badlogic.gdx.utils.ObjectMap;

class VisibleBoardState {

	private ObjectMap<Integer, GridLocation> mGridPairs = new ObjectMap<Integer, GridLocation>();
	private GridLocation mUserFrom, mUserTo, mPromotionFrom, mPromotionTo;

	/**
	 * Reverts all chess pieces back to the location they started in. Dead
	 * pieces come back to life.
	 */
	void resetList() {
		for (GridLocation location : mGridPairs.values()) {
			location.chessPiece = location.startingPiece;
			if (location.chessPiece != null) {
				location.chessPiece.resetMove();
			}
		}
	}

	/**
	 * Updates the visible board with the move that was already updated behind
	 * the scenes.
	 * 
	 * @param move
	 *            ChessPieceMovePair containing from, to, and sometimes
	 *            promotion information for the 1-2 chess pieces that need their
	 *            board state updated.
	 * @return the time the move began in milliseconds, can be used for delaying
	 *         the next move
	 */
	long performMove(ChessPieceMovePair move) {
		GridLocation primaryFrom = mGridPairs.get(move.getPrimary().getFrom());
		GridLocation primaryTo = mGridPairs.get(move.getPrimary().getTo());

		float secondPieceDelay = 0.0f;

		// primary piece always moves
		secondPieceDelay = primaryFrom.chessPiece.boardMove(move.getPrimary().getTo(), 0);

		if (move.getMoveType() != MoveType.STANDARD_NON_CAPTURE) {
			GridLocation secondaryFrom = mGridPairs.get(move.getSecondary().getFrom());
			GridLocation secondaryTo = mGridPairs.get(move.getSecondary().getTo());

			if (move.getMoveType() == MoveType.CASTLE) {
				secondaryFrom.chessPiece.boardMove(move.getSecondary().getTo(), secondPieceDelay / 2.0f);
				secondaryTo.chessPiece = secondaryFrom.chessPiece;
				secondaryFrom.chessPiece = null;
			}

			if (move.getMoveType() == MoveType.STANDARD_CAPTURE || move.getMoveType() == MoveType.EN_PASSANT) {
				secondaryFrom.chessPiece.graveyardMove(secondPieceDelay / 2.0f);
				secondaryFrom.chessPiece = null;
			}
		}

		primaryTo.chessPiece = primaryFrom.chessPiece;
		primaryFrom.chessPiece = null;

		if (move.getPrimary().getPromotion() > 0) {
			primaryTo.chessPiece.promote(move.getPrimary().getPromotion());
		}

		return System.currentTimeMillis();
	}

	/**
	 * Add chess piece, grid space highlighter, and their location to the list
	 * for use in move making and click detection.
	 * 
	 * @param indexLocation
	 *            Chesspresso 0-63 notation board location for the piece and
	 *            highlighter
	 * @param gridSpaceHighlighter
	 *            The highlighter for the board location specified by the
	 *            indexLocation
	 * @param chessPiece
	 *            The chess piece currently in the specified index location
	 */
	void registerPair(int indexLocation, GridSpaceHighlighter gridSpaceHighlighter, ChessPiece chessPiece) {
		mGridPairs.put(indexLocation, new GridLocation(indexLocation, gridSpaceHighlighter, chessPiece));
	}

	/**
	 * @return the board location data the piece is moving from
	 */
	GridLocation getUserFrom() {
		return mUserFrom;
	}

	/**
	 * @param userFrom
	 *            the board location data the piece is moving from
	 */
	void setUserFrom(GridLocation userFrom) {
		this.mUserFrom = userFrom;
	}

	/**
	 * @return the board location data the piece is moving to
	 */
	GridLocation getUserTo() {
		return mUserTo;
	}

	/**
	 * @param userTo
	 *            the board location data the promotion piece is moving from
	 */
	void setUserTo(GridLocation userTo) {
		this.mUserTo = userTo;
	}

	/**
	 * @return the board location data the promotion piece is moving from
	 */
	GridLocation getPromotionFrom() {
		return mPromotionFrom;
	}

	/**
	 * @param promotionFrom
	 *            the board location data the promotion piece is moving from
	 */
	void setPromotionFrom(GridLocation promotionFrom) {
		this.mPromotionFrom = promotionFrom;
	}

	/**
	 * @return the board location data the promotion piece is moving to
	 */
	GridLocation getPromotionTo() {
		return mPromotionTo;
	}

	/**
	 * @param promotionTo
	 *            the board location data the promotion piece is moving to
	 */
	void setPromotionTo(GridLocation promotionTo) {
		this.mPromotionTo = promotionTo;
	}

	/**
	 * @return map of all GridLocations which store the chess piece and grid
	 *         space highlighter. Keys 0-63 which refer to all 64 positions on
	 *         the board are always available.
	 */
	ObjectMap<Integer, GridLocation> getGridPairs() {
		return mGridPairs;
	}

	public class GridLocation {
		ChessPiece chessPiece;
		private ChessPiece startingPiece;
		final GridSpaceHighlighter gridSpace;
		int indexLocation;

		/**
		 * Represents the relationship between the visible chess piece models,
		 * grid location highlighters, and their location on the board.
		 * 
		 * @param indexLocation
		 *            Chesspresso 0-63 notation for board location
		 * @param gridSpace
		 *            the board location highlighter object for this location
		 * @param chessPiece
		 *            the chess piece in this index location, can be null for
		 *            empty spaces
		 */
		private GridLocation(int indexLocation, GridSpaceHighlighter gridSpace, ChessPiece chessPiece) {
			this.gridSpace = gridSpace;
			this.chessPiece = chessPiece;
			this.indexLocation = indexLocation;
			this.startingPiece = chessPiece;
		}
	}
}
