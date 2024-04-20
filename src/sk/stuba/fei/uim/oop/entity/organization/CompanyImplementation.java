package sk.stuba.fei.uim.oop.entity.organization;

import sk.stuba.fei.uim.oop.entity.grant.ProjectInterface;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;

import java.util.HashSet;
import java.util.Set;

public class CompanyImplementation implements OrganizationInterface {

    private String name;
    private Set<PersonInterface> employees;
    private Set<ProjectInterface> projects;
    private int initialBudget;
    private int remainingBudget;

    public CompanyImplementation(String name, Set<PersonInterface> employees, Set<ProjectInterface> projects, int initialBudget, int remainingBudget) {
        this.name = name;
        this.employees = employees;
        this.projects = projects;
        this.initialBudget = initialBudget;
        this.remainingBudget = remainingBudget;
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
        if (!projects.contains(pi)) {
            return 0;
        }
        // Consider grant allocation and remaining company budget
        int grantAllocation = pi.getTotalBudget(); // Get grant agency allocation (implementation detail)
        return Math.min(grantAllocation, remainingBudget);
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
        // Update remaining budget
        remainingBudget -= budgetForYear;
    }
}
