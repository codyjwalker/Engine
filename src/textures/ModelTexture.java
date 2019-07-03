package textures;

/*
 * File: ModelTexture.java Purpose: Represents a texture to texture our
 * models.
 */
public class ModelTexture {

	private static final float SHINE_DAMPER = 1;
	private static final float REFLECTIVITY = 0;

	private int textureID;
	private float shineDamper;
	private float reflectivity;
	private boolean hasTransparency;
	private boolean useFakeLighting;

	public ModelTexture(int ID) {
		this.textureID = ID;
		this.shineDamper = SHINE_DAMPER;
		this.reflectivity = REFLECTIVITY;
		this.hasTransparency = false;
		this.useFakeLighting = false;
	}

	public boolean isUsingFakeLighting() {
		return this.useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public boolean hasTransparency() {
		return this.hasTransparency;
	}

	public void setTransparancy(boolean hasTransparancy) {
		this.hasTransparency = hasTransparancy;
	}

	public int getID() {
		return this.textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

}
