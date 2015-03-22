package nkarasch.chessgdx.view.renderers;

import nkarasch.chessgdx.camera.Camera;
import nkarasch.chessgdx.logiccontroller.frontend.BoardController;
import nkarasch.chessgdx.logiccontroller.frontend.ChessPieceGraveyard;
import nkarasch.chessgdx.perspectiveview.gameobjects.ChessPiece;
import nkarasch.chessgdx.perspectiveview.gameobjects.GridSpaceHighlighter;
import nkarasch.chessgdx.perspectiveview.gameobjects.StaticObject;
import nkarasch.chessgdx.util.AssetHandler;
import nkarasch.chessgdx.view.superclasses.ABaseObject;
import chesspresso.Chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.CubemapAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class PerspectiveRenderer {

	private ModelBatch mShadowBatch;
	private ModelBatch mCubeMapBatch;
	private ModelBatch mBackgroundBatch;
	private Environment mEnvironment;
	private Environment mBackgroundEnvironment;
	private Array<ABaseObject> mDrawables;
	private DirectionalShadowLight mShadowLight;
	private StaticObject mBackground;
	private BoardController mBoardController;
	private ChessPieceGraveyard mGraveyard;
	private Vector3 mLightDirection;

	private Camera mCamera;

	/**
	 * Renderer for all visible 3D components.
	 * 
	 * @param camera
	 * @param boardController
	 *            handles interactions between the behind the scenes logic and
	 *            what is drawn
	 * @param piecePlacement
	 *            array of 64 shorts defining the contents of all 64 board
	 *            locations
	 */
	public PerspectiveRenderer(Camera camera, BoardController boardController, short[] piecePlacement) {

		mCamera = camera;
		mBoardController = boardController;

		mShadowBatch = new ModelBatch(new DepthShaderProvider());
		mCubeMapBatch = new ModelBatch(Gdx.files.internal("shaders/cubevertex.glsl"), Gdx.files.internal("shaders/cubefragment.glsl"));
		mBackgroundBatch = new ModelBatch();
		mEnvironment = new Environment();
		mBackgroundEnvironment = new Environment();
		mDrawables = new Array<ABaseObject>();
		mGraveyard = new ChessPieceGraveyard();
		mLightDirection = new Vector3(11.0f, -9.0f, 11.0f);

		configureEnvironments();
		createGameObjects(piecePlacement);
	}

	/**
	 * Configures the lighting and attributes for the environments
	 * 
	 * @param assetHandler
	 */
	private void configureEnvironments() {
		mEnvironment.set(new CubemapAttribute(CubemapAttribute.EnvironmentMap, AssetHandler.getInstance().getCubeMap()),
				new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 0.5f));

		mEnvironment.add((mShadowLight = new DirectionalShadowLight(4096, 4096, 60f, 60f, .1f, 50f)).set(0.5f, 0.5f, 0.5f,
				mLightDirection.x, mLightDirection.y, mLightDirection.z));
		mEnvironment.shadowMap = mShadowLight;

		mBackgroundEnvironment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1.0f, 1.0f, 1.0f, 1f));
	}

	/**
	 * Creates all 3D objects and adds them to the drawable list to be used in
	 * rendering.
	 * 
	 * @param assetHandler
	 * @param piecePlacement
	 *            array of 64 shorts defining the contents of all 64 board
	 *            locations
	 */
	private void createGameObjects(short[] piecePlacement) {
		AssetHandler assetHandler = AssetHandler.getInstance();
		mDrawables.add(new StaticObject(assetHandler.getOtherModel(AssetHandler.MODEL_BOARD), new Vector3(0.0f, -0.41f, 0.0f),
				ColorAttribute.createReflection(0.3f, 0.3f, 0.3f, 1.0f)));
		mDrawables.add(new StaticObject(assetHandler.getOtherModel(AssetHandler.MODEL_TABLE), new Vector3(0.0f, -15.0f, 0.0f),
				ColorAttribute.createSpecular(0.1f, 0.1f, 0.1f, 1.0f)));
		mBackground = new StaticObject(assetHandler.getOtherModel(AssetHandler.MODEL_BACKGROUND), new Vector3(-25.0f, 10f, 10.0f),
				Vector3.Y, -90.0f);

		// loop through all 64 board locations, creating a grid highlighter for
		// each one and chess pieces where applicable
		for (int i = 0; i < piecePlacement.length; i++) {
			ChessPiece chessPiece = null;
			if (piecePlacement[i] != Chess.NO_PIECE) {
				chessPiece = new ChessPiece(assetHandler.getPieceModel(piecePlacement[i]),
						assetHandler.getPieceCollisionModel(piecePlacement[i]), piecePlacement[i], i,
						mGraveyard.getNextLocation(piecePlacement[i]));
				mDrawables.add(chessPiece);
			}

			GridSpaceHighlighter boardGrid = new GridSpaceHighlighter(assetHandler.getOtherModel(AssetHandler.MODEL_GRID), i);
			mDrawables.add(boardGrid);

			mBoardController.registerPair(i, boardGrid, chessPiece);
		}
	}

	/**
	 * Calls update on every game object and starts both render passes.
	 */
	public void render() {

		// update
		mBoardController.update();
		for (ABaseObject object : mDrawables) {
			object.update(Gdx.graphics.getDeltaTime());
		}

		// draw shadow maps
		mShadowLight.begin(Vector3.Zero, mLightDirection);
		mShadowBatch.begin(mShadowLight.getCamera());
		for (ABaseObject object : mDrawables) {
			object.render(mShadowBatch, mEnvironment);
		}
		mShadowBatch.end();
		mShadowLight.end();

		// main draw
		mCubeMapBatch.begin(mCamera);
		for (ABaseObject object : mDrawables) {
			object.render(mCubeMapBatch, mEnvironment);
		}
		mCubeMapBatch.end();

		// draw background sphere
		mBackgroundBatch.begin(mCamera);
		mBackground.render(mBackgroundBatch, mBackgroundEnvironment);
		mBackgroundBatch.end();
	}
}
