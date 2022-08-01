package ismail.coding.todoappspring.mappers;

import ismail.coding.todoappspring.model.ImageModel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
@AllArgsConstructor
@NoArgsConstructor
public class ImageMapper implements RowMapper<ImageModel> {
    private int shiftBy = 0 ;
    @Override
    public ImageModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        return  new ImageModel(
                rs.getLong(shiftBy) ,
                rs.getString(shiftBy+1),
                rs.getString(shiftBy+2),
                rs.getBytes(shiftBy+3) ,
                rs.getLong(shiftBy+4)
        ) ;
    }
}
