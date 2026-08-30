package com.jhonatan.ecommerce_api.repository;

import com.jhonatan.ecommerce_api.model.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findByAtivoTrueOrderByOrdemAsc();
}
