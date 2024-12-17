package database;


import model.StatusSignature;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StatusSignatureDAO  implements DAOInterface<StatusSignature>{
    @Override
    public ArrayList<StatusSignature> selectAll() {
        ArrayList<StatusSignature> statuses = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM signature_status";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("signature_status_id");
                String name = rs.getString("signature_status_name");
                StatusSignature status = new StatusSignature(id, name);
                statuses.add(status);
            }

            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return statuses;
    }

    @Override
    public StatusSignature selectById(int id) {
        StatusSignature result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM signature_status WHERE signature_status_id = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                int statusId = rs.getInt("signature_status_id");
                String statusName = rs.getString("signature_status_name");
                result = new StatusSignature(statusId, statusName);
            }

            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int insert(StatusSignature statusSignature) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO signature_status(signature_status_name) VALUES(?)";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, statusSignature.getStatusSignatureName());

            result = st.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int insertAll(ArrayList<StatusSignature> list) {
        int result = 0;
        for (StatusSignature status : list) {
            if (this.insert(status) == 1) {
                result += 1;
            }
        }
        return result;
    }

    @Override
    public int delete(StatusSignature statusSignature) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "DELETE FROM signature_status WHERE signature_status_id = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, statusSignature.getStatusSignatureId());
            result = st.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int deleteAll(ArrayList<StatusSignature> list) {
        int result = 0;
        for (StatusSignature status : list) {
            result += delete(status);
        }
        return result;
    }

    @Override
    public int update(StatusSignature statusSignature) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE signature_status SET signature_status_name = ? WHERE signature_status_id = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, statusSignature.getStatusSignatureName());
            st.setInt(2, statusSignature.getStatusSignatureId());
            result = st.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
