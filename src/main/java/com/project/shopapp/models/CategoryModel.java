package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "categories")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false)
    private String name;
}
