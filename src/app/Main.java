package app;

import model.dao.DaoFactory;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

import java.util.List;

public class Main {
    static void main() {

        SellerDAO sellerDAO = DaoFactory.createSellerDao();
        Department dep = new Department(2, "Eletronics");

        //FindById
        Seller sell = sellerDAO.findById(3);
        System.out.println(sell);

        //FindByDepartment
        List<Seller> seller = sellerDAO.findByDepartment(dep);

        for (Seller obj : seller){
            System.out.println(obj.toString());
        }
    }
}
