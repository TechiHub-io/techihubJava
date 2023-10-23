package com.techihub.job.service.implementation;

import com.techihub.job.model.Document;
import com.techihub.job.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private DocumentRepository documentRepository;
  /*  public Document saveDocument(Document document) {
        return documentRepository.saveById(document);
    }*/
    public void deleteDocument(Long documentId) {
        documentRepository.deleteById(documentId);
    }
    public Optional<Document> getDocumentById(Long documentId) {
        return documentRepository.findById(documentId);
    }
}
