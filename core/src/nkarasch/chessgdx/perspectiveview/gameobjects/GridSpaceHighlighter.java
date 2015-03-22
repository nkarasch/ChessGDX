package nkarasch.chessgdx.perspectiveview.gameobjects;

import nkarasch.chessgdx.view.superclasses.ABoundsObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;

public class GridSpaceHighlighter extends ABoundsObject {

	public enum GridState {
		NONE, FADE_GREEN, FADE_RED, LEGAL_HOVER, ILLEGAL_HOVER
	};

	private GridState mGridState;
	private BlendingAttribute mAlphaBlend;

	/**
	 * Typically transparent plane that covers a board grid space. Can change
	 * color to specify legal or illegal moves to the user.
	 * 
	 * @param fullModelInstance
	 * @param indexPosition
	 *            Chesspresso notation board location (0 to 63)
	 */
	public GridSpaceHighlighter(ModelInstance fullModelInstance, int indexPosition) {
		super(fullModelInstance);

		mGridState = GridState.NONE;
		mAlphaBlend = new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, 0.0f);

		// translate to starting board position
		Vector3 initialPosition = getGridToPerspectiveTranslation(indexPosition);
		setTranslation(initialPosition);

		fullModelInstance.materials.get(0).set(mAlphaBlend);
	}

	@Override
	public void update(float delta) {

		if (mGridState != GridState.NONE) {

			if (mGridState == GridState.FADE_GREEN || mGridState == GridState.FADE_RED)
				if (mAlphaBlend.opacity > 0.01f) {
					mAlphaBlend.opacity -= delta;
				} else {
					setEffect(GridState.NONE);
				}
		}
	}

	/**
	 * Initializes a highlighter effect, will be processed in
	 * {@link GridSpaceHighlighter#update(float)}
	 * 
	 * @param state
	 *            effect to be initialized
	 */
	public void setEffect(GridState state) {

		mGridState = state;

		switch (state) {
		case FADE_GREEN:
			mModelInstance.materials.get(0).set(ColorAttribute.createDiffuse(0.5f, 1.0f, 0.5f, 1.0f));
			mAlphaBlend.opacity = 0.8f;
			break;
		case FADE_RED:
		case ILLEGAL_HOVER:
			mModelInstance.materials.get(0).set(ColorAttribute.createDiffuse(Color.RED));
			mAlphaBlend.opacity = 0.8f;
			break;
		case LEGAL_HOVER:
			mModelInstance.materials.get(0).set(ColorAttribute.createDiffuse(0.5f, 1.0f, 0.5f, 1.0f));
			mAlphaBlend.opacity = 0.8f;
			break;
		case NONE:
			mAlphaBlend.opacity = 0.0f;
		default:
			break;
		}
	}

	/**
	 * Board indexes to their 3D positions in the renderer.
	 * 
	 * @param indexPosition
	 *            Chesspresso notation board location (0 to 63)
	 * @return the provided index converted to its 3D location
	 */
	private Vector3 getGridToPerspectiveTranslation(int indexPosition) {
		int rank = indexPosition % 8;
		int file = indexPosition / 8;

		float x = -7.0f + file * 2.0f;
		float z = -7.0f + rank * 2.0f;

		return new Vector3(x, 0.0f, z);
	}
}
