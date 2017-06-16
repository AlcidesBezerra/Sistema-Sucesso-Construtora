/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.inject.Named;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import pojos.OperacoesAlmoxarife;
import pojos.Produtos;
import util.HibernateUtil;

/**
 *
 * @author alcid
 */
@ManagedBean
@Named(value = "produtosBean")
public class ProdutosBean {

    private HibernateUtil hu;
    private Session ses;
    private List<Produtos> list;
    private Produtos produtoSelecionado;
    public static OperacoesAlmoxarife novaOperacaoAlmoxarife = new OperacoesAlmoxarife();
    private Produtos novoProduto = new Produtos();

    private int codigoEntrada = 0;
    private double entrada = 0;
    private double saida = 0;
    private Date data = new Date();

    private boolean alerta;
    private double quantidadeAlerta;

    @PostConstruct
    public void init() {
        gerarListaDeProdutos();
        atualizarLista();
    }

    public void darEntrada() {

        novaOperacaoAlmoxarife.setProduto(getProduto(codigoEntrada));
        novaOperacaoAlmoxarife.setQuantidadeAnterior(getLastQuantidade(codigoEntrada));
        novaOperacaoAlmoxarife.setQuantidadeAtual(novaOperacaoAlmoxarife.getQuantidadeAnterior() + novaOperacaoAlmoxarife.getEntrada() - novaOperacaoAlmoxarife.getSaida());
        try {
            ses = hu.getSessionFactory().openSession();
            ses.beginTransaction();
            ses.save(novaOperacaoAlmoxarife);
            ses.getTransaction().commit();
            ses.close();

            try {
                ses = hu.getSessionFactory().openSession();
                ses.getTransaction().begin();
                Query query = ses.createSQLQuery("UPDATE produtos set quantidade_produto = :quantidade where Id = :id");
                query.setParameter("quantidade", novaOperacaoAlmoxarife.getQuantidadeAtual());
                query.setParameter("id", codigoEntrada);
                int result = query.executeUpdate();
                ses.getTransaction().commit();
                ses.close();
            } catch (HibernateException erro) {
                System.out.println(erro);
                ses.getTransaction().rollback();
                ses.close();
            }
            atualizarLista();
            FacesContext context = FacesContext.getCurrentInstance();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Operacao Inserida"));
        } catch (HibernateException e) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro!", "Cheque sua conexão ou contacte o administrador."));
        }
//salvarOperacao(novaOperacaoAlmoxarife);

    }

    public void salvarOperacao(OperacoesAlmoxarife op) {

    }

    public String getProduto(int codigo) {
        String produto;
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        Query qu = ses.createSQLQuery("select nome_produto from produtos where id = :id")
                .setParameter("id", codigo);
        qu.setMaxResults(1);
        produto = (String) qu.uniqueResult();
        ses.getTransaction().commit();
        ses.close();
        return produto;

    }

    public void onDateSelect(SelectEvent event) throws ParseException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dataString = format.format(event.getObject());
        this.data = format.parse(dataString);
        novaOperacaoAlmoxarife.setData(this.data);

    }

    public List<Produtos> atualizarLista() {
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        Query qu = ses.createQuery("from " + Produtos.class.getName());
        list = qu.list();
        ses.getTransaction().commit();
        ses.close();
        return list;
    }

    public List<Produtos> gerarListaDeProdutos() {
        if (list == null) {
            list = new ArrayList<>();
            ses = hu.getSessionFactory().openSession();
            ses.beginTransaction();
            Query qu = ses.createQuery("from " + Produtos.class.getName());
            list = qu.list();
            ses.getTransaction().commit();
            ses.close();
        }
        return list;
    }
    
    public List<Produtos> gerarListaAlerta() {
        List<Produtos> lista;
            lista = new ArrayList<>();
            ses = hu.getSessionFactory().openSession();
            ses.beginTransaction();
            Query qu = ses.createQuery("from Produtos WHERE alerta = true AND quantidade_produto <= quantidade_alerta");
            lista = qu.list();
            ses.getTransaction().commit();
            ses.close();
        
        return lista;
    }

    public void excluirProduto(Produtos produto) {
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.delete(produto);
        ses.getTransaction().commit();
        ses.close();
        atualizarLista();
        FacesContext context = FacesContext.getCurrentInstance();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "O item foi deletado com sucesso"));
    }

    public void selecionarProduto(Produtos produto) {
        produtoSelecionado = produto;
    }

    public void onRowEdit(RowEditEvent event) throws ValidatorException {
        Produtos novoProduto = (Produtos) event.getObject();

        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.update((Produtos) event.getObject());
        ses.getTransaction().commit();
        ses.close();

    }

    public void inserirNovoProduto(Produtos produto) {
        try {
            ses = hu.getSessionFactory().openSession();
            ses.beginTransaction();
            ses.save(produto);
            ses.getTransaction().commit();
            ses.close();
            atualizarLista();
            FacesContext context = FacesContext.getCurrentInstance();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "O produto foi cadastrado com sucesso."));
        } catch (HibernateException e) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "Cheque sua conexão ou contacte o administrador."));
        }

    }

    public double getLastQuantidade(int id) {
        double horimetro;
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        Query qu = ses.createSQLQuery("select quantidade_produto from produtos where id = :id")
                .setParameter("id", id);
        qu.setMaxResults(1);
        horimetro = (double) qu.uniqueResult();
        ses.getTransaction().commit();
        ses.close();
        return horimetro;

    }

    public void checkBoxAlertaListener(Produtos produto) {
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.update(produto);
        ses.getTransaction().commit();
        ses.close();
        if (produto.getAlerta()) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "O item esta marcado para alerta."));
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sucesso", "O item esta desmarcado para alerta."));
        }
    }

    public void onValueChanged(Produtos produto) {
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.update(produto);
        ses.getTransaction().commit();
        ses.close();
        FacesContext context = FacesContext.getCurrentInstance();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "O numero para alerta foi definido"));
    }

    /**
     * Creates a new instance of ProdutosBean
     */
    public ProdutosBean() {
    }

    public List<Produtos> getList() {
        return list;
    }

    public void setList(List<Produtos> list) {
        this.list = list;
    }

    public Produtos getProdutoSelecionado() {
        return produtoSelecionado;
    }

    public void setProdutoSelecionado(Produtos produtoSelecionado) {
        this.produtoSelecionado = produtoSelecionado;
    }

    public Produtos getNovoProduto() {
        return novoProduto;
    }

    public void setNovoProduto(Produtos novoProduto) {
        this.novoProduto = novoProduto;
    }

    public OperacoesAlmoxarife getNovaOperacaoAlmoxarife() {
        return novaOperacaoAlmoxarife;
    }

    public void setNovaOperacaoAlmoxarife(OperacoesAlmoxarife novaOperacaoAlmoxarife) {
        this.novaOperacaoAlmoxarife = novaOperacaoAlmoxarife;
    }

    public int getCodigoEntrada() {
        return codigoEntrada;
    }

    public void setCodigoEntrada(int codigoEntrada) {
        this.codigoEntrada = codigoEntrada;
    }

    public boolean isAlerta() {
        return alerta;
    }

    public void setAlerta(boolean alerta) {
        this.alerta = alerta;
    }

    public double getQuantidadeAlerta() {
        return quantidadeAlerta;
    }

    public void setQuantidadeAlerta(double quantidadeAlerta) {
        this.quantidadeAlerta = quantidadeAlerta;
    }

}
