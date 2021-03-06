package com.grantwiswell.banking.service.impl;

import com.grantwiswell.banking.dao.CustomerCrudDao;
import com.grantwiswell.banking.dao.impl.CustomerCrudDaoImpl;
import com.grantwiswell.banking.exception.BankException;
import com.grantwiswell.banking.model.Customer;
import com.grantwiswell.banking.service.CustomerCrudService;
import com.grantwiswell.banking.service.CustomerSearchService;
import com.grantwiswell.banking.service.impl.util.ValidationUtil;
import com.grantwiswell.banking.util.InputUtil;
import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CustomerCrudServiceImpl implements CustomerCrudService {

    CustomerCrudDao customerCrudDao = new CustomerCrudDaoImpl();
    CustomerSearchService customerSearchService = new CustomerSearchServiceImpl();
    private Logger log = Logger.getLogger(CustomerCrudServiceImpl.class);

    @Override
    public boolean createNewCustomer(String contact_email, String password, String fullName, String dobString) throws BankException {
        if(!ValidationUtil.isValidEmail(contact_email)) throw new BankException("Invalid email address! please try again.");
        if(password.length() < 8)throw new BankException("Password must be at least 8 characters! Please try again.");
        LocalDate localDob = null;

        try{
            localDob = LocalDate.parse(dobString, DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        } catch (Exception e) {
            InputUtil.setMessagePrompt(e.getMessage());
        }
        log.debug(localDob);

        if(Period.between(localDob, LocalDate.now()).getYears() < 16) throw new BankException("You must be at least 16 years old to register");

        Date dob = Date.from(localDob.atStartOfDay(ZoneId.systemDefault()).toInstant());

        if (!dob.before(new Date())) throw new BankException("You cannot be born today...");

        String[] names = fullName.split(" ");
        if(names.length > 2){
            throw new BankException("Too many names entered, please provide only your first and last name.");
        }
        else if(names.length < 2){
            throw new BankException("You have not entered both of your names, please enter your first and last name to create an account.");
        }

        boolean createSuccessful = false;
        try{
            Customer customer = new Customer(names[0], names[1], contact_email, password, dob, "PENDING");
            createSuccessful = customerCrudDao.createNewCustomer(customer) > 0;
        } catch (BankException e) {
            InputUtil.setMessagePrompt(e.getMessage());
        }

        return createSuccessful;
    }

    @Override
    public void acceptCustomer(int id) throws BankException {
        if(!ValidationUtil.isValidCustomerId(id)) throw new BankException("ID must be a 3-digit number");
        try{
            Customer customer = customerSearchService.getCustomerById(id);
            if(!customer.getStatus().equalsIgnoreCase("PENDING")) throw new BankException("Customer is not eligible to be accepted!");
            customerCrudDao.updateCustomerStatus(customer.getId(), "ACCEPTED");
        } catch (BankException e) {
            InputUtil.setMessagePrompt(e.getMessage());
        }
    }

    @Override
    public void rejectCustomer(int id) throws BankException {
        if(!ValidationUtil.isValidCustomerId(id)) throw new BankException("ID must be a 3-digit number");
        try{
            Customer customer = customerSearchService.getCustomerById(id);
            if(customer.getStatus().equalsIgnoreCase("REJECTED")) throw new BankException("Customer is already rejected!");
            customerCrudDao.updateCustomerStatus(customer.getId(), "REJECTED");
        } catch (BankException e) {
            InputUtil.setMessagePrompt(e.getMessage());
        }
    }
}
