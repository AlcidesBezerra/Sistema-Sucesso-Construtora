/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.SessionScoped;
import pojos.OperacoesEscritorio;
import pojos.Veiculos;

/**
 *
 * @author Alcid
 */
@Named(value = "userController")
@Dependent
@SessionScoped
public class userController {
    
    
    private Veiculos veiculoSelecionado = new Veiculos();
    private OperacoesEscritorio operacaoEscritorioSelecionada = new OperacoesEscritorio();
    
    
    public void selecionarVeiculo(Veiculos p){
        veiculoSelecionado = p;
    }
    
    public void selecionarOperacaoEscritorio(OperacoesEscritorio op){
        operacaoEscritorioSelecionada = op;
        System.out.println(operacaoEscritorioSelecionada.toString());
    }
    
    
    // Getters e Setters

    public Veiculos getVeiculoSelecionado() {
        return veiculoSelecionado;
    }

    public void setVeiculoSelecionado(Veiculos veiculoSelecionado) {
        this.veiculoSelecionado = veiculoSelecionado;
    }

    public OperacoesEscritorio getOperacaoEscritorioSelecionada() {
        return operacaoEscritorioSelecionada;
    }

    public void setOperacaoEscritorioSelecionada(OperacoesEscritorio operacaoEscritorioSelecionada) {
        this.operacaoEscritorioSelecionada = operacaoEscritorioSelecionada;
    }
    
    
    
    
    
    //Construtor
    public userController() {
    }
    
    

    
    
}
