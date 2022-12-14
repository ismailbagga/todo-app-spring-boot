package ismail.coding.todoappspring.mappers;

import ismail.coding.todoappspring.model.ApplicationUser;
import ismail.coding.todoappspring.enums.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
@Slf4j
public class UserMapper implements RowMapper<ApplicationUser> {
    @Override
    public ApplicationUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.debug(String.format("USER ID IS [ %s ]",rs.getString(1)));
        log.debug(String.format("USER PASSWORD IS [ %s ]",rs.getString(8)));
        return  new ApplicationUser(
                rs.getLong(1) ,
                rs.getString(2) ,
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getBoolean(6),
                rs.getString(7).equals("ADMIN")? Role.ADMIN : Role.USER,
                rs.getString(8)
                ) ;
    }
}
