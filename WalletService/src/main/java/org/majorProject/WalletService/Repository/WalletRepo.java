package org.majorProject.WalletService.Repository;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.majorProject.WalletService.Model.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface WalletRepo extends JpaRepository<UserWallet,Integer> {


    UserWallet findByContact(String sender);

    @Transactional
    @Modifying
    @Query("UPDATE UserWallet w SET w.balance=:balance WHERE w.id=:id")
    void updateWallet(@Param("id") String id, @Param("balance") double balance);
}
