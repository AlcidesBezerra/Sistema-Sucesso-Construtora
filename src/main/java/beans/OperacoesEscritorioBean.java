/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

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
import pojos.OperacoesEscritorio;
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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.component.export.PDFOptions;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Alcid
 */
@ManagedBean
@Named(value = "operacoesEscritorioBean")
public class OperacoesEscritorioBean implements Serializable {

    private HibernateUtil hu;
    private Session ses;
    public static List<OperacoesEscritorio> list = null;
    public static String nu = "naoas";
    private OperacoesEscritorio operacaoSelecionada = new OperacoesEscritorio();
    private OperacoesEscritorio novaOperacaoEscritorio = new OperacoesEscritorio();

    public static boolean iskm;
    public static OperacoesEscritorio ultimaOperacao = new OperacoesEscritorio();

    public static Date data;

    @PostConstruct
    public void init() {
        customizationOptions();
        gerarListaOperacoes();
        atualizarLista();
    }

    private PDFOptions pdfOpt;

    public List<OperacoesEscritorio> gerarListaOperacoes() {
        if (list == null) {
            System.out.println("LISTA NULA, INICIALIZANDO LISTA");
            ses = hu.getSessionFactory().openSession();
            ses.beginTransaction();
            Query qu = ses.createQuery("from " + OperacoesEscritorio.class.getName());
            list = qu.list();
            ses.getTransaction().commit();
            ses.close();
        } else {

        }
        return list;
    }

    //Busca objeto na sessão atual e modifica com o Hibernate.
    public void onRowEdit(RowEditEvent event) {

        OperacoesEscritorio novaOperacao = (OperacoesEscritorio) event.getObject();
        System.out.println(novaOperacao.toString());

        System.out.println(novaOperacao.toString());
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.update((OperacoesEscritorio) event.getObject());
        ses.getTransaction().commit();
        ses.close();
    }

    public OperacoesEscritorio getLastOperacao(String veiculo) {
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        Query qu = ses.createSQLQuery("select * from operacoes_escritorio where veiculo = :veiculo order by data desc").setParameter("veiculo", veiculo);
        qu.setMaxResults(1);
        list = qu.list();
        List<OperacoesEscritorio> result = new ArrayList<OperacoesEscritorio>();

        for (Object rows : qu.list()) {
            Object[] row = (Object[]) rows;
            ultimaOperacao.setId((int) row[0]);
            ultimaOperacao.setData((Date) row[1]);
            ultimaOperacao.setVeiculo((String) row[2]);
            ultimaOperacao.setCombustivel((Double) row[3]);
            ultimaOperacao.setValorCombustivel((Double) row[5]);
            ultimaOperacao.setHorimetroAnterior((Double) row[6]);
            ultimaOperacao.setHorimetroAtual((Double) row[7]);
            ultimaOperacao.setHorimetroPercorrido((Double) row[8]);
            ultimaOperacao.setConsumo((Double) row[9]);
            ultimaOperacao.setNota((String) row[10]);
            ultimaOperacao.setValorPendente((Double) row[11]);
            ultimaOperacao.setIsPendente((int) row[12]);

        }

        ses.getTransaction().commit();
        ses.close();

        return ultimaOperacao;
    }

    public double getLastHorimetro(String veiculo) {
        double horimetro;
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        Query qu = ses.createSQLQuery("select horimetro_atual from operacoes_escritorio where veiculo = :veiculo order by data desc")
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
        Query qu = ses.createSQLQuery("select valor_pendente from operacoes_escritorio where veiculo = :veiculo order by data desc")
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
            Logger.getLogger(OperacoesEscritorioBean.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(format.format(this.data));
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
            novaOperacaoEscritorio.setValorPendente(Double.parseDouble(request.getParameter("formNovaOperacao:txtCombustivel")) + lastPendingValue);
            novaOperacaoEscritorio.setIsPendente(1);
        }

        //Obtendo os valores do formulario
        novaOperacaoEscritorio.setData(data);
        if (Double.parseDouble(request.getParameter("formNovaOperacao:txtHorimetro")) == 0) {
            novaOperacaoEscritorio.setCombustivel(Double.parseDouble(request.getParameter("formNovaOperacao:txtCombustivel")));
        } else {
            novaOperacaoEscritorio.setCombustivel(Double.parseDouble(request.getParameter("formNovaOperacao:txtCombustivel")) + lastPendingValue);
        }
        novaOperacaoEscritorio.setVeiculo(request.getParameter("formNovaOperacao:txtVeiculo"));
        novaOperacaoEscritorio.setHorimetroAnterior(getLastHorimetro(novaOperacaoEscritorio.getVeiculo()));
        novaOperacaoEscritorio.setHorimetroAtual(Double.parseDouble(request.getParameter("formNovaOperacao:txtHorimetro")));
        novaOperacaoEscritorio.setHorimetroPercorrido(calcularHorimetroPercorrido());
        novaOperacaoEscritorio.setConsumo(calcularConsumo(iskm));
        novaOperacaoEscritorio.setNota(request.getParameter("formNovaOperacao:txtNota"));
        novaOperacaoEscritorio.setValorCombustivel(Double.parseDouble(request.getParameter("formNovaOperacao:txtValorCombustivel")));

        //Inserindo a nova operacao no banco de dados
        inserir(novaOperacaoEscritorio);
        System.out.println(novaOperacaoEscritorio.toString());
        atualizarLista();

    }

    private void inserir(OperacoesEscritorio op) {
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.save(op);
        ses.getTransaction().commit();
        ses.close();
        atualizarLista();
    }

    public void uncheckPending(OperacoesEscritorio op) {
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.update(op);
        ses.getTransaction().commit();
        ses.close();
        
    }

    public double calcularHorimetroPercorrido() {
        double horimetroPercorrido = 0;
        horimetroPercorrido = novaOperacaoEscritorio.getHorimetroAtual() - novaOperacaoEscritorio.getHorimetroAnterior();
        return horimetroPercorrido;
    }

    public double calcularConsumo(boolean iskm) {
        double consumo = 0;
        if (iskm) {
            consumo = novaOperacaoEscritorio.getHorimetroPercorrido() / novaOperacaoEscritorio.getCombustivel();
        } else {
            consumo = novaOperacaoEscritorio.getCombustivel() / novaOperacaoEscritorio.getHorimetroPercorrido();
        }
        return consumo;
    }

    public void excluirOperacao(OperacoesEscritorio operacao) {
        ses = hu.getSessionFactory().openSession();
        ses.beginTransaction();
        ses.delete(operacao);
        ses.getTransaction().commit();
        ses.close();
        atualizarLista();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "A Operacao foi deletada. Clique em RELATORIO novamente."));
        
    }
    
    
    
    public List<OperacoesEscritorio> atualizarLista() {
        System.out.println("MÉTODO ATUALIZACAO ESCRITORIO");
            ses = hu.getSessionFactory().openSession();
            ses.beginTransaction();
            Query qu = ses.createQuery("from " + OperacoesEscritorio.class.getName());
            list = qu.list();
            ses.getTransaction().commit();
            ses.close();
       
        return list;
    }

    public void select(OperacoesEscritorio p) {
        operacaoSelecionada = p;
        System.out.println(this.operacaoSelecionada.toString());

    }

    //Getters e Setters
    public OperacoesEscritorio getOperacaoSelecionada() {
        return operacaoSelecionada;
    }

    public void setOperacaoSelecionada(OperacoesEscritorio operacaoSelecionada) {
        this.operacaoSelecionada = operacaoSelecionada;
    }

    public OperacoesEscritorio getNovaOperacaoEscritorio() {
        return novaOperacaoEscritorio;
    }

    public void setNovaOperacaoEscritorio(OperacoesEscritorio novaOperacaoEscritorio) {
        this.novaOperacaoEscritorio = novaOperacaoEscritorio;
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
        OperacoesEscritorioBean.iskm = iskm;
    }

    public List<OperacoesEscritorio> getList() {
        return list;
    }

    public void setList(List<OperacoesEscritorio> list) {
        this.list = list;
    }

    
    
    
    

    /**
     * Creates a new instance of OperacoesEscritorioBean
     */
    public OperacoesEscritorioBean() {
        pdfOpt = new PDFOptions();
        customizationOptions();
    }

}
