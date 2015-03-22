package nkarasch.chessgdx;

import nkarasch.chessgdx.camera.Camera;
import nkarasch.chessgdx.logiccontroller.backend.ChessLogicController;
import nkarasch.chessgdx.logiccontroller.frontend.BoardController;
import nkarasch.chessgdx.util.AssetHandler;
import nkarasch.chessgdx.util.GraphicsSettings;
import nkarasch.chessgdx.view.renderers.OverlayRenderer;
import nkarasch.chessgdx.view.renderers.PerspectiveRenderer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;

public class GameCore implements ApplicationListener {

	public static final String TITLE = "ChessGDX";

	private Camera mCameraController;
	private ChessLogicController mLogicController;
	private BoardController mBoardController;
	private PerspectiveRenderer mPerspectiveRenderer;
	private OverlayRenderer mOverlayRenderer;

	private static Runnable rebootHook;

	/**
	 * @param rebootHook
	 *            game restart hook
	 */
	public GameCore(final Runnable rebootHook) {
		GameCore.rebootHook = rebootHook;
	}

	@Override
	public void create() {
		GraphicsSettings.setGraphics();
		mCameraController = new Camera();
		mOverlayRenderer = new OverlayRenderer(mCameraController, this);
		mLogicController = new ChessLogicController(mOverlayRenderer);
		mBoardController = new BoardController(mCameraController, mLogicController, mOverlayRenderer);
		mPerspectiveRenderer = new PerspectiveRenderer(mCameraController, mBoardController, mLogicController.getPlacement());

		Gdx.input.setInputProcessor(new InputMultiplexer(mOverlayRenderer, mBoardController, mCameraController.getOrbitController()));
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		mCameraController.update();
		mPerspectiveRenderer.render();
		mOverlayRenderer.render();
	}

	@Override
	public void resize(int width, int height) {
		// requiring restart on resize instead, this would be tedious to set up
	}

	@Override
	public void pause() {
		// not supporting mobile
	}

	@Override
	public void resume() {
		// not supporting mobile
	}

	@Override
	public void dispose() {
		AssetHandler.getInstance().dispose();
		mLogicController.dispose();
		mOverlayRenderer.dispose();
	}

	/**
	 * Restarts game client
	 */
	public static void restart() {
		Gdx.app.postRunnable(GameCore.rebootHook);
	}

	/**
	 * Restores board back to the new game state
	 * 
	 * @param isWhite
	 *            player color
	 */
	public void newGame(boolean isWhite) {
		mBoardController.setPlayerColor(isWhite);
		mBoardController.resetList();
		mLogicController.resetPosition();
		mOverlayRenderer.getCheckmateDialog().setState(false, false);
		mOverlayRenderer.getMoveHistory().resetHistory();
	}
}