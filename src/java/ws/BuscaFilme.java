/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import beans.Anuncio;
import beans.Filme;
import conexao.Banco;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Deam
 */
@WebService(serviceName = "BuscaFilme")
public class BuscaFilme {
    
    @WebMethod(operationName = "buscaFilme")
    //public String buscaFilme() {
    public List<Filme> buscaFilme(@WebParam(name = "login") String login) {
        Banco a = new Banco();
        return a.buscaFilme(login);        
    }
    
    @WebMethod(operationName = "buscaTodosFilmes")
    public List<Filme> buscaTodosFilmes(@WebParam(name = "login") String login) {
        Banco a = new Banco();
        return a.buscaTodosFilmes(login);        
    }
    
    @WebMethod(operationName = "adicionaFilmes")
    public String adicionaFilmes(@WebParam(name = "filmes") String filmes, @WebParam(name = "login") String login) {
        Banco a = new Banco();
        return a.adicionaFilmes(filmes, login);        
    }
    
    @WebMethod(operationName = "salvaImagem")
    public String salvaImagem(@WebParam(name = "id_filme") String id_filme, @WebParam(name = "login") String login, @WebParam(name = "imagem") byte[] imagem) {
        Banco a = new Banco();
        return a.salvaImagem(id_filme, login, imagem);        
    }
    
    @WebMethod(operationName = "removeFilme")
    public String removeFilme(@WebParam(name = "id_filme") String id_filme, @WebParam(name = "login") String login) {
        Banco a = new Banco();
        return a.removeFilme(id_filme, login);        
    }
    
    @WebMethod(operationName = "salvaAnuncio")
    public int salvaAnuncio(@WebParam(name = "id_filme") String id_filme, @WebParam(name = "login") String login, @WebParam(name = "descricao") String descricao) {
        Banco a = new Banco();
        return a.salvaAnuncio(id_filme, login, descricao);        
    }
    
    @WebMethod(operationName = "buscaAnuncio")    
    public List<Anuncio> buscaAnuncio(@WebParam(name = "login") String login) {
        Banco a = new Banco();
        return a.buscaAnuncio(login);        
    }   
    
    @WebMethod(operationName = "atualizaAnuncio")    
    public int atualizaAnuncio(@WebParam(name = "id") String id, @WebParam(name = "descricao") String descricao) {
        Banco a = new Banco();
        return a.atualizaAnuncio(id, descricao);        
    } 
    
    @WebMethod(operationName = "removeAnuncio")    
    public int removeAnuncio(@WebParam(name = "id_anuncio") String id_anuncio, @WebParam(name = "id_filme") String id_filme, @WebParam(name = "nome_usuario") String nome_usuario) {
        Banco a = new Banco();
        return a.removeAnuncio(id_anuncio, id_filme, nome_usuario);        
    }
    
    @WebMethod(operationName = "buscaTodosAnuncios")
    public List<Anuncio> buscaTodosAnuncios(@WebParam(name = "login") String login) {
        Banco a = new Banco();
        return a.buscaTodosAnuncios(login);        
    } 
}