package com.poseidon.transaction.dao.repo;
 import com.poseidon.transaction.dao.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{


@Query("SELECT distinct txn FROM Transaction txn WHERE txn.dateReported = CURRENT_DATE")
public List<Transaction> todaysTransaction()
;

public Transaction findBytagno(String tagNo)
;

}