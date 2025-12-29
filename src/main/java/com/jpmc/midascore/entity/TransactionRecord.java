package com.jpmc.midascore.entity;

import jakarta.persistence.*;


import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name="transaction_records")
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private float incentive;

    @ManyToOne(optional = false)
    @JoinColumn(name="sender_id")
    private UserRecord sender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "recipient_id")
    private UserRecord recipient;

    @Column(nullable = false)
    private float amount;

    @Column(nullable = false)
    private Instant timestamp;

    public TransactionRecord(){}

    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount, float incentive){
        this.sender=sender;
        this.recipient=recipient;
        this.incentive=incentive;
        this.amount=amount;
        this.timestamp=Instant.now();
    }

    public float getIncentive(){
        return incentive;
    }

    public float getAmount(){
        return amount;
    }
    public UserRecord getSender(){
        return sender;
    }
    public UserRecord getRecipient(){
        return recipient;
    }
    public Instant GetTimestamp(){
        return timestamp;
    }
}
