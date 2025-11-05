package ru.yandex.practicum.commerce.dto;

public enum OrderState {
    NEW,                // новый;
    ON_PAYMENT,         // ожидает оплаты;
    PAID,               // оплачен;
    PAYMENT_FAILED,     // неудачная оплата;
    ASSEMBLED,          // собран;
    ASSEMBLY_FAILED,    // неудачная сборка;
    ON_DELIVERY,        // ожидает доставки;
    DELIVERED,          // доставлен;
    DELIVERY_FAILED,    // неудачная доставка;
    // PRODUCT_RETURNED,   // возврат товаров;
    COMPLETED,          // завершён;
    DONE,               // выполнен;
    CANCELED            // отменён.
}