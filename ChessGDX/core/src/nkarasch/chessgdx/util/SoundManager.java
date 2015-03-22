package nkarasch.chessgdx.util;

import nkarasch.chessgdx.GameCore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

	private Music mAmbience;
	private Sound mClicked;
	private Sound mReleased;
	private Sound mMove;

	private float mEffectsVolume;

	// evil anti-pattern strikes again...
	private static SoundManager soundManager;

	/**
	 * @return SoundManager instance, used for playing all audio and managing
	 *         volumes.
	 */
	public static SoundManager getInstance() {
		if (soundManager == null) {
			soundManager = new SoundManager();
		}
		return soundManager;
	}

	private SoundManager() {

		Preferences pref = Gdx.app.getPreferences(GameCore.TITLE);

		mEffectsVolume = 1.0f;
		mAmbience = AssetHandler.getInstance().getMusic("sound/nature.mp3");
		mClicked = AssetHandler.getInstance().getSound("sound/click.wav");
		mReleased = AssetHandler.getInstance().getSound("sound/release.wav");
		mMove = AssetHandler.getInstance().getSound("sound/move.wav");

		if (pref.contains(PreferenceStrings.MUSIC_VOLUME)) {
			mAmbience.setVolume(pref.getFloat(PreferenceStrings.MUSIC_VOLUME) / 100.0f);
		} else {
			pref.putFloat(PreferenceStrings.MUSIC_VOLUME, 1.0f);
		}

		if (pref.contains(PreferenceStrings.EFFECTS_VOLUME)) {
			mEffectsVolume = pref.getFloat(PreferenceStrings.EFFECTS_VOLUME) / 100.0f;
		} else {
			pref.putFloat(PreferenceStrings.EFFECTS_VOLUME, 1.0f);
		}

		mAmbience.setLooping(true);
		mAmbience.play();

		pref.flush();
	}

	/**
	 * chess piece selected
	 */
	public void playClicked() {
		mClicked.play(mEffectsVolume);
	}

	/**
	 * chess piece released
	 */
	public void playReleased() {
		mReleased.play(mEffectsVolume);
	}

	/**
	 * AI move, the clicked and released sounds are actually this split into two
	 * parts
	 */
	public void playMove() {
		mMove.play(mEffectsVolume);
	}

	/**
	 * Adds updated values from the sound settings menu into preferences while
	 * updating them in-game.
	 * 
	 * @param musicVolume
	 *            0 to 100 maximum
	 * @param effectsVolume
	 *            0 to 100 maximum
	 */
	public void updateVolume(float musicVolume, float effectsVolume) {
		Preferences pref = Gdx.app.getPreferences(GameCore.TITLE);
		pref.putFloat(PreferenceStrings.MUSIC_VOLUME, musicVolume);
		pref.putFloat(PreferenceStrings.EFFECTS_VOLUME, effectsVolume);
		mAmbience.setVolume(musicVolume / 100.0f);
		this.mEffectsVolume = effectsVolume / 100.0f;
	}
}
