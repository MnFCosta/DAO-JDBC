package app;

import model.dao.DaoFactory;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;

public class Main {
    static void main() {
        Department dp = new Department(1, "Books");
        Seller seller = new Seller(1, "Manoel Costa", "manoel@gmail.com", new Date(), 3000.0, dp);

        SellerDAO sellerDAO = DaoFactory.createSellerDao();

        System.out.println(dp);
        System.out.println(seller);
    }
}
