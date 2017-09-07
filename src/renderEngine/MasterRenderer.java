package renderEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.Entity;
import shaders.StaticShader;
import models.TexturedModel;

public class MasterRenderer {

	private StaticShader shader = new StaticShader();
	private Renderer renderer = new Renderer(shader);
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
}
