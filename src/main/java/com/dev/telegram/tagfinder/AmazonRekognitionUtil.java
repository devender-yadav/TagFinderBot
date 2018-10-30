package com.dev.telegram.tagfinder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;

public class AmazonRekognitionUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(AmazonRekognitionUtil.class);

	private static final String ACCESS_KEY = "<ACCESS_KEY>";
	private static final String SECRET_KEY = "<SECRET_KEY>";

	private AmazonRekognitionUtil() {
	}

	private static AmazonRekognition rekognitionClient;

	private static synchronized void initialize() {
		if (rekognitionClient == null) {
			AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
			rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion(Regions.US_EAST_1)
					.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
			LOGGER.info("AWS RekognitionClient initialized!");
		}
	}

	public static List<String> getLabels(ByteBuffer byteBuffer) {
		initialize();
		DetectLabelsRequest request = new DetectLabelsRequest().withImage(new Image().withBytes(byteBuffer))
				.withMaxLabels(5).withMinConfidence(90F);
		DetectLabelsResult result = rekognitionClient.detectLabels(request);

		List<Label> labels = result.getLabels();

		List<String> labelNames = new ArrayList<>();

		for (Label label : labels) {
			labelNames.add(label.getName());
		}
		return labelNames;

	}

}
