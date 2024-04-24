package sk.stuba.fei.uim.oop.entity.grant;

import sk.stuba.fei.uim.oop.entity.organization.OrganizationInterface;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;
import sk.stuba.fei.uim.oop.utility.Constants;

import java.util.*;

public class GrantImplementation implements GrantInterface {

    private String identifier;
    private int year;
    private AgencyInterface agency;
    private int budget;
    private int remainingBudget;
    private GrantState state;
    private Set<ProjectInterface> registeredProjects;
    private HashMap<ProjectInterface, Integer> projectBudgets;
    private List<OrganizationInterface> registeredOrganizations;

    public GrantImplementation() { // No arguments constructor
        this.registeredProjects = new HashSet<>();
        this.projectBudgets = new HashMap<>();
        this.registeredOrganizations = new ArrayList<>();
    }


    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public AgencyInterface getAgency() {
        return agency;
    }

    @Override
    public void setAgency(AgencyInterface agency) {
        this.agency = agency;
    }

    @Override
    public int getBudget() {
        return budget;
    }

    @Override
    public void setBudget(int budget) {
        this.budget = budget;
        this.remainingBudget = budget;
    }

    @Override
    public int getRemainingBudget() {
        return remainingBudget;
    }

    @Override
    public int getBudgetForProject(ProjectInterface project) {
        return projectBudgets.getOrDefault(project, 0);
    }

    @Override
    public boolean registerProject(ProjectInterface project) {
        if (state == GrantState.STARTED && project.getStartingYear() == year && project.getAllParticipants().size() > 0) {
            registeredProjects.add(project);
//            registeredOrganizations.addAll(project.getApplicant().getOrganizations()); // Register applicant's organizations
            return true;
        }
        return false;
    }

    @Override
    public boolean isOrganizationRegistered(OrganizationInterface organization) {
        return registeredOrganizations.contains(organization);
    }

    @Override
    public Set<ProjectInterface> getRegisteredProjects() {
        return Set.copyOf(registeredProjects);
    }

    @Override
    public GrantState getState() {
        return state;
    }

    @Override
    public void callForProjects() {
        state = GrantState.STARTED;
    }

    @Override
    public void evaluateProjects() {
        List<ProjectInterface> eligibleProjects = new ArrayList<>();
    }

    @Override
    public void closeGrant(){
        state = GrantState.CLOSED;
    }

}