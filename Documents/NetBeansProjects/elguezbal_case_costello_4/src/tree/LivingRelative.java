package tree;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Alex, Derek, Mark
 */
public class LivingRelative extends FamilyMember {
    
    private String currentResidence;
    private String currentResidenceState;
    
    public LivingRelative(String name, String birthdate, List<FamilyMember> parents, List<FamilyMember> children, FamilyMember spouse, String currentResidence, String currentResidenceState) {
        super(name, birthdate, parents, children, spouse);
        
        this.currentResidence = currentResidence;
        this.currentResidenceState = currentResidenceState;
    }
    
    /**
     * Converts this living relative to a deceased one
     * 
     * @param deathDate
     * @return 
     */
    public DeceasedRelative convertLivingToDeceased(String deathDate) {
        return new DeceasedRelative(getName(), getBirthdate(), getParents(), getChildren(), getSpouse(), deathDate);
    }

    @Override
    public List<String> getParamaters() {
        return Arrays.asList(currentResidence, currentResidenceState);
    }
    
}
