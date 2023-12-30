package com.saas.generic.business.bootstrapping.controller;


import com.saas.generic.business.bootstrapping.config.BootstrappingConfig;
import com.saas.generic.business.bootstrapping.service.BootstrapFileProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Slf4j
@RestController
public class FileUploader {

    @Autowired
    BootstrapFileProcessor bootstrapFileProcessor;

    @GetMapping("/hello")
    public String sayHello(){
        log.info(" In Hello mapping ... ");
        return "Hello there!";
    }

    @PostMapping("/files/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        log.info(" In /files/upload mapping ... ");
        bootstrapFileProcessor.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

}
