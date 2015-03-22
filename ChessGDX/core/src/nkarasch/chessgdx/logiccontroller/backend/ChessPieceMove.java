package nkarasch.chessgdx.logiccontroller.backend;

public class ChessPieceMove {

	private int mFrom;
	private int mTo;
	private short mPromotion;
	private boolean mWhite;
	private boolean mCapturing;
	private char mPiece;
	private char mCapturingPiece;

	/**
	 * Stores all data needed for a single chess pieces movement; where it's
	 * coming from, where it's going to, it's color, and if it's being promoted.
	 */
	public ChessPieceMove() {
		mFrom = -1;
		mTo = -1;
		mPromotion = 0;
		mWhite = true;
	}

	/**
	 * @return board index the piece is moving from
	 */
	public int getFrom() {
		return mFrom;
	}

	/**
	 * @param from
	 *            - board index the piece is moving from
	 */
	void setFrom(int from) {
		this.mFrom = from;
	}

	/**
	 * @return board index the piece is moving to
	 */
	public int getTo() {
		return mTo;
	}

	/**
	 * @param to
	 *            - board index the piece is moving to
	 */
	void setTo(int to) {
		this.mTo = to;
	}

	/**
	 * @return promotion piece in ChessPresso's notation, 0 if not
	 */
	public short getPromotion() {
		return mPromotion;
	}

	/**
	 * @param promotion
	 *            promotion piece in ChessPresso's notation, 0 if not
	 */
	void setPromotion(short promotion) {
		this.mPromotion = promotion;
	}

	/**
	 * @return is the move by white
	 */
	public boolean isWhite() {
		return mWhite;
	}

	/**
	 * @param isWhite
	 *            - is the move by white
	 */
	void setWhite(boolean isWhite) {
		this.mWhite = isWhite;
	}

	/**
	 * @param isCapturing
	 *            is this a capturing move
	 */
	void setCapturing(boolean isCapturing) {
		this.mCapturing = isCapturing;
	}

	public String toString() {
		String from = (char) ((this.mFrom % 8) + 97) + "" + ((this.mFrom / 8) + 1);
		String to = (char) ((this.mTo % 8) + 97) + "" + ((this.mTo / 8) + 1);

		if (mCapturing) {
			return Character.toUpperCase(mPiece) + from + 'x' + Character.toUpperCase(mCapturingPiece) + to;
		} else {
			return Character.toUpperCase(mPiece) + from + '-' + to;
		}
	}
}