package next.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import next.model.User;
import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDao {
    static final Logger log = LoggerFactory.getLogger(UserDao.class);

    public void insert(User user) {
        JdbcTemplate template = new JdbcTemplate();
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        template.execute(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        // TODO 구현 필요함.
        String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId = ?";
        JdbcTemplate template = new JdbcTemplate();
        template.execute(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        RowMapper<User> rm = rs -> new User(rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"));

        String sql = "SELECT * FROM USERS";
        JdbcTemplate template = new JdbcTemplate();
        return template.executeAllQuery(sql, rm);
    }

    public User findByUserId(String userId) {
        RowMapper<User> rm = rs -> {
            User user = null;
            if (rs.next()) {
                user = new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"));
            }
            return user;
        };
//        RowMapper<User> rm = rs -> new User(
//                rs.getString("userId"),
//                rs.getString("password"),
//                rs.getString("name"),
//                rs.getString("email")
//        );

        JdbcTemplate template = new JdbcTemplate();

        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return template.executeQuery(sql, rm, userId);
    }
}
