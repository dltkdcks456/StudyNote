package next.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import core.jdbc.ConnectionManager;
import next.model.User;
import next.utils.JdbcTemplate;
import next.utils.PreparedStatementSetter;
import next.utils.RowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDao {
    static final Logger log = LoggerFactory.getLogger(UserDao.class);

    public void insert(User user) throws SQLException {
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setParameters(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());
            }
        };

        JdbcTemplate template = new JdbcTemplate();

        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        template.execute(sql, pss);
    }

    public void update(User user) throws SQLException {
        // TODO 구현 필요함.
        String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId = ?";
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setParameters(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getName());
                pstmt.setString(3, user.getEmail());
                pstmt.setString(4, user.getUserId());
            }
        };

        JdbcTemplate template = new JdbcTemplate();
        template.execute(sql, pss);
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM USERS";
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement psmt = con.prepareStatement(sql);
                ResultSet rs = psmt.executeQuery()) {
            User user = null;
            List<User> users = new ArrayList<>();
            while(rs.next()) {
                log.info("rs: {}", rs);
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
                log.info("user: {}", user);
                users.add(user);
            }
            return users;
        }
    }

    public User findByUserId(String userId) throws SQLException {
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setParameters(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, userId);
            }
        };
        RowMapper rm = new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs) throws SQLException {
                User user = null;
                if (rs.next()) {
                    user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                            rs.getString("email"));
                }

                return user;
            }
        };
        JdbcTemplate template = new JdbcTemplate();

        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return (User) template.executeQuery(sql, pss, rm);
    }

}
