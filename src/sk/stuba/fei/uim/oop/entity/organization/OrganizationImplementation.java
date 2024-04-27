package sk.stuba.fei.uim.oop.entity.organization;

import sk.stuba.fei.uim.oop.entity.grant.ProjectInterface;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public abstract class OrganizationImplementation implements OrganizationInterface {

    private String name;
    protected Set<PersonInterface> employees;
    protected Set<ProjectInterface> projects;

    protected HashMap<PersonInterface, Integer> employmentForEmployee;

    public OrganizationImplementation() {
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
        employees.add(p);
        setEmploymentForEmployee(p, employment);
    }

    @Override
    public Set<PersonInterface> getEmployees() {
        return new HashSet<>(employees); // Return a copy to avoid modification
    }



    @Override
    public Set<ProjectInterface> getAllProjects() {
        return new HashSet<>(projects); // Return a copy to avoid modification
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
    public abstract int getProjectBudget(ProjectInterface pi);

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
        // Implement logic to handle notification (may be relevant for Company)
        pi.setBudgetForYear(year, budgetForYear);
    }


    @Override
    public int getEmploymentForEmployee(PersonInterface p) {
        // Implement logic to find and return employment for specific person
//        p.getEmployers().add(this);
//        throw new UnsupportedOperationException("Not implemented in abstract class");
        return employmentForEmployee.getOrDefault(p, 0);



//        return 0;
    }


    public void setEmploymentForEmployee(PersonInterface employee, int employment) {
        employmentForEmployee.put(employee, employment);
    }
}
