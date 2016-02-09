/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperheuristics.algorithm.moeadfrrmab;

import jmetal.problems.CITO_CAITO;


/**
 *
 * @author vinicius
 */
public class MOEAD_AGR_FRRMAB extends MOEAD_GR_FRRMAB{

    protected double maxNr;
    protected String nrFunction;
    protected double gamma;
    
    public static String Sigmoid="sigmoid";
    public static String Linear="linear";
    public static String Exponential="exp";
    
    public MOEAD_AGR_FRRMAB(CITO_CAITO problem) {
        super(problem);
    }
    
    @Override
    protected void initializeParameters(){
        super.initializeParameters();
        maxNr= ((Double) this.getInputParameter("maxnr")).doubleValue();
        nrFunction=this.getInputParameter("nrfunction").toString();
        if(nrFunction.equals(MOEAD_AGR_FRRMAB.Sigmoid)){
            gamma= ((Double) this.getInputParameter("gamma")).doubleValue();
        }
    }
    
    @Override
     protected int selectNrValue(){
        if(this.nrFunction.equals(MOEAD_AGR_FRRMAB.Linear))
            return this.fnLinear();
        else if(this.nrFunction.equals(MOEAD_AGR_FRRMAB.Exponential)) 
            return this.fnExponential();
        else if(this.nrFunction.equals(MOEAD_AGR_FRRMAB.Sigmoid)) 
            return this.fnSigmoid();
        else
            return super.selectNrValue(); //do nothing
    }
    
    private int fnLinear(){
        double calc=(((double)this.generation) * this.maxNr) / ((double)this.maxgeneration);
        return (int) Math.ceil(calc);
    }
    
    
    private int fnExponential(){
        double calc= ((Math.exp(5 * ((double)this.generation)/((double)this.maxgeneration)) - 1) * this.maxNr) / (Math.exp(5) - 1);
        return (int) Math.ceil(calc);
    }
    
    private int fnSigmoid(){
        double calc= this.maxNr / (1 + Math.exp(-20 * (((double)this.generation)/((double)this.maxgeneration) - this.gamma)));
        return (int) Math.ceil(calc);
    }
}
