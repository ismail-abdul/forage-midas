package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.entity.TransactionRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TransactionListener {

    private final TransactionRepository transaction_repo;
    private final UserRepository user_repo;

    public TransactionListener(TransactionRepository transaction_repo, UserRepository user_repo) {
        this.transaction_repo = transaction_repo;
        this.user_repo = user_repo;
    }


    static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);

    @KafkaListener(topics = "trader-updates")
    public void listen(Transaction transaction) {
        // Do nothing for now (Task 1 only requires integration)

        // validate the transaction
        try {
            UserRecord recipient = user_repo.findById(transaction.getRecipientId());
            assert(recipient.getId() == transaction.getRecipientId());
            UserRecord sender = user_repo.findById(transaction.getSenderId());
            assert(sender.getId() == transaction.getSenderId());

            // Validate transaction.
            if (transaction.getAmount() < 0 || sender.getBalance() < transaction.getAmount())  return;

            // Determine incentive.
            RestTemplate rest = new RestTemplate();
            Incentive incentive = rest.postForObject("http://localhost:8080/incentive", transaction, Incentive.class);

            TransactionRecord record = new TransactionRecord(transaction.getAmount(), transaction.getSenderId(), transaction.getRecipientId(), incentive.getAmount());
            UserRecord new_recipient = new UserRecord (recipient.getName(), recipient.getBalance() + transaction.getAmount() + incentive.getAmount());
            UserRecord new_sender = new UserRecord (sender.getName(), sender.getBalance() - transaction.getAmount());
            transaction_repo.save(record);
            user_repo.save(new_recipient);
            user_repo.save(new_sender);
            
            // Check waldorf transaction.
            String out = String.format(
                "sender - %s: balance - %f || recipient - %s: balance - %f", 
                new_sender.getName(), new_sender.getBalance(), new_recipient.getName(), new_recipient.getBalance());
            logger.info(out);


        } catch (Exception e) { 
            logger.info(e.toString());
        }
        
    }
}
