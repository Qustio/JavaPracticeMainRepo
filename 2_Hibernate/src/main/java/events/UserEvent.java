package events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEvent {
    private Operation operation;
    private String email;

    public enum Operation {
        Created,
        Deleted
    }
}
