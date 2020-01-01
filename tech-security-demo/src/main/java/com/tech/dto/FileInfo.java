package com.tech.dto;

public class FileInfo {
	private String absolutePath;

	public FileInfo() {
	}

	public FileInfo(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
}
