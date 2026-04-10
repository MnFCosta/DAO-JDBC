package app;

import model.dao.DaoFactory;
import model.dao.SellerDAO;
import model.entities.Seller;

public class Main {
    static void main() {

        SellerDAO sellerDAO = DaoFactory.createSellerDao();

        Seller seller = sellerDAO.findById(3);

        System.out.println(seller);
    }
}
