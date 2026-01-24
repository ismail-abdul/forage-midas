package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
@IdClass(TransactionId.class)
public class TransactionRecord {
    
    @Id
    private long senderId;
    
    @Id
    private long recipientId;
    
    @Column(nullable = false)
    private float amount;

    @Column(nullable = false)
    private float incentive;

    protected TransactionRecord() {

    }
    
    public TransactionRecord(float amount, long senderId, long recipientId, float incentive) {
        this.amount = amount;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.incentive = incentive;
    }
    
    public TransactionRecord(float amount, long senderId, long recipientId) {
        this.amount = amount;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.incentive = 0;
    }

    public long getRecipientId() {
        return this.recipientId;
    }

    public long getSenderId() {
        return this.senderId;
    }

    public float getAmount() {
        return this.amount;
    }
}
