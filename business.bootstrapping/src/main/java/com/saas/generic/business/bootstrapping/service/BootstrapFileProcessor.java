package com.saas.generic.business.bootstrapping.service;

import com.saas.generic.business.bootstrapping.config.BootstrappingConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class BootstrapFileProcessor {

    @Autowired
    BootstrappingConfig bootstrappingConfig;


    public void store(MultipartFile file) {
        try {
            /*if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }*/
            log.info("bootstrappingConfig:  "+bootstrappingConfig.getFeedName());
            log.info("bootstrappingConfig:  "+bootstrappingConfig.getFeedLocationToSave());
            Path rootLocation = Paths.get("" +bootstrappingConfig.getFeedLocationToSave());
            Path destinationFile = rootLocation.resolve(
                    Paths.get(bootstrappingConfig.getFeedName()))
                    .normalize().toAbsolutePath();
            log.info(destinationFile.toAbsolutePath().toString());
            if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
                // This is a security check
                /*throw new StorageException(
                        "Cannot store file outside current directory.");*/
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            log.error("exception whilst saving file ..."+e.getMessage());
            System.out.println(" IOException .... "+e.getMessage());
            //throw new StorageException("Failed to store file.", e);
        }
    }


}
