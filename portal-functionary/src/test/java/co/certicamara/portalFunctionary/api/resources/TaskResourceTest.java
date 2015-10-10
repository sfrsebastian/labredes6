package co.certicamara.portalFunctionary.api.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import fj.data.Either;
import gp.e3.autheo.client.dtos.TokenDTO;
import io.dropwizard.testing.junit.ResourceTestRule;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import co.certicamara.portalFunctionary.util.providers.TokenProviderTest;
import co.certicamara.portalfunctionary.api.representations.TaskDTO;
import co.certicamara.portalfunctionary.api.resources.TaskResource;
import co.certicamara.portalfunctionary.domain.business.TaskBusiness;
import co.certicamara.portalfunctionary.infrastructure.exceptions.IException;
import co.certicamara.portalfunctionary.infrastructure.exceptions.TechnicalException;

import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.sun.jersey.api.client.ClientResponse;

public class TaskResourceTest {

    private final TaskBusiness userBusinessMock = Mockito.mock(TaskBusiness.class);
    private final TaskResource userResource = new TaskResource(userBusinessMock);

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder().addResource(userResource).addProvider(TokenProviderTest.class).build();
    
    

    @Before
    public void setUp() {
        
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testGetUserTasks_OK() {

        String username = "certisolucion";


        List<TaskDTO> expectedTasksList = new ArrayList<TaskDTO>();
        expectedTasksList.add(new TaskDTO("1", "1", "1", "1", "1", Instant.now(), Instant.now(),"1", "1", "category", null, null, null, null));
        expectedTasksList.add(new TaskDTO("2", "2", "2", "2", "2", Instant.now(), Instant.now(),"2", "1", "category", null, null, null, null));
        expectedTasksList.add(new TaskDTO("3", "3", "3", "3", "3", Instant.now(), Instant.now(),"3", "1", "category", null, null, null, null));

        int listSize = expectedTasksList.size();

        Either<IException, List<TaskDTO>> expectedEither = Either.right(expectedTasksList);
        Mockito.when(userBusinessMock.getUserTasks(Mockito.anyString(),Mockito.any(TokenDTO.class))).thenReturn(expectedEither);

        resources.getObjectMapper().registerModule(new JSR310Module());

        ClientResponse clientResponse = resources.client().resource("/task?username=" + username  ).get(ClientResponse.class);

        assertNotNull(clientResponse);
        assertEquals(listSize, expectedTasksList.size());
        assertEquals(expectedTasksList.size(), clientResponse.getEntity(TaskDTO[].class).length);
    }

    @Test
    public void testGetUserTasks_NOK_emptyList() {

        String username = "certisolucion";

        int listSize = 0;
        List<TaskDTO> expectedTasksList = new ArrayList<TaskDTO>();
        Either<IException, List<TaskDTO>> expectedEither = Either.right(expectedTasksList);
        Mockito.when(userBusinessMock.getUserTasks(Mockito.anyString(), Mockito.any(TokenDTO.class))).thenReturn(expectedEither);

        ClientResponse clientResponse = resources.client().resource("/task?username=" + username ).get(ClientResponse.class);

        assertNotNull(clientResponse);
        assertEquals(listSize, expectedTasksList.size());
        assertEquals(expectedTasksList.size(), clientResponse.getEntity(TaskDTO[].class).length);
    }

    @Test
    public void testGetUserTasks_NOK_TechnicalException() {

        String username = "certisolucion";

        Either<IException, List<TaskDTO>> expectedEither = Either.left(new TechnicalException("technical exception"));
        Mockito.when(userBusinessMock.getUserTasks(Mockito.anyString(), Mockito.any(TokenDTO.class))).thenReturn(expectedEither);

        ClientResponse clientResponse = resources.client().resource("/task?username=" + username ).get(ClientResponse.class);

        assertNotNull(clientResponse);
        assertEquals(500, clientResponse.getStatus());
    }
}