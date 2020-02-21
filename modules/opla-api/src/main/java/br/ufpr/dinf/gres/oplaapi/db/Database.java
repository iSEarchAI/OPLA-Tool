package br.ufpr.dinf.gres.oplaapi.db;

import br.ufpr.dinf.gres.loglog.Level;
import br.ufpr.dinf.gres.loglog.Logger;
import br.ufpr.dinf.gres.oplaapi.util.UserHome;
import exceptions.MissingConfigurationException;
import metrics.*;
import results.Execution;
import results.Experiment;
import utils.MathUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Map.Entry;

public class Database {
    private static Connection connection = null;
    private static List<Experiment> content;

    public static List<Experiment> getContent() {
        return content;
    }

    public static void setContent(List<Experiment> all) {
        content = all;
    }

    public static Collection<Execution> getAllExecutionsByExperimentId(String experimentId) {
        for (Experiment exp : content) {
            if (exp.getId().equals(experimentId.replaceAll("\\s+", ""))) {
                return exp.getExecutions();
            }
        }

        return Collections.emptyList();
    }

    public static Map<String, String> getObjectivesBySolutionId(String solutionId, String experimentId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM objectives WHERE solution_name LIKE '%");
        query.append(solutionId);
        query.append("'");

        String ordenedObjectives[] = Database.getOrdenedObjectives(experimentId).split(" ");

        try {
            try (Statement statement = database.Database.getConnection().createStatement()) {
                ResultSet r = statement.executeQuery(query.toString());
                String objectives[] = r.getString("objectives").split("\\|");
                HashMap<String, String> map = new HashMap<>();

                for (int i = 0; i < objectives.length; i++) {
                    map.put(ordenedObjectives[i], objectives[i]);
                }

                statement.close();
                return map;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger().putLog(ex.getMessage(), Level.ERROR);
        }

        return Collections.EMPTY_MAP;

    }

    public static Map<String, String> getAllObjectivesByExecution(String idExecution, String experimentId) {
        Map<String, String> funs = new HashMap<>();

        try {
            try (Statement statement = database.Database.getConnection().createStatement()) {
                StringBuilder query = new StringBuilder();

                query.append("SELECT * FROM objectives where execution_id = ");
                query.append(idExecution);
                query.append(" OR experiement_id= ");
                query.append(experimentId);

                ResultSet r = statement.executeQuery(query.toString());
                while (r.next()) {
                    funs.put(r.getString("id"), r.getString("objectives"));
                }

                statement.close();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger().putLog(ex.getMessage(), Level.ERROR);
        }

        return funs;
    }

    /**
     * @param experimentId
     * @return elegance, conventional, PLAExtensibility, featureDriven
     * @throws Exception
     */
    public static String getOrdenedObjectives(String experimentId) {
        Statement statement = null;
        try {
            statement = database.Database.getConnection().createStatement();

            StringBuilder query = new StringBuilder();
            query.append("SELECT names FROM map_objectives_names WHERE experiment_id=");
            query.append(experimentId);

            ResultSet r = statement.executeQuery(query.toString());
            return r.getString("names");

        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger().putLog(ex.getMessage(), Level.ERROR);
        } finally {
            try {
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Logger.getLogger().putLog(ex.getMessage(), Level.ERROR);
            }
        }

        return "";

    }

    public static String getAlgoritmUsedToExperimentId(String id) {
        Statement statement = null;
        try {
            statement = database.Database.getConnection().createStatement();

            StringBuilder query = new StringBuilder();
            query.append("SELECT algorithm, description FROM experiments WHERE id=");
            query.append(id);

            ResultSet r = statement.executeQuery(query.toString());
            String description = r.getString("description");
            if ("null".equals(description)) {
                return r.getString("algorithm");
            }
            return r.getString("algorithm");

        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger().putLog(ex.getMessage(), Level.ERROR);
        } finally {
            try {
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Logger.getLogger().putLog(ex.getMessage(), Level.ERROR);
            }
        }

        return "";
    }

    public static String getPlaUsedToExperimentId(String id) {
        Statement statement = null;
        try {
            statement = database.Database.getConnection().createStatement();

            StringBuilder query = new StringBuilder();
            query.append("SELECT name FROM experiments WHERE id=");
            query.append(id);

            ResultSet r = statement.executeQuery(query.toString());
            return r.getString("name");

        } catch (Exception ex) {
            Logger.getLogger().putLog(ex.getMessage(), Level.ERROR);
        } finally {
            try {
                statement.close();
            } catch (SQLException ex) {
                Logger.getLogger().putLog(ex.getMessage(), Level.ERROR);
            }
        }

        return "";
    }

    public static void reloadContent() {
        try {
            content = Experiment.all();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger().putLog(ex.getMessage(), Level.ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger().putLog(ex.getMessage(), Level.ERROR);
        }
    }

    public static PLAExtensibility getPlaExtMetricsForSolution(String idSolution, String experimentId) {
        for (Experiment exp : content) {
            if (exp.getId().equals(experimentId)) {
                for (Execution exec : exp.getExecutions()) {
                    for (PLAExtensibility plaExt : exec.getAllMetrics().getPlaExtensibility()) {
                        if (plaExt.getIdSolution().equals(idSolution)) {
                            return plaExt;
                        }
                    }
                }
            }
        }

        return null;

    }

    public static Elegance getEleganceMetricsForSolution(String idSolution, String experimentId) {
        for (Experiment exp : content) {
            if (exp.getId().equals(experimentId)) {
                for (Execution exec : exp.getExecutions()) {
                    for (Elegance elegance : exec.getAllMetrics().getElegance()) {
                        if (elegance.getIdSolution().equals(idSolution)) {
                            return elegance;
                        }
                    }
                }
            }
        }

        return null;
    }

    public static Conventional getConventionalsMetricsForSolution(String idSolution, String experimentId) {
        for (Experiment exp : content) {
            if (exp.getId().equals(experimentId)) {
                for (Execution exec : exp.getExecutions()) {
                    for (Conventional con : exec.getAllMetrics().getConventional()) {
                        if (con.getIdSolution().equals(idSolution)) {
                            return con;
                        }
                    }
                }
            }
        }

        return null;
    }

    public static FeatureDriven getFeatureDrivenMetricsForSolution(String idSolution, String experimentId) {
        for (Experiment exp : content) {
            if (exp.getId().equals(experimentId)) {
                for (Execution exec : exp.getExecutions()) {
                    for (FeatureDriven f : exec.getAllMetrics().getFeatureDriven()) {
                        if (f.getIdSolution().equals(idSolution)) {
                            return f;
                        }
                    }
                }
            }
        }

        return null;
    }

    public static List<Metrics> getAllEleganceMetricsForExperimentId(String experimentId) {
        List<Metrics> listFd = new ArrayList<>();
        for (Experiment exp : content) {
            if (exp.getId().equals(experimentId)) {
                for (Execution exec : exp.getExecutions()) {
                    for (Elegance m : exec.getAllMetrics().getElegance()) {
                        if (m.getIsAll() == 1) {
                            listFd.add(m);
                        }
                    }
                    return listFd;
                }
            }

        }

        return listFd;
    }

    public static List<Metrics> getAllFeatureDrivenMetricsForExperimentId(String experimentId) {
        List<Metrics> listFd = new ArrayList<>();
        for (Experiment exp : content) {
            if (exp.getId().equals(experimentId)) {
                for (Execution exec : exp.getExecutions()) {
                    for (FeatureDriven m : exec.getAllMetrics().getFeatureDriven()) {
                        if (m.getIsAll() == 1) {
                            listFd.add(m);
                        }
                    }
                    return listFd;
                }
            }
        }

        return listFd;
    }

    public static List<Metrics> getAllConventionalMetricsForExperimentId(String experimentId) {
        List<Metrics> listCons = new ArrayList<>();
        for (Experiment exp : content) {
            if (exp.getId().equals(experimentId)) {
                for (Execution exec : exp.getExecutions()) {
                    for (Conventional m : exec.getAllMetrics().getConventional()) {
                        if (m.getIsAll() == 1) {
                            listCons.add(m);
                        }
                    }
                    return listCons;
                }
            }

        }

        return listCons;
    }

    public static List<Metrics> getAllPLAExtMetricsForExperimentId(String experimentId) {
        List<Metrics> listCons = new ArrayList<>();
        for (Experiment exp : content) {
            if (exp.getId().equals(experimentId)) {
                for (Execution exec : exp.getExecutions()) {
                    for (PLAExtensibility m : exec.getAllMetrics().getPlaExtensibility()) {
                        if (m.getIsAll() == 1) {
                            listCons.add(m);
                        }
                    }
                    return listCons;
                }
            }

        }

        return listCons;
    }

    //addYni

    public static Wocsclass getWocsclassMetricsForSolution(String idSolution, String experimentId) {
        for (Experiment exp : content) {
            if (exp.getId().equals(experimentId)) {
                for (Execution exec : exp.getExecutions()) {
                    for (Wocsclass wocsc : exec.getAllMetrics().getWocsclass()) {
                        if (wocsc.getIdSolution().equals(idSolution)) {
                            return wocsc;
                        }
                    }
                }
            }
        }

        return null;

    }

    public static Wocsinterface getWocsinterfaceMetricsForSolution(String idSolution, String experimentId) {
        for (Experiment exp : content) {
            if (exp.getId().equals(experimentId)) {
                for (Execution exec : exp.getExecutions()) {
                    for (Wocsinterface wocsi : exec.getAllMetrics().getWocsinterface()) {
                        if (wocsi.getIdSolution().equals(idSolution)) {
                            return wocsi;
                        }
                    }
                }
            }
        }

        return null;

    }

    public static Cbcs getCbcsMetricsForSolution(String idSolution, String experimentId) {
        for (Experiment exp : content) {
            if (exp.getId().equals(experimentId)) {
                for (Execution exec : exp.getExecutions()) {
                    for (Cbcs cbcs : exec.getAllMetrics().getCbcs()) {
                        if (cbcs.getIdSolution().equals(idSolution)) {
                            return cbcs;
                        }
                    }
                }
            }
        }

        return null;

    }

    public static Svc getSvcMetricsForSolution(String idSolution, String experimentId) {
        for (Experiment exp : content) {
            if (exp.getId().equals(experimentId)) {
                for (Execution exec : exp.getExecutions()) {
                    for (Svc svc : exec.getAllMetrics().getSvc()) {
                        if (svc.getIdSolution().equals(idSolution)) {
                            return svc;
                        }
                    }
                }
            }
        }

        return null;

    }

    public static Ssc getSscMetricsForSolution(String idSolution, String experimentId) {
        for (Experiment exp : content) {
            if (exp.getId().equals(experimentId)) {
                for (Execution exec : exp.getExecutions()) {
                    for (Ssc ssc : exec.getAllMetrics().getSsc()) {
                        if (ssc.getIdSolution().equals(idSolution)) {
                            return ssc;
                        }
                    }
                }
            }
        }

        return null;

    }

    public static Av getAvMetricsForSolution(String idSolution, String experimentId) {
        for (Experiment exp : content) {
            if (exp.getId().equals(experimentId)) {
                for (Execution exec : exp.getExecutions()) {
                    for (Av av : exec.getAllMetrics().getAv()) {
                        if (av.getIdSolution().equals(idSolution)) {
                            return av;
                        }
                    }
                }
            }
        }

        return null;

    }

    //addYni
    //addYni

    public static List<Metrics> getAllWocsCMetricsForExperimentId(String experimentId) {
        List<Metrics> listFd = new ArrayList<>();
        for (Experiment exp : content) {
            if (exp.getId().equals(experimentId)) {
                for (Execution exec : exp.getExecutions()) {
                    for (Wocsclass m : exec.getAllMetrics().getWocsclass()) {
                        if (m.getIsAll() == 1) {
                            listFd.add(m);
                        }
                    }
                    return listFd;
                }
            }

        }

        return listFd;
    }

    public static List<Metrics> getAllWocsIMetricsForExperimentId(String experimentId) {
        List<Metrics> listFd = new ArrayList<>();
        for (Experiment exp : content) {
            if (exp.getId().equals(experimentId)) {
                for (Execution exec : exp.getExecutions()) {
                    for (Wocsinterface m : exec.getAllMetrics().getWocsinterface()) {
                        if (m.getIsAll() == 1) {
                            listFd.add(m);
                        }
                    }
                    return listFd;
                }
            }

        }

        return listFd;
    }

    public static List<Metrics> getAllCbcsMetricsForExperimentId(String experimentId) {
        List<Metrics> listFd = new ArrayList<>();
        for (Experiment exp : content) {
            if (exp.getId().equals(experimentId)) {
                for (Execution exec : exp.getExecutions()) {
                    for (Cbcs m : exec.getAllMetrics().getCbcs()) {
                        if (m.getIsAll() == 1) {
                            listFd.add(m);
                        }
                    }
                    return listFd;
                }
            }

        }

        return listFd;
    }

    public static List<Metrics> getAllSvcMetricsForExperimentId(String experimentId) {
        List<Metrics> listFd = new ArrayList<>();
        for (Experiment exp : content) {
            if (exp.getId().equals(experimentId)) {
                for (Execution exec : exp.getExecutions()) {
                    for (Svc m : exec.getAllMetrics().getSvc()) {
                        if (m.getIsAll() == 1) {
                            listFd.add(m);
                        }
                    }
                    return listFd;
                }
            }

        }

        return listFd;
    }

    public static List<Metrics> getAllSscMetricsForExperimentId(String experimentId) {
        List<Metrics> listFd = new ArrayList<>();
        for (Experiment exp : content) {
            if (exp.getId().equals(experimentId)) {
                for (Execution exec : exp.getExecutions()) {
                    for (Ssc m : exec.getAllMetrics().getSsc()) {
                        if (m.getIsAll() == 1) {
                            listFd.add(m);
                        }
                    }
                    return listFd;
                }
            }

        }

        return listFd;
    }

    public static List<Metrics> getAllAvMetricsForExperimentId(String experimentId) {
        List<Metrics> listFd = new ArrayList<>();
        for (Experiment exp : content) {
            if (exp.getId().equals(experimentId)) {
                for (Execution exec : exp.getExecutions()) {
                    for (Av m : exec.getAllMetrics().getAv()) {
                        if (m.getIsAll() == 1) {
                            listFd.add(m);
                        }
                    }
                    return listFd;
                }
            }

        }

        return listFd;
    }

    //addYni

    public static int getNumberOfFunctionForExperimentId(String experimentId) throws Exception {
        Statement statement = null;
        try {
            statement = database.Database.getConnection().createStatement();

            StringBuilder query = new StringBuilder();
            query.append("SELECT names FROM map_objectives_names WHERE experiment_id=");
            query.append(experimentId.trim());

            ResultSet r = statement.executeQuery(query.toString());
            return r.getString("names").split(" ").length;

        } catch (SQLException | MissingConfigurationException | ClassNotFoundException ex) {
            Logger.getLogger().putLog(ex.getMessage(), Level.ERROR);
        } finally {
            try {
                statement.close();
            } catch (SQLException ex) {
                Logger.getLogger().putLog(ex.getMessage(), Level.ERROR);
            }
        }

        return 0;
    }

    /**
     * Retorna o número de soluções não dominadas dado um experimentID
     *
     * @param experimentId
     * @return number of non dominated solutions
     */
    public static int countNumberNonDominatedSolutins(String experimentId) {
        Statement statement = null;
        try {
            statement = getConnection().createStatement();

            StringBuilder query = new StringBuilder();
            query.append("SELECT count(*) FROM objectives where experiement_id=");
            query.append(experimentId.trim());
            query.append(" AND execution_id=''");

            ResultSet r = statement.executeQuery(query.toString());
            return Integer.parseInt(r.getString("count(*)"));

        } catch (SQLException | NullPointerException ex) {
            Logger.getLogger().putLog(ex.getMessage(), Level.ERROR);
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException ex) {
                Logger.getLogger().putLog(ex.getCause().toString(), Level.ERROR);
            }
        }

        return 0;

    }

    /**
     * Retorna uma lista contendo o nome de todas as soluções dado um
     * experimentId e um executionID
     *
     * @param experimentId
     * @param executionId
     * @return
     */
    public static List<String> getAllSolutionsForExecution(String experimentId, String executionId) {
        List<String> solutionsNames = new ArrayList<>();

        Statement statement = null;
        try {
            // statement = database.Database.getConnection().createStatement();

            statement = getConnection().createStatement();

            StringBuilder query = new StringBuilder();
            query.append("SELECT solution_name FROM objectives where experiement_id=");
            query.append(experimentId.trim());
            query.append(" AND execution_id=");
            query.append(executionId);
            query.append(" OR experiement_id=");
            query.append(experimentId.trim());
            query.append(" AND execution_id=''");

            ResultSet r = statement.executeQuery(query.toString());
            while (r.next()) {
                solutionsNames.add(r.getString("solution_name"));
            }

        } catch (SQLException ex) {
            Logger.getLogger().putLog(ex.getMessage(), Level.ERROR);
        }

        return solutionsNames;

    }

    public static String generateFileName(String id) {
        String algorithmName = Database.getAlgoritmUsedToExperimentId(id);
        String plaName = Database.getPlaUsedToExperimentId(id);

        StringBuilder fileName = new StringBuilder();
        fileName.append(id);
        fileName.append("_");
        fileName.append(plaName);
        fileName.append("_");
        fileName.append(algorithmName);
        fileName.append(".txt");

        return fileName.toString();
    }

    /**
     * Esse método é usado apenas para o cálculo do hypervolume. Ele faz duas
     * coisas. :(
     * <p>
     * 1) Retorna (fileToContent) um Map<String, List<Double>>, sendo que
     * **key** = path para o arquivo em disco contendo os valores referentes ao
     * **value**.
     * <p>
     * 2) Cria os arquivos (citados em 1) no disco. Estes arquivos são
     * utilizados apenas para a chamada do script em C que de fato calcula o
     * hypervolume. Estes arquivos são deletados (método deleteGeneratedFiles()
     * em HypervolumeGenerateObjsData) após a execução e obtenção dos
     * resultados.
     */
    public static Map<String, List<Double>> getAllObjectivesForDominatedSolutions(String... exeprimentIds)
            throws Exception {
        Statement statement = null;
        Statement statementExecutions = null;
        List<String> idsExecutions = new ArrayList<>();
        HashMap<String, List<List<Double>>> listObjectivesValues = new HashMap<>();

        // Usado temporariamente. Após cálculos estes arquivos serão apagados.
        String pathToSaveFiles = UserHome.getOplaUserHome();

        Map<String, List<Double>> fileToContent = new HashMap<>();
        List<Double> values = new ArrayList<>();

        for (String exeprimentId : exeprimentIds) {
            String nameFile = (pathToSaveFiles + Database.generateFileName(exeprimentId)).replaceAll("\\s+", "");

            String[] objectives = Database.getOrdenedObjectives(exeprimentId).split(" ");

            statementExecutions = database.Database.getConnection().createStatement();
            statement = database.Database.getConnection().createStatement();

            StringBuilder executionsQuery = new StringBuilder();
            executionsQuery.append("select id from executions where experiement_id=").append(exeprimentId);

            ResultSet executionsSet = statementExecutions.executeQuery(executionsQuery.toString());

            while (executionsSet.next()) {
                idsExecutions.add(executionsSet.getString("id"));
            }

            for (String idExecuton : idsExecutions) {

                StringBuilder query = new StringBuilder();

                query.append("SELECT objectives FROM objectives where experiement_id=").append(exeprimentId)
                        .append(" AND execution_id=").append(idExecuton);

                ResultSet r = statement.executeQuery(query.toString());

                while (r.next()) {

                    String objs = r.getString("objectives").trim().replace("|", " ");
                    String[] ov = objs.split(" ");

                    for (int i = 0; i < objectives.length; i++) {

//                        if (!VolatileConfs.hypervolumeNormalized()) TODO Opção de normalizar
//                            values.add(Double.parseDouble(ov[i]));

                        if (listObjectivesValues.get(objectives[i] + "_" + idExecuton) == null) {
                            List<List<Double>> allValue = new ArrayList<>();
                            List<Double> valueFuc = new ArrayList<>();
                            valueFuc.add(Double.parseDouble(ov[i]));
                            allValue.add(valueFuc);
                            listObjectivesValues.put(objectives[i] + "_" + idExecuton, allValue);
                        } else {
                            List<Double> last = listObjectivesValues.get(objectives[i] + "_" + idExecuton)
                                    .get(listObjectivesValues.get(objectives[i] + "_" + idExecuton).size() - 1);
                            last.add(Double.parseDouble(ov[i]));
                        }


                    }

                }
            }
//            if (VolatileConfs.hypervolumeNormalized()) TODO OPção normalizar
            MathUtils.normalize(listObjectivesValues, objectives);

            FileWriter fw = new FileWriter(nameFile);
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                for (int i = 0; i < idsExecutions.size(); i++) {
                    HashMap<String, List<Double>> c = getFunctionsValueByRun(idsExecutions.get(i), listObjectivesValues,
                            objectives);
                    mergeValues(c, bw);
                }
            }

            fileToContent.put(nameFile, values);
        }

        return fileToContent;

    }

    public static List<List<Double>> getAllObjectivesForNonDominatedSolutions(String experimentId, int[] columns) {

        Statement statement = null;

        try {
            statement = database.Database.getConnection().createStatement();

            List<List<Double>> values = new ArrayList<>();

            StringBuilder query = new StringBuilder();
            query.append("SELECT objectives FROM objectives where experiement_id=").append(experimentId)
                    .append(" AND execution_id=''");

            ResultSet result = statement.executeQuery(query.toString());
            while (result.next()) {
                String objs = result.getString("objectives").trim().replace("|", " ");
                String[] ov = objs.split(" ");
                List<Double> objectiveValue = new ArrayList<>();

                // for (int i = 0; i < columns.length; i++) {
                objectiveValue.add(Double.parseDouble(ov[columns[0]].trim()));
                objectiveValue.add(Double.parseDouble(ov[columns[1]].trim()));
                // }

                values.add(objectiveValue);
            }

            return values;

        } catch (Exception ex) {
            Logger.getLogger().putLog(ex.getMessage(), Level.ERROR);
        } finally {
            try {
                statement.close();
            } catch (SQLException ex) {
                Logger.getLogger().putLog(ex.getMessage(), Level.ERROR);
            }
        }

        return Collections.emptyList();

    }

    private static HashMap<String, List<Double>> getFunctionsValueByRun(String run,
                                                                        HashMap<String, List<List<Double>>> listObjectivesValues, String[] objectives) {
        HashMap<String, List<Double>> content = new HashMap<>();

        for (int i = 0; i < objectives.length; i++) {
            for (Entry<String, List<List<Double>>> entry : listObjectivesValues.entrySet()) {
                String string = entry.getKey();
                List<List<Double>> list = entry.getValue();

                if (string.equalsIgnoreCase(objectives[i] + "_" + run)) {
                    if (content.get(objectives[i]) == null) {
                        content.put(objectives[i], list.get(0));
                    } else {
                        content.get(objectives[i]).addAll(list.get(0));
                    }

                }
            }
        }
        return content;
    }

    private static void mergeValues(HashMap<String, List<Double>> c, BufferedWriter bw) {
        Map<String, List<Double>> cClone = (HashMap<String, List<Double>>) c.clone();

        int numberSolutions = cClone.entrySet().iterator().next().getValue().size();
        try {
            for (int i = 0; i < numberSolutions; i++) {

                String line = "";
                for (Entry<String, List<Double>> entry : c.entrySet()) {
                    List<Double> list = entry.getValue();
                    line += list.get(i) + " ";
                }
                bw.append(line + "\n");

            }
            bw.append("\n");
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Database.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

    }

    private static Connection getConnection() {
        try {
            if (connection == null)
                connection = DriverManager.getConnection("jdbc:sqlite:" + UserHome.getPathToDb());

        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(Database.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        return connection;
    }

    static String getNameSolutionById(String idSolution) {
        Statement statement = null;

        try {
            statement = getConnection().createStatement();

            StringBuilder query = new StringBuilder();
            query.append("SELECT solution_name FROM objectives where id=").append(idSolution);

            ResultSet result = statement.executeQuery(query.toString());
            return result.getString("solution_name");

        } catch (SQLException ex) {
            Logger.getLogger().putLog(ex.getMessage(), Level.ERROR);
        } finally {
            try {
                statement.close();
            } catch (SQLException ex) {
                Logger.getLogger().putLog(ex.getMessage(), Level.ERROR);
            }
        }

        return "-";
    }
}