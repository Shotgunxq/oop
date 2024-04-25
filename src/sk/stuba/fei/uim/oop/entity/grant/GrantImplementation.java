package sk.stuba.fei.uim.oop.entity.grant;

import sk.stuba.fei.uim.oop.entity.organization.OrganizationInterface;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;
import sk.stuba.fei.uim.oop.utility.Constants;

import java.util.*;

public class GrantImplementation implements GrantInterface {

    private String identifier;
    private int year;
    private AgencyInterface agency;
    private int budget;
    private int remainingBudget;
    private GrantState state;
    private Set<ProjectInterface> registeredProjects;
    private HashMap<ProjectInterface, Integer> projectBudgets;
    private List<OrganizationInterface> registeredOrganizations;

    public GrantImplementation() { // No arguments constructor
        this.registeredProjects = new HashSet<>();
        this.projectBudgets = new HashMap<>();
        this.registeredOrganizations = new ArrayList<>();

    }


    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public AgencyInterface getAgency() {
        return agency;
    }

    @Override
    public void setAgency(AgencyInterface agency) {
        this.agency = agency;
    }

    @Override
    public int getBudget() {
        return budget;
    }

    @Override
    public void setBudget(int budget) {
        this.budget = budget;
        this.remainingBudget = budget;
    }

    @Override
    public int getRemainingBudget() {
        return remainingBudget;
    }


    @Override
    public int getBudgetForProject(ProjectInterface project) {
        // This method could be implemented based on specific evaluation criteria
        // For demonstration purposes, assume equal distribution among funded projects
        if (!registeredProjects.contains(project) || state != GrantState.CLOSED) {
            return 0;
        }
//TODO: ci ten projekt je zaregistrovany v gratne,,,,, ak v tam je tak pridat
        int fundedProjects = 0;

        for (ProjectInterface p : registeredProjects) {

            // Check if any project has a budget (doesn't matter which)
            if (p.getBudgetForYear(p.getStartingYear()) > 0) {
                fundedProjects++;
                break; // Optional: Once one funded project is found, we can break the loop
            }
        }

        if (fundedProjects == 0) {
            return 0;
        }

        int projectBudget = budget / fundedProjects;
        return projectBudget;
    }
    @Override
    public boolean registerProject(ProjectInterface project) {
        if (state != GrantState.STARTED || year != project.getStartingYear() || project.getAllParticipants().isEmpty()) {
            return false;
        }
        registeredProjects.add(project);
        return true;
    }


    @Override
    public Set<ProjectInterface> getRegisteredProjects() {
        return registeredProjects;
    }

    @Override
    public GrantState getState() {
        return state;
    }

    @Override
    public void callForProjects() {
        state = GrantState.STARTED;
    }

    @Override
    public void evaluateProjects() {
        List<ProjectInterface> eligibleProjects = new ArrayList<>();

        for (ProjectInterface project : registeredProjects) {
            if (checkCapacity(project)) { // Check solver capacity
                eligibleProjects.add(project);
            } else {
                project.setBudgetForYear(project.getStartingYear(), 0); // Set budget to 0 for ineligible projects
            }
        }

        // Calculate allocated budget per project (consider empty list scenario)
        int numEligibleProjects = eligibleProjects.size();
        int allocatedBudgetPerProject;
        if (numEligibleProjects == 0) {
            allocatedBudgetPerProject = 0; // No budget allocation if no projects are eligible
        } else if (numEligibleProjects == 1) {
            allocatedBudgetPerProject = budget; // Entire budget for the only project
        } else {
            allocatedBudgetPerProject = remainingBudget / (numEligibleProjects / 2);
        }

        // Update project budgets and notify organizations
        for (int i = 0; i < Math.min(numEligibleProjects, eligibleProjects.size()); i++) { // Handle potential list modification
            ProjectInterface project = eligibleProjects.get(i);
            int projectDuration = project.getEndingYear() - project.getStartingYear() + 1;
            int yearlyBudget = allocatedBudgetPerProject / projectDuration;

            // Set budget for each year of the project
            for (int year = project.getStartingYear(); year <= project.getEndingYear(); year++) {
                project.setBudgetForYear(year, yearlyBudget);
            }

            // Update projectBudgets HashMap with total allocated budget
            projectBudgets.put(project, allocatedBudgetPerProject);

            project.getApplicant().projectBudgetUpdateNotification(project, project.getStartingYear(), allocatedBudgetPerProject); // Total allocated budget
        }

        state = GrantState.CLOSED; // Update state after evaluation and allocation
    }



        @Override
        public void closeGrant() {
            if (state != GrantState.EVALUATING) {
                return;
            }

            remainingBudget = budget;
            for (ProjectInterface project : registeredProjects) {
                // Check research capacity (replace with your implementation)
                // In this example, assume all projects pass
                int projectBudget = getBudgetForProject(project);
                if (projectBudget > 0) {
                    remainingBudget -= projectBudget;
                    int yearlyBudget = projectBudget / Constants.PROJECT_DURATION_IN_YEARS;
                    for (int year = project.getStartingYear(); year <= project.getEndingYear(); year++) {
                        project.setBudgetForYear(year, yearlyBudget);
                    }
                }
            }

            state = GrantState.CLOSED;
        }

    private boolean checkCapacity(ProjectInterface project) {
        int maxWorkloadPerParticipant = 2; // Adjust this value based on your requirements

        for (PersonInterface participant : project.getAllParticipants()) {
            int participantWorkload = getParticipantWorkload(participant, this);
            if (participantWorkload + project.getEndingYear() > maxWorkloadPerParticipant) {
                return false; // Solver capacity exceeded
            }
        }
        return true;
    }

    private int getParticipantWorkload(PersonInterface participant, GrantInterface grant) {
        int workload = 0;

        for (ProjectInterface registeredProject : grant.getRegisteredProjects()) {
            if (registeredProject.getAllParticipants().contains(participant)) {
                workload += registeredProject.getWorkloadPerYear() * registeredProject.getDuration();  // Assuming workloadPerYear is available in the project
            }
        }

        return workload;
    }
}