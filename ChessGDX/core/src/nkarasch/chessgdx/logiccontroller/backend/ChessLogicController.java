package nkarasch.chessgdx.logiccontroller.backend;

import nkarasch.chessgdx.GameCore;
import nkarasch.chessgdx.view.renderers.OverlayRenderer;
import chesspresso.Chess;
import chesspresso.move.IllegalMoveException;
import chesspresso.move.Move;
import chesspresso.position.Position;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class ChessLogicController {

	private ObjectMap<Integer, Array<Short>> mLegalMoveMap;
	private UCIEngineInterface mUCIInterface;
	private Position mPosition;
	private OverlayRenderer mOverlay;

	/**
	 * Handles the behind the scenes chess game logic, taking advantage of the
	 * ChessPresso(TM) library and a user specified UCI protocol compliant chess
	 * engine. Uses Chesspresso's notation.
	 * 
	 * @param overlay
	 *            the Scene2D GUI, needed to add moves to the move history
	 *            dialog and check/checkmate display
	 */
	public ChessLogicController(OverlayRenderer overlay) {
		mLegalMoveMap = new ObjectMap<Integer, Array<Short>>(64);
		mPosition = Position.createInitialPosition();
		mUCIInterface = new UCIEngineInterface(Gdx.app.getPreferences(GameCore.TITLE).getString("engine_path"));
		this.mOverlay = overlay;
		for (int i = 0; i < 64; i++) {
			mLegalMoveMap.put(i, new Array<Short>());
		}
	}

	/**
	 * Used for both chess engine and player moves. Uses Chesspresso's move
	 * notation. Indexes 0-63 represent the locations on the board.
	 * 
	 * @param fromIndex
	 *            0 to 63 position the piece is moving from
	 * @param toIndex
	 *            0 to 63 position the piece is moving to
	 * @param promotion
	 *            if a promotion, the short representation of the piece the pawn
	 *            it turning into. Zero if not a promotion.
	 * @return all data needed to perform a move on the displayed board, moves
	 *         involve a maximum of two pieces
	 */
	public ChessPieceMovePair performMove(int fromIndex, int toIndex, short promotion) {

		short[] allMoves = mPosition.getAllMoves();
		short move = 0;

		// find Chesspresso short representation in it's legal moves list
		for (int i = 0; i < allMoves.length; i++) {
			if (Move.getFromSqi(allMoves[i]) == fromIndex && Move.getToSqi(allMoves[i]) == toIndex
					&& Move.getPromotionPiece(allMoves[i]) == promotion) {
				move = allMoves[i];
				break;
			}
		}

		ChessPieceMovePair movePair = new ChessPieceMovePair(move, Position.isWhiteToPlay(mPosition.getHashCode()));

		// Chesspresso state update
		try {
			mPosition.doMove(move);
		} catch (IllegalMoveException e) {
			e.printStackTrace();
		}

		mOverlay.getMoveHistory().addMove(movePair.getPrimary());
		mOverlay.getCheckmateDialog().setState(mPosition.isMate(), mPosition.isCheck());

		return movePair;
	}

	/**
	 * @return a map containing lists of all moves currently available. The key
	 *         is the board index. example: getAllMoves().get(4) would return a
	 *         list of every position the piece currently at A3(index 4) could
	 *         move.
	 */
	public ObjectMap<Integer, Array<Short>> getAllMoves() {

		// empties the old legal move values from all 64 lists
		for (int i = 0; i < mLegalMoveMap.size; i++) {
			mLegalMoveMap.get(i).clear();
		}

		short[] moves = mPosition.getAllMoves();
		// fills board indexes with their legal moves
		for (int i = 0; i < moves.length; i++) {
			mLegalMoveMap.get(Move.getFromSqi(moves[i])).add(moves[i]);
		}

		return mLegalMoveMap;
	}

	/**
	 * Communicates with the UCI interface to get the best move. Returns null
	 * until the engine that is running on a separate thread has finished
	 * calculating.
	 * 
	 * @return all data needed to perform a move on the displayed board, moves
	 *         involve a maximum of two pieces
	 */
	public ChessPieceMovePair getBestMove() {

		// starts a new thread that waits for the engine to find the best move
		String bestMove = mUCIInterface.getBestMove(mPosition.getFEN());

		// will equal null until that thread is done
		if (bestMove == null) {
			return null;
		}

		// runs once done
		int fromLocation = (Character.getNumericValue(bestMove.charAt(1)) - 1) * 8 + (Integer.valueOf(bestMove.charAt(0)) - 97);
		int toLocation = (Character.getNumericValue(bestMove.charAt(3)) - 1) * 8 + (Integer.valueOf(bestMove.charAt(2)) - 97);

		short promotion = Chess.NO_PIECE;
		if (bestMove.length() > 4) { // if promotion
			switch (bestMove.charAt(4)) {
			case 'n':
				promotion = Chess.KNIGHT;
				break;
			case 'b':
				promotion = Chess.BISHOP;
				break;
			case 'r':
				promotion = Chess.ROOK;
				break;
			case 'q':
			default:
				promotion = Chess.QUEEN;
				break;
			}
		}

		return performMove(fromLocation, toLocation, promotion);
	}

	/**
	 * Converts the position component of FEN notation to an array of 64 shorts.
	 * 
	 * @return array of 64 shorts storing the type of chess piece in every board
	 *         position.
	 */
	public short[] getPlacement() {
		short[] placement = new short[64];
		int index = 0;
		String[] tokens = mPosition.getFEN().split(" ")[0].split("/");

		for (int i = tokens.length - 1; i >= 0; i--) {
			for (int j = 0; j < tokens[i].length(); j++) {
				int iteratorStep = 1;
				switch (Character.toLowerCase(tokens[i].charAt(j))) {
				case 'k':
					placement[index] = Chess.BLACK_KING;
					break;
				case 'q':
					placement[index] = Chess.BLACK_QUEEN;
					break;
				case 'n':
					placement[index] = Chess.BLACK_KNIGHT;
					break;
				case 'r':
					placement[index] = Chess.BLACK_ROOK;
					break;
				case 'b':
					placement[index] = Chess.BLACK_BISHOP;
					break;
				case 'p':
					placement[index] = Chess.BLACK_PAWN;
					break;
				default:
					iteratorStep = Character.getNumericValue(tokens[i].charAt(j));
				}

				// if white
				if (Character.isUpperCase(tokens[i].charAt(j))) {
					placement[index] *= -1;
				}

				index += iteratorStep;
			}
		}

		return placement;
	}

	/**
	 * @return is it the white players turn
	 */
	public boolean isWhiteTurn() {
		return Position.isWhiteToPlay(mPosition.getHashCode());
	}

	/**
	 * @return is the board in a checkmate state
	 */
	public boolean isMate() {
		return mPosition.isMate();
	}

	/**
	 * Sets Chesspresso's state back to the starting position.
	 */
	public void resetPosition() {
		mPosition = Position.createInitialPosition();
	}

	/**
	 * Shuts down the UCI engine
	 */
	public void dispose() {
		mUCIInterface.stopEngine();
	}
}
