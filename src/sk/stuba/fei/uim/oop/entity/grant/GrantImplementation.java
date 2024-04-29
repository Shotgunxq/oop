package sk.stuba.fei.uim.oop.entity.grant;

import sk.stuba.fei.uim.oop.entity.organization.OrganizationInterface;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;
import sk.stuba.fei.uim.oop.utility.Constants;

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

        // Calculate allocated budget per eligible project
        int totalEligibleBudget = remainingBudget; // Start with the total remaining budget
        for (ProjectInterface project : eligibleProjects) {
            int projectDuration = project.getDuration();
            int budgetForProject = totalEligibleBudget / projectDuration; // Divide equally among project years
            totalEligibleBudget -= budgetForProject * projectDuration; // Update remaining budget
            for (int year = project.getStartingYear(); year < project.getStartingYear() + projectDuration; year++) {
                project.setBudgetForYear(year, budgetForProject);
            }
        }

        state = GrantState.CLOSED; // Update state after evaluation and allocation
    }




    @Override
    public void closeGrant() {
        if (state != GrantState.EVALUATING) {
            return;
        }

        // After project evaluation (assuming closeGrant is called after evaluation)
        for (ProjectInterface project : registeredProjects) {
            // Check if project is registered (assuming registration implies approval)
            if (!registeredProjects.contains(project)) {
                continue;
            }

            // Calculate budget for each independent year
            for (int year = project.getStartingYear(); year < project.getStartingYear() + project.getDuration(); year++) {
                // Access project budget for the current year (assuming no additional input needed)
                int currentYearBudget = project.getBudgetForYear(year);

                // Assuming non-zero budget implies project approval
                if (currentYearBudget > 0) {
                    // Proceed with budget access or calculations for approved projects
                    int totalBudget = project.getTotalBudget(); // Or access specific year budgets
                    // Use project budget information for your application logic (e.g., notifying organizations)
                }
            }
        }

        state = GrantState.CLOSED;
    }

    private boolean checkCapacity(ProjectInterface project) {
        int maxWorkload = Constants.MAX_EMPLOYMENT_PER_AGENCY; // The maximum workload allowed per year

        // Get the applicant organization for the project
        OrganizationInterface applicant = project.getApplicant();

        // Get all grants issued by the agency
        Set<GrantInterface> allGrants = agency.getAllGrants();

        // Map to store the total workload of each participant within the same agency for the project
        Map<PersonInterface, Integer> totalWorkloadMap = new HashMap<>();

        // Iterate over all grants issued by the agency
        for (GrantInterface grant : allGrants) {
            // Check if the grant is relevant to the project year
            if (grant.getYear() <= project.getStartingYear()) {
                // Iterate over all registered projects in the grant
                for (ProjectInterface proj : grant.getRegisteredProjects()) {
                    // Check if the project overlaps with the proposed project's duration
                    if (proj.getStartingYear() < project.getStartingYear() + project.getDuration() &&
                            proj.getStartingYear() + proj.getDuration() > project.getStartingYear()) {
                        // Get all participants of the overlapping project
                        Set<PersonInterface> overlappingParticipants = proj.getAllParticipants();
                        // Iterate over each participant of the overlapping project
                        for (PersonInterface participant : overlappingParticipants) {
                            // Initialize total employment for the participant
                            int totalEmployment = 0;
                            // Iterate over all employers of the participant
                            for (OrganizationInterface employer : participant.getEmployers()) {
                                // Check if the employer is the applicant organization
                                if (employer.equals(applicant)) {
                                    // Update the total employment for the participant
                                    totalEmployment += employer.getEmploymentForEmployee(participant);
                                }
                            }
                            // Update the total workload for the participant
                            totalWorkloadMap.put(participant, totalWorkloadMap.getOrDefault(participant, 0) + totalEmployment);
                        }
                    }
                }
            }
        }

        // Get all participants of the current project
        Set<PersonInterface> projectParticipants = project.getAllParticipants();

        // Iterate over each participant of the current project
        for (PersonInterface participant : projectParticipants) {
            // Get the total workload for the participant
            int totalWorkloadForParticipant = totalWorkloadMap.getOrDefault(participant, 0);

            // Add the planned workload for the current project
            totalWorkloadForParticipant += (applicant.getEmploymentForEmployee(participant) / 100.0) * project.getTotalBudget() / project.getDuration();

            // Check if the total workload exceeds the limit
            if (totalWorkloadForParticipant > maxWorkload) {
                System.out.println("!!!!!!!!!!!!!!!!Workload exceeded for participant " + participant.getName());
                return false; // Workload exceeded for this participant
            }
        }

        System.out.println(project.getProjectName());
        System.out.println("Workload not exceeded for any participant!!!!!!!!" + totalWorkloadMap);
        return true; // Workload not exceeded for any participant
    }




















}