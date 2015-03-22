package nkarasch.chessgdx.logiccontroller.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import nkarasch.chessgdx.GameCore;
import nkarasch.chessgdx.util.PreferenceStrings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Adapted from Rahul A R's "JavaStockfish".
 * https://github.com/rahular/chess-misc
 * /blob/master/JavaStockfish/src/com/rahul/stockfish/Stockfish.java
 * 
 * I've changed it to make best move searches non-blocking so the entire game
 * doesn't halt while the engine is calculating. There is room for improvement.
 */

// TODO make engine process shut down properly after validateEngine is called
public class UCIEngineInterface {

	private ExecutorService mService;
	private Future<String> mTask;
	private Process mEngineProcess;
	private ProcessBuilder mProcessBuilder;
	private BufferedReader mProcessReader;
	private OutputStreamWriter mProcessWriter;
	private String mFEN;
	private String mSearchType;
	private int mSearchValue;

	UCIEngineInterface(String PATH) {
		mProcessBuilder = new ProcessBuilder(PATH);
		mService = Executors.newFixedThreadPool(1);
		startEngine();
		mTask = null;

		Preferences pref = Gdx.app.getPreferences(GameCore.TITLE);
		if (pref.getBoolean(PreferenceStrings.DEPTH_SEARCH)) {
			mSearchType = "depth";
			mSearchValue = pref.getInteger(PreferenceStrings.SEARCH_VALUE);
		} else {
			mSearchType = "movetime";
			// seconds to milliseconds
			mSearchValue = pref.getInteger(PreferenceStrings.SEARCH_VALUE) * 1000;
		}
	}

	public static boolean validateEngine(String PATH) {
		UCIEngineInterface uci = new UCIEngineInterface(PATH);
		if (uci.startEngine()) {
			uci.getOutput(100);
			uci.stopEngine();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Starts UCI engine process
	 * 
	 * @return True on success. False otherwise
	 */
	private boolean startEngine() {
		try {
			mEngineProcess = mProcessBuilder.start();
			mProcessReader = new BufferedReader(new InputStreamReader(mEngineProcess.getInputStream()));
			mProcessWriter = new OutputStreamWriter(mEngineProcess.getOutputStream());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Takes in any valid UCI command and executes it
	 * 
	 * @param command
	 *            - a UCI command
	 */
	public void sendCommand(String command) {
		try {
			if (mProcessWriter != null) {
				mProcessWriter.write(command + "\n");
				mProcessWriter.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This is generally called right after 'sendCommand' for getting the raw
	 * output from Stockfish
	 * 
	 * @param waitTime
	 *            Time in milliseconds for which the function waits before
	 *            reading the output. Useful when a long running command is
	 *            executed
	 * @return Raw output from Stockfish
	 */
	private String getOutput(int waitTime) {
		StringBuffer buffer = new StringBuffer();
		try {
			Thread.sleep(waitTime);
			sendCommand("isready");
			while (true) {
				String text = mProcessReader.readLine();
				if (text.equals("readyok"))
					break;
				else
					buffer.append(text + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	/**
	 * This function returns the best move for a given position after
	 * calculating for 'waitTime' ms
	 * 
	 * @param FEN
	 *            Position string
	 * @return Best Move in PGN format
	 */
	String getBestMove(String FEN) {

		this.mFEN = FEN;

		if (mTask == null) {
			mTask = mService.submit(new GetBestMoveCallable());
			return null;
		} else {
			if (mTask.isDone()) {
				try {
					String result = mTask.get();
					mTask = null;
					return result;
				} catch (InterruptedException e) {
					e.printStackTrace();
					return null;
				} catch (ExecutionException e) {
					e.printStackTrace();
					return null;
				}
			} else {
				return null;
			}
		}

	}

	/**
	 * Stops Stockfish and cleans up before closing it
	 */
	void stopEngine() {
		try {

			sendCommand("quit");
			
			if (mProcessReader != null) {
				mProcessReader.close();
			}

			if (mProcessWriter != null) {
				mProcessWriter.close();
			}
			if (mEngineProcess != null) {
				mEngineProcess.destroy();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class GetBestMoveCallable implements Callable<String> {

		@Override
		public String call() {

			sendCommand("position fen " + mFEN);
			sendCommand("go " + mSearchType + " " + mSearchValue);
			String bestmove = null;
			try {
				// sendCommand("isready");
				while (bestmove == null && mProcessReader != null) {
					String out = mProcessReader.readLine();
					if (out != null) {
						String output[] = out.split(" ");
						if (output[0].equals("bestmove")) {
							bestmove = output[1];
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return bestmove;
		}
	}
}
