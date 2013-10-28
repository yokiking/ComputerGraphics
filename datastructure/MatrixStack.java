package core.datastructure;

public class MatrixStack {
	final static int MATRIX_STACK_SIZE = 100;
	   int matrixStackTop = 0;
	   Matrix matrixStack[] = new Matrix[MATRIX_STACK_SIZE];
	   {
	      for (int n = 0 ; n < MATRIX_STACK_SIZE ; n++)
	         matrixStack[n] = new Matrix();
	   }

	// CLEAR THE STACK BEFORE PROCESSING THIS ANIMATION FRAME

	   public void mclear() {
	      matrixStackTop = 0;
	      matrixStack[0].identity();
	   }

	   // PUSH/COPY THE MATRIX ON TOP OF THE STACK -- RETURN false IF OVERFLOW

	   public boolean mpush() {
	      if (matrixStackTop + 1 >= MATRIX_STACK_SIZE)
		 return false;

	      matrixStack[matrixStackTop + 1].copy(matrixStack[matrixStackTop]);
	      matrixStackTop++;
	      return true;
	   }
	   
	// POP OFF THE MATRIX ON TOP OF THE STACK -- RETURN false IF UNDERFLOW

	   public boolean mpop() {
	      if (matrixStackTop <= 0)
		 return false;

	      --matrixStackTop;
	      return true;
	   }

	   // RETURN THE MATRIX CURRENTLY ON TOP OF THE STACK

	   public Matrix m() {
	      return matrixStack[matrixStackTop];
	   }
	
	
	
}