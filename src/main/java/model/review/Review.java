package model.review;

import lombok.Data;

import java.util.Date;

@Data
public class Review {
    private String id;
    private String productId;
    private String userId;
    private String username;
    private Date date;
    private String title;
    private String comment;
}
