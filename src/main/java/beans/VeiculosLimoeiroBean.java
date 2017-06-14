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
import pojos.OperacoesLimoeiro;
import pojos.Veiculos;
import pojos.VeiculosLimoeiro;
import util.HibernateUtil;

/**
 *
 * @author Alcid
 */
@Named(value = "veiculosLimoeiroBean")
@SessionScoped
public class VeiculosLimoeiroBean implements Serializable {

    private HibernateUtil hu;
    private Session ses;
    private List<VeiculosLimoeiro> list;
    private VeiculosLimoeiro veiculoSelecionado = new VeiculosLimoeiro();
    
    private VeiculosLimoeiro novoVeiculo = new VeiculosLimoeiro();

    
    
            
    public VeiculosLimoeiroBean() {
    }
    
    public List<VeiculosLimoeiro> gerarListaVeiculos(){
        list = new ArrayList<>();
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        Query qu = ses.createQuery("from VeiculosLimoeiro");
        list = qu.list();
        ses.getTransaction().commit();
        ses.close();
        return list;
    }
    
    
    public void inserirNovoVeiculo(VeiculosLimoeiro veiculo){
        try{
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.save(veiculo);
        ses.getTransaction().commit();
        System.out.println(novoVeiculo.toString());
        ses.close();
        criarOperacaoInicial(veiculo);
            FacesContext context = FacesContext.getCurrentInstance();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "O veiculo foi cadastrado."));
        }catch(HibernateException e){
            FacesContext context = FacesContext.getCurrentInstance();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro!", "Cheque sua conexão ou contacte o administrador."));
        }
        
    }
    
    public void criarOperacaoInicial(VeiculosLimoeiro veiculo) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        OperacoesLimoeiro novaOperacao = new OperacoesLimoeiro();
        novaOperacao.setCombustivel(0);
        novaOperacao.setConsumo(0);
        novaOperacao.setData(date);
        novaOperacao.setHorimetroAnterior(0);
        novaOperacao.setHorimetroAtual(0);
        novaOperacao.setIsPendente(0);
        novaOperacao.setHorimetroPercorrido(0);
        novaOperacao.setNota("Sem nota");
        novaOperacao.setValorPendente(0);
        novaOperacao.setVeiculo(veiculo.getPlaca());
        inserirNovaOperacao(novaOperacao);
  
        
    }
    
    public void inserirNovaOperacao(OperacoesLimoeiro novaOperacao){
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.save(novaOperacao);
        ses.getTransaction().commit();
        System.out.println(novaOperacao.toString());
        ses.close();
        
    }
    
    public void excluirVeiculo(VeiculosLimoeiro veiculo){
    ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.delete(veiculo);
        ses.getTransaction().commit();
        System.out.println(novoVeiculo.toString());
        ses.close();
        FacesContext context = FacesContext.getCurrentInstance();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "O veiculo foi deletado, atualize a pagina."));
    }
        
    
    public void select(VeiculosLimoeiro p){
        veiculoSelecionado = p;
        System.out.println(this.veiculoSelecionado.toString());
    }

    public VeiculosLimoeiro getVeiculoSelecionado() {
        return veiculoSelecionado;
    }

    public void setVeiculoSelecionado(VeiculosLimoeiro veiculoSelecionado) {
        this.veiculoSelecionado = veiculoSelecionado;
    }

    public VeiculosLimoeiro getNovoVeiculo() {
        return novoVeiculo;
    }

    public void setNovoVeiculo(VeiculosLimoeiro novoVeiculo) {
        this.novoVeiculo = novoVeiculo;
    }

    public List<VeiculosLimoeiro> getList() {
        return list;
    }

    public void setList(List<VeiculosLimoeiro> list) {
        this.list = list;
    }
    
    
    
    

    
    
    
}
    

