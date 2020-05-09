package com.bc.fileupload;

import com.bc.fileupload.services.FileStorageHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author hp
 */
@AutoConfigureMockMvc
@SpringBootTest
public class FileuploadTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private FileStorageHandler storageService;

	@Test
	public void shouldListAllFiles() throws Exception {
    
  //              final String relativePath = "";
                
  //              final Path filePath = Paths.get(relativePath);
                
//                final Resource resource = new UrlResource(filePath.toUri());
            
            
//		given(this.storageService.loadFileAsResource(relativePath))
//				.willReturn(resource);

//		this.mvc.perform(get("/")).andExpect(status().isOk())
//				.andExpect(model().attribute("file",
//						Matchers.contains(relativePath)));
	}

	@Test
	public void shouldSaveUploadedFile() throws Exception {
//		MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
//				"text/plain", "Spring Framework".getBytes());
//		this.mvc.perform(multipart("/").file(multipartFile))
//				.andExpect(status().isFound())
//				.andExpect(header().string("Location", "/"));

//		then(this.storageService).should().store(multipartFile);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void should404WhenMissingFile() throws Exception {
//		given(this.storageService.loadAsResource("test.txt"))
//				.willThrow(StorageFileNotFoundException.class);

//		this.mvc.perform(get("/files/test.txt")).andExpect(status().isNotFound());
	}

}