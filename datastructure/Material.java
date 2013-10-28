package core.datastructure;

public class Material {
	public Vector ambient = new Vector();  //color
	public Vector diffuse = new Vector();  //color
	public Vector specular = new Vector(); //color
	public double power;
	public Vector mirrorColor = new Vector();  //color
	
	public Material(Vector argb, Vector drgb, Vector srgb, double p, Vector mc) {
		ambient = argb;
		diffuse = drgb;
		specular = srgb;
		power = p;	
		mirrorColor = mc;
	}
}