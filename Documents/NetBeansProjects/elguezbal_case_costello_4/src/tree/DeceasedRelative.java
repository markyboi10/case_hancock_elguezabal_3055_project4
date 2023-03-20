package tree;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Alex, Derek Mark
 */
public class DeceasedRelative extends FamilyMember {
    
    private String deathDate;
    
    public DeceasedRelative(String name, String birthdate, List<FamilyMember> parents, List<FamilyMember> children, FamilyMember spouse, String deathDate) {
        super(name, birthdate, parents, children, spouse);
        
        this.deathDate = deathDate;
    }

    @Override
    public List<String> getParamaters() {
        return Arrays.asList(deathDate);
    }
    
}
