package org.shark.services;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QrCodeService {

	public void generateByUrl(String url, String filename) throws WriterException, IOException {
		QRCodeWriter writer = new QRCodeWriter();
		BitMatrix bitMatrix = writer.encode(url, BarcodeFormat.QR_CODE, 300, 300);

		Path path = Paths.get("output", filename + ".png");
		MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

		System.out.println("QR code gerado: " + path.toString());
	}

}