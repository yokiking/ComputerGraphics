package core.object;

import java.util.Arrays;

import core.datastructure.HitPoint;
import core.datastructure.Matrix;
import core.datastructure.Vector;
import edu.nyu.graphics.core.shading.Root;

//double [4] coef;
//public HitList intersect
//public Vector valuedGradient
public class HalfSpace extends Geometry{
	double[] coef = new double[10];
	double[] curCoef = new double[10];

	Matrix invertMatrixT = new Matrix();
	
	public HalfSpace(double a, double b, double c, double d) {
		coef[0] = a;
		coef[1] = b;
		coef[2] = c;
		coef[3] = d;
	}
	
	@Override
	public double eval(Vector v) {
		double result = curCoef[0] * v.data[0]
		              + curCoef[1] * v.data[1]
		              + curCoef[2] * v.data[2]
		              + curCoef[3];
		                                   
		return result;
	}
	
	@Override
	public Vector valuedGradient(Vector s)  {
		Vector result = new Vector(curCoef[0], curCoef[1], curCoef[2]);                                                                   						                                                     		
		result.normalize();
		return result;
	}
	
	@Override
	public HitPoint[] intersect(Vector v, Vector w) {	
		HitPoint[] result;
		double A = curCoef[0] * w.data[0] + curCoef[1] * w.data[1] + curCoef[2] * w.data[2];
		double B = curCoef[0] * v.data[0] + curCoef[1] * v.data[1] + curCoef[2] * v.data[2] + curCoef[3];
		  System.out.println("half-space-curCoef "+Arrays.toString(curCoef));
		if (Math.abs(A) < 0.0001 && B > 0) {
			return null;
		}
		double root = -B / A;
		System.out.println("half-space-root "+root);
		if (B > 0) {
			result = new HitPoint[]{new HitPoint(root, this, true)};			
		} else {
			result = new HitPoint[]{new HitPoint(root, this, false)};		
		}
		System.out.println("hs-intersect"+Arrays.toString(result));
		return result;
	}
	
	@Override
	public void transform() {
		helpMatrix.identity();
		if (parent != null) {
			helpMatrix.multiply(parent.helpMatrix);
		}
		helpMatrix.multiply(m);
		
		invertMatrixT.copy(helpMatrix);
		invertMatrixT.invert();
		invertMatrixT.transpose();
		
		for (int i = 0; i < 4; ++i) {
			curCoef[i] = invertMatrixT.data[i][0] * coef[0]
					+ invertMatrixT.data[i][1] * coef[1]
					+ invertMatrixT.data[i][2] * coef[2]
					+ invertMatrixT.data[i][3] * coef[3];
		}
	}

}