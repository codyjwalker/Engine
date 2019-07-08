package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GUIRenderer;
import guis.GUITexture;
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
 * File: MainGameLoop.java Purpose: Contains the main() method, inside of
 * which the "infinite" game loop is contained.
 */
public class MainGameLoop {

	private static Loader loader;
	private static RawModel rawModel;
	private static ModelTexture texture, modelTexture;
	private static TexturedModel dragon;
	private static List<Entity> entities;
	private static Light light1, light2, light3, light4;
	private static List<Light> lights;
	private static Camera camera;
	private static MasterRenderer renderer;
	private static Player player;

	private static Terrain terrain, terrain2, terrain3, terrain4;
	private static TerrainTexture backgroundTexture, rTexture, gTexture,
			bTexture, blendMap;
	private static TerrainTexturePack texturePack;

	private static TexturedModel grass;
	private static TexturedModel fern;
	private static TexturedModel bush0, bush1, bush2, tree0;
	private static TexturedModel lamp;

	private static List<GUITexture> guis;
	private static GUITexture gui, gui2;
	private static GUIRenderer guiRenderer;

	// Initializes all objects needed for rendering.
	private static void init() {
		// Open up the display.
		DisplayManager.createDisplay();

		// Create Loader.
		loader = new Loader();

		// TODO: NOTE: ep. 16 Fog: Use of new OBJLoader

		// Load up RawModel & ModelTexture, create texturedModel, & extract its
		// texture.
		rawModel = OBJLoader.loadObjModel("Toothpickcharlie1", loader);
		texture = new ModelTexture(loader.loadTexture("GOld"));
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

		ModelTexture fernTextureAtlas = new ModelTexture(
				loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);
		fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),
				fernTextureAtlas);
		fern.getTexture().setTransparancy(true);

		bush0 = new TexturedModel(OBJLoader.loadObjModel("simpleBush0", loader),
				new ModelTexture(loader.loadTexture("tree")));
		bush1 = new TexturedModel(OBJLoader.loadObjModel("simpleBush1", loader),
				new ModelTexture(loader.loadTexture("tree")));
		bush2 = new TexturedModel(OBJLoader.loadObjModel("simpleBush2", loader),
				new ModelTexture(loader.loadTexture("tree")));
		// rock0 = new TexturedModel(OBJLoader.loadObjModel("simpleRock0",
		// loader),
		// new ModelTexture(loader.loadTexture("tree")));
		// rock1 = new TexturedModel(OBJLoader.loadObjModel("simpleRock1",
		// loader),
		// new ModelTexture(loader.loadTexture("tree")));
		tree0 = new TexturedModel(OBJLoader.loadObjModel("simpleTree0", loader),
				new ModelTexture(loader.loadTexture("tree")));
		// tree1 = new TexturedModel(OBJLoader.loadObjModel("simpleTree1",
		// loader),
		// new ModelTexture(loader.loadTexture("tree")));
		// tree2 = new TexturedModel(OBJLoader.loadObjModel("simpleTree2",
		// loader),
		// new ModelTexture(loader.loadTexture("tree")));
		lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader),
				new ModelTexture(loader.loadTexture("lamp")));

		// Create terrain.
		backgroundTexture = new TerrainTexture(
				loader.loadTexture("grassTerrain"));
		rTexture = new TerrainTexture(loader.loadTexture("desert"));
		gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		bTexture = new TerrainTexture(loader.loadTexture("path"));
		texturePack = new TerrainTexturePack(backgroundTexture, rTexture,
				gTexture, bTexture);
		blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		terrain = new Terrain(0, -1, loader, texturePack, blendMap,
				"heightmap");
		// terrain2 = new Terrain(-1, -1, loader, texturePack, blendMap,
		// "heightmap");
		// terrain3 = new Terrain(0, 0, loader, texturePack, blendMap,
		// "heightmap");
		// terrain4 = new Terrain(-1, 0, loader, texturePack, blendMap,
		// "heightmap");


		// Create Entities with TexturedModels.
		entities = new ArrayList<Entity>();
		Random random = new Random();
		float x, y, z;
		for (int i = 0; i < 500; i++) {
			x = random.nextFloat() * 800 - 400;
			z = random.nextFloat() * -600;
			y = terrain.getHeightOfTerrain(x, z);
			entities.add(new Entity(grass, new Vector3f(x, y, z), 0, 0, 0, 3));
			x = random.nextFloat() * 800 - 400;
			z = random.nextFloat() * -600;
			y = terrain.getHeightOfTerrain(x, z);
			entities.add(new Entity(fern, new Vector3f(x, y, z), 0, 0, 0, 2,
					random.nextInt(4)));
			x = random.nextFloat() * 800 - 400;
			z = random.nextFloat() * -600;
			y = terrain.getHeightOfTerrain(x, z);
			entities.add(new Entity(bush0, new Vector3f(x, y, z), 0, 0, 0,
					(1 + random.nextFloat())));
			x = random.nextFloat() * 800 - 400;
			z = random.nextFloat() * -600;
			y = terrain.getHeightOfTerrain(x, z);
			entities.add(new Entity(bush1, new Vector3f(x, y, z), 0, 0, 0,
					(1 + random.nextFloat())));
			x = random.nextFloat() * 800 - 400;
			z = random.nextFloat() * -600;
			y = terrain.getHeightOfTerrain(x, z);
			entities.add(new Entity(bush2, new Vector3f(x, y, z), 0, 0, 0,
					(1 + random.nextFloat())));
			// entities.add(new Entity(rock0, new Vector3f(random.nextFloat() *
			// 800 - 400, 0, random.nextFloat() * -600),
			// 0, 0, 0, 2));
			// entities.add(new Entity(rock1, new Vector3f(random.nextFloat() *
			// 800 - 400, 0, random.nextFloat() * -600),
			// 0, 0, 0, 2));
			x = random.nextFloat() * 800 - 400;
			z = random.nextFloat() * -600;
			y = terrain.getHeightOfTerrain(x, z);
			entities.add(new Entity(tree0,
					new Vector3f(random.nextFloat() * 800 - 400, -3.5f,
							random.nextFloat() * -600),
					0, 0, 0, (10 + (10 * random.nextFloat()))));
			// entities.add(new Entity(tree1, new Vector3f(random.nextFloat() *
			// 800 - 400, 0, random.nextFloat() * -600),
			// 0, 0, 0, 2));
			// entities.add(new Entity(tree2, new Vector3f(random.nextFloat() *
			// 800 - 400, 0, random.nextFloat() * -600),
			// 0, 0, 0, 2));
			// entities.add(new Entity(bench0, new Vector3f(random.nextFloat() *
			// 800 - 400, 0, random.nextFloat() * -600),
			// 0, 0, 0, 2));

		}
		/*
		 * for (int i = 0; i < 100; i++) { entities.add(new Entity(dragon, new
		 * Vector3f(random.nextFloat() * 800 - 400, 100, random.nextFloat() *
		 * -600), 0, 0, 0, 3)); }
		 */

		// Create lights.
		light1 = new Light(new Vector3f(0, 1000, -7000),
				new Vector3f(0.4f, 0.4f, 0.4f));
		light2 = new Light(new Vector3f(185, 10, -293), new Vector3f(2, 0, 0),
				new Vector3f(1, 0.01f, 0.002f));
		light3 = new Light(new Vector3f(370, 17, -300), new Vector3f(0, 2, 2),
				new Vector3f(1, 0.01f, 0.002f));
		light4 = new Light(new Vector3f(283, 7, -305), new Vector3f(2, 2, 0),
				new Vector3f(1, 0.01f, 0.002f));
		lights = new ArrayList<Light>();
		lights.add(light1);
		lights.add(light2);
		lights.add(light3);
		lights.add(light4);
		// Lamp entities.
		entities.add(
				new Entity(lamp, new Vector3f(185, -4.7f, -293), 0, 0, 0, 1));
		entities.add(
				new Entity(lamp, new Vector3f(370, 4.2f, -300), 0, 0, 0, 1));
		entities.add(
				new Entity(lamp, new Vector3f(293, -6.8f, -305), 0, 0, 0, 1));
		
		
		// Create the MasterRenderer
		renderer = new MasterRenderer(loader);

		// Create player.
		player = new Player(dragon, new Vector3f(100, 0, -50), 0, 180, 0, 2);

		// Create camera.
		camera = new Camera(player);

		// Create GUIs.
		guis = new ArrayList<GUITexture>();
		gui = new GUITexture(loader.loadTexture("socuwan"),
				new Vector2f(0.7f, 0.7f), new Vector2f(0.25f, 0.25f));
		guis.add(gui);
		gui2 = new GUITexture(loader.loadTexture("health"),
				new Vector2f(-0.8f, 0.9f), new Vector2f(0.2f, 0.2f));
		guis.add(gui2);
		guiRenderer = new GUIRenderer(loader);

	}

	public static void main(String[] args) {
		init();

		// The actual game loop. Exit when user clicks 'x' button.
		while (!Display.isCloseRequested()) {
			camera.move();
			player.move(terrain);
			// TODO: test which terrain player is standing on.
			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			// renderer.processTerrain(terrain2);
			// renderer.processTerrain(terrain3);
			// renderer.processTerrain(terrain4);

			// For each entity, for each frame, process the entity.
			for (Entity entity : entities) {
				// entity.increaseRotation(0, 0.3f, 0);
				renderer.processEntity(entity);
			}

			// Render each frame.
			renderer.render(lights, camera);

			// Render the GUI elements each frame.
			guiRenderer.render(guis);

			// Update the display each frame.
			DisplayManager.updateDisplay();
		}
		terminate();
	}

	// Cleans up renderer & loader, then closes display.
	private static void terminate() {
		// Shut down GUI renderer.
		guiRenderer.cleanUp();
		// Cleanup loader & renderer upon closing.
		renderer.cleanUp();
		loader.cleanUp();
		// Close display once loop is exited.
		DisplayManager.closeDisplay();
	}
}
