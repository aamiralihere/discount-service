package com.retailstore.discountservice;

import com.retailstore.discountservice.client.CurrencyExchangeClient;
import com.retailstore.discountservice.constant.ItemType;
import com.retailstore.discountservice.constant.UserType;
import com.retailstore.discountservice.model.Item;
import com.retailstore.discountservice.model.User;
import com.retailstore.discountservice.request.BillRequest;
import com.retailstore.discountservice.service.CurrencyService;
import com.retailstore.discountservice.service.DiscountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
class DiscountServiceTests {

    @InjectMocks
    DiscountService discountService;

    @Mock
    CurrencyService currencyService;

    @Mock
    CurrencyExchangeClient currencyExchangeClient;

    @Test
    public void testCalculateForEmployee() {
        List<UserType> userTypes = List.of(UserType.CUSTOMER, UserType.EMPLOYEE);
        User user = new User(userTypes, LocalDateTime.now());

        List<Item> items = List.of(
                new Item("Toaster", "Toaster", 1500.0, ItemType.ELECTRONIC),
                new Item("Corn Flour", "Corn Flour", 500.0, ItemType.GROCERY),
                new Item("Make-up Kit", "Make-up Kit", 1000.0, ItemType.COSMETIC));

        String originalCurrency = "PKR";
        String targetCurrency = "USD";

        when(currencyService.getExchangeRate(originalCurrency, targetCurrency)).thenReturn(0.003595);

        Double netPayableAmount = discountService.calculateDiscount(new BillRequest(user, items, originalCurrency, targetCurrency));

        assertEquals(7.5495, netPayableAmount, 0.01);
        verify(currencyService).getExchangeRate(originalCurrency, targetCurrency);
    }

    @Test
    public void testCalculateForAffiliate() {
        List<UserType> userTypes = List.of(UserType.CUSTOMER, UserType.AFFILIATE);
        User user = new User(userTypes, LocalDateTime.now());

        List<Item> items = List.of(
                new Item("Toaster", "Toaster", 1500.0, ItemType.ELECTRONIC),
                new Item("Corn Flour", "Corn Flour", 500.0, ItemType.GROCERY),
                new Item("Make-up Kit", "Make-up Kit", 1000.0, ItemType.COSMETIC));

        String originalCurrency = "PKR";
        String targetCurrency = "USD";

        when(currencyService.getExchangeRate(originalCurrency, targetCurrency)).thenReturn(0.003595);

        Double netPayableAmount = discountService.calculateDiscount(new BillRequest(user, items, originalCurrency, targetCurrency));

        assertEquals(9.347, netPayableAmount, 0.01);
        verify(currencyService).getExchangeRate(originalCurrency, targetCurrency);
    }

    @Test
    public void testCalculateForOverTwoYearsOldCustomer() {
        List<UserType> userTypes = List.of(UserType.CUSTOMER);
        User user = new User(userTypes, LocalDateTime.of(2020, 1, 1, 0, 0));

        List<Item> items = List.of(
                new Item("Toaster", "Toaster", 1500.0, ItemType.ELECTRONIC),
                new Item("Corn Flour", "Corn Flour", 500.0, ItemType.GROCERY),
                new Item("Make-up Kit", "Make-up Kit", 1000.0, ItemType.COSMETIC));

        String originalCurrency = "PKR";
        String targetCurrency = "USD";

        when(currencyService.getExchangeRate(originalCurrency, targetCurrency)).thenReturn(0.003595);

        Double netPayableAmount = discountService.calculateDiscount(new BillRequest(user, items, originalCurrency, targetCurrency));

        assertEquals(9.796375, netPayableAmount, 0.01);
        verify(currencyService).getExchangeRate(originalCurrency, targetCurrency);
    }

    @Test
    public void testCalculateForLessThanTwoYearsOldCustomer() {
        List<UserType> userTypes = List.of(UserType.CUSTOMER);
        User user = new User(userTypes, LocalDateTime.of(2024, 1, 1, 0, 0));

        List<Item> items = List.of(
                new Item("Toaster", "Toaster", 1500.0, ItemType.ELECTRONIC),
                new Item("Corn Flour", "Corn Flour", 500.0, ItemType.GROCERY),
                new Item("Make-up Kit", "Make-up Kit", 1000.0, ItemType.COSMETIC));

        String originalCurrency = "PKR";
        String targetCurrency = "USD";

        when(currencyService.getExchangeRate(originalCurrency, targetCurrency)).thenReturn(0.003595);

        Double netPayableAmount = discountService.calculateDiscount(new BillRequest(user, items, originalCurrency, targetCurrency));

        assertEquals(10.24575, netPayableAmount, 0.01);
        verify(currencyService).getExchangeRate(originalCurrency, targetCurrency);
    }

    @Test
    public void testCalculateBillAmountBasedDiscount() {
        List<UserType> userTypes = List.of(UserType.CUSTOMER);
        User user = new User(userTypes, LocalDateTime.now());

        List<Item> items = List.of(
                new Item("Toaster", "Toaster", 1500.0, ItemType.ELECTRONIC),
                new Item("Corn Flour", "Corn Flour", 500.0, ItemType.GROCERY),
                new Item("Make-up Kit", "Make-up Kit", 1000.0, ItemType.COSMETIC));

        String originalCurrency = "PKR";
        String targetCurrency = "USD";

        when(currencyService.getExchangeRate(originalCurrency, targetCurrency)).thenReturn(0.003595);

        Double netPayableAmount = discountService.calculateDiscount(new BillRequest(user, items, originalCurrency, targetCurrency));

        assertEquals(10.24575, netPayableAmount, 0.01);
        verify(currencyService).getExchangeRate(originalCurrency, targetCurrency);
    }

    @Test
    public void testCalculatePercentageBasedDiscountForGroceryItems() {
        List<UserType> userTypes = List.of(UserType.CUSTOMER, UserType.EMPLOYEE);
        User user = new User(userTypes, LocalDateTime.now());

        List<Item> items = List.of(
                new Item("Grocery Item 1", "Grocery Item 1", 1500.0, ItemType.GROCERY),
                new Item("Grocery Item 2", "Grocery Item 2", 500.0, ItemType.GROCERY),
                new Item("Grocery Item 3", "Grocery Item 3", 1000.0, ItemType.GROCERY));

        String originalCurrency = "PKR";
        String targetCurrency = "USD";

        when(currencyService.getExchangeRate(originalCurrency, targetCurrency)).thenReturn(0.003595);

        Double netPayableAmount = discountService.calculateDiscount(new BillRequest(user, items, originalCurrency, targetCurrency));

        assertEquals(10.24575, netPayableAmount, 0.01);
        verify(currencyService).getExchangeRate(originalCurrency, targetCurrency);
    }
}
