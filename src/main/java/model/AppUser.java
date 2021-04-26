package model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AppUser {
    String id;
    String login;
    String password;
}

