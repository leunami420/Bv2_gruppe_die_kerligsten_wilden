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
		int A_argb;
		int B_argb;
		int C_argb;
		int D_argb;
		int srcWidthMinusOne = src.width - 1;
		int srcHeightMinusOne = src.height - 1;

		for (int y = 0; y < dst.height; y++) {
			for (int x = 0; x < dst.width; x++) {

				int pos = y * dst.width + x;

				int x_d= x - dst.width/2;
				int y_d= y - dst.height/2;

				double y_s_temp = y_d/(Math.cos(angle_radiant)-y_d*perspectiveDistortion*Math.sin(angle_radiant));
				double x_s_temp = x_d*(perspectiveDistortion*Math.sin(angle_radiant)*y_s_temp+1);

				double x_s = x_s_temp + src.width/2;
				double y_s = y_s_temp + src.height/2;

				//naechster Nachbar
				int x_s_downrounded = (int) Math.floor(x_s);
				int x_s_uprounded = x_s_downrounded + 1;
				int y_s_downrounded = (int) Math.floor(y_s);
				int y_s_uprounded = y_s_downrounded + 1;

				double h = x_s - x_s_downrounded;
				double v = y_s - y_s_downrounded;

				if (x_s_downrounded >= 0 && y_s_downrounded >= 0 && x_s_downrounded <= srcWidthMinusOne && y_s_downrounded <= srcHeightMinusOne) {
					A_argb = src.argb[y_s_downrounded * src.width + x_s_downrounded];
				} else {
					A_argb = 0xFFFFFFFF;
				}

				if (x_s_uprounded >= 0 && y_s_downrounded >= 0 && x_s_uprounded <= srcWidthMinusOne && y_s_downrounded <= srcHeightMinusOne) {
					B_argb = src.argb[y_s_downrounded * src.width + x_s_uprounded];
				} else {
					B_argb = 0xFFFFFFFF;
				}

				if (x_s_downrounded >= 0 && y_s_uprounded >= 0 && x_s_downrounded <= srcWidthMinusOne && y_s_uprounded <= srcHeightMinusOne) {
					C_argb = src.argb[y_s_uprounded * src.width + x_s_downrounded];
				} else {
					C_argb = 0xFFFFFFFF;
				}

				if (x_s_uprounded >= 0 && y_s_uprounded >= 0 && x_s_uprounded <= srcWidthMinusOne && y_s_uprounded <= srcHeightMinusOne) {
					D_argb = src.argb[y_s_uprounded * src.width + x_s_uprounded];
				} else {
					D_argb = 0xFFFFFFFF;
				}


				//red
				int r = BiliniarInterpolate(h,v,(A_argb >> 16) & 0xff,(B_argb >> 16) & 0xff,(C_argb >> 16) & 0xff,(D_argb >> 16) & 0xff);
				//green
				int g = BiliniarInterpolate(h,v,(A_argb >> 8) & 0xff,(B_argb >> 8) & 0xff,(C_argb >> 8) & 0xff,(D_argb >> 8) & 0xff);
				//blue
				int b = BiliniarInterpolate(h,v,A_argb & 0xff,B_argb & 0xff,C_argb & 0xff,D_argb & 0xff);


				dst.argb[pos] = 0xFF000000 + (( r & 0xff) << 16) + ((g & 0xff) << 8) + (b & 0xff);




			}
		}

 		   	  	  		
		// TODO: implement the geometric transformation using bilinear interpolation
		
		// NOTE: angle contains the angle in degrees, whereas Math trigonometric functions need the angle in radiant
		
 	}

	 private int BiliniarInterpolate(double h,double v,int A,int B,int C,int D){
		double result = A*(1-h)*(1-v)+B*h*(1-v)+C*(1-h)*v+D*h*v;
		return (int) result;
	 }

 		   	  	  		
}
 		   	  	  		



