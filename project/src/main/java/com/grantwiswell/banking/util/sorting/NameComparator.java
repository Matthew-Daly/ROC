package com.grantwiswell.banking.util.sorting;

import com.grantwiswell.banking.model.Customer;

import java.util.Comparator;

public class NameComparator implements Comparator<Customer> {
    @Override
    public int compare(Customer c1, Customer c2) {
        return (c1.getLast_name() + c1.getFirst_name()).compareTo((c2.getLast_name() + c2.getFirst_name()));
    }
}
