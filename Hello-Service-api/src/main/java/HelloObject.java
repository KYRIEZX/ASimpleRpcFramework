import lombok.Data;

import java.io.Serializable;

@Data
public class HelloObject implements Serializable {
    private Integer id;
    private String message;
}
