package nkarasch.chessgdx.desktop;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nkarasch.chessgdx.GameCore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] arg) {
		final LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "ChessGDX";
		cfg.width = 1920;
		cfg.height = 1080;
		cfg.backgroundFPS = 30;
		cfg.foregroundFPS = 0;
		cfg.vSyncEnabled = false;
		cfg.samples = 16;
		cfg.resizable = false;

		final Runnable rebootable = new Runnable() {
			@Override
			public void run() {
				if (Gdx.app != null) {
					Gdx.app.exit();
				}
				DesktopLauncher.restart(DesktopLauncher.class, new String[] {});
			}
		};

		new LwjglApplication(new GameCore(rebootable), cfg);
	}

	public static void restart(Class<?> mainClass, String... parameters) {
		List<String> cmdLine = new ArrayList<String>();

		String jvmHome = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
		List<String> jvmArgs = ManagementFactory.getRuntimeMXBean().getInputArguments();
		String classPath = ManagementFactory.getRuntimeMXBean().getClassPath();

		cmdLine.add(jvmHome);

		for (String jvmArg : jvmArgs) {
			if (!jvmArg.contains("-agentlib")) {
				cmdLine.add(jvmArg);
			}
		}

		cmdLine.add("-cp");
		cmdLine.add(classPath);

		cmdLine.add(mainClass.getName());

		cmdLine.addAll(Arrays.asList(parameters));
		
		try {
			Runtime.getRuntime().exec(cmdLine.toArray(new String[cmdLine.size()]));
		} catch (Exception e) {
			System.out.println("Failed to execute command line.");
			e.printStackTrace();
		}

		System.exit(0);
	}
}
