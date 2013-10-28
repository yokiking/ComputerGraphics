package core.action;

import java.util.ArrayList;
import java.util.Arrays;

import core.datastructure.*;
import core.object.*;


public class RayTracer {
	public ArrayList<Geometry> geoList = new ArrayList<Geometry>();	
	public ArrayList<Light> lightList = new ArrayList<Light>();	
	boolean reflectOn = false;
	boolean fogOn = false;
	boolean shadowOn = false;
	boolean reflact = false;
	double fogDist = 6.0;
	Vector fogColor = new Vector();
	
	public void setReflectOn(boolean reflectOn) {
		this.reflectOn = reflectOn;
	}
	
	public boolean isReflectOn() {
		return reflectOn;
	}

	public void setFogOn(boolean fogOn) {
		this.fogOn = fogOn;
	}
	
	public boolean isFogOn() {
		return fogOn;
	}
	
	public void setShadowOn(boolean shadowOn) {
		this.shadowOn = shadowOn;
	}
	
	public boolean isShadowOn() {
		return shadowOn;
	}
	
	
	public void setFogColor(double r, double g, double b) {
		fogColor.data[0] = r;
		fogColor.data[1] = g;
		fogColor.data[2] = b;
	}
	
	public void setFogDist(double d) {
		this.fogDist = d;
	}
	
	public Vector rayTrace(Vector v, Vector w) {
		return rayTrace(v, w, 0);
	}
	
	public Vector rayTrace(Vector v, Vector w, int level) {
		if(level == 3) {
			return null;
		}
		Geometry geoToReflect = null;
		double t = Double.POSITIVE_INFINITY;
		
		for (Geometry geo : geoList) {
			HitPoint[] roots = geo.intersect(v , w);
			System.out.println(" geo "+geo+roots);
			if (roots != null) {
				System.out.println(roots[0].t);
				double minRoot = Double.POSITIVE_INFINITY;
				for (HitPoint r : roots) {
					if (r != null && r.t > 0 && r.t < minRoot) {
						minRoot = r.t;						
					}
				}				
				if (t > minRoot) {
					t = minRoot;
					geoToReflect = geo;
				}
			}
		}
		if(null == geoToReflect) {
			return null;
		}
		
		Vector s = w.mul(t).add(v);                 //
		System.out.println(s);
		Vector n = geoToReflect.valuedGradient(s);
		System.out.println("rayTracer-n"+n);
		Vector r = w.sub(n.mul(2 * n.dot(w)));   //R = w - 2(N¡¤w)N
	
		Vector phong = phongShading(geoToReflect, n, s, r, shadowOn);	
		Vector mc = geoToReflect.getMaterial().mirrorColor;		

//		System.out.println("1--"+phong);
		if (reflectOn && ! mc.equals(new Vector(0.0, 0.0, 0.0))) {    // mirrorColor not black
			Vector reflect = rayTrace(s.add(r.mul(0.001)), r, level + 1);
			if (null != reflect) {
				System.out.println("reflect "+reflect);
				phong = (phong.mul(mc.minus(1.0))).add(reflect.mul(mc));
			}
		}
//		System.out.println("2--"+phong);
		if(fogOn) {
			double fogFactor = Math.pow(0.5, t / fogDist);
			phong = phong.mul(fogFactor).add(fogColor.mul(1 - fogFactor));
		}
		return phong;				
	}
	
	public Vector phongShading(Geometry g, Vector n, Vector s, Vector r, boolean shadow) {
		Material m = g.getMaterial();
		Vector result = new Vector(0.0,0.0,0.0);
		result.copy(m.ambient);
		
		outer: for (Light light : lightList) {
			Vector v = s;
			Vector o = light.source;
			Vector w = o.sub(v);
			w.normalize();
			if(shadow) {
				for (Geometry geo : geoList) {   
					if (geo != g) {
						HitPoint[] roots = geo.intersect(v , w);
						if(null != roots && roots.length > 0) {
							for(HitPoint root : roots) {
								if(root.t > 0) {
									continue outer;
								}
							}
						}
					}
				}
			}
			
			Vector l = new Vector();			
			l.copy(o);
			l.normalize();
			Vector diffuse = m.diffuse.mul(Math.max(0, l.dot(n)));
			Vector specular = m.specular.mul(Math.pow(Math.max(0, l.dot(r)), m.power));
			result.add(light.illumin.mul(diffuse.add(specular)));
			System.out.println("phong"+result);				
		}
		
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}