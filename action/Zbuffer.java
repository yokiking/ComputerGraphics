package core.action;

import java.util.Arrays;

public class Zbuffer {
	private final int SCALE = 4096;
	
	public void vertexShading(int[][] trapezoid, int[] pix, int W) {		
		int[][] tri = scale(trapezoid);
		int[] TL = tri[0];		
		int[] TR = tri[1];
		int[] BL = tri[2];
		int[] BR = tri[3];
		int[] tempRgb = new int[3];
		
		// COMPUTE TOTAL NUMBER OF SCAN LINES IN TRAPEZOID

	    int n = BL[1] - TL[1];
	    
	    int[] dL_dy = subdiv(BL, TL, n);
	    int[] dR_dy = subdiv(BR, TR, n);
	   
	    int[] L = Arrays.copyOf(TL, TL.length);
	    int[] R = Arrays.copyOf(TR, TR.length);

	    for (int y = trapezoid[0][1] ; y <= trapezoid[2][1] ; y++) {
	       int m = R[0] - L[0]; 
	       tempRgb = new int[]{L[3], L[4], L[5]};
	       int[] rgb = unscale(tempRgb);
	       if(m == 0) {
	    	   pix[y * W + L[0] / SCALE ] = pack(rgb[0],rgb[1],rgb[2]);
	       }
	       else {	    	   
	    	   int[] d_dx = subdiv(R, L, m);
	    	   for (int x = L[0] ; x < R[0] ; x+=SCALE) {	    	  
	    		   pix[y * W + x / SCALE ] = pack(rgb[0],rgb[1],rgb[2]);
	    		   add(tempRgb, new int[]{d_dx[3], d_dx[4], d_dx[5]});
	    		   rgb = unscale(tempRgb);
	    	   }
	       }
	       add(L,dL_dy);
	       add(R,dR_dy);
	    }
	}
		
		
	    public int[][] scale(int[][] trapezoid) {
	    	int[][] result;
	    	if(null == trapezoid) {
	    		return null;
	    	}
	    	result = new int[4][6];
	    	for(int i = 0; i < trapezoid.length; i++){
		    	for(int j = 0; j < trapezoid[i].length; j++){
		    			result[i][j] = scale(trapezoid[i][j]);		    		
		    	}
	    	}	    	
	    	return result;
	    }
	    
	    public int scale(int data) {
	    	int result = data * SCALE;
	    	return result;
	    }
	    	
	    public int[] unscale(int[] rgb) {
	    	int[] result = div(rgb, SCALE);
	    	return result;
	    }
	    
	    public void add(int[] a, int[] b) {
	    	if(null == a || null == b || a.length != b.length) {
	    		return;
	    	}	    	
	    	for(int i=0; i<a.length; i++) {
	    		a[i] += b[i];
	    	}
	    }
	    
	    public int[] subdiv(int[] a, int[] b, int m) {
	    	
	    	if(null == a || null == b || a.length != b.length) {
	    		return null;
	    	}	
	    	int[] result = new int[a.length];
	    	for(int i=0; i<a.length; i++) {
	    		result[i] = (int)((a[i] - b[i]) * (double)SCALE / m )  ;
	    	}
	    	return result;
	    }
	    
	    public int[] div(int[] a, int m) {
	    	
	    	if(null == a) {
	    		return null;
	    	}	
	    	int[] result = new int[a.length];
	    	for(int i=0; i<a.length; i++) {
	    		result[i] = a[i] / m;
	    	}
	    	return result;
	    }
	    
	    public int pack(int red, int grn, int blu) {
	        return 255<<24 | clip(red,0,255)<<16
	 		      | clip(grn,0,255)<< 8
	 		      | clip(blu,0,255)    ;
	     }
	    
	    int clip(int t, int lo, int hi) { return t<lo ? lo : t>hi ? hi : t; }
         
	    public int[][][] split2Trape(int[][] triangle) {  // int[3][6]
	    	int[][][] result;  //int[2][4][6]
	    	int[] temp;  //int[6]
	    	if(null == triangle) {
	    		return null;
	    	}
	    	//sort
	    	for(int j=1; j <3; j++) {
	    		for(int i = 0; i < triangle.length - j; i++){
	    			if(triangle[i][1] > triangle[i+1][1]) { // y 
	    				temp = new int[6];
	    				temp = Arrays.copyOf(triangle[i], triangle[i].length);
	    				triangle[i] = Arrays.copyOf(triangle[i+1], triangle[i+1].length);
	    				triangle[i+1] = Arrays.copyOf(temp, temp.length);
	    			}		    	
	    		}
	    	}
	    	int[] splitPoint = lerp(triangle[1][1],triangle[0],triangle[2]);
	    	result = new int[2][][];
	    	if(triangle[1][0] < splitPoint[0] ) {
	    		result[0] = new int[][] {triangle[0],triangle[0],triangle[1],splitPoint};
	    		result[1] = new int[][] {triangle[1],splitPoint,triangle[2],triangle[2]};
	    	}
	    	else
	    	{
	    		result[0] = new int[][] {triangle[0],triangle[0],splitPoint,triangle[1]};
	    		result[1] = new int[][] {splitPoint,triangle[1],triangle[2],triangle[2]};
	    	} 	    	
	    	return result;	    	
	    }
	    
	    public int[] lerp(int ty, int[] T, int[] B) {
	    	int[] result;  //int[6]
	    	if(null == T || null == B) {
	    		return null;
	    	}
	    	int Ty = T[1];
	    	int By = B[1];
	    	double factor = (double)(ty - Ty) / (double)(By - Ty);	
	    	result = new int[T.length];
	    	for(int i=0;i < result.length; i++) {	    		
	    		   result[i] = T[i] + (int)(factor * (B[i] - T[i])); 	    		
	    	}
	    	return result;
	    }

}