/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import java.io.IOException;
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
import org.primefaces.component.export.PDFOptions;
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
    
    private PDFOptions pdfOpt;
    
    
    @PostConstruct
    public void init() {
        gerarListaAlmoxarife();
        customizationOptions();
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
    
    public void preProcessPDF(Object document) throws IOException, DocumentException {
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
    
    
    public OperacoesAlmoxarifeBean() {
        pdfOpt = new PDFOptions();
        customizationOptions();
    }

    public PDFOptions getPdfOpt() {
        return pdfOpt;
    }

    public void setPdfOpt(PDFOptions pdfOpt) {
        this.pdfOpt = pdfOpt;
    }
    
    
    
}
