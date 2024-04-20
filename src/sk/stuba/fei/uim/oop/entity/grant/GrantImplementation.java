package sk.stuba.fei.uim.oop.entity.grant;

import java.util.Set;

public class GrantImplementation implements GrantInterface {
    @Override
    public String getIdentifier() {
        return null;
    }

    @Override
    public void setIdentifier(String identifier) {

    }

    @Override
    public int getYear() {
        return 0;
    }

    @Override
    public void setYear(int year) {

    }

    @Override
    public AgencyInterface getAgency() {
        return null;
    }

    @Override
    public void setAgency(AgencyInterface agency) {

    }

    @Override
    public int getBudget() {
        return 0;
    }

    @Override
    public void setBudget(int budget) {

    }

    @Override
    public int getRemainingBudget() {
        return 0;
    }

    @Override
    public int getBudgetForProject(ProjectInterface project) {
        return 0;
    }

    @Override
    public boolean registerProject(ProjectInterface project) {
        return false;
    }

    @Override
    public Set<ProjectInterface> getRegisteredProjects() {
        return null;
    }

    @Override
    public GrantState getState() {
        return null;
    }

    @Override
    public void callForProjects() {

    }

    @Override
    public void evaluateProjects() {

    }

    @Override
    public void closeGrant() {

    }
}
