package controller.user;

import database.OrderDAO;
import database.OrderDetailDAO;
import model.Order;
import model.OrderDetail;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@WebServlet(name = "DownLoadTool", value = "/DownLoadTool")
public class DownLoadTool extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Đường dẫn tuyệt đối đến file ToolBook.exe trong thư mục webapp/files
        String filePath = getServletContext().getRealPath("/file/ToolBook.exe");
        File file = new File(filePath);

        if (file.exists()) {
            // Thiết lập header để trình duyệt tải file về
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=ToolBook.exe");

            // Gửi file về cho người dùng
            try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
                 BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream())) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        } else {
            response.getWriter().write("File không tồn tại.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}