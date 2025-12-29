package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final IncentiveRequester incentiveRequester;

    public DatabaseConduit(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository, IncentiveRequester incentiveRequester) {

        this.userRepository = userRepository;
        this.transactionRecordRepository=transactionRecordRepository;
        this.incentiveRequester=incentiveRequester;

    }

    public void save(UserRecord userRecord) {

        userRepository.save(userRecord);
    }

    @Transactional
    public void save(Transaction transaction){
        UserRecord sender=userRepository.findById(transaction.getSenderId());
        UserRecord recipient=userRepository.findById(transaction.getRecipientId());


        if(sender!=null && recipient !=null){
            if(sender.getBalance()>=transaction.getAmount()){
                //Call the Incentive API
                Incentive incentive=incentiveRequester.getIncentive(transaction);
                float incentiveAmount=incentive.getAmount();
                //Deduct from sender
                sender.setBalance(sender.getBalance()-transaction.getAmount());
                userRepository.save(sender);

                //Add to recipient
                recipient.setBalance(recipient.getBalance()+transaction.getAmount());
                userRepository.save(recipient);

                //Record transaction
                TransactionRecord record=new TransactionRecord(sender, recipient, transaction.getAmount(),incentiveAmount);
                transactionRecordRepository.save(record);
            }
        }
    }
}
