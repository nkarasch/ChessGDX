package nkarasch.chessgdx.perspectiveview.gameobjects;

import java.util.Random;

import nkarasch.chessgdx.logiccontroller.frontend.ChessPieceGraveyard;
import nkarasch.chessgdx.util.AssetHandler;
import nkarasch.chessgdx.view.superclasses.ABoundsObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class ChessPiece extends ABoundsObject {

	private final static Random random = new Random();

	private final Vector3 mGraveyardPosition;
	private final Vector3 mHomePosition;
	private final Vector3 mMoveStartPosition;
	private Vector3 mDestination;
	private final Vector3 mMoveDirection;
	private final Vector3 mCurrentPosition;
	private Vector3 mTemp;

	private boolean mMoving, mDead, mPromoted;
	private float mMoveStart, mDelay, mMoveTime, mYAxis, mMoveDistance, mMoveSpeed, mRotationSpeed;
	private short mDescription;

	/**
	 * @param modelInstance
	 *            full quality model that will be displayed
	 * @param collisionModelInstance
	 *            low polygon model used for collision detection
	 * @param description
	 *            Chesspresso notation piece type
	 * @param indexPosition
	 *            Chesspresso 0-63 location this chess piece starts on
	 * @param graveyardPosition
	 *            location the piece will go to when it died, should be a
	 *            position retrieved from
	 *            {@link ChessPieceGraveyard#getNextLocation(short)}
	 */
	public ChessPiece(ModelInstance modelInstance, ModelInstance collisionModelInstance, short description, int indexPosition,
			Vector3 graveyardPosition) {
		super(modelInstance, collisionModelInstance);

		this.mGraveyardPosition = graveyardPosition;
		this.mDescription = description;
		mHomePosition = new Vector3();
		mMoveStartPosition = new Vector3();
		mDestination = new Vector3();
		mMoveDirection = new Vector3();
		mCurrentPosition = new Vector3();
		mTemp = new Vector3();
		// rotate to face direction based on color, set diffuse to black if
		// black
		if (description < 0) {
			rotate(Vector3.Y, -90.0f);
		} else {
			modelInstance.materials.get(0).set(ColorAttribute.createDiffuse(Color.BLACK));
			rotate(Vector3.Y, 90.0f);
		}

		mTemp = indexToBoardPosition(indexPosition);

		// remember starting position for new game reset translation
		mHomePosition.set(mTemp.x, mTemp.y, mTemp.z);

		// translate to starting board position
		setTranslation(mHomePosition);
	}

	/**
	 * Change piece model to a different type, updating the low poly collision
	 * model and bounding box in the process
	 * 
	 * @param promotion
	 *            Chesspresso notation piece type to convert this one to
	 */
	public void promote(short promotion) {
		mPromoted = true;
		mModelInstance = AssetHandler.getInstance().getPieceModel(promotion);
		mCollisionModelInstance = AssetHandler.getInstance().getPieceCollisionModel(promotion);

		// rotate to face direction based on color
		if (mDescription < 0) {
			rotate(Vector3.Y, -90.0f);
		} else {
			mModelInstance.materials.get(0).set(ColorAttribute.createDiffuse(Color.BLACK));
			rotate(Vector3.Y, 90.0f);
		}
		calculateBounds();
	}

	@Override
	public void update(float delta) {
		if (mMoving) {
			mMoveStart += delta;
			if (mMoveStart > mDelay) {

				rotate(Vector3.Y, mRotationSpeed * delta);

				mCurrentPosition.set(mModelInstance.transform.val[12], mModelInstance.transform.val[13], mModelInstance.transform.val[14]);

				// up-down follows a cosine wave
				float y = 0.5f * MathUtils.cosDeg(360.0f * (mMoveStart - mDelay) / (mMoveTime * 2.0f));
				setTranslation(mCurrentPosition.x + (mMoveDirection.x * delta * mMoveSpeed), mCurrentPosition.y + (y * delta * mMoveSpeed)
						+ (mYAxis * delta), mCurrentPosition.z + (mMoveDirection.z * delta * mMoveSpeed));

				if (mCurrentPosition.epsilonEquals(mDestination, 0.1f)) {
					mMoving = false;
				} else {

					/*
					 * Protection from pieces flying away if they go to far and
					 * fail the epsilonEquals condition. This should be
					 * unreachable on modern computers but it's for safety on
					 * extremely slow machines long delta times that might lead
					 * to translations longer than 0.1.
					 */
					if (getDistanceFrom(mMoveStartPosition) > mMoveDistance) {
						setTranslation(mDestination);
						mMoving = false;
					}
				}
			}
		}
	}

	/**
	 * Move piece to another position on the board.
	 * 
	 * @param newIndexPosition
	 *            Chesspresso notation (0 to 63) index for the piece to move to
	 * @param delay
	 *            How long to wait before starting the move
	 * @return total time of move with a small offset, used to delay the start
	 *         of the captured pieces move if this is capturing
	 */
	public float boardMove(int newIndexPosition, float delay) {

		mMoving = true;
		this.mDelay = delay;
		this.mMoveStart = 0;
		this.mMoveSpeed = 4.5f;
		this.mYAxis = 0.0f;

		mDestination = indexToBoardPosition(newIndexPosition);

		mMoveDirection.set(mDestination.x - mModelInstance.transform.val[12], mDestination.y - mModelInstance.transform.val[13],
				mDestination.z - mModelInstance.transform.val[14]).nor();

		mMoveStartPosition.set(mModelInstance.transform.val[12], mModelInstance.transform.val[13], mModelInstance.transform.val[14]);

		mMoveDistance = getDistanceFrom(mDestination);
		mMoveTime = mMoveDistance / mMoveSpeed;
		mRotationSpeed = (random.nextInt(40) - 20) / mMoveTime;
		return (getDistanceFrom(mDestination) - 0.5f) / mMoveSpeed;
	}

	/**
	 * Moves piece to its pre-assigned graveyard location.
	 * 
	 * @param delay
	 *            how long to wait before starting the move
	 */
	public void graveyardMove(float delay) {
		this.mMoving = true;
		this.mDelay = delay;
		this.mMoveStart = 0;
		this.mMoveSpeed = 9.0f;
		this.mDead = true;
		mDestination = mGraveyardPosition;
		mMoveDirection.set(mDestination.x - mModelInstance.transform.val[12], mDestination.y - mModelInstance.transform.val[13],
				mDestination.z - mModelInstance.transform.val[14]).nor();

		mMoveStartPosition.set(mModelInstance.transform.val[12], mModelInstance.transform.val[13], mModelInstance.transform.val[14]);
		mMoveDistance = getDistanceFrom(mDestination);
		mMoveTime = mMoveDistance / mMoveSpeed;
		mYAxis = -0.65f / mMoveTime;
		mRotationSpeed = (random.nextInt(40) - 20) / mMoveTime;
	}

	/**
	 * Moves piece back to its starting position and changes it back to its
	 * initial type if it was promoted
	 */
	public void resetMove() {

		if (mCurrentPosition.epsilonEquals(mHomePosition, 0.1f)) {
			return;
		}

		this.mMoving = true;
		this.mDelay = 1.0f;
		this.mMoveStart = 0;
		this.mMoveSpeed = 9.0f;

		mDestination = mHomePosition;
		mMoveDirection.set(mDestination.x - mModelInstance.transform.val[12], mDestination.y - mModelInstance.transform.val[13],
				mDestination.z - mModelInstance.transform.val[14]).nor();

		mMoveStartPosition.set(mModelInstance.transform.val[12], mModelInstance.transform.val[13], mModelInstance.transform.val[14]);
		mMoveDistance = getDistanceFrom(mDestination);
		mMoveTime = mMoveDistance / mMoveSpeed;
		if (mDead) {
			mYAxis = 0.65f / mMoveTime;
			mDead = false;
		} else {
			mYAxis = 0.0f;
		}

		if (mPromoted) {
			promote(mDescription);
			mPromoted = false;
		}
	}

	/**
	 * Changes the pieces diffuse color to a green tint, used when the user
	 * clicks the piece
	 */
	public void onClicked() {
		mModelInstance.materials.get(0).set(ColorAttribute.createDiffuse(0.5f, 1.0f, 0.5f, 1.0f));
	}

	/**
	 * Changes the pieces diffuse color back to black or white
	 */
	public void onReleased() {
		if (mDescription > 0) {
			mModelInstance.materials.get(0).set(ColorAttribute.createDiffuse(Color.BLACK));
		} else {
			mModelInstance.materials.get(0).set(ColorAttribute.createDiffuse(Color.WHITE));
		}
	}

	/**
	 * @return Chesspresso convention chess piece identity
	 */
	public short getDescription() {
		return mDescription;
	}

	/**
	 * Board indexes to their 3D positions in the renderer.
	 * 
	 * @param indexPosition
	 *            Chesspresso notation board location (0 to 63)
	 * @return the provided index converted to its 3D location
	 */
	private Vector3 indexToBoardPosition(int indexPosition) {

		int rank = indexPosition % 8;
		int file = indexPosition / 8;

		float x = -7.0f + file * 2.0f;
		float z = -7.0f + rank * 2.0f;

		return mTemp.set(x, 0.0f, z);
	}
}