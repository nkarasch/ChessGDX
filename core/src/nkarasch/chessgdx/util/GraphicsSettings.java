package nkarasch.chessgdx.util;

import java.util.Iterator;

import nkarasch.chessgdx.GameCore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

public class GraphicsSettings {

	private final Array<CustomDisplayMode> mDisplayModes;

	/**
	 * Used by GraphicsSettingsTable for presenting display mode options and
	 * changing them.
	 */
	public GraphicsSettings() {

		mDisplayModes = new Array<CustomDisplayMode>();
		
		for (int i = 0; i < Gdx.graphics.getDisplayModes().length; i++) {
			mDisplayModes.add(new CustomDisplayMode(Gdx.graphics.getDisplayModes()[i]));
		}

		// find the maximum refresh rates and color depth supported
		int maxFrequency = 0, maxBPP = 0;
		for (CustomDisplayMode m : mDisplayModes) {
			if (m.refreshRate > maxFrequency) {
				maxFrequency = m.refreshRate;
			}
			if (m.bitsPerPixel > maxBPP) {
				maxBPP = m.bitsPerPixel;
			}
		}

		// filtering out display modes that are of lower refresh rates or color
		// depth, no need for duplicates in list the user sees
		Iterator<CustomDisplayMode> iterator = mDisplayModes.iterator();
		while (iterator.hasNext()) {
			CustomDisplayMode mode = iterator.next();
			if (mode.refreshRate != maxFrequency || mode.bitsPerPixel != maxBPP) {
				iterator.remove();
			}

		}

		// low width to high width
		mDisplayModes.sort();
	}

	/**
	 * @return a CustomDisplayMode instance representing the starting screen
	 *         width and screen height
	 */
	public CustomDisplayMode getLaunchDisplayMode() {
		for (CustomDisplayMode m : mDisplayModes) {
			if (m.width == Gdx.graphics.getWidth() && m.height == Gdx.graphics.getHeight()) {
				return m;
			}
		}

		return mDisplayModes.get(0);
	}

	/**
	 * @return all available display modes that aren't using lower refresh rates
	 *         or color depth
	 */
	public Array<CustomDisplayMode> getDisplayModes() {
		return mDisplayModes;
	}

	/**
	 * Sets the window display mode to match the options set in preferences in
	 * preferences. This is called at launch.
	 */
	public static void setGraphics() {
		Preferences pref = Gdx.app.getPreferences(GameCore.TITLE);
		int width = pref.getInteger(PreferenceStrings.DISPLAY_WIDTH);
		int height = pref.getInteger(PreferenceStrings.DISPLAY_HEIGHT);
		boolean fullscreen = pref.getBoolean(PreferenceStrings.FULLSCREEN);
		boolean vSync = pref.getBoolean(PreferenceStrings.VSYNC);
		if (width != 0 && height != 0) {
            if(fullscreen) {
                DisplayMode usable = Gdx.graphics.getDisplayMode();
                for(DisplayMode displayMode : Gdx.graphics.getDisplayModes()){
                    if(displayMode.width == width && displayMode.height == height){
                        usable = displayMode;
                        break;
                    }
                }
                Gdx.graphics.setFullscreenMode(usable);
            } else {
                Gdx.graphics.setWindowedMode(width, height);
            }
            Gdx.graphics.setVSync(vSync);
		} else {
            if(fullscreen) {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            } else {
                Gdx.graphics.setWindowedMode(1280, 720);
            }
            Gdx.graphics.setVSync(vSync);
		}
	}

	public class CustomDisplayMode extends DisplayMode implements Comparable<CustomDisplayMode> {

		/**
		 * @param displayMode
		 *            LibGDX base DisplayMode
		 */
		private CustomDisplayMode(DisplayMode displayMode) {
			super(displayMode.width, displayMode.height, displayMode.refreshRate, displayMode.bitsPerPixel);
		}

		@Override
		public int compareTo(CustomDisplayMode dm) {
			if (width > dm.width) {
				return 1;
			}

			if (width == dm.width) {
				return 0;
			}

			if (width < dm.width) {
				return -1;
			}
			return 0;
		}

		public String toString() {
			return width + " x " + height;
		}
	}
}
