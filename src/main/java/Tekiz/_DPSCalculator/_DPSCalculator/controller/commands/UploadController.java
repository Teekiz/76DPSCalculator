package Tekiz._DPSCalculator._DPSCalculator.controller.commands;

import Tekiz._DPSCalculator._DPSCalculator.util.upload.LocalToDBUploader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
@Tag(name = "Upload Controller API", description = "A controller used to upload local JSON files a database.")
public class UploadController
{
	private final LocalToDBUploader localToDBUploader;

	@Autowired
	public UploadController(LocalToDBUploader localToDBUploader)
	{
		log.warn("Upload controller created.");
		this.localToDBUploader = localToDBUploader;
	}

	@GetMapping("/upload")
	@Operation(summary = "Upload local documents to the database.", description = "Takes all locally stored files and uploads them to the provided database.")
	public ResponseEntity<String> uploadLocalToDB() throws IOException
	{
		localToDBUploader.save();
		return ResponseEntity.ok("Files have been uploaded.");
	}
}
