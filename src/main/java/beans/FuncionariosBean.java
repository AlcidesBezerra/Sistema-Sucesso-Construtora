/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.hibernate.Query;
import org.hibernate.Session;
import pojos.Funcionarios;
import util.HibernateUtil;

/**
 *
 * @author alcid
 */
@ManagedBean
@Named(value = "funcionariosBean")
public class FuncionariosBean implements Serializable {

    private HibernateUtil hu;
    private Session ses;
    private List<Funcionarios> list;
    private Funcionarios funcionarioSelecionado;
    
    @PostConstruct
    public void init() {
       funcionarioSelecionado = new Funcionarios();
    }


    public List<Funcionarios> gerarListaFuncionarios(){
        list = new ArrayList<>();
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        Query qu = ses.createQuery("from Funcionarios");
        list = qu.list();
        ses.getTransaction().commit();
        ses.close();
        return list;
    }
    
    public void excluirFuncionario(Funcionarios funcionario){
    ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.delete(funcionario);
        ses.getTransaction().commit();
        ses.close();
        FacesContext context = FacesContext.getCurrentInstance();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "O funcionario foi deletado, atualize a pagina."));
    }
    
    public void selecionarFuncionario(Funcionarios funcionario){
        funcionarioSelecionado = funcionario;
        System.out.println(funcionarioSelecionado.toString());
    }
    
    
    public FuncionariosBean() {
    }

}
