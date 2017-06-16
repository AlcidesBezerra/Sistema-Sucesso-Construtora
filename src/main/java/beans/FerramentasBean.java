/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import static beans.ProdutosBean.novaOperacaoAlmoxarife;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import pojos.Ferramentas;
import pojos.OperacoesFerramentas;
import util.HibernateUtil;

/**
 *
 * @author alcid
 */
@ManagedBean
@Named(value = "ferramentasBean")
public class FerramentasBean {

    private Ferramentas novaFerramenta = new Ferramentas();
    private HibernateUtil hu;
    private Session ses;
    private List<Ferramentas> list;
    private OperacoesFerramentas novaOperacaoFerramentas;
    private int codigoFerramenta;

    private Date data = new Date();

    //Nova Operacao
    public void novaOperacao() {

    }

    //Gerar lista de ferramentas
    public List<Ferramentas> gerarListaDeProdutos() {
        if (list == null) {
            list = new ArrayList<>();
            ses = hu.getSessionFactory().openSession();
            ses.beginTransaction();
            Query qu = ses.createQuery("from " + Ferramentas.class.getName());
            list = qu.list();
            ses.getTransaction().commit();
            ses.close();
        }
        return list;
    }
    
    //Gerar lista de operacoes
    public List<OperacoesFerramentas> gerarListaDeOperacoesFerramentas() {
        List<OperacoesFerramentas> lista;
            lista = new ArrayList<>();
            ses = hu.getSessionFactory().openSession();
            ses.beginTransaction();
            Query qu = ses.createQuery("from " + OperacoesFerramentas.class.getName());
            lista = qu.list();
            ses.getTransaction().commit();
            ses.close();
        return lista;
    }

    //Movimentacao
    public void inserirOperacao() {
        
        if (!getFerramenta(codigoFerramenta).equals("EM USO") && !getFerramenta(codigoFerramenta).equals("EM EM ESTOQUE") && novaOperacaoFerramentas.getRetirada()) {
            novaOperacaoFerramentas.setFerramenta(getFerramenta(codigoFerramenta));
            if(!novaOperacaoFerramentas.getRetirada()){
                novaOperacaoFerramentas.setDevolucao(true);
            }else{
            novaOperacaoFerramentas.setDevolucao(false);
            }
            ses = hu.getSessionFactory().openSession();
            ses.beginTransaction();
            ses.save(novaOperacaoFerramentas);
            ses.getTransaction().commit();
            ses.close();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "A operacao foi efetuada!"));

            ses = hu.getSessionFactory().openSession();
            ses.getTransaction().begin();
            Query query = ses.createSQLQuery("UPDATE ferramentas set situacao = :situacao, usuario = :usuario where Id = :id");
            query.setParameter("situacao", "EM USO");
            query.setParameter("usuario", novaOperacaoFerramentas.getUsuario());
            query.setParameter("id", codigoFerramenta);
            int result = query.executeUpdate();
            ses.getTransaction().commit();
            ses.close();
        }
        else if (!getFerramenta(codigoFerramenta).equals("EM ESTOQUE") && !getFerramenta(codigoFerramenta).equals("EM USO") && !novaOperacaoFerramentas.getRetirada()) {
            novaOperacaoFerramentas.setFerramenta(getFerramenta(codigoFerramenta));
            if(!novaOperacaoFerramentas.getRetirada()){
                novaOperacaoFerramentas.setDevolucao(true);
            }else{
            novaOperacaoFerramentas.setDevolucao(false);
            }
            ses = hu.getSessionFactory().openSession();
            ses.beginTransaction();
            ses.save(novaOperacaoFerramentas);
            ses.getTransaction().commit();
            ses.close();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "A operacao foi efetuada!"));

            ses = hu.getSessionFactory().openSession();
            ses.getTransaction().begin();
            Query query = ses.createSQLQuery("UPDATE ferramentas set situacao = :situacao , usuario = :usuario where Id = :id");
            query.setParameter("situacao", "EM ESTOQUE");
            query.setParameter("usuario", "-");
            query.setParameter("id", codigoFerramenta);
            int result = query.executeUpdate();
            ses.getTransaction().commit();
            ses.close();
        }

        System.out.println(novaOperacaoFerramentas);
    }

    //Inserir nova ferramenta
    public void inserirFerramenta(Ferramentas ferramenta) {
        novaFerramenta.setSituacao("EM ESTOQUE");
        novaFerramenta.setObs("-");
        novaFerramenta.setUsuario("-");
        
        try {
            ses = hu.getSessionFactory().openSession();
            ses.beginTransaction();
            ses.save(ferramenta);
            ses.getTransaction().commit();
            ses.close();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "A ferramenta foi cadastrada com sucesso."));
        } catch (HibernateException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "Cheque sua conexão ou contacte o administrador."));
        }
    }

    //Excluir ferramenta
    public void excluirFerramenta(Ferramentas ferramenta) {
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.delete(ferramenta);
        ses.getTransaction().commit();
        ses.close();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sucesso", "O item foi deletado com sucesso"));
    }
    
    public void excluirOperacaoFerramenta(OperacoesFerramentas op) {
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.delete(op);
        ses.getTransaction().commit();
        ses.close();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sucesso", "O item foi deletado com sucesso"));
    }

    //Buscar Ferramenta
    public String getFerramenta(int id) {
        String ferramenta;
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        Query qu = ses.createSQLQuery("select ferramenta from ferramentas where id = :id")
                .setParameter("id", id);
        qu.setMaxResults(1);
        ferramenta = (String) qu.uniqueResult();
        ses.getTransaction().commit();
        ses.close();

        if (checkSituacao(id).equals("EM USO") && novaOperacaoFerramentas.getRetirada()) {            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Este item ja esta em uso ou em estoque!"));
            return "EM USO";
        }
        else if (checkSituacao(id).equals("EM ESTOQUE") && !novaOperacaoFerramentas.getRetirada()) {
            return "EM ESTOQUE";
        }
        else {
            return ferramenta;
        }
        
    }

    public String getFerramentaEstoque(int id) {
        String ferramenta;
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        Query qu = ses.createSQLQuery("select ferramenta from ferramentas where id = :id")
                .setParameter("id", id);
        qu.setMaxResults(1);
        ferramenta = (String) qu.uniqueResult();
        ses.getTransaction().commit();
        ses.close();

        if (checkSituacao(id).equals("EM ESTOQUE")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Este item ja esta em estoque!"));
            return "EM ESTOQUE";
        } else {
            return ferramenta;
        }

    }

    public String checkSituacao(int id) {
        String situacao;
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        Query qu = ses.createSQLQuery("select situacao from ferramentas where id = :id")
                .setParameter("id", id);
        qu.setMaxResults(1);
        situacao = (String) qu.uniqueResult();
        ses.getTransaction().commit();
        ses.close();
        return situacao;

    }

    //Listener do editor
    public void onRowEdit(RowEditEvent event) throws ValidatorException {
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.update((Ferramentas) event.getObject());
        ses.getTransaction().commit();
        ses.close();
        FacesContext context = FacesContext.getCurrentInstance();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "O item foi modoficado com sucesso"));
    }

    //Listener do calendario
    public void onDateSelect(SelectEvent event) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dataString = format.format(event.getObject());
        this.data = format.parse(dataString);
        novaOperacaoFerramentas.setData(this.data);

    }

    //Listener do checkBox 
    public void listenerRetirada() {
        System.out.println("Retidada: " + this.novaOperacaoFerramentas.getRetirada());
    }

    //Construtor
    public FerramentasBean() {
        novaOperacaoFerramentas = new OperacoesFerramentas();
    }

    //Getters e Setters...
    public Ferramentas getNovaFerramenta() {
        return novaFerramenta;
    }

    public void setNovaFerramenta(Ferramentas novaFerramenta) {
        this.novaFerramenta = novaFerramenta;
    }

    public OperacoesFerramentas getNovaOperacaoFerramentas() {
        return novaOperacaoFerramentas;
    }

    public void setNovaOperacaoFerramentas(OperacoesFerramentas novaOperacaoFerramentas) {
        this.novaOperacaoFerramentas = novaOperacaoFerramentas;
    }

    public int getCodigoFerramenta() {
        return codigoFerramenta;
    }

    public void setCodigoFerramenta(int codigoFerramenta) {
        this.codigoFerramenta = codigoFerramenta;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

}
