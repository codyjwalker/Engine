package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

/*
 * File:	MainGameLoop.java
 * Purpose:	Contains the main() method, inside of which the "infinite"
 * 			game loop is contained.
 */
public class MainGameLoop {

	private static Loader loader;
	private static RawModel rawModel;
	private static ModelTexture texture, modelTexture;
	private static TexturedModel dragon;
	private static List<Entity> entities;
	private static Light light;
	private static Camera camera;
	private static MasterRenderer renderer;
	private static Player player;

	private static Terrain terrain, terrain2;
	private static TerrainTexture backgroundTexture, rTexture, gTexture, bTexture, blendMap;
	private static TerrainTexturePack texturePack;

	private static TexturedModel grass;
	private static TexturedModel fern;
	private static TexturedModel bush0, bush1, bush2, tree0;

	// Initializes all objects needed for rendering.
	private static void init() {
		// Open up the display.
		DisplayManager.createDisplay();

		// Create Loader.
		loader = new Loader();

		// TODO: NOTE: ep. 16 Fog: Use of new OBJLoader

		// Load up RawModel & ModelTexture, create texturedModel, & extract its texture.
		rawModel = OBJLoader.loadObjModel("DragonBlender", loader);
		texture = new ModelTexture(loader.loadTexture("white"));
		dragon = new TexturedModel(rawModel, texture);
		modelTexture = dragon.getTexture();

		// Set damper and reflectivity values.
		modelTexture.setShineDamper(10);
		modelTexture.setReflectivity(1);

		// Some nice models.
		grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
				new ModelTexture(loader.loadTexture("grassTexture")));
		grass.getTexture().setTransparancy(true);
		grass.getTexture().setUseFakeLighting(true);
		fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), new ModelTexture(loader.loadTexture("fern")));
		fern.getTexture().setTransparancy(true);

		bush0 = new TexturedModel(OBJLoader.loadObjModel("simpleBush0", loader),
				new ModelTexture(loader.loadTexture("tree")));
		bush1 = new TexturedModel(OBJLoader.loadObjModel("simpleBush1", loader),
				new ModelTexture(loader.loadTexture("tree")));
		bush2 = new TexturedModel(OBJLoader.loadObjModel("simpleBush2", loader),
				new ModelTexture(loader.loadTexture("tree")));
//		rock0 = new TexturedModel(OBJLoader.loadObjModel("simpleRock0", loader),
//				new ModelTexture(loader.loadTexture("tree")));
//		rock1 = new TexturedModel(OBJLoader.loadObjModel("simpleRock1", loader),
//				new ModelTexture(loader.loadTexture("tree")));
		tree0 = new TexturedModel(OBJLoader.loadObjModel("simpleTree0", loader),
				new ModelTexture(loader.loadTexture("tree")));
//		tree1 = new TexturedModel(OBJLoader.loadObjModel("simpleTree1", loader),
//				new ModelTexture(loader.loadTexture("tree")));
//		tree2 = new TexturedModel(OBJLoader.loadObjModel("simpleTree2", loader),
//				new ModelTexture(loader.loadTexture("tree")));

		// Create Entity with TexturedModel.
		entities = new ArrayList<Entity>();
		Random random = new Random();
		for (int i = 0; i < 500; i++) {
			entities.add(new Entity(grass, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600),
					0, 0, 0, 3));
			entities.add(new Entity(fern, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0,
					0, 0, 2));

			entities.add(new Entity(bush0, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600),
					0, 0, 0, (1 + random.nextFloat())));
			entities.add(new Entity(bush1, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600),
					0, 0, 0, (1 + random.nextFloat())));
			entities.add(new Entity(bush2, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600),
					0, 0, 0, (1 + random.nextFloat())));
//			entities.add(new Entity(rock0, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600),
//					0, 0, 0, 2));
//			entities.add(new Entity(rock1, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600),
//					0, 0, 0, 2));
			entities.add(
					new Entity(tree0, new Vector3f(random.nextFloat() * 800 - 400, -3.5f, random.nextFloat() * -600), 0,
							0, 0, (10 + (10 * random.nextFloat()))));
//			entities.add(new Entity(tree1, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600),
//					0, 0, 0, 2));
//			entities.add(new Entity(tree2, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600),
//					0, 0, 0, 2));
//			entities.add(new Entity(bench0, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600),
//					0, 0, 0, 2));

		}
		/*
		 * for (int i = 0; i < 100; i++) { entities.add(new Entity(dragon, new
		 * Vector3f(random.nextFloat() * 800 - 400, 100, random.nextFloat() * -600), 0,
		 * 0, 0, 3)); }
		 */

		// Create lightsource.
		light = new Light(new Vector3f(0, 100, 0), new Vector3f(1, 1, 1));

		// Create terrain.
		backgroundTexture = new TerrainTexture(loader.loadTexture("grassTerrain"));
		rTexture = new TerrainTexture(loader.loadTexture("desert"));
		gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		bTexture = new TerrainTexture(loader.loadTexture("path"));
		texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		terrain = new Terrain(0, -1, loader, texturePack, blendMap);
		terrain2 = new Terrain(-1, -1, loader, texturePack, blendMap);

		// Create camera.
		camera = new Camera();

		// Create the MasterRenderer
		renderer = new MasterRenderer();

		// Create player.
		player = new Player(dragon, new Vector3f(100, 0, -50), 0, 0, 0, 1);
	}

	public static void main(String[] args) {
		init();

		// The actual game loop. Exit when user clicks 'x' button.
		while (!Display.isCloseRequested()) {
			camera.move();
			player.move();
			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);

			// For each entity, for each frame, process the entity.
			for (Entity entity : entities) {
				// entity.increaseRotation(0, 0.3f, 0);
				renderer.processEntity(entity);
			}

			// Render each frame.
			renderer.render(light, camera);

			// Update the display each frame.
			DisplayManager.updateDisplay();
		}
		terminate();
	}

	// Cleans up renderer & loader, then closes display.
	private static void terminate() {
		// Cleanup loader & renderer upon closing.
		renderer.cleanUp();
		loader.cleanUp();
		// Close display once loop is exited.
		DisplayManager.closeDisplay();
	}
}
