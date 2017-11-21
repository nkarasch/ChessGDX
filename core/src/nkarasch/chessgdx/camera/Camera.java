package nkarasch.chessgdx.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;

public class Camera extends PerspectiveCamera {

	private final OrbitCamera mFPCameraController;

	/**
	 * Creates 60 degree field of view PerspectiveCamera and instantiates its
	 * controller.
	 */
	public Camera() {
		super(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.mFPCameraController = new OrbitCamera(this);

		update();

		near = 0.1f;
		far = 100.0f;
	}

	/**
	 * @return the camera controller, which implements InputListener
	 */
	public OrbitCamera getOrbitController() {
		return mFPCameraController;
	}
}
