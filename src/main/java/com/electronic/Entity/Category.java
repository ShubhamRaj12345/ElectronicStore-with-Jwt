package com.electronic.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    private  String categoryId;
    @Column(name ="category_title",length = 100, nullable = false)
    private String title;
    @Column(name = "category_desc" ,length = 2000)
    private  String description;
    private  String coverImage;

    // i am going to declair a varible that can store a list of many products
    // because one Category can have multiple products
    // this varible represent a mapping
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Products> productsList =  new ArrayList<>();


}
