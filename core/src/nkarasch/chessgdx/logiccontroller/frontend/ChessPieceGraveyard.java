package nkarasch.chessgdx.logiccontroller.frontend;

import java.util.Collections;
import java.util.Random;
import java.util.Stack;

import com.badlogic.gdx.math.Vector3;

public class ChessPieceGraveyard {

	private final Stack<Vector3> mWhiteGraveyardPositions = buildWhiteGraveyard();
	private final Stack<Vector3> mBlackGraveyardPositions = buildBlackGraveyard();

	/**
	 * @param piece
	 *            the type of chess piece a location is needed for, only used to
	 *            check whether it is black or white
	 * @return a graveyard location from the randomly sorted list of available
	 *         position
	 */
	public Vector3 getNextLocation(short piece) {
		if (piece > 0) {
			return mBlackGraveyardPositions.pop();
		} else {
			return mWhiteGraveyardPositions.pop();
		}
	}

	/**
	 * Creates Vector3 instances of positions for dead pieces to go then
	 * randomly sorts them
	 * 
	 * @return a stack of randomly sorted locations chess pieces can go when
	 *         they die
	 */
	private Stack<Vector3> buildWhiteGraveyard() {
		Stack<Vector3> graveyardPositions = new Stack<Vector3>();

		float baseX = -13.5f, baseY = -0.65f, baseZ = -11.5f;

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 8; j++) {
				graveyardPositions.add(new Vector3(baseX, baseY, baseZ));
				baseX += 2.0f;
			}
			baseX = -13.0f;
			baseZ -= 2.0f;
		}

		Collections.shuffle(graveyardPositions, new Random(System.nanoTime()));

		return graveyardPositions;
	}

	/**
	 * Creates Vector3 instances of positions for dead pieces to go then
	 * randomly sorts them
	 * 
	 * @return a stack of randomly sorted locations chess pieces can go when
	 *         they die
	 */
	private Stack<Vector3> buildBlackGraveyard() {
		Stack<Vector3> graveyardPositions = new Stack<Vector3>();

		float baseX = 13.5f, baseY = -0.65f, baseZ = 11.5f;

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 8; j++) {
				graveyardPositions.add(new Vector3(baseX, baseY, baseZ));
				baseX -= 2.0f;
			}
			baseX = 13.0f;
			baseZ += 2.0f;
		}

		Collections.shuffle(graveyardPositions, new Random(System.nanoTime()));
		return graveyardPositions;
	}
}
