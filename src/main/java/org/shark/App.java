package org.shark;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.io.File;
import java.net.URI;

import org.shark.infra.consumer.QrCodeConsumer;
import org.shark.infra.producer.LinkProducer;

public class App {

	public static void main(String[] args) {
		String queueUrl = "http://localhost:4566/000000000000/qr-queue";

		new File("output").mkdir();

		SqsClient client = SqsClient.builder()
			.region(Region.US_EAST_1)
			.endpointOverride(URI.create("http://localhost:4566"))
			.build();

		LinkProducer producer = new LinkProducer(client, queueUrl);
		QrCodeConsumer consumer = new QrCodeConsumer(client, queueUrl);

		producer.sendLink("https://github.com/");
		producer.sendLink("https://openai.com/");
		producer.sendLink("https://example.com/");

		System.out.println("== Processando fila de links ==");
		consumer.processMessages();

		client.close();
	}
}