package com.asusoftware.only_feet_api.payment.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public String createPriceForCreator(Long amountCents, String currency) {
        Stripe.apiKey = stripeApiKey;

        try {
            // Creează un produs generic
            Product product = Product.create(ProductCreateParams.builder()
                    .setName("Abonament Creator OnlyFeet")
                    .build());

            // Creează un preț asociat
            Price price = Price.create(PriceCreateParams.builder()
                    .setUnitAmount(amountCents)
                    .setCurrency(currency)
                    .setRecurring(
                            PriceCreateParams.Recurring.builder()
                                    .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
                                    .build()
                    )
                    .setProduct(product.getId())
                    .build());

            return price.getId();
        } catch (StripeException e) {
            throw new RuntimeException("Eroare la crearea prețului în Stripe: " + e.getMessage());
        }
    }
}
