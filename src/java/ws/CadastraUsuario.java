/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import conexao.Banco;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Deam
 */
@WebService(serviceName = "CadastraUsuario")
public class CadastraUsuario {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "cadastraUsuario")
    public String cadastraUsuario(@WebParam(name = "login") String login, @WebParam(name = "senha") String senha) {
        Banco a = new Banco();
        return a.cadastraUsuario(login, senha);
    }

    @WebMethod(operationName = "verificaUsuario")
    public int verificaUsuario(@WebParam(name = "login") String login, @WebParam(name = "senha") String senha) {
        Banco a = new Banco();
        return a.verificaUsuario(login, senha);
    }
}