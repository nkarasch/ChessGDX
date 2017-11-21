package nkarasch.chessgdx.view.superclasses;

import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public abstract class ABaseObject {

	protected ModelInstance mModelInstance;

	/**
	 * Lowest level of all renderable objects
	 * 
	 * @param modelInstance
	 *            rendered model instance
	 */
	protected ABaseObject(ModelInstance modelInstance) {
		this.mModelInstance = modelInstance;
	}

	/**
	 * @param delta
	 *            time passed since the last frame
	 */
	public abstract void update(float delta);

	/**
	 * @param attribute
	 *            attribute to apply to model instance
	 */
	protected void setAttributes(Attribute attribute) {
		mModelInstance.materials.get(0).set(attribute);
	}

	/**
	 * @param modelBatch
	 * @param environment
	 */
	public void render(ModelBatch modelBatch, Environment environment) {
		modelBatch.render(mModelInstance, environment);
	}

	/**
	 * @param direction
	 *            axis to rotate around
	 * @param degrees
	 *            degrees to rotate
	 */
	protected void rotate(Vector3 direction, float degrees) {
		mModelInstance.transform.rotate(direction.x, direction.y, direction.z, degrees);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	protected void setTranslation(float x, float y, float z) {
		mModelInstance.transform.setTranslation(x, y, z);
	}

	/**
	 * @param position
	 *            absolute position to move to, not the amount to move from the
	 *            current position
	 */
	protected void setTranslation(Vector3 position) {
		mModelInstance.transform.setTranslation(position.x, position.y, position.z);
	}

	/**
	 * @param otherPosition
	 *            position to calculate this objects distance from
	 * @return distance between this objects origin and the other position
	 *         vector
	 */
	public float getDistanceFrom(Vector3 otherPosition) {
		return otherPosition.dst(mModelInstance.transform.val[12], mModelInstance.transform.val[13], mModelInstance.transform.val[14]);
	}
}
