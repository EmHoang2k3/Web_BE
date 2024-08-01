package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "product_images")
@Entity
@Getter
@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductModel product;

    @Column(name = "image_url",length = 300)
    private String imageUrl;
}
