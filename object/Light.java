package core.object;

import core.datastructure.Vector;

public class Light {
	public Vector origin;
	public Vector source;
	public Vector illumin;
	
	public Light(Vector origin, Vector illumin) {
		this.origin = origin;
		this.illumin = illumin;
	}
	
	public Vector getOrigin() {
		return origin;
	}
	
	public void setSource(double x, double y, double z) {
		this.source = new Vector(x, y, z);
	}

	
}