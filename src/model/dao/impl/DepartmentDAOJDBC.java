package model.dao.impl;

import db.model.exception.DbException;
import model.dao.DepartmentDAO;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAOJDBC implements DepartmentDAO {
    private Connection conn;

    public DepartmentDAOJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {
        String sql = """
                    INSERT INTO department
                    (Name)
                    VALUES
                    (?)
                    """;

        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            st.setString(1, obj.getName());

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
    public void update(Department obj) {
        String sql = """
                    UPDATE department
                    SET Name = ?
                    WHERE Id = ?
                    """;

        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            st.setString(1, obj.getName());
            st.setInt(2, obj.getId());

            st.executeUpdate();

        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = """
                    DELETE FROM department
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
    public Department findById(Integer id) {
        String sql = """
                    SELECT department.*
                    FROM department\s
                    WHERE department.Id = ?
                    """;
        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, id);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {


                    return instantiateDep(rs);
                }
            }
            return null;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public List<Department> findAll() {
        String sql = """
                    SELECT department.*
                    FROM department
                    ORDER BY Id
                    """;

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            try (ResultSet rs = st.executeQuery()) {

                List<Department> list = new ArrayList<>();

                while (rs.next()) {
                    list.add(instantiateDep(rs));
                }

                return list;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public Department instantiateDep(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("Id"));
        dep.setName(rs.getString("Name"));
        return dep;
    }
}
