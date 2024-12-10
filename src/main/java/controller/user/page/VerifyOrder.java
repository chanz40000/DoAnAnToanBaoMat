package controller.user.page;

import database.OrderDAO;
import database.OrderDetailDAO;
import model.Order;
import model.OrderDetail;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "verify-order", value = "/verify-order")
public class VerifyOrder extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idOrderString = request.getParameter("OrderIdVerify");
        int idOrder = Integer.parseInt(idOrderString);
        OrderDAO orderDAO = new OrderDAO();
        Order order = orderDAO.selectById(idOrder);
        request.setAttribute("order", order);
        OrderDetailDAO orderDetailDAO =new OrderDetailDAO();
        List<OrderDetail> orderDetails = orderDetailDAO.selectByOrderId(idOrder);
        request.setAttribute("orderDetailList", orderDetails);
        request.getRequestDispatcher("/WEB-INF/book/verify-order.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}