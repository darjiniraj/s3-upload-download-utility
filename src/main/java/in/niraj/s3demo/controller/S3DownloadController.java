package in.niraj.s3demo.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.MultipleFileDownload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * created by Niraj on Oct, 2019
 */

@RestController
@CrossOrigin
public class S3DownloadController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${s3.bucket}")
    private String bucketName;

    @Value("${file.download-path}")
    private String filePath;

    @Value("${s3.prefix}")
    private String prefix;

    @Value("${s3.key}")
    private String keyName;


    @Autowired
    private AmazonS3 s3client;

    @GetMapping("/download-directory")
    public String downloadDirectoryFromS3() throws InterruptedException {
        logger.info("Download Process Inititated");

        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3client).build();
        logger.info("Download Started");
        try {
            /*if you want to download specific directory from bucket then give the name of directory in keyPrefix
             * if you want to download whole bucket, put "" in keyPrefix*/
            MultipleFileDownload download = transferManager.downloadDirectory(bucketName, prefix, new File(filePath));
            download.waitForCompletion();
        } catch (AmazonServiceException e) {
            logger.error("Error occured while downloading {} ", e.getErrorMessage());
        }
        logger.info("Download Completed.");
        return "Success";
    }

    @GetMapping("/download-file")
    public String downloadFileFromS3() throws InterruptedException {
        logger.info("Download Process Inititated");

        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3client).build();
        logger.info("Download Started");
        try {
            /*Give the keyname (specific file) which you want to download*/
            Download download = transferManager.download(bucketName, keyName, new File(filePath));
            download.waitForCompletion();
        } catch (AmazonServiceException e) {
            logger.error("Error occured while downloading {} ", e.getErrorMessage());
        }
        logger.info("Download Completed.");
        return "Success";
    }

}
