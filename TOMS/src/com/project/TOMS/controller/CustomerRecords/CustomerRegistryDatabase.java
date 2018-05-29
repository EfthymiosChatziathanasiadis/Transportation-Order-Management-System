/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.controller.CustomerRecords;

import com.project.TOMS.algorithms.dijkstra.controller.graphRegistryDatabase;
import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import com.project.TOMS.controller.CustomerOrders.CustomerOrderRegistryDatabase;
import com.project.TOMS.controller.DispositionRecords.DispositionRegistryDatabase;
import com.project.TOMS.database.Database;
import com.project.TOMS.model.CustomerRecords.Customer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class CustomerRegistryDatabase {

    Database database = Database.getInstance();
    static CustomerRegistryDatabase INSTANCE = null;
    ArrayList<Customer> customers;
    graphRegistryDatabase graphDatabase = graphRegistryDatabase.getInstance();

    private CustomerRegistryDatabase() {
        this.loadCustomers();
    }

    public static CustomerRegistryDatabase getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CustomerRegistryDatabase();
        }
        return INSTANCE;
    }
// TODO: create user defined exception.to handle integrity cases

    public void addCustomer(Vertex address, String company, String phone) {
        String sql = "INSERT INTO Customer(company, phone, addressId) VALUES ('"+company+"','"+phone+"','"+address.getId()+"');";
        database.connect("central");
        try {
            database.update(sql);
            int id = database.getTableCount("Customer");
            Customer customer = new Customer(id, company, phone, address);
            customers.add(customer);
        } catch (SQLException ex) {
            ex.printStackTrace();
            if (ex.getErrorCode() == 0) {

            }
        }
        database.closeConnection();
    }

    public Customer getCustomerOfOrder(int id) {
        Customer customer = null;
        if (customers == null) {
            this.getCustomers();
        }
        Iterator<Customer> it = customers.iterator();
        while (it.hasNext()) {
            customer = it.next();
            if (customer.getId() == id) {
                break;
            }
        }

        return customer;
    }
    private void loadCustomers() {
        customers = new ArrayList<Customer>();
        String sql = "SELECT * FROM Customer;";
        try {
            ResultSet rs;
            database.connect("central");
            rs = database.query(sql);

            while (rs.next()) {
                Customer customer = new Customer(rs.getInt("id"), 
                        rs.getString("company"), 
                        rs.getString("phone"),graphDatabase.getVertexWhereId(Integer.toString(rs.getInt("addressId"))));
                customers.add(customer);
            }

            database.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    public ArrayList<Customer> getCustomers() {
        
        return customers;
    }

// TODO: create user defined exception.to handle integrity cases
    public void editCustomer(Customer customer, Vertex address, String company, String phone) {
        customer.setCompany(company);
        customer.setPhone(phone);
        customer.setAddress(address);
        String sql = "UPDATE Customer SET  company='"+company+"', phone='"+phone+"', addressId='"+address.getId()+"' WHERE id='"+customer.getId()+"';";
        database.connect("central");
        try {
            database.update(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        database.closeConnection();
    }

    public void delete(Customer customer) {
        String sql = "DELETE FROM Customer WHERE id='"+customer.getId()+"';";
        database.connect("central");
        try {
            database.update(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        database.closeConnection();
        this.loadCustomers();
        CustomerOrderRegistryDatabase.getInstance().loadOrders();
        DispositionRegistryDatabase.getInstance().loadDispositions();
    }

}
