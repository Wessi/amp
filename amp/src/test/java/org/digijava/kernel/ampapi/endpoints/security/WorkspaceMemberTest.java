/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.security;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.dgfoundation.amp.AllTests_amp;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.digijava.kernel.ampapi.endpoints.security.dto.WorkspaceMember;
import org.digijava.kernel.ampapi.endpoints.util.types.ListOfLongs;
import org.junit.Test;

/**
 * @author Nadejda Mandrescu
 */
public class WorkspaceMemberTest extends AmpTestCase {
    private ObjectMapper mapper;
    private Security securityEP; 
    
    public WorkspaceMemberTest() {
        super("WorkspaceMemberTest");
        this.mapper = new ObjectMapper();
        this.securityEP = new Security();
    }
    
    @Test
    public void testValidUser() throws IOException {
        String wsMemberStr = "[  {\"id\": 13,\"user-id\": 12,\"workspace-id\": 4,\"role-id\": 2  },  {\"id\": 16,\"user-id\": 12,\"workspace-id\": 6,\"role-id\": 2  }]";
        WorkspaceMember[] expected = mapper.readValue(wsMemberStr, WorkspaceMember[].class);
        List<WorkspaceMember> actual = securityEP.getWorkspaceMembers(new ListOfLongs("12"));
        assertReflectionEquals(expected, actual.toArray());
    }
    
   @Test
    public void testInvalidUserIdEmptyListNoException() {
        assertTrue(securityEP.getWorkspaceMembers(new ListOfLongs("1")).isEmpty());
    }
    
    @Test
    public void testEmptyUserIdEmptyListNoException() {
        assertTrue(securityEP.getWorkspaceMembers(new ListOfLongs("")).isEmpty());
    }
    
    @Override
    public void setUp() {
        AllTests_amp.setUp();
    }

}
