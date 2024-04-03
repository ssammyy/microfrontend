package org.drive.mov.filemov.Controllers;

import com.google.api.client.util.Value;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drive.mov.filemov.Response.Res;
import org.drive.mov.filemov.Utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@Slf4j
public class Controller {
    @Autowired
    private Service service;
    @Autowired
    Utilities utils;

    @PostConstruct
    private Boolean runValidityChecks(){
        return utils.isActivated();
    }


    @PostMapping("/upload-to-drive")
    public Object handleFileUpload(@RequestParam("image")MultipartFile file) throws IOException, GeneralSecurityException {
        if( file. isEmpty() )
            return "file is empty";

        if(runValidityChecks()){
            File tempFile = File.createTempFile("temp", null);
            file.transferTo(tempFile);
            Res res = service.uploadFileToDrive(tempFile, file.getOriginalFilename());
            log.info("upload success " + res);
            return res;
        }else{

            return "api tire expired, contact admin";
        }



    }


    @PostMapping("/upload-folder-to-drive")
    public Object handleFolderUpload(){
        if(runValidityChecks()){
            Res res = service.uploadFolderToDrive();

            return res;
        }else{
            return "api tire expired, contact admin";
        }

    }

}
