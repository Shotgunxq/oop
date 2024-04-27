package sk.stuba.fei.uim.oop.entity.organization;

import sk.stuba.fei.uim.oop.entity.grant.ProjectInterface;
import sk.stuba.fei.uim.oop.utility.Constants;

public class CompanyImplementation extends OrganizationImplementation {

    private int remainingOwnResources = Constants.COMPANY_INIT_OWN_RESOURCES;

    @Override
    public int getProjectBudget(ProjectInterface pi) {
        if (projects.contains(pi)) {
            // Implement logic to get budget from grant agency for the project
            int grantBudget =  pi.getTotalBudget() /* grant agency budget for project */;

            // Check if company has enough resources to co-finance
            if (remainingOwnResources >= grantBudget) {
                remainingOwnResources -= grantBudget;
                return grantBudget + grantBudget; // Double the grant with company funding
            } else {
                int companyContribution = remainingOwnResources;
                remainingOwnResources = 0;
                return grantBudget + companyContribution;
            }
        } else {
            return 0;
        }
    }
    //TODO notification
}
