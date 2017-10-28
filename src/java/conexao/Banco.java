/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexao;

import beans.Anuncio;
import beans.Filme;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
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
        String atores = "";
        String diretores = "";
        String generos = "";
        //byte[] imagem = null;
        try {
            Class.forName("org.postgresql.Driver");
            String contxt = "jdbc:postgresql://localhost:5432/postgres";
            Connection connection = DriverManager.getConnection(contxt, "postgres", "123456");
            String textosql = "select f.id, f.nome, f.ano, f.nome_original, fu.anunciado from filme f, filme_usuario fu where fu.nome_usuario = ? and fu.id_filme = f.id order by f.nome";
            PreparedStatement statement = connection.prepareStatement(textosql);
            statement.setString(1, login);            
            //Statement statement = (Statement) connection.createStatement();
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                //retorno += rs.getString("nome");
                
                Filme f = new Filme();
                f.setId(rs.getString("id"));
                
                //Connection connection2 = DriverManager.getConnection(contxt, "postgres", "123456");
                String textosql2 = "select a.nome from ator a, filme_ator fa where fa.id_ator = a.id and fa.id_filme = ?";                
                PreparedStatement statement2 = connection.prepareStatement(textosql2);
                statement2.setString(1, f.getId());            
                ResultSet rs2 = statement2.executeQuery();
                while (rs2.next()) {                                       
                    atores = atores + rs2.getString("nome");
                    atores = atores + ", ";
                }
                //connection2.close();
                
                f.setAtores(atores);                
                atores = " ";
                
                //Connection connection3 = DriverManager.getConnection(contxt, "postgres", "123456");
                String textosql3 = "select d.nome from diretor d, filme_diretor fd, filme f where fd.id_filme = f.id and fd.id_diretor = d.id and f.id = ?";                
                PreparedStatement statement3 = connection.prepareStatement(textosql3);
                statement3.setString(1, f.getId());            
                ResultSet rs3 = statement3.executeQuery();
                while (rs3.next()) {                    
                    diretores = diretores + rs3.getString("nome");
                    diretores = diretores + ", ";
                }
                //connection3.close();
                
                f.setDiretores(diretores);                
                diretores = " ";
                
                //Connection connection4 = DriverManager.getConnection(contxt, "postgres", "123456");
                String textosql4 = "select g.nome from genero g, filme_genero fg, filme f where fg.id_filme = f.id and fg.id_genero = g.id and f.id = ?";                
                PreparedStatement statement4 = connection.prepareStatement(textosql4);
                statement4.setString(1, f.getId());            
                ResultSet rs4 = statement4.executeQuery();
                while (rs4.next()) {                    
                    generos = generos + rs4.getString("nome");
                    generos = generos + ", ";
                }
                //connection4.close();
                
                f.setGeneros(generos);                
                generos = " ";
                
                /*Connection connection5 = DriverManager.getConnection(contxt, "postgres", "123456");
                String textosql5 = "select i.imagem from imagem i, filme f, usuario u where i.id_filme = f.id and i.nome_usuario = u.login and u.login = ? and f.id = ?";                
                PreparedStatement statement5 = connection.prepareStatement(textosql5);
                statement5.setString(1, login);
                statement5.setString(2, f.getId());            
                ResultSet rs5 = statement5.executeQuery();
                while (rs5.next()) {                    
                    imagem = rs5.getBytes("imagem"); 
                }
                connection5.close();                
                f.setImagem(imagem);
                //imagem = null;
                imagem = new byte[0];*/
                
                f.setAnunciado(rs.getBoolean("anunciado"));
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
                
                byte[] loginBytes = Base64.getEncoder().encode(login.getBytes());                
                byte[] senhaBytes = Base64.getEncoder().encode(senha.getBytes());                
                
                /*byte[] decodedBytes = Base64.getDecoder().decode(encodedBytes);
                System.out.println("decodedBytes " + new String(decodedBytes));*/
                
                statement.setString(1, new String (loginBytes));
                statement.setString(2, new String (senhaBytes));
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
            
            byte[] loginBytes = Base64.getEncoder().encode(login.getBytes()); 
            
            statement.setString(1, new String (loginBytes));
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
            
            byte[] loginBytes = Base64.getEncoder().encode(login.getBytes());                
            byte[] senhaBytes = Base64.getEncoder().encode(senha.getBytes());                
            
            statement.setString(1, new String (loginBytes));
            statement.setString(2, new String (senhaBytes));
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
    
    public List<Filme> buscaTodosFilmes(String login) {
        String retorno = "";
        List<Filme> lista = new ArrayList<Filme>();
        try {
            Class.forName("org.postgresql.Driver");
            String contxt = "jdbc:postgresql://localhost:5432/postgres";
            Connection connection = DriverManager.getConnection(contxt, "postgres", "123456");
            String textosql = "select f.* from filme f where f.id not in (select id_filme from filme_usuario where nome_usuario = ?) order by f.nome";
            PreparedStatement statement = connection.prepareStatement(textosql);
            statement.setString(1, login);            
            //Statement statement = (Statement) connection.createStatement();
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                //retorno += rs.getString("nome");
                
                Filme f = new Filme();
                f.setNome(rs.getString("nome"));
                f.setId(rs.getString("id"));
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
    
    public String adicionaFilmes(String filmes, String login) {
        String retorno = "";        
        //int quantidade = this.buscaUsuario(login);
        //if (quantidade == 0) {        
        String filmesArray[];
        filmesArray = filmes.split(",");
        for (int i = 0; i < filmesArray.length; i++) {
            try {
                Class.forName("org.postgresql.Driver");
                String contxt = "jdbc:postgresql://localhost:5432/postgres";
                Connection connection = DriverManager.getConnection(contxt, "postgres", "123456");
                String textosql = "insert into filme_usuario values (?, ?)";
                PreparedStatement statement = connection.prepareStatement(textosql);                
                statement.setString(1, filmesArray[i]);
                statement.setString(2, login);
                statement.executeUpdate();
                //retorno = "Conta cadastrada com sucesso!!";
                //ResultSet rs = statement.executeQuery(textosql);
                //while (rs.next()) {
                //    retorno += rs.getString("nome");
                //}
                retorno = (i + 1) + " Filme(s) adicionado(s)!!";
                connection.close();
            } catch (ClassNotFoundException | SQLException ex) { 
                retorno = "Erro, tente novamente mais tarde!!";
                Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //} else {
            //retorno = "Conta já existe!!";        
        //}
        return retorno;
    }
    
    public String salvaImagem(String id_filme, String login, byte[] imagem) {
        String retorno = "";        
        
            try {
                Class.forName("org.postgresql.Driver");
                String contxt = "jdbc:postgresql://localhost:5432/postgres";
                Connection connection = DriverManager.getConnection(contxt, "postgres", "123456");
                String textosql = "insert into imagem (id_filme, nome_usuario, imagem) values (?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(textosql);                
                statement.setString(1, id_filme);
                statement.setString(2, login);                
                statement.setBytes(3, imagem);                 
                statement.executeUpdate();                
                connection.close();
            } catch (ClassNotFoundException | SQLException ex) { 
                //retorno = "Erro, tente novamente mais tarde!!";
                retorno = ex.getMessage();
                Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        //} else {
            //retorno = "Conta já existe!!";        
        //}
        return retorno;
    }
    
    public String removeFilme(String id_filme, String login) {
        String retorno = "";        
        
            try {
                Class.forName("org.postgresql.Driver");
                String contxt = "jdbc:postgresql://localhost:5432/postgres";
                Connection connection = DriverManager.getConnection(contxt, "postgres", "123456");
                String textosql = "delete from filme_usuario where id_filme = ? and nome_usuario = ?";
                PreparedStatement statement = connection.prepareStatement(textosql);                
                statement.setString(1, id_filme);
                statement.setString(2, login);                                
                statement.executeUpdate();  
                retorno = "Filme removido com sucesso!!";
                connection.close();
            } catch (ClassNotFoundException | SQLException ex) { 
                retorno = "Erro, tente novamente mais tarde!!";                
                Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return retorno;
    }
    
    public int salvaAnuncio(String id_filme, String login, String descricao) {
        int retorno = 0;        
        
            try {
                Class.forName("org.postgresql.Driver");
                String contxt = "jdbc:postgresql://localhost:5432/postgres";
                Connection connection = DriverManager.getConnection(contxt, "postgres", "123456");
                String textosql = "insert into filme_anuncio (id_filme, nome_usuario, descricao) values (?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(textosql);                
                statement.setString(1, id_filme);
                statement.setString(2, login);                
                statement.setString(3, descricao);                 
                statement.executeUpdate();                               
                
                String textosql2 = "update filme_usuario set anunciado = true where id_filme = ? and nome_usuario = ?";
                PreparedStatement statement2 = connection.prepareStatement(textosql2);                
                statement2.setString(1, id_filme);
                statement2.setString(2, login);                                
                statement2.executeUpdate();  
                
                connection.close();
            } catch (ClassNotFoundException | SQLException ex) { 
                //retorno = "Erro, tente novamente mais tarde!!";
                retorno = 1;
                Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
            }
 
        return retorno;
    }
    
    public List<Anuncio> buscaAnuncio(String login) {        
        List<Anuncio> lista = new ArrayList<Anuncio>();
        try {
            Class.forName("org.postgresql.Driver");
            String contxt = "jdbc:postgresql://localhost:5432/postgres";
            Connection connection = DriverManager.getConnection(contxt, "postgres", "123456");
            String textosql = "select fa.id, fa.descricao, f.nome, fa.id_filme from filme_anuncio fa, filme f where fa.nome_usuario = ? and fa.id_filme = f.id";
            PreparedStatement statement = connection.prepareStatement(textosql);
            statement.setString(1, login);                        
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                //retorno += rs.getString("nome");
                Anuncio a = new Anuncio();
                a.setId(rs.getString("id"));
                a.setDescricao(rs.getString("descricao"));                
                a.setNomeFilme(rs.getString("nome"));
                a.setId_filme(rs.getString("id_filme"));
                lista.add(a);
            }
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return lista;
    }
    
    public int atualizaAnuncio(String id, String descricao) {
        int retorno = 0;                
            try {
                Class.forName("org.postgresql.Driver");
                String contxt = "jdbc:postgresql://localhost:5432/postgres";
                Connection connection = DriverManager.getConnection(contxt, "postgres", "123456");
                String textosql = "update filme_anuncio set descricao = ? where id = ?";
                PreparedStatement statement = connection.prepareStatement(textosql);                
                statement.setString(1, descricao);
                statement.setString(2, id);                                
                statement.executeUpdate();
                connection.close();
            } catch (ClassNotFoundException | SQLException ex) { 
                //retorno = "Erro, tente novamente mais tarde!!";
                retorno = 1;
                Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
            }
 
        return retorno;
    }
    
    public int removeAnuncio(String id_anuncio, String id_filme, String nome_usuario) {
        int retorno = 0;        
        
            try {
                Class.forName("org.postgresql.Driver");
                String contxt = "jdbc:postgresql://localhost:5432/postgres";
                Connection connection = DriverManager.getConnection(contxt, "postgres", "123456");
                String textosql = "delete from filme_anuncio where id = ?";
                PreparedStatement statement = connection.prepareStatement(textosql);                
                statement.setString(1, id_anuncio);                                             
                statement.executeUpdate();  
                
                String textosql2 = "update filme_usuario set anunciado = false where id_filme = ? and nome_usuario = ?";
                PreparedStatement statement2 = connection.prepareStatement(textosql2);                
                statement2.setString(1, id_filme);
                statement2.setString(2, nome_usuario);
                statement2.executeUpdate();                
                
                connection.close();
                
                connection.close();
            } catch (ClassNotFoundException | SQLException ex) { 
                //retorno = "Erro, tente novamente mais tarde!!";
                retorno = 1;
                Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return retorno;
    }
    
    public List<Anuncio> buscaTodosAnuncios(String login) {        
        List<Anuncio> lista = new ArrayList<Anuncio>();
        try {
            Class.forName("org.postgresql.Driver");
            String contxt = "jdbc:postgresql://localhost:5432/postgres";
            Connection connection = DriverManager.getConnection(contxt, "postgres", "123456");
            String textosql = "select fa.id, fa.descricao, f.nome, fa.id_filme, fa.nome_usuario from filme_anuncio fa, filme f where fa.id_filme = f.id and fa.nome_usuario <> ?";
            PreparedStatement statement = connection.prepareStatement(textosql);
            statement.setString(1, login);                        
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                //retorno += rs.getString("nome");
                Anuncio a = new Anuncio();
                a.setId(rs.getString("id"));
                a.setDescricao(rs.getString("descricao"));                
                a.setNomeFilme(rs.getString("nome"));
                a.setId_filme(rs.getString("id_filme"));
                a.setNome_usuario(rs.getString("nome_usuario"));
                lista.add(a);
            }
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return lista;
    }
}