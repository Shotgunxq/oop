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

    public GrantImplementation(String identifier, int year, AgencyInterface agency, int budget) {
        this.identifier = identifier;
        this.year = year;
        this.agency = agency;
        this.budget = budget;
        this.remainingBudget = budget;
        this.state = GrantState.NEW;
        this.projectBudgets = new HashMap<>();
        this.registeredProjects = new HashSet<>();
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
        return projectBudgets.getOrDefault(project, 0);
    }

    @Override
    public boolean registerProject(ProjectInterface project) {
        if (state == GrantState.STARTED && project.getStartingYear() == year && project.getAllParticipants().size() > 0) {
            registeredProjects.add(project);
            return true;
        }
        return false;
    }



    public boolean isOrganizationRegistered(OrganizationInterface organization) {
        // Check if the organization is present in the registered list
        return registeredOrganizations.contains(organization);
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
        if (state == GrantState.NEW) {
            state = GrantState.STARTED;
        }
    }



    @Override
    public void evaluateProjects() {
        List<ProjectInterface> eligibleProjects = new ArrayList<>();

        for (ProjectInterface project : registeredProjects) {
            if (checkCapacity(project)) {
                eligibleProjects.add(project);
            } else {
                project.setBudgetForYear(project.getStartingYear(), 0);
            }
        }

        int allocatedBudgetPerProject = remainingBudget / (eligibleProjects.isEmpty() ? registeredProjects.size() : eligibleProjects.size() / 2);

        for (ProjectInterface project : eligibleProjects.isEmpty() ? registeredProjects : eligibleProjects) {
            project.setBudgetForYear(project.getStartingYear(), allocatedBudgetPerProject);
        }

        state = GrantState.CLOSED;
    }

    private boolean checkCapacity(ProjectInterface project) {
        int maxWorkloadPerParticipant = 2;

        for (PersonInterface participant : project.getAllParticipants()) {
            if (getParticipantWorkload(participant) + project.getEndingYear() > maxWorkloadPerParticipant) {
                return false;
            }
        }
        return true;
    }

    private int getParticipantWorkload(PersonInterface participant) {
        int workload = 0;

        for (GrantInterface currentGrant : agency.getAllGrants()) {
            if (isParticipantInGrant(participant, currentGrant)) {
                workload += currentGrant.getRegisteredProjects().stream()
                        .filter(project -> project.getAllParticipants().contains(participant))
                        .mapToInt(ProjectInterface::getEndingYear)
                        .sum();
            }
        }

        return workload;
    }

    private boolean isParticipantInGrant(PersonInterface participant, GrantInterface grant) {
        return participant.getEmployers().stream().anyMatch(grant::isOrganizationRegistered);
    }









//    @Override
//    public void evaluateProjects() {
//        // Check if call is in EVALUATING state (as per description)
//        if (state != GrantState.EVALUATING) {
//            throw new IllegalStateException("Grant call is not in EVALUATING state");
//        }
//
//        // List to store projects eligible for funding allocation
//        List<ProjectInterface> eligibleProjects = new ArrayList<>();
//
//        // Loop through registered projects
//        for (ProjectInterface project : registeredProjects) {
//            boolean validCapacity = true;
//
//            // Check solver capacity needs implementation (similar to previous example)
//            // validCapacity = checkSolverCapacity(project); // Implement this method
//
//            // Add project to eligible list if capacity is valid (assuming implemented)
//            if (validCapacity) {
//                eligibleProjects.add(project);
//            }
//        }
//
//        // Budget allocation based on number of eligible projects (handle edge cases)
//        int numProjects = eligibleProjects.size();
//        int allocatedBudgetPerProject;
//        if (numProjects == 0) {
//            allocatedBudgetPerProject = 0; // No projects receive funding
//        } else if (numProjects == 1) {
//            allocatedBudgetPerProject = remainingBudget; // Single project receives entire budget
//        } else {
//            allocatedBudgetPerProject = remainingBudget / (numProjects / 2); // Budget divided equally among half of projects
//        }
//
//        // Set budget for each eligible project across its duration
//        for (ProjectInterface project : eligibleProjects) {
//            int projectDuration = project.getEndingYear();
//            for (int year = project.getStartingYear(); year < project.getStartingYear() + projectDuration; year++) {
//                project.setBudgetForYear(year, allocatedBudgetPerProject);
//            }
//        }
//
//        // Update call status to EVALUATED
//        state = GrantState.EVALUATING;
//    }


    @Override
    public void closeGrant() {
        if (state == GrantState.EVALUATING) {
            for (ProjectInterface project : registeredProjects) {
                // Check if budget exists for the project before accessing it
                if (projectBudgets.containsKey(project)) {
                    int allocatedBudget = projectBudgets.get(project);
                    int yearlyBudget = allocatedBudget / Constants.PROJECT_DURATION_IN_YEARS;
                    project.setBudgetForYear(year, yearlyBudget);
                } else {
                    // Handle projects without allocated budget (optional)
                    // You might want to log a message or set a default budget here
                    System.out.println("Project " + project + " did not receive funding.");
                }
            }
            state = GrantState.CLOSED;
        }
    }

}
