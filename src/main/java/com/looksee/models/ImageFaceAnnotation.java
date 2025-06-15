package com.looksee.models;

import java.util.UUID;

import com.google.cloud.vision.v1.BoundingPoly;
import com.google.cloud.vision.v1.Likelihood;

import lombok.Getter;
import lombok.Setter;

/**
 * Stores image face annotations
 */
@Getter
@Setter
public class ImageFaceAnnotation extends LookseeObject{
	
	private String locale;
	private float score;
	
	private int x1;
	private int y1;
	
	private int x2;
	private int y2;
	
	private int x3;
	private int y3;
	
	private int x4;
	private int y4;
	
    private String joyLikelihood;
    private String sorrowLikelihood;
    private String angerLikelihood;
    private String surpriseLikelihood;
    private String underExposedLikelihood;
    private String blurredLikelihood;
    private String headwearLikelihood;
    
	/**
	 * Constructs a new {@link ImageFaceAnnotation}
	 */
	public ImageFaceAnnotation() {
		setLocale("");
		setScore(0.0F);
		setX1(0);
		setY1(0);
		
		setX2(0);
		setY2(0);
		
		setX3(0);
		setY3(0);
		
		setX4(0);
		setY4(0);
		
		setJoyLikelihood("");
		setSorrowLikelihood("");
		setAngerLikelihood("");
		setSurpriseLikelihood("");
		setUnderExposedLikelihood("");
		setBlurredLikelihood("");
		setHeadwearLikelihood("");
    }

	public ImageFaceAnnotation(Likelihood angerLikelihood,
								Likelihood joyLikelihood,
								Likelihood blurredLikelihood,
								Likelihood headwearLikelihood,
								Likelihood sorrowLikelihood,
								Likelihood surpriseLikelihood,
								Likelihood underExposedLikelihood,
								BoundingPoly bounding_poly
	) {
		setAngerLikelihood(angerLikelihood.toString());
		setJoyLikelihood(joyLikelihood.toString());
		setBlurredLikelihood(blurredLikelihood.toString());
		setHeadwearLikelihood(headwearLikelihood.toString());
		setSorrowLikelihood(sorrowLikelihood.toString());
		setSurpriseLikelihood(surpriseLikelihood.toString());
		setUnderExposedLikelihood(underExposedLikelihood.toString());
		
		setX1(bounding_poly.getVerticesList().get(0).getX());
		setY1(bounding_poly.getVerticesList().get(0).getY());
		
		setX2(bounding_poly.getVerticesList().get(1).getX());
		setY2(bounding_poly.getVerticesList().get(1).getY());
		
		setX3(bounding_poly.getVerticesList().get(2).getX());
		setY3(bounding_poly.getVerticesList().get(2).getY());

		setX4(bounding_poly.getVerticesList().get(3).getX());
		setY4(bounding_poly.getVerticesList().get(3).getY());
	}

	/**
	 * Generates a key for the image face annotation
	 *
	 * @return the key for the image face annotation
	 */
	@Override
	public String generateKey() {
		return "imagefaceannotation::"+UUID.randomUUID();
	}
}
