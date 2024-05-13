package com.fpedFIND.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpedFIND.Entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
	Image findByName(String name);
	List<Image> findByNameContaining(String name);
	Optional<Image> findById(Long id);
	 Page<Image> findAllByOrderByImageDateTimeDesc(PageRequest pageable);
}