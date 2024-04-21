package sk.stuba.fei.uim.oop.entity.grant;

import sk.stuba.fei.uim.oop.entity.organization.OrganizationInterface;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;
import sk.stuba.fei.uim.oop.utility.Constants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ProjectImplementation implements ProjectInterface {

    private String projectName;
    private int startingYear;
//    private int endingYear;
    private OrganizationInterface applicant;
    private Set<PersonInterface> participants;
    private HashMap<Integer, Integer> budgetsByYear;

    public ProjectImplementation() {
        this.projectName = projectName;
        this.startingYear = startingYear;
//        this.endingYear = endingYear;
        this.applicant = applicant;
        this.participants = new HashSet<>();
        this.budgetsByYear = new HashMap<>();
    }

    @Override
    public String getProjectName() {
        return projectName;
    }

    @Override
    public void setProjectName(String name) {
        this.projectName = name;
    }

    @Override
    public int getStartingYear() {
        return startingYear;
    }

    @Override
    public void setStartingYear(int year) {
        this.startingYear = year;
    }

    @Override
    public int getEndingYear() {
        return startingYear + Constants.PROJECT_DURATION_IN_YEARS - 1;
    }

    @Override
    public int getBudgetForYear(int year) {
//        return budgetsByYear.getOrDefault(year, 0);
        // Retrieve base budget allocated by grant agency
        int baseBudget = budgetsByYear.getOrDefault(year,0);// ... logic to get base budget

        // If project is submitted by a company, add co-financing
        if (applicant instanceof OrganizationInterface) {
            OrganizationInterface company = (OrganizationInterface) applicant;
            // Call company method to retrieve co-financing for this project and year
            int companyContribution = company.getProjectBudget(this);
            baseBudget += companyContribution;
        }

        return baseBudget;

    }

    @Override
    public void setBudgetForYear(int year, int budget) {
        budgetsByYear.put(year, budget);
    }

    @Override
    public int getTotalBudget() {
        int totalBudget = 0;
        for (int budget : budgetsByYear.values()) {
            totalBudget += budget;
        }
        return totalBudget;
    }

    @Override
    public void addParticipant(PersonInterface participant) {
        if (applicant != null && applicant.getName() != null) {
            participants.add(participant);
        }
    }

    @Override
    public Set<PersonInterface> getAllParticipants() {
        return participants;

    }

    @Override
    public OrganizationInterface getApplicant() {
        return applicant;
    }

    @Override
    public void setApplicant(OrganizationInterface applicant) {
        this.applicant = applicant;
    }
}
