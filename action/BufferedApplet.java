package core.action;


// THIS CLASS HANDLES DOUBLE BUFFERING 

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import core.datastructure.Matrix;
import core.datastructure.Vector;
import core.object.Geometry;
import core.object.Light;

public abstract class BufferedApplet extends java.applet.Applet implements Runnable
{
   // YOU MUST DEFINE A METHOD TO RENDER THE APPLET

   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
public abstract void render(Graphics g);

   // A BACKGROUND THREAD CALLS REPAINT EVERY 30 MILLISECONDS,

   public void start() { if (t == null) (t = new Thread(this)).start(); }
   public void run()   { try { while (true) { repaint(); t.sleep(30); } }
                         catch(Exception e){}; }

   // WHICH CALLS UPDATE, WHICH CALLS YOUR RENDER METHOD.
   // THE IMAGE YOU'VE RENDERED IS THEN COPIED TO THE SCREEN.

   public void update(Graphics g) {
      if (width != getWidth() || height != getHeight()) {
         image = createImage(width = getWidth(), height = getHeight());
         buffer = image.getGraphics();
      }
      render(buffer);
      g.drawImage(image,0,0,this);
   }  
   
   public abstract void viewport(double src[], int dst[]);
   
	public void draw(Graphics g, Geometry geo) {
		
		if(null == geo.shape) 
			return;
		
		double[][] tempvertice = null;
		int[][] viewPortTemp = null;		
		boolean isFillPolygon = geo.getIsFillPolygon();
		g.setColor(geo.getColor());
		Matrix m;		
		m = geo.getGlobalMatrix();
//		System.out.println(m);

		for(int i=0;i<geo.faces.length;i++) {
			tempvertice = new double[geo.faces[i].length][];
			viewPortTemp = new int[geo.faces[i].length][];
			for(int j=0;j<geo.faces[i].length;j++) {				
				tempvertice[j] = new double[4];				
				viewPortTemp[j] = new int[2];				
				m.transform(geo.vertices[geo.faces[i][j]],tempvertice[j]);
				viewport(tempvertice[j],viewPortTemp[j]);
			}
			if(null != viewPortTemp) {
				Polygon poly = new Polygon();
				for(int j=0;j<viewPortTemp.length - 1;j++) {
					g.drawLine(viewPortTemp[j][0],viewPortTemp[j][1],
							viewPortTemp[j + 1][0],viewPortTemp[j + 1][1]);	
					poly.addPoint(viewPortTemp[j][0],viewPortTemp[j][1]);
				}
				g.drawLine(viewPortTemp[viewPortTemp.length - 1][0],
						viewPortTemp[viewPortTemp.length - 1][1],
						viewPortTemp[0][0],viewPortTemp[0][1]);
				poly.addPoint(viewPortTemp[viewPortTemp.length - 1][0],
						viewPortTemp[viewPortTemp.length - 1][1]);
				if(isFillPolygon) {
					g.fillPolygon(poly);
				}
			}
		}		
	}
   
   public void draw(Graphics g) {
	   traverse(g,world);
   }
   
   public void traverse(Graphics g,Geometry geo) {
	   if(null == geo) 
		   return;	   
	   draw(g,geo);
	   for(int i=0;i<geo.getNumChildren();i++) {
		   traverse(g,geo.getChild(i));
	   }	   
   }

   protected Thread t;
   protected Image image;
   protected Graphics buffer;
   protected Geometry world;
   protected int width = 0, height = 0; 
   protected RayTracer rayTracer = new RayTracer();
   protected double focalLength = 6.0;
   protected int azimuth = 0;
   protected int inclination = 0;
   
   public Geometry getWorld() {	
	   world = new Geometry();
	   return world;
   } 
   
   public void addLight(double x, double y, double z, double r, double g, double b) {
	   rayTracer.lightList.add(new Light(new Vector(x, y, z), new Vector(r, g, b)));	   
   }
   
   public void addGeo(Geometry geo) {
	   rayTracer.geoList.add(geo);
   }
   
   public void setFL(double focalLength) {
	   this.focalLength = focalLength;
   }

}
