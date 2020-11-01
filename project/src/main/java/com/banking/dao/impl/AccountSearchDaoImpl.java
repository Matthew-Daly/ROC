package com.banking.dao.impl;

import com.banking.dao.AccountSearchDao;
import com.banking.dao.queries.AccountQueries;
import com.banking.dao.util.DaoAccountUtil;
import com.banking.dao.util.DaoUtil;
import com.banking.exception.BankException;
import com.banking.jdbutil.PostgresSqlConnection;
import com.banking.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountSearchDaoImpl implements AccountSearchDao {
    @Override
    public Account getAccountByNumber(int number) throws BankException {
        return null;
    }

    @Override
    public List<Account> getAccountsByCustomerId(int customerId) throws BankException {
        List<Account> accountList = new ArrayList<>();

        try(Connection connection = PostgresSqlConnection.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(AccountQueries.GET_ACCOUNTS_BY_CUSTOMER_ID);
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Account account = DaoAccountUtil.getAccountFromResultSet(resultSet);
                accountList.add(account);
            }
            if(accountList.size() == 0){
                System.out.println("This customer has no accounts!");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(DaoUtil.INTERNAL_ERROR  + "\n"+e.getMessage());
        }

        return accountList;
    }

    @Override
    public Account[] getAccountInBalanceRange(double minBalance, double maxBalance) throws BankException {
        return new Account[0];
    }
}
