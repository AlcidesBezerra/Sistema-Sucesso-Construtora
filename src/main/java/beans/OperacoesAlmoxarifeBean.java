/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.primefaces.event.RowEditEvent;
import pojos.OperacoesAlmoxarife;
import pojos.Produtos;
import util.HibernateUtil;

/**
 *
 * @author alcid
 */
@ManagedBean
@Named(value = "operacoesAlmoxarifeBean")
public class OperacoesAlmoxarifeBean {

    private HibernateUtil hu;
    private Session ses;
    private List<OperacoesAlmoxarife> list;
    
    
    @PostConstruct
    public void init() {
        gerarListaAlmoxarife();
        atualizarLista();
    }
    
    
    public List<OperacoesAlmoxarife> gerarListaAlmoxarife() {
        if (list == null) {
        list = new ArrayList<>();
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        Query qu = ses.createQuery("from " + OperacoesAlmoxarife.class.getName());
        list = qu.list();
        ses.getTransaction().commit();
        ses.close();
        }
        return list;
    }
    
    public void excluirProduto(OperacoesAlmoxarife op) {
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.delete(op);
        ses.getTransaction().commit();
        ses.close();
        atualizarLista();
        FacesContext context = FacesContext.getCurrentInstance();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "O item foi deletado com sucesso"));
    }
    
    public List<OperacoesAlmoxarife> atualizarLista() {
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        Query qu = ses.createQuery("from " + OperacoesAlmoxarife.class.getName());
        list = qu.list();
        ses.getTransaction().commit();
        ses.close();
        return list;
    }
    
    public void onRowEdit(RowEditEvent event) throws ValidatorException {
        OperacoesAlmoxarife novoProduto = (OperacoesAlmoxarife) event.getObject();
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.update((OperacoesAlmoxarife) event.getObject());
        ses.getTransaction().commit();
        ses.close();

    }
    
    
    public OperacoesAlmoxarifeBean() {
    }
    
}
