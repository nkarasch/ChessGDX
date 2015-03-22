package nkarasch.chessgdx.view.superclasses;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public abstract class ABoundsObject extends ABaseObject {

	protected ModelInstance mCollisionModelInstance;
	private BoundingBox mBoundingBox;
	private Vector3 mBBOriginMin, mBBOriginMax;

	/**
	 * Uses a combination of bounding boxes and low poly models for click
	 * detection.
	 * 
	 * @param fullModelInstance
	 *            full quality model instance
	 * @param collisionModelInstance
	 *            low poly version of model used for collision detection
	 */
	public ABoundsObject(ModelInstance fullModelInstance, ModelInstance collisionModelInstance) {
		super(fullModelInstance);
		this.mCollisionModelInstance = collisionModelInstance;
		calculateBounds();
	}

	/**
	 * Uses bounding boxes click detection, not taking advantage of the
	 * increased accuracy of using low poly models.
	 * 
	 * @param fullModelInstance
	 *            full quality model instance
	 */
	public ABoundsObject(ModelInstance fullModelInstance) {
		super(fullModelInstance);
		this.mCollisionModelInstance = null;
		calculateBounds();
	}

	/**
	 * Instantiate the bounding box
	 */
	public void calculateBounds() {
		this.mBoundingBox = mModelInstance.calculateBoundingBox(new BoundingBox());
		mBBOriginMin = new Vector3(mBoundingBox.min);
		mBBOriginMax = new Vector3(mBoundingBox.max);
	}

	/**
	 * {@inheritDoc}
	 */
	public void rotate(Vector3 direction, float degrees) {
		super.rotate(direction, degrees);
		if (mCollisionModelInstance != null) {
			mCollisionModelInstance.transform.rotate(direction.x, direction.y, direction.z, degrees);
		}
		mBoundingBox.mul(mCollisionModelInstance.transform);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTranslation(float x, float y, float z) {
		super.setTranslation(x, y, z);
		if (mCollisionModelInstance != null) {
			mCollisionModelInstance.transform.setTranslation(x, y, z);
		}

		mBoundingBox.set(mBoundingBox.min.set(mBBOriginMin.x + x, mBBOriginMin.y + y, mBBOriginMin.z + z),
				mBoundingBox.max.set(mBBOriginMax.x + x, mBBOriginMax.y + y, mBBOriginMax.z + z));
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTranslation(Vector3 position) {
		super.setTranslation(position);
		if (mCollisionModelInstance != null) {
			mCollisionModelInstance.transform.setTranslation(position.x, position.y, position.z);
		}

		mBoundingBox.set(mBoundingBox.min.set(mBBOriginMin.x + position.x, mBBOriginMin.y + position.y, mBBOriginMin.z + position.z),
				mBoundingBox.max.set(mBBOriginMax.x + position.x, mBBOriginMax.y + position.y, mBBOriginMax.z + position.z));
	}

	/**
	 * @return low poly model used for high precision collision detection
	 */
	public ModelInstance getCollisionModelInstance() {
		return mCollisionModelInstance;
	}

	/**
	 * @return bounding box used for low precision collision detection
	 */
	public BoundingBox getBoundingBox() {
		return mBoundingBox;
	}
}
