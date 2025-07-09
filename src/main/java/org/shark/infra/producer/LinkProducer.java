package org.shark.infra.producer;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

public class LinkProducer {
	private final SqsClient client;
	private final String queueUrl;

	public LinkProducer(SqsClient client, String queueUrl) {
		this.client = client;
		this.queueUrl = queueUrl;
	}

	public void sendLink(String url) {
		SendMessageRequest request = SendMessageRequest.builder()
			.queueUrl(queueUrl)
			.messageBody(url)
			.build();

		client.sendMessage(request);
		System.out.println("Link enviado para fila /qr-queue " + url);
	}
}