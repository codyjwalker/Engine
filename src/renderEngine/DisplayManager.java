package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

/*
 * File:	DisplayManager.java
 * Purpose:	Responsible for managing the display.
 */
public class DisplayManager {

	private static final int WIDTH = 2560;
	private static final int HEIGHT = 1440;
	private static final int FPS_CAP = 120;

	private static long lastFrameTime;
	private static float delta;

	// Opens display upon starting of the engine.
	public static void createDisplay() {

		// Set attributes required for creating the display.
		ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);

		try {
			// Set size & location of the display.
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setLocation(0, 0);
			// Create the display.
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("A Very Nice Display!");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		// Tell OpenGL to render the game on the whole display.
		GL11.glViewport(0, 0, WIDTH, HEIGHT);

		// Initialize global upon creation of display.
		lastFrameTime = getCurrentTime();
	}

	// Updates the display each frame.
	public static void updateDisplay() {
		// Tell engine to run at set FPS count.
		Display.sync(FPS_CAP);
		Display.update();
		long currFrameTime = getCurrentTime();
		delta = (currFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currFrameTime;
	}

	public static float getFrameTimeSeconds() {
		return delta;
	}

	// Closes the display upon exiting.
	public static void closeDisplay() {

		Display.destroy();
	}

	// Gets the current time in miliseconds by dividing ticks by #ticks per second.
	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}

}
