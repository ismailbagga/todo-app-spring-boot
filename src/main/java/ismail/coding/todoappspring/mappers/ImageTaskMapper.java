package ismail.coding.todoappspring.mappers;

import ismail.coding.todoappspring.model.ImageModel;
import ismail.coding.todoappspring.model.TaskModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageTaskMapper implements  RowMapper<TaskModel> {
    private  int shiftBy = 0  ;

    public ImageTaskMapper(int shiftBy) {
        this.shiftBy = shiftBy;
    }



    @Override
    public TaskModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        TaskModel taskModel = new TaskMapper().mapRow(rs,rowNum) ;
        ImageModel imageModel = new ImageMapper(shiftBy).mapRow(rs,rowNum) ;
        if ( taskModel == null) throw new IllegalStateException("to do model is null") ;
        taskModel.setImageModel(imageModel) ;
        return taskModel;
    }
}
