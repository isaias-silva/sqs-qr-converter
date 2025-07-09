package org.shark;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.io.File;
import java.net.URI;
import java.util.Scanner;

import org.shark.infra.consumer.QrCodeConsumer;
import org.shark.infra.producer.LinkProducer;

public class App {

	public static void main(String[] args) {
		String queueUrl = "http://localhost:4566/000000000000/qr-queue";

		new File("output").mkdir();

		SqsClient client = SqsClient.builder().region(Region.US_EAST_1).endpointOverride(URI.create("http://localhost:4566"))
			.build();

		LinkProducer producer = new LinkProducer(client, queueUrl);
		QrCodeConsumer consumer = new QrCodeConsumer(client, queueUrl);

		Runnable runnable = () -> {
			System.out.println("== escutando fila ==");
			consumer.listen();

		};
		Thread listenThread = new Thread(runnable);
		listenThread.start();
		while (true) {
			Scanner scanner = new Scanner(System.in);

			System.out.println("digite a url que deseja converter em qrcode:");
			String url = scanner.next();
			producer.sendLink(url);
		}
	}
}