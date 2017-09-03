/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import beans.Filme;
import conexao.Banco;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;

/**
 *
 * @author Deam
 */
@WebService(serviceName = "BuscaFilme")
public class BuscaFilme {
    
    @WebMethod(operationName = "buscaFilme")
    //public String buscaFilme() {
    public List<Filme> buscaFilme() {
        Banco a = new Banco();
        return a.buscaFilme();        
    }
}