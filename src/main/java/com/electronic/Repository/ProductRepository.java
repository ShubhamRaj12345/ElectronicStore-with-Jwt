package com.electronic.Repository;

import com.electronic.Entity.Category;
import com.electronic.Entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Products , String> {


    Page<Products> findByTitleContaining(String subTitle,  Pageable pageable);
    Page<Products> findByLiveTrue(Pageable pageable);
    Page<Products> findByCategory(Category category , Pageable pageable);


}
