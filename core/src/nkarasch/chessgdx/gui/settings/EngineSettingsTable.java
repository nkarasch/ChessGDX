package nkarasch.chessgdx.gui.settings;

import nkarasch.chessgdx.GameCore;
import nkarasch.chessgdx.gui.MenuSkin;
import nkarasch.chessgdx.logiccontroller.backend.UCIEngineInterface;
import nkarasch.chessgdx.util.PreferenceStrings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

class EngineSettingsTable extends Table {

	/**
	 * Dialog to let the user enter the path to their engine, whether they want
	 * it to operate on best move or time, and how deep/how long to search.
	 * 
	 * @param skin
	 */
	EngineSettingsTable(Skin skin) {

		Integer[] depthOrTime = new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,
				26, 27, 28, 29, 30 };
		final String[] searchStyles = new String[] { " Depth   ", " Time   " };
		final Preferences pref = Gdx.app.getPreferences(GameCore.TITLE);

		final TextField enginePath = new TextField("Stockfish6/Windows/stockfish-6-64.exe", skin);
		final List<String> searchStyle = new List<String>(skin);
		final SelectBox<Integer> depthOrTimeSelector = new SelectBox<Integer>(skin);
		final Label searchLabel = new Label("Search Time ", skin);
		TextButton tbCancel = new TextButton("Cancel", skin);
		TextButton tbAccept = new TextButton("Accept? (requires restart)", skin);

		// error display
		final Label statusLabel1 = new Label("Engine path cannot be validated. Make", skin);
		final Label statusLabel2 = new Label("sure you are using the full path to a UCI", skin);
		final Label statusLabel3 = new Label("protocol compliant chess engine executable.", skin);

		add(new Label("Engine Settings", skin, MenuSkin.LB_LARGE)).colspan(2).center();
		row().fill();
		add(new Label("Engine Path  ", skin)).left();
		add(enginePath).left();

		row().fill();
		add(new Label("Search Style", skin)).left();
		add(searchStyle).left();

		row().fill();
		add(searchLabel).left();
		add(depthOrTimeSelector).left();

		row().fill();
		add(tbCancel).left();
		add(tbAccept).left();

		row().fill();
		add(statusLabel1).colspan(2).fillX();

		row().fill();
		add(statusLabel2).colspan(2).fillX();

		row().fill();
		add(statusLabel3).colspan(2).fillX();

		// set to default to Stockfish6, will validate its location and not
		// bring up the engine settings menu if successful
		if (!pref.contains(PreferenceStrings.ENGINE_PATH)) {
			pref.putString(PreferenceStrings.ENGINE_PATH, "Stockfish6/Windows/stockfish-6-64.exe");
			pref.flush();
		}

		if (!UCIEngineInterface.validateEngine(pref.getString(PreferenceStrings.ENGINE_PATH))) {
			setVisible(true);
			enginePath.setText("enter path to engine");
		} else {
			setVisible(false);
			enginePath.setText(pref.getString(PreferenceStrings.ENGINE_PATH));
		}

		statusLabel1.setVisible(false);
		statusLabel2.setVisible(false);
		statusLabel3.setVisible(false);

		searchStyle.setItems(searchStyles);

		depthOrTimeSelector.setItems(depthOrTime);
		depthOrTimeSelector.setSelected(pref.getInteger(PreferenceStrings.SEARCH_VALUE));

		searchStyle.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (searchStyle.getSelected().equals(searchStyles[0])) {
					searchLabel.setText("Search Depth (moves)  ");
				} else {
					searchLabel.setText("Search Time (seconds) ");
				}
			}
		});

		if (pref.getBoolean(PreferenceStrings.DEPTH_SEARCH)) {
			searchStyle.setSelected(searchStyles[0]);
			searchLabel.setText("Search Depth (moves)  ");
		} else {
			searchStyle.setSelected(searchStyles[1]);
			searchLabel.setText("Search Time (seconds) ");
		}

		tbAccept.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {

				if (UCIEngineInterface.validateEngine(enginePath.getText())) {
					String selected = searchStyle.getSelected();
					pref.putBoolean(PreferenceStrings.DEPTH_SEARCH, selected.equals(searchStyles[0]));
					pref.putString(PreferenceStrings.ENGINE_PATH, enginePath.getText());
					pref.putInteger(PreferenceStrings.SEARCH_VALUE, depthOrTimeSelector.getSelected());
					pref.flush();
					setVisible(false);
					GameCore.restart();
				} else {
					statusLabel1.setVisible(true);
					statusLabel2.setVisible(true);
					statusLabel3.setVisible(true);
				}
			}
		});

		tbCancel.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (UCIEngineInterface.validateEngine(enginePath.getText())) {
					setVisible(false);
				} else {
					Gdx.app.exit();
				}
			}
		});

		background(skin.get(MenuSkin.TEX_LOW_ALPHA_GREY, TextureRegionDrawable.class));
		pack();

		setPosition(Gdx.graphics.getWidth() / 2 - getWidth() / 2, Gdx.graphics.getHeight() / 2 - getHeight() / 2);

		String currentPath = Gdx.app.getPreferences(GameCore.TITLE).getString("engine_path");
		if (currentPath != null && !currentPath.equals("")) {
			if (UCIEngineInterface.validateEngine(currentPath)) {
				setVisible(false);
			} else {
				setVisible(true);
			}
		} else {
			setVisible(true);
		}
	}
}
