package app;

import model.dao.DaoFactory;
import model.dao.DepartmentDAO;
import model.entities.Department;

import java.util.List;

public class Main2 {
    static void main() {
        //Lembrar que nessa implementação a conexão com a DB nunca é fechada pelo programa, isso poderia causar problemas
        //em um programa que estivesse em execução constante em um servidor (novas conexões seriam criadas toda hora sem ser fechadas),
        // mas como está localmente e o programa termina de executar, a conexão é fechada automaticamente

        DepartmentDAO depDao = DaoFactory.createDepDao();

        System.out.println("####################FINDBYID####################");
        System.out.println(depDao.findById(1));

        System.out.println("####################FINDALL####################");
        List<Department> list = depDao.findAll();

        for (Department dep : list){
            System.out.println(dep.toString());
        }

//        System.out.println("####################CREATE####################");
//        Department dep = new Department(null, "Tomfoolery");
//        depDao.insert(dep);
//
//        System.out.println("New Department registered: ID = " + dep.getId());

        System.out.println("####################UPDATE####################");
        Department department = depDao.findById(7);
        String oldname = department.getName();
        department.setName("Machining");
        depDao.update(department);

        System.out.printf("Updated department %d name from %s to %s%n", department.getId(), oldname, department.getName());

        System.out.println("####################DELETE####################");
        depDao.deleteById(9);
        System.out.println("Deleted");
    }
}
