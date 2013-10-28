package core.object;

import core.datastructure.HitPoint;
import core.datastructure.Vector;

public class Polyhedra extends Geometry {
	
	public Polyhedra() {
	}
	
	@Override
	public HitPoint[] intersect(Vector v, Vector w) {
		System.out.println("polyhedra-intersect"+this);
		if(null == children || children.size() == 0) {
			return null;
		}
		HitPoint hIn = null;
		HitPoint hOut = null;
		double rIn = Double.NEGATIVE_INFINITY;
		double rOut = Double.POSITIVE_INFINITY;
		for(int i=0; i< children.size(); i++) {
			HitPoint[] r = children.get(i).intersect(v, w);
			if((null == r) || (r[0].sign && r[0].t <= 0)) {
				return null;				
			}
			if (r != null && r[0].t > 0) {
				if (r[0].sign && r[0].t > rIn) {
					rIn = r[0].t;
					hIn = r[0];
				}
				else if (!r[0].sign && r[0].t < rOut) {
					rOut = r[0].t;
					hOut = r[0];
				}
			}
		}
		if (rIn > rOut || (null == hIn && null == hOut) ) {
			return null;
		}else if (null != hIn && null == hOut){
			return new HitPoint[] {hIn};
		}else if (null != hOut && null == hIn){
			return new HitPoint[] {hOut};
		}else{
			return new HitPoint[] {hIn, hOut};
		}			
	}
	
	@Override
	public double eval(Vector s) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public Vector valuedGradient(Vector s) {
		for (Geometry geo : children) {
			if (Math.abs(geo.eval(s)) < 0.00001) {
				return geo.valuedGradient(s);
			}
		}
		return null;
	}
	
}