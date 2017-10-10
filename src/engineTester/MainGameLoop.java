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
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrians.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();

		/* Terrain texture */
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkflowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		/****************/

		TexturedModel tree = new TexturedModel(OBJLoader.loadObjModel("tree", loader),
				new ModelTexture(loader.loadTexture("tree")));
		TexturedModel pineTree = new TexturedModel(OBJLoader.loadObjModel("pine", loader),
				new ModelTexture(loader.loadTexture("pine")));
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
				new ModelTexture(loader.loadTexture("grassTexture")));
		TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
				new ModelTexture(loader.loadTexture("flower")));
		
		
		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);
		
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), fernTextureAtlas);
		
		
		
		
		ModelTexture bobbleTextureAtlas = new ModelTexture(loader.loadTexture("lowPolyTree"));
		bobbleTextureAtlas.setNumberOfRows(2);
		
		TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", loader),
				bobbleTextureAtlas);

		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		flower.getTexture().setHasTransparency(true);
		flower.getTexture().setUseFakeLighting(true);
		fern.getTexture().setHasTransparency(true);

		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");

		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random(676452);
		for (int i = 0; i < 400; i++) {
			if (i % 5 == 0) {
				float x = random.nextFloat() * 400;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(grass, new Vector3f(x, y, z), 0, 0, 0, 1.8f));
				x = random.nextFloat() * 400;
				z = random.nextFloat() * -600;
				y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(flower, new Vector3f(x, y, z), 0, 0, 0, 2.3f));
			}
			if (i % 20 == 0) {
				float x = random.nextFloat() * 400;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(fern, random.nextInt(4),new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));
				x = random.nextFloat() * 400 ;
				z = random.nextFloat() * -600;
				y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(bobble, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0,
						random.nextFloat() * 0.1f + 0.6f));
				x = random.nextFloat() * 400 ;
				z = random.nextFloat() * -600;
				y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(pineTree, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0,
						random.nextFloat() * 3.0f + 1.5f));
				x = random.nextFloat() * 400;
				z = random.nextFloat() * -600;
				y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(tree, new Vector3f(x, y, z), 0, 0, 0, random.nextFloat() * 3 + 4));
			}

		}

		Light light = new Light(new Vector3f(20000, 40000, 20000), new Vector3f(1, 1, 1));

		MasterRenderer renderer = new MasterRenderer();

		RawModel playerRawModel = OBJLoader.loadObjModel("person", loader);
		TexturedModel PlayerTexturedModel = new TexturedModel(playerRawModel,
				new ModelTexture(loader.loadTexture("playerTexture")));

		Player player = new Player(PlayerTexturedModel, new Vector3f(0, 0, -60), 0, 0, 0, 1);
		Camera camera = new Camera(player);
		
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("socuwan"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
		GuiTexture gui2 = new GuiTexture(loader.loadTexture("thinmatrix"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
		guis.add(gui);
		guis.add(gui2);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		

		while (!Display.isCloseRequested()) {
			player.move(terrain);
			camera.move();
			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			for (Entity entity : entities) {
				renderer.processEntity(entity);
			}
			renderer.render(light, camera);
			guiRenderer.render(guis);
			DisplayManager.updateDisplay();
		}

		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
