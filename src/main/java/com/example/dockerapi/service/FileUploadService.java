package com.example.dockerapi.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;
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
		// 同時アップロード時の上書きを防ぐため一意のキーを生成
		String s3ObjectKey = UUID.randomUUID().toString().replace("-", "").substring(0, 16) + ".png";
		File uploadFile = new File(s3ObjectKey);

		try (FileOutputStream uploadFileStream = new FileOutputStream(uploadFile)) {
			byte[] bytes = fileUploadForm.getMultipartFile().getBytes();
			uploadFileStream.write(bytes);

			PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(s3PathName)
				.key(s3ObjectKey)
				.build();

			s3Client.putObject(putObjectRequest, RequestBody.fromFile(Paths.get(s3ObjectKey)));
			uploadFile.delete();

			// レスポンス用の公開URL（クライアント・DB保存用）
			String publicUrl = "https://java-finalwork.s3.ap-southeast-2.amazonaws.com/" + "mainimages.png";
			return publicUrl;
		} catch (S3Exception e) {
			e.printStackTrace();
			throw new IOException("S3アップロードに失敗しました: " + e.getMessage(), e);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
}
