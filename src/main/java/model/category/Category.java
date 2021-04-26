package model.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private String id;
    private String slug;
    private String name;
    private String description;
    private String parentId;
    private List<Category> ancestorCategories;
}
