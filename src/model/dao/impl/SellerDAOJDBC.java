package model.dao.impl;

import db.model.exception.DbException;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDAOJDBC implements SellerDAO {
    private Connection conn;

    public SellerDAOJDBC(Connection conn) {
        this.conn = conn;
    }


    @Override
    public void insert(Seller obj) {
        String sql = "INSERT INTO seller "
                + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                + "VALUES "
                + "(?, ?, ?, ?, ?) ";

        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0){
                try (ResultSet rs = st.getGeneratedKeys()){
                    if (rs.next()){
                        int id = rs.getInt(1);
                        obj.setId(id);
                    }
                }
            }else {
                throw new DbException("Unexpected error, no rows affected!");
            }
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void update(Seller obj) {
        String sql = """
                    UPDATE seller
                    SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ?
                    WHERE Id = ?
                    """;

        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());
            st.setInt(6, obj.getId());

            st.executeUpdate();

        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = """
                    DELETE FROM seller
                    WHERE Id = ?
                    """;

        try (PreparedStatement st = conn.prepareStatement(sql)){
            conn.setAutoCommit(false);

            st.setInt(1, id);

            st.executeUpdate();

            conn.commit();

        }catch (SQLException e){
            try {
                conn.rollback();
                throw new DbException("Transaction rolled back! Caused by: " + e.getMessage());
            } catch (SQLException ex) {
                throw new DbException("Database rollback failed, God save you, Caused by: " + ex.getMessage());
            }
        }
    }

    @Override
    public Seller findById(Integer id) {
        String sql = "SELECT seller.*,department.Name as DepName "
                + "FROM seller INNER JOIN department "
                + "ON seller.DepartmentId = department.Id "
                + "WHERE seller.Id = ? ";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, id);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Department dep = instantiateDep(rs);

                    return instantiateSeller(rs, dep);
                }
            }
            return null;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    //O problema dessa implementação é que os dados do objeto department passado não necessariamente condizem com os do banco de dados
    //Apenas sua chave primária (Id) precisa estar correta, caso outros valores estejam diferentes, será criada uma lista com dados que não condizem
    //com o banco de dados
    public List<Seller> findByDepartment(Department dep) {

        String sql = "SELECT seller.*,department.Name as DepName "
                + "FROM seller INNER JOIN department "
                + "ON seller.DepartmentId = department.Id "
                + "WHERE DepartmentId = ? "
                + "ORDER BY Name  ";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, dep.getId());

            try (ResultSet rs = st.executeQuery()) {

                List<Seller> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(instantiateSeller(rs, dep));
                }

                return list;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    //Essa implantação com Map poderia ser utilizada no findByDepartment para assegurar que o mesmo Department é usado
    //em todos os Sellers E que os dados que Department é inicializado com são condizentes com os dados reais do banco.
    @Override
    public List<Seller> findAll() {
        String sql = "SELECT seller.*,department.Name as DepName "
                + "FROM seller INNER JOIN department "
                + "ON seller.DepartmentId = department.Id "
                + "ORDER BY Name ";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            try (ResultSet rs = st.executeQuery()) {

                List<Seller> list = new ArrayList<>();
                Map<Integer, Department> map = new HashMap<>();

                while (rs.next()) {
                    Department dep = map.get(rs.getInt("DepartmentId"));

                    if (dep == null){
                        dep = instantiateDep(rs);
                        map.put(rs.getInt("DepartmentId"), dep);
                    }

                    list.add(instantiateSeller(rs, dep));
                }

                return list;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public Department instantiateDep(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    public Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller obj = new Seller();
        obj.setId(rs.getInt("Id"));
        obj.setName(rs.getString("Name"));
        obj.setEmail(rs.getString("Email"));
        obj.setBaseSalary(rs.getDouble("BaseSalary"));
        obj.setBirthDate(rs.getDate("BirthDate"));
        obj.setDepartment(dep);

        return obj;
    }
}
