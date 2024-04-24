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

    public GrantImplementation() {
        this.year= getYear();
        this.identifier= getIdentifier();
        this.agency = getAgency();
        this.budget = getBudget();
        this.remainingBudget = getBudget();
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
                project.setBudgetForYear(project.getStartingYear(), 0); // Set budget to 0 for ineligible projects
            }
        }

        // Calculate allocated budget per project (consider empty list scenario)
        int numEligibleProjects = eligibleProjects.size();
        int allocatedBudgetPerProject;
        if (numEligibleProjects == 0) {
            allocatedBudgetPerProject = 0; // No budget allocation if no projects are eligible
        } else {
            allocatedBudgetPerProject = remainingBudget / (numEligibleProjects / 2);
        }

        // Update project budgets and notify organizations
        for (ProjectInterface project : eligibleProjects) {
            int projectDuration = project.getEndingYear() - project.getStartingYear() + 1;
            int yearlyBudget = allocatedBudgetPerProject / projectDuration;

            // Set budget for each year of the project
            for (int year = project.getStartingYear(); year <= project.getEndingYear(); year++) {
                project.setBudgetForYear(year, yearlyBudget);
            }

            project.getApplicant().projectBudgetUpdateNotification(project, project.getStartingYear(), allocatedBudgetPerProject); // Total allocated budget
        }
        state = GrantState.CLOSED;
//        state = GrantState.EVALUATING;
    }


//    private boolean checkCapacity(ProjectInterface project) {
//        int maxWorkloadPerYear = 5;  // Adjust this value based on your requirements
//        int maxOverallWorkload = 10;  // Optional: Overall workload limit across all grants (default: no limit)
//
//        for (PersonInterface participant : project.getAllParticipants()) {
//            int participantWorkload = getParticipantWorkload(participant, this);
//            int projectWorkloadPerYear = project.getWorkloadPerYear();
//
//            // Check workload per year for the project
//            for (int year = project.getStartingYear(); year <= project.getEndingYear(); year++) {
//                if (participantWorkload + projectWorkloadPerYear > maxWorkloadPerYear) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
    private boolean checkCapacity(ProjectInterface project) {
        int maxWorkloadPerParticipant = 2;

        for (PersonInterface participant : project.getAllParticipants()) {
            if (getParticipantWorkload(participant, this) + project.getEndingYear() > maxWorkloadPerParticipant) {
                return false;
            }
        }
        return true;
    }

    private int getParticipantWorkload(PersonInterface participant, GrantInterface grant) {
        int workload = 0;

        for (ProjectInterface project : grant.getRegisteredProjects()) {
            if (project.getAllParticipants().contains(participant)) {
                workload += project.getWorkloadPerYear() * project.getDuration();  // Assuming workloadPerYear is available in the project
            }
        }

        return workload;
    }
//    private int getParticipantWorkload(PersonInterface participant) {
//        int workload = 0;
//
//        for (GrantInterface currentGrant : agency.getAllGrants()) {
//            if (isParticipantInGrant(participant, currentGrant)) {
//                workload += currentGrant.getRegisteredProjects().stream()
//                        .filter(project -> project.getAllParticipants().contains(participant))
//                        .mapToInt(ProjectInterface::getEndingYear)
//                        .sum();
//            }
//        }
//
//        return workload;
//    }

    private boolean isParticipantInGrant(PersonInterface participant, GrantInterface grant) {
        return participant.getEmployers().stream().anyMatch(grant::isOrganizationRegistered);
    }

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
