package model.dao;

import db.model.util.DB;
import model.dao.impl.DepartmentDAOJDBC;
import model.dao.impl.SellerDAOJDBC;


public class DaoFactory {

    public static SellerDAO createSellerDao(){
        return new SellerDAOJDBC(DB.getConnection());
    }

    public static DepartmentDAO createDepDao(){return new DepartmentDAOJDBC(DB.getConnection());}

}
