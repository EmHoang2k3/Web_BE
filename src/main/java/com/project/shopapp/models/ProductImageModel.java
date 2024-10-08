package com.project.shopapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    public static final int MAXIMUM_IMAGES_PER_PRODUCT = 5;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private ProductModel product;

    @Column(name = "image_url",length = 300)
    private String imageUrl;
}
