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
	/**
	 * The locale of the image face annotation
	 */
	private String locale;
	
	/**
	 * The score of the image face annotation
	 */
	private float score;
	
	/**
	 * The x1 of the image face annotation
	 */
	private int x1;

	/**
	 * The y1 of the image face annotation
	 */
	private int y1;
	
	/**
	 * The x2 of the image face annotation
	 */
	private int x2;
	
	/**
	 * The y2 of the image face annotation
	 */
	private int y2;
	
	/**
	 * The x3 of the image face annotation
	 */
	private int x3;
	
	/**
	 * The y3 of the image face annotation
	 */
	private int y3;
	
	/**
	 * The x4 of the image face annotation
	 */
	private int x4;
	
	/**
	 * The y4 of the image face annotation
	 */
	private int y4;
	
	/**
	 * The joy likelihood of the image face annotation
	 */
    private String joyLikelihood;
	
	/**
	 * The sorrow likelihood of the image face annotation
	 */
    private String sorrowLikelihood;

	/**
	 * The anger likelihood of the image face annotation
	 */
    private String angerLikelihood;
	
	/**
	 * The surprise likelihood of the image face annotation
	 */
    private String surpriseLikelihood;
	
	/**
	 * The under exposed likelihood of the image face annotation
	 */
    private String underExposedLikelihood;
	
	/**
	 * The blurred likelihood of the image face annotation
	 */
    private String blurredLikelihood;
	
	/**
	 * The headwear likelihood of the image face annotation
	 */
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
