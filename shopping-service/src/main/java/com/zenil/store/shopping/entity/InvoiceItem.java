package com.zenil.store.shopping.entity;

import com.zenil.store.shopping.model.Product;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Positive;

@Entity
@Data
@Table(name = "tbl_invoice_item")
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Positive(message = "El stock debe ser mayor a cero")
    private Double quantity;

    private Double price;

    @Transient
    private Double subTotal;

    @Transient
    private Product product;

    public Double getSubtotal() {
        if (this.price > 0 && this.quantity > 0) {
            return this.quantity * this.price;
        } else {
            return (double) 0;
        }
    }

    public InvoiceItem() {
        this.quantity = (double) 0;
        this.price = (double) 0;
    }

}
