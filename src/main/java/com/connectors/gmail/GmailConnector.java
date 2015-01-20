package com.connectors.gmail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;

import com.tools.EmailConstants;
import com.tools.data.EmailModel;

public class GmailConnector {

	public static void main(String args[]) {
		readGmail();
	}

	public static List<EmailModel> readGmail() {

		List<EmailModel> emailList = new ArrayList<EmailModel>();

		Properties props2 = System.getProperties();

		props2.setProperty(EmailConstants.EMAIL_STORE, EmailConstants.PROTOCOL);

		Session session2 = Session.getDefaultInstance(props2, null);

		try {

			Store store = session2.getStore(EmailConstants.PROTOCOL);
			store.connect(EmailConstants.RECEIVING_HOST, EmailConstants.USERNAME, EmailConstants.PASSWORD);
			Folder folder = store.getFolder("INBOX");// get inbox

			folder.open(Folder.READ_ONLY);// open folder only to read
			Message message[] = folder.getMessages();

			for (int i = 0; i < message.length; i++) {
				EmailModel modelNow = new EmailModel();
				modelNow.setSubject(message[i].getSubject());

				modelNow.setRecievedDate(message[i].getReceivedDate());
				modelNow.setSentDate(message[i].getSentDate());

				BodyPart bp = ((Multipart) message[i].getContent()).getBodyPart(0);

				modelNow.setContent(bp.getContent().toString());

				System.out.println("------------------------");
				System.out.println(message[i].getSubject());
				System.out.println(message[i].getReceivedDate());
				System.out.println(message[i].getSentDate());
				System.out.println(bp.getContent().toString());
				// message[i].setFlag(Flags.Flag.DELETED, true);

				emailList.add(modelNow);
			}

			folder.close(true);
			store.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return emailList;
	}

}
