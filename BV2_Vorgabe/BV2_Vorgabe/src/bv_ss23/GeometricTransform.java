// BV Ue2 SS2023 Vorgabe
//
// Copyright (C) 2023 by Klaus Jung
// All rights reserved.
// Date: 2023-03-23
 		   	  	  		

package bv_ss23;


public class GeometricTransform {
 		   	  	  		
	public enum InterpolationType { 
		NEAREST("Nearest Neighbour"), 
		BILINEAR("Bilinear");
		
		private final String name;       
	    private InterpolationType(String s) { name = s; }
	    public String toString() { return this.name; }
	};
	
	public void perspective(RasterImage src, RasterImage dst, double angle, double perspectiveDistortion, InterpolationType interpolation) {
		switch(interpolation) {
		case NEAREST:
			perspectiveNearestNeighbour(src, dst, angle, perspectiveDistortion);
			break;
		case BILINEAR:
			perspectiveBilinear(src, dst, angle, perspectiveDistortion);
			break;
		default:
			break;	
		}
		
	}
 		   	  	  		
	/**
	 * @param src source image
	 * @param dst destination Image
	 * @param angle rotation angle in degrees
	 * @param perspectiveDistortion amount of the perspective distortion 
	 */
	public void perspectiveNearestNeighbour(RasterImage src, RasterImage dst, double angle, double perspectiveDistortion) {

		double angle_radiant = Math.toRadians(angle);
		for (int y = 0; y < dst.height; y++) {
			for (int x = 0; x < dst.width; x++) {
				int x_d= x- dst.width/2;
				int y_d= y- dst.height/2;

			}}

		// TODO: implement the geometric transformation using nearest neighbour image rendering
		
		// NOTE: angle contains the angle in degrees, whereas Math trigonometric functions need the angle in radiant
		
	}


	/**
	 * @param src source image
	 * @param dst destination Image
	 * @param angle rotation angle in degrees
	 * @param perspectiveDistortion amount of the perspective distortion 
	 */
	public void perspectiveBilinear(RasterImage src, RasterImage dst, double angle, double perspectiveDistortion) {
 		   	  	  		
		// TODO: implement the geometric transformation using bilinear interpolation
		
		// NOTE: angle contains the angle in degrees, whereas Math trigonometric functions need the angle in radiant
		
 	}

 		   	  	  		
}
 		   	  	  		



