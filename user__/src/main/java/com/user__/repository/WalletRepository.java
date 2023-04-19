package com.user__.repository;




import com.user__.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
  @Query(value = "SELECT * FROM WALLETS WHERE APP_USERS_ID=?1",nativeQuery = true)
  Wallet findWalletByUserId(Long userId);
  Wallet findByWalletName(String name);
}
