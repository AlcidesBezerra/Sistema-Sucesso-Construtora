package pojos;
// Generated 15/06/2017 12:56:05 by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * OperacoesTanque generated by hbm2java
 */
public class OperacoesTanque  implements java.io.Serializable {


     private Integer id;
     private Date data;
     private double entrada;
     private double saida;
     private double quantidadeAnterior;
     private double quantidadeAtual;
     private String veiculo;
     private double valor;

    public OperacoesTanque() {
    }

    public OperacoesTanque(Date data, double entrada, double saida, double quantidadeAnterior, double quantidadeAtual, String veiculo, double valor) {
       this.data = data;
       this.entrada = entrada;
       this.saida = saida;
       this.quantidadeAnterior = quantidadeAnterior;
       this.quantidadeAtual = quantidadeAtual;
       this.veiculo = veiculo;
       this.valor = valor;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Date getData() {
        return this.data;
    }
    
    public void setData(Date data) {
        this.data = data;
    }
    public double getEntrada() {
        return this.entrada;
    }
    
    public void setEntrada(double entrada) {
        this.entrada = entrada;
    }
    public double getSaida() {
        return this.saida;
    }
    
    public void setSaida(double saida) {
        this.saida = saida;
    }
    public double getQuantidadeAnterior() {
        return this.quantidadeAnterior;
    }
    
    public void setQuantidadeAnterior(double quantidadeAnterior) {
        this.quantidadeAnterior = quantidadeAnterior;
    }
    public double getQuantidadeAtual() {
        return this.quantidadeAtual;
    }
    
    public void setQuantidadeAtual(double quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }
    public String getVeiculo() {
        return this.veiculo;
    }
    
    public void setVeiculo(String veiculo) {
        this.veiculo = veiculo;
    }
    public double getValor() {
        return this.valor;
    }
    
    public void setValor(double valor) {
        this.valor = valor;
    }




}


