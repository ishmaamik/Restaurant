package app.Websocket.Events;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ItemSummary {
    private Integer quantity;
    private UUID itemId;
    private String name;
    private String notes;
}
