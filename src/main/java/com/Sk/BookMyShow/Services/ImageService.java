package com.Sk.BookMyShow.Services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Sk.BookMyShow.DTOs.ImageInfo;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j 
public class ImageService {

    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5 MB
    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/webp");

    private final Cloudinary cloudinary;

    // single file upload

    public ImageInfo upload(MultipartFile file , String folderPath){
        
        validate(file);

        try{
            Map<?,?> result = cloudinary.uploader().upload(
                file.getBytes(), ObjectUtils.asMap(
                            "folder", folderPath,            // jaise "bookmyshow/theatres"
                            "resource_type", "image",
                            "overwrite", false
                ));
            
                return ImageInfo.builder()
                                .url((String) result.get("secure_url"))
                                .publicId((String) result.get("public_id"))
                                .build();
            
        }
        catch (IOException e) {
            log.error("Cloudinary upload failed", e);
            throw new RuntimeException("Image upload failed");
        }
    }

    public List<ImageInfo> uploadFiles(List<MultipartFile> files,String folderPath){

        if(files == null || files.isEmpty()) {
            log.error("List of Images can't be empty");
            throw new IllegalArgumentException("Files is empty");
        }
        if (files.size() > 10) {
            throw new IllegalArgumentException("Maximum 10 images allowed at once");
        }
        files.forEach( this :: validate);
        List<ImageInfo> uploadedImages = new ArrayList<>();

        try {
            for(MultipartFile file: files){
            ImageInfo imageInfo = upload(file, folderPath);
            uploadedImages.add(imageInfo);
            
        }
        return uploadedImages;
        } catch (Exception e) {
            uploadedImages.forEach(image -> delete(image.getPublicId()));
            throw e;
        }

    }

     // ---------- VALIDATION ----------
    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("File size must be under 5 MB");
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Only JPG, PNG and WEBP images are allowed");
        }
    }

    // ---------delete --------
    private void delete(String publicId){
        try{
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        }catch (IOException e) {
            log.error("Cloudinary delete failed for {}", publicId, e);
        }
    }
}
