package model.dao;

import model.dao.impl.SellerDAOJDBC;

public class DaoFactory {

    public static SellerDAO createSellerDao(){
        return new SellerDAOJDBC();
    }
}
