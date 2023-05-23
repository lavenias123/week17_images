package com.promineotech.jeep.entity;

public enum ImageMimeType {
	IMAGE_JPEG("image/jpeg");
//	IMAGE_PNG("image/png");
	private String mimeType;
	
	private ImageMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getMimeType() {
		return mimeType;
		/**
		 * @return
		 */
	}
	
	// this helper is created to convert String IMAGE_JPEG to enum ("image/jpeg") 
	public static ImageMimeType fromString(String mimeType) {
		
		// loop thru all mimeTypes
		for(ImageMimeType imt : ImageMimeType.values())
				if(imt.getMimeType().equals(mimeType)) {
					return imt;
				}
				
		return null;
	}
}
