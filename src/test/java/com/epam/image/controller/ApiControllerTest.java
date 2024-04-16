package com.epam.image.controller;

import com.epam.image.model.Image;
import com.epam.image.model.ImageStatus;
import com.epam.image.service.DynamoDbService;
import com.epam.image.service.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiController.class)
public class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private S3Service s3Service;

    @MockBean
    private DynamoDbService dynamoDbService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetImagesByLabel() throws Exception {
        String label = "testLabel";
        Image testImage = Image.builder()
                .id(1)
                .objectPath("http://example.com/image.jpg")
                .objectSize("1024")
                .timeAdded(LocalDateTime.now())
                .timeUpdated(LocalDateTime.now())
                .labels(Arrays.asList("testLabel"))
                .status(ImageStatus.RECOGNITION_COMPLETED)
                .build();

        given(dynamoDbService.getImagesByLabel(label)).willReturn(Arrays.asList(testImage));

        mockMvc.perform(MockMvcRequestBuilders.get("/image/" + label)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].objectPath").value("http://example.com/image.jpg"));
    }
}
