package core.object;

import java.util.ArrayList;
import java.util.Arrays;

import core.datastructure.HitPoint;
import core.datastructure.Vector;

public class ComboGeo extends Geometry{
	public enum BooleanOperator {AND, OR, DIFF};
	public ArrayList<BooleanOperator> opList = new ArrayList<BooleanOperator>();
	
	public ComboGeo(Geometry geo) {
		add(geo);
	}
	
	public void addComponent(Geometry geo, BooleanOperator op) {
		add(geo);
		opList.add(op);
	}
	
	@Override
	public double eval(Vector s) {
		//to-do
		return 0;
	}
	
	@Override
	public HitPoint[] intersect(Vector v, Vector w) {
		System.out.println("combo-intersect"+this);
		if(null == children || children.size() == 0) {
			return null;
		}
		System.out.println(children.get(0));
		HitPoint[] h1 = children.get(0).intersect(v, w);
		System.out.println("h1"+Arrays.toString(h1));
		HitPoint[] h2;
		for(int i=1; i<children.size(); i++) {
			h2 = children.get(i).intersect(v, w);
			System.out.println("h2"+Arrays.toString(h2));
			switch (opList.get(i - 1)) {
			case AND:
				HitPoint.and(h1, h2);
				break;
			case OR:
				HitPoint.or(h1, h2);
				break;
			case DIFF:
				HitPoint.diff(h1, h2);
				break;
		    default:
		    	break;
			}					
		}
//		System.out.println("combogeo"+h1[0].t);
		return h1;	
	}
	
	public Vector valuedGradient(Vector s) {
		for (Geometry g : children) {
			if (Math.abs(g.eval(s)) < 0.00001) {
				return g.valuedGradient(s);
			}
		}
		return null;
	}
}