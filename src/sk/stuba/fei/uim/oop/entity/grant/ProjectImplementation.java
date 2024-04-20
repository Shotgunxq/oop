package sk.stuba.fei.uim.oop.entity.grant;

import sk.stuba.fei.uim.oop.entity.organization.OrganizationInterface;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;

import java.util.Set;

public class ProjectImplementation implements ProjectInterface {
    @Override
    public String getProjectName() {
        return null;
    }

    @Override
    public void setProjectName(String name) {

    }

    @Override
    public int getStartingYear() {
        return 0;
    }

    @Override
    public void setStartingYear(int year) {

    }

    @Override
    public int getEndingYear() {
        return 0;
    }

    @Override
    public int getBudgetForYear(int year) {
        return 0;
    }

    @Override
    public void setBudgetForYear(int year, int budget) {

    }

    @Override
    public int getTotalBudget() {
        return 0;
    }

    @Override
    public void addParticipant(PersonInterface participant) {

    }

    @Override
    public Set<PersonInterface> getAllParticipants() {
        return null;
    }

    @Override
    public OrganizationInterface getApplicant() {
        return null;
    }

    @Override
    public void setApplicant(OrganizationInterface applicant) {

    }
}
