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
    private OrganizationInterface applicant;
    private Set<PersonInterface> participants;
    private HashMap<Integer, Integer> budgetsByYear;
    private int workloadPerYear;
    private int companyCoFunding; // Added field to store company co-funding

    public ProjectImplementation() {
        this.participants = new HashSet<>();
        this.budgetsByYear = new HashMap<>();
        this.companyCoFunding = 0; // Initialize co-funding to 0
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
        return startingYear + Constants.PROJECT_DURATION_IN_YEARS;
    }

    @Override
    public int getBudgetForYear(int year) {
        // Retrieve base budget allocated by grant agency
        int baseBudget = budgetsByYear.getOrDefault(year, 0);

        // If project is submitted by a company, add co-financing
        if (applicant instanceof OrganizationInterface) {
            OrganizationInterface company = (OrganizationInterface) applicant;
            // No need to call company method here, use the stored value
            baseBudget += companyCoFunding;
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

    @Override
    public int getWorkloadPerYear() {
        return workloadPerYear;
    }

    @Override
    public int getDuration() {
        return Constants.PROJECT_DURATION_IN_YEARS;
    }

    // Possible setter for workloadPerYear if needed
    public void setWorkloadPerYear(int workloadPerYear) {
        this.workloadPerYear = workloadPerYear;
    }

    // Added method to get company co-funding
    public int getCompanyCoFunding() {
        return companyCoFunding;
    }

    // Added method to set company co-funding
    public void setCompanyCoFunding(int companyCoFunding) {
        this.companyCoFunding = companyCoFunding;
    }
}
