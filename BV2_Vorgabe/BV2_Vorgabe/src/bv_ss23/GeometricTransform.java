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

				int srcWidthMinusOne = src.width - 1;
				int srcHeightMinusOne = src.height - 1;

				int x_d= x - dst.width/2;
				int y_d= y - dst.height/2;

				double y_s_s = y_d/(Math.cos(angle_radiant)-y_d*perspectiveDistortion*Math.sin(angle_radiant));
				double x_s_s = x_d*(perspectiveDistortion*Math.sin(angle_radiant)*y_s_s+1);

				double x_s = x_s_s + src.width/2;
				double y_s = y_s_s + src.height/2;

				//naechster Nachbar
				int x_s_n = (int)Math.round(x_s);
				int y_s_n = (int)Math.round(y_s);

				int OrgPos = (y_s_n * src.width + x_s_n);
				int pos = y * dst.width + x;

				if (x_s < 0 || x_s > srcWidthMinusOne || y_s < 0 || y_s > srcHeightMinusOne) {
					dst.argb[pos] = 0x00444444;
				} else {
					dst.argb[pos] = src.argb[OrgPos];
				}



			}
		}

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

		double angle_radiant = Math.toRadians(angle);
		for (int y = 0; y < dst.height; y++) {
			for (int x = 0; x < dst.width; x++) {

				int srcWidthMinusOne = src.width - 1;
				int srcHeightMinusOne = src.height - 1;

				int x_d= x - dst.width/2;
				int y_d= y - dst.height/2;

				double y_s_s = y_d/(Math.cos(angle_radiant)-y_d*perspectiveDistortion*Math.sin(angle_radiant));
				double x_s_s = x_d*(perspectiveDistortion*Math.sin(angle_radiant)*y_s_s+1);

				double x_s = x_s_s + src.width/2;
				double y_s = y_s_s + src.height/2;

				//naechster Nachbar
				int x_s_n = (int)Math.round(x_s);
				int y_s_n = (int)Math.round(y_s);

				int OrgPos = (y_s_n * src.width + x_s_n);
				int pos = y * dst.width + x;

				if (x_s < 0 || x_s > srcWidthMinusOne || y_s < 0 || y_s > srcHeightMinusOne) {
					dst.argb[pos] = 0x00444444;
				} else {
					dst.argb[pos] = src.argb[OrgPos];
				}



			}
		}

 		   	  	  		
		// TODO: implement the geometric transformation using bilinear interpolation
		
		// NOTE: angle contains the angle in degrees, whereas Math trigonometric functions need the angle in radiant
		
 	}

 		   	  	  		
}
 		   	  	  		



