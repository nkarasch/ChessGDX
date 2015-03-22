package nkarasch.chessgdx.util;

import chesspresso.Chess;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader.ModelParameters;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.FacedCubemapData;
import com.badlogic.gdx.utils.ObjectMap;

public class AssetHandler {

	public static final String MODEL_BOARD = "board";
	public static final String MODEL_TABLE = "table";
	public static final String MODEL_BACKGROUND = "background";
	public static final String MODEL_GRID = "grid";
	
	private static AssetHandler assetHandler;

	/**
	 * @return AssetHandler singleton. This class loads all Texture/Model/Sound
	 *         data into an instance of LibGDX's AssetManager and provides get
	 *         functions for retrieving them.
	 */
	public static AssetHandler getInstance() {
		if (assetHandler == null) {
			assetHandler = new AssetHandler();
		}
		return assetHandler;
	}

	private AssetManager mAssetManager;
	private ObjectMap<Short, Model> mChessPieceModels;
	private ObjectMap<Short, Model> mPieceCollisionModels;
	private ObjectMap<String, Model> mOtherModels;
	private Cubemap mCubemap;

	private AssetHandler() {
		mAssetManager = load();
		mChessPieceModels = createChessPieceModels();
		mPieceCollisionModels = createChessPieceCollisionModels();
		mOtherModels = createOtherModels();
		mCubemap = getCubeMap();
	}

	/**
	 * @param description
	 *            Chesspresso convention chess piece identity
	 * @return ModelInstance of the full quality chess piece matching the
	 *         description, will always be white.
	 */
	public ModelInstance getPieceModel(short description) {
		if (description > 0) {// black piece
			return new ModelInstance(mChessPieceModels.get(description));
		} else if (description < 0) {// white piece
			return new ModelInstance(mChessPieceModels.get((short) (-1 * description)));
		} else {
			return null;
		}
	}

	/**
	 * @param description
	 *            Chesspresso convention chess piece identity
	 * @return ModelInstance of a low poly chess piece matching the description.
	 *         These are used for accurate collision detection without good
	 *         performance.
	 */
	public ModelInstance getPieceCollisionModel(short description) {
		if (description > 0) {// black piece
			return new ModelInstance(mPieceCollisionModels.get(description));
		} else {// white piece
			return new ModelInstance(mPieceCollisionModels.get((short) (-1 * description)));
		}
	}

	/**
	 * Used to get all models that aren't chess pieces.
	 * 
	 * @param description
	 *            model description, not used for chess pieces
	 * @return ModelInstance of specified model
	 */
	public ModelInstance getOtherModel(String description) {
		return new ModelInstance(mOtherModels.get(description));
	}

	/**
	 * @param path
	 *            to texture
	 * @return Texture instance of asset located at the specified path
	 */
	public Texture getTexture(String path) {
		return mAssetManager.get(path, Texture.class);
	}

	/**
	 * @param path
	 *            to Wav
	 * @return Sound instance of asset located at the specified path
	 */
	public Sound getSound(String path) {
		return mAssetManager.get(path, Sound.class);
	}

	/**
	 * @param path
	 *            to mp3
	 * @return Music instance of asset located at the specified path
	 */
	public Music getMusic(String path) {
		return mAssetManager.get(path, Music.class);
	}

	/**
	 * @return Cubemap used for the environmental cubemap
	 */
	public Cubemap getCubeMap() {
		if (mCubemap == null) {
			Texture back = mAssetManager.get("3D/cubemap/background_back.jpg", Texture.class);
			Texture front = mAssetManager.get("3D/cubemap/background_front.jpg", Texture.class);
			Texture up = mAssetManager.get("3D/cubemap/background_up.jpg", Texture.class);
			Texture down = mAssetManager.get("3D/cubemap/background_down.jpg", Texture.class);
			Texture left = mAssetManager.get("3D/cubemap/background_left.jpg", Texture.class);
			Texture right = mAssetManager.get("3D/cubemap/background_right.jpg", Texture.class);

			FacedCubemapData data = new FacedCubemapData(right.getTextureData(), left.getTextureData(), up.getTextureData(),
					down.getTextureData(), front.getTextureData(), back.getTextureData());
			return new Cubemap(data);
		} else {
			return mCubemap;
		}
	}

	/**
	 * @return AssetManager instance with every file used in the game loaded.
	 */
	private AssetManager load() {

		AssetManager assetManager = new AssetManager();

		ModelParameters boardAttributes = new ModelParameters();
		boardAttributes.textureParameter.genMipMaps = true;
		boardAttributes.textureParameter.minFilter = TextureFilter.MipMapLinearLinear;
		boardAttributes.textureParameter.magFilter = TextureFilter.MipMapLinearLinear;

		// "other" models
		assetManager.load("3D/models/board.g3db", Model.class, boardAttributes);
		assetManager.load("3D/models/grid.g3db", Model.class);
		assetManager.load("3D/models/table.g3db", Model.class);
		assetManager.load("3D/models/background.g3db", Model.class);

		// full quality chess piece models
		assetManager.load("3D/models/pawn-full.g3db", Model.class);
		assetManager.load("3D/models/rook-full.g3db", Model.class);
		assetManager.load("3D/models/knight-full.g3db", Model.class);
		assetManager.load("3D/models/bishop-full.g3db", Model.class);
		assetManager.load("3D/models/king-full.g3db", Model.class);
		assetManager.load("3D/models/queen-full.g3db", Model.class);

		// low poly count models for collisions
		assetManager.load("3D/models/pawn-reduced.g3db", Model.class);
		assetManager.load("3D/models/rook-reduced.g3db", Model.class);
		assetManager.load("3D/models/knight-reduced.g3db", Model.class);
		assetManager.load("3D/models/bishop-reduced.g3db", Model.class);
		assetManager.load("3D/models/king-reduced.g3db", Model.class);
		assetManager.load("3D/models/queen-reduced.g3db", Model.class);

		// cubemap face textures
		assetManager.load("3D/cubemap/background_back.jpg", Texture.class);
		assetManager.load("3D/cubemap/background_front.jpg", Texture.class);
		assetManager.load("3D/cubemap/background_up.jpg", Texture.class);
		assetManager.load("3D/cubemap/background_down.jpg", Texture.class);
		assetManager.load("3D/cubemap/background_left.jpg", Texture.class);
		assetManager.load("3D/cubemap/background_right.jpg", Texture.class);

		// promotion GUI textures
		assetManager.load("UI/bishop-silhouette.png", Texture.class);
		assetManager.load("UI/queen-silhouette.png", Texture.class);
		assetManager.load("UI/rook-silhouette.png", Texture.class);
		assetManager.load("UI/knight-silhouette.png", Texture.class);

		// GUI elements
		assetManager.load("UI/button-up.jpg", Texture.class);
		assetManager.load("UI/button-down.jpg", Texture.class);
		assetManager.load("UI/checkbox-checked.jpg", Texture.class);
		assetManager.load("UI/checkbox-unchecked.jpg", Texture.class);
		assetManager.load("UI/exit.jpg", Texture.class);
		assetManager.load("UI/settings-clicked.jpg", Texture.class);
		assetManager.load("UI/settings-unclicked.jpg", Texture.class);
		assetManager.load("UI/slide-clicked.jpg", Texture.class);
		assetManager.load("UI/slide-unclicked.jpg", Texture.class);
		assetManager.load("UI/text-cursor.png", Texture.class);
		assetManager.load("UI/slider-knob.png", Texture.class);
		assetManager.load("UI/loading.png", Texture.class);

		// audio
		assetManager.load("sound/nature.mp3", Music.class);
		assetManager.load("sound/click.wav", Sound.class);
		assetManager.load("sound/move.wav", Sound.class);
		assetManager.load("sound/release.wav", Sound.class);

		assetManager.finishLoading();

		return assetManager;
	}

	/**
	 * @return ObjectMap of all full quality chess piece models, using
	 *         Chesspresso short notation for keys
	 */
	private ObjectMap<Short, Model> createChessPieceModels() {
		ObjectMap<Short, Model> chessPieceModels = new ObjectMap<Short, Model>();
		chessPieceModels.put(Chess.PAWN, mAssetManager.get("3D/models/pawn-full.g3db", Model.class));
		chessPieceModels.put(Chess.ROOK, mAssetManager.get("3D/models/rook-full.g3db", Model.class));
		chessPieceModels.put(Chess.KNIGHT, mAssetManager.get("3D/models/knight-full.g3db", Model.class));
		chessPieceModels.put(Chess.BISHOP, mAssetManager.get("3D/models/bishop-full.g3db", Model.class));
		chessPieceModels.put(Chess.KING, mAssetManager.get("3D/models/king-full.g3db", Model.class));
		chessPieceModels.put(Chess.QUEEN, mAssetManager.get("3D/models/queen-full.g3db", Model.class));
		return chessPieceModels;
	}

	/**
	 * @return ObjectMap of all low poly chess piece models, using Chesspresso
	 *         short notation for keys
	 */
	private ObjectMap<Short, Model> createChessPieceCollisionModels() {
		ObjectMap<Short, Model> pieceCollisionModels = new ObjectMap<Short, Model>();
		pieceCollisionModels.put(Chess.PAWN, mAssetManager.get("3D/models/pawn-reduced.g3db", Model.class));
		pieceCollisionModels.put(Chess.ROOK, mAssetManager.get("3D/models/rook-reduced.g3db", Model.class));
		pieceCollisionModels.put(Chess.KNIGHT, mAssetManager.get("3D/models/knight-reduced.g3db", Model.class));
		pieceCollisionModels.put(Chess.BISHOP, mAssetManager.get("3D/models/bishop-reduced.g3db", Model.class));
		pieceCollisionModels.put(Chess.KING, mAssetManager.get("3D/models/king-reduced.g3db", Model.class));
		pieceCollisionModels.put(Chess.QUEEN, mAssetManager.get("3D/models/queen-reduced.g3db", Model.class));
		return pieceCollisionModels;
	}

	/**
	 * @return ObjectMap of all models that aren't chess pieces
	 */
	private ObjectMap<String, Model> createOtherModels() {
		ObjectMap<String, Model> otherModels = new ObjectMap<String, Model>();
		otherModels.put(MODEL_BOARD, mAssetManager.get("3D/models/board.g3db", Model.class));
		otherModels.put(MODEL_GRID, mAssetManager.get("3D/models/grid.g3db", Model.class));
		otherModels.put(MODEL_TABLE, mAssetManager.get("3D/models/table.g3db", Model.class));
		otherModels.put(MODEL_BACKGROUND, mAssetManager.get("3D/models/background.g3db", Model.class));
		return otherModels;
	}

	/**
	 * Dispose of all assets stored in the AssetManager and Cubemap
	 */
	public void dispose() {
		mAssetManager.dispose();
		mCubemap.dispose();
	}
}
