package nkarasch.chessgdx.perspectiveview.gameobjects;

import nkarasch.chessgdx.view.superclasses.ABaseObject;

import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class StaticObject extends ABaseObject {

	/**
	 * For standard 3D objects that don't have any unique logic.
	 * 
	 * @param modelInstance
	 *            model to be drawn when this object is rendered
	 * @param position
	 *            absolute position
	 * @param attribute
	 *            attribute for the ModelInstance
	 */
	public StaticObject(ModelInstance modelInstance, Vector3 position, Attribute attribute) {
		super(modelInstance);
		setTranslation(position);
		setAttributes(attribute);
	}

	/**
	 * For standard 3D objects that don't have any unique logic.
	 * 
	 * @param modelInstance
	 *            model to be drawn when this object is rendered
	 * @param position
	 *            absolute position
	 * @param rotationDirection
	 *            axis to apply the rotation on
	 * @param rotationDegrees
	 *            how many degrees to rotate on the given axis
	 */
	public StaticObject(ModelInstance modelInstance, Vector3 position, Vector3 rotationDirection, float rotationDegrees) {
		super(modelInstance);

		setTranslation(position);
		rotate(rotationDirection, rotationDegrees);
	}

	@Override
	public void update(float delta) {
	}
}
