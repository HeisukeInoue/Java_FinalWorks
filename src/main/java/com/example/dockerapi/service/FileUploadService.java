package com.example.dockerapi.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;
import com.example.dockerapi.dto.FileUploadForm;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class FileUploadService {
	
	private final S3Client s3Client;
	
	public FileUploadService(S3Client s3Client) {
		this.s3Client = s3Client;
	}
	
	public String fileUpload(FileUploadForm fileUploadForm, String s3PathName) 
			throws IOException {
		String fileName = "mainimages.png";
		File uploadFile = new File(fileName);
	
		// try-with-resources
		try (FileOutputStream uploadFileStream = new FileOutputStream(uploadFile)) {
			byte[] bytes = fileUploadForm.getMultipartFile().getBytes();
			uploadFileStream.write(bytes);
			
			// AWS SDK v2 の形式で S3 にアップロード
			PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(s3PathName)  // バケット名
				.key(fileName)       // S3内のファイル名（キー）
				.build();
			
			s3Client.putObject(putObjectRequest, RequestBody.fromFile(Paths.get(fileName)));
			uploadFile.delete();

            String wholeFileName = "https://java-finalwork.s3.ap-southeast-2.amazonaws.com/" + fileName;

			return wholeFileName; // アップロードしたファイルのURLを返す
		} catch (S3Exception e) {
			e.printStackTrace();
			throw new IOException("S3アップロードに失敗しました: " + e.getMessage(), e);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
}
