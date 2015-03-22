package nkarasch.chessgdx.logiccontroller.backend;

import chesspresso.move.Move;

public class ChessPieceMovePair {

	public enum MoveType {
		STANDARD_NON_CAPTURE, STANDARD_CAPTURE, CASTLE, EN_PASSANT
	}

	private MoveType mMoveType;
	private ChessPieceMove mPrimary;
	private ChessPieceMove mSecondary;

	/**
	 * Converts a Chesspresso short move into all of the data needed for moving
	 * pieces on the displayed chess board. Up to two pieces can be affected by
	 * a move, hence the primary and secondary.
	 * 
	 * @param move
	 *            Chesspresso notation move
	 * @param whiteTurn
	 *            is the move by white
	 */
	ChessPieceMovePair(short move, boolean whiteTurn) {

		int fromIndex = Move.getFromSqi(move);
		int toIndex = Move.getToSqi(move);

		mPrimary = new ChessPieceMove();
		mPrimary.setPromotion((short) Move.getPromotionPiece(move));
		mPrimary.setFrom(fromIndex);
		mPrimary.setTo(toIndex);
		mPrimary.setWhite(whiteTurn);

		mSecondary = new ChessPieceMove();
		mSecondary.setWhite(whiteTurn);

		if (Move.isCapturing(move)) {
			if (Move.isEPMove(move)) {
				mMoveType = MoveType.EN_PASSANT;
				if (whiteTurn) {
					mSecondary.setFrom(toIndex - 8);
				} else {
					mSecondary.setFrom(toIndex + 8);
				}
			} else {
				mMoveType = MoveType.STANDARD_CAPTURE;
				mSecondary.setFrom(toIndex);
			}
			mSecondary.setTo(-2); // all capturing moves have a dead second
									// piece
			mPrimary.setCapturing(true);
		} else {
			mMoveType = MoveType.STANDARD_NON_CAPTURE;
			if (Move.isCastle(move)) {
				mMoveType = MoveType.CASTLE;

				if (whiteTurn) {
					if (Move.isLongCastle(move)) {
						mSecondary.setFrom(0);
						mSecondary.setTo(3);
					} else {
						mSecondary.setFrom(7);
						mSecondary.setTo(5);
					}
				} else {
					if (Move.isLongCastle(move)) {
						mSecondary.setFrom(56);
						mSecondary.setTo(59);
					} else {
						mSecondary.setFrom(63);
						mSecondary.setTo(61);
					}
				}
			}
		}
	}

	/**
	 * @return the main piece movement data.
	 */
	public ChessPieceMove getPrimary() {
		return mPrimary;
	}

	/**
	 * @return the secondary piece movement data. Captures and castles involve a
	 *         secondary piece.
	 */
	public ChessPieceMove getSecondary() {
		return mSecondary;
	}

	/**
	 * @return MoveType enum of whether the move is capturing, non-capturing,
	 *         castling, or "en passant"
	 */
	public MoveType getMoveType() {
		return mMoveType;
	}
}
