package com.dev.aws.ml.utils;

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
import com.dev.telegram.tagfinder.utils.Constants;
import com.dev.telegram.tagfinder.utils.PropertyReader;

public class AmazonRekognitionUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(AmazonRekognitionUtil.class);

	private AmazonRekognitionUtil() {
	}

	private static AmazonRekognition rekognitionClient;

	private static synchronized void initialize() {
		if (rekognitionClient == null) {
			AWSCredentials credentials = new BasicAWSCredentials(
					PropertyReader.getProperty(Constants.AWS_REKOGNITION_ACCESSKEY),
					PropertyReader.getProperty(Constants.AWS_REKOGNITION_SECRETKEY));
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
