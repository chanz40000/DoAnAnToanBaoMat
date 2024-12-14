/**
 *
 */
package util;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import model.Order;
import model.OrderDetail;
import model.Product;
import model.User;


/**
 *
 */
public class Email {
	//su dung TLS de dang nhap vao email
	//smtp: simple mail transfer protocol= giao thuc truyen tai don gian hoa
	//ssl: bao mat
	//email: 21130574@st.hcmuaf.edu.vn
	//password: objd edul gnes zspk

	static String from="21130574@st.hcmuaf.edu.vn";
    static String password="objd edul gnes zspk";

	public static void sendEmail(String to, String noiDung, String subject) {

		//properties: khai bao thuoc tinh
		Properties props = new Properties();
		//su dung server de gui mail
		props.put("mail.smtp.host", "smtp.gmail.com");//SMTP HOST
		//
//		props.put("mail.smtp.port", "smtp.gmail.com");//TLS 587 SSL 465
		props.put("mail.smtp.port", "587");//TLS 587 SSL 465
		//khi su dung host de gui mail thi phai dang nhap
		props.put("mail.smtp.auth", "true");
		//
		props.put("mail.smtp.starttls.enable", "true");
		//create authenticator
		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		};

		//phien lam viec: dang nhap vao gmail voi tai khoan auth
		Session session = Session.getInstance(props, auth);
		//gui email
		//final String to="trangthuyjungkook@gmail.com";
		//tao mot tin nhan
		MimeMessage msg = new MimeMessage(session);

		try {
			// Cài đặt kiểu nội dung và mã hóa UTF-8
			msg.setHeader("Content-type", "text/html; charset=UTF-8");

			//nguoi gui
			msg.setFrom(from);

			//tao ra dia chi de nhan email
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

			//tieu de
			msg.setSubject(subject, "UTF-8");

			//quy dinh ngay gui
			msg.setSentDate(new Date());

			//quy dinh email nhan phan hoi
			//msg.setReplyTo(null)

			//noi dung
			msg.setContent(
					"<!DOCTYPE html>\r\n"
							+ "<html>\r\n"
							+ "<head>\r\n"
							+ "<meta charset=\"UTF-8\">\r\n"
							+ "<title>Insert title here</title>\r\n"
							+ "</head>\r\n"
							+ "<body>\r\n"
							+ "<h1>My First Heading</h1>\r\n"
							+ "<p>"+noiDung+"</p>\r\n"
							+"<img src=\"https://i.pinimg.com/564x/44/5f/52/445f522692dfd26142559260b61daf69.jpg\" alt=\"\" width=\"70\" height=\"70\" >"
							+ "</body>\r\n"
							+ "</html>", "text/html; charset=UTF-8");

			//gui email
			Transport.send(msg);
			System.out.println("gui email thanh cong");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			System.out.println("gap loi trong qua trinh gui email");
			e.printStackTrace();
		}
	}
	public String sendNotify(User user, Order order, List<OrderDetail> orderDetails) {
		// Định dạng giá trị tiền
		DecimalFormat df = new DecimalFormat("#,###");
		String formattedPrice = df.format(order.getTotalPrice()).replace(",", ".");

		String htmlCode = "<!DOCTYPE html>\n" +
				"<html>\n" +
				"<head>\n" +
				"    <title>Đơn hàng</title>\n" +
				"    <style>\n" +
				"        body {\n" +
				"            font-family: Arial, sans-serif;\n" +
				"            margin: 20px;\n" +
				"        }\n" +
				"        h1 {\n" +
				"            margin-bottom: 10px;\n" +
				"        }\n" +
				"        table {\n" +
				"            width: 100%;\n" +
				"            border-collapse: collapse;\n" +
				"            margin-bottom: 20px;\n" +
				"        }\n" +
				"        th, td {\n" +
				"            border: 1px solid #ccc;\n" +
				"            padding: 8px;\n" +
				"            text-align: center; /* Canh giữa dữ liệu */\n" +
				"        }\n" +
				"        th {\n" +
				"            background-color: #f2f2f2;\n" +
				"            font-weight: bold;\n" +
				"        }\n" +
				"        tr:nth-child(even) {\n" +
				"            background-color: #f9f9f9;\n" +
				"        }\n" +
				"        tr:hover {\n" +
				"            background-color: #e6e6e6;\n" +
				"        }\n" +
				"    </style>\n" +
				"</head>\n" +
				"<body>\n" +
				"    <h6>Chúng tôi nhận thấy thông tin đơn hàng bị sai thông tin do ai đó đã sửa thông tin do đó đơn hàng đã bị huỷ hoặc sẽ có chính sách hoàn tiền. Thông tin:</h6>\n" +
				"    <h2>Thông tin khách hàng</h2>\n" +
				"    <table>\n" +
				"        <tr>\n" +
				"            <th>Tên khách hàng</th>\n" +
				"            <th>Địa chỉ</th>\n" +
				"            <th>Số điện thoại</th>\n" +
				"            <th>Email</th>\n" +
				"        </tr>\n" +
				"        <tr>\n" +
				"            <td>" + user.getName() + "</td>\n" +
				"            <td>" + order.getAddress() + "</td>\n" +
				"            <td>" + user.getPhone() + "</td>\n" +
				"            <td>" + user.getEmail() + "</td>\n" +
				"        </tr>\n" +
				"    </table>\n" +
				"\n" +
				"    <h2>Thông tin người giao</h2>\n" +
				"    <table>\n" +
				"        <tr>\n" +
				"            <th>Tên người giao</th>\n" +
				"            <th>Số điện thoại</th>\n" +
				"            <th>Tên công ty</th>\n" +
				"            <th>Địa chỉ</th>\n" +
				"        </tr>\n" +
				"        <tr>\n" +
				"            <td>Nguyễn Dư Lập</td>\n" +
				"            <td>0867415853</td>\n" +
				"            <td>Doraemon</td>\n" +
				"            <td>Đại Học Nông Lâm tp.HCM</td>\n" +
				"        </tr>\n" +
				"    </table>\n" +
				"\n" +
				"    <h2>Thông tin đơn hàng</h2>\n" +
				"    <table>\n" +
				"        <tr>\n" +
				"            <th>Mã sản phẩm</th>\n" +
				"            <th>Số lượng</th>\n" +
				"            <th>Tên sản phẩm</th>\n" +
				"            <th>Ghi chú</th>\n" +
				"        </tr>\n";

		for (OrderDetail orderDetail : orderDetails) {
			Product product = orderDetail.getProduct();
			int productId = product.getProductId();
			int quantity = orderDetail.getQuantity();
			String productName = product.getProduct_name();
			String information = ""; // Assuming no note field is present

			htmlCode += "<tr>\n" +
					"<td>" + productId + "</td>\n" +
					"<td>" + quantity + "</td>\n" +
					"<td>" + productName + "</td>\n" +
					"<td>" + information + "</td>\n" +
					"</tr>\n";
		}

		htmlCode += "</table>\n" +
				"<h2>Tổng tiền: " + formattedPrice + "</h2>\n" +
				"</body>\n" +
				"</html>\n";

		return htmlCode;
	}


	public static void sendEmailWithAttachment(String to, String subject, String body, String nameFile, String contentFile) {
		// Configure mail server properties
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		// Create authenticator
		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		};

		// Create a new email session
		Session session = Session.getInstance(props, auth);

		try {
			// Create a new email message
			MimeMessage message = new MimeMessage(session);

			// Set email attributes
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
			message.setSubject(subject, "UTF-8");
			message.setSentDate(new Date());

			// Create a multipart message for attachment
			Multipart multipart = new MimeMultipart();

			// Add text part
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setContent(body, "text/html; charset=UTF-8");
			multipart.addBodyPart(textPart);

			File attachment = new File(nameFile);

			FileWriter writer = new FileWriter(attachment);
				// Write content to file
				writer.write(contentFile);
				writer.flush();

			// Add attachment part
			if (attachment != null && attachment.exists()) {
				MimeBodyPart attachmentPart = new MimeBodyPart();
				attachmentPart.attachFile(attachment);
				multipart.addBodyPart(attachmentPart);
			}

			// Set the complete message parts
			message.setContent(multipart);
			// Send the message
			Transport.send(message);
			System.out.println("Email sent successfully with attachment.");
		} catch (Exception e) {
			System.out.println("Error while sending email with attachment.");
			e.printStackTrace();
		}
	}



	public static void main(String[] args) {
// Example usage
		String recipient = "trangthuyjungkook@gmail.com";
		String subject = "Test Email with Attachment";
		String body = "<h1>This is a test email</h1><p>Please find the attached file below.</p>";
		File attachment = new File("C:\\Users\\ADMIN\\Downloads\\CreateKeyServlet.java"); // Replace with the actual file path

		//sendEmailWithAttachment(recipient, subject, body, attachment);

	}

}
