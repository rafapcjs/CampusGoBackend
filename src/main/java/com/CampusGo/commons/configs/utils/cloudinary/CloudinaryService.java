package com.CampusGo.commons.configs.utils.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

//      * Sube un archivo a Cloudinary y devuelve la URL segura de acceso.
    public String upload(MultipartFile file, String folder) throws IOException {
        // 1. Convertir a File temporal
        File temp = File.createTempFile("upload-", file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(temp)) {
            fos.write(file.getBytes());
        }

        // 2. Subir con carpeta
        Map<?,?> result = cloudinary.uploader()
                .upload(temp, ObjectUtils.asMap("folder", folder));

        // 3. Borrar temp y devolver URL segura
        temp.delete();
        return result.get("secure_url").toString();
    }
}