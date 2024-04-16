package com.epam.image.service;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.epam.image.model.Image;
import com.epam.image.model.ImageStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DynamoDbService {

    private final DynamoDbClient dynamoDbClient;
    private final String tableName;

    public DynamoDbService(DynamoDbClient dynamoDbClient,
                           @Value("${aws.dynamodb.tableName}") String tableName) {
        this.dynamoDbClient = dynamoDbClient;
        this.tableName = tableName;
    }

    public List<Image> getImagesByLabel(String label) {
        QueryRequest queryRequest = QueryRequest.builder()
                .tableName(tableName)
                .keyConditionExpression("LabelValue = :v_label")
                .expressionAttributeValues(Collections.singletonMap(":v_label", AttributeValue.builder().s(label).build()))
                .build();

        QueryResponse queryResponse = dynamoDbClient.query(queryRequest);

        return queryResponse.items().stream()
                .map(this::mapToImage)
                .collect(Collectors.toList());
    }

    private Image mapToImage(Map<String, AttributeValue> item) {
        return Image.builder()
                .id(Integer.parseInt(item.get("id").n()))
                .objectPath(item.get("objectPath").s())
                .objectSize(item.get("objectSize").s())
                .timeAdded(LocalDateTime.parse(item.get("timeAdded").s()))
                .timeUpdated(LocalDateTime.parse(item.get("timeUpdated").s()))
                .labels(Arrays.asList(item.get("labels").ss().toArray(new String[0])))
                .status(ImageStatus.valueOf(item.get("status").s()))
                .build();
    }
}

