package in.niraj.s3demo.controller;

import com.amazonaws.auth.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.TransferProgress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

import static java.lang.String.valueOf;

/**
 * created by Niraj on Oct, 2019
 */

@RestController
@CrossOrigin
public class S3UploadController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${s3.bucket}")
    private String bucketName;

    @Value("${file.upload-path}")
    private String filePath;

    @Value("${s3.prefix}")
    private String prefix;

    @Autowired
    private AmazonS3 s3client;


    @GetMapping("/upload")
    public String uploadDirectoryToS3() {
        logger.info("Upload started");

        TransferManager transferManager =
                TransferManagerBuilder.standard().withS3Client(s3client).build();

        transferManager.uploadDirectory(bucketName, prefix,
                new File(filePath), true);
        logger.info("Upload Completed.");
        return "Success";
    }

}
