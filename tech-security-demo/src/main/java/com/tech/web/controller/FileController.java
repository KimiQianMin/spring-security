package com.tech.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tech.dto.FileInfo;

@RestController
@RequestMapping("/file")
public class FileController {

	@PostMapping
	public FileInfo upload(MultipartFile file) throws IOException {
		System.out.println(file.getName());
		System.out.println(file.getOriginalFilename());
		System.out.println(file.getSize());

		File localFile = new File("fileUploadTest.txt");
		file.transferTo(localFile);
		return new FileInfo(localFile.getAbsolutePath());
	}

	@GetMapping("/{id}")
	public void download(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
		try (FileInputStream inputStream = new FileInputStream(new File(
				"D:\\gpn\\Kimi\\learning\\spring-security-1-master-workspace\\tech-security-demo\\fileUploadTest.txt"));
				ServletOutputStream outputStream = response.getOutputStream();) {
			// 声明响应类型
			response.setContentType("application/x-download");
			// 下载的文件名称
			response.addHeader("Content-Disposition", "attachment;filename-test.txt");
			IOUtils.copy(inputStream, outputStream);
			outputStream.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
