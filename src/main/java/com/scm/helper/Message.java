package com.scm.helper;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class Message {
    private String content;
    private MessageType type;
}
