package pojos;
// Generated 15/06/2017 12:56:05 by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * OperacoesLimoeiro generated by hbm2java
 */
public class OperacoesLimoeiro  implements java.io.Serializable {


     private Integer id;
     private String veiculo;
     private Date data;
     private double combustivel;
     private double horimetroAnterior;
     private double horimetroAtual;
     private double horimetroPercorrido;
     private double consumo;
     private String nota;
     private double valorPendente;
     private int isPendente;

    public OperacoesLimoeiro() {
    }

    public OperacoesLimoeiro(String veiculo, Date data, double combustivel, double horimetroAnterior, double horimetroAtual, double horimetroPercorrido, double consumo, String nota, double valorPendente, int isPendente) {
       this.veiculo = veiculo;
       this.data = data;
       this.combustivel = combustivel;
       this.horimetroAnterior = horimetroAnterior;
       this.horimetroAtual = horimetroAtual;
       this.horimetroPercorrido = horimetroPercorrido;
       this.consumo = consumo;
       this.nota = nota;
       this.valorPendente = valorPendente;
       this.isPendente = isPendente;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getVeiculo() {
        return this.veiculo;
    }
    
    public void setVeiculo(String veiculo) {
        this.veiculo = veiculo;
    }
    public Date getData() {
        return this.data;
    }
    
    public void setData(Date data) {
        this.data = data;
    }
    public double getCombustivel() {
        return this.combustivel;
    }
    
    public void setCombustivel(double combustivel) {
        this.combustivel = combustivel;
    }
    public double getHorimetroAnterior() {
        return this.horimetroAnterior;
    }
    
    public void setHorimetroAnterior(double horimetroAnterior) {
        this.horimetroAnterior = horimetroAnterior;
    }
    public double getHorimetroAtual() {
        return this.horimetroAtual;
    }
    
    public void setHorimetroAtual(double horimetroAtual) {
        this.horimetroAtual = horimetroAtual;
    }
    public double getHorimetroPercorrido() {
        return this.horimetroPercorrido;
    }
    
    public void setHorimetroPercorrido(double horimetroPercorrido) {
        this.horimetroPercorrido = horimetroPercorrido;
    }
    public double getConsumo() {
        return this.consumo;
    }
    
    public void setConsumo(double consumo) {
        this.consumo = consumo;
    }
    public String getNota() {
        return this.nota;
    }
    
    public void setNota(String nota) {
        this.nota = nota;
    }
    public double getValorPendente() {
        return this.valorPendente;
    }
    
    public void setValorPendente(double valorPendente) {
        this.valorPendente = valorPendente;
    }
    public int getIsPendente() {
        return this.isPendente;
    }
    
    public void setIsPendente(int isPendente) {
        this.isPendente = isPendente;
    }




}


