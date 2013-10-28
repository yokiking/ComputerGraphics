//design
//public HitList union (HitList a, HitList b)
//public HitList intersection (HitList a, HitList b)
//public HitList difference (HitList a, HitList b)

//double t;
//int surfaceId;
//boolean sign; true in false out

//public void sortByT
package core.datastructure;

import core.object.Geometry;

public class HitPoint {
	public double t;
	public Geometry geo;
	public boolean sign; 
	
	public HitPoint (double t, Geometry geo, boolean sign) {
		this.t = t;
		this.geo = geo;
		this.sign = sign;
	}
	
	
	public static HitPoint[] not(HitPoint[] roots) {
		for (HitPoint r : roots) {
			if (r != null) {
				r.sign = !r.sign;
			}
		}
		return roots;
	}
	
	public static HitPoint[] and(HitPoint[] h1, HitPoint[] h2) {
		if (h1 == null || h2 == null) {
			return null;
		}
		HitPoint[] result = new HitPoint[h1.length + h2.length];
		int i = 0, j = 0, k = 0;
		while (i < h1.length && j < h2.length && h1[i] != null && h2[j] != null) {
			if (h1[i].t < h2[j].t) {
				if (isIn(h1[i].t, h2)) {
					result[k++] = h1[i];
				} 
				++i;
			} else {
				if (isIn(h2[j].t, h1)) {
					result[k++] = h2[j];
				}
				++j;
			}
		}
		if (i == h1.length) {
			while (j < h2.length && h2[j] != null) {
				if (isIn(h2[j].t, h1)) {
					result[k++] = h2[j];
				}
				++j;
			}
		}
		if (j == h2.length) {
			while (i < h1.length && h1[i] != null) {
				if (isIn(h1[i].t, h2)) {
					result[k++] = h1[i];
				} 
				++i;
			}
		}
		return result;
	}
	
	private static boolean isIn(double t, HitPoint[] roots) {
		for (int i = 0; i < roots.length; i += 2) {
			if (roots[i] == null) {
				break;
			}			
			if (i + 1 < roots.length && roots[i + 1] != null) {			
				if (roots[i].sign) {
					if ((roots[i].t > roots[i + 1].t && (t >= roots[i].t || t <= roots[i + 1].t))
					 || (roots[i].t < roots[i + 1].t && t >= roots[i].t && t <= roots[i + 1].t)) {
						return true;
					}
				} 				
				else {
					if ((roots[i].t > roots[i + 1].t && (t >= roots[i].t || t <= roots[i + 1].t))
					 || (roots[i].t < roots[i + 1].t && t >= roots[i].t && t <= roots[i + 1].t)) {
						return true;
					}
				}
			} 
			else {				
				if (roots[i].sign) {
					if ((roots[i].t > 0 && t >= roots[i].t) 
					 || (roots[i].t < 0 && t <= roots[i].t)) {
						return true;
					}
				}				
				else {
					if ((roots[i].t > 0 && t <= roots[i].t) 
					 || (roots[i].t < 0 && t >= roots[i].t)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static HitPoint[] or(HitPoint[] h1, HitPoint[] h2) {
		if (h1 != null && h2 == null) {
			return h1;
		} else if (h1 == null && h2 != null) {
			return h2;
		} else if (h1 != null && h2 != null){
			return not(and(not(h1), not(h2)));
		} else {
			return null;
		}
	}
	
	public static HitPoint[] diff(HitPoint[] h1, HitPoint[] h2) {
		if (h1 != null && h2 == null) {
			return h1;
		} else if (h1 == null && h2 != null) {
			return null;
		} else if (h1 != null && h2 != null){
			return and(h1, not(h2));
		} else {
			return null;
		}
	}
	
	@Override
	public String toString() {
		return new String("t = "+t+" "+geo+" in "+sign);
	}
	
}
