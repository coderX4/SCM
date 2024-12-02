package com.scm.forms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailForm {
    private String from;
    private String to;
    private String subject;
    private String body;
}
