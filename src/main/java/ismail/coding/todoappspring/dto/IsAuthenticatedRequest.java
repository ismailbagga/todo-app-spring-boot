package ismail.coding.todoappspring.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class IsAuthenticatedRequest {
   private String username ;
   private boolean isAuthenticated ;
}
