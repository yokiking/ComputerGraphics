package core.object;

import core.datastructure.HitPoint;
import core.datastructure.Vector;

public class Sphere extends Geometry {
	private Vector center;
	private double radius = 1.0;
	
	public Sphere(Vector center, double radius) {
		super();
		super.sphere(100, 100);
		this.m.identity();
		this.m.translate(center.data[0], center.data[1], center.data[2]);
		this.center = center;
		this.radius = radius;
		this.m.scale(radius, radius, radius);		
	}
	
	public void setCenter(Vector center) {
		this.center = center;
		this.m.identity();
		this.m.translate(center.data[0], center.data[1], center.data[2]);
		this.m.scale(radius, radius, radius);
	}
	
	public Vector getCenter() {
		return center;
	}
	
	public double getRadius() {
		return radius;
	}
	
	@Override
	public Vector valuedGradient(Vector surf) {
		Vector result = (surf.sub(center)).div(radius);   // N = (S - C) / r 
		return result;		
	}
	
	@Override
	public HitPoint[] intersect(Vector v, Vector w) {		
		double a = 1.0;
		double b = (w.mul(2)).dot(v.sub(center));
		double c = (v.sub(center)).square() - radius * radius;
		double discriminant = b * b / 4  - a * c ;
		if (discriminant >= 0) {
			HitPoint[] result = new HitPoint[2];
			double[] root = new double[2];
			root[0] = - b / 2 - Math.sqrt(discriminant);
			root[1] = - b / 2 + Math.sqrt(discriminant);
			result[0] = new HitPoint(root[0], this, true);
			result[1] = new HitPoint(root[1], this, false);
			return result;
		}
		return null;		
	}
	
	
	
	
	
	
}