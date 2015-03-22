package nkarasch.chessgdx.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

class OrbitCamera extends InputAdapter {

	private final Camera mCamera;
	private final Vector3 mTmp;
	private boolean mRightMousePressed;

	private float mSensitivity;
	private float mZoomSpeed;
	private float mZoomMin;
	private float mZoomMax;

	/**
	 * A camera controller that always looks toward (0,0,0) and rotates around
	 * it when the right mouse button is clicked. Also has preset camera
	 * positions for pressed of the number keys 1-6. Extends InputAdapter.
	 * 
	 * @param camera
	 *            a PerspectiveCamera instance
	 */
	OrbitCamera(Camera camera) {
		this.mCamera = camera;

		mTmp = new Vector3();
		mSensitivity = 0.5f;
		mZoomSpeed = 0.25f;
		mZoomMin = 3.0f;
		mZoomMax = 22.0f;

		camera.position.set(-1.0f, 20.0f, 0.0f);
		camera.lookAt(0.0f, 0.0f, 0.0f);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		if (mRightMousePressed) {
			float deltaX = -Gdx.input.getDeltaX() * mSensitivity;
			float deltaY = -Gdx.input.getDeltaY() * mSensitivity;

			mTmp.set(mCamera.up);
			mCamera.position.rotate(mTmp, deltaX);
			mTmp.set(mCamera.direction).crs(mCamera.up).nor();

			if (deltaY < 0 && mCamera.direction.y < -0.99f) {
				deltaY = 0.01f;
			}

			if (deltaY > 0 && mCamera.direction.y > -0.2f) {
				deltaY = -0.01f;
			}

			mCamera.position.rotate(mTmp, deltaY);
			mCamera.up.set(0.0f, 1.0f, 0.0f);
			mCamera.lookAt(0.0f, 0.0f, 0.0f);

		}

		return false;
	}

	@Override
	public boolean scrolled(int amount) {

		float distance = mCamera.position.dst(mTmp);

		if ((distance < mZoomMax + (-amount * mZoomSpeed)) && (distance > mZoomMin + (-amount * mZoomSpeed))) {
			mTmp.set(mCamera.direction).nor().scl(-amount * mZoomSpeed);
			mCamera.position.add(mTmp);
			mTmp.set(0.0f, 0.0f, 0.0f);
		}
		mCamera.lookAt(0.0f, 0.0f, 0.0f);
		return super.scrolled(amount);
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.NUM_1) {
			mCamera.position.set(-1.0f, 20.0f, 0.0f);
		}

		if (keycode == Input.Keys.NUM_2) {
			mCamera.position.set(1.0f, 20.0f, 0.0f);
		}

		if (keycode == Input.Keys.NUM_3) {
			mCamera.position.set(-11.0f, 18.0f, 0.0f);
		}

		if (keycode == Input.Keys.NUM_4) {
			mCamera.position.set(11.0f, 18.0f, 0.0f);
		}

		if (keycode == Input.Keys.NUM_5) {
			mCamera.position.set(0.0f, 3.0f, -20.0f);
		}

		if (keycode == Input.Keys.NUM_6) {
			mCamera.position.set(0.0f, 3.0f, 20.0f);
		}

		mCamera.up.set(Vector3.Y);
		mCamera.lookAt(0.0f, 0.0f, 0.0f);
		return super.keyDown(keycode);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.RIGHT) {
			mRightMousePressed = true;
		}

		return super.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		mRightMousePressed = false;
		return super.touchUp(screenX, screenY, pointer, button);
	}
}
