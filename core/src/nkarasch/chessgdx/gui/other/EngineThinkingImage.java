package nkarasch.chessgdx.gui.other;

import nkarasch.chessgdx.gui.MenuSkin;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class EngineThinkingImage extends Image {

	/**
	 * A spinning icon that tells the user the engine is currently processing
	 * the next move.
	 * 
	 * @param skin
	 */
	public EngineThinkingImage(Skin skin) {
		super(skin, MenuSkin.IMG_LOADING_ICON);
		RepeatAction mSpinAction = new RepeatAction();
		mSpinAction.setCount(RepeatAction.FOREVER);
		mSpinAction.setAction(Actions.rotateBy(-360.0f, 1.0f));

		setOrigin(getWidth() / 2, getHeight() / 2);
		addAction(mSpinAction);

		setVisible(false);
	}
}
