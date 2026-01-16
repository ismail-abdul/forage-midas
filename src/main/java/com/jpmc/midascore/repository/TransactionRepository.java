package com.jpmc.midascore.repository;

import java.util.Optional;
import com.jpmc.midascore.entity.TransactionId;
import com.jpmc.midascore.entity.TransactionRecord;
import org.springframework.data.repository.CrudRepository;


public interface TransactionRepository extends CrudRepository<TransactionRecord, TransactionId>{
    // Optional<TransactionRecord> findTransactionRecordById(TransactionId transactionId);
    Optional<TransactionRecord> findById(TransactionId transactionId);
}
