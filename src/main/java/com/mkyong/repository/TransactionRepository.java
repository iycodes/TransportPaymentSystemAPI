package com.mkyong.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mkyong.model.TransactionEntity;

@Primary
@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    Optional<TransactionEntity> findById(String id);

    Slice<TransactionEntity> findBySenderIdOrReceiverId(String senderId, String receiverId,
            org.springframework.data.domain.Pageable pageable);

    String checkTxByTimeQuery = """
            select * from transactions where (sender_id = :sender_id) and (receiver_id = :receiver_id) and (created_at between :datee and Now() );
            """;

    @Query(nativeQuery = true, value = checkTxByTimeQuery)
    Optional<TransactionEntity> checkRecentTx(@Param("sender_id") String sender_id,
            @Param("receiver_id") String receiver_id,
            @Param("datee") LocalDateTime datee);

    //
    String checkPendingTxQuery = """
            SELECT * FROM transactions WHERE (status = 0) AND (created_at between (NOW() - INTERVAL '31000 Minutes') and Now())  ORDER BY created_at ASC LIMIT 300;
            """;

    @Query(nativeQuery = true, value = checkPendingTxQuery)
    List<TransactionEntity> checkPendingTx();

    // Optional<
    // @org.springframework.transaction.annotation.Transactional
    // @Modifying
    // @Query(nativeQuery = true, value = queryyy)
    // void testingPay()
    // throws Exception;

    @Query(nativeQuery = true, value = query1)
    Optional<String> fetchTransaction(@Param(value = "id") String id);

    String queryy = "DELETE FROM transactions WHERE id= '1-2-1714997991'; SELECT * FROM transfer_money4();";
    String query1 = """
            CREATE OR REPLACE FUNCTION select_something1(id_param TEXT) RETURNS void
             AS $func$
              BEGIN  SELECT * FROM transactions WHERE id = id_param ;
               END; $func$  LANGUAGE plpgsql;
            DECLARE idd TEXT := '1-2-17149979914'
            SELECT select_something1(idd) INTO ;

                       """;;
    String query = """
            CREATE OR REPLACE FUNCTION transfer_money7(id_param TEXT,  sender_id_param INT,  receiver_id_param INT) RETURNS void AS
                         $func$
                         DECLARE new_row transactions%ROWTYPE;
                         BEGIN
                            -- Update sender's balance (ensure sufficient funds)
                                IF (SELECT balance FROM users WHERE id = 1) >= 10 THEN
                                    UPDATE users
                                    SET balance = balance - 10
                                    WHERE id = 1
                                    AND balance >= 10;

                            -- Update receiver's balance
                                    UPDATE users
                                    SET balance = balance + 10
                                    WHERE id = 2;

                            -- update transactions table
                                    INSERT INTO transactions (id, sender_id,amount, receiver_id) VALUES (id_param,sender_id_param,10,receiver_id_param)
                                    RETURNING * INTO new_row;
                                    -- RAISE NOTICE 'TRANSFER SUCCESFUL';
                                ELSE
                                RAISE EXCEPTION 'Insufficient funds for user with ID\\: %', 1;
                                END IF;
                             -- RETURN new_row;
                         END;
                         $func$
                         LANGUAGE plpgsql;
               SELECT transfer_money7('1-2-17149979917',1,2) ;
                                """;;

    String queryz = "DO $$ " +
            "BEGIN " +
            "UPDATE users " +
            "SET balance = balance - 20 " +
            "WHERE id = 1 " +
            "AND balance >= 20; " +
            "IF FOUND THEN " +
            "UPDATE users " +
            "SET balance = balance + 20 " +
            "WHERE id = 2; " +
            "INSERT INTO transactions (id, sender_id, amount, receiver_id) " +
            "VALUES (:txid, 1, 20, 2); " +
            "RAISE NOTICE 'Transfer successful!'; " +
            "ELSE " +
            "RAISE EXCEPTION 'Insufficient funds for user with ID: %', 1; " +
            "END IF; " +
            "END $$;";

    String queryyy = """
            DO $$
            BEGIN
                -- Step 1: Deduct the amount from the sender's balance
                UPDATE users
                SET balance = balance - 20
                WHERE id = 1
                AND balance >= 20;

                -- Step 2: Check if the sender had sufficient funds and proceed with the transfer
                IF FOUND THEN
                    -- Step 3: Add the amount to the receiver's balance
                    UPDATE users
                    SET balance = balance + 20
                    WHERE id = 2;

                    -- Step 4: Log the transaction
                    INSERT INTO transactions (id, sender_id,amount, receiver_id)
                    VALUES (1-2-271499799174,1,20,2);

                    -- Step 5: Inform user about successful transfer
                    RAISE NOTICE 'Transfer successful!';
                ELSE
                    -- Step 6: Inform user about insufficient funds
                    RAISE EXCEPTION 'Insufficient funds for user with ID: %', 1;
                END IF;
            END $$;
                           """;

    // PaymentResponseDto makePayment();
}
