package app;

import model.dao.DaoFactory;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
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

        //FindAll
        System.out.println("####################FINDALL####################");
        List<Seller> allSellers = sellerDAO.findAll();

        for (Seller obj : allSellers){
            System.out.println(obj.toString());
        }

        //CREATE
        System.out.println("####################CREATE####################");
        Seller newSeller = new Seller(null, "Walter", "walter@gmail.com", new Date(), 5000.0, dep);
        sellerDAO.insert(newSeller);

        System.out.println("New Seller registered, ID = " + newSeller.getId());

        //UPDATE
        System.out.println("####################UPDATE####################");
        Seller update = sellerDAO.findById(1);
        update.setName("Pitoco");
        sellerDAO.update(update);
        System.out.println("Update complete");

        //DELETE
    }
}
