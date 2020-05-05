package br.ufpr.dinf.gres.architecture.representation;

import br.ufpr.dinf.gres.architecture.exceptions.AttributeNotFoundException;
import br.ufpr.dinf.gres.architecture.helpers.UtilResources;
import br.ufpr.dinf.gres.architecture.representation.relationship.DependencyRelationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.RealizationRelationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.RelationshiopCommons;
import br.ufpr.dinf.gres.architecture.representation.relationship.Relationship;
import br.ufpr.dinf.gres.architecture.touml.Types;
import br.ufpr.dinf.gres.architecture.touml.VisibilityKind;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class Interface extends Element {


    private static final long serialVersionUID = -1779316062511432020L;

    static Logger LOGGER = LogManager.getLogger(Interface.class.getName());
    private final Set<Method> methods = new HashSet<Method>();
    private final Set<Attribute> attributes = new HashSet<Attribute>();
    private RelationshipsHolder relationshipHolder;
    private PatternsOperations patternsOperations;


    public Interface(RelationshipsHolder relationshipHolder, String name, Variant variantType, String namespace, String id) {
        super(name, variantType, "interface", namespace, id);
        setRelationshipHolder(relationshipHolder);
    }

    /**
     * Use este construtor quando você deseja criar uma interface.<br /><br />
     * <p>
     * OBS 1: O ID para esta interface será gerado automaticamente.<br/>
     * OBS 2: Esse construtor NAO adicionar a interface na br.ufpr.dinf.gres.arquitetura<br/>
     *
     * @param architecture       Architecture em questão
     * @param name               - Nome da interface
     * @param relationshipHolder
     */
    public Interface(RelationshipsHolder relationshipHolder, String name, Package packagee) {
        this(relationshipHolder, name, null, UtilResources.createNamespace(ArchitectureHolder.getName(), packagee.getName()), UtilResources.getRandonUUID());
        this.setPatternOperations(new PatternsOperations());
    }

    public Interface(RelationshipsHolder relationshipHolder, String name) {
        this(relationshipHolder, name, null, UtilResources.createNamespace(ArchitectureHolder.getName(), name), UtilResources.getRandonUUID());
        this.setPatternOperations(new PatternsOperations());
    }

    public Interface(RelationshipsHolder relationshipHolder, String name, String id) {
        this(relationshipHolder, name, null, UtilResources.createNamespace(ArchitectureHolder.getName(), name), id);
        this.setPatternOperations(new PatternsOperations());
    }

    /**
     * Use este construtor quando você deseja criar uma interface usando algum ID passado por você<br /><br />
     * <p>
     * OBS 1: Esse construtor NAO adicionar a interface na br.ufpr.dinf.gres.arquitetura<br/>
     *
     * @param name - Nome da interface
     * @param id   -  ID para a interface
     */
    public Interface(RelationshipsHolder relationshipHolder, String name, String id, Package packagee) {
        this(relationshipHolder, name, null, UtilResources.createNamespace(ArchitectureHolder.getName(), packagee.getName()), id);
        this.setPatternOperations(new PatternsOperations());
    }


    public Attribute createAttribute(String name, Types.Type type, VisibilityKind visibility) {
        String id = UtilResources.getRandonUUID();
        Attribute a = new Attribute(name, visibility.toString(), type.getName(), ArchitectureHolder.getName() + "::"
                + this.getName(), id);
        addExternalAttribute(a);
        return a;
    }

    public void setAttribute(Attribute attr) {
        this.attributes.add(attr);
    }

    public Set<Attribute> getAllAttributes() {
        if (attributes.isEmpty())
            return Collections.emptySet();
        return Collections.unmodifiableSet(attributes);
    }

    public Attribute findAttributeByName(String name) throws AttributeNotFoundException {
        String message = "atributo '" + name + "' não encontrado na classe '" + this.getName() + "'.\n";

        for (Attribute att : getAllAttributes())
            if (name.equalsIgnoreCase(att.getName()))
                return att;
        LOGGER.info(message);
        throw new AttributeNotFoundException(message);
    }

    public boolean moveAttributeToInterface(Attribute attribute, Class destinationKlass) {
        if (!destinationKlass.addExternalAttribute(attribute))
            return false;

        if (!removeAttribute(attribute)) {
            destinationKlass.removeAttribute(attribute);
            return false;
        }
        attribute.setNamespace(ArchitectureHolder.getName() + "::" + destinationKlass.getName());
        LOGGER.info("Moveu atributo: " + attribute.getName() + " de " + this.getName() + " para "
                + destinationKlass.getName());
        return true;
    }

    public boolean addExternalAttribute(Attribute a) {
        if (this.attributes.add(a)) {
            LOGGER.info("Atributo: " + a.getName() + " adicionado na classe: " + this.getName());
            return true;
        } else {
            LOGGER.info("TENTOU remover o Atributo: " + a.getName() + " da classe: " + this.getName()
                    + " porém não consegiu");
            return false;
        }
    }

    public boolean removeAttribute(Attribute attribute) {
        if (this.attributes.remove(attribute)) {
            LOGGER.info("Atributo: " + attribute.getName() + " removido da classe: " + this.getName());
            return true;
        } else {
            LOGGER.info("TENTOU remover o atributo: " + attribute.getName() + " classe: " + this.getName()
                    + " porém não consegiu");
            return false;
        }
    }


    public Set<Method> getMethods() {
        return Collections.unmodifiableSet(methods);
    }

    public boolean removeOperation(Method operation) {
        if (methods.remove(operation)) {
            LOGGER.info("Removeu operação '" + operation + "', da interface: " + this.getName());
            return true;
        } else {
            LOGGER.info("TENTOU removeu operação '" + operation + "', da interface: " + this.getName() + " porém não conseguiu");
            return false;
        }
    }

    public Method createOperation(String operationName) throws Exception {
        Method operation = new Method(operationName, false, null, "void", false, null, "", ""); //Receber id
        methods.add(operation);
        return operation;
    }

    public boolean moveOperationToInterface(Method operation, Interface interfaceToMove) {
        if (!interfaceToMove.addExternalOperation(operation))
            return false;

        if (!removeOperation(operation)) {
            interfaceToMove.removeOperation(operation);
            return false;
        }
        operation.setNamespace(ArchitectureHolder.getName() + "::" + interfaceToMove.getName());
        LOGGER.info("Moveu operação: " + operation.getName() + " de " + this.getName() + " para " + interfaceToMove.getName());
        return true;

    }


    public boolean addExternalOperation(Method operation) {
        if (methods.add(operation)) {
            LOGGER.info("Operação " + operation.getName() + " adicionado na interface " + this.getName());
            return true;
        } else {
            LOGGER.info("TENTOU remover a operação: " + operation.getName() + " da interface: " + this.getName() + " porém não consegiu");
            return false;
        }

    }


    public Set<Element> getImplementors() {
        Set<Element> implementors = new HashSet<Element>();

        Set<Relationship> relations = getRelationshipHolder().getRelationships();

        for (Relationship relationship : relations) {

            if (relationship instanceof RealizationRelationship) {
                RealizationRelationship realization = (RealizationRelationship) relationship;
                if (realization.getSupplier().equals(this))
                    implementors.add(realization.getClient());
            }

        }

        return Collections.unmodifiableSet(implementors);
    }

//	public Set<Element> getRealImplementors() {
//		Set<Element> implementors = new HashSet<Element>();
//		
//		for(Package p : getArchitecture().getAllPackages()){
//			for(RealizationRelationship r : getArchitecture().getAllRealizations()){
//				if(r.getClient().equals(p)){
//					implementors.add(p);
//				}
//			}
//		}
//					
//		return Collections.unmodifiableSet(implementors);
//	}

    public Set<Element> getDependents() {
        Set<Element> dependents = new HashSet<Element>();

        Set<Relationship> relations = getRelationshipHolder().getRelationships();

        for (Relationship relationship : relations) {
            if (relationship instanceof DependencyRelationship) {
                DependencyRelationship dependency = (DependencyRelationship) relationship;
                if (dependency.getSupplier().equals(this)) {
                    dependents.add(dependency.getClient());
                }
            }
        }
        return Collections.unmodifiableSet(dependents);
    }

    @Override
    public Set<Concern> getAllConcerns() {
        Set<Concern> concerns = new HashSet<Concern>(getOwnConcerns());
        for (Method operation : getMethods())
            concerns.addAll(operation.getAllConcerns());
        concerns.addAll(this.getOwnConcerns());

        return Collections.unmodifiableSet(concerns);
    }

    public List<DependencyRelationship> getDependencies() {
        List<DependencyRelationship> dependencies = new ArrayList<DependencyRelationship>();

        for (DependencyRelationship dependency : getRelationshipHolder().getAllDependencies()) {
            if (dependency.getSupplier().equals(this))
                dependencies.add(dependency);
        }

        return dependencies;
    }

    public boolean addExternalMethod(Method method) {
        if (methods.add(method)) {
            LOGGER.info("Operação " + method.getName() + " adicionado na interface " + this.getName());
            return true;
        } else {
            LOGGER.info("TENTOU remover a operação: " + method.getName() + " da interface: " + this.getName() + " porém não consegiu");
            return false;
        }
    }

    /**
     * Procura os elementos (Pacotes e Classes) que
     * implementam a interface em questão ou que a requerem a interface em questão e a remove da lista de interfaces implementadas
     * ou requeridas
     *
     * @param interfacee
     */
    public void removeInterfaceFromRequiredOrImplemented() {
        for (Iterator<Relationship> i = getRelationshipHolder().getRelationships().iterator(); i.hasNext(); ) {
            Relationship r = i.next();

            if (r instanceof RealizationRelationship) {
                RealizationRelationship realization = (RealizationRelationship) r;
                if (realization.getSupplier().equals(this)) {
                    if (realization.getClient() instanceof Package) {
                        ((Package) realization.getClient()).removeImplementedInterface(this);
                    }
                    if (realization.getClient() instanceof Class) {
                        ((Class) realization.getClient()).removeImplementedInterface(this);
                    }

                }
            }

            if (r instanceof DependencyRelationship) {
                DependencyRelationship dependency = (DependencyRelationship) r;
                if (dependency.getSupplier().equals(this)) {
                    if (dependency.getClient() instanceof Package) {
                        ((Package) dependency.getClient()).removeRequiredInterface(this);
                    }
                    if (dependency.getClient() instanceof Class) {
                        ((Class) dependency.getClient()).removeRequiredInterface(this);
                    }

                }
            }
        }
    }

    public RelationshipsHolder getRelationshipHolder() {
        return relationshipHolder;
    }

    public void setRelationshipHolder(RelationshipsHolder relationshipHolder) {
        this.relationshipHolder = relationshipHolder;
    }

    public Set<Relationship> getRelationships() {
        return Collections.unmodifiableSet(RelationshiopCommons.getRelationships(relationshipHolder.getRelationships(), this));
    }

    public void setPatternOperations(PatternsOperations patternOperations) {
        this.patternsOperations = patternOperations;
    }

    public PatternsOperations getPatternsOperations() {
        return this.patternsOperations;
    }

    public List<RealizationRelationship> getRealizationImplementors() {
        List<RealizationRelationship> realization = new ArrayList<RealizationRelationship>();

        for (RealizationRelationship r : getRelationshipHolder().getAllRealizations()) {
            if (r.getSupplier().equals(this))
                realization.add(r);
        }

        return realization;
    }

    //Modificado Thais

    public void copyDependencyRelationship(Interface source, Interface targetInterface, Concern concern) {

        RelationshipsHolder newRelation = new RelationshipsHolder();
        newRelation.setRelationships(targetInterface.getRelationships());

        for (DependencyRelationship d : source.getDependencies()) {
            boolean existe = false;
            for (DependencyRelationship dAlvo : targetInterface.getDependencies()) {

                if (d.getSupplier().equals(dAlvo.getSupplier())) {
                    existe = true;
                }
            }
            if (!existe) {
                if (d.getSupplier().containsConcern(concern)) {
                    DependencyRelationship newDependence = new DependencyRelationship(d.getSupplier(), targetInterface, d.getName());
                    newRelation.addRelationship(newDependence);
                }
            }

        }
        targetInterface.setRelationshipHolder(newRelation);
    }

    //Modificado Thais

    public void copyRealizationRelationship(Interface source, Interface targetInterface, Concern concern) {

        RelationshipsHolder newRelation2 = new RelationshipsHolder();
        newRelation2.setRelationships(targetInterface.getRelationships());

        for (RealizationRelationship r : source.getRealizationImplementors()) {
            boolean existe = false;
            for (RealizationRelationship cAlvo : targetInterface.getRealizationImplementors()) {

                if (r.getSupplier().equals(cAlvo.getSupplier())) {
                    existe = true;
                }
            }
            if (!existe) {
                if (r.getSupplier().containsConcern(concern)) {
                    RealizationRelationship newImplementor = new RealizationRelationship(r.getSupplier(), targetInterface, r.getName(), r.getId());
                    newRelation2.addRelationship(newImplementor);
                }
            }

            targetInterface.setRelationshipHolder(newRelation2);

        }

    }

    //Modificado Thais

    public void copyAllDependenciesSuppliers(Interface source, Interface targetInterface) {

        RelationshipsHolder newRelation = new RelationshipsHolder();
        newRelation.setRelationships(targetInterface.getRelationships());

        for (DependencyRelationship d : source.getDependencies()) {
            boolean existe = false;
            for (DependencyRelationship dAlvo : targetInterface.getDependencies()) {

                if (d.getSupplier().equals(dAlvo.getSupplier())) {
                    existe = true;
                }
            }
            if (!existe) {
                DependencyRelationship newDependence = new DependencyRelationship(d.getSupplier(), targetInterface, d.getName());
                newRelation.addRelationship(newDependence);
            }

        }
        targetInterface.setRelationshipHolder(newRelation);
    }

}
