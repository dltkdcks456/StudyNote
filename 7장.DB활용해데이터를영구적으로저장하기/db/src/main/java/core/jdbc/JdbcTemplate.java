package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    // 가변 인자를 사용
    public void execute(String sql, Object... parameters) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (int i = 0; i < parameters.length; i++) {
                Object param = parameters[i];
                pstmt.setObject(i + 1, param);
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> T executeQuery(String sql, RowMapper<T> rm, Object... parameters) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            for (int i = 0; i < parameters.length; i++) {
                Object object = parameters[i];
                pstmt.setObject(i + 1, object);
            }
            rs = pstmt.executeQuery();

            return rm.mapRow(rs);
        } catch (SQLException e) {
            throw  new DataAccessException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        }
    }


    public <T> List<T> executeAllQuery(String sql, RowMapper<T> rm) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            List<T> list = new ArrayList<>();
            while (rs.next()) {
                list.add(rm.mapRow(rs));
            }

            return list;
        } catch (SQLException e) {
            throw  new DataAccessException(e);
        }
    }
}
