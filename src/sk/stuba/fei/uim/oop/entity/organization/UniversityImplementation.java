package sk.stuba.fei.uim.oop.entity.organization;

import sk.stuba.fei.uim.oop.entity.grant.ProjectInterface;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;

import java.util.HashSet;
import java.util.Set;

public class UniversityImplementation implements OrganizationInterface {

    private String name;
    private Set<PersonInterface> employees;
    private Set<ProjectInterface> projects;

    public UniversityImplementation() {
        this.name = getName();
        this.employees = getEmployees();
        this.projects = getAllProjects();
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
        employees.add(p);
    }

    @Override
    public Set<PersonInterface> getEmployees() {
        return Set.copyOf(employees); // Return an unmodifiable copy
    }

    @Override
    public int getEmploymentForEmployee(PersonInterface p) {
        // You can implement logic to retrieve employment from storage if needed
        return 0; // Placeholder, implement logic to return actual employment level
    }

    @Override
    public Set<ProjectInterface> getAllProjects() {
        return Set.copyOf(projects); // Return an unmodifiable copy
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
    }
}
