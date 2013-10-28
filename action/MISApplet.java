package core.action;

/*
   This is the support code that implements a frame buffer in a Java Applet,
   by using a Memory Image Source.   
*/

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import core.datastructure.Vector;
import core.object.Geometry;
import core.object.Light;


public abstract class MISApplet extends Applet implements Runnable {

	private static final long serialVersionUID = -2029187623761122226L;

	public int W, H;
    public int[] pix;              // THE FRAME BUFFER ARRAY
    protected MemoryImageSource mis;  // MEMORY IMAGE SOURCE CONTAINING FRAME BUFFER
    private Image im;               // IMAGE CONTAINING THE MEMORY IMAGE SOURCE
    private double startTime;       // CLOCK TIME THAT THE APPLET STARTED
    protected Geometry world;
    protected RayTracer rayTracer = new RayTracer();
    protected double focalLength = 6.0;
    protected int azimuth = 0;
    protected int inclination = 0;
    protected int rgb[] = new int[3];
    

    public void initialize() {}

    public abstract void initFrame(double time);           // INITIALIZE EACH FRAME
    public abstract void setPixel(int x, int y, int rgb[]); // SET COLOR AT EACH PIXEL
    
    // INITIALIZE THINGS WHEN APPLET STARTS UP

    public void init() {
    	
        setLayout(new BorderLayout());
        W = getBounds().width;      // FIND THE RESOLUTION OF THE JAVA APPLET
        H = getBounds().height;
        pix = new int[W*H];         // ALLOCATE A FRAME BUFFER IMAGE
        mis = new MemoryImageSource(W, H, pix, 0, W);
        mis.setAnimated(true);
        im = createImage(mis);      // MAKE MEMORY IMAGE SOURCE FOR FRAME BUFFER

    	initialize();
	    startTime = clockTime();    // FETCH CLOCK TIME WHEN APPLET STARTS
        new Thread(this).start();   // START THE BACKGROUND RENDERING THREAD
    }

    // UPDATE DISPLAY AT EACH FRAME, BY DRAWING FROM MEMORY IMAGE SOURCE
    public void update(Graphics g) { 
        computeImage(clockTime() - startTime);
        mis.newPixels(0, 0, W, H, true);
        g.drawImage(im, 0, 0, null);
    } 

    // BACKGROUND THREAD: COMPUTE AND DRAW FRAME, EVERY 30 MILLISEC

    public void run() {
        while(true) {
            computeImage(clockTime() - startTime);
            mis.newPixels(0, 0, W, H, true);
            repaint();
            try {
                Thread.sleep(30);
            }
            catch(InterruptedException ie) {}
        }
    }  

    // COMPUTE IMAGE, GIVEN ANIMATION TIME

   
    public abstract void computeImage(double time);// {
//	initFrame(time);                 // INITIALIZE COMPUTATION FOR FRAME
//        int i = 0;
//        for(int y = 0; y < H; y++)
//        for(int x = 0; x < W; x++) { // COMPUTE COLOR FOR EACH PIXEL
//           setPixel(x, y, rgb);
//	   pix[i++] = pack(rgb[0],rgb[1],rgb[2]);
//        }
//    }

    public int pack(int red, int grn, int blu) {
       return 255<<24 | clip(red,0,255)<<16
		      | clip(grn,0,255)<< 8
		      | clip(blu,0,255)    ;
    }

    public int unpack(int packedRGB, int component) {
       return packedRGB >> 8*(2-component) & 255;
    }

    public int xy2i(int x, int y) { return x + W * y; }

    int clip(int t, int lo, int hi) { return t<lo ? lo : t>hi ? hi : t; }

    // RETURN THE TIME, IN SECONDS, ON THE CLOCK

    double clockTime() { return System.currentTimeMillis() / 1000.; }

   
    
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

//	@Override
//	public void render(Graphics g) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void viewport(double[] src, int[] dst) {
//		// TODO Auto-generated method stub
//		
//	}
    
    
}
