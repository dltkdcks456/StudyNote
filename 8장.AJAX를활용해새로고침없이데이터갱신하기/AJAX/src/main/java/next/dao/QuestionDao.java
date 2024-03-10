package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import next.model.Question;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class QuestionDao {

    public List<Question> findAll() {
        JdbcTemplate template = new JdbcTemplate();
        String sql = "SELECT * FROM QUESTIONS order by questionId desc";
        RowMapper<Question> rm = new RowMapper<Question>() {
            @Override
            public Question mapRow(ResultSet rs) throws SQLException {
                return new Question(rs.getLong("questionId"),
                        rs.getString("writer"),
                        rs.getString("title"),
                        null,
                        rs.getTimestamp("createdDate"),
                        rs.getInt("countOfAnswer"));
            }
        };
        return template.query(sql, rm);
    }

    public Question findById(long questionId) {
        JdbcTemplate template = new JdbcTemplate();
        String sql = "SELECT questionId, writer, title, contents, createdDate, countOfAnswer FROM QUESTIONS" +
                " WHERE questionId = ?";
        RowMapper<Question> rm = new RowMapper<Question>() {
            @Override
            public Question mapRow(ResultSet rs) throws SQLException {
                return new Question(rs.getLong("questionId"),
                        rs.getString("writer"),
                        rs.getString("title"),
                        rs.getString("contents"),
                        rs.getTimestamp("createdDate"),
                        rs.getInt("countOfAnswer"));
            }
        };
        return template.queryForObject(sql, rm, questionId);
    }
}
