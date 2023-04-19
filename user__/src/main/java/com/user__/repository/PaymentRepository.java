package com.user__.repository;






import com.user__.entity.Payment;
import com.user__.response.TransactionResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    @Query(value = "select p.amount_paid as amount,(select s.service_name  from services s join payments p on p.services_id= s.id where p.billings_id=?1 and \n" +
            "p.paid_by= b.app_users_id ) \n" +
            "as service,(select concat(a.first_name,\" \",a.last_name) \n" +
            "from app_users a join payments p on p.paid_by= a.id where p.billings_id=?1  and \n" +
            "p.paid_by= b.app_users_id ) \n" +
            "as sender,(select concat(a.first_name,\" \",a.last_name) \n" +
            "from app_users a join payments p on a.id=p.paid_to where p.billings_id=?1  and \n" +
            "p.paid_by= b.app_users_id ) as receiver,\n" +
            "(select email  from app_users a join payments p on a.id=p.paid_to where p.billings_id=?1  and \n" +
            "p.paid_by= b.app_users_id ) as emailp,\n" +
            "(select email  from app_users a join payments p on a.id=p.paid_by where p.billings_id=?1  and \n" +
            "p.paid_by= b.app_users_id ) as emailc,\n" +
            "b.invoice_no as invoice,  \n" +
            "p.date as time from payments p join billings b on p.billings_id= b.id where p.billings_id=?1  and \n" +
            "p.paid_by= b.app_users_id;",nativeQuery = true)
    TransactionResponseDto getTransactionResponse(Long id);






}