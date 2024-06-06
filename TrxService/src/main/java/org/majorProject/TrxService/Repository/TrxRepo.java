package org.majorProject.TrxService.Repository;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.majorProject.TrxService.Model.Trx;
import org.majorProject.TrxService.Model.TxnStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TrxRepo extends JpaRepository<Trx,Integer> {


    @Modifying
    @Transactional
    @Query("UPDATE Trx t SET t.message = :message , t.txnStatus = :txnStatus WHERE t.txnId = :txnId")
    void updateTrx(@Param("txnId") String txnId,@Param("message") String message,@Param("txnStatus") TxnStatus txnStatus);

    Page<Trx> findBySenderId(String username, Pageable pagable);

}
