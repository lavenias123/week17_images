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
}
