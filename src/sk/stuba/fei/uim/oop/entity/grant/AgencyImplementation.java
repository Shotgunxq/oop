package sk.stuba.fei.uim.oop.entity.grant;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AgencyImplementation implements AgencyInterface {
    private String name;
    private HashMap<Integer, Set<GrantInterface>> grantsByYear;


    public AgencyImplementation() {
        this.grantsByYear = new HashMap<>();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void addGrant(GrantInterface gi, int year) {
//        if (grants.containsKey(year)) {
//            grants.get(year).add(gi);
//        } else {
//            grants.put(year, Set.of(gi));
//        }
        Set<GrantInterface> grants = grantsByYear.get(year);
        if (grants == null) {
            grants = new HashSet<>();
            grantsByYear.put(year, grants);
        }
        grants.add(gi);
    }

    @Override
    public Set<GrantInterface> getAllGrants() {
        Set<GrantInterface> allGrants = new HashSet<>();
        for (Set<GrantInterface> grants : grantsByYear.values()) {
            allGrants.addAll(grants);
        }
        return allGrants;

    }

    @Override
    public Set<GrantInterface> getGrantsIssuedInYear(int year) {
        return grantsByYear.get(year);
    }
}
