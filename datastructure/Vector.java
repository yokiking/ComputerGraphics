package core.datastructure;

import java.util.Arrays;

public class Vector {
	public double[] data = new double[3];
	
	public Vector() {		
	}
	
	public Vector(double v1, double v2, double v3) {
		data[0] = v1;
		data[1] = v2;
		data[2] = v3;
	}
	
	public void copy(Vector src) {
		data = Arrays.copyOf(src.data, 3);
	}
	
	public void set(double v1, double v2, double v3) {
		data[0] = v1;
		data[1] = v2;
		data[2] = v3;
	}
	
	public void set(double[] data) {
		this.data[0] = data[0];
		this.data[1] = data[1];
		this.data[2] = data[2];
	}
	
	
	
	public Vector add(Vector v) {
		Vector result = new Vector(data[0] + v.data[0]
		                         , data[1] + v.data[1]
		                         , data[2] + v.data[2]);
		return result;		
	}
	
	public Vector sub(Vector v) {
		Vector result = new Vector(data[0] - v.data[0]
		                         , data[1] - v.data[1]
		                         , data[2] - v.data[2]);
		return result;		
	}
	
	public Vector minus(double d) {
		Vector result = new Vector(d - data[0] , d - data[1] , d - data[2]);
		return result;	
	}
	public Vector mul(double d) {
		Vector result = new Vector(data[0] * d , data[1] * d , data[2] * d);
		return result;		
	}
	
	public Vector mul(Vector v) {
		Vector result = new Vector(data[0] * v.data[0]
		                         , data[1] * v.data[1]
		                         , data[2] * v.data[2]);
		return result;		
	}
	
	public Vector div(double d) {
		Vector result = new Vector(data[0] / d , data[1] / d , data[2] / d);
		return result;		
	}
	
	public double dot(Vector v) {
		double result = data[0] * v.data[0] + data[1] * v.data[1] + data[2] * v.data[2];
		return result;		
	}
	
	public double square() {
		double result = dot(this);
		return result;
	}
	
	public void normalize() {
		double norm = Math.sqrt(square());
		data[0] /= norm;
		data[1] /= norm;
		data[2] /= norm;
	}
	
	public double distance(Vector v) {
		double result = Math.sqrt(Math.pow(v.data[0] - data[0],2) +  Math.pow(v.data[1] - data[1],2)
				                 + Math.pow(v.data[2] - data[2],2));
		return result;		
	}
	
	@Override
	public boolean equals(Object o) {
		Vector v = (Vector) o;
		for (int i = 0; i < 3; i++) {
			if (Math.abs(data[i] - v.data[i]) > 0.001) {
				return false;
			}
		}
		return true;	
	}
	
	@Override 
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < 3; i++) {
			sb.append(String.format("%.2f ", data[i]));			
		}
		sb.append("\n");
		return sb.toString();
	}
}