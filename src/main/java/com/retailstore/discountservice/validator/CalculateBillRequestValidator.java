package com.retailstore.discountservice.validator;

import com.retailstore.discountservice.exception.CurrencyNotFoundException;
import com.retailstore.discountservice.exception.NullValueFoundException;
import com.retailstore.discountservice.exception.UnsupportedCurrencyException;
import com.retailstore.discountservice.request.BillRequest;

import java.util.Objects;

public class CalculateBillRequestValidator {

	private CalculateBillRequestValidator() {
		throw new AssertionError();
	}

	public static void validateRequest(BillRequest bill) {
        if(Objects.isNull(bill)) {
            throw new NullValueFoundException("The bill value is invalid");
        }

        if(Objects.isNull(bill.getUser()) || Objects.isNull(bill.getUser().getUserTypes())) {
            throw new NullValueFoundException("Either the user is null or it does not have user types");
        }

        if(Objects.isNull(bill.getItems())) {
            throw new NullValueFoundException("No Items found inside Bill");
        }

        if(Objects.isNull(bill.getOriginalCurrency())) {
            throw new UnsupportedCurrencyException("The original currency value is invalid");
        }

        if(Objects.isNull(bill.getTargetCurrency())) {
            throw new UnsupportedCurrencyException("The target currency value is invalid");
        }
	}
}