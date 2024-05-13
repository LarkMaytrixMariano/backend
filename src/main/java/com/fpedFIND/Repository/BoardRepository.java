package com.fpedFIND.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fpedFIND.Entity.Board;



public interface BoardRepository extends JpaRepository<Board, Long> {

}
