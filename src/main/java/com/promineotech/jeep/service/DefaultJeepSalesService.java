package com.promineotech.jeep.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.promineotech.jeep.dao.JeepSalesDao;
import com.promineotech.jeep.entity.Image;
import com.promineotech.jeep.entity.ImageMimeType;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

public class DefaultJeepSalesService implements JeepSalesService {

	@Autowired
	private JeepSalesDao jeepSalesDao;

	@Transactional(readOnly = true)
	@Override
	public Image retrieveImage(String imageId) {
		return jeepSalesDao.retrieveImage(imageId)
				.orElseThrow(() -> new NoSuchElementException(
						"Couldn't find image with ID = " + imageId));
	}
	
	@Transactional
	@Override
	public String uploadImage(MultipartFile file, Long modelPK) {
		String imageId = UUID.randomUUID().toString();

		try(InputStream inputStream = file.getInputStream()) {
			BufferedImage bufferedImage = ImageIO.read(inputStream);
			
			//@formatter: off
			Image image = Image.builder()
					.modelFK(modelPK)
					.imageId(imageId)
					.width(bufferedImage.getWidth())
					.height(bufferedImage.getHeight())
					.mimeType(ImageMimeType.IMAGE_JPEG)
					.name(file.getOriginalFilename())
					.data(toByteArray(bufferedImage, "jpeg"))
					
					.build();
			//@formatter: on
			
			// I created a reference 
			jeepSalesDao.saveImage(image);
			
			// I'm trying to figure out why I'm getting a 500 instead of 201 @7:07 he had a debug.log result: no log
//			log.debug("uploading image with ID={}", imageId);
			
			return imageId;
			
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		
		
	}
	
	private byte[] toByteArray(BufferedImage bufferedImage, String renderType) {
		
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
			if(!ImageIO.write(bufferedImage, renderType, baos)) {
				throw new RuntimeException("Could not write to image buffer");
			}
			return baos.toByteArray();
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			} // catch	
		
			
			
	} // byte[]

	@Transactional(readOnly = true)
	@Override
	public List<Jeep> fetchJeeps(JeepModel model, String trim) {
		log.info("The fetchJeeps method was called with model={} and trim={}", model, trim);

		List<Jeep> jeeps = jeepSalesDao.fetchJeeps(model, trim);
//		The method fetchJeeps(JeepModel, String) from the type JeepSalesDao refers to the missing type List

		if(jeeps.isEmpty() ) {
			String msg = String.format("No jeeps found with model=%s and trim=%s", model, trim);
			
			throw new NoSuchElementException(msg);
		}
		
		// sort
		Collections.sort(jeeps);
		return jeeps;
	}



	

}
