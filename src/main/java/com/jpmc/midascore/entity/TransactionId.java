package com.jpmc.midascore.entity;

import java.io.Serializable;

public class TransactionId implements Serializable {

    private long senderId;

    private long recipientId;

    public TransactionId() {
        // Required by JPA
    }

    public TransactionId(long senderId, long recipientId) {
        this.senderId = senderId;
        this.recipientId = recipientId;
    }
    
}
