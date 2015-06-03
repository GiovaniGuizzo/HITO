package hyperheuristics.hypervolume;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import jmetal.base.SolutionSet;
import jmetal.qualityIndicator.Hypervolume;
import jmetal.qualityIndicator.util.MetricsUtil;

/**
 *
 * @author giovaniguizzo
 */
public class HypervolumeHandler {

    private SolutionSet population;
    private final MetricsUtil metricUtil;
    private final Hypervolume hypervolume;

    public HypervolumeHandler() {
        this.population = new SolutionSet();
        this.metricUtil = new MetricsUtil();
        this.hypervolume = new Hypervolume();
    }

    public void addParetoFront(SolutionSet front) {
        population = population.union(front);
    }

    public void addParetoFront(String path) {
        addParetoFront(metricUtil.readNonDominatedSolutionSet(path));
    }

    public void clear() {
        this.population = new SolutionSet();
    }

    public double calculateHypervolume(String frontPath, int numberOfObjectives) {
        return calculateHypervolume(metricUtil.readNonDominatedSolutionSet(frontPath), numberOfObjectives);
    }

    public double calculateHypervolume(SolutionSet front, int numberOfObjectives) {
        if (population.size() != 0) {
            double[][] referencePoint = getReferencePoint(numberOfObjectives);
            double[] maximumValues = metricUtil.getMaximumValues(population.writeObjectivesToMatrix(), numberOfObjectives);
            double[] minimumValues = metricUtil.getMinimumValues(population.writeObjectivesToMatrix(), numberOfObjectives);
            double[][] objectives = metricUtil.getNormalizedFront(front.writeObjectivesToMatrix(), maximumValues, minimumValues);
            return hypervolume.hypervolume(objectives, referencePoint, numberOfObjectives);
        }
        return 0D;
    }

    private double[][] getReferencePoint(int numberOfObjectives) {
        double[][] referencePoint = new double[numberOfObjectives][numberOfObjectives];
        for (int i = 0; i < referencePoint.length; i++) {
            double[] objective = referencePoint[i];
            objective[i] = 1.01;
            for (int j = 0; j < objective.length; j++) {
                if (i != j) {
                    objective[j] = 0;
                }
            }
        }
        return referencePoint;
    }
}
