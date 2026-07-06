package bj.mef.sigfl_backend.invoice.domain.model.entities;

import java.time.LocalDate;
import java.util.Currency;

import bj.mef.sigfl_backend.invoice.domain.model.InvoiceStatus;
import bj.mef.sigfl_backend.invoice.domain.model.valueObjects.*;
import bj.mef.sigfl_backend.invoice.domain.model.valueObjects.Number;

public class Invoice {

    private final InvoiceId id;
    private final Number number;
    private final Customer customer;
    private final Amount amount;
    private final Currency currency;
    private InvoiceStatus status;
    private final LocalDate dueDate;

    public Invoice(
            InvoiceId id,
            Number number,
            Customer customer,
            Amount amount,
            Currency currency,
            LocalDate dueDate
    ) {

        if (number == null) {
            throw new IllegalArgumentException("Invoice number is required");
        }

        if (customer == null) {
            throw new IllegalArgumentException("Customer is required");
        }

        if (amount == null) {
            throw new IllegalArgumentException("Amount is required");
        }

        if (currency == null) {
            throw new IllegalArgumentException("Currency is required");
        }

        this.id = id;
        this.number = number;
        this.customer = customer;
        this.amount = amount;
        this.currency = currency;
        this.dueDate = dueDate;
        this.status = InvoiceStatus.DRAFT;
    }

    public void validate() {
        if (status != InvoiceStatus.DRAFT) {
            throw new IllegalStateException("Only draft invoice can be validated");
        }
        status = InvoiceStatus.VALIDATED;
    }

    public void send() {
        if (status != InvoiceStatus.VALIDATED) {
            throw new IllegalStateException("Invoice must be validated first");
        }
        status = InvoiceStatus.SENT;
    }

    public void pay() {
        if (status != InvoiceStatus.SENT) {
            throw new IllegalStateException("Invoice must be sent first");
        }
        status = InvoiceStatus.PAID;
    }

    public boolean isOverdue() {
        return dueDate != null && dueDate.isBefore(LocalDate.now());
    }

    // getters

    public InvoiceId getId(){
        return id;
    }

    public Number getNumber() {
        return number;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Amount getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
}