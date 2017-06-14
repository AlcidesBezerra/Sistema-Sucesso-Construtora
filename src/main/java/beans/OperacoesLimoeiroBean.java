/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.Query;
import org.hibernate.Session;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.event.SelectEvent;
import util.HibernateUtil;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.component.export.PDFOptions;
import org.primefaces.event.RowEditEvent;
import pojos.OperacoesEscritorio;
import pojos.OperacoesLimoeiro;
import pojos.OperacoesTanque;
/**
 *
 * @author alcid
 */
@ManagedBean
@Named(value = "operacoesLimoeiroBean")
public class OperacoesLimoeiroBean implements Serializable {
    
    private HibernateUtil hu;
    private Session ses;
    public static List<OperacoesLimoeiro> list = null;
    private OperacoesLimoeiro operacaoSelecionada = new OperacoesLimoeiro();
    private OperacoesLimoeiro novaOperacaoLimoeiro = new OperacoesLimoeiro();
    public static boolean iskm;
    public static OperacoesLimoeiro ultimaOperacao = new OperacoesLimoeiro();
    private PDFOptions pdfOpt;
    public static Date data;
    
    public static String nova = "string";
    
    @PostConstruct
    public void init() {
        customizationOptions();
        gerarListaOperacoes();
        System.out.println("INIT");
        atualizarListaOperacoes();
    }
    
    
    
    
    public OperacoesLimoeiro getLastOperacao(String veiculo) {
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        Query qu = ses.createSQLQuery("select * from operacoes_limoeiro where veiculo = :veiculo order by data desc").setParameter("veiculo", veiculo);
        qu.setMaxResults(1);
        list = qu.list();
        List<OperacoesLimoeiro> result = new ArrayList<OperacoesLimoeiro>();

        for (Object rows : qu.list()) {
            Object[] row = (Object[]) rows;
            ultimaOperacao.setId((int) row[0]);
            ultimaOperacao.setVeiculo((String) row[1]);
            ultimaOperacao.setData((Date) row[2]);         
            ultimaOperacao.setCombustivel((Double) row[3]);
            ultimaOperacao.setHorimetroAnterior((Double) row[4]);
            ultimaOperacao.setHorimetroAtual((Double) row[5]);
            ultimaOperacao.setHorimetroPercorrido((Double) row[6]);
            ultimaOperacao.setConsumo((Double) row[7]);
            ultimaOperacao.setNota((String) row[8]);
            ultimaOperacao.setValorPendente((Double) row[9]);
            ultimaOperacao.setIsPendente((int) row[10]);

        }
        ses.getTransaction().commit();
        ses.close();

        return ultimaOperacao;
    }
    
    //Busca objeto na sessão atual e modifica com o Hibernate.
    public void onRowEdit(RowEditEvent event) {

        OperacoesLimoeiro novaOperacao = (OperacoesLimoeiro) event.getObject();
        System.out.println(novaOperacao.toString());

        System.out.println(novaOperacao.toString());
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.update((OperacoesLimoeiro) event.getObject());
        ses.getTransaction().commit();
        ses.close();
    }
    
    
    public double getLastHorimetro(String veiculo) {
        double horimetro;
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        Query qu = ses.createSQLQuery("select horimetro_atual from operacoes_limoeiro where veiculo = :veiculo order by data desc")
                .setParameter("veiculo", veiculo);
        qu.setMaxResults(1);
        horimetro = (double) qu.uniqueResult();
        ses.getTransaction().commit();
        ses.close();
        return horimetro;

    }
    
    public double getLastPendindValue(String veiculo) {
        double valor;
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        Query qu = ses.createSQLQuery("select valor_pendente from operacoes_limoeiro where veiculo = :veiculo order by data desc")
                .setParameter("veiculo", veiculo);
        qu.setMaxResults(1);
        valor = (double) qu.uniqueResult();
        ses.getTransaction().commit();
        ses.close();
        return valor;

    }
    
    public void checkboxChanged(AjaxBehaviorEvent event) {

        iskm = ((SelectBooleanCheckbox) event.getSource()).isSelected();

    }
    
    public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
        Document pdf = (Document) document;

        pdf.setPageSize(PageSize.A4);
        pdf.setMargins(-50, -50, 25, 25);
        pdf.open();

    }
    
    public void customizationOptions() {

        pdfOpt = new PDFOptions();
        pdfOpt.setFacetFontColor("#0000ff");
        pdfOpt.setFacetFontStyle("BOLD");
        pdfOpt.setFacetFontSize("11");
        pdfOpt.setCellFontSize("12");
    }
    
    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String data = format.format(event.getObject());
        try {
            this.data = format.parse(data);

        } catch (ParseException ex) {
            Logger.getLogger(OperacoesLimoeiroBean.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(format.format(this.data));
    }
    

    
    

    
    public List<OperacoesLimoeiro> gerarListaOperacoes() {
        if(list == null){
        list = new ArrayList<>();
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        Query qu = ses.createQuery("from OperacoesLimoeiro");
        list = qu.list();
        ses.getTransaction().commit();
        ses.close();
        }
        return list;
    }
    
    public  List<OperacoesLimoeiro> atualizarListaOperacoes() {
        System.out.println("Método de atualização");
        list = new ArrayList<>();
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        Query qu = ses.createQuery("from OperacoesLimoeiro");
        list = qu.list();
        ses.getTransaction().commit();
        ses.close();
        
        return list;
    }
    
    public void testarNovaOperacao() {
        
        

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        //Variaveis temporarias
        double lastPendingValue = 0;
        double pendingValue = 0;

        //Obtendo a ultima operacao
        getLastOperacao(request.getParameter("formNovaOperacao:txtVeiculo"));

        if (ultimaOperacao.getIsPendente() == 1 && ultimaOperacao.getValorPendente() != 0) {
            lastPendingValue = ultimaOperacao.getValorPendente();
            ultimaOperacao.setIsPendente(0);
            uncheckPending(ultimaOperacao);
        }

        //Verificando se haverá valor pendente
        if (Double.parseDouble(request.getParameter("formNovaOperacao:txtHorimetro")) == 0) {
            novaOperacaoLimoeiro.setValorPendente(Double.parseDouble(request.getParameter("formNovaOperacao:txtCombustivel")) + lastPendingValue);
            novaOperacaoLimoeiro.setIsPendente(1);
        }

        //Obtendo os valores do formulario
        novaOperacaoLimoeiro.setData(data);
        if (Double.parseDouble(request.getParameter("formNovaOperacao:txtHorimetro")) == 0) {
            novaOperacaoLimoeiro.setCombustivel(Double.parseDouble(request.getParameter("formNovaOperacao:txtCombustivel")));
        } else {
            novaOperacaoLimoeiro.setCombustivel(Double.parseDouble(request.getParameter("formNovaOperacao:txtCombustivel")) + lastPendingValue);
        }
        novaOperacaoLimoeiro.setVeiculo(request.getParameter("formNovaOperacao:txtVeiculo"));
        novaOperacaoLimoeiro.setHorimetroAnterior(getLastHorimetro(novaOperacaoLimoeiro.getVeiculo()));
        novaOperacaoLimoeiro.setHorimetroAtual(Double.parseDouble(request.getParameter("formNovaOperacao:txtHorimetro")));
        novaOperacaoLimoeiro.setHorimetroPercorrido(calcularHorimetroPercorrido());
        novaOperacaoLimoeiro.setConsumo(calcularConsumo(iskm));
        novaOperacaoLimoeiro.setNota(request.getParameter("formNovaOperacao:txtNota"));
        
        operacaoTanque();
        
        
        //Inserindo a nova operacao no banco de dados
        inserir(novaOperacaoLimoeiro);
        atualizarListaOperacoes();

    }
    
    public double getLastTanque() {
        double valor;
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        Query qu = ses.createSQLQuery("select quantidade_atual from operacoes_tanque order by data desc");
        qu.setMaxResults(1);
        valor = (double) qu.uniqueResult();
        ses.getTransaction().commit();
        ses.close();
        return valor;

    }

    private void inserir(OperacoesLimoeiro op) {
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.save(op);
        ses.getTransaction().commit();
        ses.close();
        atualizarListaOperacoes();
    }
    
    private void operacaoTanque(){
        OperacoesTanque novaOperacaoTanque = new OperacoesTanque();
        novaOperacaoTanque.setEntrada(0);
        novaOperacaoTanque.setSaida(novaOperacaoLimoeiro.getCombustivel());
        novaOperacaoTanque.setData(novaOperacaoLimoeiro.getData());
        novaOperacaoTanque.setQuantidadeAnterior(getLastTanque());
        novaOperacaoTanque.setQuantidadeAtual(novaOperacaoTanque.getQuantidadeAnterior() - novaOperacaoTanque.getSaida());
        novaOperacaoTanque.setVeiculo(novaOperacaoLimoeiro.getVeiculo());
        System.out.println(novaOperacaoTanque.toString());
        inserirTanque(novaOperacaoTanque);
    }
    
    private void inserirTanque(OperacoesTanque op) {
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.save(op);
        ses.getTransaction().commit();
        ses.close();
    }

    public void uncheckPending(OperacoesLimoeiro op) {
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.update(op);
        ses.getTransaction().commit();
        ses.close();
    }

    public double calcularHorimetroPercorrido() {
        double horimetroPercorrido = 0;
        horimetroPercorrido = novaOperacaoLimoeiro.getHorimetroAtual() - novaOperacaoLimoeiro.getHorimetroAnterior();
        return horimetroPercorrido;
    }

    public double calcularConsumo(boolean iskm) {
        double consumo = 0;
        if (iskm) {
            consumo = novaOperacaoLimoeiro.getHorimetroPercorrido() / novaOperacaoLimoeiro.getCombustivel();
        } else {
            consumo = novaOperacaoLimoeiro.getCombustivel() / novaOperacaoLimoeiro.getHorimetroPercorrido();
        }
        return consumo;
    }

    public void excluirOperacao(OperacoesLimoeiro operacao) {
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.delete(operacao);
        ses.getTransaction().commit();
        ses.close();
        atualizarListaOperacoes();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "A Operacao foi deletada. Clique em RELATORIO novamente."));
    }
    
    
    
    
    public OperacoesLimoeiroBean() {
    }

    public static Date getData() {
        return data;
    }

    public static void setData(Date data) {
        OperacoesLimoeiroBean.data = data;
    }
    
    public void select(OperacoesLimoeiro p) {
        operacaoSelecionada = p;
        System.out.println(this.operacaoSelecionada.toString());

    }

    //Getters e Setters
    public OperacoesLimoeiro getOperacaoSelecionada() {
        return operacaoSelecionada;
    }

    public void setOperacaoSelecionada(OperacoesLimoeiro operacaoSelecionada) {
        this.operacaoSelecionada = operacaoSelecionada;
    }

    public OperacoesLimoeiro getNovaOperacaoLimoeiro() {
        return novaOperacaoLimoeiro;
    }

    public void setNovaOperacaoEscritorio(OperacoesLimoeiro novaOperacaoLimoeiro) {
        this.novaOperacaoLimoeiro = novaOperacaoLimoeiro;
    }

    public PDFOptions getPdfOpt() {
        return pdfOpt;
    }

    public void setPdfOpt(PDFOptions pdfOpt) {
        this.pdfOpt = pdfOpt;
    }

    public static boolean isIskm() {
        return iskm;
    }

    public static void setIskm(boolean iskm) {
        OperacoesLimoeiroBean.iskm = iskm;
    }

    public List<OperacoesLimoeiro> getList() {
        return list;
    }

    public void setList(List<OperacoesLimoeiro> list) {
        this.list = list;
    }


    
    

    
    
    
    
}
