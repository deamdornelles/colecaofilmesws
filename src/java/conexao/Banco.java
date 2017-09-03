/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexao;

import beans.Filme;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Deam
 */
public class Banco {

    public static void main(String arg[]) {
        //Banco a = new Banco();
        //System.out.println(a.buscaFilme());
    }

    //public String buscaFilme() {
    public List<Filme> buscaFilme(String login) {
        String retorno = "";
        List<Filme> lista = new ArrayList<Filme>();
        try {
            Class.forName("org.postgresql.Driver");
            String contxt = "jdbc:postgresql://localhost:5432/postgres";
            Connection connection = DriverManager.getConnection(contxt, "postgres", "123456");
            String textosql = "select f.nome, f.ano, f.nome_original from filme f, filme_usuario fu where fu.nome_usuario = ? and fu.id_filme = f.id order by f.nome";
            PreparedStatement statement = connection.prepareStatement(textosql);
            statement.setString(1, login);            
            //Statement statement = (Statement) connection.createStatement();
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                //retorno += rs.getString("nome");
                
                Filme f = new Filme();
                f.setNome(rs.getString("nome"));
                f.setAno(rs.getInt("ano"));
                f.setNomeOriginal(rs.getString("nome_original"));
                lista.add(f);
            }
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }
        //return retorno;
        return lista;
    }

    public String cadastraUsuario(String login, String senha) {
        String retorno;        
        int quantidade = this.buscaUsuario(login);
        if (quantidade == 0) {
            try {
                Class.forName("org.postgresql.Driver");
                String contxt = "jdbc:postgresql://localhost:5432/postgres";
                Connection connection = DriverManager.getConnection(contxt, "postgres", "123456");
                String textosql = "insert into usuario (login, senha) values (?, ?)";
                PreparedStatement statement = connection.prepareStatement(textosql);
                statement.setString(1, login);
                statement.setString(2, senha);
                statement.executeUpdate();
                retorno = "Conta cadastrada com sucesso!!";
                //ResultSet rs = statement.executeQuery(textosql);
                //while (rs.next()) {
                //    retorno += rs.getString("nome");
                //}
                connection.close();
            } catch (ClassNotFoundException | SQLException ex) { 
                retorno = "Erro, tente novamente mais tarde!!";
                Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            retorno = "Conta já existe!!";        
        }
        return retorno;
    }

    public int buscaUsuario(String login) {
        int quantidade = 0;
        try {
            Class.forName("org.postgresql.Driver");
            String contxt = "jdbc:postgresql://localhost:5432/postgres";
            Connection connection = DriverManager.getConnection(contxt, "postgres", "123456");
            String textosql = "select * from usuario where login = ?";
            PreparedStatement statement = connection.prepareStatement(textosql);
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                quantidade++;
            }            
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }
        return quantidade;
    }
    
    public int verificaUsuario(String login, String senha) {
        int quantidade = 0;
        try {
            Class.forName("org.postgresql.Driver");
            String contxt = "jdbc:postgresql://localhost:5432/postgres";
            Connection connection = DriverManager.getConnection(contxt, "postgres", "123456");
            String textosql = "select * from usuario where login = ? and senha = ?";
            PreparedStatement statement = connection.prepareStatement(textosql);
            statement.setString(1, login);
            statement.setString(2, senha);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                quantidade++;
            }            
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }
        return quantidade;
    }
}
