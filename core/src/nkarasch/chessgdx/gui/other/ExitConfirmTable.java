package nkarasch.chessgdx.gui.other;

import nkarasch.chessgdx.gui.MenuSkin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ExitConfirmTable extends Table {

	/**
	 * Asks the user if they are sure they want to exit.
	 * @param skin
	 */
	public ExitConfirmTable(Skin skin) {
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();
		
		add(new Label("Are you sure?", skin, MenuSkin.LB_LARGE)).fill().colspan(2);
		row().fill();

		TextButton btYes = new TextButton("Yes", skin, "default");
		btYes.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		});
		TextButton btNo = new TextButton("No", skin, "default");
		btNo.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				setVisible(false);
			}
		});

		add(btYes).left();
		add(btNo).left();

		setVisible(false);
		pack();
		setPosition(screenWidth / 2 - getWidth() / 2, screenHeight / 2 - getHeight() / 2);
		background(new TextureRegionDrawable(skin.get(MenuSkin.TEX_LOW_ALPHA_GREY, TextureRegionDrawable.class)));
	}
}
