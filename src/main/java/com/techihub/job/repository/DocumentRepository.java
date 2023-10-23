package com.techihub.job.repository;

import com.techihub.job.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findById(Long documentId);
  /*  Document saveById(Document document);*/
    void deleteById(Long documentId);



}



