package sk.stuba.fei.uim.oop.entity.organization;

import sk.stuba.fei.uim.oop.entity.grant.ProjectInterface;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;

import java.util.HashSet;
import java.util.Set;

public class CompanyImplementation implements OrganizationInterface {

    private String name;
    private Set<PersonInterface> employees;
    private Set<ProjectInterface> projects;
    private int initialOwnResources; // COMPANY_INIT_OWN_RESOURCES

    public CompanyImplementation(String name, Set<PersonInterface> employees, Set<ProjectInterface> projects, int initialOwnResources) {
        this.name = name;
        this.employees = employees;
        this.projects = projects;
        this.initialOwnResources = initialOwnResources;
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
        return Set.copyOf(employees);
    }

    @Override
    public int getEmploymentForEmployee(PersonInterface p) {
        return 0;

    }

    @Override
    public Set<ProjectInterface> getAllProjects() {
        return Set.copyOf(projects);
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

    }

    @Override
    public int getProjectBudget(ProjectInterface pi) {
        if (this instanceof OrganizationInterface) {
            OrganizationInterface company = (OrganizationInterface) this;

            // Project funding needs for the year (replace with your calculation logic)
            int projectNeeds = calculateProjectNeeds(pi);

            // Available company resources considering co-funded projects
            int availableResources = Math.max(0, initialOwnResources - getBudgetSpentOnProjects());

            // Company contribution capped by project needs and available resources
            int companyContribution = Math.min(projectNeeds, availableResources);

            // Update remaining resources after co-financing
            initialOwnResources -= companyContribution;

            // Retrieve grant-allocated budget and add company contribution
            int baseBudget = pi.getBudgetForYear(java.util.Calendar.YEAR); // Assuming YEAR is the year
            return baseBudget + companyContribution;
        } else {
            // University case, return only grant-allocated budget
            return pi.getBudgetForYear(java.util.Calendar.YEAR);
        }
    }

    // Helper method to calculate project funding needs (implementation details)
    private int calculateProjectNeeds(ProjectInterface pi) {
        // ... replace with your logic to determine project funding needs for the year
        return 10000; // Placeholder value for demonstration
    }

    // Helper method to get total budget spent on co-funded projects (implementation details)
    private int getBudgetSpentOnProjects() {
        int totalSpent = 0;
        for (ProjectInterface project : projects) {
            // Logic to determine company contribution for each project and year
            totalSpent += getProjectBudget(project) - project.getBudgetForYear(java.util.Calendar.YEAR);
        }
        return totalSpent;
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
        pi.setBudgetForYear(year, budgetForYear); // Update project budget
    }}