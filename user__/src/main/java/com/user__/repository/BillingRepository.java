package com.user__.repository;


import com.user__.entity.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingRepository extends JpaRepository<Billing,Long> {

    @Query(value = "SELECT * FROM billings where invoice_no= ?1",nativeQuery = true)
    Billing findByInvoiceNo(String invoiceNo);

    @Query(value = "SELECT account_no from m_savings_account where id =?1;", nativeQuery = true)
    String getAccountNo(String accountId);
}
