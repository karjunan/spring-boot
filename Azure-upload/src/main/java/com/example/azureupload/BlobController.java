package com.example.azureupload;


import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.sas.AccountSasPermission;
import com.azure.storage.common.sas.AccountSasResourceType;
import com.azure.storage.common.sas.AccountSasService;
import com.azure.storage.common.sas.AccountSasSignatureValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.time.OffsetDateTime;

@RestController
@RequestMapping("blob")
public class BlobController {

    @Value("azure-blob://test/app.png")
    private Resource blobFile;


    @Value("AZURE_STORAGE_CONNECTION_STRING")
    private String conn;

    @GetMapping("/readBlobFile")
    public String readBlobFile() throws IOException {
        return StreamUtils.copyToString(
                this.blobFile.getInputStream(),
                Charset.defaultCharset());
    }

    @PostMapping("/writeBlobFile")
    public String writeBlobFile(@RequestBody String data) throws IOException {
        try (OutputStream os = ((WritableResource) this.blobFile).getOutputStream()) {
            os.write(data.getBytes());
        }
        return "file was updated";
    }

    private BlobServiceClient getBlobServiceClient() {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString("DefaultEndpointsProtocol=https;AccountName=appuploaddaccount;AccountKey=TpXrie4dN0JoqGfmXr2pHsFpZfV9aC4qcKaJNcPANrJ+FUsSWdeUnI23Q4nNrPPZb1QhYe0W0TB8+ASt1CJyPw==;EndpointSuffix=core.windows.net")
                .buildClient();
        return blobServiceClient;
    }
    @GetMapping("/client")
    public String createBlobStorageClient() {

        // Create a BlobServiceClient object using a connection string
        OffsetDateTime expiryTime = OffsetDateTime.now().plusDays(1);
        AccountSasPermission accountSasPermission = new AccountSasPermission().setReadPermission(true);
        AccountSasService services = new AccountSasService().setBlobAccess(true);
        AccountSasResourceType resourceTypes = new AccountSasResourceType().setObject(true);
        AccountSasSignatureValues accountSasValues =
                new AccountSasSignatureValues(expiryTime, accountSasPermission, services, resourceTypes);
        String sasToken = getBlobServiceClient().generateAccountSas(accountSasValues);

        return sasToken;
    }

    @GetMapping("/write")
    public void writeToBlob(){
        BlobContainerClient blobContainerClient = getBlobServiceClient().getBlobContainerClient("test");
        BlobClient blobClient = blobContainerClient.getBlobClient("app1.png");
        System.out.println("\nUploading to Blob storage as blob:\n\t" + blobClient.getBlobUrl());
        blobClient.uploadFromFile("/Users/krishnaarjun/Desktop/bkp/app1.png");
    }



}