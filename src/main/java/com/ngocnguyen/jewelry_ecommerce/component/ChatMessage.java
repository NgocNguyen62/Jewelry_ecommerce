package com.ngocnguyen.jewelry_ecommerce.component;

import com.ngocnguyen.jewelry_ecommerce.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private MessageType type;
    private String content;
    private User sender;
    private User receiver;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}
