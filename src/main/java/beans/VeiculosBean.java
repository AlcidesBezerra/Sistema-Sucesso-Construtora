/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import pojos.OperacoesEscritorio;
import pojos.Veiculos;
import util.HibernateUtil;

/**
 *
 * @author Alcid
 */
@Named(value = "veiculosBean")
@SessionScoped
public class VeiculosBean implements Serializable {

    private HibernateUtil hu;
    private Session ses;
    private List<Veiculos> list;
    private Veiculos veiculoSelecionado = new Veiculos();

    private Veiculos novoVeiculo = new Veiculos();

    public VeiculosBean() {
    }

    public List<Veiculos> gerarListaVeiculos() {
        list = new ArrayList<>();
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        Query qu = ses.createQuery("from " + Veiculos.class.getName());
        list = qu.list();
        ses.getTransaction().commit();
        ses.close();
        return list;
    }

    public void cadastrarVeiculo() {
        System.out.println("BESTEIRA");
    }

    public void inserirNovoVeiculo(Veiculos veiculo) {
        try {
            ses = hu.getSessionFactory().openSession();
            ses.beginTransaction();
            ses.save(veiculo);
            ses.getTransaction().commit();
            System.out.println(novoVeiculo.toString());
            ses.close();
            criarOperacaoInicial(veiculo);
            FacesContext context = FacesContext.getCurrentInstance();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "O veiculo foi cadastrado."));
        } catch (HibernateException e) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro!", "Cheque sua conexão ou contacte o administrador."));
        }

    }

    public void criarOperacaoInicial(Veiculos veiculo) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        OperacoesEscritorio novaOperacao = new OperacoesEscritorio();
        novaOperacao.setCombustivel(0);
        novaOperacao.setConsumo(0);
        novaOperacao.setData(date);
        novaOperacao.setHorimetroAnterior(0);
        novaOperacao.setHorimetroAtual(0);
        novaOperacao.setIsPendente(0);
        novaOperacao.setHorimetroPercorrido(0);
        novaOperacao.setNota("Sem nota");
        novaOperacao.setValorCombustivel(0);
        novaOperacao.setValorPendente(0);
        novaOperacao.setVeiculo(veiculo.getPlaca());
        inserirNovaOperacao(novaOperacao);
  
        
    }
    public void inserirNovaOperacao(OperacoesEscritorio novaOperacao){
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.save(novaOperacao);
        ses.getTransaction().commit();
        System.out.println(novaOperacao.toString());
        ses.close();
    }

    public void excluirVeiculo(Veiculos veiculo) {
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.delete(veiculo);
        ses.getTransaction().commit();
        System.out.println(novoVeiculo.toString());
        ses.close();
        FacesContext context = FacesContext.getCurrentInstance();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "O veiculo foi deletado, atualize a pagina."));
    }
    

    public void select(Veiculos p) {
        veiculoSelecionado = p;
        System.out.println(this.veiculoSelecionado.toString());
    }

    public Veiculos getVeiculoSelecionado() {
        return veiculoSelecionado;
    }

    public void setVeiculoSelecionado(Veiculos veiculoSelecionado) {
        this.veiculoSelecionado = veiculoSelecionado;
    }

    public Veiculos getNovoVeiculo() {
        return novoVeiculo;
    }

    public void setNovoVeiculo(Veiculos novoVeiculo) {
        this.novoVeiculo = novoVeiculo;
    }

    public List<Veiculos> getList() {
        return list;
    }

    public void setList(List<Veiculos> list) {
        this.list = list;
    }

}
