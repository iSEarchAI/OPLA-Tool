package br.ufpr.dinf.gres.oplaapi;

import jmetal4.experiments.FeatureMutationOperators;
import jmetal4.interactive.InteractiveFunction;
import learning.ClusteringAlgorithm;
import learning.Moment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptimizationDto {
    private String algorithm = "NSGAII";
    private String description = "teste";
    private Boolean mutation = true;
    private Double mutationProbability = 0.9;
    private String inputArchitecture = "/home/wmfsystem/oplatool/plas/agm/agm.uml";
    private Integer numberRuns = 1;
    private Integer populationSize = 4;
    private Integer maxEvaluations = 30;
    private Boolean crossover = false;
    private Double crossoverProbability = 0.0;
    private Boolean interactive = false;
    private Integer maxInteractions = 0;
    private Integer firstInteraction = 0;
    private Integer intervalInteraction = 0;
    private ClusteringAlgorithm clusteringAlgorithm = ClusteringAlgorithm.KMEANS;
    private Moment clusteringMoment = Moment.NONE;
    private InteractiveFunction interactiveFunction;
    private List<String> mutationOperators = Arrays.asList("featureMutation", "moveMethodMutation", "moveAttributeMutation", "moveOperationMutation", "addClassMutation", "addManagerClassMutation");
    private List<String> patterns = new ArrayList<>();
    private List<String> objectiveFunctions = Arrays.asList("featureDriven","aclass","coe");

    public OptimizationDto() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getMutation() {
        return mutation;
    }

    public void setMutation(Boolean mutation) {
        this.mutation = mutation;
    }

    public Double getMutationProbability() {
        return mutationProbability;
    }

    public void setMutationProbability(Double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    public String getInputArchitecture() {
        return inputArchitecture;
    }

    public void setInputArchitecture(String inputArchitecture) {
        this.inputArchitecture = inputArchitecture;
    }

    public Integer getNumberRuns() {
        return numberRuns;
    }

    public void setNumberRuns(Integer numberRuns) {
        this.numberRuns = numberRuns;
    }

    public Integer getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(Integer populationSize) {
        this.populationSize = populationSize;
    }

    public Integer getMaxEvaluations() {
        return maxEvaluations;
    }

    public void setMaxEvaluations(Integer maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    public Boolean getCrossover() {
        return crossover;
    }

    public void setCrossover(Boolean crossover) {
        this.crossover = crossover;
    }

    public Double getCrossoverProbability() {
        return crossoverProbability;
    }

    public void setCrossoverProbability(Double crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    public Boolean getInteractive() {
        return interactive;
    }

    public void setInteractive(Boolean interactive) {
        this.interactive = interactive;
    }

    public Integer getMaxInteractions() {
        return maxInteractions;
    }

    public void setMaxInteractions(Integer maxInteractions) {
        this.maxInteractions = maxInteractions;
    }

    public Integer getFirstInteraction() {
        return firstInteraction;
    }

    public void setFirstInteraction(Integer firstInteraction) {
        this.firstInteraction = firstInteraction;
    }

    public Integer getIntervalInteraction() {
        return intervalInteraction;
    }

    public void setIntervalInteraction(Integer intervalInteraction) {
        this.intervalInteraction = intervalInteraction;
    }

    public ClusteringAlgorithm getClusteringAlgorithm() {
        return clusteringAlgorithm;
    }

    public void setClusteringAlgorithm(ClusteringAlgorithm clusteringAlgorithm) {
        this.clusteringAlgorithm = clusteringAlgorithm;
    }

    public Moment getClusteringMoment() {
        return clusteringMoment;
    }

    public void setClusteringMoment(Moment clusteringMoment) {
        this.clusteringMoment = clusteringMoment;
    }

    public InteractiveFunction getInteractiveFunction() {
        return interactiveFunction;
    }

    public void setInteractiveFunction(InteractiveFunction interactiveFunction) {
        this.interactiveFunction = interactiveFunction;
    }

    public List<String> getMutationOperators() {
        return mutationOperators;
    }

    public void setMutationOperators(List<String> mutationOperators) {
        this.mutationOperators = mutationOperators;
    }

    public List<String> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<String> patterns) {
        this.patterns = patterns;
    }

    public List<String> getObjectiveFunctions() {
        return objectiveFunctions;
    }

    public void setObjectiveFunctions(List<String> objectiveFunctions) {
        this.objectiveFunctions = objectiveFunctions;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
