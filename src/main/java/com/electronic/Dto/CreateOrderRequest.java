package com.electronic.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateOrderRequest {


    @NotBlank(message = "cart Id is required")
    private String cartId;
    @NotBlank(message = "user Id is required")
    private  String userId;
    private  String orderStatus ="PENDING";
    private  String paymentStatus ="NOTPAID";

    @NotBlank(message = "Address is required")
    private String billingAddress;
    @NotBlank(message = "Phone Number is required")
    private String billingPhone;
    @NotBlank(message = "Billing name  is required")
    private String billingName;


}
