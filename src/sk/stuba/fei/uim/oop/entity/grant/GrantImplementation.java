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
        // Check if call is in EVALUATING state (as per requirement)

        // List to store projects eligible for funding allocation
        List<ProjectInterface> eligibleProjects = new ArrayList<>();

        // Loop through registered projects in registration order
        for (ProjectInterface project : registeredProjects) {
            boolean validCapacity = true;

            // Check research capacity of participants (assuming a checkCapacity method)
            validCapacity = checkCapacity(project);

            // Add project to eligible list if capacity is valid
            if (validCapacity) {
                eligibleProjects.add(project);
            } else {
                // Set funding to 0 for projects with capacity issues
                project.setBudgetForYear(project.getStartingYear(), 0);
            }
        }

        // Handle edge cases for budget allocation (at least one project funded)
        int numEligibleProjects = eligibleProjects.size();
        int allocatedBudgetPerProject;
        if (numEligibleProjects == 0) {
            // No projects eligible, distribute remaining budget equally among all (at least one gets funded)
            allocatedBudgetPerProject = remainingBudget / registeredProjects.size();
            for (ProjectInterface project : registeredProjects) {
                project.setBudgetForYear(project.getStartingYear(), allocatedBudgetPerProject);
            }
        } else {
            // Budget distributed equally among half of eligible projects
            allocatedBudgetPerProject = remainingBudget / (numEligibleProjects / 2);
            for (ProjectInterface project : eligibleProjects) {
                project.setBudgetForYear(project.getStartingYear(), allocatedBudgetPerProject);
            }
        }

        // Update grant state after evaluation (assuming a CLOSED state)
        state = GrantState.CLOSED;
    }

    // Helper method to check research capacity of project participants
    private boolean checkCapacity(ProjectInterface project) {
        // Loop through all participants of the project
        for (PersonInterface participant : project.getAllParticipants()) {
            // Get participant's workload across all their projects within this agency
            int participantWorkload = getParticipantWorkload(participant, this);

            // Define maximum workload allowed per participant per agency (replace 2 with your actual constant)
            int maxWorkloadPerParticipant = 2;

            // Check if participant's workload exceeds the limit for this project duration
            int projectDuration = project.getEndingYear();
            if (participantWorkload + projectDuration > maxWorkloadPerParticipant) {
                return false; // Capacity check fails for this project
            }
        }
        // All participants' capacity is sufficient
        return true;
    }

    private int getParticipantWorkload(PersonInterface participant, GrantInterface grant) {
        // Total workload (project duration) for the participant within the agency
        int workload = 0;

        // Loop through all grants managed by the agency
        for (GrantInterface currentGrant : agency.getAllGrants()) {
            // Check if the participant is involved in the current grant
            if (isParticipantInGrant(participant, currentGrant)) {
                // Loop through registered projects within the current grant
                for (ProjectInterface project : currentGrant.getRegisteredProjects()) {
                    // Check if the participant is involved in this specific project
                    if (isParticipantInProject(participant, project)) {
                        // Add the project duration to the workload
                        workload += project.getEndingYear();
                    }
                }
            }
        }

        return workload;
    }

    private boolean isParticipantInGrant(PersonInterface participant, GrantInterface grant) {
        // Loop through all organizations the participant is employed by
        for (OrganizationInterface organization : participant.getEmployers()) {
            // Check if the organization is registered for the given grant
            if (grant.isOrganizationRegistered(organization)) {
                return true; // Participant is involved (employed by a registered organization)
            }
        }

        // Participant is not employed by any organization registered for the grant
        return false;
    }

    private boolean isParticipantInProject(PersonInterface participant, ProjectInterface project) {
        // Check if the participant is listed as a solver in the project
        for (PersonInterface solver : project.getAllParticipants()) {
            if (participant.equals(solver)) {
                return true; // Participant is a solver in the project
            }
        }

        // Participant is not listed

        return false;
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
