package com.fpedFIND.UserController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.fpedFIND.Service.UserService;
import com.fpedFIND.Entity.Image;
import com.fpedFIND.Paths.GlobalPaths;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import javax.imageio.ImageIO;


@RestController
@RequestMapping("/images")
@CrossOrigin(origins = "*")
public class ImageController {

    @Autowired
    private UserService imageService;

    @GetMapping
    public List<Image> getAllImages() {
        return imageService.getAllImages();
    }

    @GetMapping("/{id}")
    public Image getImageById(@PathVariable Long id) {
        return imageService.getImageById(id);
    }
    
    @GetMapping("/latest")
    public List<Image> getLatestImages() {
        return imageService.getLatestImages();
    }
    
    @PostMapping("/upload")
    public Image saveImage(@RequestParam("data") MultipartFile file, @RequestParam("name") String name) {
        Image data = new Image();
        String originalFilename = name != null && !name.isEmpty() ? name : file.getOriginalFilename();
        data.setName(originalFilename);
        data.setImageDateTime(LocalDateTime.now());
        try {
            // Get the file and save it somewhere
            file.getBytes();

            // Convert the image to PNG format
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            byte[] pngBytes = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();

            // Save the PNG bytes instead of the original image bytes
            data.setData(pngBytes); // set the image data
            Path dir = GlobalPaths.SERVER_FOLDER_PATH;

            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            // Change the file extension to .png
            String pngFilename;
            if (originalFilename.contains(".")) {
                pngFilename = originalFilename.substring(0, originalFilename.lastIndexOf('.')) + ".png";
            } else {
                pngFilename = originalFilename + ".png";
            }
            Path path = dir.resolve(pngFilename);
            Files.write(path, pngBytes);

            data.setImagePath(path.toString());
        } catch (IOException e) {
            // handle exception
        }
        return imageService.saveImage(data);
    }

    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable Long id) {
        // Find the existing image
        Image existingImage = imageService.findById(id);
        if (existingImage == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Delete the image file
            Path path = Paths.get(existingImage.getImagePath());
            if (Files.exists(path)) {
                Files.delete(path);
            }

            // Delete the image object
            imageService.deleteImage(existingImage.getImageId());
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            // handle exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PatchMapping("/update/{oldName}")
    public Image updateImageName(@PathVariable String oldName, @RequestParam("newName") String newName) {
        // Find the existing image
        Image existingImage = imageService.findByName(oldName);
        if (existingImage == null) {
            throw new RuntimeException("Image not found");
        }

        try {
            // Rename the image file
            Path oldPath = Paths.get(existingImage.getImagePath());
            if (Files.exists(oldPath)) {
                String extension = "";
                String fileName = oldPath.getFileName().toString();
                int i = fileName.lastIndexOf('.');
                if (i > 0) {
                    extension = fileName.substring(i+1);
                }
                Path newPath = oldPath.resolveSibling(newName + "." + extension);
                Files.move(oldPath, newPath);
                existingImage.setImagePath(newPath.toString());
            }

            // Update the image object and save it
            existingImage.setName(newName);
            return imageService.saveImage(existingImage);
        } catch (IOException e) {
            // handle exception
        }
        return null;
    }
    
}