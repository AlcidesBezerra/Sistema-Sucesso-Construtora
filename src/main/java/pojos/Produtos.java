package pojos;
// Generated 15/06/2017 12:56:05 by Hibernate Tools 4.3.1



/**
 * Produtos generated by hbm2java
 */
public class Produtos  implements java.io.Serializable {


     private Integer id;
     private String nomeProduto;
     private double quantidadeProduto;
     private boolean alerta;
     private double quantidadeAlerta;

    public Produtos() {
    }

    public Produtos(String nomeProduto, double quantidadeProduto, boolean alerta, double quantidadeAlerta) {
       this.nomeProduto = nomeProduto;
       this.quantidadeProduto = quantidadeProduto;
       this.alerta = alerta;
       this.quantidadeAlerta = quantidadeAlerta;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNomeProduto() {
        return this.nomeProduto;
    }
    
    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }
    public double getQuantidadeProduto() {
        return this.quantidadeProduto;
    }
    
    public void setQuantidadeProduto(double quantidadeProduto) {
        this.quantidadeProduto = quantidadeProduto;
    }
    public boolean getAlerta() {
        return this.alerta;
    }
    
    public void setAlerta(boolean alerta) {
        this.alerta = alerta;
    }
    public double getQuantidadeAlerta() {
        return this.quantidadeAlerta;
    }
    
    public void setQuantidadeAlerta(double quantidadeAlerta) {
        this.quantidadeAlerta = quantidadeAlerta;
    }




}


