package org.majorProject.TrxService.Repository;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.majorProject.TrxService.Model.Trx;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TrxRepo extends JpaRepository<Trx,Integer> {


    @Modifying
    @Transactional
    @Query("UPDATE Trx t SET t.message = :message , t.txnStatus = :trxStatus WHERE t.txnId = :trxid")
    void updateTrx(@Param("trxid") String trxid,@Param("message") String message,@Param("trxStatus") String trxStatus);

    Page<Trx> findBySenderId(String username, Pageable pagable);

}
