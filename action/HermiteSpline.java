package core.action;

import java.util.Arrays;

public class HermiteSpline {
	
	private double[] a;
	private double[] b;
	private double[] c;
	private double[] d;
	private double[] times;
		
	public static final double[][] HERMITE = {  //Hermite matrix
		{ 2  , -2  ,  1  ,  1  },
		{-3  ,  3  , -2  , -1  },
		{ 0  ,  0  ,  1  ,  0  },
		{ 1  ,  0  ,  0  ,  0  } 
	};
	
	
	public HermiteSpline (double[] t, double[] v) {		
		times = Arrays.copyOf(t, t.length);
		a = new double[t.length - 1];
		b = new double[t.length - 1];
		c = new double[t.length - 1];
		d = new double[t.length - 1];		
		
		for(int i = 0; i < t.length - 1; i++) {
			double[] weights = new double[4];  //P1 P4 R1 R4
			if(i == 0) {
				weights[0] = v[i];
				weights[1] = v[i + 1];
				weights[2] = 2 * (v[1] - v[0]);
				weights[3] = (v[i + 2] - v[i]) / (t[i + 2] - t[i]) * (t[i + 1] - t[i]);
			}
			else if(i == t.length - 2) {
				weights[0] = v[i];
				weights[1] = v[i + 1];
				weights[2] = (v[i + 1] - v[i - 1]) / (t[i + 1] - t[i - 1]) * (t[i + 1] - t[i]);
				weights[3] = 2 * (v[v.length - 1] - v[v.length - 2]); 
			}
			else {			
				weights[0] = v[i];
				weights[1] = v[i + 1];
				weights[2] = (v[i + 1] - v[i - 1]) / (t[i + 1] - t[i - 1]) * (t[i + 1] - t[i]);
				weights[3] = (v[i + 2] - v[i]) / (t[i + 2] - t[i]) * (t[i + 1] - t[i]);
			}
			
			for(int j = 0; j < 4; j ++) {
				a[i] += HERMITE[0][j] * weights[j];
				b[i] += HERMITE[1][j] * weights[j];
				c[i] += HERMITE[2][j] * weights[j];
				d[i] += HERMITE[3][j] * weights[j];	
			}			
		}
		
	}
	
	public double interpolate(double time) {
		double result = 0.0;
		double t = 0.0;
		for(int i = 0; i < times.length - 1; i++) {
			if(times[i] < time && time <= times[i + 1]) {				
				t = (time - times[i]) / (times[i + 1] - times[i]);
				result = t * (t * (t * a[i] + b[i]) + c[i]) + d[i];
				break;
			}
		}
	
		return result;
	}
	
}