/**
 * Copyright (c) 2015-2016, Michael Yang 杨福海 (fuhai999@gmail.com).
 *
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shoory.framework.starter.email.component;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.shoory.framework.starter.api.constants.BizException;
import com.shoory.framework.starter.email.constant.EmailError;
import com.shoory.framework.starter.email.pojo.PojoAttachment;

@Component
public class EmailComponent {
	private static final Logger logger = LoggerFactory.getLogger(EmailComponent.class);

	public void send(String host, String port, String username, String password, boolean useSSL, String[] to,
			String[] cc, String subject, String content, List<PojoAttachment> attachments) {
		try {
			Properties props = new Properties();

			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.smtp.auth", "true");
			props.setProperty("mail.smtp.host", host);
			props.setProperty("mail.smtp.port", port);
			if (useSSL) {
				props.setProperty("mail.smtp.starttls.enable", "true");
				props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			}
			// error:javax.mail.MessagingException: 501 Syntax: HELO hostname
			props.setProperty("mail.smtp.localhost", "127.0.0.1");

			Session session = Session.getInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(MimeUtility.encodeText(username) + "<" + username + ">"));

			message.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
			message.setRecipients(Message.RecipientType.TO,
					Arrays.stream(to).distinct().map(this::emailToAddress).toArray(Address[]::new));
			message.setRecipients(Message.RecipientType.CC,
					Arrays.stream(cc).distinct().map(this::emailToAddress).toArray(Address[]::new));

			//
			Multipart multipart = new MimeMultipart();
			// 内容
			{
				BodyPart contentPart = new MimeBodyPart();
				contentPart.setContent(content, "text/html;charset=utf-8");
				multipart.addBodyPart(contentPart);
			}

			attachments.stream()
				.forEach(attachment -> this.appendAttachment(multipart, attachment));

			message.setContent(multipart);
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException(EmailError.ERROR_EMAIL_FAILED);
		}
	}

	
	
	private InternetAddress emailToAddress(String email) {
		try {
			return new InternetAddress(email);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException(EmailError.ERROR_EMAIL_ADDRESS);
		}
	}

	private void appendAttachment(Multipart multipart, PojoAttachment attachment) {
		BodyPart attachmentBodyPart = new MimeBodyPart();
		DataSource source = new ByteArrayDataSource(attachment.getBytes(), attachment.getContentType());

		try {
			attachmentBodyPart.setDataHandler(new DataHandler(source));
			attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
			multipart.addBodyPart(attachmentBodyPart);
		} catch (MessagingException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException(EmailError.ERROR_EMAIL_ATTACHMENT);
		}
	}

}
