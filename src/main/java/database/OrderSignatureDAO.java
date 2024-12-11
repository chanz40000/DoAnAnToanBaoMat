package database;

import model.Order;
import model.OrderSignature;
import model.StatusOrder;

import java.sql.*;
import java.util.ArrayList;

public class OrderSignatureDAO implements DAOInterface<OrderSignature> {

    @Override
    public ArrayList<OrderSignature> selectAll() {
        ArrayList<OrderSignature> orderSignatures = new ArrayList<>();
        try {
            // Tạo kết nối đến cơ sở dữ liệu
            Connection con = JDBCUtil.getConnection();

            // Tạo câu lệnh SQL
            String sql = "SELECT * FROM order_signatures";

            PreparedStatement st = con.prepareStatement(sql);

            // Thực thi câu lệnh SQL
            ResultSet rs = st.executeQuery();

            // Duyệt qua kết quả và tạo đối tượng OrderSignature
            while (rs.next()) {
                int id = rs.getInt("id");
                int orderId = rs.getInt("order_id");
                String hash = rs.getString("hash");
                String signature = rs.getString("signature");
                int statusId = rs.getInt("signature_status_id");
                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp updatedAt = rs.getTimestamp("updated_at");

                // Lấy thông tin Order và StatusOrder từ các bảng tương ứng
                Order order = new OrderDAO().selectById(orderId);
                StatusOrder statusOrder = new StatusOrderDAO().selectById(statusId);

                OrderSignature orderSignature = new OrderSignature(id, order, hash, signature, statusOrder, createdAt, updatedAt);
                orderSignatures.add(orderSignature);
            }

            // Đóng kết nối
            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orderSignatures;
    }

    @Override
    public OrderSignature selectById(int id) {
        OrderSignature orderSignature = null;

        try {
            // Tạo kết nối đến cơ sở dữ liệu
            Connection con = JDBCUtil.getConnection();

            // Tạo câu lệnh SQL
            String sql = "SELECT * FROM order_signatures WHERE id = ?";

            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);

            // Thực thi câu lệnh SQL
            ResultSet rs = st.executeQuery();

            // Nếu tìm thấy bản ghi, tạo đối tượng OrderSignature
            if (rs.next()) {
                int orderId = rs.getInt("order_id");
                String hash = rs.getString("hash");
                String signature = rs.getString("signature");
                int statusId = rs.getInt("signature_status_id");
                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp updatedAt = rs.getTimestamp("updated_at");

                // Lấy thông tin Order và StatusOrder từ các bảng tương ứng
                Order order = new OrderDAO().selectById(orderId);
                StatusOrder statusOrder = new StatusOrderDAO().selectById(statusId);

                orderSignature = new OrderSignature(id, order, hash, signature, statusOrder, createdAt, updatedAt);
            }
            // Đóng kết nối
            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderSignature;
    }

    @Override
    public int insert(OrderSignature orderSignature) throws SQLException {
        int result = 0;
        try {
            // Tạo kết nối đến cơ sở dữ liệu
            Connection con = JDBCUtil.getConnection();

            // Tạo câu lệnh SQL
            String sql = "INSERT INTO order_signatures (order_id, hash, signature, signature_status_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, orderSignature.getOrderId().getOrderId());
            ps.setString(2, orderSignature.getHash());
            ps.setString(3, orderSignature.getSignature());
            ps.setInt(4, orderSignature.getStatusId().getStatusId());
            ps.setTimestamp(5, orderSignature.getCreatedAt());
            ps.setTimestamp(6, orderSignature.getUpdatedAt());

            result = ps.executeUpdate();

            // Đóng kết nối
            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int insertAll(ArrayList<OrderSignature> list) throws SQLException {
        int result = 0;
        for (OrderSignature orderSignature : list) {
            result += this.insert(orderSignature);
        }
        return result;
    }

    @Override
    public int delete(OrderSignature orderSignature) {
        int result = 0;
        try {
            // Tạo kết nối đến cơ sở dữ liệu
            Connection con = JDBCUtil.getConnection();

            // Tạo câu lệnh SQL
            String sql = "DELETE FROM order_signatures WHERE id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, orderSignature.getId());

            result = ps.executeUpdate();

            // Đóng kết nối
            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int deleteAll(ArrayList<OrderSignature> list) {
        int result = 0;
        for (OrderSignature orderSignature : list) {
            result += this.delete(orderSignature);
        }
        return result;
    }

    @Override
    public int update(OrderSignature orderSignature) {
        int result = 0;
        try {
            // Tạo kết nối đến cơ sở dữ liệu
            Connection con = JDBCUtil.getConnection();

            // Tạo câu lệnh SQL
            String sql = "UPDATE order_signatures SET order_id = ?, hash = ?, signature = ?, signature_status_id = ?, created_at = ?, updated_at = ? WHERE id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, orderSignature.getOrderId().getOrderId());
            ps.setString(2, orderSignature.getHash());
            ps.setString(3, orderSignature.getSignature());
            ps.setInt(4, orderSignature.getStatusId().getStatusId());
            ps.setTimestamp(5, orderSignature.getCreatedAt());
            ps.setTimestamp(6, orderSignature.getUpdatedAt());
            ps.setInt(7, orderSignature.getId());

            result = ps.executeUpdate();

            // Đóng kết nối
            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
