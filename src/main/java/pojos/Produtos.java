package pojos;
// Generated 09/06/2017 22:31:26 by Hibernate Tools 4.3.1



/**
 * Produtos generated by hbm2java
 */
public class Produtos  implements java.io.Serializable {


     private Integer id;
     private String nomeProduto;
     private double quantidadeProduto;

    public Produtos() {
    }

    public Produtos(String nomeProduto, double quantidadeProduto) {
       this.nomeProduto = nomeProduto;
       this.quantidadeProduto = quantidadeProduto;
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

    @Override
    public String toString() {
        return "Produtos{" + "id=" + id + ", nomeProduto=" + nomeProduto + ", quantidadeProduto=" + quantidadeProduto + '}';
    }
    
    




}


