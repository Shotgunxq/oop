package sk.stuba.fei.uim.oop.entity.organization;

import sk.stuba.fei.uim.oop.entity.grant.ProjectInterface;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class UniversityImplementation implements OrganizationInterface {

    private String name;
    private Set<PersonInterface> employees;
    private Set<ProjectInterface> projects;

    private HashMap<PersonInterface, Integer> employmentForEmployee;

    public UniversityImplementation() {
        this.employees = new HashSet<>();
        this.projects = new HashSet<>();
        this.employmentForEmployee = new HashMap<>();
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
        // You can add logic to validate employment and store it if needed
        this.employees.add(p);
        setEmploymentForEmployee(p, employment);
    }

    @Override
    public Set<PersonInterface> getEmployees() {
        return new HashSet<>(employees); // Return an unmodifiable copy
    }

    @Override
    public int getEmploymentForEmployee(PersonInterface p) {
        return employmentForEmployee.getOrDefault(p, 0);
    }

    @Override
    public Set<ProjectInterface> getAllProjects() {
        return new HashSet<>(projects); // Return an unmodifiable copy
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
        if (!projects.contains(pi)) {
            return 0;
        }
        // Universities rely solely on grant funding
        int grantAllocation = pi.getTotalBudget(); // Get grant agency allocation (implementation detail)
        return grantAllocation;
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
        // Update project budget based on notification (implementation detail if needed)
        pi.setBudgetForYear(year, budgetForYear);
    }

    // Helper method to set employment for an employee
    public void setEmploymentForEmployee(PersonInterface employee, int employment) {
        employmentForEmployee.put(employee, employment);
    }
}
