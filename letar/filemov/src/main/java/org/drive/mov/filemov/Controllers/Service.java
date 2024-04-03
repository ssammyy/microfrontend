package org.drive.mov.filemov.Controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Value;
import com.google.api.services.drive.DriveScopes;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.drive.mov.filemov.Response.Res;
import com.google.api.services.drive.Drive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Objects;

@Component
@Slf4j
public class Service {
    @Autowired
    private Environment environment;


    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private String SERVICE_ACCOUNT_KEY_PATH;

    @PostConstruct
    private void initializeServiceAccountKeyPath() {
        SERVICE_ACCOUNT_KEY_PATH = getPathGoogleCredential();
    }


    private String getPathGoogleCredential() {
        String currentDir = System.getProperty("user.dir");

        String dir = environment.getProperty("configurations.google.json.credentials.path");
        Path filePath = null;
        if (dir != null) {
            filePath = Paths.get(dir, "creds.json");
        } else
            log.info("xxxxxx failed to get the path from cofig xxxxx");
        return filePath.toString();

    }

    public Res uploadFileToDrive(File file, String fileName) throws GeneralSecurityException, IOException {
        Res res = new Res();

        try {
            String mimeType = Files.probeContentType(file.toPath());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            String folderID = environment.getProperty("configurations.google.drive.folderId");
            Drive drive = createDriveService();
            com.google.api.services.drive.model.File metadata = new com.google.api.services.drive.model.File();
            metadata.setName(fileName);
            metadata.setParents(Collections.singletonList(folderID));
            FileContent mediaContent = new FileContent(mimeType, file);
            com.google.api.services.drive.model.File uploadedFile = drive
                    .files()
                    .create(metadata, mediaContent)
                    .setFields("id")
                    .execute();
            String imageUrl = "https://drive.google.com/uc?export=view&id=" + uploadedFile.getId();
            moveFilesToBackup(file);
            res.setStatus(200);
            res.setMessage("Success");
            res.setUrl(imageUrl);
        } catch (Exception e) {
            log.warn("Error occurred try again later \n Error message :: " + e.getMessage());
            res.setStatus(500);
            res.setMessage("an error occurred try again later" + e.getMessage());
        }

        return res;
    }

    private Drive createDriveService() throws GeneralSecurityException, IOException {
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(SERVICE_ACCOUNT_KEY_PATH))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));
        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential)
                .build();

    }


    /***
     * upload all files in a given folder
     */

    public Res uploadFolderToDrive() {
        Res res = new Res();
        File folder = new File(Objects.requireNonNull(environment.getProperty("configurations.google.folder.path")));
        File[] files = folder.listFiles();

        try {
            for (File file : files) {
                if (file.isFile()) {
                    uploadFileToDrive(file, file.getName());
                }
            }
            res.setStatus(200);
            res.setMessage("Success");
        } catch (Exception e) {
            log.warn("Error occurred while uploading folder to Google Drive: " + e.getMessage());
            res.setStatus(500);
            res.setMessage("An error occurred, " + e.getMessage());
        }

        return res;

    }
    private void moveFilesToBackup(File file) throws Exception {
        String backupFolderPath = environment.getProperty("configurations.backup.folder.path");
        Path sourcePath = file.toPath();
        Path targetPath = null;
        if (backupFolderPath != null) {
            targetPath = Paths.get(backupFolderPath, file.getName());
        } else {
            log.warn("Failed to backup the file");

        }
        Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        log.info("File moved to backup folder: " + targetPath);
    }

}
