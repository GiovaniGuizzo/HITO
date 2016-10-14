package hyperheuristics.igd;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import jmetal.base.SolutionSet;
import jmetal.qualityIndicator.InvertedGenerationalDistance;
import jmetal.qualityIndicator.util.MetricsUtil;
import jmetal.util.NonDominatedSolutionList;

/**
 *
 * @author giovaniguizzo
 */
public class IGDHandler {

    private SolutionSet population;
    private NonDominatedSolutionList nonDominatedPopulation;
    private final MetricsUtil metricUtil;
    private final InvertedGenerationalDistance igd;

    public IGDHandler() {
        this.population = new SolutionSet();
        this.nonDominatedPopulation = new NonDominatedSolutionList();
        this.metricUtil = new MetricsUtil();
        this.igd = new InvertedGenerationalDistance();
    }

    public void addParetoFront(SolutionSet front) {
        nonDominatedPopulation.addAll(front);
        population = population.union(front);
    }

    public void addParetoFront(String path) {
        addParetoFront(metricUtil.readNonDominatedSolutionSet(path));
    }

    public void clear() {
        this.population = new SolutionSet();
    }

    public double calculateIGD(String frontPath, int numberOfObjectives) {
        return calculateIGD(metricUtil.readNonDominatedSolutionSet(frontPath), numberOfObjectives);
    }

    public double calculateIGD(SolutionSet front, int numberOfObjectives) {
        if (population.size() != 0) {
            double[] maximumValues = metricUtil.getMaximumValues(population.writeObjectivesToMatrix(), numberOfObjectives);
            double[] minimumValues = metricUtil.getMinimumValues(population.writeObjectivesToMatrix(), numberOfObjectives);
            return igd.invertedGenerationalDistance(front.writeObjectivesToMatrix(), nonDominatedPopulation.writeObjectivesToMatrix(), maximumValues, minimumValues);
        }
        return 0D;
    }

}
