package ismail.coding.todoappspring.mappers;

import ismail.coding.todoappspring.model.TaskModel;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
@Data
public class TaskMapper implements RowMapper<TaskModel> {
    private  final int  moveRowBy = 0 ;

    @Override
    public TaskModel mapRow(ResultSet rs, int rowNum) throws SQLException {


        return  new TaskModel(
                rs.getLong(1),
                rs.getString(2),
                rs.getString(3),
                rs.getDate(4),
                rs.getDate(5),
                rs.getLong(6),
                rs.getBoolean(7)
                ) ;
    }
}
