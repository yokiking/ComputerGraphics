package core.object;

import java.util.Arrays;

import core.datastructure.HitPoint;
import core.datastructure.Matrix;
import core.datastructure.Vector;

//double [10]  coef;
//Matrix coefMatrix;
//public void transform()
//public double eval(Vector v)

//Vector valuedGradient(x,y,z)
//HitList intersect(Vector v, Vector w) {
//

public class SecondOrderPoly extends Geometry{
	double[] coef = new double[10];
	double[] curCoef = new double[10];
	
	Matrix coefMatrix = new Matrix();
	Matrix invertMatrixT = new Matrix();
	Matrix invertMatrix = new Matrix();
	
	public SecondOrderPoly(double a, double b, double c, double d,
			               double e, double f, double g, double h,
			               double i, double j) {
		coef[0] = a;
		coef[1] = b;
		coef[2] = c;
		coef[3] = d;
		coef[4] = e;
		coef[5] = f;
		coef[6] = g;
		coef[7] = h;
		coef[8] = i;
		coef[9] = j;
	}
	
	@Override
	public double eval(Vector v) {
		double result = curCoef[0] * v.data[0] * v.data[0]
		              + curCoef[1] * v.data[1] * v.data[1]
		              + curCoef[2] * v.data[2] * v.data[2]
		              + curCoef[3] * v.data[1] * v.data[2]
		              + curCoef[4] * v.data[2] * v.data[0]
		              + curCoef[5] * v.data[0] * v.data[1]
		              + curCoef[6] * v.data[0]
		              + curCoef[7] * v.data[1]
		              + curCoef[8] * v.data[2]
		              + curCoef[9];
		                                   
		return result;
	}
	
	@Override
	public Vector valuedGradient(Vector s)  {
		Vector result = new Vector(2 * curCoef[0] * s.data[0] + curCoef[4] * s.data[2] + curCoef[5] * s.data[1] + curCoef[6]
		                         , 2 * curCoef[1] * s.data[1] + curCoef[3] * s.data[2] + curCoef[5] * s.data[0] + curCoef[7]
		                         , 2 * curCoef[2] * s.data[2] + curCoef[3] * s.data[1] + curCoef[4] * s.data[0] + curCoef[8]);                                                                                                						                                                     		
		result.normalize();
		return result;
	}
	
	@Override
	public HitPoint[] intersect(Vector v, Vector w) {		
		double A = w.data[0] * (curCoef[0] * w.data[0] + curCoef[3] * w.data[1]) 
		 		 + w.data[1] * (curCoef[1] * w.data[1] + curCoef[4] * w.data[2]) 
		         + w.data[2] * (curCoef[2] * w.data[2] + curCoef[5] * w.data[0]);
		double B = 2 * (curCoef[0] * v.data[0] * w.data[0] 
		         + curCoef[1] * v.data[1] * w.data[1] 
			     + curCoef[2] * v.data[2] * w.data[2]) 
			     + curCoef[3] * (v.data[1] * w.data[0] + v.data[0] * w.data[1]) 
			     + curCoef[4] * (v.data[2] * w.data[1] + v.data[1] * w.data[2]) 
			     + curCoef[5] * (v.data[0] * w.data[2] + v.data[2] * w.data[0]) 
			     + curCoef[6] * w.data[0] 
			     + curCoef[7] * w.data[1] 
			     + curCoef[8] * w.data[2];
        double C = v.data[0] * (curCoef[0] * v.data[0] + curCoef[3] * v.data[1] + curCoef[6]) 
		         + v.data[1] * (curCoef[1] * v.data[1] + curCoef[4] * v.data[2] + curCoef[7]) 
		         + v.data[2] * (curCoef[2] * v.data[2] + curCoef[5] * v.data[0] + curCoef[8]) + curCoef[9];

        double discriminant = B * B - 4 * A * C;
    	System.out.println("sec-intersect"+this);
        System.out.println("curCoef "+Arrays.toString(curCoef));
        System.out.println("sec-disc "+discriminant);
        if(discriminant >= 0.0) {
           HitPoint[] result = new HitPoint[2];
	       double root1 = (-B - Math.sqrt(discriminant)) / (2 * A);
	       double root2 = (-B + Math.sqrt(discriminant)) / (2 * A);
	       result[0] = new HitPoint(root1, this, true);
	       result[1] = new HitPoint(root2, this, false);
	       return result;
        }
        return null;
	}
	
	@Override
	public void transform() {
		helpMatrix.identity();
		if(null != parent) {
			helpMatrix.multiply(parent.helpMatrix);			
		}
		helpMatrix.multiply(m);    		
		coefMatrix = new Matrix();
		coefMatrix.identity();
		coefMatrix.data[1][1] = coef[1];
		coefMatrix.data[2][2] = coef[2];
		coefMatrix.data[0][1] = coef[3];
		coefMatrix.data[1][2] = coef[4];
		coefMatrix.data[0][2] = coef[5];
		coefMatrix.data[0][3] = coef[6];
		coefMatrix.data[1][3] = coef[7];
		coefMatrix.data[2][3] = coef[8];
		coefMatrix.data[3][3] = coef[9];
		
		invertMatrix.copy(helpMatrix);
		invertMatrix.invert();
		invertMatrixT.copy(invertMatrix);
		invertMatrixT.transpose();
		invertMatrixT.multiply(coefMatrix);
		invertMatrixT.multiply(invertMatrix);
		
		curCoef[0] = invertMatrixT.data[0][0];
		curCoef[1] = invertMatrixT.data[1][1];
		curCoef[2] = invertMatrixT.data[2][2];
		curCoef[3] = invertMatrixT.data[0][1] + invertMatrixT.data[1][0];
		curCoef[4] = invertMatrixT.data[1][2] + invertMatrixT.data[2][1];
		curCoef[5] = invertMatrixT.data[0][2] + invertMatrixT.data[2][0];
		curCoef[6] = invertMatrixT.data[0][3] + invertMatrixT.data[3][0];
		curCoef[7] = invertMatrixT.data[1][3] + invertMatrixT.data[3][1];
		curCoef[8] = invertMatrixT.data[2][3] + invertMatrixT.data[3][2];
		curCoef[9] = invertMatrixT.data[3][3];
		
	}
	
	
	
}