package sk.stuba.fei.uim.oop.entity.grant;

import sk.stuba.fei.uim.oop.entity.organization.OrganizationInterface;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;

import java.util.*;
import java.util.stream.Collectors;

public class GrantImplementation implements GrantInterface {

    private String identifier;
    private int year;
    private AgencyInterface agency;
    private int budget;
    private int remainingBudget;
    //TODO: aby sa raz tam pridalo a aby poradie sa zanechalo SET HASHSET NEICO EXISST
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
    //TODO cely tento bolo prepisane podla konzultacie lebo ja uz mam ten projekt ktory dostal peniaze a nemusim
    // pre iterovat cez vsetky projekty iba ten jeden projekt a jeho roky ci nieco podobne
    public int getBudgetForProject(ProjectInterface project) {
        // This method could be implemented based on specific evaluation criteria
        // For demonstration purposes, assume equal distribution among funded projects
        if (!registeredProjects.contains(project)) {
            return 0;
        }
        //TODO: ci ten projekt je zaregistrovany v gratne,,,,, ak v tam je tak pridat
        //TODO roky projektu iterovat
        int fundedProjects = 0;

        for (int y = project.getStartingYear(); y <= project.getEndingYear(); y++) {
            // Check if any project has a budget (doesn't matter which)
            if (y > 0) { //TODO always true
                //TODO: kolko ma ten projekt  nie plus plus a po prvom iteracii sa breakne
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
    int allocatedBudgetPerProject;

    @Override
    public void evaluateProjects() {
        List<ProjectInterface> eligibleProjects = new ArrayList<>();

        for (ProjectInterface project : registeredProjects) {
            boolean isEligible = checkCapacity(project); // Check solver capacity

            // Set project budget based on eligibility
            if (isEligible) {
                eligibleProjects.add(project);
            } else {
                project.setBudgetForYear(project.getStartingYear(), 0); // Set budget to 0 for ineligible projects
            }
        }

        // Calculate allocated budget per eligible project (consider empty list scenario)
        int numEligibleProjects = eligibleProjects.size();
        if (numEligibleProjects == 0) {
            allocatedBudgetPerProject = 0; // No budget allocation if no projects are eligible
        } else {
            allocatedBudgetPerProject = remainingBudget / numEligibleProjects;
        }

        // Update budgets and notify organizations (only for eligible projects)
        for (ProjectInterface project : eligibleProjects) {
            // Calculate yearly budget based on project duration

            int projectDuration = project.getEndingYear() - project.getStartingYear() + 1;
            int yearlyBudget = allocatedBudgetPerProject / projectDuration;

            // Set budget for each year of the project
            for (int year = project.getStartingYear(); year < project.getStartingYear() + projectDuration; year++) {
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

        // After project evaluation (assuming closeGrant is called after evaluation)
        for (ProjectInterface project : registeredProjects) {
            // Check if project is registered (assuming registration implies approval)
            if (!registeredProjects.contains(project)) {
                continue;
            }

            // Access project budget for the current year (assuming no additional input needed)
            int currentYearBudget = project.getBudgetForYear(year);

            // Assuming non-zero budget implies project approval
            if (currentYearBudget > 0) {
                // Proceed with budget access or calculations for approved projects
                int totalBudget = project.getTotalBudget(); // Or access specific year budgets
                // Use project budget information for your application logic (e.g., notifying organizations)
            }
        }

        state = GrantState.CLOSED;
    }

    private boolean checkCapacity(ProjectInterface project) {
        int maxWorkload = 5; // The workload cannot exceed 5

        for (PersonInterface participant : project.getAllParticipants()) {
            int participantWorkload = getParticipantWorkload(participant, this);

            // Check if participant's workload + project workload exceeds the limit
            if (participantWorkload + (participantWorkload / 100.0) * project.getWorkloadPerYear() * project.getDuration() > maxWorkload) {
                return false; // Solver capacity exceeded
            }
        }
        return true;
    }



    private int getParticipantWorkload(PersonInterface participant, GrantInterface grant) {
        int totalWorkload = 0;
        Set<OrganizationInterface> employers = participant.getEmployers();

        // Loop through each employer
        for (OrganizationInterface employer : employers) {
            int employerWorkload = 0;

            // Get projects registered in the grant year (filter by agency)
            Set<ProjectInterface> registeredProjects = employer.getRunningProjects(grant.getYear()).stream()
                    .filter(p -> p.getApplicant() == grant.getAgency()) // Filter by agency
                    .collect(Collectors.toSet());

            // Loop through registered projects of the employer
            for (ProjectInterface project : registeredProjects) {
                // Check if participant is involved in the project
                if (project.getAllParticipants().contains(participant)) {
                    // Get employment level for the person at this employer
                    int employment = employer.getEmploymentForEmployee(participant);

                    // Calculate workload based on employment and project workload
                    employerWorkload += (employment / 100.0) * project.getWorkloadPerYear() * project.getDuration();
                }
            }

            totalWorkload += employerWorkload;
        }

        return totalWorkload;
    }


}