package sk.stuba.fei.uim.oop.entity.grant;

import java.util.Set;

public class AgencyImplementation implements AgencyInterface {
    private String name;

    public AgencyImplementation(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void addGrant(GrantInterface gi, int year) {

    }

    @Override
    public Set<GrantInterface> getAllGrants() {
        return null;
    }

    @Override
    public Set<GrantInterface> getGrantsIssuedInYear(int year) {
        return null;
    }
}
