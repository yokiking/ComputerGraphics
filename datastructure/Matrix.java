package core.datastructure;

import java.util.Arrays;

public class Matrix {
	public double data[][] = new double[4][4];

	public void set(int i, int j, double value) {
		data[i][j] = value;
	} // SET ONE VALUE

	
	public double get(int i, int j) {
		return data[i][j];
	} // GET ONE VALUE

	
	public void copy(Matrix src) {
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				data[row][col] = src.get(row, col);
			}
		}
	}

	public void multiply(double[][] src) {
        double temp[][] = new double[4][4];
        
        // FIRST COPY MY ORIGINAL DATA TO A TEMPORARY LOCATION
        for (int row = 0; row < 4; row++) {
        	for (int col = 0; col < 4; col++) {
        		temp[row][col] = data[row][col];
        	}
        }

        // USE TEMP TO DO THE MATRIX MULTIPLICATION
        for (int row = 0; row < 4; row++) {
        	for (int col = 0; col < 4; col++) {
        		data[row][col] = 0;
        		for (int i = 0; i < 4; i++) {
        			data[row][col] += temp[row][i] * src[i][col];
        		}
        	}
		}
	}
	
	public void multiply(Matrix src) {
		multiply(src.data);
    }
	
	
	public void transform(double src[], double dst[]) {
		for (int row = 0; row < dst.length; row++) {
			dst[row] = 0;
			for (int i = 0; i < src.length; i++) {
				dst[row] += data[row][i] * src[i];
			}
		}
	}
	
	private void createClearData(double dst[][]) {
		for(int i=0;i<dst.length;i++)
			for(int j=0;j<dst[i].length;j++)
				dst[i][j] = 0;
//    	0	0	0	0
//    	0	0	0	0
//    	0	0	0	0
//    	0	0	0	0
		
	}
	
	public void clear() {
		createClearData(data);
	}
	
	private void createIdentityData(double dst[][]) {
    	dst[0][0] = 1;
    	dst[1][1] = 1;
    	dst[2][2] = 1;
    	dst[3][3] = 1;
//    	1	0	0	0
//    	0	1	0	0
//    	0	0	1	0
//    	0	0	0	1
    }
  
    public void identity() {
       clear();
	   createIdentityData(data);
    }
    
    private void createPerspectiveData(double dst[][],double f) {
    	dst[0][0] = 1;
    	dst[1][1] = 1;
    	dst[2][2] = 1;
    	dst[3][3] = 1;
    	dst[2][3] = -1/f;
//    	1	0	0	0
//    	0	1	0	0
//    	0	0	1	0
//    	0	0	-1/f	1
		
	}
	
	public void perspective(double f) {
		createPerspectiveData(data,f);
	}
    
	private void createTranslationData(double a, double b, double c, double dst[][]) {
		createIdentityData(dst);
	    dst[0][3] = a;
	    dst[1][3] = b;
	    dst[2][3] = c;
//	    1	0	0	a
//	    0	1	0	b
//	    0	0	1	c
//	    0	0	0	1
    }

	public void translate(double a, double b, double c) {
		double[][] temp = new double[4][4];
		createTranslationData(a, b, c, temp);
		multiply(temp);
	}
	
	private void createScaleData(double a, double b, double c, double dst[][]) {
		createIdentityData(dst);
	    dst[0][0] = a;
	    dst[1][1] = b;
	    dst[2][2] = c;
//	    a	0	0	0
//	    0	b	0	0
//	    0	0	c	0
//	    0	0	0	1
	}
			
	public void scale(double a, double b, double c) {
		double[][] temp = new double[4][4];
		createScaleData(a, b, c, temp);
		multiply(temp);
	}
	
	private void createXRotationData(double theta, double dst[][]) {
    	createIdentityData(dst);
    	dst[1][1] =  Math.cos(theta);
    	dst[1][2] = -Math.sin(theta);
    	dst[2][1] =  Math.sin(theta);
    	dst[2][2] =  Math.cos(theta);
//    	1	    0	   0	0
//    	0	cos(¦È)	-sin(¦È)	0
//    	0	sin(¦È)	cos(¦È)	0
//    	0	    0	   0	1
    }
    
	public void rotateX(double theta) {
		double[][] temp = new double[4][4];
		createXRotationData(theta, temp);
		multiply(temp);
	}
	
	
	private void createYRotationData(double theta, double dst[][]) {
		createIdentityData(dst);
    	dst[0][0] =  Math.cos(theta);
    	dst[2][0] = -Math.sin(theta);
    	dst[0][2] =  Math.sin(theta);
    	dst[2][2] =  Math.cos(theta);
//    	cos(¦È)	0	sin(¦È)	0
//    	0	    1	   0	0
//    	-sin(¦È)	0	cos(¦È)	0
//    	0	    0	   0	1
	}
	
	public void rotateY(double theta) {
		double[][] temp = new double[4][4];
		createYRotationData(theta, temp);
		multiply(temp);
	}
	
	private void createZRotationData(double theta, double dst[][]) {
		createIdentityData(dst);
    	dst[0][0] =  Math.cos(theta);
    	dst[0][1] = -Math.sin(theta);
    	dst[1][0] =  Math.sin(theta);
    	dst[1][1] =  Math.cos(theta);
//    	cos(¦È)	-sin(¦È)	0	0
//    	sin(¦È)	cos(¦È)	0	0
//    	0	    0	    1	0
//    	0	    0	    0	1
	}
	
	public void rotateZ(double theta) {
		double[][] temp = new double[4][4];
		createZRotationData(theta, temp);
		multiply(temp);
	}
	
	public void invert() {
		double[][] temp = new double[4][4];
		// COMPUTE ADJOINT COFACTOR MATRIX FOR THE ROTATION/SCALE 3x3 SUBMATRIX
		int iu, iv, ju, jv, i, j;

		for (i = 0; i < 3; ++i)
			for (j = 0; j < 3; ++j) {
				iu = (i + 1) % 3;
				iv = (i + 2) % 3;
				ju = (j + 1) % 3;
				jv = (j + 2) % 3;
				temp[j][i] = data[iu][ju] * data[iv][jv]
						- data[iu][jv] * data[iv][ju];
			}

		// RENORMALIZE BY DETERMINANT TO INVERT ROTATION/SCALE SUBMATRIX
		double det = data[0][0] * temp[0][0] + data[1][0] * temp[0][1] + data[2][0] * temp[0][2];
		for (i = 0; i < 3; ++i)
			for (j = 0; j < 3; ++j)
				temp[i][j] = temp[i][j] / det;

		// INVERT TRANSLATION
		for (i = 0; i < 3; ++i)
			temp[i][3] = -1 * temp[i][0] * data[0][3] - temp[i][1]
					* data[1][3] - temp[i][2] * data[2][3];

		// NO PERSPECTIVE
		for (i = 0; i < 4; ++i)
			temp[3][i] = (i < 3 ? 0 : 1);
		
		for (i = 0; i < 4; ++i)
			for (j = 0; j < 4; ++j)
				data[i][j] = temp[i][j];
					
	}
	
	public void transpose() {
		for (int i = 0; i < 4; ++i) {
			for (int j = i + 1; j < 4; ++j) {
				double val = data[i][j];
				data[i][j] = data[j][i];
				data[j][i] = val;
			}
		}
	}
	
		    
	   
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < 4; ++i) {
			sb.append(Arrays.toString(data[i]));
			sb.append("\n");
		}
		return sb.toString();
	}
	
}
