package skatn.remindmeback.submithistory.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import skatn.remindmeback.submithistory.repository.dto.QuestionSubmitHistoryCountDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class QuestionSubmitHistoryQueryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public QuestionSubmitHistoryQueryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<QuestionSubmitHistoryCountDto> getLast30Days(long memberId) {
        String sql = """
                SELECT
                    dates.date as date,
                    count(qsh.id) as count
                FROM (
                    SELECT SUBDATE(CURDATE(), INTERVAL t2.i*10 + t1.i DAY) AS date
                    FROM
                      (SELECT 0 i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) t1,
                      (SELECT 0 i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3) t2
                    WHERE t2.i*10 + t1.i < 30
                ) AS dates
                LEFT JOIN
                    question_submit_history AS qsh
                    ON DATE_FORMAT(qsh.created_at, '%Y-%m-%d') = dates.date
                    AND qsh.created_by = :memberId
                GROUP BY date
                ORDER BY date;
                """;

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("memberId", memberId);

        return jdbcTemplate.query(sql, param, historyCountRowMapper());
    }

    public Map<String, List<QuestionSubmitHistoryCountDto>> getDailyWithinYear(long memberId, int year) {
        String sql = """
                SELECT
                    dates.date as date,
                    count(qsh.id) as count
                FROM (
                    SELECT ADDDATE(:start, INTERVAL t3.i*100 + t2.i*10 + t1.i DAY) AS date
                    FROM
                      (SELECT 0 i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) t1,
                      (SELECT 0 i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) t2,
                      (SELECT 0 i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3) t3
                    WHERE ADDDATE(:start, INTERVAL t3.i*100 + t2.i*10 + t1.i DAY) <= :end
                ) AS dates
                LEFT JOIN
                    question_submit_history AS qsh
                    ON DATE_FORMAT(qsh.created_at, '%Y-%m-%d') = dates.date
                    AND qsh.created_by = :memberId
                GROUP BY date
                ORDER BY date
                """;

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("start", LocalDate.of(year, 1, 1));
        param.addValue("end", LocalDate.of(year, 12, 31));
        param.addValue("memberId", memberId);

        List<QuestionSubmitHistoryCountDto> histories = jdbcTemplate.query(sql, param, historyCountRowMapper());
        return histories.stream()
                .collect(Collectors.groupingBy(
                        history -> history.date().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        TreeMap::new,
                        Collectors.toList()
                ));
    }

    private RowMapper<QuestionSubmitHistoryCountDto> historyCountRowMapper() {
        return (rs, rowNum) -> new QuestionSubmitHistoryCountDto(
                LocalDate.parse(rs.getString("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                rs.getInt("count")
        );
    }
}
