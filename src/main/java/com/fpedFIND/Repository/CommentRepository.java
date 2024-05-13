package com.fpedFIND.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.fpedFIND.Entity.CommentTagging;

@Repository
public interface CommentRepository extends JpaRepository<CommentTagging, Long> {
    // You can add custom query methods here if needed
    Optional<CommentTagging> findById(Long id);

    List<CommentTagging> findByFileId(@Param("file_id") Integer fileId);
    
    // Define a custom query to find the latest comment for each file
    @Query("SELECT c FROM CommentTagging c " +
            "WHERE c.timestamp = (SELECT MAX(c2.timestamp) FROM CommentTagging c2 WHERE c2.fileId = c.fileId)")
    List<CommentTagging> findLatestCommentsForEachFile();

}