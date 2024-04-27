package sk.stuba.fei.uim.oop.entity.organization;

import sk.stuba.fei.uim.oop.entity.grant.ProjectInterface;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;
import sk.stuba.fei.uim.oop.utility.Constants;

import java.util.HashSet;
import java.util.Set;

public class CompanyImplementation implements OrganizationInterface {

    private String name;
    private Set<PersonInterface> employees;
    private Set<ProjectInterface> projects;
    private int initialOwnResources; // COMPANY_INIT_OWN_RESOURCES

    public CompanyImplementation() {
        this.employees = new HashSet<>();
        this.projects = new HashSet<>();
        this.initialOwnResources = Constants.COMPANY_INIT_OWN_RESOURCES; // Placeholder value for demonstration
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void addEmployee(PersonInterface p, int employment) {
        employees.add(p);
    }

    @Override
    public Set<PersonInterface> getEmployees() {
        return employees;
    }
    //TODO implement this method the same as in UniversityImplementation
    @Override
    public int getEmploymentForEmployee(PersonInterface p) {
         // Placeholder, implement logic to return actual employment level
        return 0;
    }

    @Override
    public Set<ProjectInterface> getAllProjects() {
        return projects;
    }

    @Override
    public Set<ProjectInterface> getRunningProjects(int year) {
        Set<ProjectInterface> runningProjects = new HashSet<>();
        for (ProjectInterface project : projects) {
            if (project.getStartingYear() <= year && project.getEndingYear() >= year) {
                runningProjects.add(project);
            }
        }
        return runningProjects;
    }

    @Override
    public void registerProjectInOrganization(ProjectInterface project) {
        projects.add(project);
        // Set budget for each year of the project
        int projectDuration = project.getDuration();
        int yearlyBudget = project.getTotalBudget() / projectDuration;

        for (int year = project.getStartingYear(); year <= project.getEndingYear(); year++) {
            project.setBudgetForYear(year, yearlyBudget);
        }
    }


    @Override
    public int getProjectBudget(ProjectInterface pi) {
        int projectNeeds = calculateProjectNeeds(pi);
        int availableResources = Math.max(0, initialOwnResources - getBudgetSpentOnProjects());
        int companyContribution = Math.min(projectNeeds, availableResources);
        initialOwnResources -= companyContribution;
        int baseBudget = pi.getBudgetForYear(java.util.Calendar.YEAR);
        return baseBudget + companyContribution;
    }

    private int calculateProjectNeeds(ProjectInterface pi) {
        // Placeholder logic for demonstration
        return 10000;
    }

    private int getBudgetSpentOnProjects() {
        int totalSpent = 0;
        for (ProjectInterface project : projects) {
            totalSpent += calculateCompanyContribution(project) - project.getBudgetForYear(java.util.Calendar.YEAR);
        }
        return totalSpent;
    }

    private int calculateCompanyContribution(ProjectInterface project) {
        // Placeholder logic for demonstration
        return 5000; // Assuming company contributes a fixed amount for each project
    }

    @Override
    public int getBudgetForAllProjects() {
        int totalBudget = 0;
        for (ProjectInterface project : projects) {
            totalBudget += getProjectBudget(project);
        }
        return totalBudget;
    }

    @Override
    public void projectBudgetUpdateNotification(ProjectInterface pi, int year, int budgetForYear) {
        pi.setBudgetForYear(year, budgetForYear);
    }
}
