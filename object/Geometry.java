package core.object;

import java.awt.Color;
import java.util.ArrayList;

import core.datastructure.HitPoint;
import core.datastructure.Material;
import core.datastructure.Matrix;
import core.datastructure.Vector;

public class Geometry 
{
	public enum  Shape {CUBE,CYLINDER,TORUS,SPHERE,SUSHI};
	
	public double vertices[][];
	public int faces[][];
	public Shape shape = null;
	
	public Matrix m = new Matrix();     //shape
	public Matrix helpMatrix = new Matrix();    //animation of itself
	public Material material = null;
	public ArrayList<Geometry> children = new ArrayList<Geometry>();
	protected Geometry parent = null;
	protected Color color = null;
	protected boolean isFillPolygon = false;

	public Geometry() {		
		m.identity();
		helpMatrix.identity();
	}
	
	public Matrix getMatrix() {
		return m;
	}
	
	public Matrix getHelpMatrix() {
		return helpMatrix;
	}
	
	public Material getMaterial() {
		return material;
	}	
	
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public Matrix getGlobalMatrix() {
		Matrix globalMatrix = new Matrix();
		Geometry geo = this;
		if(null == geo.getParent()) 
			globalMatrix.copy(geo.getMatrix());
		else {
			geo = geo.getParent();
			globalMatrix = geo.getGlobalMatrix();
			globalMatrix.multiply(getMatrix());			
		}
		globalMatrix.multiply(getHelpMatrix());
		return 	globalMatrix;
	}
	
	public Geometry getParent() {
		return parent;
	}		
	
	public Geometry add() {       // add a child geometry object
		Geometry child = new Geometry();
		children.add(child);
		child.parent = this;
		return child;
	}
	
	public void add(Geometry child) {       // add a child geometry object		
		children.add(child);
		child.parent = this;		
	}

	public void remove(Geometry child) {   // remove a child geometry object
		children.remove(child);
		child.parent = null;		
	}
	
	public void clear() {  // clear the children list before add sth.
		children.clear();
	}

	public int getNumChildren() {         // find out how many children there are
		return children.size();
	}

	public Geometry getChild(int index) {   // get the ith child
		return children.get(index);
	}	

	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public boolean getIsFillPolygon() {
		return isFillPolygon;
	}
	
	public void setIsFillPolygon(boolean isFillPolygon) {
		this.isFillPolygon = isFillPolygon;
	}
	
	public Geometry cube() {
		shape = Shape.CUBE;
		//vertices[24][4] faces[6][4];
		vertices = new double[][] {
			{-1,1,1,1},{1,1,1,1},{1,1,-1,1},{-1,1,-1,1},
			{-1,1,1,1},{1,1,1,1},{1,-1,1,1},{-1,-1,1,1},
			{-1,-1,-1,1},{1,-1,-1,1},{1,-1,1,1},{-1,-1,1,1},
			{1,1,1,1},{1,1,-1,1},{1,-1,-1,1},{1,-1,1,1},
			{-1,1,1,1},{-1,1,-1,1},{-1,-1,-1,1},{-1,-1,1,1},
			{-1,1,-1,1},{1,1,-1,1},{1,-1,-1,1},{-1,-1,-1,1}
		};
		                         
		faces = new int[][] {
				{0,1,2,3},{4,5,6,7},{8,9,10,11},
				{12,13,14,15},{16,17,18,19},{20,21,22,23}
		};
		
		return this;
	}
	
	public Geometry sphere(int m,int n) {
		shape = Shape.SPHERE;
		double theta,phi;		
		vertices = new double[(m+1)*(n+1)][4];
		faces = new int[m*n][4];
		 
		for(int i=0;i<m+1;i++) {
			for(int j=0;j<n+1;j++)	{
				theta = 2 * Math.PI * i / m;
				phi = Math.PI * j / n - Math.PI / 2; 
				vertices[i + (m + 1) * j][0] = Math.cos(theta) * Math.cos(phi);
				vertices[i + (m + 1) * j][1] = Math.sin(theta) * Math.cos(phi);
				vertices[i + (m + 1) * j][2] = Math.sin(phi);
				vertices[i + (m + 1) * j][3] = 1;
			}
		}
		
		for(int i=0;i<m;i++) {
			for(int j=0;j<n;j++) {
				faces[i + m * j][0] = i + (m + 1) * j;   
			    faces[i + m * j][1] = i + 1 + (m + 1) * j;
			    faces[i + m * j][2] = i + 1 + (m +1) * (j + 1);
				faces[i + m * j][3] = i + (m + 1) * (j + 1);
			}
		}
		return this;
		
	}
	
	public Geometry cylinder(int m)
	{
		shape = Shape.CYLINDER;
		double theta;		
		vertices = new double[2 * m + 4][4];
		faces = new int[3 * m][];
		
		for(int i=0;i< m + 1;i++)
		{
			theta = 2 * Math.PI * i / m;
			vertices[i][0] = Math.cos(theta);
			vertices[i][1] = Math.sin(theta);
			vertices[i][2] = 1;
			vertices[i][3] = 1;			
			vertices[i + m + 2][0] = Math.cos(theta);
			vertices[i + m + 2][1] = Math.sin(theta);
			vertices[i + m + 2][2] = -1;
			vertices[i + m + 2][3] = 1;
		}
	   
		vertices[m + 1][0] = 0;
		vertices[m + 1][1] = 0;
		vertices[m + 1][2] = 1;
		vertices[m + 1][3] = 1;
		vertices[2 * m + 3][0] = 0;
		vertices[2 * m + 3][1] = 0;
		vertices[2 * m + 3][2] = -1;
		vertices[2 * m + 3][3] = 1;
		
		//m + 1 faces on the flank of cylinder.
		//m faces on both end-caps of cylinder.	
		//total 3 * m faces.
		for(int i=0;i<m;i++) {
	
			faces[i] = new int[4];
			faces[i][0] = i;
			faces[i][1] = m + 1;
			faces[i][2] = 2 * m + 3;
			faces[i][3] = i + m + 2;			
			
		
			faces[i + m] = new int[3];
			faces[i + m][0] = i;
			faces[i + m][1] = i + 1;
			faces[i + m][2] = m + 1;
			
			faces[i + 2 * m] = new int[3];
			faces[i + 2 * m][0] = i + m + 2;
			faces[i + 2 * m][1] = i + m + 3;
			faces[i + 2 * m][2] = 2 * m + 3;		
		}
		return this;
	}
	
	public Geometry eclipse (double q,int a,int b,int m)
	{
		shape = Shape.CYLINDER;
		double theta;		
		vertices = new double[2 * m + 4][4];
		faces = new int[3 * m][];
		
		for(int i=0;i< m + 1;i++)
		{
			theta = q * 2 * Math.PI * i / m;
			vertices[i][0] = a * Math.cos(theta);
			vertices[i][1] = b * Math.sin(theta);
			vertices[i][2] = 1;
			vertices[i][3] = 1;			
			vertices[i + m + 2][0] = a * Math.cos(theta);
			vertices[i + m + 2][1] = b * Math.sin(theta);
			vertices[i + m + 2][2] = -1;
			vertices[i + m + 2][3] = 1;
		}
	   
		vertices[m + 1][0] = 0;
		vertices[m + 1][1] = 0;
		vertices[m + 1][2] = 1;
		vertices[m + 1][3] = 1;
		vertices[2 * m + 3][0] = 0;
		vertices[2 * m + 3][1] = 0;
		vertices[2 * m + 3][2] = -1;
		vertices[2 * m + 3][3] = 1;
		
		//m + 1 faces on the flank of cylinder.
		//m faces on both end-caps of cylinder.	
		//total 3 * m faces.
		for(int i=0;i<m;i++) {
	
			faces[i] = new int[4];
			faces[i][0] = i;
			faces[i][1] = m + 1;
			faces[i][2] = 2 * m + 3;
			faces[i][3] = i + m + 2;			
			
		
			faces[i + m] = new int[3];
			faces[i + m][0] = i;
			faces[i + m][1] = i + 1;
			faces[i + m][2] = m + 1;
			
			faces[i + 2 * m] = new int[3];
			faces[i + 2 * m][0] = i + m + 2;
			faces[i + 2 * m][1] = i + m + 3;
			faces[i + 2 * m][2] = 2 * m + 3;		
		}
		return this;
	}
	
	public void sushi(int m,double r)
	{
		shape = Shape.SUSHI;
		double theta;		
		vertices = new double[4 * m + 4][4];
		faces = new int[4 * m][4];
		
		for(int i=0;i< m + 1;i++)
		{
			theta = 2 * Math.PI * i / m;
			vertices[i][0] = Math.cos(theta);
			vertices[i][1] = Math.sin(theta);			
			vertices[i][2] = 1;
			vertices[i][3] = 1;			
			vertices[i + m + 1][0] = Math.cos(theta);
			vertices[i + m + 1][1] = Math.sin(theta);
			vertices[i + m + 1][2] = -1;
			vertices[i + m + 1][3] = 1;
			//
			vertices[i + 2 * m + 2][0] = r * Math.cos(theta);
			vertices[i + 2 * m + 2][1] = r * Math.sin(theta);			
			vertices[i + 2 * m + 2][2] = 1;
			vertices[i + 2 * m + 2][3] = 1;			
			vertices[i + 3 * m + 3][0] = r * Math.cos(theta);
			vertices[i + 3 * m + 3][1] = r * Math.sin(theta);
			vertices[i + 3 * m + 3][2] = -1;
			vertices[i + 3 * m + 3][3] = 1;
			
		}		
		//m + 1 faces on the flank of cylinder.
		//m faces on both end-caps of cylinder.	
		//total 3 * m + 3 faces.
		for(int i=0;i<m;i++) {
	
			faces[i] = new int[4];
			faces[i][0] = i;
			faces[i][1] = i + 1;
			faces[i][2] = i + m + 2;			
			faces[i][3] = i + m + 1;
		
			faces[i + m] = new int[4];
			faces[i + m][0] = i;
			faces[i + m][1] = i + 1;
			faces[i + m][2] = i+ 2 * m  + 3;
			faces[i + m][3] = i+ 2 * m  + 2;			
			
			faces[i + 2 * m] = new int[4];
			faces[i + 2 * m][0] = i + 2 * m + 2;
			faces[i + 2 * m][1] = i + 2 * m + 3;
			faces[i + 2 * m][2] = i + 3 * m + 4;	
			faces[i + 2 * m][3] = i + 3 * m + 3;
			
			faces[i + 3 * m] = new int[4];
			faces[i + 3 * m][0] = i + m + 2;
			faces[i + 3 * m][1] = i + m + 1;
			faces[i + 3 * m][2] = i + 3 * m + 3;	
			faces[i + 3 * m][3] = i + 3 * m + 4;
		}
	}
	
	
	public void torus(int m, int n,double r)	{
		shape = Shape.TORUS;
		double theta,phi;		
		vertices = new double[(m+1)*(n+1)][4];
		faces = new int[m*n][4];
		 
		for(int i=0;i<m+1;i++) {
			for(int j=0;j<n+1;j++)	{
				theta = 2 * Math.PI * i / m;
				phi = 2 * Math.PI * j / m;
				vertices[i + (m + 1) * j][0] = (1 + r * Math.cos(phi)) * Math.cos(theta);
				vertices[i + (m + 1) * j][1] = (1 + r * Math.cos(phi)) * Math.sin(theta);
				vertices[i + (m + 1) * j][2] = r * Math.sin(phi);
				vertices[i + (m + 1) * j][3] = 1;
			}
		}
		
		for(int i=0;i<m;i++)
			for(int j=0;j<n;j++)
			{
				faces[i + m * j][0] = i + (m + 1) * j;   
			    faces[i + m * j][1] = i + 1 + (m + 1) * j;
			    faces[i + m * j][2] = i + 1 + (m +1) * (j + 1);
				faces[i + m * j][3] = i + (m + 1) * (j + 1);
			}
	}
	
	public HitPoint[] intersect(Vector v, Vector w){
		System.out.println("geo-intersect"+this);		
		return null;}	
	
	public void transform() {
		helpMatrix.identity();
		if (parent != null) {
			helpMatrix.multiply(parent.helpMatrix);
		}
		helpMatrix.multiply(m);
		
		for (Geometry g : children) {
			g.transform();
		}
	}
	
	public double eval(Vector v) {return 0;}
	
	public Vector valuedGradient(Vector surf)  {return null;}
		
}