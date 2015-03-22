package nkarasch.chessgdx.view.renderers;

import nkarasch.chessgdx.GameCore;
import nkarasch.chessgdx.camera.Camera;
import nkarasch.chessgdx.gui.MenuSkin;
import nkarasch.chessgdx.gui.other.CheckMateTable;
import nkarasch.chessgdx.gui.other.EngineThinkingImage;
import nkarasch.chessgdx.gui.other.ExitConfirmTable;
import nkarasch.chessgdx.gui.other.PromotionTable;
import nkarasch.chessgdx.gui.settings.SettingsMenuGroup;
import nkarasch.chessgdx.gui.slider.MoveHistoryTable;
import nkarasch.chessgdx.gui.slider.SlidingMenuGroup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class OverlayRenderer extends Stage {

	private Skin mSkin;
	private PromotionTable mPromotionDialog;
	private CheckMateTable mCheckmateDialog;
	private EngineThinkingImage mEngineThinking;
	private SlidingMenuGroup mSlidingMenu;

	/**
	 * Foundation of the Scene2D based GUI.
	 * 
	 * @param camera
	 * @param core
	 *            root of game, is passed in so GUI elements can trigger
	 *            {@link GameCore#restart()} and
	 *            {@link GameCore#newGame(boolean)}
	 */
	public OverlayRenderer(Camera camera, GameCore core) {

		mSkin = new MenuSkin();
		ExitConfirmTable exitConfirm = new ExitConfirmTable(mSkin);
		SettingsMenuGroup settings = new SettingsMenuGroup(mSkin);
		mSlidingMenu = new SlidingMenuGroup(mSkin, exitConfirm, settings, core);
		mEngineThinking = new EngineThinkingImage(mSkin);
		mPromotionDialog = new PromotionTable(mSkin);
		mCheckmateDialog = new CheckMateTable(mSkin);

		addActor(mPromotionDialog);
		addActor(mCheckmateDialog);
		addActor(settings);
		addActor(mSlidingMenu);
		addActor(mEngineThinking);
		addActor(exitConfirm);
	}

	public void render() {
		act(Gdx.graphics.getDeltaTime());
		draw();
	}

	public void dispose() {
		mSkin.dispose();
		super.dispose();
	}

	/**
	 * @return UI element displaying the move history for both black and white
	 */
	public MoveHistoryTable getMoveHistory() {
		return mSlidingMenu.getMoveHistory();
	}

	/**
	 * @return UI element for letting the user which piece they want to promote
	 *         their pawns to
	 */
	public PromotionTable getPromotionDialog() {
		return mPromotionDialog;
	}

	/**
	 * 
	 * @return UI element informing the user when the board is in a check or
	 *         checkmate state
	 */
	public CheckMateTable getCheckmateDialog() {
		return mCheckmateDialog;
	}

	/**
	 * @return Icon in the bottom left of the screen that rotates when the
	 *         engine is in the process of finding a move
	 */
	public EngineThinkingImage getEngineThinkingImage() {
		return mEngineThinking;
	}
}
