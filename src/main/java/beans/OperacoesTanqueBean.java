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
import pojos.OperacoesLimoeiro;
import pojos.OperacoesTanque;
/**
 *
 * @author alcid
 */
@Named(value = "operacoesTanqueBean")
@SessionScoped
public class OperacoesTanqueBean implements Serializable {
    
    private HibernateUtil hu;
    private Session ses;
    private List<OperacoesTanque> list;
    private OperacoesTanque operacaoSelecionada = new OperacoesTanque();
    private OperacoesTanque novaOperacaoTanque = new OperacoesTanque();
    public static boolean iskm;
    public static OperacoesTanque ultimaOperacao = new OperacoesTanque();
    private PDFOptions pdfOpt;
    public static Date data;
    
    
    
    
    public OperacoesTanque getLastOperacao() {
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        Query qu = ses.createSQLQuery("select * from operacoes_tanque order by data desc");
        qu.setMaxResults(1);
        list = qu.list();
        List<OperacoesTanque> result = new ArrayList<OperacoesTanque>();

        for (Object rows : qu.list()) {
            Object[] row = (Object[]) rows;
            ultimaOperacao.setId((int) row[0]);
            ultimaOperacao.setData((Date) row[1]); 
            ultimaOperacao.setEntrada((Double) row[2]);
            ultimaOperacao.setSaida((Double) row[3]);
            ultimaOperacao.setQuantidadeAnterior((Double) row[4]);
            ultimaOperacao.setQuantidadeAtual((Double) row[5]);
            ultimaOperacao.setVeiculo((String) row[6]);
            ultimaOperacao.setValor((Double) row[7]);

        }
        ses.getTransaction().commit();
        ses.close();

        return ultimaOperacao;
    }
    
    public double getLastValor() {
        double horimetro;
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        Query qu = ses.createSQLQuery("select quantidade_atual from operacoes_tanque order by data desc");
        qu.setMaxResults(1);
        horimetro = (double) qu.uniqueResult();
        ses.getTransaction().commit();
        ses.close();
        return horimetro;

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
            Logger.getLogger(OperacoesEscritorioBean.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(format.format(this.data));
    }
    
    @PostConstruct
    public void init() {
        customizationOptions();
    }
    
    

    
    public List<OperacoesTanque> gerarListaOperacoes() {
        list = new ArrayList<>();
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        Query qu = ses.createQuery("from OperacoesTanque");
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
        getLastOperacao();


        //Obtendo os valores do formulario
        novaOperacaoTanque.setVeiculo("N/D");
        novaOperacaoTanque.setData(data);
        novaOperacaoTanque.setEntrada(Double.parseDouble(request.getParameter("formNovaOperacao:txtEntrada")));
        novaOperacaoTanque.setQuantidadeAnterior(getLastValor());
        novaOperacaoTanque.setQuantidadeAtual(calcularQuantidade());
        novaOperacaoTanque.setValor(Double.parseDouble(request.getParameter("formNovaOperacao:txtValorCombustivel")));
        
        
        //Inserindo a nova operacao no banco de dados
        inserir(novaOperacaoTanque);

    }
    
    public double calcularQuantidade(){
        double resultado;
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        resultado = novaOperacaoTanque.getQuantidadeAnterior() + Double.parseDouble(request.getParameter("formNovaOperacao:txtEntrada"));
        return resultado;
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

    private void inserir(OperacoesTanque op) {
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.save(op);
        ses.getTransaction().commit();
        ses.close();
    }
    
    


    public void excluirOperacao(OperacoesTanque operacao) {
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.delete(operacao);
        ses.getTransaction().commit();
        ses.close();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "A Operacao foi deletada. Clique em RELATORIO novamente."));
    }
    
    
    
    
    public OperacoesTanqueBean() {
    }

    public HibernateUtil getHu() {
        return hu;
    }

    public void setHu(HibernateUtil hu) {
        this.hu = hu;
    }

    public Session getSes() {
        return ses;
    }

    public void setSes(Session ses) {
        this.ses = ses;
    }

    public List<OperacoesTanque> getList() {
        return list;
    }

    public void setList(List<OperacoesTanque> list) {
        this.list = list;
    }

    public OperacoesTanque getOperacaoSelecionada() {
        return operacaoSelecionada;
    }

    public void setOperacaoSelecionada(OperacoesTanque operacaoSelecionada) {
        this.operacaoSelecionada = operacaoSelecionada;
    }

    public OperacoesTanque getNovaOperacaoTanque() {
        return novaOperacaoTanque;
    }

    public void setNovaOperacaoTanque(OperacoesTanque novaOperacaoTanque) {
        this.novaOperacaoTanque = novaOperacaoTanque;
    }

    public static boolean isIskm() {
        return iskm;
    }

    public static void setIskm(boolean iskm) {
        OperacoesTanqueBean.iskm = iskm;
    }

    public static OperacoesTanque getUltimaOperacao() {
        return ultimaOperacao;
    }

    public static void setUltimaOperacao(OperacoesTanque ultimaOperacao) {
        OperacoesTanqueBean.ultimaOperacao = ultimaOperacao;
    }

    public PDFOptions getPdfOpt() {
        return pdfOpt;
    }

    public void setPdfOpt(PDFOptions pdfOpt) {
        this.pdfOpt = pdfOpt;
    }

    public static Date getData() {
        return data;
    }

    public static void setData(Date data) {
        OperacoesTanqueBean.data = data;
    }

    
    
}
