package org.shark.infra.consumer;

import java.util.List;
import java.util.UUID;

import org.shark.services.QrCodeService;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

public class QrCodeConsumer {
	private final SqsClient client;
	private final String queueUrl;
	private final QrCodeService qrCodeService;

	public QrCodeConsumer(SqsClient client, String queueUrl) {
		this.client = client;
		this.queueUrl = queueUrl;
		this.qrCodeService = new QrCodeService();
	}

	public void processMessages() {
		ReceiveMessageRequest request = ReceiveMessageRequest.builder()
			.queueUrl(queueUrl)
			.maxNumberOfMessages(5)
			.waitTimeSeconds(5)
			.build();

		List<Message> messages = client.receiveMessage(request).messages();
		for (Message msg : messages) {

			String url = msg.body();
			try {
				String id = UUID.randomUUID().toString();

				qrCodeService.generateByUrl(url, id);

				client.deleteMessage(DeleteMessageRequest.builder()
					.queueUrl(queueUrl)
					.receiptHandle(msg.receiptHandle())
					.build());

			} catch (Exception e) {
				System.err.println("Erro ao gerar QR Code: " + e.getMessage());
			}
		}
	}
}